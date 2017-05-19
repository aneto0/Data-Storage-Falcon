import java.util.*;
import java.util.concurrent.*;
import gov.aps.jca.*;
import gov.aps.jca.dbr.*;
import gov.aps.jca.event.*;
import java.io.*;
import MDSplus.*;


public class SaveEpicsSnapshotInMds
{
    static boolean debug = false;

    static Data DBR2Data(DBR dbr) throws Exception
    {
        Data data = null;
        int count = dbr.getCount();
        if(dbr.isBYTE())
        {
            byte[] val = ((DBR_Byte)dbr).getByteValue();
            if(count > 1)
                data = new Int8Array(val);
            else
                data = new Int8(val[0]);
        }
        else if(dbr.isSHORT())
        {
            short[] val = ((DBR_Short)dbr).getShortValue();
            if(count > 1)
                data = new Int16Array(val);
            else
                data = new Int16(val[0]);
        }
        else if(dbr.isINT())
        {
            int[] val = ((DBR_Int)dbr).getIntValue();
            if(count > 1)
                data = new Int32Array(val);
            else
                data = new Int32(val[0]);
        }
        else if(dbr.isFLOAT())
        {
            float[] val = ((DBR_Float)dbr).getFloatValue();
            if(count > 1)
                data = new Float32Array(val);
            else
                data = new Float32(val[0]);
        }
        else if(dbr.isDOUBLE())
        {
            double[] val = ((DBR_Double)dbr).getDoubleValue();
            if(count > 1)
                data = new Float64Array(val);
            else
                data = new Float64(val[0]);
        }
        else if(dbr.isSTRING())
        {
            java.lang.String[] val = ((DBR_String)dbr).getStringValue();
            if(count > 1)
                data = new StringArray(val);
            else
                data = new MDSplus.String(val[0]);
        }
        else throw new Exception("Unsupported DBR type");
        return data;
    }

    static long DBR2Time(DBR dbr) throws Exception
    {
        if(!dbr.isTIME())
            throw new Exception("Time not supported");

        if(dbr.isBYTE())
            return (long)(((DBR_TIME_Byte)dbr).getTimeStamp().asDouble()*1E9);
        if(dbr.isSHORT())
            return (long)(((DBR_TIME_Short)dbr).getTimeStamp().asDouble()*1E9);
        if(dbr.isINT())
            return (long)(((DBR_TIME_Int)dbr).getTimeStamp().asDouble()*1E9);
        if(dbr.isFLOAT())
            return (long)(((DBR_TIME_Float)dbr).getTimeStamp().asDouble()*1E9);
        if(dbr.isDOUBLE())
            return (long)(((DBR_TIME_Double)dbr).getTimeStamp().asDouble()*1E9);
        throw new Exception("Unsupported Type in getTimestamp()");
    }

    static DBRType DBR2TimedType(DBR dbr) throws Exception
    {
        if(dbr.isBYTE())
            return DBRType.TIME_BYTE;
        if(dbr.isSHORT())
            return DBRType.TIME_SHORT;
        if(dbr.isINT())
            return DBRType.TIME_INT;
        if(dbr.isFLOAT())
            return DBRType.TIME_FLOAT;
        if(dbr.isDOUBLE())
            return DBRType.TIME_DOUBLE;
        throw new Exception("Unsupported Type in getTimestamp()");
    }

    static int DBR2NItems(DBR dbr) throws Exception
    {
        return dbr.getCount();
    }

    static int CAStatus2Severity(CAStatus status)
    {
        CASeverity severity = status.getSeverity();
        if(severity == CASeverity.SUCCESS) return 0;
        if(severity == CASeverity.INFO) return 1;
        if(severity == CASeverity.WARNING) return 2;
        if(severity == CASeverity.ERROR) return 3;
        if(severity == CASeverity.SEVERE) return 4;
        if(severity == CASeverity.FATAL) return 5;
        return 0;
    }



    public static void main(java.lang.String[] args)
    {
        if(args.length != 2 && args.length != 1)
        {
            System.out.println("Usage:java SaveEpicsSnapshotInMds <experiment> [shot]");
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
            Tree tree;
            if(shot != -1)
            {
                tree = new Tree(experiment, -1);
                tree.createPulse(shot);
                tree.close();
                tree = new Tree(experiment, shot);
            }
            else
                tree = new Tree(experiment, -1);


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
                        DBR valDbr = valChan.get();
                        ctxt.pendIO(5.);

                        //Get SCAN mode for this channel
                        TreeNode valNode = tree.getNode(nodeName+":VAL");
                        Data valData = DBR2Data(valDbr);
                        System.out.println("Storing " + recName + ".VAL in " + nodeName + ":VAL with value: " + valData);
                        valNode.putData(valData);
                    }catch(Exception exc)
                    {
                        System.err.println("Error handling record "+ recName + ": " + exc);
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
