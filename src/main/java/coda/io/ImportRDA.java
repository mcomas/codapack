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
import java.util.Arrays;
import java.util.Vector;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFileChooser;
import org.renjin.sexp.DoubleVector;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.SEXP;
import org.renjin.sexp.StringVector;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author david
 */
public class ImportRDA {
    
    Rengine re;
    ScriptEngineManager manager;
    ScriptEngine engine;
    
    String fname;
    StringVector df_names;
    //Creem la llista de dataFrames que contindrà els data frames seleccionats
    ArrayList<DataFrame> sel_dfs = new ArrayList<DataFrame>();
    JFileChooser cf;
    String prefix = null;
    String suffix = null;

    //El constructor
    public ImportRDA(JFileChooser chooseFile, Rengine r) throws ScriptException{
        manager = new ScriptEngineManager(); //Static ?
        engine = manager.getEngineByName("Renjin");
        re = r;
        cf = chooseFile;
        df_names = getDataFramesNames(chooseFile.getSelectedFile().getAbsolutePath().replace("\\","/"));
        
    }
    
    private void resaveFileVersion2(String fileName){
        re.eval("rm(list = ls())");
        re.eval("load('" + fileName + "')");
        re.eval("save(list = ls(), file = '" + fileName + "', version = 2)");
        re.eval("rm(list = ls())");
    }

    //Obté el nom dels dataframes que conté l'arxiu filename i el retorna
    public StringVector getDataFramesNames(String filename) throws ScriptException{
        if(re == null) {
            throw new RuntimeException("Renjin Script Engine not found on the classpath.");
        }
        fname = filename;
        
        resaveFileVersion2(fname);
        
        engine.eval("load('" + fname + "')");
        engine.eval("CDP_nms = ls()");
        engine.eval("CDP_x = sapply(lapply(CDP_nms, get), is.data.frame)");
        StringVector sdf = (StringVector)engine.eval("CDP_nms[CDP_x==TRUE]");
        
        return sdf;
    }

    //Aquest mètode és l'encarregat d'obrir els dataframes seleccionats
    public ArrayList<DataFrame> getDfSelected(String[] sel_names, String pre, String su) throws DataFrame.DataFrameException, ScriptException {
        int d=0;
        prefix = pre;
        suffix = su;
        for (String sel_name : sel_names){
            for (String name : df_names.toArray()) {
                if (sel_name.equals(name)){
                    String titledf = name;
                    if (prefix!=null && suffix!=null) titledf=prefix+name+suffix;
                    else if (prefix!=null) titledf=prefix+name;
                    else if (suffix!=null) titledf=name+suffix;
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
