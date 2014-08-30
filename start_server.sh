#!/bin/bash
#
# Script to restart Server if it is gone...
#
# use /bin/bash to run commands, no matter what /etc/passwd says
SHELL=/bin/sh
# mail any output to 'paul', no matter whose crontab this is
MAILTO=ttjsun

pre="java"
pattern="Server-1.0-SNAPSHOT-jar-with-dependencies.jar"

result=`ps aux | grep java | grep $pattern`
rc=$?

if [[ $rc != 0 ]] ; then
	echo Restart needed
	nohup $pre -jar target/Server-1.0-SNAPSHOT-jar-with-dependencies.jar 8090 localhost >> jetty_`date +%Y`_`date +%b`_`date +%d`_`date +%H``date +%M`.log &
	echo Done  `date`
fi

