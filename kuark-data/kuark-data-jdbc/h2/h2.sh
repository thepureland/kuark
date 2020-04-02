#!/bin/sh
#TODO test
echo "start H2 server with web console ..."
dir=$(dirname "$0")
java -cp "$dir/h2-1.4.200.jar:$H2DRIVERS:$CLASSPATH" org.h2.tools.Console -tcpPort 9092 -webPort 8082 "$@"
