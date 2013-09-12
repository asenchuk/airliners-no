/* http://airliners.net gallery API */

var controllers = require('./controllers');
var middleware = require('./middleware');
var mongo = require('./mongo');
var express = require('express');
var http = require('http');

var app = express();

/* env configuration */
var port = process.env.VMC_APP_PORT || 1337

if(process.env.VCAP_SERVICES) {
    var env = JSON.parse(process.env.VCAP_SERVICES);
    var mongoConfig = env['mongodb-1.8'][0]['credentials'];
} else {
    var mongoConfig = {
        hostname: "localhost",
        port: 27017,
        db: "airliners"
    }
}

/* express configuration */
app.use(express.compress());
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(middleware.apiParams());
app.use(mongo.init(mongoConfig));
app.use(app.router);

app.use(function(error, req, res, next) {
console.log(error);
    res.send(500);
});

/* routes */
app.get('/search', controllers.search);
app.post('/search', controllers.search);

app.get('/top15', controllers.top15);
app.post('/top15', controllers.top15);

app.get('/top', controllers.top);
app.post('/top', controllers.top);

app.get('/photo/:id([0-9]+)', controllers.photo);

app.all('*', controllers.default);

/* start up */
http.createServer(app).listen(port);
