#!/bin/sh

a=0
while [ "$a" -lt 1000 ]    # this is loop1
do
   a=`expr $a + 1`
   ./NTNU_check.sh
done

