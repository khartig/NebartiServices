#!/bin/bash

sudo service tomcat7 stop
sudo rm -rf /var/lib/tomcat7/webapps/ROOT
sudo rm /var/lib/tomcat7/webapps/ROOT.war
sudo cp ../nebartiServices.war /var/lib/tomcat7/webapps/ROOT.war
sudo service tomcat7 start
