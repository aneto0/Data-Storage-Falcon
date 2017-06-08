#!/bin/sh

#Export all the falcon tree paths
MDSPLUS_LOC="192.168.130.46:8000::/tmp"
export falcon=$MDSPLUS_LOC
export falcon_mon=$MDSPLUS_LOC
export falcon_trend=$MDSPLUS_LOC
export falcon_conf=$MDSPLUS_LOC
export falcon_fast=$MDSPLUS_LOC

CODE_DIRECTORY=/home/codac-dev/Projects/Data-Storage-Falcon/

echo "Creating new Falcon pulse"
mdstcl < $CODE_DIRECTORY/Tools/CreateFalconPulse.tcl

PULSE_NUMBER=`mdstcl < $CODE_DIRECTORY/Tools/ShowFalconCurrentPulse.tcl | grep shot | cut -d ' ' -f 4`
caput TEST-AUX-FDAQ:Pulse_Number $PULSE_NUMBER

echo "Storing current configuration in last falcon pulse: $PULSE_NUMBER"
java -cp .:/usr/local/mdsplus/java/classes/MdsPlus.jar:/usr/local/mdsplus/java/classes/mdsobjects.jar:$CODE_DIRECTORY/Build/:/opt/epics-3.15.5/lib/jca-2.3.6.jar:/opt/codac-5.4/lib/caj-1.1.15.jar:javax.json-1.0.jar SaveEpicsSnapshotInMds falcon_conf 0

echo "Triggering PON Sampler"
caput TEST-AUX-FDAQ:Ponsamp_Status_CMD 1

echo "Triggering Fast acquisition"
caput TEST-AUX-FDAQ:Fast_Status_CMD 1

