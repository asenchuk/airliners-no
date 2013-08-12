/* http://airliners.net galery API */

var airliners = require('./airliners');
var config = require('./config');
var http = require('http');
var url = require('url');
var router = require('router')();

function perform(req, res, f) {
    var handler = function(error, result) {
        if(error) {
            res.writeHead((error >= 400) ? error : 503);
            res.end(desc);
        } else {
            res.writeHead(200);
            res.end(JSON.stringify(result));
        }
    }
    
    var args = Array.prototype.slice.call(arguments, 3);
    args.push(handler);
    
    airliners[f].apply(airliners, args);
}

router.get('/search', function(req, res) {
    var query = url.parse(req.url, true).query;
    perform(req, res, "search", query);
});

router.get('/top15', function(req, res) {
    perform(req, res, "loadTop15");
});

router.get('/top', function(req, res) {
    var query = url.parse(req.url, true).query;
    perform(req, res, "loadTop", query.limit, query.page);
});

router.get('/photo/{id}([0-9]+)', function(req, res) {
    perform(req, res, "loadPhotoById", req.params.id);
});

http.createServer(router).listen(config.port);
