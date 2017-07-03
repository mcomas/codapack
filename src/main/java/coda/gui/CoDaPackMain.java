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
import coda.Workspace;
import coda.io.WorkspaceIO;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
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
    
   
    public MainMenu menu;
    public static Output output;
    public static DataList dataList;
    public static Table table;
      
    public static Workspace workspace;
    
    @Override 
    public void start(Stage stage) {
        //setUserAgentStylesheet(STYLESHEET_CASPIAN);
        
        dataList = new DataList();
        dataList.setPrefWidth(100);
        
        TabPane tp = new TabPane();
        tp.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab tabOutput = new Tab();
        tabOutput.setText("Output");
        BorderPane bp1 = new BorderPane();
        tabOutput.setContent(bp1);
        
        Tab tabCommands = new Tab();
        tabCommands.setText("Commands");
        BorderPane bp2 = new BorderPane();
        tabCommands.setContent(bp2);
        
        output = new Output();
        bp1.setCenter(output);
        bp2.setCenter(new Pane());
        tp.getTabs().add(tabOutput);
        tp.getTabs().add(tabCommands);
        
        table = new Table();   
        stage.getIcons().add(new Image(CoDaPackMain.class.getResourceAsStream( CoDaPackConf.RESOURCE_PATH + "logoL.png" )));
        workspace = WorkspaceIO.openWorkspace("alimentation.cdp");
        table.addDataFrame(workspace.getActiveDataFrame());
        
        
        SplitPane sp = new SplitPane();
        sp.setOrientation(Orientation.VERTICAL);
        final StackPane sp1 = new StackPane();
        sp1.getChildren().add(tp);
        final StackPane sp2 = new StackPane();
        sp2.getChildren().add(table);
        sp.getItems().addAll(sp1, sp2);
      
        
        menu = new MainMenu(workspace);

        
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(dataList);
        borderPane.setCenter(sp);
        borderPane.setTop(menu);
        
        Scene scene = new Scene(borderPane, 800, 500, Color.WHITE);        
        stage.setTitle("CoDaPack 3"); 
        stage.setScene(scene); 
        stage.sizeToScene(); 
        stage.show();

        
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
    public void runMenuItem(String title){
        if(title.equals(MainMenu.ITEM_IMPORT_XLS)){
            FlowPane pane1=new FlowPane();
            

            
            //this.setStyle("-fx-background-color:tan;-fx-padding:10px;");
        }
       
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

//    public void eventCoDaPack(String action) throws ScriptException{

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
