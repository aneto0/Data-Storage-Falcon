import MDSplus.Tree;
import MDSplus.TreeNode;
import MDSplus.Float32;
import MDSplus.Int32;
import MDSplus.Data;

import java.io.*;
import java.util.*;

/*
   xsltproc sdd2mds.xsl SI_4.xml > si.xml
   javac -cp /usr/local/mdsplus/java/classes/mdsobjects.jar Xml2Mds.java
   java -cp /usr/local/mdsplus/java/classes/mdsobjects.jar:. Xml2Mds si   
   */


class Xml2MdsConfig
{
    Hashtable nameH = new Hashtable();

    String toMdsName(String inName)
    {
        if(inName.length() <= 10)
            return inName.toUpperCase();
        String truncatedName = inName.substring(0, 10);
        int tNum;
        try {
            tNum = ((Integer)nameH.get(truncatedName)).intValue();
        }catch(Exception exc)
        {
            tNum = 0;
        }
        String retName = truncatedName+tNum;
        tNum++;
        nameH.put(truncatedName, new Integer(tNum));
        return retName.toUpperCase();
    }

    public void createConfigPulseFile(String xmlVariablesFile, String expName, boolean debug)
    {
        int num_pv_node = 0;
        String currLine;
        Vector<String> pathNames = new Vector<String>();


        Tree tree = null;

        System.out.println("createConfigPulseFile");
        int numPvNodes = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(xmlVariablesFile+".xml"));
            tree = new Tree(expName, -1, "NEW");
            String currVarName, currDescription, currDirection;
            while((currLine = br.readLine()) != null)
            {
                if(currLine.trim().equals(""))
                    continue;
                StringTokenizer st0 = new StringTokenizer(currLine, ";");

                currVarName = st0.nextToken().trim();
                //Check if the currVarName contains the _Act keyword
                if (!currVarName.toUpperCase().contains("_ACT")) {
                    continue;
                }
                try {
                    currDescription = st0.nextToken();
                }catch(Exception exc){currDescription = null;}
                String dataType = st0.nextToken();
                st0.nextToken();
                st0.nextToken();
                try
                {
                    StringTokenizer st = new StringTokenizer(currVarName, ":-_");
                    String currPath = "";
                    String nodePath;
                    while(st.hasMoreTokens())
                    {
                        String currName = toMdsName(st.nextToken());
                        if (debug) System.out.println("Adding "+currPath + "." + currName);
                        nodePath = currPath + "." + currName;
                        if(pathNames.indexOf(nodePath) == -1)
                        {
                            tree.addNode(nodePath, "STRUCTURE");
                            pathNames.addElement(nodePath);
                        }
                        currPath += "."+currName;
                        if(!st.hasMoreTokens()) //Last part
                        {
                            tree.addNode(currPath+":REC_NAME", "TEXT");
                            TreeNode currNode = tree.getNode(currPath+":REC_NAME");
                            if( currVarName != null ) 
                                currNode.putData(new MDSplus.String(currVarName));
                            tree.addNode(currPath+":DESCRIPTION", "TEXT");
                            if(currDescription != null)
                            {
                                currNode = tree.getNode(currPath+":DESCRIPTION");
                                if (debug)  System.out.println("currDescription : "+currDescription);
                                currNode.putData(new MDSplus.String(currDescription));
                            }
                            dataType = dataType.trim();
                            System.out.println("Type:" + dataType);
                            if(dataType.equals("") || dataType.equals("int") || dataType.equals("uint") || dataType.equals("float"))    
                                tree.addNode(currPath+":VAL", "NUMERIC");
                            else
                                tree.addNode(currPath+":VAL", "TEXT");
                            numPvNodes++;
                        }
                    }

                }catch(Exception exc)
                {
                    System.out.println("Error creating model: " + exc);
                    return;
                }
            }
            tree.write();
            System.out.println("Added " + numPvNodes + " process variable nodes ");
        }
        catch(Exception exc)
        {
            try{
                tree.write();
            }catch(Exception exc1){}
            System.out.println("Error on eperiment "+expName+" : "+exc);
        }
    }	


    public static void main(String args[])
    {

        Xml2MdsConfig xml2MdsConfig = new Xml2MdsConfig();
        if( args.length < 2)
        {
            System.out.println("Usage: java Xml2MdsConfig <variables> <ExperimentName> [debug] ");
            System.exit(0);
        }

        xml2MdsConfig.createConfigPulseFile(args[0], args[1], (args.length >= 3 && args[2].equals("debug")));
    }
}

