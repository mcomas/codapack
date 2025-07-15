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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mcomas
 */
public class CoDaPackConf {
    public static String HTTP_ROOT = "http://ima.udg.edu/codapack/versioning/";

    public static String CoDaVersion = "2 03 10 (2025)";

    public static int[] getVersionNum(String version_str) {
        int num[] = new int[3];
        String[] version = version_str.split(" ");
        num[0] = Integer.parseInt(version[0]);
        num[1] = Integer.parseInt(version[1]);
        num[2] = Integer.parseInt(version[2]);
        return num;
    }

    public static String getVersion() {
        String[] version = CoDaVersion.split(" ");
        return version[0] + "." + version[1] + "." + version[2];
    }

    public static String newestVersion(String v1, String v2) {
        String[] version1 = v1.split(" ");
        String[] version2 = v2.split(" ");

        int iversion1, iversion2;

        for (int s = 0; s < 3; s++) {
            iversion1 = Integer.parseInt(version1[s]);
            iversion2 = Integer.parseInt(version2[s]);
            if (iversion1 > iversion2) {
                return v1;
            }
            if (iversion1 < iversion2) {
                return v2;
            }
        }
        return v1;
    }

    public static boolean updateNeeded(String v) {
        int[] actVersion = getVersionNum(CoDaPackConf.refusedVersion);
        int[] newVersion = new int[3];

        String[] version = v.split(" ");
        newVersion[0] = Integer.parseInt(version[0]);
        newVersion[1] = Integer.parseInt(version[1]);
        newVersion[2] = Integer.parseInt(version[2]);

        // Same version
        if (newVersion[0] == actVersion[0] & newVersion[1] == actVersion[1] &
                newVersion[2] == actVersion[2])
            return false;

        // New version
        if (newVersion[0] == actVersion[0] & newVersion[1] == actVersion[1] &
                newVersion[2] > actVersion[2])
            return true;
        if (newVersion[0] == actVersion[0] & newVersion[1] > actVersion[1])
            return true;
        if (newVersion[0] > actVersion[0])
            return true;

        // Otherwise actual version is newer than published version
        return false;
    }

    private final static DecimalFormatSymbols decimalFormatSymb = new DecimalFormatSymbols();
    static {
        decimalFormatSymb.setDecimalSeparator('.');
    }
    public static char decimalFormat = '.';
    public static String decimalOutputFormat = "0.0000";
    public static String decimalTableFormat = "0.00";
    public static String decimalExportFormat = "0.00########";
    public static String closureTo = "1.0";
    public static boolean showDev = false;

    public static String temporalDir = System.getProperty("java.io.tmpdir");
    public static String tmpFile(String fname) {
        return (Paths.get(temporalDir, fname).toString());
    }

    public static String workingDir = System.getenv("CDP_WORKING_DIR"); //System.getProperty("user.dir");
    static{
        if(workingDir == null) workingDir = System.getProperty("user.dir");
        System.out.println(String.format("Working directory at: %s", workingDir));
    }

    public static String configurationFile = Paths.get(workingDir, ".codapack").toString();
    public static String recentFiles = Paths.get(workingDir, ".recent_files").toString();


    public static String resourcesDir = System.getenv("CDP_RESOURCES");
    static{
        if(resourcesDir == null) resourcesDir = ".";
        System.out.println(String.format("Resources directory at: %s", resourcesDir));
    }


    public static String refusedVersion = CoDaVersion;
    public static String rScriptPath = System.getenv("CDP_R_SCRIPTS");
    static{
        if(rScriptPath == null) Paths.get(resourcesDir, "Rscripts").toString();
        System.out.println(String.format("R scripts at: %s", rScriptPath));
    }

    public static String helpPath = Paths.get(resourcesDir, "Help").toString();
    
    //public static String mathJaxPath = getMathJaxPath();

    // private static DecimalFormat decimalOutputFormat = new
    // DecimalFormat("0.0000", decimalFormat);
    // private static DecimalFormat decimalOutputFormat = new
    // DecimalFormat("##0.##E0", decimalFormat);
    // private static DecimalFormat decimalTableFormat = new DecimalFormat("0.00",
    // decimalFormat);
    // private static DecimalFormat decimalTableFormat = new
    // DecimalFormat("##0.##E0", decimalFormat);
    // private static DecimalFormat decimalExportFormat = new
    // DecimalFormat("0.00########");

    /*public static String getRScriptDefaultPath() {
        String defaultRscriptPath = Paths.get(codapackDir, "Rscripts/").toString();
        System.out.println(defaultRscriptPath);
        // if (!System.getProperty("os.name").startsWith("Windows") &
        //         !System.getProperty("os.name").startsWith("Linux")) {
        //     defaultRscriptPath = System.getenv("SCRIPTS_DIRECTORY") + defaultRscriptPath;
        // }
        return defaultRscriptPath;
    }*/

    /*
    public static String getHelpPath() {
        String helpPath = Paths.get(codapackDir, "Help").toString();
        if (!System.getProperty("os.name").startsWith("Windows") &
                !System.getProperty("os.name").startsWith("Linux")) {
            helpPath = System.getenv("HELP_DIRECTORY") + helpPath;
        }
        return helpPath;
    }*/

    /*
    public static String getMathJaxPath() {

        if (System.getProperty("os.name").startsWith("Windows") || System.getProperty("os.name").startsWith("Linux")) {
            File mathJaxFile = new File("MathJax\\MathJax.js");
            return mathJaxFile.getAbsolutePath();
        } else {
            return System.getenv("MATHJAX_DIRECTORY") + "MathJax.js";
        }
    }
    */

    public static void setScriptsPath(String path) {
        rScriptPath = path + "/";
    }

    public static void setShowDev(boolean showMenu) {
        showDev = showMenu;
    }

    public static void setClosureTo(String closure) {
        closureTo = closure;
    }

    public String getClosureTo() {
        return closureTo;
    }

    public boolean getShowDev() {
        return showDev;
    }

    public static void setDecimalFormat(char f) {
        decimalFormat = f;
        decimalFormatSymb.setDecimalSeparator(f);
    }

    public static char getDecimalFormat() {
        return decimalFormat;
    }

    public static void setDecimalOutputFormat(String df) {
        decimalOutputFormat = df;
    }

    public static DecimalFormat getDecimalOutputFormat() {
        return new DecimalFormat(decimalOutputFormat, decimalFormatSymb);
    }

    public static void setDecimalTableFormat(String df) {
        decimalTableFormat = df;
    }

    public static DecimalFormat getDecimalTableFormat() {
        return new DecimalFormat(decimalTableFormat, decimalFormatSymb);
    }

    public static void setDecimalExportFormat(String df) {
        decimalExportFormat = df;
    }

    public static DecimalFormat getDecimalExportFormat() {
        return new DecimalFormat(decimalExportFormat);
    }

    private static Color ouputColor = new Color(162, 193, 215);

    public static void setOutputColor(Color c) {
        ouputColor = c;
    }

    public static Color getOutputColor() {
        return ouputColor;
    }

    

    public static void saveConfiguration() {
        try {
            JSONObject configuration = new JSONObject();
            configuration.put("decimal-format", decimalFormat);
            configuration.put("decimal-output", decimalOutputFormat);
            configuration.put("decimal-table", decimalTableFormat);
            configuration.put("decimal-export", decimalExportFormat);
            configuration.put("last-path", workingDir);
            configuration.put("refused-version", refusedVersion);
            configuration.put("closure-to", closureTo);
            configuration.put("menu-dev", showDev);
            configuration.put("r-script-path", rScriptPath);
            PrintWriter printer = new PrintWriter(CoDaPackConf.configurationFile);

            configuration.write(printer);
            printer.flush();
            printer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CoDaPackConf.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(CoDaPackConf.class.getName()).log(Level.SEVERE, null, ex);
        } /*
           * catch (UnsupportedEncodingException ex) {
           * Logger.getLogger(CoDaPackConf.class.getName()).log(Level.SEVERE, null, ex);
           * }
           */
    }

    public static void loadConfiguration() {
        try {
            FileReader file = new FileReader(CoDaPackConf.configurationFile);
            JSONObject configuration = new JSONObject(new BufferedReader(file).readLine());
            file.close();
            closureTo = configuration.getString("closure-to");
            showDev = configuration.getBoolean("menu-dev");
            decimalFormat = (char) configuration.getInt("decimal-format");
            decimalOutputFormat = configuration.getString("decimal-output");
            decimalTableFormat = configuration.getString("decimal-table");
            decimalExportFormat = configuration.getString("decimal-export");
            workingDir = configuration.getString("last-path");
            refusedVersion = configuration.getString("refused-version");
            rScriptPath = configuration.getString("r-script-path");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CoDaPackConf.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CoDaPackConf.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(CoDaPackConf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
