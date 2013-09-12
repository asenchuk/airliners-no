/* http://airliners.net gallery API */

var airliners = require('./airliners');

var PHOTO_CACHE_COLLECTION_NAME = 'photos';
var PHOTO_CACHE_EXPIRATION = 24 * 60 * 60 * 1000;
var TOP15_CACHE_EXPIRATION = 6 * 60 * 60 * 1000;

function perform(req, res, f) {
    var handler = function(error, result) {
        if(error) {
            res.send((error >= 400) ? error : 500, result);
        } else {
            res.json(result);
        }
    }

    var args = Array.prototype.slice.call(arguments, 3);
    args.push(handler);

    airliners[f].apply(airliners, args);
}

exports.search = function(req, res) {
    perform(req, res, 'search', req.apiParams);
};

exports.top15 = function() {
    var lastUpdateTimestamp = 0;
    var lastResult;

    return function(req, res) {
        if(lastUpdateTimestamp >= new Date().getTime() - TOP15_CACHE_EXPIRATION) {
            res.send(lastResult);
        } else {
            airliners.loadTop15(function(err, result) {
                if(err) {
                    res.send((error >= 400) ? error : 500, result);
                } else {
                    res.json(result);

                    lastResult = result;
                    lastUpdateTimestamp = new Date().getTime();
                }
            });
        }
    }
}();

exports.top = function(req, res) {
    perform(req, res, 'loadTop', req.apiParams.limit, req.apiParams.page);
};

exports.photo = function(req, res) {
    var id = req.params.id;
    req.mongo.collection(PHOTO_CACHE_COLLECTION_NAME, function(err, coll) {
        coll.find({
            _id: id,
            timestamp: {'$gte': new Date().getTime() - PHOTO_CACHE_EXPIRATION}
        }).nextObject(function(err, entry) {
            if(entry) {
                res.json(entry.photo);
            } else {
                airliners.loadPhotoById(id, function(error, result) {
                    if(error) {
                        res.send((error >= 400) ? error : 500, result);
                    } else {
                        res.json(result);

                        var cacheObj = {
                            _id: result.id,
                            timestamp: new Date().getTime(),
                            photo: result
                        }

                        coll.save(cacheObj, function(err, doc) {
                            // noop
                        });
                    }
                });
            }
        });
    });
};

exports.default = function(req, res) {
    res.send(404);
};
