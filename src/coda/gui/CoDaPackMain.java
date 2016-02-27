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
import coda.gui.table.TablePanel;
import coda.gui.utils.FileNameExtensionFilter;
import coda.io.CoDaPackImporter;
import coda.io.ExportData;
import coda.io.WorkspaceIO;
import coda.plot2.TernaryPlot2dDisplay;
import coda.plot2.objects.Ternary2dGridObject;
import coda.plot2.objects.Ternary2dObject;
import coda.plot2.window.TernaryPlot2dWindow;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    
    
    public static String RESOURCE_PATH = "resources/";

    private final Dimension screenDimension;
    
    //Panel showing the non-column outputs and the data contained in the dataFrame
    public static JTabbedPane outputs;
    public static OutputPanel outputPanel;
    public static TablePanel tablePanel;
    //Panel with the active variable
    public static DataList dataList;
    private JSplitPane jSplitPane;
    
    
    private JComboBox dataFrameSelector;
    private final DataFrameSelectorListener dataFrameListener = new DataFrameSelectorListener();
    

    
    private String ITEM_APPLICATION_NAME;
    // Menu
    private CoDaPackMenu jMenuBar;


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
        this.setIconImage(
                Toolkit.getDefaultToolkit().getImage(
                CoDaPackMain.RESOURCE_PATH + "logoL.png"));
        initComponents();
        outputPanel.addWelcome(CoDaPackConf.getVersion());
    }
    private void initComponents() {
        ITEM_APPLICATION_NAME = "CoDaPack v" + CoDaPackConf.getVersion();
        outputPanel = new OutputPanel();
        tablePanel = new TablePanel();
        
        //Panel with the active variable
        dataList = new DataList();
        jMenuBar = new CoDaPackMenu();
        jMenuBar.addCoDaPackMenuListener(new CoDaPackMenuListener(){
            @Override
            public void menuItemClicked(String v) {
                eventCoDaPack(v);
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
        dfSelect.add(new JLabel("Data Frames"));
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
        }else{
            JOptionPane.showMessageDialog(this,"<html>Dataframe <i>" +
                    df.name + "</i> is already loaded.</html>");
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
    public void eventCoDaPack(String action){
        String title = action;
        String ruta = fillRecentPath();
        JFileChooser chooseFile = new JFileChooser(ruta);
        if(title.equals(jMenuBar.ITEM_IMPORT_XLS)){
            chooseFile.resetChoosableFileFilters();
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("Excel files", "xls", "xlsx"));
            if(chooseFile.showOpenDialog(jSplitPane) ==
                    JFileChooser.APPROVE_OPTION){
                ImportXLSMenu importMenu = new ImportXLSMenu(this, true, chooseFile);
                importMenu.setVisible(true);
                DataFrame df = importMenu.getDataFrame();
                if( df != null) addDataFrame(df);
                importMenu.dispose();
            }
            ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
            copyRecentPath(ruta);
        }else if(title.equals(jMenuBar.ITEM_IMPORT_CSV)){
            chooseFile.resetChoosableFileFilters();
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("Text file", "txt"));
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("CSV file", "csv"));
            
            if(chooseFile.showOpenDialog(jSplitPane) ==
                    JFileChooser.APPROVE_OPTION){
                ImportCSVMenu importMenu = new ImportCSVMenu(this, true, chooseFile);
                importMenu.setVisible(true);
                DataFrame df = importMenu.getDataFrame();
                if( df != null) addDataFrame(df);
                importMenu.dispose();
            }
            ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
            copyRecentPath(ruta);
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
            copyRecentPath(ruta);
        }else if(title.equals(jMenuBar.ITEM_EXPORT_R)){
            new ExportRDataMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_OPEN)){
            CoDaPackImporter imp = new CoDaPackImporter().setParameters(this);
            ArrayList<DataFrame> dfs = imp.importDataFrames();
            for(DataFrame df: dfs)
                addDataFrame(df);
            jMenuBar.fillRecentFiles();
            jMenuBar.saveRecentFile(imp.getParameters());
            //jMenuBar.addRecentFile(imp.getParameters());
        }else if("format:codapack".equals(action.split("¿")[0])){
            CoDaPackImporter imp = new CoDaPackImporter().setParameters(action);
            ArrayList<DataFrame> dfs = imp.importDataFrames();
            
            for(DataFrame df: dfs)
                addDataFrame(df);
            jMenuBar.fillRecentFiles();
            jMenuBar.saveRecentFile(imp.getParameters());
            //jMenuBar.addRecentFile(imp.getParameters());
        }else if(title.equals(jMenuBar.ITEM_CLEAR_RECENT)){
            jMenuBar.removeRecentFiles();
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
                } catch (JSONException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
            ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
            copyRecentPath(ruta);
        }else if(title.equals(jMenuBar.ITEM_DEL_DATAFRAME)){
            if( dataFrame.size() > 0 ){
                removeDataFrame(dataFrame.get(activeDataFrame));
            }else{
                JOptionPane.showMessageDialog(this, "No table available");
            }
        }else if(title.equals(jMenuBar.ITEM_QUIT)){
            jMenuBar.copyRecentFiles();
            int response = JOptionPane.showConfirmDialog(this, "Do you want to exit?", "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION){
                dispose();
                System.exit(0);
            }
        }else if(title.equals(jMenuBar.ITEM_CONF)){
            new ConfigurationMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_RAW_ALR)){
            new TransformationALRMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_RAW_CLR)){
            new TransformationCLRMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_RAW_ILR)){
            new TransformationILRMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_CLAS_STATS_SUMMARY)){
            new ClasStatsSummaryMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_COMP_STATS_SUMMARY)){
            new CompStatsSummaryMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_NORM_TEST)){
            new NormalityTestMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_ATIP_INDEX)){
            new AtipicalityIndexMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_CENTER)){
            new CenterDataMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_CLOSURE)){
            new ClosureDataMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_AMALGAM)){
            new AmalgamationDataMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_PERTURBATE)){
            new PerturbateDataMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_POWER)){
            new PowerDataMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_ZEROS)){
            new ZeroReplacementMenu(this).setVisible(true);
        }else if (title.equals(jMenuBar.ITEM_SETDETECTION)){
            new SetDetectionLimitMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_TERNARY_PLOT)){
            new TernaryPlotMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_PRED_REG_PLOT)){
            new PredictiveRegionMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_CONF_REG_PLOT)){
            new ConfidenceRegionMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_EMPTY_TERNARY_PLOT)){
            String names[] = {"X", "Y", "Z"};
            TernaryPlot2dDisplay display = new TernaryPlot2dDisplay(names);

            double definedGrid[] =
        {0.01, 0.05, 0.10, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99};
            Ternary2dObject gridObject = new Ternary2dGridObject(display, definedGrid);
            display.addCoDaObject(gridObject);
            double center[] = {1,1,1};

            TernaryPlot2dWindow frame = new TernaryPlot2dWindow(this.getActiveDataFrame(), display, "Ternary Plot -- Testing version");
            frame.setLocationRelativeTo(this);
            frame.setCenter(center);
            frame.setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_BIPLOT)){
            new Biplot3dMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_ILR_BIPLOT)){
            new ILRCLRPlotMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_DENDROGRAM_PLOT)){
            new DendrogramMenu(this).setVisible(true);
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
            new PrincipalComponentMenu(this).setVisible(true);
        }else if(title.equals(jMenuBar.ITEM_FORCE_UPDATE)){
            this.forceUpdate();
        }else if(title.equals(jMenuBar.ITEM_ABOUT)){
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
                
        
        /*
         * CoDaPack main class is created and shown
         */
        CoDaPackMain main = new CoDaPackMain();
        
        UpdateConnection uc = new UpdateConnection(main);
        new Thread(uc).start();
        
        main.setVisible(true);
    }
    public void forceUpdate(){
        String previous  = CoDaPackConf.CoDaVersion;
        try {
            
            CoDaPackConf.CoDaVersion = "2 00 0";
            CoDaPackConf.saveConfiguration();
            Process ps = Runtime.getRuntime().exec("java -jar CoDaPackUpdater.jar");
        } catch (IOException ex) {
            Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }
    public static class UpdateConnection implements Runnable{
        CoDaPackMain main;
        public UpdateConnection(CoDaPackMain main){
            this.main = main;
        }
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
                        int response = JOptionPane.showConfirmDialog(main, "CoDaPack Updater needs to be updated", "Update confirmation",
                            JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if(response == JOptionPane.YES_OPTION){
                            //System.out.println("Updating CoDaPack updater");
                            url = new URL(CoDaPackConf.HTTP_ROOT + "codapack/CoDaPackUpdater.jar");

                            InputStream is = url.openStream();
                            
                            File tempFile = new File("CoDaPackUpdater.jar_temp");
                            FileOutputStream fos = new FileOutputStream(tempFile);
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
                        int response = JOptionPane.showConfirmDialog(main, "There is a new version of CoDaPack available. Do you want to install it? If you say yes, CoDaPack is going to be closed automatically.", "Update confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
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
    //LLegeix l'ultim path escrit al arxiu recentPath.txt
    public String fillRecentPath() {
        String path = null;
        File arx = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            arx = new File("recentPath.txt");
            fr = new FileReader(arx);
            br = new BufferedReader(fr);
            String linia;
            if ((linia=br.readLine())!=null) {
                path=linia;
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
        return path;
    }
    //Copia o substitueix l'últim path
    public void copyRecentPath(String path) {
        FileWriter fit = null;
        PrintWriter pw = null;
        try {
            fit = new FileWriter("recentPath.txt");
            pw = new PrintWriter(fit);
            pw.println(path);
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
}
