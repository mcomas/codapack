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
package coda.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.apache.commons.io.filefilter.RegexFileFilter;

import coda.ext.json.JSONArray;
import coda.ext.json.JSONException;
import coda.ext.json.JSONObject;
import coda.gui.menu.RBasedGenericMenu;
import coda.gui.utils.DataSelector;
import coda.gui.utils.DataSelector1to1;
import coda.gui.utils.DataSelector1to2;

/**
 *
 * @author marc
 */
public class CoDaPackMenu extends JMenuBar {

    String nomPersonalDirectory = "menus_personalitzables";

    public HashMap<String,String> item_key = new HashMap<>();

    public JMenu menuFile;
    public final String ITEM_FILE = "File";
    public JMenuItem itemOpen;
    public final String ITEM_OPEN = "Open Workspace";
    public JMenuItem itemAdd;
    public final String ITEM_ADD = "Add Workspace...";
    public JMenuItem itemSave;
    public final String ITEM_SAV = "Save Workspace...";
    public JMenuItem itemSaveWork;
    public final String ITEM_SAVE = "Save as...";
    public JMenu menuRecent;
    public final String ITEM_RECENT = "Recent Workspace";
    public JMenuItem itemClearRecent;
    public final String ITEM_CLEAR_RECENT = "Clear Items";
    public JMenuItem itemNewDF;
    public final String ITEM_NEWDF = "New DataFrame";
    public JMenu menuImport;
    public final String ITEM_IMPORT = "Import";
    public JMenuItem itemImportXLS;
    public final String ITEM_IMPORT_XLS = "Import XLS Data...";
    public JMenuItem itemImportCSV;
    public final String ITEM_IMPORT_CSV = "Import CSV/Text Data...";
    public JMenuItem itemImportRDA;
    public final String ITEM_IMPORT_RDA = "Import R Data...";
    public JMenu menuExport;
    public final String ITEM_EXPORT = "Export";
    public JMenuItem itemExportXLS;
    public final String ITEM_EXPORT_XLS = "Export Data to XLS...";
    public JMenuItem itemExportCSV;
    public final String ITEM_EXPORT_CSV = "Export CSV/Text Data...";
    public JMenuItem itemExportR;
    public final String ITEM_EXPORT_R = "Export Data to R Data...";
    public JMenuItem itemdelDataFrame;
    public final String ITEM_DEL_DATAFRAME = "Delete Table";
    public JMenuItem itemDeleteAllTables;
    public final String ITEM_DELETE_ALL_TABLES = "Delete All Tables";
    public JMenuItem itemClearOutputs;
    public final String ITEM_CLEAR_OUTPUTS = "Clear the output";

    public JMenuItem itemConfiguration;
    public final String ITEM_CONF = "Configuration";
    public JMenuItem itemQuit;
    public final String ITEM_QUIT = "Quit CoDaPack";

    CoDaPackMain mainApplication;

    public JMenu personalMenu;
    public final String ITEM_PERSONALMENU = "My Personal Menu";
    public JMenuItem itemModelCrearMenu;
    public final String ITEM_MODEL_CPM = "Crear Menu Personal";
    public JMenuItem itemModelImportarMenu;
    public final String ITEM_MODEL_IPM = "Importar Menu";
    public JMenuItem itemModelExportarMenu;
    public final String ITEM_MODEL_EPM = "Exportar Menu";
    public JMenu itemModelPM;
    public final String ITEM_MODEL_PM = "Personal Menu";

    public JMenu menuHelp;
    public final String ITEM_HELP = "Help";
    // public JMenuItem itemForceUpdate;
    // public final String ITEM_FORCE_UPDATE = "Check for Updates";
    public JMenuItem itemAbout;
    public final String ITEM_ABOUT = "About";
    public JMenuItem itemR_Test;
    public final String R_TEST = "Get R status";

    public JMenu menuDevelopment;
    public final String ITEM_DEVELOPMENT = "Development";
    public JMenuItem itemModelS0;
    public final String ITEM_MODEL_S0 = "Model S0";
    public JMenuItem itemModelS1;
    public final String ITEM_MODEL_S1 = "Model S1";
    public JMenuItem itemModelS2;
    public final String ITEM_MODEL_S2 = "Model S2";
    public JMenuItem itemModelS3;
    public final String ITEM_MODEL_S3 = "Model S3";
    public JMenuItem itemModelS4;
    public final String ITEM_MODEL_S4 = "Model S4";

    // public JMenuItem itemModelAddtoHTMLJavaScript;
    // public final String ITEM_MODEL_AddtoHTMLJavaScript = "Add to HTML JavaScript";


 


    private JMenuItem addJMenuItem(JMenu menu, JMenuItem item, String title) {
        // item.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I,
        // java.awt.event.InputEvent.CTRL_MASK));
        menu.add(item);
        item.setText(title);
        item.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMenuItem jMenuItem = (JMenuItem) evt.getSource();
                String title = jMenuItem.getText();
                for (CoDaPackMenuListener e : listeners) {
                    e.menuItemClicked(title);
                }
            }
        });
        return item;
    }

    public void saveLoadRecentFiles() {

    }

    HashMap<String, JMenuItem> recentFile;

    // Definició del LinkedHashMap que farà la gestió dels arxius recents
    LinkedHashMap<String, String> newRecentFile = new LinkedHashMap();

    // Afegeix o actualitza la llista d'arxius recents
    public void saveRecentFile(String rf) {
        String pathFile;
        // Si la te, li treiem l'inici al nom del path
        if (rf.startsWith("format:codapack?")) {
            pathFile = rf.substring(16);
        } else
            pathFile = rf;
        // Obtenim el nom del fitxer amb l'extensió, que utilitzarem com a clau
        String fname = new File(pathFile).getName();
        // Comprovem si el fitxer es troba al hashmap, si es troba l'eliminem
        if (newRecentFile.containsKey(pathFile)) {
            newRecentFile.remove(pathFile);
        }
        // Inserim clau valor al final del hashmap
        newRecentFile.put(pathFile, pathFile);
        // Actualitzada la llista anem a actualitzar el menu i l'acció pertinent
        loadRecentFiles();
    }

    // S'encarrega d'escriure el submenú Recent Files i definir l'acció pertinent
    public void loadRecentFiles() {
        // Borra tots els items del menú Recent Files
        menuRecent.removeAll();
        // Comprovem que hi hagi entrada per posar a Recent Files
        if (newRecentFile.size() > 0) {
            // Creem la colecció i l'iterador per a recorrer els valors del LinkedHashMap
            Collection c = newRecentFile.values();
            Iterator itr = c.iterator();
            String[] v = new String[newRecentFile.size()];
            int j = 0;
            // Fem el recorregut pertinent per posar els paths en l'array
            while (itr.hasNext()) {
                String s = (String) itr.next();
                v[j] = s;
                j++;
            }
            for (int i = v.length - 1; i >= 0; i--) {
                // Creem i afegim el valor a Recent Files
                JMenuItem item = new JMenuItem();
                menuRecent.add(item);
                // Tornem a posar el prefix al pathFile
                String s1 = "format:codapack?";
                final String rf = s1.concat(v[i]);
                // Posem el text al item
                item.setText(v[i]);
                // Definim l'acció a realitzar al clicar l'item
                item.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        JMenuItem jMenuItem = (JMenuItem) evt.getSource();
                        String title = jMenuItem.getText();
                        for (CoDaPackMenuListener e : listeners) {
                            e.menuItemClicked(rf);
                        }
                        active_path = title;
                    }
                });
            }
        }
        // Afegim un separador i l'item Clear Items a Recent Files
        menuRecent.addSeparator();
        addJMenuItem(menuRecent, itemClearRecent, ITEM_CLEAR_RECENT);
    }

    // Aquest mètode emplena el LinkedHashMap des de l'arxiu .recent_files cada
    // vegada que s'inicia el programa
    public void fillRecentFiles() {
        File arx = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            arx = new File(CoDaPackConf.recentFiles);
            arx.createNewFile();
            fr = new FileReader(arx);
            br = new BufferedReader(fr);
            String linia;
            while ((linia = br.readLine()) != null) {
                newRecentFile.put(linia, linia);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr)
                    fr.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    // Aquest mètode sobreescriu l'arxiu .recent_files, quan es tanca el programa i
    // cada vegada que es fa un Clear Items
    public void copyRecentFiles() {
        FileWriter fit = null;
        PrintWriter pw = null;
        try {
            fit = new FileWriter(CoDaPackConf.recentFiles);
            pw = new PrintWriter(fit);
            String s;
            Collection c = newRecentFile.values();
            Iterator itr = c.iterator();
            if (newRecentFile.size() > 20) {
                int i = newRecentFile.size() - 20;
                for (int j = 0; j < i; j++) {
                    itr.next();
                }
            }
            while (itr.hasNext()) {
                s = (String) itr.next();
                pw.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fit)
                    fit.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    // Neteja el menu Recent Files
    public void removeRecentFiles() {
        newRecentFile.clear();
        copyRecentFiles();
        loadRecentFiles();
    }
    public void fill_menu(JMenu super_menu, JSONArray json_array) throws JSONException{
        for(int i=0;i<json_array.length();i++){
            if(json_array.get(i) instanceof String){
                super_menu.addSeparator();
                continue;
            }
            JSONObject json_submenu = json_array.getJSONObject(i);
            String name = (String)json_submenu.keys().next();
            
            if(json_submenu.get(name) instanceof JSONObject){
                JSONObject json_obj = json_submenu.getJSONObject(name);
                String id = json_obj.getString("id");
                item_key.put(name, id);
                JMenuItem mi = new JMenuItem();
                boolean groups = false;
                boolean two_selectors = false;
                int variable_type = DataSelector.ONLY_NUMERIC;
                if(json_obj.has("code") && json_obj.getString("code").equals("r")){
                    if(!CoDaPackMain.R_available){
                        mi.setEnabled(false);
                    }
                    if(json_obj.has("script") & json_obj.has("controls")){
                        JSONArray json_controls = json_obj.getJSONArray("controls");
                        String Rscript = json_obj.getString("script");
                        System.out.println(json_controls.toString());
                        if(json_obj.has("options")){
                            ArrayList<String> loptions = new ArrayList<String>();
                            if(json_obj.get("options") instanceof String){
                                loptions.add(json_obj.getString("options"));
                            }else{
                                JSONArray options = json_obj.getJSONArray("options");
                                for(int j = 0; j<options.length(); j++) loptions.add(options.getString(j));
                            }
                            if(loptions.contains("groups")){
                                groups = true;
                            }
                            if(loptions.contains("2selectors")){
                                two_selectors = true;
                            }
                            if(loptions.contains("all_variables")){
                                variable_type = DataSelector.ALL_VARIABLES;
                            }
                            if(loptions.contains("only_categoric")){
                                variable_type = DataSelector.ONLY_CATEGORIC;
                            }
                        }
                        
                        if(two_selectors){
                            DataSelector1to2 ds12 = new DataSelector1to2(mainApplication.getActiveDataFrame(), groups, variable_type);
                            if(json_obj.has("textA")) ds12.setTextA(json_obj.getString("textA"));
                            if(json_obj.has("textB")) ds12.setTextB(json_obj.getString("textB"));
                            mainApplication.dynamicMenus.put(id, 
                                new RBasedGenericMenu(mainApplication, 
                                                    CoDaPackMain.re, 
                                                    name,
                                                    Rscript,
                                                    json_controls,
                                                    ds12));
                        }else{
                            mainApplication.dynamicMenus.put(id, 
                                new RBasedGenericMenu(mainApplication, 
                                                    CoDaPackMain.re, 
                                                    name,
                                                    Rscript,
                                                    json_controls,
                                                    new DataSelector1to1(mainApplication.getActiveDataFrame(), groups, variable_type)));
                        }
                        
                    }
                }
                addJMenuItem(super_menu, mi, name);  

                //System.out.println(name + "item menu");
            }else{
                JMenu sub_menu = new JMenu(name);
                fill_menu(sub_menu, json_submenu.getJSONArray(name));
                super_menu.add(sub_menu);
                
                //System.out.println(name + "Array");
            }
        }

    }
    public CoDaPackMenu(CoDaPackMain mainApp){

        mainApplication = mainApp;

        this.recentFile = new HashMap<String, JMenuItem>();

        

        menuFile = new JMenu();
        itemOpen = new JMenuItem();
        itemAdd = new JMenuItem();
        itemSave = new JMenuItem();
        itemSaveWork = new JMenuItem();
        menuRecent = new JMenu();
        itemClearRecent = new JMenuItem();
        itemNewDF = new JMenuItem();
        menuImport = new JMenu();
        itemImportCSV = new JMenuItem();
        itemImportXLS = new JMenuItem();
        itemImportRDA = new JMenuItem();
        menuExport = new JMenu();
        itemExportCSV = new JMenuItem();
        itemExportXLS = new JMenuItem();
        itemExportR = new JMenuItem();
        itemdelDataFrame = new JMenuItem();
        itemDeleteAllTables = new JMenuItem();
        itemClearOutputs = new JMenuItem();
        itemConfiguration = new JMenuItem();
        itemQuit = new JMenuItem();

        
        personalMenu = new JMenu();
        itemModelCrearMenu = new JMenuItem();
        itemModelImportarMenu = new JMenuItem();
        itemModelExportarMenu = new JMenuItem();
        itemModelPM = new JMenu();

        menuHelp = new JMenu();
        //itemForceUpdate = new JMenuItem();
        itemAbout = new JMenuItem();
        itemR_Test = new JMenuItem();
        if(!CoDaPackMain.R_available) itemR_Test.setEnabled(false);

        menuDevelopment = new JMenu();
        itemModelS0 = new JMenuItem();
        itemModelS1 = new JMenuItem();
        itemModelS2 = new JMenuItem();
        itemModelS3 = new JMenuItem();
        itemModelS4 = new JMenuItem();
        //itemModelAddtoHTMLJavaScript = new JMenuItem();


        menuFile.setText(ITEM_FILE);
        addJMenuItem(menuFile, itemOpen, ITEM_OPEN);
        addJMenuItem(menuFile, itemAdd, ITEM_ADD);
        addJMenuItem(menuFile, itemSave, ITEM_SAV);
        addJMenuItem(menuFile, itemSaveWork, ITEM_SAVE);
        menuRecent.setText(ITEM_RECENT);
        menuFile.add(menuRecent);
        fillRecentFiles();
        loadRecentFiles();

        menuFile.addSeparator();
        addJMenuItem(menuFile, itemdelDataFrame, ITEM_DEL_DATAFRAME);
        addJMenuItem(menuFile, itemDeleteAllTables, ITEM_DELETE_ALL_TABLES);
        addJMenuItem(menuFile, itemClearOutputs, ITEM_CLEAR_OUTPUTS);

        menuFile.addSeparator();
        menuImport.setText(ITEM_IMPORT);
        menuFile.add(menuImport);
        addJMenuItem(menuImport, itemImportCSV, ITEM_IMPORT_CSV);
        addJMenuItem(menuImport, itemImportXLS, ITEM_IMPORT_XLS);
        addJMenuItem(menuImport, itemImportRDA, ITEM_IMPORT_RDA);

        menuExport.setText(ITEM_EXPORT);
        menuFile.add(menuExport);
        addJMenuItem(menuExport, itemExportCSV, ITEM_EXPORT_CSV);
        addJMenuItem(menuExport, itemExportXLS, ITEM_EXPORT_XLS);
        addJMenuItem(menuExport, itemExportR, ITEM_EXPORT_R);

        menuFile.addSeparator();
        addJMenuItem(menuFile, itemConfiguration, ITEM_CONF);
        menuFile.addSeparator();
        addJMenuItem(menuFile, itemQuit, ITEM_QUIT);
        menuFile.addSeparator();
        add(menuFile);


        FileReader file;
        JSONObject codapack_menu = null;
        try {
            
            file = new FileReader("codapack_structure.json");
            BufferedReader br = new BufferedReader(file);
            codapack_menu = new JSONObject(br.readLine());
            file.close();
            //System.out.println("JSON read");
            //System.out.println(codapack_menu.toString(2));
            JSONArray json_array = codapack_menu.getJSONArray("CoDaPack Menu");
            for(int i = 0; i < json_array.length(); i++){
                JSONObject json_item = json_array.getJSONObject(i);
                String name_item = (String)json_item.keys().next();
                JMenu menu_item = new JMenu(name_item);
                fill_menu(menu_item, json_item.getJSONArray(name_item));
                add(menu_item);
            }
         
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "The file is not available");
        } catch (JSONException ex) {
            System.out.println(ex.getMessage());
        }        

        menuHelp.setText(ITEM_HELP);
        //addJMenuItem(menuHelp, itemForceUpdate, ITEM_FORCE_UPDATE);
        addJMenuItem(menuHelp, itemAbout, ITEM_ABOUT);
        addJMenuItem(menuHelp, itemR_Test, R_TEST);
        add(menuHelp);


/*
        personalMenu.setText(ITEM_PERSONALMENU);
        addJMenuItem(personalMenu, itemModelCrearMenu, ITEM_MODEL_CPM);
        addJMenuItem(personalMenu, itemModelImportarMenu, ITEM_MODEL_IPM);
        addJMenuItem(personalMenu, itemModelExportarMenu, ITEM_MODEL_EPM);
        // addJMenuItem(PersonalMenu,itemModelCrearMenu, ITEM_MODEL_CPM);
        itemModelPM.setText(ITEM_MODEL_PM);
        personalMenu.add(itemModelPM);
        crearPersonalMenu();
        for (int i = 0; i < PersonalMenuItems.size(); i++) {
            addJMenuItem(itemModelPM, PersonalMenuItems.get(i), NomsMenuItems.get(i));
        }
        add(personalMenu);
*/



        menuDevelopment.setText(ITEM_DEVELOPMENT);
        addJMenuItem(menuDevelopment, itemModelS0, ITEM_MODEL_S0);
        addJMenuItem(menuDevelopment, itemModelS1, ITEM_MODEL_S1);
        addJMenuItem(menuDevelopment, itemModelS2, ITEM_MODEL_S2);
        addJMenuItem(menuDevelopment, itemModelS3, ITEM_MODEL_S3);
        addJMenuItem(menuDevelopment, itemModelS4, ITEM_MODEL_S4);
        //addJMenuItem(menuDevelopment, itemModelAddtoHTMLJavaScript, ITEM_MODEL_AddtoHTMLJavaScript);

        /*
         * addJMenuItem(menuDevelopment,itemModelCrearMenu, ITEM_MODEL_CPM);
         * itemModelPM.setText(ITEM_MODEL_PM);
         * menuDevelopment.add(itemModelPM);
         * crearPersonalMenu();
         * for(int i= 0; i< PersonalMenuItems.size(); i++){
         * addJMenuItem(itemModelPM,PersonalMenuItems.get(i), NomsMenuItems.get(i));
         * }
         */

    }

    public void addDataFrame() {

    }

    /*
     * 
     * Delegating events
     */
    ArrayList<CoDaPackMenuListener> listeners = new ArrayList<CoDaPackMenuListener>();

    public void addCoDaPackMenuListener(CoDaPackMenuListener listener) {
        listeners.add(listener);
    }

    public interface CoDaPackMenuListener {
        public void menuItemClicked(String label);
    }

    public void activeDevMenu() {
        add(menuDevelopment);
        this.repaint();
        super.updateUI();
    }

    public void disableDevMenu() {
        remove(menuDevelopment);
        this.repaint();
        super.updateUI();
    }


       /*
     * public JMenuItem itemModelCrearMenu;
     * public final String ITEM_MODEL_CPM = "Crear Personal Menu";
     * public JMenu itemModelPM;
     * public final String ITEM_MODEL_PM = "Personal Menu";
     */

     ArrayList<JMenuItem> PersonalMenuItems = new ArrayList<JMenuItem>();
     ArrayList<String> NomsMenuItems = new ArrayList<String>();
 
     public String active_path = null;
 
     private void crearPersonalMenu() {
         try {
             File archivo = new File("./" + nomPersonalDirectory);
             // System.out.println("Current directory path using canonical path method :- " +
             // archivo);
             String canonicalPath = new File("./" + nomPersonalDirectory).getCanonicalPath();
             // System.out.println("Current directory path using canonical path method :- " +
             // canonicalPath);
 
             /*
              * String usingSystemProperty = System.getProperty("user.dir");
              * System.out.println("Current directory path using system property:- " +
              * usingSystemProperty);
              */
             if (!archivo.exists()) {
                 JOptionPane.showMessageDialog(null, "Configuration file does not exist!!");
 
             } else {
                 if (archivo.isFile()) {
                     JOptionPane.showMessageDialog(null, "El archiu " + canonicalPath + " es un archiu");
 
                 } else {
                     // ------------
 
                     // File dir = new File(".");
                     FileFilter fileFilter = new RegexFileFilter("^(.*?)");
                     File[] files = archivo.listFiles(fileFilter);
                     for (File file : files) {
                         String nomArchiu = file.toString().substring(nomPersonalDirectory.length() + 3);
 
                         JMenuItem JmenuItemAux = new JMenuItem();
                         PersonalMenuItems.add(JmenuItemAux);
                         NomsMenuItems.add(nomArchiu);
 
                     }
                     // ------------
 
                 }
             }
         } catch (IOException e) {
             System.out.println("IOException Occured" + e.getMessage());
         }
 
     }
}
