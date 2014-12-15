#!/bin/bash
sudo ./startDBCleaner.sh &>/var/log/nebartiServices/dbCleaner.log &
sudo ./startDataIngest.sh &>/var/log/nebartiServices/dataIngest.log &
sudo ./startSummarizer.sh &>/var/log/nebartiServices/summarizer.log &
sudo ./startWebPageLoader.sh &>/var/log/nebartiServices/webPageLoader.log &
