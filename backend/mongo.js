/* http://airliners.net gallery API */

var createConnectionUrl = function(config) {
    var credentials = (config.username && config.password) ? config.username + ':' + config.password + '@' : '';
    var url = "mongodb://" + credentials + config.hostname + ":" + config.port + "/" + config.db;
    return url;
}

exports.init = function(config) {
    var client;

    require('mongodb').connect(createConnectionUrl(config), function(err, _client) {
        if(err) {
            throw err;
        } else {
            client = _client;
        }
    });

    return function(req, res, next) {
        req.mongo = client;
        next();
    }
}
