#!/bin/sh

# script running from:
# ssh  -o ConnectTimeout=10  sune@sune.simula.nornet
#
# using crontab every minute

curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.heimdal.uninett.ntnu.nornet:9090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S_%N`
curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.heimdal.powertech.ntnu.nornet:9090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S_%N`

