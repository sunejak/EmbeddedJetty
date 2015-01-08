#!/bin/sh

# script running from:
# ssh  -o ConnectTimeout=10  sune@sune.simula.nornet
#
# using crontab every minute

curl -s --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.heimdal.uninett.ntnu.nornet:9090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S_%N`
curl -s --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.heimdal.powertech.ntnu.nornet:9090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S_%N`

curl -s --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.bymarka.uninett.ntnu.nornet:9090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S_%N`
curl -s --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.bymarka.powertech.ntnu.nornet:9090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S_%N`

curl -s --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.lerkendal.uninett.ntnu.nornet:9090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S_%N`
curl -s --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.lerkendal.powertech.ntnu.nornet:9090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S_%N`

curl -s --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.byaasen.uninett.ntnu.nornet:9090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S_%N`
curl -s --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.byaasen.powertech.ntnu.nornet:9090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S_%N`

curl -s --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.bakklandet.uninett.ntnu.nornet:9090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S_%N`
curl -s --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.bakklandet.powertech.ntnu.nornet:9090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S_%N`

curl -s --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.bybro.uninett.ntnu.nornet:9090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S_%N`
curl -s --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.bybro.powertech.ntnu.nornet:9090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S_%N`

