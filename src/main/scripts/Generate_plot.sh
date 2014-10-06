#!/bin/bash
#
# Script to generate plot from a name in a logfile
#
logfile=$1
if [[ ${logfile} == "" ]] ; then
    echo "Usage: logfile pattern timezone secondaryPattern"
    exit 1
fi

filename=$2
if [[ ${filename} == "" ]] ; then
    echo "Usage: logfile pattern timezone secondaryPattern"
    exit 1
fi

timezone=$3
if [[ ${timezone} == "" ]] ; then
    timezone=CEST
    echo "Setting timezone to $timezone"
fi

grep ${filename} ${logfile} > buffer.log
rc=$?
if [[ ${rc} != 0 ]] ; then
    echo "Could not find data in $logfile"
    exit ${rc}
fi

secondary=$4
if [[ ${secondary} != "" ]] ; then
        grep "$secondary" buffer.log > secondary.log
        if [[ ${rc} != 0 ]] ; then
            echo "Could not find data in $logfile"
                exit ${rc}
        fi
        rm buffer.log
        cp secondary.log buffer.log
fi

count=`grep Invo buffer.log | wc -l`
exceptions=`grep Exce buffer.log | wc -l`
slow=`grep Time buffer.log | wc -l`

echo "reset" > run.plt
echo "pfile='buffer.log'"  >> run.plt
echo "set terminal gif giant size 1024,738" >> run.plt
echo "set output '$filename.gif'" >> run.plt
echo "set style data fsteps" >> run.plt
echo "set timefmt \"%b %d %H:%M:%S $timezone %Y\"" >> run.plt
echo "set xdata time" >> run.plt
echo "set ylabel \"Time/status\"" >> run.plt
echo "set y2label \"Free memory\"" >> run.plt
echo "set format x \"%H:%M:%S\n%d/%m\n%Y\"" >> run.plt
echo "set grid" >> run.plt
echo "set autoscale y" >> run.plt
echo "set autoscale y2" >> run.plt
echo "set autoscale x" >> run.plt
echo "set y2tics" >> run.plt
echo "set tics out" >> run.plt
echo "set title \"Time and status for $filename with $count invocations and $exceptions exceptions and $slow time issues \"" >> run.plt
echo "set key left" >> run.plt
# entries bigger than 5 seconds can fall outside
echo "set yrange [ -1 : 5000 ]" >> run.plt
# offset the Memory values so that they don't cover the Time values
echo "set y2range [ -2000000 : ]" >> run.plt
echo "plot pfile using 12:4 t 'Time' axes x1y1 lt rgbcolor \"green\", \\" >> run.plt
echo "     pfile using 12:9 t 'Status' axes x1y1 lt rgbcolor \"magenta\", \\" >> run.plt
echo "     pfile using 12:18 t 'Memory' axes x1y2 lt rgbcolor \"black\"" >> run.plt

gnuplot run.plt
rc=$?
if [[ ${rc} != 0 ]] ; then
    echo "Could not plot data"
    exit ${rc}
    else
    echo "Result in $filename.gif  $count and $exceptions Exceptions and $slow Slow invocations"
fi

rm run.plt buffer.log
rc=$?
if [[ ${rc} != 0 ]] ; then
    echo "Could not delete temporary files"
    exit ${rc}
fi


