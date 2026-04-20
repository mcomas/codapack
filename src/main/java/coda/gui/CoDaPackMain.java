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

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
//import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.script.ScriptException;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import coda.DataFrame;
import coda.DataFrame.DataFrameException;
import coda.ext.json.JSONException;
import coda.gui.CoDaPackMenu.CoDaPackMenuListener;
import coda.gui.menu.AddNumericVariablesMenu;
import coda.gui.menu.AdvancedFilterMenu;
import coda.gui.menu.BalanceDendrogramMenu;
import coda.gui.menu.CLRBiplotMenu;
import coda.gui.menu.CLRPlotMenu;
import coda.gui.menu.CalculateNewVarMenu;
import coda.gui.menu.Categoric2NumericMenu;
import coda.gui.menu.CenterConfidenceRegionPlotMenu;
import coda.gui.menu.CenterDataMenu;
import coda.gui.menu.ChangeGroupNameMenu;
import coda.gui.menu.ClasStatsSummaryMenu;
import coda.gui.menu.ClosureDataMenu;
import coda.gui.menu.CompStatsSummaryMenu;
import coda.gui.menu.ConfigurationMenu;
import coda.gui.menu.CreateNewTableMenu;
import coda.gui.menu.DeleteVariablesMenu;
import coda.gui.menu.DiscretizeMenu;
import coda.gui.menu.ExportRDataMenu;
import coda.gui.menu.FilterMenu;
import coda.gui.menu.ILRCLRBiplotMenu;
import coda.gui.menu.ImportCSVMenu;
import coda.gui.menu.ImportRDAMenu;
import coda.gui.menu.ImportXLSMenu;
import coda.gui.menu.LogRatioNormalityTestMenu;
import coda.gui.menu.Numeric2CategoricMenu;
import coda.gui.menu.PerturbateDataMenu;
import coda.gui.menu.PowerDataMenu;
import coda.gui.menu.PredictiveRegionPlotMenu;
import coda.gui.menu.RBasedGenericMenu_jri;
import coda.gui.menu.SetDetectionLimitMenu;
import coda.gui.menu.SortDataMenu;
import coda.gui.menu.TernaryQuaternaryPCPlotMenu;
import coda.gui.menu.TernaryQuaternaryPlotMenu;
import coda.gui.menu.TransformationALRMenu;
import coda.gui.menu.TransformationCLRMenu;
import coda.gui.menu.TransformationILRRawMenu;
import coda.gui.menu.TransformationRawILRMenu;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;
import coda.gui.table.TablePanel;
import coda.gui.utils.FileNameExtensionFilter;
import coda.io.CoDaPackImporter;
import coda.io.ExportData;
import coda.io.ExportRDA;
import coda.io.Importer;
import coda.io.WorkspaceIO;
import coda.plot2.TernaryPlot2dDisplay;
import coda.plot2.objects.Ternary2dGridObject;
import coda.plot2.objects.Ternary2dObject;
import coda.plot2.window.TernaryPlot2dWindow;
import coda.service.RDataFileService;
import coda.service.RIntegrationService;
import coda.util.AppLogger;
import coda.util.RScriptEngine;

/**
 *
 * @author mcomas
 */
public final class CoDaPackMain extends JFrame {
    public static final long serialVersionUID = 1L;
    // CoDaPack version

    // dataFrame is the class containing the data
    private final ArrayList<DataFrame> dataFrame = new ArrayList<DataFrame>();
    private int activeDataFrame = -1;

    public static String RESOURCE_PATH = "/";

    private final Dimension screenDimension;

    // Panel showing the non-column outputs and the data contained in the dataFrame
    public static JTabbedPane outputs;
    public static OutputPanel outputPanel;
    public static OutputPanel[] outputPanels;
    public static TablePanel tablePanel;
    // Panel with the active variable
    public static DataList dataList;
    private JSplitPane jSplitPane;

    /**
     * VARIABLE R
     */
    public static String[] Rargs = {"--no-save" }; // {"--vanilla" };
    public static RScriptEngine re = null;

    private JComboBox<String> dataFrameSelector;
    private final DataFrameSelectorListener dataFrameListener = new DataFrameSelectorListener();

    String ArchiuSeleccionat;

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
    private SortDataMenu sortDataMenu;
    private FilterMenu filterMenu;
    private AdvancedFilterMenu advancedFilterMenu;
    private CompStatsSummaryMenu compStatsSummaryMenu;
    private ClasStatsSummaryMenu clasStatsSummaryMenu;
    private LogRatioNormalityTestMenu logRatioNormalityTestMenu;
    private TernaryQuaternaryPlotMenu ternaryQuaternaryPlotMenu;
    private TernaryQuaternaryPCPlotMenu ternaryQuaternaryPCPlotMenu;
    private PredictiveRegionPlotMenu predictiveRegionPlotMenu;
    private CenterConfidenceRegionPlotMenu centerConfidenceRegionPlotMenu;
    private CLRBiplotMenu cLRBiplotMenu;
    private CLRPlotMenu cLRPlotMenu;
    private ILRCLRBiplotMenu iLRCLRBiplotMenu;
    private BalanceDendrogramMenu balanceDendrogramMenu;
    private ChangeGroupNameMenu changeGroupNameMenu;
    private ConfigurationMenu configurationMenu;

    public static CoDaPackConf config = new CoDaPackConf();

    JTabbedPane tabbedPane = new JTabbedPane();

    // Mac users expect the menu bar to be at the top of the screen, not in the
    // window, so enable that setting. (This is ignored on non-Mac systems).
    // Setting should be set in an static context
    static {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "CoDaPack");
        System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
    }

    public CoDaPackMain() throws Exception {
        screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Es carrega el logo del CoDaPack
        initComponents();
        this.setIconImage(
                Toolkit.getDefaultToolkit().getImage(
                        getClass().getResource(CoDaPackMain.RESOURCE_PATH + "logoL.png")));
        outputPanel.addWelcome(CoDaPackConf.getVersion());
    }
    public CoDaPackMain(boolean rcoda_call){
        screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        
        initComponents(rcoda_call);
        if(!rcoda_call){
            // Es carrega el logo del CoDaPack
            
            this.setIconImage(
            Toolkit.getDefaultToolkit().getImage(
            getClass().getResource(CoDaPackMain.RESOURCE_PATH + "logoL.png")));
            try {
                outputPanel.addWelcome(CoDaPackConf.getVersion());
            } catch (Exception e) {
                AppLogger.error(CoDaPackMain.class, "Unable to initialize welcome output", e);
            }
}
        
    }
    public void closeApplication() {
        CoDaPackConf.saveConfiguration();
        System.exit(0);
    }

    private void initComponents() {
        initComponents(false);
    }
    private void initComponents(boolean rcoda_call) {
        ITEM_APPLICATION_NAME = "CoDaPack v" + CoDaPackConf.getVersion();
        outputPanel = new OutputPanel();
        outputPanels = new OutputPanel[1];        
        outputPanels[0] = outputPanel;


        tablePanel = new TablePanel(this);
        dataList = new DataList();
        if(!rcoda_call){
            // Panel with the active variable            
            jMenuBar = new CoDaPackMenu(this);
            jMenuBar.addCoDaPackMenuListener(new CoDaPackMenuListener() {
                @Override
                public void menuItemClicked(String v) {
                    try {
                        eventCoDaPack(v);
                    } catch (ScriptException ex) {
                        AppLogger.error(CoDaPackMain.class, "Menu action failed: " + v, ex);
                    }
                }
            });
        }
        setTitle(ITEM_APPLICATION_NAME);
        int w_size = Math.round(3*screenDimension.width/4);
        int h_size = Math.round(3*screenDimension.height/4);
        setPreferredSize(new Dimension(w_size, h_size));
        setLocation((screenDimension.width - w_size) / 2,
                (screenDimension.height - h_size) / 2);

        dataList.setSize(new Dimension(150, 700));
        dataFrameSelector = new JComboBox<String>();
        dataFrameSelector.setPrototypeDisplayValue("XXXXXXXXXXXXXX");
        dataFrameSelector.addItemListener(dataFrameListener);

        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BorderLayout());
        JPanel dfSelect = new JPanel();
        dfSelect.add(new JLabel("Tables"));
        dfSelect.add(dataFrameSelector);
        westPanel.add(dfSelect, BorderLayout.NORTH);
        westPanel.add(dataList, BorderLayout.CENTER);

        // -------------tabbedPanned

        tabbedPane.setFocusable(false);
        // tabbedPane.setSize(500, 340);

        tabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (SwingUtilities.isRightMouseButton(evt)) {
                    int index = tabbedPane.getSelectedIndex();

                    if (index != 0) {
                        JPopupMenu popupMenu = new JPopupMenu();
                        JMenuItem delete = new JMenuItem("DELETE");
                        delete.addActionListener(new java.awt.event.ActionListener() {
                            @Override
                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                tabbedPane.remove(index);
                            }
                        });
                        popupMenu.add(delete);
                        popupMenu.show(tabbedPane, evt.getX(), evt.getY());
                    }
                }
            }
        });

        tabbedPane.add("Main", outputPanel);

        GraphicsConfiguration gc = outputPanel.getGraphicsConfiguration();
        AppLogger.info(CoDaPackMain.class, "GraphicsConfiguration: " + gc);
        
        jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane, tablePanel);
        // -------------
        // jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, outputPanel,
        // tablePanel);
        jSplitPane.setDividerSize(7);
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setDividerLocation(350);

        JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westPanel, jSplitPane);
        main.setDividerSize(7);
        getContentPane().add(main, BorderLayout.CENTER);

        setJMenuBar(jMenuBar);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jMenuBar.itemQuit.doClick();
            }
        });
        pack();

        // gc = outputPanel.getGraphicsConfiguration();
        // System.out.println("GraphicsConfiguration: " + gc);
        // double outputPanelScaling = 1.0;
        // if (gc != null) {
        //     AffineTransform tx = gc.getDefaultTransform();
        //     outputPanelScaling = tx.getScaleX();
        // } 
        int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        double scale = dpi / 96.0; // 96 DPI és l'estàndard "1.0"
        AppLogger.info(CoDaPackMain.class, "DPI: " + dpi + " -> scale: " + scale);
        AppLogger.info(CoDaPackMain.class, "Current scaling: " + scale);
        // outputPanel.browser.setZoom(2);

    }

    public boolean searchPanel(String name) {
        boolean trobat = false;
        int i = 0;
        while (!trobat && i < outputPanels.length) {
            if (outputPanels[i].returnHTMLName().equals(name)) {
                trobat = true;
                outputPanel = outputPanels[i];
                tabbedPane.setSelectedIndex(i);
            }
            i++;
        }
        return trobat;
    }

    public void returnToMainPanel() {
        outputPanel = outputPanels[0];
    }

    public void addTabbedPannel(String nameHtml, String HTMLtext) {

        OutputPanel[] _outputPanels = new OutputPanel[outputPanels.length + 1];

        for (int i = 0; i < outputPanels.length; i++)
            _outputPanels[i] = outputPanels[i];

        OutputPanel auxOutputPanel = new OutputPanel(nameHtml);
        _outputPanels[outputPanels.length] = auxOutputPanel;

        outputPanels = new OutputPanel[_outputPanels.length];
        outputPanels = _outputPanels.clone();

        tabbedPane.add(nameHtml, auxOutputPanel);
        JLabel tabTitleLabel = new JLabel(nameHtml);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabTitleLabel);
    }

    public boolean isDataFrameNameAvailable(String name) {
        for (DataFrame df : dataFrame)
            if (df.name.equals(name))
                return false;
        return true;
    }

    public void removeDataFrame(DataFrame df) {
        dataFrameSelector.removeItemListener(dataFrameListener);
        dataFrameSelector.removeItem(df.name);
        dataFrame.remove(df);
        int s = dataFrame.size();
        if (s != 0) {
            activeDataFrame = 0;
            DataFrame new_df = dataFrame.get(activeDataFrame);
            dataList.setData(new_df);
            tablePanel.setDataFrame(new_df);
            dataFrameSelector.setSelectedItem(new_df.name);
            dataFrameSelector.addItemListener(dataFrameListener);
        } else {
            activeDataFrame = -1;
            dataList.clearData();
            tablePanel.clearData();
            dataFrameSelector.removeAllItems();
        }
    }

    public void addDataFrame(DataFrame df) {
        if (isDataFrameNameAvailable(df.name)) {
            activeDataFrame = dataFrame.size();
            dataFrame.add(df);
            dataList.setData(df);
            tablePanel.setDataFrame(df);
            dataFrameSelector.removeItemListener(dataFrameListener);
            dataFrameSelector.addItem(df.name);
            dataFrameSelector.setSelectedItem(df.name);
            dataFrameSelector.addItemListener(dataFrameListener);
            df.setChange(false);
        } else {
            JOptionPane.showMessageDialog(this, "<html>Dataframe <i>" +
                    df.name + "</i> is already loaded.</html>");
        }
    }

    public void addDataFrameRDR(DataFrame df) {
        if (isDataFrameNameAvailable(df.name)) {
            activeDataFrame = dataFrame.size();
            dataFrame.add(df);
            dataList.setData(df);
            tablePanel.setDataFrame(df);
            dataFrameSelector.removeItemListener(dataFrameListener);
            dataFrameSelector.addItem(df.name);
            dataFrameSelector.setSelectedItem(df.name);
            dataFrameSelector.addItemListener(dataFrameListener);
            df.setChange(false);
        } else {
            JOptionPane.showMessageDialog(this, "<html>Dataframe <i>" +
                    df.name + "</i> is already loaded.<br/>Please, use Prefix or Suffix Options.</html>");
        }
    }

    public void updateDataFrame(DataFrame df) {
        dataList.setData(df);
        dataFrameSelector.setSelectedItem(df.name);
        tablePanel.setDataFrame(df);
    }

    public ArrayList<DataFrame> getAllDataFrames() {
        return dataFrame;
    }

    public boolean hasUnsavedChanges() {
        Iterator<DataFrame> i = dataFrame.iterator();
        while (i.hasNext()) {
            if (i.next().getChange()) {
                return true;
            }
        }
        return false;
    }

    public void markAllDataFramesSaved() {
        Iterator<DataFrame> i = dataFrame.iterator();
        while (i.hasNext()) {
            i.next().setChange(false);
        }
    }

    public void clearAllDataFrames() {
        while (!dataFrame.isEmpty()) {
            removeDataFrame(dataFrame.get(0));
        }
        if (jMenuBar != null) {
            jMenuBar.active_path = null;
        }
    }

    public DataFrame getActiveDataFrame() {
        if (activeDataFrame == -1)
            return null;
        return dataFrame.get(activeDataFrame);
    }

    public int getActiveDataFrameIndex() {
        return activeDataFrame;
    }

    public ConfigurationMenu getConfigurationMenu() {
        return configurationMenu;
    }

    public void setConfigurationMenu(ConfigurationMenu configurationMenu) {
        this.configurationMenu = configurationMenu;
    }

    public void eventCoDaPack(String title) throws ScriptException {  
        if(jMenuBar.item_key.containsKey(title)){
            String id = jMenuBar.item_key.get(title);
            AppLogger.info(CoDaPackMain.class, title + " with key " + id);
            if(dynamicMenus.containsKey(id)){
                AppLogger.info(CoDaPackMain.class, "Loading dynamicMenu " + id);
                dynamicMenus.get(id).reLocate();
                dynamicMenus.get(id).updateMenuDialog();
                dynamicMenus.get(id).setVisible(true);
            } else{
                eventCoDaPackWithKey(jMenuBar.item_key.get(title));
        }}else{
            //System.out.println(title + " default menu");
            eventCoDaPackDefault(title);
        }
    }

    HashMap<String,RBasedGenericMenu_jri> dynamicMenus = new HashMap<String,RBasedGenericMenu_jri>();
    
    public void eventCoDaPackWithKey(String key){
        switch(key){
            case "TransformationALR":
                if(transformationALRMenu == null) transformationALRMenu = new TransformationALRMenu(this);
                transformationALRMenu.updateMenuDialog();
                transformationALRMenu.setVisible(true);
                break;
            case "TransformationCLR":
                if(transformationCLRMenu == null) transformationCLRMenu = new TransformationCLRMenu(this);
                transformationCLRMenu.updateMenuDialog();
                transformationCLRMenu.setVisible(true);
                break;
            case "TransformationRawOLR":
                if(transformationRawILRMenu == null) transformationRawILRMenu = new TransformationRawILRMenu(this);
                transformationRawILRMenu.updateMenuDialog();
                transformationRawILRMenu.setVisible(true);
                break;
            case "TransformationOLRRaw":
                if(transformationILRRawMenu == null) transformationILRRawMenu = new TransformationILRRawMenu(this);
                transformationILRRawMenu.updateMenuDialog();
                transformationILRRawMenu.setVisible(true);
                break;
            case "CenterData":
                if(centerDataMenu == null) centerDataMenu = new CenterDataMenu(this);
                centerDataMenu.updateMenuDialog();
                centerDataMenu.setVisible(true);
                break;
            case "ClosureData":
                if(closureDataMenu == null) closureDataMenu = new ClosureDataMenu(this);
                closureDataMenu.updateMenuDialog();
                closureDataMenu.setVisible(true);
                break;
            case "PerturbateData":
                if(perturbateDataMenu == null) perturbateDataMenu = new PerturbateDataMenu(this);
                perturbateDataMenu.updateMenuDialog();
                perturbateDataMenu.setVisible(true);
                break;
            case "PowerData":
                if(powerDataMenu == null) powerDataMenu = new PowerDataMenu(this);
                powerDataMenu.updateMenuDialog();
                powerDataMenu.setVisible(true);
                break;
            case "Discretize":
                if(discretizeMenu == null) discretizeMenu = new DiscretizeMenu(this);
                discretizeMenu.updateMenuDialog();
                discretizeMenu.setVisible(true);
                break;
            case "CalculateNewVar":
                if(calculateNewVarMenu == null) calculateNewVarMenu = new CalculateNewVarMenu(this);
                calculateNewVarMenu.updateMenuDialog();
                calculateNewVarMenu.setVisible(true);
                break;
            case "SortData":
                if(sortDataMenu == null) sortDataMenu = new SortDataMenu(this);
                sortDataMenu.updateMenuDialog();
                sortDataMenu.setVisible(true);
                break;
            case "Numeric2Categoric":
                new Numeric2CategoricMenu(this).setVisible(true);
                break;
            case "Categoric2Numeric":
                new Categoric2NumericMenu(this).setVisible(true);
                break;
            case "ChangeGroupName":
                if(changeGroupNameMenu == null) changeGroupNameMenu = new ChangeGroupNameMenu(this);
                changeGroupNameMenu.updateMenuDialog();
                changeGroupNameMenu.setVisible(true);
                break;
            case "Filter":
                if(filterMenu == null) filterMenu = new FilterMenu(this);
                filterMenu.updateMenuDialog();
                filterMenu.setVisible(true);
                break;
            case "AdvancedFilter":
                if(advancedFilterMenu == null) advancedFilterMenu = new AdvancedFilterMenu(this);
                advancedFilterMenu.updateMenuDialog();
                advancedFilterMenu.setVisible(true);
                break;
            case "CreateNewTable":
                new CreateNewTableMenu(this).setVisible(true);
                break;
            case "AddNumericVariables":
                new AddNumericVariablesMenu(this).setVisible(true);
                break;
            case "DeleteVariables":
                new DeleteVariablesMenu(this).setVisible(true);
                break;
            case "SetDetectionLimit":
                if(setDetectionLimitMenu == null) setDetectionLimitMenu = new SetDetectionLimitMenu(this);
                setDetectionLimitMenu.updateMenuDialog();
                setDetectionLimitMenu.setVisible(true);
                break;
            case "CompStatsSummary":
                if(compStatsSummaryMenu == null) compStatsSummaryMenu = new CompStatsSummaryMenu(this);
                compStatsSummaryMenu.updateMenuDialog();
                compStatsSummaryMenu.setVisible(true);
                break;
            case "ClasStatsSummary":
                if(clasStatsSummaryMenu == null) clasStatsSummaryMenu = new ClasStatsSummaryMenu(this);
                clasStatsSummaryMenu.updateMenuDialog();
                clasStatsSummaryMenu.setVisible(true);
                break;
            case "LogRatioNormalityTest":
                if(logRatioNormalityTestMenu == null) logRatioNormalityTestMenu = new LogRatioNormalityTestMenu(this);
                logRatioNormalityTestMenu.updateMenuDialog();
                logRatioNormalityTestMenu.setVisible(true);
                break;
            case "TernaryQuaternaryPlot":
                if(ternaryQuaternaryPlotMenu == null) ternaryQuaternaryPlotMenu = new TernaryQuaternaryPlotMenu(this);
                ternaryQuaternaryPlotMenu.updateMenuDialog();
                ternaryQuaternaryPlotMenu.setVisible(true);
                break;
            case "TernaryQuaternaryPlotEmpty":
                String names[] = { "X", "Y", "Z" };
                TernaryPlot2dDisplay display = new TernaryPlot2dDisplay(names);

                double definedGrid[] = { 0.01, 0.05, 0.10, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99 };
                Ternary2dObject gridObject = new Ternary2dGridObject(display, definedGrid);
                display.addCoDaObject(gridObject);
                double center[] = { 1, 1, 1 };

                TernaryPlot2dWindow frame = new TernaryPlot2dWindow(this.getActiveDataFrame(), display,
                        "Ternary/Quaternary Plot -- Testing version",
                        Paths.get(CoDaPackConf.helpPath, "Graphs.Ternary-Quaternary Plot-Empty.yaml").toString(),
                        "Ternary/Quaternary Plot Empty Help Menu");
                frame.setLocationRelativeTo(this);
                frame.setCenter(center);
                frame.setVisible(true);
                break;
            case "TernaryQuaternaryPCPlot":
                if(ternaryQuaternaryPCPlotMenu == null) ternaryQuaternaryPCPlotMenu = new TernaryQuaternaryPCPlotMenu(this);
                ternaryQuaternaryPCPlotMenu.updateMenuDialog();
                ternaryQuaternaryPCPlotMenu.setVisible(true);
                break;
            case "PredictiveRegionPlot":
                if(predictiveRegionPlotMenu == null) predictiveRegionPlotMenu = new PredictiveRegionPlotMenu(this);
                predictiveRegionPlotMenu.updateMenuDialog();
                predictiveRegionPlotMenu.setVisible(true);
                break;
            case "CenterConfidenceRegionPlot":
                if(centerConfidenceRegionPlotMenu == null) centerConfidenceRegionPlotMenu = new CenterConfidenceRegionPlotMenu(this);
                centerConfidenceRegionPlotMenu.updateMenuDialog();
                centerConfidenceRegionPlotMenu.setVisible(true);
                break;
            case "CLRBiplot":
                if(cLRBiplotMenu == null) cLRBiplotMenu = new CLRBiplotMenu(this);
                cLRBiplotMenu.updateMenuDialog();
                cLRBiplotMenu.setVisible(true);
                break;
            case "CLRPlot":
                if(cLRPlotMenu == null) cLRPlotMenu = new CLRPlotMenu(this);
                cLRPlotMenu.updateMenuDialog();
                cLRPlotMenu.setVisible(true);
                break;
            case "OLRCLRBiplot":
                if(iLRCLRBiplotMenu == null) iLRCLRBiplotMenu = new ILRCLRBiplotMenu(this);
                iLRCLRBiplotMenu.updateMenuDialog();
                iLRCLRBiplotMenu.setVisible(true);
                break;
            case "BalanceDendrogram":
                if(balanceDendrogramMenu == null) balanceDendrogramMenu = new BalanceDendrogramMenu(this);
                balanceDendrogramMenu.updateMenuDialog();
                balanceDendrogramMenu.setVisible(true);
                break;
            default:
                AppLogger.warning(CoDaPackMain.class, "No menu handler found for key: " + key);
        }
    }
    public void eventCoDaPackDefault(String title) throws ScriptException{
        if (FileImportExportController.handleAction(title, this, jMenuBar, jSplitPane)) {
            return;
        }
        if (WorkspaceActionController.handleAction(title, this, jMenuBar)) {
            return;
        }
        if (GeneralUiActionController.handleAction(title, this, jMenuBar)) {
            return;
        }
    }

    public class DataFrameSelectorListener implements ItemListener {
        public void itemStateChanged(ItemEvent ie) {
            JComboBox<String> combo = (JComboBox<String>) ie.getSource();
            if (activeDataFrame != combo.getSelectedIndex()) {
                activeDataFrame = combo.getSelectedIndex();
                dataList.setData(dataFrame.get(activeDataFrame));
                tablePanel.setDataFrame(dataFrame.get(activeDataFrame));
                dataFrame.get(activeDataFrame).setChange(true);
            }
        }
    }

    static boolean jriAvailable = true;
    static {
        jriAvailable = RIntegrationService.initializeNativeLibrary();
    }
    /**
     * @param args the command line arguments
     */
    static boolean is_R_available(){
        if(!jriAvailable) return(false);
        return RIntegrationService.isRAvailable();
    }
    public static boolean R_available = false;
    public static void main(String args[]) throws Exception {
        /*
         * Look and Feel: change appearence according to OS
         */
        AppLogger.info(CoDaPackMain.class, "Current JVM version - " + System.getProperty("java.version"));
        //Runtime.getRuntime().exec("Rscript install_packages.R");
        CoDaPackConf.workingDir = System.getProperty("user.dir");
        R_available = is_R_available();
        re = RIntegrationService.initializeEngine(Rargs);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
            AppLogger.error(CoDaPackMain.class, "Unable to set system look and feel", ex);
        }

        /*
         * Congifuration file creation if it not exists. Using static class CoDaPackConf
         */
        File f = new File(CoDaPackConf.configurationFile);
        AppLogger.info(CoDaPackMain.class, "Configuration file: " + f.getAbsolutePath());
        if (f.exists()){            
            CoDaPackConf.loadConfiguration();
        // CoDaPackConf.saveConfiguration();
        }else{
            CoDaPackConf.saveConfiguration();
        }
        /*
         * CoDaPack connects to IMA server through a thread. Avoiding problems in IMA
         * server
         */

        /*
         * CoDaPack main class is created and shown
         */
        CoDaPackMain main = new CoDaPackMain();

        // UpdateConnection uc = new UpdateConnection(main);
        // new Thread(uc).start();

        main.setVisible(true);

        // Si s'ha clicat un arxiu associat ens arribarà com argument i el tractem
        if (args.length > 0) {
            // Guardem la ruta i l'arxiu a recent files
            main.jMenuBar.saveRecentFile(args[0]);
            // Obrim l'arxiu
            CoDaPackImporter imp = new CoDaPackImporter().setParameters("format:codapack?" + args[0]);
            ArrayList<DataFrame> dfs = imp.importDataFrames();

            for (DataFrame df : dfs) {
                main.addDataFrame(df);
            }
        }

    }

    public String getNameTXT() {

        return ArchiuSeleccionat;
    }

    public static class UpdateConnection implements Runnable {
        CoDaPackMain main;

        public UpdateConnection(CoDaPackMain main) {
            this.main = main;
        }

        public void run() {
            //checkForUpdate();
        }

        public void redirectForUpdating(String newversion) {
            Object[] options = { "Quit and download...",
                    "No, thanks. Maybe later",
                    "No, thanks. Stop asking" };
            int n = JOptionPane.showOptionDialog(main,
                    "CoDaPack " +
                            newversion.replace(' ', '.') +
                            " is now available (you're using " +
                            CoDaPackConf.CoDaVersion.replace(' ', '.') +
                            ").",
                    "Update Available",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (n == JOptionPane.YES_OPTION) {
                try {
                    Desktop.getDesktop().browse(new URI("http://mcomas.net/codapack"));
                } catch (URISyntaxException | IOException ex) {
                    AppLogger.error(CoDaPackMain.class, "Unable to open browser for updater", ex);
                }
                main.closeApplication();
            }
            if (n == JOptionPane.CANCEL_OPTION) {
                CoDaPackConf.refusedVersion = newversion;
                CoDaPackConf.saveConfiguration();
            }
        }
        /* 
        public void checkForUpdate() {
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
                if (CoDaPackConf.updateNeeded(serverData.getString("codapack-version")))
                    redirectForUpdating(serverData.getString("codapack-version"));
                else
                    JOptionPane.showMessageDialog(null, "No update available");
            } catch (MalformedURLException ex) {
                Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        */

    }
}
