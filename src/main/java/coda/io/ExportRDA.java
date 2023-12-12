package coda.io;

import javax.script.ScriptException;
import javax.swing.JFileChooser;

import org.rosuda.JRI.Rengine;

public class ExportRDA {
    Rengine re;
    //ScriptEngineManager manager;
    //ScriptEngine engine;
    
    String fname;
    String[] df_names;
    JFileChooser cf;
    public ExportRDA(Rengine r) throws ScriptException{
        //manager = new ScriptEngineManager(); //Static ?
        //engine = manager.getEngineByName("Renjin");
        re = r;        
        //cf = chooseFile;
        
    }
}
