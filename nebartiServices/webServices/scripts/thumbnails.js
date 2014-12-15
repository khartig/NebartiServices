function loadThumbnails(imgsrcs) {
    pages = [];
    var totalPages = imgsrcs.length;
    var pagesLoaded = 0;

    for (var i in imgsrcs) {
        console.log('Loading page = ' + imgsrcs[i]);
        pages[i] = new WebPage();
        pages[i].open(imgsrcs[i], function (status) {
            if (++pagesLoaded === totalPages) {            
                phantom.exit();
            } 
        });
    }
}

function getThumbnails(webPage) {
    var page = require('webpage').create();
    page.settings.userAgent = 'SpecialAgent';

    page.open(webPage, function (status) {
        if (status !== 'success') {
            console.log('Unable to access network');
            phantom.exit();
        } else {
            var d = new Date();
            var dateString = d.toUTCString();
            console.log(dateString + ' : Evaluating ' + webPage);

            var results = page.evaluate(function () {
                var img_array = new Array();
                img_array.push(document.getElementById('thumbnail_img_0').src);
                img_array.push(document.getElementById('thumbnail_img_1').src);
                img_array.push(document.getElementById('thumbnail_img_2').src);
                img_array.push(document.getElementById('thumbnail_img_3').src);
                return img_array;
            });

            console.log('loading thumbnails');
            page.release();
            loadThumbnails(results);
        }

    });
}

var system = require('system');
if (system.args.length != 2) {
    console.log('Must pass in url as argument. Usage: thumbnail.js http://url');
} else {
    console.log('arg: ' + system.args[1]);
    getThumbnails(system.args[1]);
}

