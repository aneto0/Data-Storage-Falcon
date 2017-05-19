import java.util.*;
import java.util.concurrent.*;
import gov.aps.jca.*;
import gov.aps.jca.dbr.*;
import gov.aps.jca.event.*;
import java.io.*;
import MDSplus.*;


public class LoadEpicsSnapshotFromMds
{
    static boolean debug = false;


    public static void main(java.lang.String[] args)
    {
        if(args.length != 2 && args.length != 1)
        {
            System.out.println("Usage:java LoadEpicsSnapshotFromMds <experiment> [shot]");
            return;
        }
        java.lang.String experiment = args[0];
        int shot = -1;
        if(args.length == 2)
        {
            try {
                shot = Integer.parseInt(args[1]);
            }catch(Exception exc)
            {
                System.out.println("Invalid shot number");
                System.exit(0);
            }
        }
        else
            shot = -1;
        try {
            JCALibrary jca = JCALibrary.getInstance();
            Context ctxt = jca.createContext(JCALibrary.CHANNEL_ACCESS_JAVA);
            Tree tree = new Tree(experiment, shot);
            TreeNodeArray treeNodeArr = tree.getNodeWild("***");
            java.lang.String []nodeNames = treeNodeArr.getPath();
            int[] nids = treeNodeArr.getNid();
            for(int i = 0; i < nodeNames.length; i++)
            {
                if(nodeNames[i].endsWith(":REC_NAME"))
                {
                    java.lang.String nodeName = nodeNames[i].substring(0, nodeNames[i].length() - 9);
                    java.lang.String recName  = "";

                    try {
                        recName = new TreeNode(nids[i], tree).getData().getString();
                        recName = recName.trim();
                        Channel valChan = ctxt.createChannel(recName+".VAL");
                        ctxt.pendIO(5.);
                        TreeNode valNode = tree.getNode(nodeName+":VAL");
                        Data valData = valNode.getData();	
                        if(valData instanceof Int8 || valData instanceof Uint8)
                            valChan.put( valData.getByte());
                        else if(valData instanceof Int16 || valData instanceof Uint16)
                            valChan.put( valData.getShort());
                        else if(valData instanceof Int32 || valData instanceof Uint32)
                            valChan.put( valData.getInt());
                        else if(valData instanceof Int64 || valData instanceof Uint64)
                            valChan.put( valData.getInt());
                        else if(valData instanceof Float32)
                            valChan.put( valData.getFloat());
                        else if(valData instanceof Float64)
                            valChan.put( valData.getDouble());
                        else if(valData instanceof Int8Array || valData instanceof Uint8Array)
                            valChan.put( valData.getByteArray());
                        else if(valData instanceof Int16Array || valData instanceof Uint16Array)
                            valChan.put( valData.getShortArray());
                        else if(valData instanceof Int32Array || valData instanceof Uint32Array)
                            valChan.put( valData.getIntArray());
                        else if(valData instanceof Int64Array || valData instanceof Uint64Array)
                            valChan.put( valData.getIntArray());
                        else if(valData instanceof Float32Array)
                            valChan.put( valData.getFloatArray());
                        else if(valData instanceof Float64Array)
                            valChan.put( valData.getDoubleArray());
                        else if(valData instanceof MDSplus.String)
                            valChan.put( valData.getString());
                        else if(valData instanceof MDSplus.StringArray)
                            valChan.put( valData.getStringArray());
                        else
                            System.out.println("Unsupported type!");

                        DBR valDbr = valChan.get();
                        ctxt.pendIO(5.);
                        System.out.println("Writing value: " + valData + " read from " + nodeName + ":VAL into " + recName + ".VAL");
                        valNode.putData(valData);
                    }catch(Exception exc)
                    {
                        System.err.println("Error handling record "+ recName + " [" + nodeName + ":VAL] : " + exc);
                    }
                }
            }
            ctxt.dispose();
        }catch(Exception exc)
        {
            System.err.println("Generic error: "+ exc);
            System.exit(0);
        }
    }
}
