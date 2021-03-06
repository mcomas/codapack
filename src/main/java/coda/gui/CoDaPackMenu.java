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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author marc
 */
public class CoDaPackMenu extends JMenuBar{
    /*
     * Defining actions
     */
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

    public JMenu menuData;
    public final String ITEM_DATA = "Data";
        public JMenu menuTransforms;
        public final String ITEM_TRANS = "Transformations";
            public JMenuItem itemTransformALR;
            public final String ITEM_RAW_ALR = "ALR";
            public JMenuItem itemTransformCLR;
            public final String ITEM_RAW_CLR = "CLR";
            public JMenuItem itemTransformRawILR;
            public final String ITEM_T_RAW_ILR = "Raw-ILR";
            public JMenuItem itemTransformILRRaw;
            public final String ITEM_T_ILR_RAW = "ILR-Raw";
        public JMenu menuOperations;
        public final String ITEM_OPER = "Operations";
            public JMenuItem itemCenter;
            public final String ITEM_CENTER = "Centering";
            public JMenuItem itemClosure;
            public final String ITEM_CLOSURE = "Subcomposition/Closure";
            public JMenuItem itemPerturbate;
            public final String ITEM_PERTURBATE = "Perturbation";
            public JMenuItem itemPower;
            public final String ITEM_POWER = "Power transformation";
        public JMenu menuManipulate;
        public final String ITEM_MANI = "Manipulate";
            public JMenuItem itemDiscretize;
            public final String ITEM_DISCRETIZE = "Discretize/Segment variable";
            public JMenuItem itemCalculateNewVar;
            public final String ITEM_CALCULATE_NEW_VAR = "Calculate new Variable";
            public final JMenuItem itemSortData;
            public final String ITEM_SORT_DATA = "Sort data";
            public JMenuItem itemCategorizeVariables;
            public final String ITEM_CAT_VAR = "Numeric to categorical";
            public JMenuItem itemNumerizeVariables;
            public final String ITEM_NUM_VAR = "Categorical to Numeric";
            public JMenuItem itemChangeCategoricalNameGroup;
            public final String ITEM_CHANGE_CAT_NAME_GROUP = "Change Categorical label";
        public JMenu menuFilters;
        public final String ITEM_FILT = "Filters";
            public final JMenuItem itemFilter;
            public final String ITEM_FILTER = "Categorical filter";
            public final JMenuItem itemAdvFilter;
            public final String ITEM_ADV_FILTER = "Advanced filter";
        public JMenuItem itemAmalgamation;
        public final String ITEM_AMALGAM = "Amalgamation";
        public JMenuItem itemZeros;
        public final String ITEM_ZEROS = "Non-parametric multiplicative zero replacement";
        public JMenuItem itemCreateFrame;
        public final String ITEM_CREATE_FRAME = "Create new Table";
        public JMenuItem itemAddVariables;
        public final String ITEM_ADD_VAR = "Add Numeric Variables";
        public JMenuItem itemDeleteVariables;
        public final String ITEM_DEL_VAR = "Delete variables";
        
    public JMenu menuIrregularData;
    public final String ITEM_IRREG_DATA = "Irregular data";
        public JMenuItem zPatternsPlot;
        public final String ITEM_ZPATTERNS = "ZPatterns plot";
        public JMenuItem itemSetDetectionLimit;
        public final String ITEM_SETDETECTION ="Set detection limit";
        public JMenuItem itemZerosR;
        public final String ITEM_ZEROS_R = "Non-parametric zero Replacement";
        public JMenuItem itemLogRatio;
        public final String ITEM_LOG_RATIO = "Logratio-EM zero Replacement";
        public JMenuItem itemBayesianMultReplace;
        public final String ITEM_BAYESIAN_MULT_REPLACE = "Bayesian Multiplicative zero Replacement";
        public JMenuItem itemEM_Missing;
        public final String ITEM_EM_MISSING = "Logratio-EM missing replacement";
        public JMenuItem itemEM_Zero_Missing;
        public final String ITEM_EM_ZERO_MISSING = "Logratio-EM Zero & missing replacement";
        public JMenuItem itemAtipicalityIndex;
        public final String ITEM_ATIP_INDEX = "Atipicality index";

    public JMenu menuStatistics;
    public final String ITEM_STATS = "Statistics";
        public JMenuItem itemCompStatsSummary;
        public final String ITEM_COMP_STATS_SUMMARY = "Compositional statistics summary";
        public JMenuItem itemClasStatsSummary;
        public final String ITEM_CLAS_STATS_SUMMARY = "Classical statistics summary";
        public JMenu menuMultiAnalysis;
        public final String ITEM_MULTI_ANALYSIS = "Multivariate Analysis";
            public JMenu menuRegression;
            public final String ITEM_REGRESSION = "Regression";
                public JMenuItem item_Reg_XReal_YComp;
                public final String ITEM_REG_XREAL_YCOMP = "X real Y composition";
                public JMenuItem item_Reg_XComp_YReal;
                public final String ITEM_REG_XCOMP_YREAL = "X composition Y real";
                public JMenuItem item_Reg_XComp_YComp;
                public final String ITEM_REG_XCOMP_YCOMP = "X composition Y composition";
            public JMenu menuCluster;
            public final String ITEM_CLUSTER = "Cluster";
                public JMenuItem item_Clust_Kmeans;
                public final String ITEM_CLUST_KMEANS = "K-Means";
            public JMenuItem item_Manova;
            public final String ITEM_MANOVA = "Manova";
            public JMenuItem item_Disc_Analysis;
            public final String ITEM_DISC_ANALYSIS = "Discriminant Analysis";
        public JMenuItem itemNormalityTest;
        public final String ITEM_NORM_TEST = "Log-Ratio Normality Test";
        public JMenuItem itemClasUniNormTest;
        public final String ITEM_CLAS_UNI_NORM_TEST = "Classical Univariate Normality test";


    public JMenu menuGraphs;
    public final String ITEM_GRAPHS = "Graphs";
        public JMenuItem itemTernaryPlot;
        public final String ITEM_TERNARY_PLOT = "Ternary/Quaternary plot";
        public JMenuItem itemEmptyTernaryPlot;
        public final String ITEM_EMPTY_TERNARY_PLOT = "Ternary/Quaternary plot [Empty]";
        public JMenuItem itemIlrBiPlot;
        public final String ITEM_ILR_BIPLOT = "ILR/CLR plot";
        public JMenuItem itemBiPlot;
        public final String ITEM_BIPLOT = "CLR biplot";
        public JMenuItem itemDendrogramPlot;
        public final String ITEM_DENDROGRAM_PLOT = "Balance dendrogram";
        public JMenuItem itemALRPlot;
        public final String ITEM_ALR_PLOT = "ALR plot";
        public JMenuItem itemCLRPlot;
        public final String ITEM_CLR_PLOT = "CLR plot";
        public JMenuItem itemILRPlot;
        public final String ITEM_ILR_PLOT = "ILR plot";
        public JMenuItem principalComponentPlot;
        public final String ITEM_PC_PLOT = "Ternary/Quaternary Principal Components";
        public JMenuItem predictiveRegionPlot;
        public final String ITEM_PRED_REG_PLOT = "Predictive Region";
        public JMenuItem confidenceRegionPlot;
        public final String ITEM_CONF_REG_PLOT = "Center Confidence Region";
        public JMenuItem itemBoxplot;
        public final String ITEM_BOXPLOT = "Boxplot";
        public JMenuItem itemScatterplot;
        public final String ITEM_SCATTERPLOT = "Scatterplot 2D/3D";
        public JMenuItem itemGeoMeanPlot;
        public final String ITEM_GEO_MEAN_PLOT = "Geometric mean barplot";

   public JMenu menuHelp;
   public final String ITEM_HELP = "Help";
        public JMenuItem itemForceUpdate;
        public final String ITEM_FORCE_UPDATE = "Check for Updates";
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

    public String active_path = null;
    
    private JMenuItem addJMenuItem(JMenu menu, JMenuItem item, String title){
        //item.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        menu.add(item);
        item.setText(title);
        item.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMenuItem jMenuItem = (JMenuItem)evt.getSource();
                String title = jMenuItem.getText();
                for(CoDaPackMenuListener e: listeners){
                    e.menuItemClicked(title);
                }
            }
        });
        return item;
    }
    public void saveLoadRecentFiles(){
        
    }

    HashMap<String ,JMenuItem> recentFile;
    
    //Definició del LinkedHashMap que farà la gestió dels arxius recents
    LinkedHashMap<String, String> newRecentFile= new LinkedHashMap();
    
    //Afegeix o actualitza la llista d'arxius recents
    public void saveRecentFile(String rf){ 
        String pathFile;
        //Si la te, li treiem l'inici al nom del path
        if (rf.startsWith("format:codapack?")) {
            pathFile = rf.substring(16);
        }
        else pathFile = rf;
        //Obtenim el nom del fitxer amb l'extensió, que utilitzarem com a clau
        String fname = new File(pathFile).getName();
        //Comprovem si el fitxer es troba al hashmap, si es troba l'eliminem
        if (newRecentFile.containsKey(pathFile)){
            newRecentFile.remove(pathFile);
        }
        //Inserim clau valor al final del hashmap
        newRecentFile.put(pathFile,pathFile);
        //Actualitzada la llista anem a actualitzar el menu i l'acció pertinent
        loadRecentFiles();
    }
    
    //S'encarrega d'escriure el submenú Recent Files i definir l'acció pertinent
    public void loadRecentFiles() {
        //Borra tots els items del menú Recent Files
        menuRecent.removeAll();
        //Comprovem que hi hagi entrada per posar a Recent Files
        if (newRecentFile.size()>0) {
            //Creem la colecció i l'iterador per a recorrer els valors del LinkedHashMap
            Collection c=newRecentFile.values();
            Iterator itr=c.iterator();
            String[] v = new String[newRecentFile.size()];
            int j=0;
            //Fem el recorregut pertinent per posar els paths en l'array
            while(itr.hasNext()) {
                String s=(String) itr.next();
                v[j]=s;
                j++;
            }
            for (int i=v.length-1;i>=0;i--) {
                //Creem i afegim el valor a Recent Files
                JMenuItem item = new JMenuItem();
                menuRecent.add(item);
                //Tornem a posar el prefix al pathFile
                String s1="format:codapack?";
                final String rf=s1.concat(v[i]);
                //Posem el text al item
                item.setText(v[i]);
                //Definim l'acció a realitzar al clicar l'item
                item.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        JMenuItem jMenuItem = (JMenuItem)evt.getSource();
                        String title = jMenuItem.getText();
                        for(CoDaPackMenuListener e: listeners){
                            e.menuItemClicked(rf);
                        }
                        active_path = title;
                    }});
            }
        }
        //Afegim un separador i l'item Clear Items a Recent Files
        menuRecent.addSeparator();
        addJMenuItem(menuRecent, itemClearRecent, ITEM_CLEAR_RECENT);
    }
    
    //Aquest mètode emplena el LinkedHashMap des de l'arxiu .recent_files cada vegada que s'inicia el programa
    public void fillRecentFiles() {
        File arx = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            arx = new File(System.getProperty("java.io.tmpdir") + ".recent_files");
            fr = new FileReader(arx);
            br = new BufferedReader(fr);
            String linia;
            while ((linia=br.readLine())!=null) {
                newRecentFile.put(linia,linia);
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
    }
    
    //Aquest mètode sobreescriu l'arxiu .recent_files, quan es tanca el programa i cada vegada que es fa un Clear Items
    public void copyRecentFiles() {
        FileWriter fit = null;
        PrintWriter pw = null;
        try {
            fit = new FileWriter(System.getProperty("java.io.tmpdir") + ".recent_files");
            pw = new PrintWriter(fit);
            String s;
            Collection c=newRecentFile.values();
            Iterator itr=c.iterator();
            if (newRecentFile.size()>20) {
                int i=newRecentFile.size()-20;
                for (int j=0;j<i;j++) {
                    itr.next();
                }
            }
            while(itr.hasNext()) {
                s=(String) itr.next();
                pw.println(s);
            }
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
    
    //Neteja el menu Recent Files
    public void removeRecentFiles() {
        newRecentFile.clear();
        copyRecentFiles();
        loadRecentFiles();
    }
    
    public CoDaPackMenu(){
        this.recentFile = new HashMap<String ,JMenuItem>();
        
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

        menuData = new JMenu();
            menuTransforms = new JMenu();
                itemTransformALR = new JMenuItem();
                itemTransformCLR = new JMenuItem();
                itemTransformRawILR = new JMenuItem();
                itemTransformILRRaw = new JMenuItem();
            menuOperations = new JMenu();
                itemCenter = new JMenuItem();
                itemClosure = new JMenuItem();
                itemPerturbate = new JMenuItem();
                itemPower = new JMenuItem();
            menuManipulate = new JMenu();
                itemDiscretize = new JMenuItem();
                itemCalculateNewVar = new JMenuItem();
                itemSortData = new JMenuItem();
                itemCategorizeVariables = new JMenuItem();
                itemNumerizeVariables = new JMenuItem();
                itemChangeCategoricalNameGroup = new JMenuItem();
            menuFilters = new JMenu();
                itemFilter = new JMenuItem();
                itemAdvFilter = new JMenuItem();
            /*itemAmalgamation = new JMenuItem();*/
            /*itemZeros = new JMenuItem();*/
            itemCreateFrame = new JMenuItem();
            itemAddVariables = new JMenuItem();
            itemDeleteVariables = new JMenuItem();
            
        menuIrregularData = new JMenu();
            zPatternsPlot = new JMenuItem();
            itemSetDetectionLimit = new JMenuItem();
            itemZerosR = new JMenuItem();
            itemLogRatio = new JMenuItem();
            itemBayesianMultReplace = new JMenuItem();
            itemEM_Missing = new JMenuItem();
            itemEM_Zero_Missing = new JMenuItem();
            itemAtipicalityIndex = new JMenuItem();

        menuStatistics = new JMenu();
            itemCompStatsSummary = new JMenuItem();
            itemClasStatsSummary = new JMenuItem();
            menuMultiAnalysis = new JMenu();
                menuRegression = new JMenu();
                    item_Reg_XReal_YComp = new JMenuItem();
                    item_Reg_XComp_YReal = new JMenuItem();
                    item_Reg_XComp_YComp = new JMenuItem();
                menuCluster = new JMenu();
                    item_Clust_Kmeans = new JMenuItem();
                item_Manova = new JMenuItem();
                item_Disc_Analysis = new JMenuItem();
            itemNormalityTest = new JMenuItem();
            itemClasUniNormTest = new JMenuItem();

        menuGraphs = new JMenu();
            itemTernaryPlot = new JMenuItem();
            itemEmptyTernaryPlot = new JMenuItem();
            itemBiPlot = new JMenuItem();
            itemIlrBiPlot = new JMenuItem();
            itemDendrogramPlot = new JMenuItem();
            itemALRPlot = new JMenuItem();
            itemCLRPlot = new JMenuItem();
            itemILRPlot = new JMenuItem();
            principalComponentPlot = new JMenuItem();
            predictiveRegionPlot = new JMenuItem();
            confidenceRegionPlot = new JMenuItem();
            itemBoxplot = new JMenuItem();
            itemScatterplot = new JMenuItem();
            itemGeoMeanPlot = new JMenuItem();

        menuHelp = new JMenu();
            itemForceUpdate = new JMenuItem();
            itemAbout = new JMenuItem();
            itemR_Test = new JMenuItem();
            
        menuDevelopment = new JMenu();
            itemModelS0 = new JMenuItem();
            itemModelS1 = new JMenuItem();
            itemModelS2 = new JMenuItem();
            itemModelS3 = new JMenuItem();
            itemModelS4 = new JMenuItem();
        
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
        addJMenuItem(menuFile,itemDeleteAllTables, ITEM_DELETE_ALL_TABLES);
        addJMenuItem(menuFile, itemClearOutputs, ITEM_CLEAR_OUTPUTS);
        
        menuFile.addSeparator();
        menuImport.setText(ITEM_IMPORT);
        menuFile.add(menuImport);
        addJMenuItem(menuImport, itemImportCSV, ITEM_IMPORT_CSV);
        addJMenuItem(menuImport, itemImportXLS, ITEM_IMPORT_XLS);
        addJMenuItem(menuImport, itemImportRDA, ITEM_IMPORT_RDA);
        
        menuExport.setText(ITEM_EXPORT);
        menuFile.add(menuExport);
        addJMenuItem(menuExport,itemExportCSV,ITEM_EXPORT_CSV);
        addJMenuItem(menuExport, itemExportXLS, ITEM_EXPORT_XLS);
        addJMenuItem(menuExport, itemExportR, ITEM_EXPORT_R);
        
        menuFile.addSeparator();
        addJMenuItem(menuFile, itemConfiguration, ITEM_CONF);
        menuFile.addSeparator();
        addJMenuItem(menuFile, itemQuit, ITEM_QUIT);
        menuFile.addSeparator();  
        add(menuFile);

        menuData.setText(ITEM_DATA);
        menuTransforms.setText(ITEM_TRANS);
        menuData.add(menuTransforms);
            addJMenuItem(menuTransforms, itemTransformALR, ITEM_RAW_ALR);
            addJMenuItem(menuTransforms, itemTransformCLR, ITEM_RAW_CLR);
            addJMenuItem(menuTransforms, itemTransformRawILR, ITEM_T_RAW_ILR);
            addJMenuItem(menuTransforms, itemTransformILRRaw, ITEM_T_ILR_RAW);
        menuData.addSeparator();
        menuOperations.setText(ITEM_OPER);
        menuData.add(menuOperations);
            addJMenuItem(menuOperations,itemCenter,ITEM_CENTER);
            addJMenuItem(menuOperations,itemClosure,ITEM_CLOSURE);
            addJMenuItem(menuOperations,itemPerturbate,ITEM_PERTURBATE);
            addJMenuItem(menuOperations,itemPower, ITEM_POWER);
        menuData.addSeparator();
        menuManipulate.setText(ITEM_MANI);
        menuData.add(menuManipulate);
            addJMenuItem(menuManipulate,itemDiscretize,ITEM_DISCRETIZE);
            addJMenuItem(menuManipulate,itemCalculateNewVar, ITEM_CALCULATE_NEW_VAR);
            addJMenuItem(menuManipulate, itemSortData, ITEM_SORT_DATA);
            addJMenuItem(menuManipulate, itemCategorizeVariables, ITEM_CAT_VAR);
            addJMenuItem(menuManipulate, itemNumerizeVariables, ITEM_NUM_VAR);
            addJMenuItem(menuManipulate, itemChangeCategoricalNameGroup, ITEM_CHANGE_CAT_NAME_GROUP);
        menuData.addSeparator();
        menuFilters.setText(ITEM_FILT);
        menuData.add(menuFilters);
            addJMenuItem(menuFilters, itemFilter, ITEM_FILTER);
            addJMenuItem(menuFilters, itemAdvFilter, ITEM_ADV_FILTER);
        menuData.addSeparator();
        addJMenuItem(menuData,itemCreateFrame,ITEM_CREATE_FRAME);
        addJMenuItem(menuData, itemAddVariables, ITEM_ADD_VAR);
        addJMenuItem(menuData, itemDeleteVariables, ITEM_DEL_VAR);
        //addJMenuItem(menuData, itemAmalgamation, ITEM_AMALGAM);
        //addJMenuItem(menuData, itemZeros, ITEM_ZEROS);

        add(menuData);
        
        menuIrregularData.setText(ITEM_IRREG_DATA);
            addJMenuItem(menuIrregularData, zPatternsPlot, ITEM_ZPATTERNS);
            addJMenuItem(menuIrregularData, itemSetDetectionLimit, ITEM_SETDETECTION);
            addJMenuItem(menuIrregularData, itemZerosR, ITEM_ZEROS_R);
            addJMenuItem(menuIrregularData, itemLogRatio, ITEM_LOG_RATIO);
            addJMenuItem(menuIrregularData, itemBayesianMultReplace, ITEM_BAYESIAN_MULT_REPLACE);
            menuIrregularData.addSeparator();
            addJMenuItem(menuIrregularData, itemEM_Missing, ITEM_EM_MISSING);
            addJMenuItem(menuIrregularData, itemEM_Zero_Missing, ITEM_EM_ZERO_MISSING);
            menuIrregularData.addSeparator();
            addJMenuItem(menuIrregularData, itemAtipicalityIndex, ITEM_ATIP_INDEX);
            
        add(menuIrregularData);

        menuStatistics.setText(ITEM_STATS);
        addJMenuItem(menuStatistics, itemCompStatsSummary, ITEM_COMP_STATS_SUMMARY);        
        addJMenuItem(menuStatistics, itemClasStatsSummary, ITEM_CLAS_STATS_SUMMARY);
        menuMultiAnalysis.setText(ITEM_MULTI_ANALYSIS);
        menuStatistics.add(menuMultiAnalysis);
            menuRegression.setText(ITEM_REGRESSION);
            menuMultiAnalysis.add(menuRegression);
                addJMenuItem(menuRegression,item_Reg_XReal_YComp,ITEM_REG_XREAL_YCOMP);
                addJMenuItem(menuRegression,item_Reg_XComp_YReal,ITEM_REG_XCOMP_YREAL);
                //addJMenuItem(menuRegression,item_Reg_XComp_YComp,ITEM_REG_XCOMP_YCOMP);
            menuCluster.setText(ITEM_CLUSTER);
            menuMultiAnalysis.add(menuCluster);
                addJMenuItem(menuCluster,item_Clust_Kmeans,ITEM_CLUST_KMEANS);
            addJMenuItem(menuMultiAnalysis,item_Manova,ITEM_MANOVA);
            addJMenuItem(menuMultiAnalysis,item_Disc_Analysis,ITEM_DISC_ANALYSIS);
        menuStatistics.addSeparator();
        addJMenuItem(menuStatistics, itemNormalityTest, ITEM_NORM_TEST);
        addJMenuItem(menuStatistics, itemClasUniNormTest, ITEM_CLAS_UNI_NORM_TEST);
        add(menuStatistics);

        menuGraphs.setText(ITEM_GRAPHS);
        addJMenuItem(menuGraphs, itemTernaryPlot, ITEM_TERNARY_PLOT);
        addJMenuItem(menuGraphs, itemEmptyTernaryPlot, ITEM_EMPTY_TERNARY_PLOT);
        addJMenuItem(menuGraphs, principalComponentPlot, ITEM_PC_PLOT);
        addJMenuItem(menuGraphs, predictiveRegionPlot, ITEM_PRED_REG_PLOT);
        addJMenuItem(menuGraphs, confidenceRegionPlot, ITEM_CONF_REG_PLOT);
        menuGraphs.addSeparator();
        addJMenuItem(menuGraphs, itemBoxplot, ITEM_BOXPLOT);
        addJMenuItem(menuGraphs, itemScatterplot, ITEM_SCATTERPLOT);
        addJMenuItem(menuGraphs,itemGeoMeanPlot, ITEM_GEO_MEAN_PLOT);
        //addJMenuItem(menuGraphs, itemALRPlot, ITEM_ALR_PLOT);
        //addJMenuItem(menuGraphs, itemCLRPlot, ITEM_CLR_PLOT);
        //addJMenuItem(menuGraphs, itemILRPlot, ITEM_ILR_PLOT);
        menuGraphs.addSeparator();
        addJMenuItem(menuGraphs, itemBiPlot, ITEM_BIPLOT);
        addJMenuItem(menuGraphs, itemIlrBiPlot, ITEM_ILR_BIPLOT);
        addJMenuItem(menuGraphs, itemDendrogramPlot, ITEM_DENDROGRAM_PLOT);
        add(menuGraphs);       

        menuHelp.setText(ITEM_HELP);
        addJMenuItem(menuHelp, itemForceUpdate, ITEM_FORCE_UPDATE);
        addJMenuItem(menuHelp, itemAbout, ITEM_ABOUT);
        addJMenuItem(menuHelp,itemR_Test, R_TEST);
        add(menuHelp);
        
        menuDevelopment.setText(ITEM_DEVELOPMENT);
        addJMenuItem(menuDevelopment,itemModelS0, ITEM_MODEL_S0);
        addJMenuItem(menuDevelopment,itemModelS1, ITEM_MODEL_S1);
        addJMenuItem(menuDevelopment,itemModelS2, ITEM_MODEL_S2);
        addJMenuItem(menuDevelopment,itemModelS3, ITEM_MODEL_S3);
        addJMenuItem(menuDevelopment,itemModelS4, ITEM_MODEL_S4);
    }

    public void addDataFrame(){
        
    }
    /*
     * 
     * Delegating events
     */
    ArrayList<CoDaPackMenuListener> listeners = new ArrayList<CoDaPackMenuListener>();
    public void addCoDaPackMenuListener(CoDaPackMenuListener listener){
        listeners.add(listener);
    }
    public interface CoDaPackMenuListener{
        public void menuItemClicked(String label);
    }
    
    public void activeDevMenu(){
        add(menuDevelopment);
        this.repaint();
        super.updateUI();
    }
    
    public void disableDevMenu(){
        remove(menuDevelopment);
        this.repaint();
        super.updateUI();
    }
}
