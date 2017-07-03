/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió
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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.io;

import coda.DataFrame;
import coda.Element;
import coda.Numeric;
import coda.Text;
import coda.Variable;
import coda.Workspace;
import coda.Zero;
import coda.ext.json.JSONArray;
import coda.ext.json.JSONException;
import coda.ext.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marc
 */
public class WorkspaceIO {
    public static void saveWorkspace(String fname, ArrayList<DataFrame> df) throws JSONException{
        JSONObject configuration = new JSONObject();
        JSONArray dataFrames = new JSONArray();
       
        for(int i=0;i<df.size();i++){
            dataFrames.put(DataFrametoJSON(df.get(i)));
        }
        configuration.put("dataframes",dataFrames);
        try {
            PrintWriter printer = new PrintWriter(fname);

            configuration.write(printer);
            printer.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Error writting");
        }
    }
    public static Workspace openWorkspace(String fname){
        Workspace workspace = new Workspace();
        FileReader file = null;
        try {
            JSONObject configuration;
            file = new FileReader(fname);
            BufferedReader br = new BufferedReader(file);
            configuration = new JSONObject(br.readLine());
            file.close();

            JSONArray dataFrames = configuration.getJSONArray("dataframes");
            for(int i=0;i<dataFrames.length();i++){
                DataFrame df = JSONtoDataFrame(dataFrames.getJSONObject(i));
                workspace.addDataFrame(df);
            }
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(WorkspaceIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return workspace;
    }
    public static JSONObject DataFrametoJSON(DataFrame df){
        JSONObject dataFrame = new JSONObject();
        JSONArray variables = new JSONArray();
        try {
            dataFrame.put("name", df.name);
            for(int i=0;i<df.nVariables(); i++){
                variables.put(VariabletoJSON(df.get(df.getNames().get(i))));
            }
            dataFrame.put("variables", variables);
            dataFrame.put("number_rows", df.nObservations());
        } catch (JSONException ex) {
            Logger.getLogger(DataFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataFrame;
    }
    public static DataFrame JSONtoDataFrame(JSONObject df){
        DataFrame dataFrame = new DataFrame();
        try {
            dataFrame.name = df.getString("name");
            JSONArray variables = df.getJSONArray("variables");
            Variable variable;
            for(int i=0;i<variables.length();i++){
                variable = JSONtoVariable(variables.getJSONObject(i));
                dataFrame.getNames().add(i, variable.getName());
                dataFrame.add(variable);
            }
        } catch (JSONException ex) {
            Logger.getLogger(DataFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataFrame;
    }
    public static JSONObject VariabletoJSON(Variable var){
        JSONObject variable = new JSONObject();
        JSONArray values = new JSONArray();

        try {
            variable.put("n", var.getName());
            variable.put("t", var.getType());
            if( var.getType() == var.VAR_NUMERIC ){               
                for(int i=0;i<var.size();i++){
                    JSONObject obj = new JSONObject();
                    Element el = var.get(i);
                    if( el instanceof Zero){
                        Zero zero = (Zero)el;
                        obj.put("l", zero.detection);
                    }else if( el instanceof Numeric){
                        Numeric el_n = (Numeric)el;
                        obj.put("v", el_n.getValue().toString());
                        /*
                        if(value.isNaN()){
                            obj.put("value", "NaN");
                        }else{
                            obj.put("value", value);
                        }
                         * */
                    }
                    values.put(obj);
                }
            }else{
                for(int i=0;i<var.size();i++){
                    Text el = (Text)var.get(i);
                    values.put(el.getValue());
                }
            }
            variable.put("a", values);
        } catch (JSONException ex) {
            Logger.getLogger(Variable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return variable;
    }
    public static Variable JSONtoVariable(JSONObject var){
        Variable variable = new Variable();
        try {
            variable.setName(var.getString("n"));
            variable.setType(var.getInt("t"));
            JSONArray values = var.getJSONArray("a");
            if(variable.getType() == Variable.VAR_NUMERIC){
                for(int i=0;i<values.length();i++){
                    JSONObject object = values.getJSONObject(i);
                    if(object.has("l")){
                        variable.add(i, new Zero(Double.parseDouble(object.getString("l"))));//Double.parseDouble(object.getString("value")));
                    }else{
                        variable.add(i, new Numeric(Double.parseDouble(object.getString("v"))));
                    }
                }
            }else{
                for(int i=0;i<values.length();i++){
                    variable.add(i, new Text(values.getString(i)));
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(Variable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return variable;
    }
}
