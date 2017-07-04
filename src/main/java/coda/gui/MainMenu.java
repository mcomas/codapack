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

import coda.Workspace;
import java.util.ArrayList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javax.swing.event.HyperlinkEvent;



/**
 *
 * @author marc
 */
public class MainMenu extends MenuBar{
    /*
     * Defining actions
     */
    public CoDaPackMain main;
    public Menu menuFile;
    public final static String ITEM_FILE = "File";
        public MenuItem itemOpen;
        public final static String ITEM_OPEN = "Open Workspace";
        public MenuItem itemAdd;
        public final static String ITEM_ADD = "Add Workspace...";
        public MenuItem itemSave;
        public final static String ITEM_SAV = "Save...";
        public MenuItem itemSaveWork;
        public final static String ITEM_SAVE = "Save Workspace...";
        public Menu menuRecent;
        public final static String ITEM_RECENT = "Recent Workspace";
            public MenuItem itemClearRecent;
            public final static String ITEM_CLEAR_RECENT = "Clear Items";
        public MenuItem itemNewDF;
        public final static String ITEM_NEWDF = "New DataFrame";
        public Menu menuImport;
        public final static String ITEM_IMPORT = "Import";
            public MenuItem itemImportXLS;
            public final static String ITEM_IMPORT_XLS = "Import XLS Data...";
            public MenuItem itemImportCSV;
            public final static String ITEM_IMPORT_CSV = "Import CSV/Text Data...";
            public MenuItem itemImportRDA;
            public final static String ITEM_IMPORT_RDA = "Import R Data...";
        public Menu menuExport;
        public final static String ITEM_EXPORT = "Export";
            public MenuItem itemExportXLS;
            public final static String ITEM_EXPORT_XLS = "Export Data to XLS...";
            public MenuItem itemExportR;
            public final static String ITEM_EXPORT_R = "Export Data to R Data...";
        public MenuItem itemdelDataFrame;
        public final static String ITEM_DEL_DATAFRAME = "Delete Table";
        public MenuItem itemConfiguration;
        public final static String ITEM_CONF = "Configuration";
        public MenuItem itemQuit;
        public final static String ITEM_QUIT = "Quit CoDaPack";

    public Menu menuData;
    public final static String ITEM_DATA = "Data";
        public Menu menuTransforms;
        public final static String ITEM_TRANS = "Transformations";
            public MenuItem itemTransformALR;
            public final static String ITEM_RAW_ALR = "ALR";
            public MenuItem itemTransformCLR;
            public final static String ITEM_RAW_CLR = "CLR";
            public MenuItem itemTransformRawILR;
            public final static String ITEM_T_RAW_ILR = "Raw-ILR";
            public MenuItem itemTransformILRRaw;
            public final static String ITEM_T_ILR_RAW = "ILR-Raw";
        public MenuItem itemCenter;
        public final static String ITEM_CENTER = "Centering";
        public MenuItem itemClosure;
        public final static String ITEM_CLOSURE = "Subcomposition/Closure";
        public MenuItem itemAmalgamation;
        public final static String ITEM_AMALGAM = "Amalgamation";
        public MenuItem itemPerturbate;
        public final static String ITEM_PERTURBATE = "Perturbation";
        public MenuItem itemPower;
        public final static String ITEM_POWER = "Power transformation";
        public MenuItem itemZeros;
        public final static String ITEM_ZEROS = "Rounded zero replacement";
        public MenuItem itemSetDetectionLimit;
        public final static String ITEM_SETDETECTION ="Set detection limit";
        public MenuItem itemCategorizeVariables;
        public final static String ITEM_CAT_VAR = "Numeric to categorical";
        public MenuItem itemNumerizeVariables;
        public final static String ITEM_NUM_VAR = "Categorical to Numeric";
        public MenuItem itemAddVariables;
        public final static String ITEM_ADD_VAR = "Add Numeric Variables";
        public MenuItem itemDeleteVariables;
        public final static String ITEM_DEL_VAR = "Delete variables";

    public Menu menuStatistics;
    public final static String ITEM_STATS = "Statistics";
        public MenuItem itemCompStatsSummary;
        public final static String ITEM_COMP_STATS_SUMMARY = "Compositional statistics summary";
        public MenuItem itemClasStatsSummary;
        public final static String ITEM_CLAS_STATS_SUMMARY = "Classical statistics summary";
        public MenuItem itemNormalityTest;
        public final static String ITEM_NORM_TEST = "Additive Logistic Normality Tests";
        public MenuItem itemAtipicalityIndex;
        public final static String ITEM_ATIP_INDEX = "Atipicality index";


    public Menu menuGraphs;
    public final static String ITEM_GRAPHS = "Graphs";
        public MenuItem itemTernaryPlot;
        public final static String ITEM_TERNARY_PLOT = "Ternary plot";
        public MenuItem itemEmptyTernaryPlot;
        public final static String ITEM_EMPTY_TERNARY_PLOT = "Ternary plot [Empty]";
        public MenuItem itemIlrBiPlot;
        public final static String ITEM_ILR_BIPLOT = "ILR/CLR plot";
        public MenuItem itemBiPlot;
        public final static String ITEM_BIPLOT = "CLR biplot";
        public MenuItem itemDendrogramPlot;
        public final static String ITEM_DENDROGRAM_PLOT = "Balance dendrogram";
        public MenuItem itemALRPlot;
        public final static String ITEM_ALR_PLOT = "ALR plot";
        public MenuItem itemCLRPlot;
        public final static String ITEM_CLR_PLOT = "CLR plot";
        public MenuItem itemILRPlot;
        public final static String ITEM_ILR_PLOT = "ILR plot";
        public MenuItem principalComponentPlot;
        public final static String ITEM_PC_PLOT = "Ternary Principal Components";
        public MenuItem predictiveRegionPlot;
        public final static String ITEM_PRED_REG_PLOT = "Predictive Region";
        public MenuItem confidenceRegionPlot;
        public final static String ITEM_CONF_REG_PLOT = "Center Confidence Region";

   public Menu menuHelp;
   public final static String ITEM_HELP = "Help";
        public MenuItem itemForceUpdate;
        public final static String ITEM_FORCE_UPDATE = "Check for Updates";
        public MenuItem itemAbout;
        public final static String ITEM_ABOUT = "About";

    public String active_path = null;
    
    private MenuItem addMenuItem(Menu menu, MenuItem item, String title){
        //item.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        menu.getItems().add(item);
        item.setText(title);
        item.setOnAction(new EventHandler(){
            @Override
            public void handle(Event event) {
                MenuItem menuItem = (MenuItem)event.getSource();
                String title = menuItem.getText();
                System.out.println(title);
                main.runMenuItem(title);
                //for(CoDaPackMenuListener e: listeners){
                //  e.menuItemClicked(title);
                //}
            }
        });
        return item;
    }
    
    public MainMenu(CoDaPackMain main){
        this.main = main;
        //this.recentFile = new HashMap<String ,MenuItem>();
        
        menuFile = new Menu(ITEM_FILE);
            itemOpen = new MenuItem();
            itemAdd = new MenuItem();
            itemSave = new MenuItem();
            itemSaveWork = new MenuItem();
            menuRecent = new Menu();
                itemClearRecent = new MenuItem();
            itemNewDF = new MenuItem();
            menuImport = new Menu();
                itemImportCSV = new MenuItem();
                itemImportXLS = new MenuItem();
                itemImportRDA = new MenuItem();
            menuExport = new Menu();
                itemExportXLS = new MenuItem();
                itemExportR = new MenuItem();
            itemdelDataFrame = new MenuItem();
            itemConfiguration = new MenuItem();
            itemQuit = new MenuItem();

        menuData = new Menu();
            menuTransforms = new Menu();
                itemTransformALR = new MenuItem();
                itemTransformCLR = new MenuItem();
                itemTransformRawILR = new MenuItem();
                itemTransformILRRaw = new MenuItem();
            itemCenter = new MenuItem();
            itemClosure = new MenuItem();
            itemAmalgamation = new MenuItem();
            itemPerturbate = new MenuItem();
            itemPower = new MenuItem();
            itemZeros = new MenuItem();
            itemSetDetectionLimit = new MenuItem();
            itemCategorizeVariables = new MenuItem();
            itemNumerizeVariables = new MenuItem();
            itemAddVariables = new MenuItem();
            itemDeleteVariables = new MenuItem();

        menuStatistics = new Menu();
            itemCompStatsSummary = new MenuItem();
            itemClasStatsSummary = new MenuItem();
            itemNormalityTest = new MenuItem();
            itemAtipicalityIndex = new MenuItem();                        

        menuGraphs = new Menu();
            itemTernaryPlot = new MenuItem();
            itemEmptyTernaryPlot = new MenuItem();
            itemBiPlot = new MenuItem();
            itemIlrBiPlot = new MenuItem();
            itemDendrogramPlot = new MenuItem();
            itemALRPlot = new MenuItem();
            itemCLRPlot = new MenuItem();
            itemILRPlot = new MenuItem();
            principalComponentPlot = new MenuItem();
            predictiveRegionPlot = new MenuItem();
            confidenceRegionPlot = new MenuItem();

        menuHelp = new Menu();
            itemForceUpdate = new MenuItem();
            itemAbout = new MenuItem();  
        
        
        menuFile.setText(ITEM_FILE);
        addMenuItem(menuFile, itemOpen, ITEM_OPEN);
        addMenuItem(menuFile, itemAdd, ITEM_ADD);
        addMenuItem(menuFile, itemSave, ITEM_SAV);
        addMenuItem(menuFile, itemSaveWork, ITEM_SAVE);
        menuRecent.setText(ITEM_RECENT);
        menuFile.getItems().add(menuRecent);
        //fillRecentFiles();
        //loadRecentFiles();
        
        menuFile.getItems().add(new SeparatorMenuItem());
        addMenuItem(menuFile, itemdelDataFrame, ITEM_DEL_DATAFRAME);
        
        menuFile.getItems().add(new SeparatorMenuItem());
        menuImport.setText(ITEM_IMPORT);
        menuFile.getItems().add(menuImport);
        addMenuItem(menuImport, itemImportCSV, ITEM_IMPORT_CSV);
        addMenuItem(menuImport, itemImportXLS, ITEM_IMPORT_XLS);
        addMenuItem(menuImport, itemImportRDA, ITEM_IMPORT_RDA);
        
        menuExport.setText(ITEM_EXPORT);
        menuFile.getItems().add(menuExport);
        addMenuItem(menuExport, itemExportXLS, ITEM_EXPORT_XLS);
        addMenuItem(menuExport, itemExportR, ITEM_EXPORT_R);
        
        menuFile.getItems().add(new SeparatorMenuItem());  
        addMenuItem(menuFile, itemConfiguration, ITEM_CONF);
        menuFile.getItems().add(new SeparatorMenuItem());  
        addMenuItem(menuFile, itemQuit, ITEM_QUIT);
        menuFile.getItems().add(new SeparatorMenuItem());    
        getMenus().add(menuFile);

        menuData.setText(ITEM_DATA);
        menuTransforms.setText(ITEM_TRANS);
        menuData.getItems().add(menuTransforms);
        addMenuItem(menuTransforms, itemTransformALR, ITEM_RAW_ALR);
        addMenuItem(menuTransforms, itemTransformCLR, ITEM_RAW_CLR);
        addMenuItem(menuTransforms, itemTransformRawILR, ITEM_T_RAW_ILR);
        addMenuItem(menuTransforms, itemTransformILRRaw, ITEM_T_ILR_RAW);
        addMenuItem(menuData, itemCenter, ITEM_CENTER);
        addMenuItem(menuData, itemClosure, ITEM_CLOSURE);
        addMenuItem(menuData, itemAmalgamation, ITEM_AMALGAM);
        addMenuItem(menuData, itemPerturbate, ITEM_PERTURBATE);
        addMenuItem(menuData, itemPower, ITEM_POWER);
        addMenuItem(menuData, itemSetDetectionLimit, ITEM_SETDETECTION); 
        addMenuItem(menuData, itemZeros, ITEM_ZEROS);
        menuData.getItems().add(new SeparatorMenuItem());  
        addMenuItem(menuData, itemCategorizeVariables, ITEM_CAT_VAR);
        addMenuItem(menuData, itemNumerizeVariables, ITEM_NUM_VAR);
        menuData.getItems().add(new SeparatorMenuItem());  
        addMenuItem(menuData, itemAddVariables, ITEM_ADD_VAR);
        addMenuItem(menuData, itemDeleteVariables, ITEM_DEL_VAR);
        getMenus().add(menuData);

        menuStatistics.setText(ITEM_STATS);
        addMenuItem(menuStatistics, itemCompStatsSummary, ITEM_COMP_STATS_SUMMARY);        
        addMenuItem(menuStatistics, itemClasStatsSummary, ITEM_CLAS_STATS_SUMMARY);        
        getMenus().add(menuStatistics);
        menuStatistics.getItems().add(new SeparatorMenuItem());  
        addMenuItem(menuStatistics, itemNormalityTest, ITEM_NORM_TEST);
        addMenuItem(menuStatistics, itemAtipicalityIndex, ITEM_ATIP_INDEX);

        menuGraphs.setText(ITEM_GRAPHS);
        addMenuItem(menuGraphs, itemTernaryPlot, ITEM_TERNARY_PLOT);
        addMenuItem(menuGraphs, itemEmptyTernaryPlot, ITEM_EMPTY_TERNARY_PLOT);
        addMenuItem(menuGraphs, principalComponentPlot, ITEM_PC_PLOT);
        addMenuItem(menuGraphs, predictiveRegionPlot, ITEM_PRED_REG_PLOT);
        addMenuItem(menuGraphs, confidenceRegionPlot, ITEM_CONF_REG_PLOT);        
        menuGraphs.getItems().add(new SeparatorMenuItem());  
        addMenuItem(menuGraphs, itemALRPlot, ITEM_ALR_PLOT);
        addMenuItem(menuGraphs, itemCLRPlot, ITEM_CLR_PLOT);
        addMenuItem(menuGraphs, itemILRPlot, ITEM_ILR_PLOT);
        menuGraphs.getItems().add(new SeparatorMenuItem());  
        addMenuItem(menuGraphs, itemBiPlot, ITEM_BIPLOT);
        addMenuItem(menuGraphs, itemIlrBiPlot, ITEM_ILR_BIPLOT);
        addMenuItem(menuGraphs, itemDendrogramPlot, ITEM_DENDROGRAM_PLOT);
        getMenus().add(menuGraphs);       

        menuHelp.setText(ITEM_HELP);
        addMenuItem(menuHelp, itemForceUpdate, ITEM_FORCE_UPDATE);
        addMenuItem(menuHelp, itemAbout, ITEM_ABOUT);
        getMenus().add(menuHelp);
        
    }    

    
    
    
//       HashMap<String ,MenuItem> recentFile;
//    LinkedHashMap<String, String> newRecentFile= new LinkedHashMap();
//    
//    //Afegeix o actualitza la llista d'arxius recents
//    public void saveRecentFile(String rf){ 
//        String pathFile;
//        //Si la te, li treiem l'inici al nom del path
//        if (rf.startsWith("format:codapack?")) {
//            pathFile = rf.substring(16);
//        }
//        else pathFile = rf;
//        //Obtenim el nom del fitxer amb l'extensió, que utilitzarem com a clau
//        String fname = new File(pathFile).getName();
//        //Comprovem si el fitxer es troba al hashmap, si es troba l'eliminem
//        if (newRecentFile.containsKey(pathFile)){
//            newRecentFile.remove(pathFile);
//        }
//        //Inserim clau valor al final del hashmap
//        newRecentFile.put(pathFile,pathFile);
//        //Actualitzada la llista anem a actualitzar el menu i l'acció pertinent
//        loadRecentFiles();
//    }
//    
//    //S'encarrega d'escriure el submenú Recent Files i definir l'acció pertinent
//    public void loadRecentFiles() {
//        //Borra tots els items del menú Recent Files
//        menuRecent.removeAll();
//        //Comprovem que hi hagi entrada per posar a Recent Files
//        if (newRecentFile.size()>0) {
//            //Creem la colecció i l'iterador per a recorrer els valors del LinkedHashMap
//            Collection c=newRecentFile.values();
//            Iterator itr=c.iterator();
//            String[] v = new String[newRecentFile.size()];
//            int j=0;
//            //Fem el recorregut pertinent per posar els paths en l'array
//            while(itr.hasNext()) {
//                String s=(String) itr.next();
//                v[j]=s;
//                j++;
//            }
//            for (int i=v.length-1;i>=0;i--) {
//                //Creem i afegim el valor a Recent Files
//                MenuItem item = new MenuItem();
//                menuRecent.add(item);
//                //Tornem a posar el prefix al pathFile
//                String s1="format:codapack?";
//                final String rf=s1.concat(v[i]);
//                //Posem el text al item
//                item.setText(v[i]);
//                //Definim l'acció a realitzar al clicar l'item
//                item.addActionListener(new java.awt.event.ActionListener() {
//                    @Override
//                    public void actionPerformed(java.awt.event.ActionEvent evt) {
//                        MenuItem jMenuItem = (MenuItem)evt.getSource();
//                        String title = jMenuItem.getText();
//                        for(CoDaPackMenuListener e: listeners){
//                            e.menuItemClicked(rf);
//                        }
//                        active_path = title;
//                    }});
//            }
//        }
//        //Afegim un separador i l'item Clear Items a Recent Files
//        menuRecent.getItems().add(new SeparatorMenuItem());  
//        addMenuItem(menuRecent, itemClearRecent, ITEM_CLEAR_RECENT);
//    }
//    
//    //Aquest mètode emplena el LinkedHashMap des de l'arxiu .recent_files cada vegada que s'inicia el programa
//    public void fillRecentFiles() {
//        File arx = null;
//        FileReader fr = null;
//        BufferedReader br = null;
//        try {
//            arx = new File(".recent_files");
//            fr = new FileReader(arx);
//            br = new BufferedReader(fr);
//            String linia;
//            while ((linia=br.readLine())!=null) {
//                newRecentFile.put(linia,linia);
//            }
//        }
//        catch (Exception e) {
//           e.printStackTrace(); 
//        }
//        finally {
//            try {
//                if (null != fr) fr.close();
//            }
//            catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//    }
//    
//    //Aquest mètode sobreescriu l'arxiu .recent_files, quan es tanca el programa i cada vegada que es fa un Clear Items
//    public void copyRecentFiles() {
//        FileWriter fit = null;
//        PrintWriter pw = null;
//        try {
//            fit = new FileWriter(".recent_files");
//            pw = new PrintWriter(fit);
//            String s;
//            Collection c=newRecentFile.values();
//            Iterator itr=c.iterator();
//            if (newRecentFile.size()>20) {
//                int i=newRecentFile.size()-20;
//                for (int j=0;j<i;j++) {
//                    itr.next();
//                }
//            }
//            while(itr.hasNext()) {
//                s=(String) itr.next();
//                pw.println(s);
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            try {
//                if (null!= fit) fit.close();
//            }
//            catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//    }
//    
//    //Neteja el menu Recent Files
//    public void removeRecentFiles() {
//        newRecentFile.clear();
//        copyRecentFiles();
//        loadRecentFiles();
//    }
}
