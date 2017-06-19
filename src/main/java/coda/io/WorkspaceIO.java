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
import coda.ext.json.JSONArray;
import coda.ext.json.JSONException;
import coda.ext.json.JSONObject;
import coda.gui.CoDaPackMain;
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
    public static void saveWorkspace(String fname, CoDaPackMain application) throws JSONException{
        JSONObject configuration = new JSONObject();
        JSONArray dataFrames = new JSONArray();
       
        ArrayList<DataFrame> df = application.getAllDataFrames();
        for(int i=0;i<df.size();i++){
            dataFrames.put(df.get(i).toJSON());
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
    public static void openWorkspace(String fname, CoDaPackMain application){
        FileReader file = null;
        try {
            JSONObject configuration;
            file = new FileReader(fname);
            BufferedReader br = new BufferedReader(file);
            configuration = new JSONObject(br.readLine());
            file.close();

            JSONArray dataFrames = configuration.getJSONArray("dataframes");
            for(int i=0;i<dataFrames.length();i++)
                application.addDataFrame(
                        DataFrame.createFromJSON(dataFrames.getJSONObject(i)));
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(WorkspaceIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
