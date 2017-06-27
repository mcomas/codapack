/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import coda.DataFrame;
import coda.Variable;
import coda.io.ImportData;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.renjin.script.RenjinScriptEngineFactory;
import org.renjin.sexp.DoubleArrayVector;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.Vector;

/**
 *
 * @author marc
 */
public class renjinCalls {
    public static void main(String args[]) throws IOException, FileNotFoundException, InvalidFormatException {
        DataFrame df = ImportData.importXLS("halimba.xls", true);
        ListVector dataframe = new ListVector();
        // Al2O3	SiO2	Fe2O3	TiO2	H20	residual	Group
        
        RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
        ScriptEngine engine = factory.getScriptEngine();
        engine.put("`Al2 O3`", new DoubleArrayVector(df.get("Al2 O3").getNumericalData()));
        engine.put("`SiO2`", new DoubleArrayVector(df.get("SiO2").getNumericalData()));
        try {
            Vector v = (Vector) engine.eval("`Al2 O3` + `SiO2`");
            System.out.println(v.getElementAsDouble(0));
        } catch (ScriptException ex) {
            Logger.getLogger(renjinCalls.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
