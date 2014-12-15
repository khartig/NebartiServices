#!/bin/bash

while true
do
    phantomjs ./thumbnails.js "http://nebarti.com/home/modelTemplate.html?modelName=mitt_romney&visibleName=Mitt%20Romney"
    phantomjs ./thumbnails.js "http://nebarti.com/home/modelTemplate.html?modelName=barack_obama&visibleName=Barack%20Obama"
    phantomjs ./thumbnails.js "http://nebarti.com/home/modelTemplate.html?modelName=gary_johnson&visibleName=Gary%20Johnson"
    sleep 3m
done
