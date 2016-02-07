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
import coda.DataFrame.DataFrameException;
import coda.Variable;
import coda.Zero;
import coda.ext.json.JSONArray;
import coda.ext.json.JSONException;
import coda.ext.json.JSONObject;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author marc
 */
public class CoDaPackImporter implements Importer{
    String fname;
    String ruta=fillRecentPath();
    ArrayList<DataFrame> dfs = new ArrayList<DataFrame>();
    @Override
    public CoDaPackImporter setParameters(Component frame) {
        JFileChooser chooseFile = new JFileChooser(ruta);
        chooseFile.setFileFilter(
                    new FileNameExtensionFilter("CoDaPack files", "cdp"));
        chooseFile.showOpenDialog(frame);
        fname = chooseFile.getSelectedFile().getAbsolutePath();
        System.out.println("El fname del arxiu és: "+fname);
        ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
        System.out.println("La ruta del arxiu és: "+ruta);
        copyRecentPath(ruta);
        return this;
    }
    Variable readVariable(JSONObject var){
        Variable variable = new Variable();
        try {
            variable.setName( var.getString("n") );
            int dtype = var.getInt("t");
            JSONArray values = var.getJSONArray("a");
            if(dtype == Variable.VAR_NUMERIC){ // VAR_NUMERIC 2
                variable.setType(Variable.VAR_NUMERIC);
                for(int i=0;i<values.length();i++){
                    JSONObject object = values.getJSONObject(i);
                    if(object.has("l")){
                        variable.add(new Zero(Double.parseDouble(object.getString("l"))));//Double.parseDouble(object.getString("value")));
                    }else{
                        variable.add(variable.setElementFromString(object.getString("v")));
                    }
                }
                
            }else{
                variable.setType(Variable.VAR_TEXT);
                for(int i=0;i<values.length();i++){
                    variable.add(variable.setElementFromString(values.getString(i)));
                }
                
            }
        } catch (JSONException ex) {
            Logger.getLogger(Variable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return variable;
    }
    DataFrame readDataFrame(JSONObject df){
        DataFrame dataFrame = new DataFrame();
        try {
            dataFrame.setName(df.getString("name"));
            JSONArray variables = df.getJSONArray("variables");
            Variable variable;
            for(int i=0;i<variables.length();i++){
                variable = readVariable(variables.getJSONObject(i));
                dataFrame.add(variable);
                
            }
        } catch (JSONException ex) {
            Logger.getLogger(DataFrame.class.getName()).log(Level.SEVERE, null, ex);
        }catch (DataFrameException ex) {
            Logger.getLogger(CoDaPackImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataFrame;
    }
    @Override
    public DataFrame importDataFrame() {
        int i = 0;
        FileReader file;
        DataFrame df = null;
        try {
            JSONObject configuration;
            file = new FileReader(fname);
            BufferedReader br = new BufferedReader(file);
            configuration = new JSONObject(br.readLine());
            file.close();

            JSONArray dataFrames = configuration.getJSONArray("dataframes");
            df = readDataFrame(dataFrames.getJSONObject(i));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "The file is not available");
        } catch (JSONException ex) {
        }
        return df;
    }
    public ArrayList<DataFrame> importDataFrames() {
        FileReader file;
        try {
            JSONObject configuration;
            file = new FileReader(fname);
            BufferedReader br = new BufferedReader(file);
            configuration = new JSONObject(br.readLine());
            file.close();

            JSONArray dataFrames = configuration.getJSONArray("dataframes");
            for(int i=0;i<dataFrames.length();i++)
                dfs.add(readDataFrame(dataFrames.getJSONObject(i)));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "The file is not available");
        } catch (JSONException ex) {
        }
        return dfs;
    }
    @Override
    public String getParameters() {
        String conf = "format:codapack";
         conf += "¿" + fname;
         return conf;
    }

    @Override
    public CoDaPackImporter setParameters(String pars) {
        String parameters[] = pars.split("¿");
        if("format:codapack".equals(parameters[0])) {
            fname = parameters[1];
            
        }
        return this;
    }
    public String fillRecentPath() {
        String path = null;
        File arx = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            arx = new File("recentPath.txt");
            fr = new FileReader(arx);
            br = new BufferedReader(fr);
            String linia;
            if ((linia=br.readLine())!=null) {
                path=linia;
            }
        }
        catch (Exception e) {
           e.printStackTrace(); 
        }
        finally {
            try {
                if (null != fr) fr.close();
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return path;
    }
    
    public void copyRecentPath(String path) {
        FileWriter fit = null;
        PrintWriter pw = null;
        try {
            fit = new FileWriter("recentPath.txt");
            pw = new PrintWriter(fit);
            pw.println(path);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (null!= fit) fit.close();
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    
}
