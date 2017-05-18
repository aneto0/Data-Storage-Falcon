#
# Compiles all the MDSPlus java tools and builds the MDSPlus trees
#
CLASSPATH=.:/usr/local/mdsplus/java/classes/MdsPlus.jar:/usr/local/mdsplus/java/classes/mdsobjects.jar:javax.json-1.0.jar:$(CODAC_ROOT)/lib/jca-2.3.6.jar:$(CODAC_ROOT)/lib/caj-1.1.15.jar
JFLAGS = 
JC = javac
JAVA = java
BUILD_DIR = ../Build
#Only set the TREE_LOCATION if it not set already
TREE_LOCATION ?= ../Tree

#
# The exports must be done at this level
# 
export falcon_path=$(TREE_LOCATION) 
export falcon_conf_path=$(TREE_LOCATION) 
export falcon_trend_path=$(TREE_LOCATION)
export falcon_mon_path=$(TREE_LOCATION)
export falcon_fast_path=$(TREE_LOCATION)

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) -cp $(CLASSPATH) -d $(BUILD_DIR) $*.java

CLASSES = \
	ChannelArchiver.java  \
	EpicsDispatcher.java  \
	LoadEpicsConf.java  \
	StoreEpicsConf.java \
	Xml2MdsConfig.java \
	Xml2MdsSnapshot.java \
	Xml2MdsTrend.java

default: classes

classes: $(CLASSES:.java=.class)

trees: 
	$(JAVA) -cp $(CLASSPATH):$(BUILD_DIR) Xml2MdsConfig ../Configurations/SDD_falcon_var falcon_conf
	$(JAVA) -cp $(CLASSPATH):$(BUILD_DIR) Xml2MdsTrend ../Configurations/SDD_falcon_var falcon_trend
	$(JAVA) -cp $(CLASSPATH):$(BUILD_DIR) Xml2MdsSnapshot ../Configurations/SDD_falcon_var falcon_mon
	mdstcl < CreateFalconMDSPlusFastTree.tcl
	mdstcl < CreateFalconMDSPlusTrees.tcl

clean:
	$(RM) $(BUILD_DIR)/*.class
