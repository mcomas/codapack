/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió - David Gàmez
 *
 *	This file is part of CoDaPack.
 *
 *  CoDaPack is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CoDaPack is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CoDaPack.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.io;

import coda.DataFrame;
import coda.Variable;
import java.util.ArrayList;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFileChooser;
import org.renjin.sexp.DoubleVector;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.LogicalVector;
import org.renjin.sexp.SEXP;
import org.renjin.sexp.StringVector;

/**
 *
 * @author david
 */
public class ImportRDA {
    static ScriptEngineManager manager = new ScriptEngineManager();
    static ScriptEngine engine = manager.getEngineByName("Renjin");
    
    static String fname;
    static StringVector df_names;
    //Creem la llista de dataFrames que contindrà els data frames seleccionats
    ArrayList<DataFrame> sel_dfs = new ArrayList<DataFrame>();
    JFileChooser cf;
    String prefix = null;
    String suffix = null;
    
    public ImportRDA(JFileChooser chooseFile) throws ScriptException {
        cf = chooseFile;
        df_names = getDataFramesNames(chooseFile.getSelectedFile().getAbsolutePath());
    }
    
    static public StringVector getDataFramesNames(String filename) throws ScriptException {
        if(engine == null) {
            throw new RuntimeException("Renjin Script Engine not found on the classpath.");
        }
        fname = filename;
        engine.eval("load('" + fname + "')");
        engine.eval("x = sapply(sapply(ls(), get), is.data.frame)");
        System.out.println(engine.eval("names(x)"));
        System.out.println(engine.eval("names(x)[(x==TRUE)]"));
        StringVector sdf = (StringVector)engine.eval("names(x)[(x==TRUE)]");
        
        return sdf;
    }
    
    public ArrayList<DataFrame> getDfSelected(String[] sel_names, String pre, String su) throws ScriptException, DataFrame.DataFrameException {
        int d=0;
        prefix = pre;
        suffix = su;
        for (String sel_name : sel_names){
            for (String name : df_names.toArray()) {
                if (sel_name.equals(name)){
                    String titledf = name;
                    if (prefix!=null) titledf=prefix+name;
                    if (suffix!=null) titledf=name+suffix;
                    DataFrame dataf = new DataFrame(titledf);
                    
                    ListVector df = (ListVector)engine.eval(name);

                    for(int j=0; j < df.length();j++){
                        SEXP var = df.getElementAsSEXP(j);
                        if(var.isNumeric()){
                            String varname = df.getName(j);
                            DoubleVector dv = (DoubleVector)engine.eval("as.double(" + name + "[['" + df.getName(j) + "']])");
                            double[] vardouble = new double[dv.length()];
                            int i=0;
                            for(double v :dv.toDoubleArray()){
                                vardouble[i]=v;
                                i++;
                            }
                            Variable vardf = new Variable(varname,vardouble);
                            Variable vardfin = dataf.add(vardf);
                        }else{
                            String varname = df.getName(j);
                            StringVector sv = (StringVector)engine.eval("as.character(" + name + "[['" + df.getName(j) + "']])");
                            String[] varstring = new String[sv.length()];
                            int i=0;
                            for(String v : sv.toArray()){
                                varstring[i]=v;
                                i++;
                            }
                            Variable vardf = new Variable(varname,varstring);
                            Variable vardfin = dataf.add(vardf);
                        }
                    }
                    sel_dfs.add(dataf);
                    d++;
                }
            }
        }
        return sel_dfs;
    }
    
}
