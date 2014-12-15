#!/bin/bash

classpath="-cp ../libs/dataIngest/*"
classpath=$classpath:../dataIngest.jar

launchTarget=com.idot.dataingest.schedulers.dbcleaner.DBCleanerScheduler

java $classpath $launchTarget
