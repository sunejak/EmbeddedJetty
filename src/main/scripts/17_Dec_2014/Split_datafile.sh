#!/bin/bash
#
# Split the datafile into daily chunks
#
logfile=$1
if [[ ${logfile} == "" ]] ; then
    echo "Usage: logfile"
    exit 1
fi

csplit ${logfile} '/DUMMY/' {*}

for names in `ls xx*`
do
#
# Find the date stamp in the beginning of the file
#
newName=`head -2 ${names} | grep 00:00 | sed "s:  : :g" | cut -d " " -f 2,3,6 | tr " " "_" `
if [[ ${newName} != "" ]]
then
#
# Check if directory exists
#
if [[ -x ${newName} ]]
then
echo Reuse existing directory ${newName}
else
mkdir ${newName}
fi
echo Move ${names} to ${newName}
mv ${names} ${newName}/${logfile}
cp index.template ${newName}/index.html

cd ${newName}
for entries in `grep -A 150 DUMMY ${logfile} | grep -e "=00_00" | sed "s:ntnu-test.: :g" | sed "s:.nornet: :g" | cut -d " " -f10 | sort | uniq`
do
allCalls=`grep ${entries} ${logfile} | wc -l`
okCalls=`grep ${entries} ${logfile} | grep "s 200" | wc -l`
failedCalls=`grep ${entries} ${logfile} | grep "s 000" | wc -l`
echo On ${newName} for ${entries} total number of calls \( ${allCalls} \) ok ${okCalls} failed ${failedCalls}
../Generate_alive.sh ${logfile} ${entries} ${newName}
rc=$?
if [[ $rc != 0 ]] ; then
    echo "Could not generate plot with Generate_alive.sh Data.log ${entries} ${newName}"
    exit $rc
fi

for slivers in `grep -A 150 DUMMY ${logfile} | grep -e "=00_00" | cut -d "." -f7 | sort | uniq`
do
../Generate_alive.sh ${logfile} ${slivers} ${newName}
rc=$?
if [[ $rc != 0 ]] ; then
    echo "Could not generate plot with Generate_alive.sh ${logfile} ${slivers} ${newName}"
    exit $rc
fi
done

done
cd ..
else
echo No date stamp in file:  ${names}
fi
done

exit 0