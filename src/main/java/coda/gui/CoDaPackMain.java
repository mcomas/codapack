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


import coda.DataFrame;
import coda.ext.json.JSONException;
import coda.ext.json.JSONObject;
import coda.gui.CoDaPackMain.UpdateConnection;
import coda.gui.CoDaPackMenu.CoDaPackMenuListener;
import coda.gui.menu.*;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;
import coda.gui.output.OutputText;
import coda.gui.table.TablePanel;
import coda.gui.utils.FileNameExtensionFilter;
import coda.io.CoDaPackImporter;
import coda.io.ExportData;
import coda.io.ImportRDA;
import coda.io.WorkspaceIO;
import coda.plot2.TernaryPlot2dDisplay;
import coda.plot2.objects.Ternary2dGridObject;
import coda.plot2.objects.Ternary2dObject;
import coda.plot2.window.TernaryPlot2dWindow;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.JFXPanel;
import javax.script.ScriptException;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

// R LIBRARIES
import org.rosuda.JRI.*;

/**
 *
 * @author mcomas
 */
public final class CoDaPackMain extends JFrame{
    public static final long serialVersionUID = 1L;
    // CoDaPack version
    
    // dataFrame is the class containing the data
    private final ArrayList<DataFrame> dataFrame = new ArrayList<DataFrame>();
    private int activeDataFrame = -1;
    
    
    public static String RESOURCE_PATH = "/";

    private final Dimension screenDimension;
    
    //Panel showing the non-column outputs and the data contained in the dataFrame
    public static JTabbedPane outputs;
    public static OutputPanel outputPanel;
    public static TablePanel tablePanel;
    //Panel with the active variable
    public static DataList dataList;
    private JSplitPane jSplitPane;
    
    /**
     * VARIABLE R
     */
    public static String[] Rargs = {"--vanilla"};
    public static Rengine re = new Rengine(Rargs, false, null);
    
    private JComboBox dataFrameSelector;
    private final DataFrameSelectorListener dataFrameListener = new DataFrameSelectorListener();
    

    
    private String ITEM_APPLICATION_NAME;
    // Menu
    public static CoDaPackMenu jMenuBar;
    
    // Menus 
    
    private TransformationALRMenu transformationALRMenu;
    private TransformationCLRMenu transformationCLRMenu;
    private TransformationRawILRMenu transformationRawILRMenu;
    private TransformationILRRawMenu transformationILRRawMenu;
    private DiscretizeMenu discretizeMenu;
    private CenterDataMenu centerDataMenu;
    private ClosureDataMenu closureDataMenu;
    private CalculateNewVarMenu calculateNewVarMenu;
    private PerturbateDataMenu perturbateDataMenu;
    private PowerDataMenu powerDataMenu;
    private SetDetectionLimitMenu setDetectionLimitMenu;
    private ZeroReplacementMenu zeroReplacementMenu;
    private ZeroReplacementRMenu zeroReplacementRMenu;
    private LogRatioEMMenu logRatioEMMenu;
    private BayesianMultRepMenu bayesianMultRepMenu;
    private EM_MissingMenu EM_MissingMenu;
    private EM_ZeroMissingMenu EM_ZeroMissingMenu;
    private SortDataMenu sortDataMenu;
    private FilterMenu filterMenu;
    private AdvancedFilterMenu advancedFilterMenu;
    private CompStatsSummaryMenu compStatsSummaryMenu;
    private ClasStatsSummaryMenu clasStatsSummaryMenu;
    private LM1 LM1;
    private LM2 LM2;
    private ClusterMenu clusterMenu;
    private ManovaMenu manovaMenu;
    private DiscriminantMenu discriminantMenu;
    private NormalityTestMenu normalityTestMenu;
    //private AtipicalityIndexMenu atipicalityIndexMenu;
    private AtypMenu atypMenu;
    private TernaryPlotMenu ternaryPlotMenu;
    private PrincipalComponentMenu principalComponentMenu;
    private PredictiveRegionMenu predictiveRegionMenu;
    private ConfidenceRegionMenu confidenceRegionMenu;
    private ZpatternsMenu zpatternsMenu;
    private BoxplotMenu boxplotMenu;
    private ScatterplotMenu scatterplotMenu;
    private GeoMeanPlotMenu geoMeanPlotMenu;
    private Biplot3dMenu biplot3dMenu;
    private ILRCLRPlotMenu iLRCLRPlotMenu;
    private DendrogramMenu dendrogramMenu;
    private ClasUniNormTestMenu clasUniNormTestMenu;
    private ChangeGroupNameMenu changeGroupNameMenu;
    private ConfigurationMenu configurationMenu;

    public static CoDaPackConf config = new CoDaPackConf();
    
    // Mac users expect the menu bar to be at the top of the screen, not in the
    // window, so enable that setting. (This is ignored on non-Mac systems).
    // Setting should be set in an static context
    static{
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "CoDaPack");
        System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
    }
    public CoDaPackMain() {
        screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        // Es carrega el logo del CoDaPack        
        initComponents();
        this.setIconImage(
                Toolkit.getDefaultToolkit().
                        getImage(
                getClass().getResource(CoDaPackMain.RESOURCE_PATH + "logoL.png")));
        outputPanel.addWelcome(CoDaPackConf.getVersion());
    }
    public void closeApplication(){
        CoDaPackConf.saveConfiguration();
        System.exit(0);
    }
    private void initComponents() {
        ITEM_APPLICATION_NAME = "CoDaPack v" + CoDaPackConf.getVersion();
        outputPanel = new OutputPanel();
        tablePanel = new TablePanel(this);
        
        //Panel with the active variable
        dataList = new DataList();
        jMenuBar = new CoDaPackMenu();
        jMenuBar.addCoDaPackMenuListener(new CoDaPackMenuListener(){
            @Override
            public void menuItemClicked(String v) {
                try {
                    eventCoDaPack(v);
                } catch (ScriptException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        setTitle(ITEM_APPLICATION_NAME);
        setPreferredSize(new Dimension(1000,700));
        setLocation((screenDimension.width-1000)/2,
                    (screenDimension.height-700)/2);

        dataList.setSize(new Dimension(150, 700));
        dataFrameSelector = new JComboBox();
        dataFrameSelector.setPrototypeDisplayValue("XXXXXXXXXXXXXX");
        dataFrameSelector.addItemListener(dataFrameListener);

        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BorderLayout());
        JPanel dfSelect = new JPanel();
        dfSelect.add(new JLabel("Tables"));
        dfSelect.add(dataFrameSelector);
        westPanel.add(dfSelect, BorderLayout.NORTH);
        westPanel.add(dataList, BorderLayout.CENTER);

        jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, outputPanel, tablePanel);
        jSplitPane.setDividerSize(7);
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setDividerLocation(350);

        JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westPanel, jSplitPane);
        main.setDividerSize(7);    
        getContentPane().add(main, BorderLayout.CENTER);
        
        setJMenuBar(jMenuBar);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
       
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                jMenuBar.itemQuit.doClick();
            }
        });        
        pack();
    }
    public boolean isDataFrameNameAvailable(String name){
        for(DataFrame df : dataFrame)
            if(df.name.equals(name)) return false;
        return true;
    }
    public void removeDataFrame(DataFrame df){
        dataFrameSelector.removeItemListener(dataFrameListener);
        dataFrameSelector.removeItem(df.name);
        dataFrame.remove(df);
        int s = dataFrame.size();
        if( s != 0){
            activeDataFrame = 0;
            DataFrame new_df = dataFrame.get(activeDataFrame);
            dataList.setData(new_df);
            tablePanel.setDataFrame(new_df);
            dataFrameSelector.setSelectedItem(new_df.name);
            dataFrameSelector.addItemListener(dataFrameListener);
        }else{
            activeDataFrame = -1;
            dataList.clearData();
            tablePanel.clearData();
            dataFrameSelector.removeAllItems();
        }
    }
    public void addDataFrame(DataFrame df){
        if(isDataFrameNameAvailable(df.name)){
            activeDataFrame = dataFrame.size();
            dataFrame.add(df);
            dataList.setData(df);
            tablePanel.setDataFrame(df);
            dataFrameSelector.removeItemListener(dataFrameListener);
            dataFrameSelector.addItem(df.name);
            dataFrameSelector.setSelectedItem(df.name);
            dataFrameSelector.addItemListener(dataFrameListener);
            df.setChange(false);
        }else{
            JOptionPane.showMessageDialog(this,"<html>Dataframe <i>" +
                    df.name + "</i> is already loaded.</html>");
        }
    }
    public void addDataFrameRDR(DataFrame df){
        if(isDataFrameNameAvailable(df.name)){
            activeDataFrame = dataFrame.size();
            dataFrame.add(df);
            dataList.setData(df);
            tablePanel.setDataFrame(df);
            dataFrameSelector.removeItemListener(dataFrameListener);
            dataFrameSelector.addItem(df.name);
            dataFrameSelector.setSelectedItem(df.name);
            dataFrameSelector.addItemListener(dataFrameListener);
            df.setChange(false);
        }else{
            JOptionPane.showMessageDialog(this,"<html>Dataframe <i>" +
                    df.name + "</i> is already loaded.<br/>Please, use Prefix or Suffix Options.</html>");
        }
    }
    public void updateDataFrame(DataFrame df){
        dataList.setData(df);
        tablePanel.setDataFrame(df);
    }
    public ArrayList<DataFrame> getAllDataFrames(){
        return dataFrame;
    }
    public DataFrame getActiveDataFrame(){
        if(activeDataFrame == -1) return null;
        return dataFrame.get(activeDataFrame);
    }
    public void eventCoDaPack(String action) throws ScriptException{
        String title = action;
        String ruta = CoDaPackConf.lastPath; //fillRecentPath();
        JFileChooser chooseFile = new JFileChooser(ruta);
        if(title.equals(jMenuBar.ITEM_IMPORT_XLS)){
            chooseFile.resetChoosableFileFilters();
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("Excel files", "xls", "xlsx"));
            if(chooseFile.showOpenDialog(jSplitPane) ==
                    JFileChooser.APPROVE_OPTION){
                ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
                ImportXLSMenu importMenu = new ImportXLSMenu(this, true, chooseFile);
                importMenu.setVisible(true);
                DataFrame df = importMenu.getDataFrame();
                if( df != null) {
                    addDataFrame(df);
                }
                importMenu.dispose();
                CoDaPackConf.lastPath = ruta;
            }
        }else if(title.equals(jMenuBar.ITEM_IMPORT_RDA)){
            //Aquí tractem l'event IMPORT_RDA
            chooseFile.resetChoosableFileFilters();
            //Filtrem per llegir només els arxius RDA
            chooseFile.setFileFilter(new FileNameExtensionFilter("R data file", "RData", "rda"));

            //Comprovem si es selecciona un arxiu aprovat
            if(chooseFile.showOpenDialog(jSplitPane) == JFileChooser.APPROVE_OPTION){
                //Creem una nova instància ImportRDA, serà l'encarregada de mostrar i obrir els dataframes
                ImportRDA impdf = new ImportRDA(chooseFile);
                //Creem una nova instància ImportRDAMenu, serà l'encarregada de gestionar el menú
                ImportRDAMenu imprdam = new ImportRDAMenu(this, chooseFile, impdf);
                //Fem el menú visible
                imprdam.setVisible(true,true);
                
            }
            //Copiem la ruta per recordar-la
            ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
            //Guardem la ruta
            CoDaPackConf.lastPath = ruta;
        }else if(title.equals(jMenuBar.ITEM_IMPORT_CSV)){
            chooseFile.resetChoosableFileFilters();
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("Text file", "txt"));
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("CSV file", "csv"));
            
            if(chooseFile.showOpenDialog(jSplitPane) ==
                    JFileChooser.APPROVE_OPTION){

                ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
                ImportCSVMenu importMenu = new ImportCSVMenu(this, true, chooseFile);
                importMenu.setVisible(true);
                DataFrame df = importMenu.getDataFrame();
                if( df != null) {
                    addDataFrame(df);
                }
                importMenu.dispose();
                CoDaPackConf.lastPath = ruta;
            }
        }else if(title.equals(jMenuBar.ITEM_EXPORT_CSV)){
            if(this.getActiveDataFrame() != null){
                chooseFile.resetChoosableFileFilters();
                chooseFile.setFileFilter(new FileNameExtensionFilter("Text file", "txt"));
                chooseFile.setFileFilter(new FileNameExtensionFilter("CSV file", "csv"));
                
                DataFrame df = this.getActiveDataFrame();
                
                for(int i=0; i < df.size(); i++){
                    re.eval(df.get(i).getName() + " <- NULL");
                
                    if(df.get(i).isNumeric()){
                        for(double j : df.get(i).getNumericalData()){
                            re.eval(df.get(i).getName() + " <- c(" + df.get(i).getName() +"," + String.valueOf(j) + ")");
                        }
                    }
                    else{
                        for(String j : df.get(i).getTextData()){
                            re.eval(df.get(i).getName() + " <- c(" + df.get(i).getName() +",'" + j + "')");
                        }
                    }
                }
            
                String dataFrameString = "mydf <- data.frame(";
                
                for(int i=0; i < df.size(); i++){
                    dataFrameString += df.get(i).getName();
                    if(i != df.size()-1) dataFrameString += ",";
                }
            
                dataFrameString += ")";
                re.eval(dataFrameString); // creem el dataframe amb R

                if(chooseFile.showOpenDialog(jSplitPane) == JFileChooser.APPROVE_OPTION){
                    ruta = chooseFile.getCurrentDirectory().getAbsolutePath(); 
                    if(chooseFile.getFileFilter().getDescription().equals("CSV file")){
                        re.eval("utils::write.csv(mydf, \"" + ruta.replaceAll("\\\\", "/") + "/" + chooseFile.getSelectedFile().getName() + ".csv\", row.names = FALSE)");
                    }
                    else{
                        re.eval("utils::write.table(mydf, \"" + ruta.replaceAll("\\\\", "/") + "/" + chooseFile.getSelectedFile().getName() + ".txt\", row.names = FALSE)");
                    }
                }
                CoDaPackConf.lastPath = ruta;
            }
            else{
                JOptionPane.showMessageDialog(null, "No data to export");
            }
        }else if(title.equals(jMenuBar.ITEM_EXPORT_XLS)){
            chooseFile.resetChoosableFileFilters();
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("Excel files", "xls"));
            if(chooseFile.showSaveDialog(jSplitPane) ==
                    JFileChooser.APPROVE_OPTION){

                try {
                    ExportData.exportXLS(chooseFile.getSelectedFile().getAbsolutePath(), dataFrame.get(activeDataFrame));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
            CoDaPackConf.lastPath = ruta;
        }else if(title.equals(jMenuBar.ITEM_EXPORT_R)) {
            new ExportRDataMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_OPEN)){
            if (!dataFrame.isEmpty()) {
                //Comprovar si hi ha canvis. si n'hi ha finestra
                boolean hasChange = false;
                Iterator<DataFrame> i = dataFrame.iterator();
                while (hasChange == false && i.hasNext()) {
                    DataFrame df = i.next();
                    if (df.getChange()) hasChange = true;
                }
                if (hasChange) {
                    int response = JOptionPane.showConfirmDialog(this, "<html>Your changes will be lost if you close <br/>Do you want to continue?</html>", "Confirm",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION){
                        dataFrame.clear();
                        activeDataFrame = -1;
                        jMenuBar.active_path = null;
                        dataList.clearData();
                        tablePanel.clearData();
                        dataFrameSelector.removeAllItems();
                        CoDaPackImporter imp = new CoDaPackImporter().setParameters(this);
                        ArrayList<DataFrame> dfs = imp.importDataFrames();
                        for (DataFrame df : dfs) {
                            addDataFrame(df);
                        }
                        jMenuBar.fillRecentFiles();
                        jMenuBar.saveRecentFile(imp.getParameters());
                        String fn = imp.getParameters();
                        if (fn.startsWith("format:codapack?")) {
                            jMenuBar.active_path = fn.substring(16);
                        } else jMenuBar.active_path = fn;
                    }
                }
                else {
                    dataFrame.clear();
                    activeDataFrame = -1;
                    jMenuBar.active_path = null;
                    dataList.clearData();
                    tablePanel.clearData();
                    dataFrameSelector.removeAllItems();
                    CoDaPackImporter imp = new CoDaPackImporter().setParameters(this);
                    ArrayList<DataFrame> dfs = imp.importDataFrames();
                    for (DataFrame df : dfs) {
                        addDataFrame(df);
                    }
                    jMenuBar.fillRecentFiles();
                    jMenuBar.saveRecentFile(imp.getParameters());
                    String fn = imp.getParameters();
                    if (fn.startsWith("format:codapack?")) {
                        jMenuBar.active_path = fn.substring(16);
                    } else jMenuBar.active_path = fn;
                }
            }else {
                CoDaPackImporter imp = new CoDaPackImporter().setParameters(this);
                ArrayList<DataFrame> dfs = imp.importDataFrames();
                for (DataFrame df : dfs) {
                    addDataFrame(df);
                }
                jMenuBar.fillRecentFiles();
                jMenuBar.saveRecentFile(imp.getParameters());
                String fn = imp.getParameters();
                if (fn.startsWith("format:codapack?")) {
                    jMenuBar.active_path = fn.substring(16);
                } else jMenuBar.active_path = fn;
            }
        }else if(title.equals(jMenuBar.ITEM_ADD)){
            CoDaPackImporter imp = new CoDaPackImporter().setParameters(this);
            ArrayList<DataFrame> dfs = imp.importDataFrames();
            for(DataFrame df: dfs) {
                addDataFrame(df);
            }
            jMenuBar.fillRecentFiles();
            jMenuBar.saveRecentFile(imp.getParameters());
            //jMenuBar.addRecentFile(imp.getParameters());
        }else if("format:codapack".equals(action.split("\\?")[0])){
            CoDaPackImporter imp = new CoDaPackImporter().setParameters(action);
            ArrayList<DataFrame> dfs = imp.importDataFrames();
            for(DataFrame df: dfs) {
                addDataFrame(df);
            }
            jMenuBar.fillRecentFiles();
            jMenuBar.saveRecentFile(imp.getParameters());
            //jMenuBar.addRecentFile(imp.getParameters());
        }else if(title.equals(jMenuBar.ITEM_CLEAR_RECENT)) {
            jMenuBar.removeRecentFiles();
        }
        else if(title.equals(jMenuBar.ITEM_SAV)){
            if (jMenuBar.active_path != null) {
                String fileNameExt = ".cdp";
                String fileName = jMenuBar.active_path;
                String fn;
                if (fileName.endsWith(".xls") || fileName.endsWith(".rda") || fileName.endsWith(".cdp") || fileName.endsWith(".rda") || fileName.endsWith(".txt") || fileName.endsWith(".csv")) {
                    fn = fileName.substring(0, fileName.length() - 4);
                } else if (fileName.endsWith(".xlsx")) fn = fileName.substring(0, fileName.length() - 5);
                else if (fileName.endsWith(".RData")) fn = fileName.substring(0, fileName.length() - 6);
                else fn = fileName + fileNameExt;
                try {
                    WorkspaceIO.saveWorkspace(fn + fileNameExt, this);
                    jMenuBar.saveRecentFile(fn + fileNameExt);
                    //Posem el valor de change de tots els dataframes a false
                    Iterator<DataFrame> i = dataFrame.iterator();
                    while (i.hasNext()) {
                        DataFrame df = i.next();
                        df.setChange(false);
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
            else {
                chooseFile.resetChoosableFileFilters();
                chooseFile.setFileFilter(
                        new FileNameExtensionFilter("CoDaPack Workspace", "cdp"));
                if( chooseFile.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
                    String filename = chooseFile.getSelectedFile().getAbsolutePath();
                    try {
                        WorkspaceIO.saveWorkspace(
                                filename.endsWith(".cdp") ? filename : filename + ".cdp", this);
                        ruta = filename + ".cdp";
                        jMenuBar.saveRecentFile(ruta);
                        //Posem el valor de change de tots els dataframes a false
                        Iterator<DataFrame> i = dataFrame.iterator();
                        while (i.hasNext()) {
                            DataFrame df = i.next();
                            df.setChange(false);
                        }
                    } catch (JSONException ex) {
                        Logger.getLogger(CoDaPackMain.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                }
                ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
                CoDaPackConf.lastPath = ruta;
            }
        }else if(title.equals(jMenuBar.ITEM_SAVE)){
            chooseFile.resetChoosableFileFilters();
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("CoDaPack Workspace", "cdp"));
            if( chooseFile.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
                String filename = chooseFile.getSelectedFile().getAbsolutePath();
                try {
                    WorkspaceIO.saveWorkspace(
                            filename.endsWith(".cdp") ? filename : filename + ".cdp", this);
                ruta = filename + ".cdp";
                jMenuBar.saveRecentFile(ruta);
                    //Posem el valor de change de tots els dataframes a false
                    Iterator<DataFrame> i = dataFrame.iterator();
                    while (i.hasNext()) {
                        DataFrame df = i.next();
                        df.setChange(false);
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
            ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
            CoDaPackConf.lastPath = ruta;
        }else if(title.equals(jMenuBar.ITEM_DEL_DATAFRAME)){
            if( dataFrame.size() > 0 ){
                removeDataFrame(dataFrame.get(activeDataFrame));
            }else{
                JOptionPane.showMessageDialog(this, "No table available");
            }
        }else if(title.equals(jMenuBar.ITEM_DELETE_ALL_TABLES)){
            int responseDeleteAllTables = JOptionPane.showConfirmDialog(null, "Are you sure to delete all the tables?","Delete All Tables", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            if(responseDeleteAllTables == JOptionPane.YES_OPTION){
                while(dataFrame.size() > 0){
                    DataFrame aux = dataFrame.get(0);
                    this.removeDataFrame(aux);
                }
            }
        }else if(title.equals(jMenuBar.ITEM_CLEAR_OUTPUTS)){
            int responseCleanOutput = JOptionPane.showConfirmDialog(null, "Are you sure to clean the output?","Clean the output", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(responseCleanOutput == JOptionPane.YES_OPTION){
                outputPanel.clearOutput();
            }
        }else if(title.equals(jMenuBar.ITEM_QUIT)){
            jMenuBar.copyRecentFiles();
            //Comprovar si hi ha canvis. si n'hi ha finestra
            boolean hasChange = false;
            Iterator<DataFrame> i = dataFrame.iterator();
            while (hasChange == false && i.hasNext()) {
                DataFrame df = i.next();
                if (df.getChange()) hasChange = true;
            }
            if (hasChange) {
                int response = JOptionPane.showConfirmDialog(this, "<html>Your changes will be lost if you close <br/>Do you want to exit?</html>", "Confirm",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    int responseSaveHTML = JOptionPane.showConfirmDialog(null, "Do you want to save the session?", "Save the session",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(responseSaveHTML == JOptionPane.NO_OPTION){
                        outputPanel.deleteHtml();
                    }
                    dispose();
                    this.closeApplication();
                }
            }else {
                int response = JOptionPane.showConfirmDialog(this, "Do you want to exit?", "Confirm",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    int responseSaveHTML = JOptionPane.showConfirmDialog(null, "Do you want to save the session?", "Save the session",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(responseSaveHTML == JOptionPane.NO_OPTION){
                        outputPanel.deleteHtml();
                    }
                    dispose();
                    this.closeApplication();
                }
            }
        }else if(title.equals(jMenuBar.ITEM_CONF)){
            if(configurationMenu == null) configurationMenu = new ConfigurationMenu(this);
            configurationMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_CREATE_FRAME)){
            new DataFrameCreator(this);
        }else if(title.equals(jMenuBar.ITEM_RAW_ALR)){
            if(transformationALRMenu == null || transformationALRMenu.getDataFrame() != this.getActiveDataFrame() || !transformationALRMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) transformationALRMenu = new TransformationALRMenu(this);
            transformationALRMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_RAW_CLR)){
            if(transformationCLRMenu == null || transformationCLRMenu.getDataFrame() != this.getActiveDataFrame() || !transformationCLRMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) transformationCLRMenu = new TransformationCLRMenu(this);
            transformationCLRMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_T_RAW_ILR)) {
            if(transformationRawILRMenu == null || transformationRawILRMenu.getDataFrame() != this.getActiveDataFrame() || !transformationRawILRMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) transformationRawILRMenu = new TransformationRawILRMenu(this);
            transformationRawILRMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_T_ILR_RAW)) {
            if(transformationILRRawMenu == null || transformationILRRawMenu.getDataFrame() != this.getActiveDataFrame() || !transformationILRRawMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) transformationILRRawMenu = new TransformationILRRawMenu(this);
            transformationILRRawMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_DISCRETIZE)){
            if(discretizeMenu == null || discretizeMenu.getDataFrame() != this.getActiveDataFrame() || !discretizeMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) discretizeMenu = new DiscretizeMenu(this,re); 
            discretizeMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_CLAS_STATS_SUMMARY)){
            if(clasStatsSummaryMenu == null || clasStatsSummaryMenu.getDataFrame() != this.getActiveDataFrame() || !clasStatsSummaryMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) clasStatsSummaryMenu = new ClasStatsSummaryMenu(this);
            clasStatsSummaryMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_REG_XREAL_YCOMP)){
            if(LM1 == null || LM1.getDataFrame() != this.getActiveDataFrame() || !LM1.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) LM1 = new LM1(this,re);
            LM1.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_REG_XCOMP_YREAL)){
            if(LM2 == null || LM2.getDataFrame() != this.getActiveDataFrame() || !LM2.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) LM2 = new LM2(this,re);
            LM2.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_CLUST_KMEANS)){
            if(clusterMenu == null || clusterMenu.getDataFrame() != this.getActiveDataFrame() || !clusterMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) clusterMenu = new ClusterMenu(this,re);
            clusterMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_MANOVA)){
            if(manovaMenu == null || manovaMenu.getDataFrame() != this.getActiveDataFrame() || !manovaMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) manovaMenu = new ManovaMenu(this,re);
            manovaMenu.setVisible(true);
        }else if(title.contains(jMenuBar.ITEM_DISC_ANALYSIS)){
            if(discriminantMenu == null || discriminantMenu.getDataFrame() != this.getActiveDataFrame() || !discriminantMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) discriminantMenu = new DiscriminantMenu(this,re);
            discriminantMenu.setVisible(true);        
        }else if(title.equals(jMenuBar.ITEM_COMP_STATS_SUMMARY)){
            if(compStatsSummaryMenu == null || compStatsSummaryMenu.getDataFrame() != this.getActiveDataFrame() || !compStatsSummaryMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) compStatsSummaryMenu = new CompStatsSummaryMenu(this); 
            compStatsSummaryMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_NORM_TEST)){
            if(normalityTestMenu == null || normalityTestMenu.getDataFrame() != this.getActiveDataFrame() || !normalityTestMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) normalityTestMenu = new NormalityTestMenu(this);
            normalityTestMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_CLAS_UNI_NORM_TEST)){
            if(clasUniNormTestMenu == null || clasUniNormTestMenu.getDataFrame() != this.getActiveDataFrame() || !clasUniNormTestMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) clasUniNormTestMenu = new ClasUniNormTestMenu(this,re);
            clasUniNormTestMenu.setVisible(true);
        }
        else if(title.equals(jMenuBar.ITEM_ATIP_INDEX)){
            if(atypMenu == null || atypMenu.getDataFrame() != this.getActiveDataFrame() || !atypMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) atypMenu = new AtypMenu(this,re);
            atypMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_CENTER)){
            if(centerDataMenu == null || centerDataMenu.getDataFrame() != this.getActiveDataFrame() || !centerDataMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) centerDataMenu = new CenterDataMenu(this); 
            centerDataMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_CLOSURE)){
            if(closureDataMenu == null || closureDataMenu.getDataFrame() != this.getActiveDataFrame() || !closureDataMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) closureDataMenu = new ClosureDataMenu(this);
            closureDataMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_CALCULATE_NEW_VAR)){
            if(calculateNewVarMenu == null || calculateNewVarMenu.getDataFrame() != this.getActiveDataFrame() || !calculateNewVarMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) calculateNewVarMenu = new CalculateNewVarMenu(this,re);
            calculateNewVarMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_AMALGAM)){
            new AmalgamationDataMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_PERTURBATE)){
            if(perturbateDataMenu == null || perturbateDataMenu.getDataFrame() != this.getActiveDataFrame() || !perturbateDataMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) perturbateDataMenu = new PerturbateDataMenu(this);
            perturbateDataMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_POWER)){
            if(powerDataMenu == null || powerDataMenu.getDataFrame() != this.getActiveDataFrame() || !powerDataMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) powerDataMenu = new PowerDataMenu(this);
            powerDataMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_ZEROS)){
            if(zeroReplacementMenu == null || zeroReplacementMenu.getDataFrame() != this.getActiveDataFrame() || !zeroReplacementMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) zeroReplacementMenu = new ZeroReplacementMenu(this);
            zeroReplacementMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_ZEROS_R)){
            if(zeroReplacementRMenu == null || zeroReplacementRMenu.getDataFrame() != this.getActiveDataFrame() || !zeroReplacementRMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) zeroReplacementRMenu = new ZeroReplacementRMenu(this,re);
            zeroReplacementRMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_LOG_RATIO)){
            if(logRatioEMMenu == null || logRatioEMMenu.getDataFrame() != this.getActiveDataFrame() || !logRatioEMMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) logRatioEMMenu = new LogRatioEMMenu(this,re);
            logRatioEMMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_BAYESIAN_MULT_REPLACE)){
            if(bayesianMultRepMenu == null || bayesianMultRepMenu.getDataFrame() != this.getActiveDataFrame() || !bayesianMultRepMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) bayesianMultRepMenu = new BayesianMultRepMenu(this,re);
            bayesianMultRepMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_EM_MISSING)){
            if(EM_MissingMenu == null || EM_MissingMenu.getDataFrame() != this.getActiveDataFrame() || !EM_MissingMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) EM_MissingMenu = new EM_MissingMenu(this,re);
            EM_MissingMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_EM_ZERO_MISSING)){
            if(EM_ZeroMissingMenu == null || EM_ZeroMissingMenu.getDataFrame() != this.getActiveDataFrame() || !EM_ZeroMissingMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) EM_ZeroMissingMenu = new EM_ZeroMissingMenu(this,re);
            EM_ZeroMissingMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_SORT_DATA)){
            if(sortDataMenu == null || sortDataMenu.getDataFrame() != this.getActiveDataFrame() || !sortDataMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) sortDataMenu = new SortDataMenu(this,re);
            sortDataMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_FILTER)){
            if(filterMenu == null || filterMenu.getDataFrame() != this.getActiveDataFrame() || !filterMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) filterMenu = new FilterMenu(this);
            filterMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_ADV_FILTER)){
            if(advancedFilterMenu == null || advancedFilterMenu.getDataFrame() != this.getActiveDataFrame() || !advancedFilterMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) advancedFilterMenu = new AdvancedFilterMenu(this,re);
            advancedFilterMenu.setVisible(true);
        }else if (title.equals(jMenuBar.ITEM_SETDETECTION)){
            if(setDetectionLimitMenu == null || setDetectionLimitMenu.getDataFrame() != this.getActiveDataFrame() || !setDetectionLimitMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) setDetectionLimitMenu = new SetDetectionLimitMenu(this);  
            setDetectionLimitMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_CHANGE_CAT_NAME_GROUP)){
            if(changeGroupNameMenu == null || changeGroupNameMenu.getDataFrame() != this.getActiveDataFrame() || !changeGroupNameMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) changeGroupNameMenu = new ChangeGroupNameMenu(this);
            changeGroupNameMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_TERNARY_PLOT)){
            if(ternaryPlotMenu == null || ternaryPlotMenu.getDataFrame() != this.getActiveDataFrame() || !ternaryPlotMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) ternaryPlotMenu = new TernaryPlotMenu(this);
            ternaryPlotMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_PRED_REG_PLOT)){
            if(predictiveRegionMenu == null || predictiveRegionMenu.getDataFrame() != this.getActiveDataFrame() || !predictiveRegionMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) predictiveRegionMenu = new PredictiveRegionMenu(this);
            predictiveRegionMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_CONF_REG_PLOT)){
            if(confidenceRegionMenu == null || confidenceRegionMenu.getDataFrame() != this.getActiveDataFrame() || !confidenceRegionMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) confidenceRegionMenu = new ConfidenceRegionMenu(this);
            confidenceRegionMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_BOXPLOT)){
            if(boxplotMenu == null || boxplotMenu.getDataFrame() != this.getActiveDataFrame() || !boxplotMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) boxplotMenu = new BoxplotMenu(this,re);
            boxplotMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_SCATTERPLOT)){
            if(scatterplotMenu == null || scatterplotMenu.getDataFrame() != this.getActiveDataFrame() || !scatterplotMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) scatterplotMenu = new ScatterplotMenu(this,re);
            scatterplotMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_ZPATTERNS)){
            if(zpatternsMenu == null || zpatternsMenu.getDataFrame() != this.getActiveDataFrame() || !zpatternsMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) zpatternsMenu = new ZpatternsMenu(this,re);
            zpatternsMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_GEO_MEAN_PLOT)){
            if(geoMeanPlotMenu == null || geoMeanPlotMenu.getDataFrame() != this.getActiveDataFrame() || !geoMeanPlotMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) geoMeanPlotMenu = new GeoMeanPlotMenu(this,re);
            geoMeanPlotMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_EMPTY_TERNARY_PLOT)){
            String names[] = {"X", "Y", "Z"};
            TernaryPlot2dDisplay display = new TernaryPlot2dDisplay(names);

            double definedGrid[] =
        {0.01, 0.05, 0.10, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99};
            Ternary2dObject gridObject = new Ternary2dGridObject(display, definedGrid);
            display.addCoDaObject(gridObject);
            double center[] = {1,1,1};

            TernaryPlot2dWindow frame = new TernaryPlot2dWindow(this.getActiveDataFrame(), display, "Ternary/Quaternary Plot -- Testing version");
            frame.setLocationRelativeTo(this);
            frame.setCenter(center);
            frame.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_BIPLOT)){
            if(biplot3dMenu == null || biplot3dMenu.getDataFrame() != this.getActiveDataFrame() || !biplot3dMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) biplot3dMenu = new Biplot3dMenu(this);
            biplot3dMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_ILR_BIPLOT)){
            if(iLRCLRPlotMenu == null || iLRCLRPlotMenu.getDataFrame() != this.getActiveDataFrame() || !iLRCLRPlotMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) iLRCLRPlotMenu = new ILRCLRPlotMenu(this);
            iLRCLRPlotMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_DENDROGRAM_PLOT)){
            if(dendrogramMenu == null || dendrogramMenu.getDataFrame() != this.getActiveDataFrame() || !dendrogramMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) dendrogramMenu = new DendrogramMenu(this); 
            dendrogramMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_ALR_PLOT)){
            new ALRPlotMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_CLR_PLOT)){
            new CLRPlotMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_ILR_PLOT)){
            new ILRPlotMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_ADD_VAR)){
            new AddMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_DEL_VAR)){
            new DeleteMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_CAT_VAR)){
            new Numeric2CategoricMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_NUM_VAR)){
            new Categoric2NumericMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_PC_PLOT)){
            if(principalComponentMenu == null || principalComponentMenu.getDataFrame() != this.getActiveDataFrame() || !principalComponentMenu.getDataFrameNames().equals(this.getActiveDataFrame().getNames())) principalComponentMenu = new PrincipalComponentMenu(this);
            principalComponentMenu.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_FORCE_UPDATE)){
            CoDaPackConf.refusedVersion = CoDaPackConf.CoDaVersion;
            UpdateConnection uc = new UpdateConnection(this);
            new Thread(uc).start();
        }else if(title.equals(jMenuBar.ITEM_ABOUT)){
            new CoDaPackAbout(this).setVisible(true);
        }else if(title.equals(jMenuBar.R_TEST)){
            // first we get the session info
            re.eval("a <- capture.output(sessionInfo())");
            OutputElement e = new OutputForR(re.eval("a").asStringArray());
            outputPanel.addOutput(e);
            
            
            // after we get the system variables
            
            re.eval("a <- capture.output(Sys.getenv())");
            e = new OutputForR(re.eval("a").asStringArray());
            outputPanel.addOutput(e);
            
            // finally the capabilities
            
            re.eval("a <- capture.output(capabilities())");
            e = new OutputForR(re.eval("a").asStringArray());
            outputPanel.addOutput(e);
        }else if(title.equals(jMenuBar.ITEM_MODEL_S0)){
            new S0(this,re).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_MODEL_S1)){
            new S1(this,re).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_MODEL_S2)){
            new S2(this,re).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_MODEL_S3)){
            new S3(this,re).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_MODEL_S4)){
            new S4(this,re).setVisible(true);
        }
    }
    public class DataFrameSelectorListener implements ItemListener{
        public void itemStateChanged(ItemEvent ie) {
            JComboBox combo = (JComboBox)ie.getSource();
            if(activeDataFrame != combo.getSelectedIndex()){
                activeDataFrame = combo.getSelectedIndex();
                dataList.setData(dataFrame.get(activeDataFrame));
                tablePanel.setDataFrame(dataFrame.get(activeDataFrame));
                dataFrame.get(activeDataFrame).setChange(true);
            }
        }
    }    
    
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]){
        /*
         * Look and Feel: change appearence according to OS
         */

       try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CoDaPackMain.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CoDaPackMain.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CoDaPackMain.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(CoDaPackMain.class.getName())
                    .log(Level.SEVERE, null, ex);
        } 
        
        /*
         *  Congifuration file creation if it not exists. Using static class CoDaPackConf
         */
        File f = new File(CoDaPackConf.configurationFile);
        if(f.exists())
            CoDaPackConf.loadConfiguration();
        else
            CoDaPackConf.saveConfiguration();
        
        /*
         * CoDaPack connects to IMA server through a thread. Avoiding problems in IMA server
         */
                
        
        /*
         * CoDaPack main class is created and shown
         */
        CoDaPackMain main = new CoDaPackMain();
        
        //UpdateConnection uc = new UpdateConnection(main);
        //new Thread(uc).start();
        
        main.setVisible(true);
        
        //Si s'ha clicat un arxiu associat ens arribarà com argument i el tractem
        if (args.length>0) {
            //Guardem la ruta i l'arxiu a recent files
            main.jMenuBar.saveRecentFile(args[0]);
            //Obrim l'arxiu 
            CoDaPackImporter imp = new CoDaPackImporter().setParameters("format:codapack?" + args[0]);
            ArrayList<DataFrame> dfs = imp.importDataFrames();
            
            for(DataFrame df: dfs) {
                main.addDataFrame(df);
            }
        }    
    }
    
    public static class UpdateConnection implements Runnable{
        CoDaPackMain main;
        public UpdateConnection(CoDaPackMain main){
            this.main = main;
        }
        public void run() {
            checkForUpdate();
        }
        public void redirectForUpdating(String newversion){
            Object[] options = {"Quit and download...",
                "No, thanks. Maybe later",
                "No, thanks. Stop asking"};
            int n = JOptionPane.showOptionDialog(main,
                    "CoDaPack " + 
                            newversion.replace(' ', '.') + 
                            " is now available (you're using " + 
                            CoDaPackConf.CoDaVersion.replace(' ','.') +
                            ").",
                    "Update Available",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            if(n == JOptionPane.YES_OPTION){
                try {
                    Desktop.getDesktop().browse(new URI("http://mcomas.net/codapack"));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
                }
                main.closeApplication();
            }
            if(n == JOptionPane.CANCEL_OPTION){
                CoDaPackConf.refusedVersion = newversion;
                CoDaPackConf.saveConfiguration();
            }
        }
        public void checkForUpdate(){
                try {
                    JSONObject serverData = null;

                    System.out.println("Connecting to server...");
                    URL url = new URL(CoDaPackConf.HTTP_ROOT + "codapack-updater.json");

                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                    try {
                        serverData = new JSONObject(br.readLine());
                    } catch (JSONException ex) {
                        Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    br.close();
                    System.out.println("Server version is: " + serverData.getString("codapack-version"));
                    if(CoDaPackConf.updateNeeded(serverData.getString("codapack-version")))
                        redirectForUpdating(serverData.getString("codapack-version"));
                    else JOptionPane.showMessageDialog(null, "No update available");
                } catch (MalformedURLException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
}
