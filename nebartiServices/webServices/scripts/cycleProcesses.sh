#!/bin/bash

while true
do
    sudo ./shutdown.sh
    sudo service tomcat7 restart
    sleep 30s

    sudo ./startup.sh
    sleep 1h
done
