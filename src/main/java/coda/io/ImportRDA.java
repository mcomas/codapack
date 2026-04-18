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

import java.util.Arrays;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RList;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author david
 */
public class ImportRDA implements Importer{
    
    Rengine re;
    //ScriptEngineManager manager;
    //ScriptEngine engine;
    
    String fname;
    public String[] df_names;
    //Creem la llista de dataFrames que contindrà els data frames seleccionats
    // ArrayList<DataFrame> sel_dfs = new ArrayList<DataFrame>();
    String prefix = null;
    String suffix = null;

    //El constructor
    public ImportRDA(String fpath, Rengine r){
        re = r;        
        fname = fpath.replace("\\", "/");        
    }
    // Quote every file path and object name before embedding it in R code so
    // import still works with spaces or special characters.
    private String quote(String value){
        return REXP.quoteString(value);
    }
    public String[] getDataFramesNames(String filename){
            
        re.eval("etreball = new.env()");
        re.eval("load(" + quote(filename) + ", envir = etreball)");
        re.eval("CDP_nms = ls(envir = etreball)");
        re.eval("CDP_x = sapply(lapply(CDP_nms, get, envir = etreball), is.data.frame)");
        String[] sdf = re.eval("CDP_nms[CDP_x==TRUE]").asStringArray();        
        return sdf;
    }
    private void resaveFileVersion2(String fileName,String tempFile){        

        re.eval("load(" + quote(fileName) + ", envir = etreball)");

        re.eval("save(list = ls(envir = etreball), file = " + quote(tempFile) + ", version = 2, envir = etreball)");

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
    @Override
    public String[] getAvailableDataFramesNames() {
        re.eval("etreball = new.env()");
        re.eval("load(" + quote(fname) + ", envir = etreball)");
        re.eval("CDP_nms = ls(envir = etreball)");
        re.eval("CDP_x = sapply(lapply(CDP_nms, get, envir = etreball), is.data.frame)");
        String[] sdf = re.eval("CDP_nms[CDP_x==TRUE]").asStringArray();        
        return sdf;    
    }

    @Override
    public DataFrame importDataFrame(String df_name) throws DataFrameException {
        DataFrame dataf = new DataFrame();
        RList df = re.eval("(d <- get(" + quote(df_name) + ", envir = etreball))").asList();
        for(String j: df.keys()){
            REXP var = df.at(j);
            int itype = var.getType();
            if(itype == REXP.XT_ARRAY_INT ||      // 32
                itype == REXP.XT_ARRAY_DOUBLE    // 33
                ){ // numeric
                String varname = j;
                double[] vardouble = var.asDoubleArray();
                Variable vardf = new Variable(varname,vardouble);
                dataf.add(vardf);
            }
            if(itype == REXP.XT_ARRAY_BOOL_INT ||  // 37
                itype == REXP.XT_ARRAY_BOOL){       // 36
                String varname = j;
                double[] varinteger = Arrays.stream(var.asIntArray()).asDoubleStream().toArray();
                Variable vardf = new Variable(varname,varinteger);
                dataf.add(vardf);
            }
            if(itype == REXP.XT_ARRAY_STR){
                String varname = j;
                re.eval(".v = d[[" + quote(varname) + "]]");
                re.eval(".v[is.na(.v)] = 'na'");
                String[] varstring = re.eval(".v").asStringArray();
                Variable vardf = new Variable(varname,varstring);
                dataf.add(vardf);
                
            }
            if(itype == REXP.XT_FACTOR){
                String varname = j;
                re.eval(".v = as.character(d[[" + quote(varname) + "]])");
                re.eval(".v[is.na(.v)] = 'na'");
                String[] varstring = re.eval(".v").asStringArray();
                Variable vardf = new Variable(varname,varstring);
                dataf.add(vardf);
            }
            
        }
        re.eval("rm(d, .v)");
        return dataf;
    }
    
}
