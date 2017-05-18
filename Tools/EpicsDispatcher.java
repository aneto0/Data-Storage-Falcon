import java.util.*;
import java.util.concurrent.*;
import gov.aps.jca.*;
import gov.aps.jca.dbr.*;
import gov.aps.jca.event.*;
import java.io.*;
import javax.json.*;

public class EpicsDispatcher
{
    static class ActionDescriptor {
        String pvName;
        int [] values;
        String [] commands;
        ActionDescriptor(String pvName, int []values, String [] commands)
        {
            this.pvName = pvName;
            this.values = values;
            this.commands = commands;
            System.out.println("Action created. PV: " + pvName);
            for(int i = 0; i < values.length; i++)
                System.out.println("Value: " + values[i] + "   Commands: " + commands[i]); 
        }
    }
    static class ActionMonitor implements MonitorListener
    {
        ActionDescriptor actionDescriptor;
        boolean isFirst = true;

        ActionMonitor(ActionDescriptor actionDescriptor)
        {
            this.actionDescriptor = actionDescriptor;
        }
        public synchronized void monitorChanged(MonitorEvent e)
        {
            if(isFirst)  //Do not trigger anything at startup
            {
                isFirst = false;
                return;
            }
            System.out.println("MONITOR: " + actionDescriptor.pvName);
            int newVal;
            DBR dbr = e.getDBR();
            try {
                if(dbr.isBYTE())
                {
                    byte[] val = ((DBR_Byte)dbr).getByteValue();
                    newVal = val[0];
                }
                else if(dbr.isSHORT())
                {
                    short[] val = ((DBR_Short)dbr).getShortValue();
                    newVal = val[0];
                }
                else if(dbr.isINT())
                {
                    int[] val = ((DBR_Int)dbr).getIntValue();
                    newVal = val[0];
                }
                else if(dbr.isFLOAT())
                {
                    float[] val = ((DBR_Float)dbr).getFloatValue();
                    newVal = (int)val[0];
                }
                else if(dbr.isDOUBLE())
                {
                    double[] val =  ((DBR_Double)dbr).getDoubleValue();
                    newVal = (int)val[0];
                }
                else throw new Exception("Unsupported DBR type");
                System.out.println("New Value: " + newVal);
                //Allow to associate more than one command to the same value
                boolean atLeastOneCommandFound = false;
                for(int i = 0; i < actionDescriptor.values.length; i++)
                {
                    if(actionDescriptor.values[i] == newVal)
                    {
                        atLeastOneCommandFound = true;
                        System.out.println("Executing: " + actionDescriptor.commands[i]);
                        String line;
                        Process p = Runtime.getRuntime().exec(actionDescriptor.commands[i]);
                        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()) );
                        while ((line = in.readLine()) != null) 
                        {
                            System.out.println(line);
                        }
                        p.waitFor();
                    }
                }
                if (!atLeastOneCommandFound)        
                {
                    System.out.println("No command associated with value " + newVal + " for PV " + actionDescriptor.pvName);
                }

            }catch(Exception exc)
            {
                System.out.println(exc);
            }
        }
    } // End inner class ActionMonitor


    static Vector <ActionDescriptor> actionsV = new Vector<ActionDescriptor>();
    static Runtime runtime = Runtime.getRuntime();


    static void parseDescription(String description) throws Exception
    {
        FileInputStream fis = new FileInputStream(description);
            JsonReader rdr = Json.createReader(fis);
            JsonObject obj = rdr.readObject();
            JsonArray pvObjs = obj.getJsonArray("pvlist");
        for(int i = 0; i < pvObjs.size(); i++)
        {
            String pvName = pvObjs.getJsonObject(i).getString("name");
            JsonArray actionsObjs = pvObjs.getJsonObject(i).getJsonArray("actions");
            int []values = new int[actionsObjs.size()];
            String []commands = new String[actionsObjs.size()];
            for(int j = 0; j < actionsObjs.size(); j++)
            {
                values[j] = actionsObjs.getJsonObject(j).getInt("value");
                commands[j] = actionsObjs.getJsonObject(j).getString("command");
            }
            actionsV.addElement(new ActionDescriptor(pvName, values, commands));
        }
    }


    public static void main(String args[])
    {
        if(args.length != 1)
        {
            System.out.println("Usage: java EpicsDispatcher <configuration file>");
            System.exit(0);
        }
        try {
            JCALibrary jca = JCALibrary.getInstance();
            Context ctxt = jca.createContext(JCALibrary.CHANNEL_ACCESS_JAVA);

            //Load configuration
            parseDescription(args[0]);
            for(int i = 0; i < actionsV.size(); i++)
            {
                try {
                    Channel chan = ctxt.createChannel(actionsV.elementAt(i).pvName);
                    ctxt.pendIO(5.);
                    chan.addMonitor(DBRType.TIME_INT, 1, Monitor.VALUE, 
                            new ActionMonitor(actionsV.elementAt(i)));
                    ctxt.pendIO(5.);
                }catch(Exception exc)
                {
                    System.out.println("Error handling record "+ actionsV.elementAt(i).pvName + ": " + exc);
                }
            }
        }catch(Exception exc)
        {
            System.out.println(exc);
        }	
    }
}


