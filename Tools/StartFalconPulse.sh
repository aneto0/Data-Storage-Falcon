#!/bin/sh

#Export all the falcon tree paths
MDSPLUS_LOC="192.168.130.46:8000::/tmp"
export falcon=$MDSPLUS_LOC
export falcon_mon=$MDSPLUS_LOC
export falcon_trend=$MDSPLUS_LOC
export falcon_conf=$MDSPLUS_LOC
export falcon_fast=$MDSPLUS_LOC

echo "Creating new Falcon pulse"
mdstcl < CreateFalconPulse.tcl

echo "Storing current configuration in last falcon pulse"
java -cp .:/usr/local/mdsplus/java/classes/MdsPlus.jar:/usr/local/mdsplus/java/classes/mdsobjects.jar:/home/codac-dev/Projects/Data-Storage-Falcon/Build/:/opt/epics-3.15.5/lib/jca-2.3.6.jar:/opt/codac-5.4/lib/caj-1.1.15.jar:javax.json-1.0.jar SaveEpicsSnapshotInMds falcon_conf 0

echo "Triggering PON Sampler"
caput FALCON:PONSAMP:STATUS_CMD 1

echo "Triggering Fast acquisition"
caput FALCON:FAST:STATUS_CMD 1

