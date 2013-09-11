/* http://airliners.net gallery API */

var airliners = require('./airliners');

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
    perform(req, res, "search", req.apiParams);
};

exports.top15 = function(req, res) {
    perform(req, res, "loadTop15");
};

exports.top = function(req, res) {
    perform(req, res, "loadTop", req.apiParams.limit, req.apiParams.page);
};

exports.photo = function(req, res) {
    perform(req, res, "loadPhotoById", req.params.id);
};

exports.default = function(req, res) {
    res.send(404);
};
