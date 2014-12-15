#!/bin/bash
ps aux | pkill -9 -u root java 
ps aux | pkill -9 -f -u root WebPageLoader
ps aux | pkill -9 -f -u root phantomjs
