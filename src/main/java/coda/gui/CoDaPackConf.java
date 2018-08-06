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

package coda.gui;

import coda.ext.json.JSONException;
import coda.ext.json.JSONObject;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Versioning history
 * 
 * 2 01 16
 * - Text in figures and other objects can be resized from menu File-> Configuration at tab Sizes.
 * - Exportation to R has been modified. Now, CoDaPack generates a RData file instead of a source code.
 * 
 * 2 01 15
 * 
 * 
 * 2 01 14
 * - Decimal point configuration available through File->Configuration menu
 * - MAC version: Save to R menu (problem solved)
 * 
 * 2 01 13
 * - Sorry, the update 2 01 12 was not correctly integrated in CoDaPack. This update correctly integrates the previous one
 * 
 * 2 01 12
 * - The user can select the fraction to be used in the zero replacement menu
 * 
 * 2 01 11
 *  - Problem when exporting variables with non-regular characters to R  solved
 * 
 * 2 01 10
 * - Amalgamation problems solved
 * - Export tables to R data-frames as source code
 * - Text file importation
 * - CRITICAL: CLR variances in Variation Array corrected
 * 
 * 2 01 9
 * - Datasets without headers importation problems solved
 * - In graphics: Now it is possible to associate a variable as observation name
 * - In graphics: Show/Hide all observation labels automatically
 *
 * 2 01 8
 * - Added Excel formula evaluation capabilities
 * - Order inside groups in the manually partition table
 * - Dendrogram: Counting from 1 in the ilr generated variables
 *
 * 2 01 7
 * - Predictive region estimation
 * - Confidence region for the mean
 * - Empty Ternary Plot Simplistic Editor
 * - Biplot: covariance biplot as default biplot representation
 *
 * 2 01 6
 * - Correlation matrix calculation in Classical Statistics
 * - Biplot: representation of UV decomposition
 * - Biplot: coordinates output
 *
 * 2 01 5
 *
 * - Update protocol improved
 * - IMPORTANT: xlsx files compatibilities removed
 * - Atipicality index calculation implemented
 * - Simple color menu editor for plots
 * - Add numeric variables menu allows tabs and spaces in variable names
 * - Number of groups on plots unlimited
 *
 *
 * @author mcomas
 */
public class CoDaPackConf {
    /*
     * Decimal configuration output
     * 
     * This object keeps the last version
     */
    public static String HTTP_ROOT = "http://ima.udg.edu/codapack/versioning/";
    //public static String HTTP_ROOT = "http://mcomas.net/codapack/versioning/";


    public static String CoDaVersion = "2 02 21";
    public static int CoDaUpdaterVersion = 4;

    public static int[] getVersionNum(String version_str){
        int num[] = new int[3];
        String[] version = version_str.split(" ");
        num[0] = Integer.parseInt(version[0]);
        num[1] = Integer.parseInt(version[1]);
        num[2] = Integer.parseInt(version[2]);
        return num;
    }
    public static String getVersion(){
        String[] version = CoDaVersion.split(" ");
        return version[0] + "." + version[1] + "." + version[2];
    }
    public static String newest(String v1, String v2){
        String[] version1 = v1.split(" ");
        String[] version2 = v2.split(" ");
        
        int iversion1, iversion2;
        
        for(int s=0;s<3;s++){
            iversion1 = Integer.parseInt(version1[s]);
            iversion2 = Integer.parseInt(version2[s]);
            if(iversion1 > iversion2){
                return v1;
            }
            if(iversion1 < iversion2){
                return v2;
            } 
        }
        return v1;
    }
    public static boolean updateNeeded(String v){
        int[] actVersion = getVersionNum(CoDaPackConf.refusedVersion);
        int [] newVersion = new int[3];
        
        String[] version = v.split(" ");
        newVersion[0] = Integer.parseInt(version[0]);
        newVersion[1] = Integer.parseInt(version[1]);
        newVersion[2] = Integer.parseInt(version[2]);
        
        // Same version
        if(newVersion[0] == actVersion[0] & newVersion[1] == actVersion[1] &
                newVersion[2] == actVersion[2]) return false;
        
        // New version
        if(newVersion[0] == actVersion[0] & newVersion[1] == actVersion[1] &
                newVersion[2] > actVersion[2]) return true;
        if(newVersion[0] == actVersion[0] & newVersion[1] > actVersion[1]) return true;
        if(newVersion[0] > actVersion[0]) return true;
        
        // Otherwise actual version is newer than published version
        return false;
    }
    private final static DecimalFormatSymbols decimalFormatSymb = new DecimalFormatSymbols();
    static{
        decimalFormatSymb.setDecimalSeparator('.');
    }
    public static char decimalFormat = '.';
    public static String decimalOutputFormat = "0.0000";
    public static String decimalTableFormat = "0.00";
    public static String decimalExportFormat = "0.00########";
    public static String closureTo = "1.0";
    public static String lastPath = System.getProperty("user.dir");
    public static String refusedVersion = CoDaVersion;
    //private static DecimalFormat decimalOutputFormat = new DecimalFormat("0.0000", decimalFormat);
    //private static DecimalFormat decimalOutputFormat = new DecimalFormat("##0.##E0", decimalFormat);
    //private static DecimalFormat decimalTableFormat = new DecimalFormat("0.00", decimalFormat);
    //private static DecimalFormat decimalTableFormat = new DecimalFormat("##0.##E0", decimalFormat);
    //private static DecimalFormat decimalExportFormat = new DecimalFormat("0.00########");
    
    public static void setClosureTo(String closure){
        closureTo = closure;
    }
    
    public String getClosureTo(){
        return closureTo;
    }

    public static void setDecimalFormat(char f){
        decimalFormat = f;
        decimalFormatSymb.setDecimalSeparator(f);
    }
    public static char getDecimalFormat(){
        return decimalFormat;
    }
    public static void setDecimalOutputFormat(String df){
        decimalOutputFormat = df;
    }
    public static DecimalFormat getDecimalOutputFormat(){ 
        return new DecimalFormat(decimalOutputFormat, decimalFormatSymb);
    }
    public static void setDecimalTableFormat(String df){
        decimalTableFormat = df;
    }
    public static DecimalFormat getDecimalTableFormat(){
        return new DecimalFormat(decimalTableFormat, decimalFormatSymb);
    }
    public static void setDecimalExportFormat(String df){
        decimalExportFormat = df;
    }
    public static DecimalFormat getDecimalExportFormat(){ 
        return new DecimalFormat(decimalExportFormat);
    }

    private static Color ouputColor = new Color(162,193,215);
    public static void setOutputColor(Color c){ ouputColor = c; }
    public static Color getOutputColor(){ return ouputColor; }

    public static String configurationFile = ".codapack";
    public static void saveConfiguration(){
        try {
            JSONObject configuration = new JSONObject();
            configuration.put("decimal-format", decimalFormat);
            configuration.put("decimal-output", decimalOutputFormat);
            configuration.put("decimal-table", decimalTableFormat);
            configuration.put("decimal-export", decimalExportFormat);
            configuration.put("last-path", lastPath);
            configuration.put("refused-version", refusedVersion);
            configuration.put("closure-to", closureTo);

            PrintWriter printer = new PrintWriter(CoDaPackConf.configurationFile);

            configuration.write(printer);
            printer.flush();
            printer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CoDaPackConf.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(CoDaPackConf.class.getName()).log(Level.SEVERE, null, ex);
        }/* catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CoDaPackConf.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    public static void loadConfiguration(){
        try {
            FileReader file = new FileReader(CoDaPackConf.configurationFile);
            JSONObject configuration = new JSONObject(new BufferedReader(file).readLine());
            file.close();
            closureTo = configuration.getString("closure-to");
            decimalFormat = (char) configuration.getInt("decimal-format");
            decimalOutputFormat = configuration.getString("decimal-output");
            decimalTableFormat = configuration.getString("decimal-table");
            decimalExportFormat = configuration.getString("decimal-export");
            lastPath = configuration.getString("last-path");
            refusedVersion = configuration.getString("refused-version");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CoDaPackConf.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CoDaPackConf.class.getName()).log(Level.SEVERE, null, ex);
        }catch (JSONException ex) {
            Logger.getLogger(CoDaPackConf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
