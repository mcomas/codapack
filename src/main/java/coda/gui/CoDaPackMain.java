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
import coda.gui.table.Table;
import coda.util.Node;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



/**
 *
 * @author mcomas
 */
public final class CoDaPackMain extends Application{
    private final ArrayList<DataFrame> dataFrame = new ArrayList<DataFrame>();
    private int activeDataFrame = -1;
    public ArrayList<DataFrame> getAllDataFrames(){
        return dataFrame;
    }
    public void addDataFrame(DataFrame df){
//        if(isDataFrameNameAvailable(df.name)){
//            activeDataFrame = dataFrame.size();
//            dataFrame.add(df);
//            dataList.setData(df);
//            tablePanel.setDataFrame(df);
//            dataFrameSelector.removeItemListener(dataFrameListener);
//            dataFrameSelector.addItem(df.name);
//            dataFrameSelector.setSelectedItem(df.name);
//            dataFrameSelector.addItemListener(dataFrameListener);
//            df.setChange(false);
//        }else{
//            JOptionPane.showMessageDialog(this,"<html>Dataframe <i>" +
//                    df.name + "</i> is already loaded.</html>");
//        }
    }
    public MainMenu menu;
    public static Output outputPane;
    public static DataList datalistPane;
    public static Table tablePane;
    
    public static CoDaPackConf config = new CoDaPackConf();
    
    @Override 
    public void start(Stage stage) {

  
        datalistPane = new DataList();
        datalistPane.setPrefWidth(100);
        outputPane = new Output();
        tablePane = new Table(this);   
        
        SplitPane sp = new SplitPane();
        sp.setOrientation(Orientation.VERTICAL);
        final StackPane sp1 = new StackPane();
        sp1.getChildren().add(outputPane);
        final StackPane sp2 = new StackPane();
        sp2.getChildren().add(tablePane);
        sp.getItems().addAll(sp1, sp2);
      
        
        menu = new MainMenu();

        
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(datalistPane);
        borderPane.setCenter(sp);
        borderPane.setTop(menu);
        
        Scene scene = new Scene(borderPane, 800, 500, Color.WHITE);        
        stage.setTitle("CoDaPack"); 
        stage.setScene(scene); 
        stage.sizeToScene(); 
        stage.show();

        
        //((VBox) scene.getRoot()).getChildren().addAll(menuBar);
//        //Panel with the active variable
//        dataList = new DataList();
//        jMenuBar = new CoDaPackMenu();
//        jMenuBar.addCoDaPackMenuListener(new CoDaPackMenuListener(){
//            @Override
//            public void menuItemClicked(String v) {
//                try {
//                    eventCoDaPack(v);
//                } catch (ScriptException ex) {
//                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        addKeyListener( new KeyAdapter(){
//            @Override
//            public void keyTyped(KeyEvent e) {
//            }
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_R && e.getModifiers() == KeyEvent.CTRL_MASK) {
//                    runRCmd();
//                }              
//            }
//            @Override
//            public void keyReleased(KeyEvent e) {
//            }
//        });
//        
//        setFocusable(true);
//        
//        setTitle(ITEM_APPLICATION_NAME);
//        setPreferredSize(new Dimension(1000,700));
//        setLocation((screenDimension.width-1000)/2,
//                    (screenDimension.height-700)/2);
//
//        dataList.setSize(new Dimension(150, 700));
//        dataFrameSelector = new JComboBox();
//        dataFrameSelector.setPrototypeDisplayValue("XXXXXXXXXXXXXX");
//        dataFrameSelector.addItemListener(dataFrameListener);
//
//        JPanel westPanel = new JPanel();
//        westPanel.setLayout(new BorderLayout());
//        JPanel dfSelect = new JPanel();
//        dfSelect.add(new JLabel("Data Frames"));
//        dfSelect.add(dataFrameSelector);
//        westPanel.add(dfSelect, BorderLayout.NORTH);
//        westPanel.add(dataList, BorderLayout.CENTER);
//
//        jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, outputPanel, tablePanel);
//        jSplitPane.setDividerSize(7);
//        jSplitPane.setOneTouchExpandable(true);
//        jSplitPane.setDividerLocation(350);
//
//        JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westPanel, jSplitPane);
//        main.setDividerSize(7);    
//        getContentPane().add(main, BorderLayout.CENTER);
//        
//        setJMenuBar(jMenuBar);
//        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        
//        
//        addWindowListener(new WindowAdapter(){
//            @Override
//            public void windowClosing(WindowEvent e){
//                jMenuBar.itemQuit.doClick();
//            }
//        });        
//        pack();

    }
    public static void main(String[] args) {
        Application.launch(args);
    }
    
//    public static final long serialVersionUID = 1L;
//    // CoDaPack version
//    
//    // dataFrame is the class containing the data
//    
//
//    private final Dimension screenDimension;
//    

//    
//    
//    private JComboBox dataFrameSelector;
//    private final DataFrameSelectorListener dataFrameListener = new DataFrameSelectorListener();
//    
//
//    
//    private String ITEM_APPLICATION_NAME;
//    // Menu
//    
//
//
//    
//    
//    // Mac users expect the menu bar to be at the top of the screen, not in the
//    // window, so enable that setting. (This is ignored on non-Mac systems).
//    // Setting should be set in an static context
//    static{
//        System.setProperty("apple.laf.useScreenMenuBar", "true");
//        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "CoDaPack");
//        System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
//    }
//    public CoDaPackMain() {
//        screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
//        // Es carrega el logo del CoDaPack        
//        initComponents();
//        this.setIconImage(
//                Toolkit.getDefaultToolkit().
//                        getImage(
//                getClass().getResource(CoDaPackMain.RESOURCE_PATH + "logoL.png")));
//        outputPanel.addWelcome(CoDaPackConf.getVersion());
//    }
//    public void closeApplication(){
//        CoDaPackConf.saveConfiguration();
//        System.exit(0);
//    }
//    private void initComponents() {
//        ITEM_APPLICATION_NAME = "CoDaPack v" + CoDaPackConf.getVersion();
//        outputPanel = new OutputPanel();
//        tablePanel = new TablePanel();
//        
//        //Panel with the active variable
//        dataList = new DataList();
//        jMenuBar = new CoDaPackMenu();
//        jMenuBar.addCoDaPackMenuListener(new CoDaPackMenuListener(){
//            @Override
//            public void menuItemClicked(String v) {
//                try {
//                    eventCoDaPack(v);
//                } catch (ScriptException ex) {
//                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        addKeyListener( new KeyAdapter(){
//            @Override
//            public void keyTyped(KeyEvent e) {
//            }
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_R && e.getModifiers() == KeyEvent.CTRL_MASK) {
//                    runRCmd();
//                }              
//            }
//            @Override
//            public void keyReleased(KeyEvent e) {
//            }
//        });
//        
//        setFocusable(true);
//        
//        setTitle(ITEM_APPLICATION_NAME);
//        setPreferredSize(new Dimension(1000,700));
//        setLocation((screenDimension.width-1000)/2,
//                    (screenDimension.height-700)/2);
//
//        dataList.setSize(new Dimension(150, 700));
//        dataFrameSelector = new JComboBox();
//        dataFrameSelector.setPrototypeDisplayValue("XXXXXXXXXXXXXX");
//        dataFrameSelector.addItemListener(dataFrameListener);
//
//        JPanel westPanel = new JPanel();
//        westPanel.setLayout(new BorderLayout());
//        JPanel dfSelect = new JPanel();
//        dfSelect.add(new JLabel("Data Frames"));
//        dfSelect.add(dataFrameSelector);
//        westPanel.add(dfSelect, BorderLayout.NORTH);
//        westPanel.add(dataList, BorderLayout.CENTER);
//
//        jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, outputPanel, tablePanel);
//        jSplitPane.setDividerSize(7);
//        jSplitPane.setOneTouchExpandable(true);
//        jSplitPane.setDividerLocation(350);
//
//        JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westPanel, jSplitPane);
//        main.setDividerSize(7);    
//        getContentPane().add(main, BorderLayout.CENTER);
//        
//        setJMenuBar(jMenuBar);
//        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        
//        
//        addWindowListener(new WindowAdapter(){
//            @Override
//            public void windowClosing(WindowEvent e){
//                jMenuBar.itemQuit.doClick();
//            }
//        });        
//        pack();
//    }
//    public void runRCmd(){
//        JPanel pan=new JPanel();
//        pan.setLayout(new FlowLayout());
//        pan.add(new JLabel("Execute R command:"));
//        pan.add(new JTextField(20));
//        pan.add(new JButton("Execute"));
//
//        JDialog jd = new JDialog();
//        jd.setLocationRelativeTo(this);
//        jd.setSize(300, 100);
//        jd.add(pan);
//        jd.setVisible(true);
//    }
//    public boolean isDataFrameNameAvailable(String name){
//        for(DataFrame df : dataFrame)
//            if(df.name.equals(name)) return false;
//        return true;
//    }
//    public void removeDataFrame(DataFrame df){
//        dataFrameSelector.removeItemListener(dataFrameListener);
//        dataFrameSelector.removeItem(df.name);
//        dataFrame.remove(df);
//        int s = dataFrame.size();
//        if( s != 0){
//            activeDataFrame = 0;
//            DataFrame new_df = dataFrame.get(activeDataFrame);
//            dataList.setData(new_df);
//            tablePanel.setDataFrame(new_df);
//            dataFrameSelector.setSelectedItem(new_df.name);
//            dataFrameSelector.addItemListener(dataFrameListener);
//        }else{
//            activeDataFrame = -1;
//            dataList.clearData();
//            tablePanel.clearData();
//            dataFrameSelector.removeAllItems();
//        }
//    }

//    public void addDataFrameRDR(DataFrame df){
//        if(isDataFrameNameAvailable(df.name)){
//            activeDataFrame = dataFrame.size();
//            dataFrame.add(df);
//            dataList.setData(df);
//            tablePanel.setDataFrame(df);
//            dataFrameSelector.removeItemListener(dataFrameListener);
//            dataFrameSelector.addItem(df.name);
//            dataFrameSelector.setSelectedItem(df.name);
//            dataFrameSelector.addItemListener(dataFrameListener);
//            df.setChange(false);
//        }else{
//            JOptionPane.showMessageDialog(this,"<html>Dataframe <i>" +
//                    df.name + "</i> is already loaded.<br/>Please, use Prefix or Suffix Options.</html>");
//        }
//    }
//    public void updateDataFrame(DataFrame df){
//        dataList.setData(df);
//        tablePanel.setDataFrame(df);
//    }
//    public DataFrame getActiveDataFrame(){
//        if(activeDataFrame == -1) return null;
//        return dataFrame.get(activeDataFrame);
//    }
//    public void eventCoDaPack(String action) throws ScriptException{
//        String title = action;
//        String ruta = CoDaPackConf.lastPath; //fillRecentPath();
//        JFileChooser chooseFile = new JFileChooser(ruta);
//        if(title.equals(jMenuBar.ITEM_IMPORT_XLS)){
//            chooseFile.resetChoosableFileFilters();
//            chooseFile.setFileFilter(
//                    new FileNameExtensionFilter("Excel files", "xls", "xlsx"));
//            if(chooseFile.showOpenDialog(jSplitPane) ==
//                    JFileChooser.APPROVE_OPTION){
//                ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
//                ImportXLSMenu importMenu = new ImportXLSMenu(this, true, chooseFile);
//                importMenu.setVisible(true);
//                DataFrame df = importMenu.getDataFrame();
//                if( df != null) {
//                    addDataFrame(df);
//                }
//                importMenu.dispose();
//                CoDaPackConf.lastPath = ruta;
//            }
//        }else if(title.equals(jMenuBar.ITEM_IMPORT_RDA)){
//            //Aquí tractem l'event IMPORT_RDA
//            chooseFile.resetChoosableFileFilters();
//            //Filtrem per llegir només els arxius RDA
//            chooseFile.setFileFilter(new FileNameExtensionFilter("R data file", "RData", "rda"));
//
//            //Comprovem si es selecciona un arxiu aprovat
//            if(chooseFile.showOpenDialog(jSplitPane) == JFileChooser.APPROVE_OPTION){
//                //Creem una nova instància ImportRDA, serà l'encarregada de mostrar i obrir els dataframes
//                ImportRDA impdf = new ImportRDA(chooseFile);
//                //Creem una nova instància ImportRDAMenu, serà l'encarregada de gestionar el menú
//                ImportRDAMenu imprdam = new ImportRDAMenu(this, chooseFile, impdf);
//                //Fem el menú visible
//                imprdam.setVisible(true,true);
//                
//            }
//            //Copiem la ruta per recordar-la
//            ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
//            //Guardem la ruta
//            CoDaPackConf.lastPath = ruta;
//        }else if(title.equals(jMenuBar.ITEM_IMPORT_CSV)){
//            chooseFile.resetChoosableFileFilters();
//            chooseFile.setFileFilter(
//                    new FileNameExtensionFilter("Text file", "txt"));
//            chooseFile.setFileFilter(
//                    new FileNameExtensionFilter("CSV file", "csv"));
//            
//            if(chooseFile.showOpenDialog(jSplitPane) ==
//                    JFileChooser.APPROVE_OPTION){
//
//                ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
//                ImportCSVMenu importMenu = new ImportCSVMenu(this, true, chooseFile);
//                importMenu.setVisible(true);
//                DataFrame df = importMenu.getDataFrame();
//                if( df != null) {
//                    addDataFrame(df);
//                }
//                importMenu.dispose();
//                CoDaPackConf.lastPath = ruta;
//            }
//        }else if(title.equals(jMenuBar.ITEM_EXPORT_XLS)){
//            chooseFile.resetChoosableFileFilters();
//            chooseFile.setFileFilter(
//                    new FileNameExtensionFilter("Excel files", "xls"));
//            if(chooseFile.showSaveDialog(jSplitPane) ==
//                    JFileChooser.APPROVE_OPTION){
//
//                try {
//                    ExportData.exportXLS(chooseFile.getSelectedFile().getAbsolutePath(), dataFrame.get(activeDataFrame));
//                } catch (FileNotFoundException ex) {
//                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex) {
//                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
//            CoDaPackConf.lastPath = ruta;
//        }else if(title.equals(jMenuBar.ITEM_EXPORT_R)) {
//            new ExportRDataMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_OPEN)){
//            if (!dataFrame.isEmpty()) {
//                //Comprovar si hi ha canvis. si n'hi ha finestra
//                boolean hasChange = false;
//                Iterator<DataFrame> i = dataFrame.iterator();
//                while (hasChange == false && i.hasNext()) {
//                    DataFrame df = i.next();
//                    if (df.getChange()) hasChange = true;
//                }
//                if (hasChange) {
//                    int response = JOptionPane.showConfirmDialog(this, "<html>Your changes will be lost if you close <br/>Do you want to continue?</html>", "Confirm",
//                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                    if (response == JOptionPane.YES_OPTION){
//                        dataFrame.clear();
//                        activeDataFrame = -1;
//                        jMenuBar.active_path = null;
//                        dataList.clearData();
//                        tablePanel.clearData();
//                        dataFrameSelector.removeAllItems();
//                        CoDaPackImporter imp = new CoDaPackImporter().setParameters(this);
//                        ArrayList<DataFrame> dfs = imp.importDataFrames();
//                        for (DataFrame df : dfs) {
//                            addDataFrame(df);
//                        }
//                        jMenuBar.fillRecentFiles();
//                        jMenuBar.saveRecentFile(imp.getParameters());
//                        String fn = imp.getParameters();
//                        if (fn.startsWith("format:codapack?")) {
//                            jMenuBar.active_path = fn.substring(16);
//                        } else jMenuBar.active_path = fn;
//                    }
//                }
//                else {
//                    dataFrame.clear();
//                    activeDataFrame = -1;
//                    jMenuBar.active_path = null;
//                    dataList.clearData();
//                    tablePanel.clearData();
//                    dataFrameSelector.removeAllItems();
//                    CoDaPackImporter imp = new CoDaPackImporter().setParameters(this);
//                    ArrayList<DataFrame> dfs = imp.importDataFrames();
//                    for (DataFrame df : dfs) {
//                        addDataFrame(df);
//                    }
//                    jMenuBar.fillRecentFiles();
//                    jMenuBar.saveRecentFile(imp.getParameters());
//                    String fn = imp.getParameters();
//                    if (fn.startsWith("format:codapack?")) {
//                        jMenuBar.active_path = fn.substring(16);
//                    } else jMenuBar.active_path = fn;
//                }
//            }else {
//                CoDaPackImporter imp = new CoDaPackImporter().setParameters(this);
//                ArrayList<DataFrame> dfs = imp.importDataFrames();
//                for (DataFrame df : dfs) {
//                    addDataFrame(df);
//                }
//                jMenuBar.fillRecentFiles();
//                jMenuBar.saveRecentFile(imp.getParameters());
//                String fn = imp.getParameters();
//                if (fn.startsWith("format:codapack?")) {
//                    jMenuBar.active_path = fn.substring(16);
//                } else jMenuBar.active_path = fn;
//            }
//        }else if(title.equals(jMenuBar.ITEM_ADD)){
//            CoDaPackImporter imp = new CoDaPackImporter().setParameters(this);
//            ArrayList<DataFrame> dfs = imp.importDataFrames();
//            for(DataFrame df: dfs) {
//                addDataFrame(df);
//            }
//            jMenuBar.fillRecentFiles();
//            jMenuBar.saveRecentFile(imp.getParameters());
//            //jMenuBar.addRecentFile(imp.getParameters());
//        }else if("format:codapack".equals(action.split("\\?")[0])){
//            CoDaPackImporter imp = new CoDaPackImporter().setParameters(action);
//            ArrayList<DataFrame> dfs = imp.importDataFrames();
//            for(DataFrame df: dfs) {
//                addDataFrame(df);
//            }
//            jMenuBar.fillRecentFiles();
//            jMenuBar.saveRecentFile(imp.getParameters());
//            //jMenuBar.addRecentFile(imp.getParameters());
//        }else if(title.equals(jMenuBar.ITEM_CLEAR_RECENT)) {
//            jMenuBar.removeRecentFiles();
//        }
//        else if(title.equals(jMenuBar.ITEM_SAV)){
//            if (jMenuBar.active_path != null) {
//                String fileNameExt = ".cdp";
//                String fileName = jMenuBar.active_path;
//                String fn;
//                if (fileName.endsWith(".xls") || fileName.endsWith(".rda") || fileName.endsWith(".cdp") || fileName.endsWith(".rda") || fileName.endsWith(".txt") || fileName.endsWith(".csv")) {
//                    fn = fileName.substring(0, fileName.length() - 4);
//                } else if (fileName.endsWith(".xlsx")) fn = fileName.substring(0, fileName.length() - 5);
//                else if (fileName.endsWith(".RData")) fn = fileName.substring(0, fileName.length() - 6);
//                else fn = fileName + fileNameExt;
//                try {
//                    WorkspaceIO.saveWorkspace(fn + fileNameExt, this);
//                    jMenuBar.saveRecentFile(fn + fileNameExt);
//                    //Posem el valor de change de tots els dataframes a false
//                    Iterator<DataFrame> i = dataFrame.iterator();
//                    while (i.hasNext()) {
//                        DataFrame df = i.next();
//                        df.setChange(false);
//                    }
//                } catch (JSONException ex) {
//                    Logger.getLogger(CoDaPackMain.class.getName())
//                            .log(Level.SEVERE, null, ex);
//                }
//            }
//            else {
//                chooseFile.resetChoosableFileFilters();
//                chooseFile.setFileFilter(
//                        new FileNameExtensionFilter("CoDaPack Workspace", "cdp"));
//                if( chooseFile.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
//                    String filename = chooseFile.getSelectedFile().getAbsolutePath();
//                    try {
//                        WorkspaceIO.saveWorkspace(
//                                filename.endsWith(".cdp") ? filename : filename + ".cdp", this);
//                        ruta = filename + ".cdp";
//                        jMenuBar.saveRecentFile(ruta);
//                        //Posem el valor de change de tots els dataframes a false
//                        Iterator<DataFrame> i = dataFrame.iterator();
//                        while (i.hasNext()) {
//                            DataFrame df = i.next();
//                            df.setChange(false);
//                        }
//                    } catch (JSONException ex) {
//                        Logger.getLogger(CoDaPackMain.class.getName())
//                                .log(Level.SEVERE, null, ex);
//                    }
//                }
//                ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
//                CoDaPackConf.lastPath = ruta;
//            }
//        }else if(title.equals(jMenuBar.ITEM_SAVE)){
//            chooseFile.resetChoosableFileFilters();
//            chooseFile.setFileFilter(
//                    new FileNameExtensionFilter("CoDaPack Workspace", "cdp"));
//            if( chooseFile.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
//                String filename = chooseFile.getSelectedFile().getAbsolutePath();
//                try {
//                    WorkspaceIO.saveWorkspace(
//                            filename.endsWith(".cdp") ? filename : filename + ".cdp", this);
//                ruta = filename + ".cdp";
//                jMenuBar.saveRecentFile(ruta);
//                    //Posem el valor de change de tots els dataframes a false
//                    Iterator<DataFrame> i = dataFrame.iterator();
//                    while (i.hasNext()) {
//                        DataFrame df = i.next();
//                        df.setChange(false);
//                    }
//                } catch (JSONException ex) {
//                    Logger.getLogger(CoDaPackMain.class.getName())
//                            .log(Level.SEVERE, null, ex);
//                }
//            }
//            ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
//            CoDaPackConf.lastPath = ruta;
//        }else if(title.equals(jMenuBar.ITEM_DEL_DATAFRAME)){
//            if( dataFrame.size() > 0 ){
//                removeDataFrame(dataFrame.get(activeDataFrame));
//            }else{
//                JOptionPane.showMessageDialog(this, "No table available");
//            }
//        }else if(title.equals(jMenuBar.ITEM_QUIT)){
//            jMenuBar.copyRecentFiles();
//            //Comprovar si hi ha canvis. si n'hi ha finestra
//            boolean hasChange = false;
//            Iterator<DataFrame> i = dataFrame.iterator();
//            while (hasChange == false && i.hasNext()) {
//                DataFrame df = i.next();
//                if (df.getChange()) hasChange = true;
//            }
//            if (hasChange) {
//                int response = JOptionPane.showConfirmDialog(this, "<html>Your changes will be lost if you close <br/>Do you want to exit?</html>", "Confirm",
//                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                if (response == JOptionPane.YES_OPTION) {
//                    dispose();
//                    this.closeApplication();
//                }
//            }else {
//                int response = JOptionPane.showConfirmDialog(this, "Do you want to exit?", "Confirm",
//                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                if (response == JOptionPane.YES_OPTION) {
//                    dispose();
//                    this.closeApplication();
//                }
//            }
//        }else if(title.equals(jMenuBar.ITEM_CONF)){
//            new ConfigurationMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_RAW_ALR)){
//            new TransformationALRMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_RAW_CLR)){
//            new TransformationCLRMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_T_RAW_ILR)) {
//            new TransformationRawILRMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_T_ILR_RAW)) {
//            new TransformationILRRawMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_CLAS_STATS_SUMMARY)){
//            new ClasStatsSummaryMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_COMP_STATS_SUMMARY)){
//            new CompStatsSummaryMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_NORM_TEST)){
//            new NormalityTestMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_ATIP_INDEX)){
//            new AtipicalityIndexMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_CENTER)){
//            new CenterDataMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_CLOSURE)){
//            new ClosureDataMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_AMALGAM)){
//            new AmalgamationDataMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_PERTURBATE)){
//            new PerturbateDataMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_POWER)){
//            new PowerDataMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_ZEROS)){
//            new ZeroReplacementMenu(this).setVisible(true);
//        }else if (title.equals(jMenuBar.ITEM_SETDETECTION)){
//            new SetDetectionLimitMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_TERNARY_PLOT)){
//            new TernaryPlotMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_PRED_REG_PLOT)){
//            new PredictiveRegionMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_CONF_REG_PLOT)){
//            new ConfidenceRegionMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_EMPTY_TERNARY_PLOT)){
//            String names[] = {"X", "Y", "Z"};
//            TernaryPlot2dDisplay display = new TernaryPlot2dDisplay(names);
//
//            double definedGrid[] =
//        {0.01, 0.05, 0.10, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99};
//            Ternary2dObject gridObject = new Ternary2dGridObject(display, definedGrid);
//            display.addCoDaObject(gridObject);
//            double center[] = {1,1,1};
//
//            TernaryPlot2dWindow frame = new TernaryPlot2dWindow(this.getActiveDataFrame(), display, "Ternary Plot -- Testing version");
//            frame.setLocationRelativeTo(this);
//            frame.setCenter(center);
//            frame.setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_BIPLOT)){
//            new Biplot3dMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_ILR_BIPLOT)){
//            new ILRCLRPlotMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_DENDROGRAM_PLOT)){
//            new DendrogramMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_ALR_PLOT)){
//            new ALRPlotMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_CLR_PLOT)){
//            new CLRPlotMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_ILR_PLOT)){
//            new ILRPlotMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_ADD_VAR)){
//            new AddMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_DEL_VAR)){
//            new DeleteMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_CAT_VAR)){
//            new Numeric2CategoricMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_NUM_VAR)){
//            new Categoric2NumericMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_PC_PLOT)){
//            new PrincipalComponentMenu(this).setVisible(true);
//        }else if(title.equals(jMenuBar.ITEM_FORCE_UPDATE)){
//            CoDaPackConf.refusedVersion = CoDaPackConf.CoDaVersion;
//            UpdateConnection uc = new UpdateConnection(this);
//            new Thread(uc).start();
//        }else if(title.equals(jMenuBar.ITEM_ABOUT)){
//            new CoDaPackAbout(this).setVisible(true);
//        }
//    }
//    public class DataFrameSelectorListener implements ItemListener{
//        public void itemStateChanged(ItemEvent ie) {
//            JComboBox combo = (JComboBox)ie.getSource();
//            if(activeDataFrame != combo.getSelectedIndex()){
//                activeDataFrame = combo.getSelectedIndex();
//                dataList.setData(dataFrame.get(activeDataFrame));
//                tablePanel.setDataFrame(dataFrame.get(activeDataFrame));
//                dataFrame.get(activeDataFrame).setChange(true);
//            }
//        }
//    }    
//    /**
//    * @param args the command line arguments
//    */
//    public static void main(String args[]){
//        /*
//         * Look and Feel: change appearence according to OS
//         */
//
//       try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(CoDaPackMain.class.getName())
//                    .log(Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            Logger.getLogger(CoDaPackMain.class.getName())
//                    .log(Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            Logger.getLogger(CoDaPackMain.class.getName())
//                    .log(Level.SEVERE, null, ex);
//        } catch (UnsupportedLookAndFeelException ex) {
//            Logger.getLogger(CoDaPackMain.class.getName())
//                    .log(Level.SEVERE, null, ex);
//        }
//        
//        
//        /*
//         *  Congifuration file creation if it not exists. Using static class CoDaPackConf
//         */
//        File f = new File(CoDaPackConf.configurationFile);
//        if(f.exists())
//            CoDaPackConf.loadConfiguration();
//        else
//            CoDaPackConf.saveConfiguration();
//        
//        /*
//         * CoDaPack connects to IMA server through a thread. Avoiding problems in IMA server
//         */
//                
//        
//        /*
//         * CoDaPack main class is created and shown
//         */
//        CoDaPackMain main = new CoDaPackMain();
//        
//        UpdateConnection uc = new UpdateConnection(main);
//        new Thread(uc).start();
//        
//        main.setVisible(true);
//        
//        //Si s'ha clicat un arxiu associat ens arribarà com argument i el tractem
//        if (args.length>0) {
//            //Guardem la ruta i l'arxiu a recent files
//            main.jMenuBar.saveRecentFile(args[0]);
//            //Obrim l'arxiu 
//            CoDaPackImporter imp = new CoDaPackImporter().setParameters("format:codapack?" + args[0]);
//            ArrayList<DataFrame> dfs = imp.importDataFrames();
//            
//            for(DataFrame df: dfs) {
//                main.addDataFrame(df);
//            }
//        }    
//    }
//    
//    public static class UpdateConnection implements Runnable{
//        CoDaPackMain main;
//        public UpdateConnection(CoDaPackMain main){
//            this.main = main;
//        }
//        public void run() {
//            checkForUpdate();
//        }
//        public void redirectForUpdating(String newversion){
//            Object[] options = {"Quit and download...",
//                "No, thanks. Maybe later",
//                "No, thanks. Stop asking"};
//            int n = JOptionPane.showOptionDialog(main,
//                    "CoDaPack " + 
//                            newversion.replace(' ', '.') + 
//                            " is now available (you're using " + 
//                            CoDaPackConf.CoDaVersion.replace(' ','.') +
//                            ").",
//                    "Update Available",
//                    JOptionPane.YES_NO_CANCEL_OPTION,
//                    JOptionPane.QUESTION_MESSAGE,
//                    null,
//                    options,
//                    options[1]);
//            if(n == JOptionPane.YES_OPTION){
//                try {
//                    Desktop.getDesktop().browse(new URI("http://mcomas.net/codapack"));
//                } catch (URISyntaxException ex) {
//                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex) {
//                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                main.closeApplication();
//            }
//            if(n == JOptionPane.CANCEL_OPTION){
//                CoDaPackConf.refusedVersion = newversion;
//                CoDaPackConf.saveConfiguration();
//            }
//        }
//        public void checkForUpdate(){
//                try {
//                    JSONObject serverData = null;
//
//                    System.out.println("Connecting to server...");
//                    URL url = new URL(CoDaPackConf.HTTP_ROOT + "codapack-updater.json");
//
//                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
//                    try {
//                        serverData = new JSONObject(br.readLine());
//                    } catch (JSONException ex) {
//                        Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    br.close();
//                    System.out.println("Server version is: " + serverData.getString("codapack-version"));
//                    if(CoDaPackConf.updateNeeded(serverData.getString("codapack-version")))
//                        redirectForUpdating(serverData.getString("codapack-version"));
//                } catch (MalformedURLException ex) {
//                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex) {
//                Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (JSONException ex) {
//                Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
//            } 
//        }
//    }
}
