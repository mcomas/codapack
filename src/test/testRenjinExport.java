/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.renjin.eval.Context;
import org.renjin.primitives.io.serialization.RDataWriter;
import org.renjin.sexp.DoubleArrayVector;
import org.renjin.sexp.DoubleVector;
import org.renjin.sexp.IntArrayVector;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.PairList;
import org.renjin.sexp.SEXP;
import org.renjin.sexp.StringArrayVector;
import org.renjin.sexp.StringVector;

/**
 *
 * @author marc
 */
public class testRenjinExport {
    public static void main(String args[]) throws ScriptException, FileNotFoundException, IOException{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("Renjin");
        if(engine == null) {
            throw new RuntimeException("Renjin Script Engine not found on the classpath.");
        }
        
        ListVector.NamedBuilder dataframe = new ListVector.NamedBuilder();
        double []x = {1,2,3,4,5,6};
        String []y = {"a", "b", "a", "b", "a", "b"};
        int []id = {1,2,3,4,5,6};
        dataframe.add("var1", new DoubleArrayVector(x));
        dataframe.add("var2", new StringArrayVector(y));
        
        dataframe.setAttribute("row.names", new IntArrayVector(id));
        dataframe.setAttribute("class", new StringArrayVector("data.frame"));
        PairList.Builder df = new PairList.Builder();
        df.add("df", dataframe.build());
        
        String fname = "OutMyfile.RData";
        Context context = Context.newTopLevelContext(); 
        FileOutputStream fos = new FileOutputStream(fname);
        GZIPOutputStream zos = new GZIPOutputStream(fos);
        RDataWriter writer = new RDataWriter(context, zos);
        writer.save(df.build());
        zos.close();
        fos.close();
    }
}
