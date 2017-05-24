# Falcon Data Storage and Supervision

This project contains all the files related to the management of the Falcon data storage and supervision system. 

## Functions

The main functions of the Falcon data acquisition system are:
* [F1] Define and manage the concept of an experiment, so that a new MDSplus pulse number is created everytime an experiment is executed;
* [F2] Define and manage an MDSplus database model to store the control system configuration against a given pulse number;
* [F3] Define and manage an MDSplus database model to store the plant signals acquired by the control system; 
* [F3.1] Some signals are to be continuosly stored, everytime the signal changes and irrespectively of the pulse number;	 
* [F3.2] Some signals are to be stored and associated against a given pulse number of an experiment;
* [F3.2.1] Some signals are to be stored at a rate of less than 1 kHz;
* [F3.2.2] Some signals are to be stored at a rate greater or equal to 1 kHz; 
* [F4] Allow to configure the control system with the configuration parameters that were used in a previous experiment. 
 
These functions are implemented in the following subsystems:
* [F3.2.1] in the [PON Sampler](https://vcis-gitlab.f4e.europa.eu/aneto/PON-Sampler-Falcon);
* [F3.2.2] in the [Fast Data Acquisition](https://vcis-gitlab.f4e.europa.eu/aneto/PON-Sampler-Falcon);
* [F1], [F2] and [F4] in the Supervisor service of the Data Storage System (described in this document); 
* [F3.1] in the Trend service of the Data Storage System (described in this document);

## Networks

The following data acquisition networks are deployed in the Falcon plant:
* PON: for the configuration and monitoring of the control system. The traffic is EPICS ChannelAccess;
* DAN: for storage of plant configuration and signals in the MDSplus database. The traffic is MDSplus mdsip;
* SDN: to provide the time synchronisation between the PON Sampler and the Fast Data Acquisition subsystems. The traffic is multicast UDP. 

## Data Storage System Architecture

The Data Storage System provides the Supervisor and the Trend archiver java based services. 

![alt text](Documentation/Images/networks.png "Network layout")

### Supervisor

The Supervisor is a java program named [EpicsDispatcher](https://vcis-gitlab.f4e.europa.eu/aneto/Data-Storage-Falcon/blob/master/Tools/EpicsDispatcher.java). It allows to associate the execution of a command line action against the change of an EPICS PV. The configuration of the Supervisor is provided by this [configuration file](https://vcis-gitlab.f4e.europa.eu/aneto/Data-Storage-Falcon/blob/master/Configurations/FalconPulseSupervisor.json).

|   |   |   |   |   |
|---|---|---|---|---|
|   |   |   |   |   |
|   |   |   |   |   |
|   |   |   |   |   |



| Tables        | Are           | Cool  |
| ------------- |:-------------:| -----:|
| col 3 is      | right-aligned | $1600 |
| col 2 is      | centered      |   $12 |
| zebra stripes | are neat      |    $1 |


| PV | Value | Action | Comments |
| ---| ----- | ------ | -------- |
| FALCON:PULSE:START | 1 | Execute [StopFalconPulse.sh] (https://vcis-gitlab.f4e.europa.eu/aneto/Data-Storage-Falcon/blob/master/Tools/StopFalconPulse.sh) | Puts the PON Sampler and the Fast Data Acquisition systems in IDLE (data is not stored) | 


  
 