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
 * - Export tables to R dataframes as source code
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
 * - Preditive region estimation
 * - Confidence region for the mean
 * - Empty Ternary Plot Simplistic Editor
 * - Biplot: covariance biplot as default biplot representation
 *
 * 2 01 6
 * - Correlation matrix calculation in Clasical Statistics
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


    public static String CoDaVersion = "2 01 10";
    public static int CoDaUpdaterVersion = 4;

    public static int[] getVersionNum(){
        int num[] = new int[3];
        String[] version = CoDaVersion.split(" ");
        num[0] = Integer.parseInt(version[0]);
        num[1] = Integer.parseInt(version[1]);
        num[2] = Integer.parseInt(version[2]);
        return num;
    }
    public static String getVersion(){
        String[] version = CoDaVersion.split(" ");
        return version[0] + "." + version[1] + "." + version[2];
    }
    public static boolean updateNeeded(String v){
        int[] actVersion = getVersionNum();

        String[] version = v.split(" ");
        boolean b = false;
        if(actVersion[0] < Integer.parseInt(version[0])) b = true;
        else if (actVersion[1] < Integer.parseInt(version[1])) b = true;
        else if (actVersion[2] < Integer.parseInt(version[2])) b = true;

        return b;
    }
    private final static DecimalFormatSymbols decimalFormatSymb = new DecimalFormatSymbols();
    static{
        decimalFormatSymb.setDecimalSeparator('.');
    }
    public static char decimalFormat = '.';
    public static String decimalOutputFormat = "0.0000";
    public static String decimalTableFormat = "0.00";
    public static String decimalExportFormat = "0.00########";
    //private static DecimalFormat decimalOutputFormat = new DecimalFormat("0.0000", decimalFormat);
    //private static DecimalFormat decimalOutputFormat = new DecimalFormat("##0.##E0", decimalFormat);
    //private static DecimalFormat decimalTableFormat = new DecimalFormat("0.00", decimalFormat);
    //private static DecimalFormat decimalTableFormat = new DecimalFormat("##0.##E0", decimalFormat);
    //private static DecimalFormat decimalExportFormat = new DecimalFormat("0.00########");

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

    public static void saveConfiguration(){
        try {
            JSONObject configuration = new JSONObject();
            configuration.put("codapack-version", CoDaVersion);
            configuration.put("codapack-updater-version", CoDaUpdaterVersion);
            configuration.put("decimal-format", decimalFormat);
            configuration.put("decimal-output", decimalOutputFormat);
            configuration.put("decimal-table", decimalTableFormat);
            configuration.put("decimal-export", decimalExportFormat);

            PrintWriter printer = new PrintWriter("codapack.conf");

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
            FileReader file = new FileReader("codapack.conf");
            JSONObject configuration = new JSONObject(new BufferedReader(file).readLine());
            file.close();

            CoDaVersion = configuration.getString("codapack-version");
            CoDaUpdaterVersion = configuration.getInt("codapack-updater-version");

            decimalFormat = (char) configuration.getInt("decimal-format");
            decimalOutputFormat = configuration.getString("decimal-output");
            decimalTableFormat = configuration.getString("decimal-table");
            decimalExportFormat = configuration.getString("decimal-export");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CoDaPackConf.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CoDaPackConf.class.getName()).log(Level.SEVERE, null, ex);
        }catch (JSONException ex) {
            Logger.getLogger(CoDaPackConf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
