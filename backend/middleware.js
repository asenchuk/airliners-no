/* http://airliners.net gallery API */

exports.apiParams = function(name) {
    var _name = name || "apiParams";
    return function(req, res, next) {
        req[_name] = (req.get('content-type') == 'application/json' && !!req.body) ? req.body : req.query;
        next();
    }
}
