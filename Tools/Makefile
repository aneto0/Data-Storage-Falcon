#
# Compiles all the MDSPlus java tools and builds the MDSPlus trees
#
CLASSPATH=.:/usr/local/mdsplus/java/classes/MdsPlus.jar:/usr/local/mdsplus/java/classes/mdsobjects.jar:javax.json-1.0.jar:/opt/epics-3.15.5/lib/jca-2.3.6.jar
JFLAGS = 
JC = javac
JAVA = java
BUILD_DIR=../Build
TREE_LOCATION=../Tree

#
# The exports must be done at this level
# 
export falcon_path=$(TREE_LOCATION) 
export falcon_trend_path=$(TREE_LOCATION)

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

trend: classes
	$(JAVA) -cp $(CLASSPATH):$(BUILD_DIR) Xml2MdsTrend ../Configurations/SDD_falcon_var falcon_trend

trees: trend
	mdstcl < CreateFalconMDSPlusTrees.tcl

clean:
	$(RM) $(BUILD_DIR)/*.class
