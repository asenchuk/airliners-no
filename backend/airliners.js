/* http://airliners.net galery access node.js lib */

var util = require('util');
var http = require('http');
var https = require('https');
var cheerio = require('cheerio');
var urlParser = require('url');
var querystring = require('querystring');

var AIRLINERS_PHOTO_URL = 'http://www.airliners.net/photo/%s/L/';
var AIRLINERS_PHOTO_SEARCH_URL = 'http://www.airliners.net/search/photo.search';
var AIRLINERS_TOP15_URL = 'http://www.airliners.net/search/photo.search?topchoice';
var AIRLINERS_TOP_URL = 'http://www.airliners.net/search/photo.search?choicehistory';

/* Load photo info by id */
exports.loadPhotoById = function(id, callback) {
    var url = util.format(AIRLINERS_PHOTO_URL, id);
    
    loadPageByUrl(url, function(error, res) {
        if(error) {
            callback(error, res);
            return;
        }
        
        var $ = cheerio.load(res);
        
        // check photo
        var photoEl = $('#bigphoto');
        if(photoEl.length == 0) {
            callback(404, 'page doesn\'t contain a photo');
            return;
        }
        
        var result = {};
        result.id = id;
        
        // prev/next
        var idRegexp = /\/(\d+)\/L\/$/;
        result.prev = $('.mpPrev').attr('href').match(idRegexp)[1];
        result.next = $('.mpNext').attr('href').match(idRegexp)[1];
        
        // image url
        result.image = photoEl.attr('src');
        
        // aircraft & airline
        var aircraftInfo = $('a[href*=airlinesearch]').parent().text().split('More: ');
        result.airline = aircraftInfo[1];
        result.aircraft = aircraftInfo[2];
        
        // taken at & on
        var takenAtInfo = $('a[href*=placesearch]').parent().text().split('More: ');
        result.takenAt = takenAtInfo[1];
        result.takenOn = takenAtInfo[2];
        
        // reg
        var reg = $('a[href*=regsearch]').text();
        result.reg = (reg.length > 0) ? reg : undefined;
        
        // cn
        var cn = $('a[href*=cnsearch]').text();
        result.cn = (cn.length > 0) ? cn : undefined;
        
        // code
        var code = $('a[href*=codesearch]').text();
        result.code = (code.length > 0) ? code : undefined;
        
        // remark
        var remarkEl = $('a[href*=regsearch]').parent().clone();
        $(remarkEl).find('*').remove();
        var remarkMatch = remarkEl.text().trim().match(/(\/\s*)?(\(cn \))?(.*)$/);
        var remark = remarkMatch[3].trim();
        result.remark = (remark.length > 0) ? remark : undefined;
        
        // author
        result.author = $('a[href^=\\/message\\/]').text().replace('Contact ', '');
        
        // position
        $('span').each(function() {
            var match = $(this).text().match(/^(\d+) of (\d+)$/);
            if(match) {
                result.pos = parseInt(match[1]);
                result.count = parseInt(match[2]);
                return false;
            } else {
                return true;
            }
        });
        
        callback(null, result);
    });
}

exports.maxSearhPageLimit = 120;
exports.defaultSearchPageLimit = 15;
exports.searchAttrs = {
    aircraft: 'aircraftsearch',
    airline: 'airlinesearch',
    place: 'placesearch',
    country: 'countrysearch',
    remark: 'adv_remark',
    reg: 'regsearch',
    cn: 'cnsearch',
    code: 'codesearch',
    date: 'datesearch',
    year: 'yearsearch',
    limit: 'page_limit',
    page: 'page'
};

exports.search = function(attrs, callback) {
    // default query attrs
    var query = {
        sort_order: 'photo_id desc',
        page_limit: exports.defaultSearchPageLimit,
        advanced_search: 'true',
        engine_version: '6.0'
    };
    
    for(var key in attrs) {
        var searchKey = exports.searchAttrs[key];
        if(searchKey) {
            query[searchKey] = attrs[key];
        }
    }
    
    query.page_limit = Math.min(query.page_limit, exports.maxSearhPageLimit);
    
    var url = AIRLINERS_PHOTO_SEARCH_URL + '?' + querystring.stringify(query);
    handleSearchPage(url, callback);
}

exports.loadTop15 = function(callback) {
    handleSearchPage(AIRLINERS_TOP15_URL, callback);
}

exports.loadTop = function(limit, page, callback) {
    var query = {};
    
    if(limit) {
        query[exports.searchAttrs.limit] = limit;
    }
    
    if(page) {
        query[exports.searchAttrs.page] = page;
    }
    
    var additionalQuery = querystring.stringify(query);
    var url = AIRLINERS_TOP_URL + ((additionalQuery.length > 0) ? ('&' + additionalQuery) : '');
    handleSearchPage(url, callback);
}

function handleSearchPage(url, callback) {
    loadPageByUrl(url, function(error, res) {
        if(error) {
            callback(error, res);
            return;
        }
        
        if(res.indexOf('Sorry, No Hits.') != -1) {
            callback(null, {count: 0});
            return;
        }
        
        var result = {};
        
        var match = res.match(/Displaying photos (\d+(,\d+)?) to (\d+(,\d+)?) from a total of (\d+(,\d+)?)/);
        result.from = parseInt(match[1].replace(',', ''));
        result.to = parseInt(match[3].replace(',', ''));
        result.total = parseInt(match[5].replace(',', ''));
        
        var items = [];
        
        $ = cheerio.load(res, {
            lowerCaseTags: true
        });
        
        if(!$) {
            callback(503, 'temporarily unavailable');
            return;
        }
        
        $('table[hspace=4][border=0][cellpadding=0][width=620]').each(function() {
            var item = {};
            
            // id
            item.id = $(this).find('center:contains(\'Photo ID:\')').text().replace('Photo ID: ', '');
            
            // thumb
            var img = $(this).find('img[alt=\'Click here for full size photo!\']');
            item.thumb = img.attr('src') || img.attr('SRC');
            
            // aircraft
            item.aircraft = $(this).find('a[href*=\'aircraft_genericsearch\']').text();
            
            // airline
            item.airline = $(this).find('a[href*=\'airlinesearch\']').text();
            
            // place
            item.place = $(this).find('a[href*=\'placesearch\']').text();
            
            // country
            item.country = $(this).find('a[href*=\'countrysearch\']').text();
            
            // date
            item.date = $(this).find('a[href*=\'datesearch\']').text();
            
            // reg
            var reg = $(this).find('a[href*=\'regsearch\']').text();
            item.reg = (reg.length > 0) ? reg : undefined;
            
            // cn
            var cn = $(this).find('a[href*=\'cnsearch\']').text();
            item.cn = (cn.length > 0) ? cn : undefined;
            
            // code
            var code = $(this).find('a[href*=\'codesearch\']').text();
            item.code = (code.length > 0) ? code : undefined;
            
            // author
            item.author = $(this).find('a[href*=\'photographersearch\']').text();
            
            items.push(item);
        });
        
        result.count = items.length;
        result.items = items;
        
        callback(null, result);
    });
}

function loadPageByUrl(url, callback) {
    var urlComponents = urlParser.parse(url);
    var httpObj;
    
    if(urlComponents.protocol === 'http:') {
        httpObj = http;
    } else if(urlComponents.protocol === 'https:') {
        httpObj = https;
    } else {
        throw 'Unsupported protocol';
    }
    
    httpObj.get({
        host: urlComponents.host,
        port: urlComponents.port,
        path: urlComponents.href
    }, function(res) {
        if(res.statusCode === 200) {
            res.setEncoding('utf8');
            
            var output = '';
            res.on('data', function(chunk) {
                output += chunk;
            }).on('end', function() {
                callback(null, output);
            });
        } else {
            callback(res.statusCode, 'can\'t load a site page');
        }
    });
}
