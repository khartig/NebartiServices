#!/bin/bash

classpath="-cp ../libs/dataIngest/*"
classpath=$classpath:../dataIngest.jar

launchTarget=com.idot.dataingest.schedulers.summarizer.SummarizerScheduler

java $classpath $launchTarget
