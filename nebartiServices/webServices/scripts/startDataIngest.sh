#!/bin/bash

classpath="-cp ../libs/dataIngest/*"
classpath=$classpath:../dataIngest.jar

launchTarget=com.idot.dataingest.DataSourceManager

java $classpath $launchTarget
