#!/bin/bash

#
# Script to generate plot from a name in a logfile
#
logfile=$1
if [[ $logfile == "" ]] ; then
    echo "Usage: logfile pattern "
    exit 1
fi

pattern=$2
if [[ $pattern == "" ]] ; then
    echo "Usage: logfile pattern "
    exit 1
fi

filename=$3

grep $pattern $logfile | tr "=" " " > buffer.log
rc=$?
if [[ $rc != 0 ]] ; then
    echo "Could not find data in $logfile"
    exit $rc
fi

allCalls=`grep Took: buffer.log | wc -l`
okCalls=`grep "s 200" buffer.log | wc -l`
failedCalls=`grep "s 000" buffer.log | wc -l`

echo "reset" > run.plt
echo "pfile='buffer.log'"  >> run.plt
echo "set terminal gif giant size 1024,738" >> run.plt
echo "set output '$pattern.gif'" >> run.plt
echo "set style data fsteps" >> run.plt
echo "set timefmt \"%H_%M_%S\"" >> run.plt
echo "set xdata time" >> run.plt
echo "set ylabel \"Time\"" >> run.plt
echo "set y2label \"Status\"" >> run.plt
echo "set format x \"%H:%M\"" >> run.plt
echo "set grid" >> run.plt
echo "set autoscale y" >> run.plt
echo "set autoscale y2" >> run.plt
echo "set autoscale x" >> run.plt
echo "set y2tics" >> run.plt
echo "set tics out" >> run.plt
echo "set title \"Time and status $pattern $filename \n total number of calls \( ${allCalls} \) ok ${okCalls} failed ${failedCalls} \"" >> run.plt
echo "set key left" >> run.plt
echo "set yrange [ 0 : 9 ]" >> run.plt
echo "set y2range [ -1 : 500 ]" >> run.plt
echo "plot pfile using 10:2 t 'Time' axes x1y1 lt rgbcolor \"green\", \\" >> run.plt
echo "     pfile using 10:8 t 'Status' axes x1y2 lt rgbcolor \"magenta\" \\" >> run.plt

gnuplot run.plt
rc=$?
if [[ $rc != 0 ]] ; then
    echo "Could not plot data"
    exit $rc
#    else
#    echo "Result in $pattern.gif"
fi

rm run.plt buffer.log
rc=$?
if [[ $rc != 0 ]] ; then
    echo "Could not delete temporary files"
    exit $rc
fi
exit 0
