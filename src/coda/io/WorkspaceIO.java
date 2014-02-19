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
