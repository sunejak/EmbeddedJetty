#!/bin/sh

# script running from:
# ssh  -o ConnectTimeout=10  sune@sune.simula.nornet
#
# using crontab every minute

curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.aunegaarden.uninett.uit.nornet:8090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S`
curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.aunegaarden.telenor.uit.nornet:8090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S`
curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.aunegaarden.powertech.uit.nornet:8090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S`

curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.kettwig.dfn.ude.nornet:8090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S`
curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.kettwig.versatel.ude.nornet:8090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S`

curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.vaenern.sunet.kau.nornet:8090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S`

curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.floeibanen.uninett.uib.nornet:8090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S`
curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.floeibanen.bkk.uib.nornet:8090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S`

curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.furuset.uninett.uio.nornet:8090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S`
curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.furuset.broadnet.uio.nornet:8090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S`
curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.furuset.powertech.uio.nornet:8090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S`

curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.ullern.uninett.simula.nornet:8090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S`
curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.ullern.kvantel.simula.nornet:8090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S`
curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.ullern.powertech.simula.nornet:8090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S`
curl --max-time 9  --write-out "Took: %{time_total} s %{time_connect} s %{time_namelookup} s %{http_code} %{url_effective}\n"  ntnu-test.ullern.telenor.simula.nornet:8090/token/ReceiveToken?Alive_Check=`date +%H_%M_%S`
