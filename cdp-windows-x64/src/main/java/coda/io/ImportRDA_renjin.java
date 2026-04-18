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
import coda.DataFrame.DataFrameException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFileChooser;

import org.renjin.sexp.DoubleVector;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.SEXP;
import org.renjin.sexp.StringVector;

// import org.rosuda.JRI.REXP;
// import org.rosuda.JRI.RList;
//import org.renjin.sexp.DoubleVector;
//import org.renjin.sexp.ListVector;
//import org.renjin.sexp.SEXP;
//import org.renjin.sexp.StringVector;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author david
 */
public class ImportRDA_renjin implements Importer {
    
    ScriptEngineManager manager;
    ScriptEngine engine;
    
    String fname;
    public String[] df_names;
    //Creem la llista de dataFrames que contindrà els data frames seleccionats
    ArrayList<DataFrame> sel_dfs = new ArrayList<DataFrame>();
    String file_path = null;
    String prefix = null;
    String suffix = null;

    //El constructor
    public ImportRDA_renjin(String fpath) throws ScriptException{
        manager = new ScriptEngineManager(); //Static ?
        engine = manager.getEngineByName("Renjin");

        file_path = fpath.replace("\\", "/");
        //df_names = getDataFramesNames(fpath);
        //System.out.println(Arrays.toString(df_names));
        
    }
    public String[] getDataFramesNames(String filename) throws ScriptException{
        filename = filename.replace("\\", "/");
        engine.eval("etreball = new.env()");
        String E1 = "load('#PATH#', envir = etreball)";
        engine.eval(E1.replace("#PATH#", filename));
        engine.eval("CDP_nms = ls(envir = etreball)");
        engine.eval("CDP_x = sapply(lapply(CDP_nms, get, envir = etreball), is.data.frame)");
        String[] sdf = ((StringVector)engine.eval ("CDP_nms[CDP_x==TRUE]")).toArray();
        return sdf;
    }
    // private void resaveFileVersion2(String fileName,String tempFile){        

    //     // re.eval("load('"+ fileName + "', envir = etreball)");

    //     // re.eval("save(list = ls(envir = etreball), file = '" + tempFile + "', version = 2, envir = etreball)");

    // }
    @Override
    public DataFrame importDataFrame(String df_name) throws DataFrameException {
        DataFrame DF = new DataFrame();
        String E1 = "d <- get('#DFNAME#', envir = etreball)";
        try {
            engine.eval(E1.replace("#DFNAME#", df_name));
            ListVector df = (ListVector)engine.eval("d");
            // for(int i = 0; i < df.length(); i++){
            //     String vname = df.getName(i);
            //     System.out.println(df.getTypeName());
            // }
            for(int j=0; j < df.length();j++){
                SEXP var = df.getElementAsSEXP(j);
                if(var.isNumeric()){
                    String varname = df.getName(j);
                    DoubleVector dv = (DoubleVector)engine.eval("as.double(" + df_name + "[['" + df.getName(j) + "']])");
                    double[] vardouble = new double[dv.length()];
                    int i=0;
                    for(double v :dv.toDoubleArray()){
                        vardouble[i]=v;
                        i++;
                    }
                    Variable vardf = new Variable(varname,vardouble);
                    DF.add(vardf);
                }else{
                    String varname = df.getName(j);
                    StringVector sv = (StringVector)engine.eval("as.character(" + df_name + "[['" + df.getName(j) + "']])");
                    String[] varstring = new String[sv.length()];
                    int i=0;
                    for(String v : sv.toArray()){
                        varstring[i]=v;
                        i++;
                    }
                    Variable vardf = new Variable(varname,varstring);
                    DF.add(vardf);
                }
            }
        } catch (ScriptException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return(DF);

    }
    // //Aquest mètode és l'encarregat d'obrir els dataframes seleccionats
    // public ArrayList<DataFrame> getDfSelected(String[] sel_names, String pre, String su) throws DataFrame.DataFrameException, ScriptException {
    //     int d=0;
    //     prefix = pre;
    //     suffix = su;
    //     for (String sel_name : sel_names){
    //         for (String name : df_names) {
    //             if (sel_name.equals(name)){
    //                 String titledf = name;
    //                 if (prefix!=null && suffix!=null) titledf=prefix+name+suffix;
    //                 else if (prefix!=null) titledf=prefix+name;
    //                 else if (suffix!=null) titledf=name+suffix;
    @Override
    public String[] getAvailableDataFramesNames() {
        
        String E1 = "load('#PATH#', envir = etreball)";
        String[] sdf = null;
        try {
            engine.eval("etreball = new.env()");
            engine.eval(E1.replace("#PATH#", file_path));
            engine.eval("CDP_nms = ls(envir = etreball)");
            engine.eval("CDP_x = sapply(lapply(CDP_nms, get, envir = etreball), is.data.frame)");
            sdf = ((StringVector)engine.eval ("CDP_nms[CDP_x==TRUE]")).toArray();
        } catch (ScriptException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return sdf;
    }

    //                 DataFrame dataf = new DataFrame(titledf);
    //                 String E1 = "(d <- get('#DFNAME#', envir = etreball))";
    //                 RList df = re.eval(E1.replace("#DFNAME#", name)).asList();                
    //                 for(String j: df.keys()){
    //                     REXP var = df.at(j);
    //                     System.out.println(var.getType());
    //                     int itype = var.getType();
    //                     // https://www.rforge.net/org/doc/constant-values.html#org.rosuda.JRI.REXP.XT_NULL
    //                     if(itype == REXP.XT_ARRAY_INT ||      // 32
    //                        itype == REXP.XT_ARRAY_DOUBLE    // 33
    //                         ){ // numeric
    //                         String varname = j;
    //                         double[] vardouble = var.asDoubleArray();
    //                         System.out.println(vardouble.length);
    //                         Variable vardf = new Variable(varname,vardouble);
    //                         Variable vardfin = dataf.add(vardf);
    //                     }
    //                     if(itype == REXP.XT_ARRAY_BOOL_INT ||  // 37
    //                        itype == REXP.XT_ARRAY_BOOL){       // 36
    //                         String varname = j;
    //                         double[] varinteger = Arrays.stream(var.asIntArray()).asDoubleStream().toArray();
    //                         Variable vardf = new Variable(varname,varinteger);
    //                         Variable vardfin = dataf.add(vardf);
    //                     }
    //                     if(itype == REXP.XT_ARRAY_STR){
    //                         String varname = j;
    //                         String[] varstring = var.asStringArray();
                            
    //                         System.out.println(varstring.length);
    //                         Variable vardf = new Variable(varname,varstring);
    //                         Variable vardfin = dataf.add(vardf);
                           
                            
    //                     }
    //                     if(itype == REXP.XT_FACTOR){
    //                         String varname = j;
    //                         String[] varstring = re.eval("as.character(d[['" + varname + "']])").asStringArray();
    //                         System.out.println(varstring.length);
    //                         Variable vardf = new Variable(varname,varstring);
    //                         Variable vardfin = dataf.add(vardf);
    //                     }
                        
    //                 }
    //                 sel_dfs.add(dataf);
                  
    //             }
    //         }
    //     }
    //     return sel_dfs;
    // }
    
}
