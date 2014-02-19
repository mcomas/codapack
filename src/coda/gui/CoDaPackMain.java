package coda.gui;


import coda.DataFrame;
import coda.ext.json.JSONException;
import coda.ext.json.JSONObject;
import coda.gui.CoDaPackMain.UpdateConnection;
import coda.gui.menu.*;
import coda.gui.utils.FileNameExtensionFilter;
import coda.io.ExportData;
import coda.io.ImportData;
import coda.io.WorkspaceIO;
import coda.plot2.TernaryPlot2dDisplay;
import coda.plot2.objects.Ternary2dGridObject;
import coda.plot2.objects.Ternary2dObject;
import coda.plot2.window.TernaryPlot2dWindow;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
/**
 *
 * @author mcomas
 */
public final class CoDaPackMain extends JFrame{
    public static final long serialVersionUID = 1L;
    // CoDaPack version
    
    // dataFrame is the class containing the data
    private ArrayList<DataFrame> dataFrame = new ArrayList<DataFrame>();
    private int activeDataFrame = -1;
    
    
    
    public static String RESOURCE_PATH = "resources/";
    // User computer parameters
    private Dimension screenDimension;    
    //Panel showing the non-column outputs and the data contained in the dataFrame
    public static JTabbedPane outputs;
    public static OutputPanel outputPanel;
    public static TablePanel tablePanel;
    //Panel with the active variable
    public static DataList dataList;
    private JSplitPane jSplitPane;
    
    
    private JComboBox dataFrameSelector;
    private DataFrameSelectorListener dataFrameListener =
            new DataFrameSelectorListener();
    

    
    private String ITEM_APPLICATION_NAME;
    // Menu
    private JMenuBar jMenuBar;
        private JMenu menuFile;
        private final String ITEM_FILE = "File";
            private JMenuItem itemOpen;
            private final String ITEM_OPEN = "Open Workspace...";
            private JMenuItem itemSave;
            private final String ITEM_SAVE = "Save Workspace...";
            private JMenuItem itemSaveAs;            
            private final String ITEM_SAVEAS = "Save Workspace As...";
            private JMenuItem itemCloseWS;
            private final String ITEM_CLOSEWS = "Close Workspace";
            private JMenuItem itemNewDF;
            private final String ITEM_NEWDF = "New DataFrame";
            private JMenuItem itemImport;
            private final String ITEM_IMPORT = "Import XLS Table...";
            private JMenuItem itemImportCSV;
            private final String ITEM_IMPORT_CSV = "Import Text Table...";
            private JMenuItem itemExport;
            private final String ITEM_EXPORT_XLS = "Export Table to XLS...";
            private JMenuItem itemExportR;
            private final String ITEM_EXPORT_R = "Export Data to R...";
            private JMenuItem itemdelDataFrame;
            private final String ITEM_DEL_DATAFRAME = "Delete Table";
            private JMenuItem itemConfiguration;
            private final String ITEM_CONF = "Configuration";
            private JMenuItem itemQuit;
            private final String ITEM_QUIT = "Quit CoDaPack";

        private JMenu menuData;
        private final String ITEM_DATA = "Data";
            private JMenu menuTransforms;
            private final String ITEM_TRANS = "Transformations";
                private JMenuItem itemTransformALR;
                private final String ITEM_RAW_ALR = "ALR";
                private JMenuItem itemTransformCLR;
                private final String ITEM_RAW_CLR = "CLR";
                private JMenuItem itemTransformILR;
                private final String ITEM_RAW_ILR = "ILR";
            private JMenuItem itemCenter;
            private final String ITEM_CENTER = "Centering";
            private JMenuItem itemClosure;
            private final String ITEM_CLOSURE = "Subcomposition/Closure";
            private JMenuItem itemAmalgamation;
            private final String ITEM_AMALGAM = "Amalgamation";
            private JMenuItem itemPerturbate;
            private final String ITEM_PERTURBATE = "Perturbation";
            private JMenuItem itemPower;
            private final String ITEM_POWER = "Power transformation";
            private JMenuItem itemZeros;
            private final String ITEM_ZEROS = "Rounded zero replacement";
            private JMenuItem itemCategorizeVariables;
            private final String ITEM_CAT_VAR = "Numeric to categorical";
            private JMenuItem itemNumerizeVariables;
            private final String ITEM_NUM_VAR = "Categorical to Numeric";
            private JMenuItem itemAddVariables;
            private final String ITEM_ADD_VAR = "Add Numeric Variables";
            private JMenuItem itemDeleteVariables;
            private final String ITEM_DEL_VAR = "Delete variables";

        private JMenu menuStatistics;
        private final String ITEM_STATS = "Statistics";
            private JMenuItem itemCompStatsSummary;
            private final String ITEM_COMP_STATS_SUMMARY = "Compositional statistics summary";
            private JMenuItem itemClasStatsSummary;
            private final String ITEM_CLAS_STATS_SUMMARY = "Classical statistics summary";
            private JMenuItem itemNormalityTest;
            private final String ITEM_NORM_TEST = "Additive Logistic Normality Tests";
            private JMenuItem itemAtipicalityIndex;
            private final String ITEM_ATIP_INDEX = "Atipicality index";
            

        private JMenu menuGraphs;
        private final String ITEM_GRAPHS = "Graphs";
            private JMenuItem itemTernaryPlot;
            private final String ITEM_TERNARY_PLOT = "Ternary plot";
            private JMenuItem itemEmptyTernaryPlot;
            private final String ITEM_EMPTY_TERNARY_PLOT = "Ternary plot [Empty]";
            private JMenuItem itemIlrBiPlot;
            private final String ITEM_ILR_BIPLOT = "ILR/CLR plot";
            private JMenuItem itemBiPlot;
            private final String ITEM_BIPLOT = "CLR biplot";
            private JMenuItem itemDendrogramPlot;
            private final String ITEM_DENDROGRAM_PLOT = "Balance dendrogram";
            private JMenuItem itemALRPlot;
            private final String ITEM_ALR_PLOT = "ALR plot";
            private JMenuItem itemCLRPlot;
            private final String ITEM_CLR_PLOT = "CLR plot";
            private JMenuItem itemILRPlot;
            private final String ITEM_ILR_PLOT = "ILR plot";
            private JMenuItem principalComponentPlot;
            private final String ITEM_PC_PLOT = "Ternary Principal Components";
            private JMenuItem predictiveRegionPlot;
            private final String ITEM_PRED_REG_PLOT = "Predictive Region";
            private JMenuItem confidenceRegionPlot;
            private final String ITEM_CONF_REG_PLOT = "Center Confidence Region";

       private JMenu menuDistributions;
       private final String ITEM_DISTRIBUTIONS = "Distributions";
            private JMenu sampleGenerators;
            private final String SAMPLE_GENERATORS = "Random sample generators";
            private JMenuItem itemAdditiveLogisticNormal;
            private final String ITEM_ALN_DISTRIBUTION = "Additive Logistic Normal";

       /*private JMenu menuDistributions;
       private final String ITEM_DIST = "Distributions";
            private JMenu menuRand;
            private final String ITEM_RAND = "Gaussian generators";
                private JMenuItem itemNormSample;
                private final String ITEM_NORM_SAMPLE = "Using sample";*/

       private JMenu menuHelp;
       private final String ITEM_HELP = "Help";
            private JMenuItem itemAbout;
            private final String ITEM_ABOUT = "About";

       public static CoDaPackConf config = new CoDaPackConf();
    private JFileChooser chooseFile = new JFileChooser();
    // Mac users expect the menu bar to be at the top of the screen, not in the
    // window, so enable that setting. (This is ignored on non-Mac systems).
    static{
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name",
                "CoDaPack");
        System.setProperty("com.apple.mrj.application.growbox.intrudes",
                "false");
    }
    /** Creates new form CoDaPack */
    public CoDaPackMain() {
        screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setIconImage(Toolkit.getDefaultToolkit()
                .getImage(CoDaPackMain.RESOURCE_PATH + "logo.png"));
        if(this.getIconImage() == null){
            CoDaPackMain.RESOURCE_PATH = "";
            this.setIconImage(Toolkit.getDefaultToolkit()
                .getImage(CoDaPackMain.RESOURCE_PATH + "logo.png"));
        }
        
        /*
         *  Menu creation:
         *  Three important objects are created: 
         *  - OutputPanel
         *  - TablePanel
         *  - DataList
         * 
         *  The names of menus are also important
         */
        initComponents();
        outputPanel.addWelcome(CoDaPackConf.getVersion());

    }
    private void addJMenuItem(JMenu menu, JMenuItem item, String title){
        //item.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        menu.add(item);
        item.setText(title);
        item.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemActionPerformed(evt);
            }
        });
    }
    private void initComponents() {
        ITEM_APPLICATION_NAME = "CoDaPack v" + CoDaPackConf.getVersion();
        //outputs = new JTabbedPane();
        outputPanel = new OutputPanel();
        //outputs.add(outputPanel, "default");
        tablePanel = new TablePanel();
        //Panel with the active variable
        dataList = new DataList();

        jMenuBar = new JMenuBar();
            menuFile = new JMenu();
                itemOpen = new JMenuItem();
                itemSave = new JMenuItem();
                itemSaveAs = new JMenuItem();
                itemCloseWS = new JMenuItem();
                itemNewDF = new JMenuItem();
                itemImportCSV = new JMenuItem();
                itemImport = new JMenuItem();
                itemExport = new JMenuItem();
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

       menuDistributions = new JMenu();
            sampleGenerators = new JMenu();
            itemAdditiveLogisticNormal = new JMenuItem();

        menuHelp = new JMenu();
            itemAbout = new JMenuItem();
        


        setTitle(ITEM_APPLICATION_NAME);
        setPreferredSize(new Dimension(1000,700));
        setLocation((screenDimension.width-1000)/2,
                (screenDimension.height-700)/2);


        dataList.setSize(new Dimension(150, 700));
        dataFrameSelector = new JComboBox();
        dataFrameSelector.setPrototypeDisplayValue("XXXXXXXXXXXXXX");
        //dataFrameSelector.setMaximumSize(new Dimension(10,20));
        dataFrameSelector.addItemListener(dataFrameListener);



        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BorderLayout());
        JPanel dfSelect = new JPanel();
        dfSelect.add(new JLabel("Data Frames"));
        dfSelect.add(dataFrameSelector);
        westPanel.add(dfSelect, BorderLayout.NORTH);
        //westPanel.add(dataFrameSelector, BorderLayout.NORTH);
        westPanel.add(dataList, BorderLayout.CENTER);
        //getContentPane().add(westPanel, BorderLayout.WEST);

        //jSplitPane = new JSplitPane(
        //        JSplitPane.VERTICAL_SPLIT, outputs, tablePanel);
        jSplitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT, outputPanel, tablePanel);
        jSplitPane.setDividerSize(7);
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setDividerLocation(350);
        //getContentPane().add(jSplitPane, BorderLayout.CENTER);

        JSplitPane main =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westPanel, jSplitPane);
        main.setDividerSize(7);    
        getContentPane().add(main, BorderLayout.CENTER);

        menuFile.setText(ITEM_FILE);
        addJMenuItem(menuFile, itemOpen, ITEM_OPEN);
        addJMenuItem(menuFile, itemSave, ITEM_SAVE);
        //addJMenuItem(menuFile, itemSaveAs, ITEM_SAVEAS);
        itemSaveAs.setEnabled(false);
        //addJMenuItem(menuFile, itemCloseWS, ITEM_CLOSEWS);
        itemCloseWS.setEnabled(false);
        menuFile.addSeparator();
        //addJMenuItem(menuFile, itemNewDF, ITEM_NEWDF);
        itemNewDF.setEnabled(false);
        addJMenuItem(menuFile, itemImportCSV, ITEM_IMPORT_CSV);
        addJMenuItem(menuFile, itemImport, ITEM_IMPORT);
        addJMenuItem(menuFile, itemExport, ITEM_EXPORT_XLS);
        addJMenuItem(menuFile, itemExportR, ITEM_EXPORT_R);
        //itemExport.setEnabled(false);
        addJMenuItem(menuFile, itemdelDataFrame, ITEM_DEL_DATAFRAME);
       // menuFile.addSeparator();
        addJMenuItem(menuFile, itemConfiguration, ITEM_CONF);
        menuFile.addSeparator();
        addJMenuItem(menuFile, itemQuit, ITEM_QUIT);
        jMenuBar.add(menuFile);


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
        jMenuBar.add(menuData);


        menuStatistics.setText(ITEM_STATS);
        addJMenuItem(menuStatistics, itemCompStatsSummary, ITEM_COMP_STATS_SUMMARY);        
        addJMenuItem(menuStatistics, itemClasStatsSummary, ITEM_CLAS_STATS_SUMMARY);        
        jMenuBar.add(menuStatistics);
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
        jMenuBar.add(menuGraphs);

        /*
        menuDistributions.setText(ITEM_DISTRIBUTIONS);
        sampleGenerators.setText(SAMPLE_GENERATORS);
        menuDistributions.add(sampleGenerators);
        addJMenuItem(sampleGenerators, itemAdditiveLogisticNormal, ITEM_ALN_DISTRIBUTION);
        jMenuBar.add(menuDistributions);
        */

        menuHelp.setText(ITEM_HELP);
        addJMenuItem(menuHelp, itemAbout, ITEM_ABOUT);
        jMenuBar.add(menuHelp);
        
        setJMenuBar(jMenuBar);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
       
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                itemQuit.doClick();
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
        }else{
            JOptionPane.showMessageDialog(null,"<html>Name <i>" +
                    df.name + "</i> is not available.</html>");
        }
    }
    public void updateDataFrame(DataFrame df){
        //int pos = activeDataFrame;
        //dataFrame.remove(activeDataFrame);
        //dataFrame.add(df);
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
    void itemActionPerformed(ActionEvent ev){
        JMenuItem jMenuItem = (JMenuItem)ev.getSource();
        String title = jMenuItem.getText();
        if(title.equals(ITEM_IMPORT)){
            chooseFile.resetChoosableFileFilters();
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("Excel files", "xls"));
            if(chooseFile.showOpenDialog(jSplitPane) ==
                    JFileChooser.APPROVE_OPTION){
                ImportMenu importMenu = new ImportMenu(this, true, chooseFile);
                importMenu.setVisible(true);
                DataFrame df = importMenu.getDataFrame();
                if( df != null) addDataFrame(df);
                importMenu.dispose();
            }
        }else if(title.equals(ITEM_IMPORT_CSV)){
            chooseFile.resetChoosableFileFilters();
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("Text file", "txt"));
            if(chooseFile.showOpenDialog(jSplitPane) ==
                    JFileChooser.APPROVE_OPTION){
                String path = chooseFile.getSelectedFile().getAbsolutePath();
                String fname = chooseFile.getSelectedFile().getName();
                DataFrame df = ImportData.importText(path);
                df.name = fname;
                if( df != null) addDataFrame(df);
            }
        }else if(title.equals(ITEM_EXPORT_XLS)){
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
        }else if(title.equals(ITEM_EXPORT_R)){
            new ExportMenu(this).setVisible(true);
        }else if(title.equals(ITEM_OPEN)){
            chooseFile.resetChoosableFileFilters();
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("CoDaPack Workspace", "cdp"));
            if(chooseFile.showOpenDialog(jSplitPane) ==
                    JFileChooser.APPROVE_OPTION){
                WorkspaceIO.openWorkspace(chooseFile.getSelectedFile()
                        .getAbsolutePath(), this);
            }
        }else if(title.equals(ITEM_SAVE)){
            chooseFile.resetChoosableFileFilters();
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("CoDaPack Workspace", "cdp"));
            if( chooseFile.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
                String filename = chooseFile.getSelectedFile().getAbsolutePath();
                try {
                    WorkspaceIO.saveWorkspace(
                            filename.endsWith(".cdp") ? filename : filename + ".cdp", this);
                    /*
                    try {
                        PrintWriter printer = new PrintWriter(
                                chooseFile.getSelectedFile().getPath()+ ".csv");
                        outputPanel.writeTXT(printer);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(CoDaPackMain.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                     * */
                } catch (JSONException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
        }else if(title.equals(ITEM_DEL_DATAFRAME)){
            removeDataFrame(dataFrame.get(activeDataFrame));
        }else if(title.equals(ITEM_QUIT)){
            int response = JOptionPane.showConfirmDialog(null, "Do you want to exit?", "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION){
                dispose();
                System.exit(0);
            }
        }else if(title.equals(ITEM_CONF)){
            new ConfigurationMenu(this).setVisible(true);
        }else if(title.equals(ITEM_RAW_ALR)){
            new TransformationALRMenu(this).setVisible(true);
        }else if(title.equals(ITEM_RAW_CLR)){
            new TransformationCLRMenu(this).setVisible(true);
        }else if(title.equals(ITEM_RAW_ILR)){
            new TransformationILRMenu(this).setVisible(true);
        }else if(title.equals(ITEM_CLAS_STATS_SUMMARY)){
            new ClasStatsSummaryMenu(this).setVisible(true);
        }else if(title.equals(ITEM_COMP_STATS_SUMMARY)){
            new CompStatsSummaryMenu(this).setVisible(true);
        }else if(title.equals(ITEM_NORM_TEST)){
            new NormalityTestMenu(this).setVisible(true);
        }else if(title.equals(ITEM_ATIP_INDEX)){
            new AtipicalityIndexMenu(this).setVisible(true);
        }else if(title.equals(ITEM_CENTER)){
            new CenterDataMenu(this).setVisible(true);
        }else if(title.equals(ITEM_CLOSURE)){
            new ClosureDataMenu(this).setVisible(true);
        }else if(title.equals(ITEM_AMALGAM)){
            new AmalgamationDataMenu(this).setVisible(true);
        }else if(title.equals(ITEM_PERTURBATE)){
            new PerturbateDataMenu(this).setVisible(true);
        }else if(title.equals(ITEM_POWER)){
            new PowerDataMenu(this).setVisible(true);
        }else if(title.equals(ITEM_ZEROS)){
            new ZeroReplacementMenu(this).setVisible(true);
        }else if(title.equals(ITEM_TERNARY_PLOT)){
            new TernaryPlotMenu(this).setVisible(true);
        }else if(title.equals(ITEM_PRED_REG_PLOT)){
            new PredictiveRegionMenu(this).setVisible(true);
        }else if(title.equals(ITEM_CONF_REG_PLOT)){
            new ConfidenceRegionMenu(this).setVisible(true);
        }else if(title.equals(ITEM_EMPTY_TERNARY_PLOT)){
            String names[] = {"X", "Y", "Z"};
            TernaryPlot2dDisplay display = new TernaryPlot2dDisplay(names);

            double definedGrid[] =
        {0.01, 0.05, 0.10, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99};
            Ternary2dObject gridObject = new Ternary2dGridObject(display, definedGrid);
            display.addCoDaObject(gridObject);
            double center[] = {1,1,1};

            TernaryPlot2dWindow frame = new TernaryPlot2dWindow(this.getActiveDataFrame(), display, "Ternary Plot -- Testing version");
            frame.setCenter(center);
            frame.setVisible(true);
        }else if(title.equals(ITEM_BIPLOT)){
            new Biplot3dMenu(this).setVisible(true);
        }else if(title.equals(ITEM_ILR_BIPLOT)){
            new ILRPlotMenuNew(this).setVisible(true);
        }else if(title.equals(ITEM_DENDROGRAM_PLOT)){
            new DendrogramMenu(this).setVisible(true);
        }else if(title.equals(ITEM_ALR_PLOT)){
            new ALRPlotMenu(this).setVisible(true);
        }else if(title.equals(ITEM_CLR_PLOT)){
            new CLRPlotMenu(this).setVisible(true);
        }else if(title.equals(ITEM_ILR_PLOT)){
            new ILRPlotMenu(this).setVisible(true);
        }else if(title.equals(ITEM_ADD_VAR)){
            new AddMenu(this).setVisible(true);
        }else if(title.equals(ITEM_DEL_VAR)){
            new DeleteMenu(this).setVisible(true);
        }else if(title.equals(ITEM_CAT_VAR)){
            new Numeric2CategoricMenu(this).setVisible(true);
        }else if(title.equals(ITEM_NUM_VAR)){
            new Categoric2NumericMenu(this).setVisible(true);
        }else if(title.equals(ITEM_PC_PLOT)){
            new PrincipalComponentMenu(this).setVisible(true);
        }else if(title.equals(ITEM_ALN_DISTRIBUTION)){
            new NormalSampleMenu(this);
        }else if(title.equals(ITEM_ABOUT)){
            new CoDaPackAbout(this).setVisible(true);
        }
    }
    public class DataFrameSelectorListener implements ItemListener{
        public void itemStateChanged(ItemEvent ie) {
            JComboBox combo = (JComboBox)ie.getSource();
            if(activeDataFrame != combo.getSelectedIndex()){
                activeDataFrame = combo.getSelectedIndex();
                dataList.setData(dataFrame.get(activeDataFrame));
                tablePanel.setDataFrame(dataFrame.get(activeDataFrame));
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
            //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
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
        File f = new File("codapack.conf");
        if(f.exists())
            CoDaPackConf.loadConfiguration();
        else
            CoDaPackConf.saveConfiguration();
        
        /*
         * CoDaPack connects to IMA server through a thread. Avoiding problems in IMA server
         */
                
        UpdateConnection uc = new UpdateConnection();
        new Thread(uc).start();
        /*
         * CoDaPack main class is created and shown
         */
        new CoDaPackMain().setVisible(true);
    }
    public static class UpdateConnection implements Runnable{

        public void run() {
            boolean CoDaPackUpdaterUpdated = false;
            JSONObject data = null;
            try {
                URL url = new URL(CoDaPackConf.HTTP_ROOT + "codapack-updater.json");
                InputStreamReader isr = new InputStreamReader( url.openStream() );

                try {
                    data = new JSONObject(new BufferedReader(isr).readLine());
                } catch (JSONException ex) {
                    System.out.println("Error reading the JSON file");
                }

                isr.close();

                if(data != null){
                    int updaterVersion = 0;
                    try {
                        updaterVersion = data.getInt("updater-version");
                    } catch (JSONException ex) {
                        Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    // The updater needs to be updated
                    if(CoDaPackConf.CoDaUpdaterVersion < updaterVersion){
                        int response = JOptionPane.showConfirmDialog(null, "CoDaPack Updater needs to be updated", "Update confirmation",
                            JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if(response == JOptionPane.YES_OPTION){
                            //System.out.println("Updating CoDaPack updater");
                            url = new URL(CoDaPackConf.HTTP_ROOT + "codapack/CoDaPackUpdater.jar");

                            InputStream is = url.openStream();
                            FileOutputStream fos = null;
                            File tempFile = new File("CoDaPackUpdater.jar_temp");
                            fos = new FileOutputStream(tempFile);
                            int oneChar;
                            while ((oneChar=is.read()) != -1){
                                fos.write(oneChar);
                            }
                            is.close();
                            fos.close();

                            (new File("CoDaPackUpdater.jar")).delete();
                            boolean result = tempFile.renameTo(new File("CoDaPackUpdater.jar"));

                            CoDaPackConf.CoDaUpdaterVersion = updaterVersion;
                            CoDaPackConf.saveConfiguration();
                            CoDaPackUpdaterUpdated = true;
                        }
                    }else{
                        // No update is needed
                        CoDaPackUpdaterUpdated = true;
                    }
                }else{
                    CoDaPackUpdaterUpdated = false;
                }
            } catch (IOException ex) {
                CoDaPackUpdaterUpdated = false;
                System.out.println("Some problem connecting to IMA server");
            }

            // If updater is up to date updating process, if needed, can start
            if(CoDaPackUpdaterUpdated == true){
                try {
                    // Checking if CoDaPack is updated
                    if (CoDaPackConf.updateNeeded(data.getString("codapack-version"))) {
                        int response = JOptionPane.showConfirmDialog(null, "There is a new version of CoDaPack available. Do you want to install it? If you say yes, CoDaPack is going to be closed automatically.", "Update confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (response == JOptionPane.YES_OPTION) {
                            try {
                                Process ps = Runtime.getRuntime().exec("java -jar CoDaPackUpdater.jar");
                                System.exit(0);
                            } catch (IOException ex) {
                                System.out.println("It's not possible to execute the updater");
                            }
                        }
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}