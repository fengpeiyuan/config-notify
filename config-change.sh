#!/bin/sh

V1="10.213.42.125:6379"
V2="10.213.42.128:6379"
V=$V1

for i in {1..999999} 
do
echo '@@@ Before:'$V
/home/fengpeiyuan/zookeeper-3.3.6/bin/zkCli.sh -server 10.209.36.19:8081 set /test/t $V 2>&1 
if [ $V != $V1 ]
then
	V=$V1
else
        V=$V2
fi
echo '@@@ After:'$V
sleep 10
done
