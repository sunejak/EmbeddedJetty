#!/bin/sh
#
# Use "GnuPlot" show all data.
#

logfile=$1
if [[ ${logfile} == "" ]] ; then
    echo "Usage: logfile timezone"
    exit 1
fi

timezone=$2
if [[ ${timezone} == "" ]] ; then

    # extract timezone from logfile
    timezone=`tail -10 ${logfile} | grep Invoke | sed "s:  : :g" | cut -d " " -f15 | tail -1`
    echo "Setting timezone to $timezone found from ${logfile}"
fi

#
# find the list of unique entries in log file.
#

for names in `tail -1000 ${logfile} | grep Invoke_HTTP_Get | cut -d " " -f7 | sed "s%http://ntnu-test.%%g" | sed "s%:8090/token/ReceiveToken%%g" | sort | uniq`
do

echo Plotting ${names}
./Generate_plot.sh ${logfile} ${names} ${timezone}

done