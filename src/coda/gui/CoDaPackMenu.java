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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
        public final String ITEM_OPEN = "Open Workspace...";
        public JMenuItem itemSave;
        public final String ITEM_SAVE = "Save Workspace...";
        public JMenuItem itemRecent;
        public final String ITEM_RECENT = "Open Recent";
        public JMenuItem itemNewDF;
        public final String ITEM_NEWDF = "New DataFrame";
        public JMenu menuImport;
        public final String ITEM_IMPORT = "Import";
            public JMenuItem itemImportXLS;
            public final String ITEM_IMPORT_XLS = "Import XLS Data...";
            public JMenuItem itemImportCSV;
            public final String ITEM_IMPORT_CSV = "Import CSV/Text Data...";
        public JMenu menuExport;
        public final String ITEM_EXPORT = "Export";
            public JMenuItem itemExportXLS;
            public final String ITEM_EXPORT_XLS = "Export Data to XLS...";
            public JMenuItem itemExportR;
            public final String ITEM_EXPORT_R = "Export Data to RData...";
        public JMenuItem itemdelDataFrame;
        public final String ITEM_DEL_DATAFRAME = "Delete Table";
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
            public JMenuItem itemTransformILR;
            public final String ITEM_RAW_ILR = "ILR";
        public JMenuItem itemCenter;
        public final String ITEM_CENTER = "Centering";
        public JMenuItem itemClosure;
        public final String ITEM_CLOSURE = "Subcomposition/Closure";
        public JMenuItem itemAmalgamation;
        public final String ITEM_AMALGAM = "Amalgamation";
        public JMenuItem itemPerturbate;
        public final String ITEM_PERTURBATE = "Perturbation";
        public JMenuItem itemPower;
        public final String ITEM_POWER = "Power transformation";
        public JMenuItem itemZeros;
        public final String ITEM_ZEROS = "Rounded zero replacement";
        public JMenuItem itemCategorizeVariables;
        public final String ITEM_CAT_VAR = "Numeric to categorical";
        public JMenuItem itemNumerizeVariables;
        public final String ITEM_NUM_VAR = "Categorical to Numeric";
        public JMenuItem itemAddVariables;
        public final String ITEM_ADD_VAR = "Add Numeric Variables";
        public JMenuItem itemDeleteVariables;
        public final String ITEM_DEL_VAR = "Delete variables";

    public JMenu menuStatistics;
    public final String ITEM_STATS = "Statistics";
        public JMenuItem itemCompStatsSummary;
        public final String ITEM_COMP_STATS_SUMMARY = "Compositional statistics summary";
        public JMenuItem itemClasStatsSummary;
        public final String ITEM_CLAS_STATS_SUMMARY = "Classical statistics summary";
        public JMenuItem itemNormalityTest;
        public final String ITEM_NORM_TEST = "Additive Logistic Normality Tests";
        public JMenuItem itemAtipicalityIndex;
        public final String ITEM_ATIP_INDEX = "Atipicality index";


    public JMenu menuGraphs;
    public final String ITEM_GRAPHS = "Graphs";
        public JMenuItem itemTernaryPlot;
        public final String ITEM_TERNARY_PLOT = "Ternary plot";
        public JMenuItem itemEmptyTernaryPlot;
        public final String ITEM_EMPTY_TERNARY_PLOT = "Ternary plot [Empty]";
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
        public final String ITEM_PC_PLOT = "Ternary Principal Components";
        public JMenuItem predictiveRegionPlot;
        public final String ITEM_PRED_REG_PLOT = "Predictive Region";
        public JMenuItem confidenceRegionPlot;
        public final String ITEM_CONF_REG_PLOT = "Center Confidence Region";

   public JMenu menuHelp;
   public final String ITEM_HELP = "Help";
        public JMenuItem itemForceUpdate;
        public final String ITEM_FORCE_UPDATE = "Force update";
        public JMenuItem itemAbout;
        public final String ITEM_ABOUT = "About";
    
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
    public void addRecentFile(final String rf){
            if(rf != null){
                String pars[] = rf.split("¿");
                String path = pars[1];
                
                if(recentFile.containsKey(path)){
                    menuFile.remove(recentFile.remove(path));
                }
                
                JMenuItem item = new JMenuItem();
                menuFile.add(item);
                String fname = new File(path).getName();
                item.setText(fname);
                item.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        JMenuItem jMenuItem = (JMenuItem)evt.getSource();
                        String title = jMenuItem.getText();
                        for(CoDaPackMenuListener e: listeners){
                            e.menuItemClicked(rf);
                        }
                    }});
               recentFile.put(path, item);
            }
        
    }    
    HashMap<String ,JMenuItem> recentFile;
    
    public CoDaPackMenu(){
        this.recentFile = new HashMap<String ,JMenuItem>();
        
            menuFile = new JMenu();
                itemOpen = new JMenuItem();
                itemSave = new JMenuItem();
                itemRecent = new JMenuItem();
                itemNewDF = new JMenuItem();
                menuImport = new JMenu();
                    itemImportCSV = new JMenuItem();
                    itemImportXLS = new JMenuItem();
                menuExport = new JMenu();
                    itemExportXLS = new JMenuItem();
                    itemExportR = new JMenuItem();
                itemdelDataFrame = new JMenuItem();
                itemConfiguration = new JMenuItem();
                itemQuit = new JMenuItem();

        menuData = new JMenu();
            menuTransforms = new JMenu();
                itemTransformALR = new JMenuItem();
                itemTransformCLR = new JMenuItem();
                itemTransformILR = new JMenuItem();
            itemCenter = new JMenuItem();
            itemClosure = new JMenuItem();
            itemAmalgamation = new JMenuItem();
            itemPerturbate = new JMenuItem();
            itemPower = new JMenuItem();
            itemZeros = new JMenuItem();
            itemCategorizeVariables = new JMenuItem();
            itemNumerizeVariables = new JMenuItem();
            itemAddVariables = new JMenuItem();
            itemDeleteVariables = new JMenuItem();

        menuStatistics = new JMenu();
            itemCompStatsSummary = new JMenuItem();
            itemClasStatsSummary = new JMenuItem();
            itemNormalityTest = new JMenuItem();
            itemAtipicalityIndex = new JMenuItem();                        

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

        menuHelp = new JMenu();
            itemForceUpdate = new JMenuItem();
            itemAbout = new JMenuItem();  
        
        
        menuFile.setText(ITEM_FILE);
        addJMenuItem(menuFile, itemOpen, ITEM_OPEN);
        addJMenuItem(menuFile, itemSave, ITEM_SAVE);
        addJMenuItem(menuFile, itemRecent, ITEM_RECENT);

        menuFile.addSeparator();        
        addJMenuItem(menuFile, itemdelDataFrame, ITEM_DEL_DATAFRAME);
        
        menuFile.addSeparator();
        menuImport.setText(ITEM_IMPORT);
        menuFile.add(menuImport);
        addJMenuItem(menuImport, itemImportCSV, ITEM_IMPORT_CSV);
        addJMenuItem(menuImport, itemImportXLS, ITEM_IMPORT_XLS);
        
        menuExport.setText(ITEM_EXPORT);
        menuFile.add(menuExport);
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
        addJMenuItem(menuTransforms, itemTransformILR, ITEM_RAW_ILR);
        addJMenuItem(menuData, itemCenter, ITEM_CENTER);
        addJMenuItem(menuData, itemClosure, ITEM_CLOSURE);
        addJMenuItem(menuData, itemAmalgamation, ITEM_AMALGAM);
        addJMenuItem(menuData, itemPerturbate, ITEM_PERTURBATE);
        addJMenuItem(menuData, itemPower, ITEM_POWER);
        addJMenuItem(menuData, itemZeros, ITEM_ZEROS);
        menuData.addSeparator();
        addJMenuItem(menuData, itemCategorizeVariables, ITEM_CAT_VAR);
        addJMenuItem(menuData, itemNumerizeVariables, ITEM_NUM_VAR);
        menuData.addSeparator();
        addJMenuItem(menuData, itemAddVariables, ITEM_ADD_VAR);
        addJMenuItem(menuData, itemDeleteVariables, ITEM_DEL_VAR);
        add(menuData);

        menuStatistics.setText(ITEM_STATS);
        addJMenuItem(menuStatistics, itemCompStatsSummary, ITEM_COMP_STATS_SUMMARY);        
        addJMenuItem(menuStatistics, itemClasStatsSummary, ITEM_CLAS_STATS_SUMMARY);        
        add(menuStatistics);
        menuStatistics.addSeparator();
        addJMenuItem(menuStatistics, itemNormalityTest, ITEM_NORM_TEST);
        addJMenuItem(menuStatistics, itemAtipicalityIndex, ITEM_ATIP_INDEX);

        menuGraphs.setText(ITEM_GRAPHS);
        addJMenuItem(menuGraphs, itemTernaryPlot, ITEM_TERNARY_PLOT);
        addJMenuItem(menuGraphs, itemEmptyTernaryPlot, ITEM_EMPTY_TERNARY_PLOT);
        addJMenuItem(menuGraphs, principalComponentPlot, ITEM_PC_PLOT);
        addJMenuItem(menuGraphs, predictiveRegionPlot, ITEM_PRED_REG_PLOT);
        addJMenuItem(menuGraphs, confidenceRegionPlot, ITEM_CONF_REG_PLOT);        
        menuGraphs.addSeparator();
        addJMenuItem(menuGraphs, itemALRPlot, ITEM_ALR_PLOT);
        addJMenuItem(menuGraphs, itemCLRPlot, ITEM_CLR_PLOT);
        addJMenuItem(menuGraphs, itemILRPlot, ITEM_ILR_PLOT);
        menuGraphs.addSeparator();
        addJMenuItem(menuGraphs, itemBiPlot, ITEM_BIPLOT);
        addJMenuItem(menuGraphs, itemIlrBiPlot, ITEM_ILR_BIPLOT);
        addJMenuItem(menuGraphs, itemDendrogramPlot, ITEM_DENDROGRAM_PLOT);
        add(menuGraphs);       

        menuHelp.setText(ITEM_HELP);
        addJMenuItem(menuHelp, itemForceUpdate, ITEM_FORCE_UPDATE);
        addJMenuItem(menuHelp, itemAbout, ITEM_ABOUT);
        add(menuHelp);
        
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
}
