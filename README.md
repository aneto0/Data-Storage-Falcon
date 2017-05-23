# Falcon Data Storage and Supervision

This project contains all the files related to the management of the Falcon data storage and supervision system. 

## Description

The main functions of the Falcon data acquisition system are:
* [F1] Define and manage the concept of an experiment, so that a new MDSplus pulse number is created everytime an experiment is executed;
* [F2] Define and manage an MDSplus database model to store the control system configuration against a given pulse number;
* [F3] Define and manage an MDSplus database model to store the plant signals acquired by the control system; 
* [F3.1] Some signals are to be continuosly stored, everytime the signal changes and irrespectively of the pulse number;	 
* [F3.2] Some signals are to be stored and associated against a given pulse number of an experiment;
* [F3.2.1] Some signals are to be stored at a rate < 1 kHz;
* [F3.2.2] Some signals are to be stored at a rate >= 1 kHz; 
* [F4] Allow to configure the control system with the configuration parameters that were used in a previous experiment. 
 
These functions are implemented in the following subsystems:
* The [Fast Data Acquisition](https://vcis-gitlab.f4e.europa.eu/aneto/PON-Sampler-Falcon) implements [F3.2.2];
* The [PON Sampler](https://vcis-gitlab.f4e.europa.eu/aneto/PON-Sampler-Falcon) implements [F3.2.1];
* The Data-Storage-System (described in this document) implements [F1], [F2], [F3.1] and [F4]; 

