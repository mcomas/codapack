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
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import org.rosuda.JRI.Rengine;

import coda.DataFrame;
import coda.DataFrame.DataFrameException;
import coda.ext.json.JSONException;
import coda.gui.CoDaPackMenu.CoDaPackMenuListener;
import coda.gui.menu.AddNumericVariablesMenu;
import coda.gui.menu.AddToPersonalMenu;
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
import coda.gui.menu.CrearMenuPersonal;
import coda.gui.menu.CreateNewTableMenu;
import coda.gui.menu.DeleteVariablesMenu;
import coda.gui.menu.DiscretizeMenu;
import coda.gui.menu.ExportPersonalMenu;
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
import coda.gui.menu.T1;
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
import coda.io.ImportRDA;
import coda.io.ImportRDA_renjin;
import coda.io.Importer;
import coda.io.WorkspaceIO;
import coda.plot2.TernaryPlot2dDisplay;
import coda.plot2.objects.Ternary2dGridObject;
import coda.plot2.objects.Ternary2dObject;
import coda.plot2.window.TernaryPlot2dWindow;
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
                // TODO Auto-generated catch block
                e.printStackTrace();
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
                        Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
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

    public DataFrame getActiveDataFrame() {
        if (activeDataFrame == -1)
            return null;
        return dataFrame.get(activeDataFrame);
    }

    public void eventCoDaPack(String title) throws ScriptException {  
        if(jMenuBar.item_key.containsKey(title)){
            String id = jMenuBar.item_key.get(title);
            System.out.println(title + " with key " + id);
            if(dynamicMenus.containsKey(id)){
                System.out.println("Loading dynamicMenu " + id);
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
                System.out.println("key: " + key + " does not match");
        }
    }
    public void eventCoDaPackDefault(String title) throws ScriptException{
        String ruta = CoDaPackConf.workingDir; // fillRecentPath();
        JFileChooser chooseFile = new JFileChooser(ruta);
        if (title.equals(jMenuBar.ITEM_IMPORT_XLS)) {
            /*
            JOptionPane.showMessageDialog(this,
                    "Be aware that data must have valid variable names. A valid variable name consists of letters, numbers and the dot or underline characters.\nThe variable name must start with a letter or the dot not followed by a number.\nIf your variable names don't follow this rule, you can correct it on the data table of CoDaPack.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            */
            chooseFile.resetChoosableFileFilters();
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("Excel files", "xls", "xlsx"));
            if (chooseFile.showOpenDialog(jSplitPane) == JFileChooser.APPROVE_OPTION) {
                ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
                ImportXLSMenu importMenu = new ImportXLSMenu(this, true, chooseFile);
                importMenu.setVisible(true);
                DataFrame df = importMenu.getDataFrame();
                if (df != null) {
                    addDataFrame(df);
                }
                importMenu.dispose();
                CoDaPackConf.workingDir = ruta;
            }
        } else if (title.equals(jMenuBar.ITEM_IMPORT_RDA)) {
            // JOptionPane.showMessageDialog(this,
            //         "Be aware that data must have valid variable names. A valid variable name consists of letters, numbers and the dot or underline characters.\nThe variable name must start with a letter or the dot not followed by a number.\nIf your variable names don't follow this rule, you can correct it on the data table of CoDaPack.",
            //         "Warning", JOptionPane.WARNING_MESSAGE);
            
            // Aquí tractem l'event IMPORT_RDA
            chooseFile.resetChoosableFileFilters();
            // Filtrem per llegir només els arxius RDA
            chooseFile.setFileFilter(new FileNameExtensionFilter("R data file", "RData", "rda"));

            // Comprovem si es selecciona un arxiu aprovat
            if (chooseFile.showOpenDialog(jSplitPane) == JFileChooser.APPROVE_OPTION) {
                // Creem una nova instància ImportRDA, serà l'encarregada de mostrar i obrir els
                // dataframes
                Importer importRDA = null;
                if(is_R_available()){
                    importRDA = new ImportRDA(chooseFile.getSelectedFile().getAbsolutePath(), re);
                }else{
                    importRDA = new ImportRDA_renjin(chooseFile.getSelectedFile().getAbsolutePath());
                }
                //ImportRDA impdf = new ImportRDA(chooseFile.getSelectedFile().getAbsolutePath(), re);
                //ImportRDA_renjin impRDA_Renjin = new ImportRDA_renjin(chooseFile.getSelectedFile().getAbsolutePath());
                //ImportRDA impRDA = new ImportRDA(chooseFile.getSelectedFile().getAbsolutePath(), re);
                // Creem una nova instància ImportRDAMenu, serà l'encarregada de gestionar el
                // menú
                ImportRDAMenu imprdam;
                try {
                    imprdam = new ImportRDAMenu(this, importRDA);
                    imprdam.setVisible(true);
                } catch (DataFrameException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // Fem el menú visible
                

            }
            // Copiem la ruta per recordar-la
            ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
            // Guardem la ruta
            CoDaPackConf.workingDir = ruta;
        } else if (title.equals(jMenuBar.ITEM_IMPORT_CSV)) {
            JOptionPane.showMessageDialog(this,
                    "Be aware that data must have valid variable names. A valid variable name consists of letters, numbers and the dot or underline characters.\nThe variable name must start with a letter or the dot not followed by a number.\nIf your variable names don't follow this rule, you can correct it on the data table of CoDaPack.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            chooseFile.resetChoosableFileFilters();
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("Text file", "txt"));
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("CSV file", "csv"));

            if (chooseFile.showOpenDialog(jSplitPane) == JFileChooser.APPROVE_OPTION) {

                ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
                ImportCSVMenu importMenu = new ImportCSVMenu(this, true, chooseFile);
                importMenu.setVisible(true);
                DataFrame df = importMenu.getDataFrame();
                if (df != null) {
                    addDataFrame(df);
                }
                importMenu.dispose();
                CoDaPackConf.workingDir = ruta;
            }
        } else if (title.equals(jMenuBar.ITEM_EXPORT_CSV)) {
            if (this.getActiveDataFrame() != null) {
                chooseFile.resetChoosableFileFilters();
                chooseFile.setFileFilter(new FileNameExtensionFilter("Text file", "txt"));
                chooseFile.setFileFilter(new FileNameExtensionFilter("CSV file", "csv"));

                DataFrame df = this.getActiveDataFrame();

                for (int i = 0; i < df.size(); i++) {
                    re.eval(df.get(i).getName() + " <- NULL");

                    if (df.get(i).isNumeric()) {
                        for (double j : df.get(i).getNumericalData()) {
                            re.eval(df.get(i).getName() + " <- c(" + df.get(i).getName() + "," + String.valueOf(j)
                                    + ")");
                        }
                    } else {
                        for (String j : df.get(i).getTextData()) {
                            re.eval(df.get(i).getName() + " <- c(" + df.get(i).getName() + ",'" + j + "')");
                        }
                    }
                }

                String dataFrameString = "mydf <- data.frame(";

                for (int i = 0; i < df.size(); i++) {
                    dataFrameString += df.get(i).getName();
                    if (i != df.size() - 1)
                        dataFrameString += ",";
                }

                dataFrameString += ")";
                re.eval(dataFrameString); // creem el dataframe amb R

                if (chooseFile.showOpenDialog(jSplitPane) == JFileChooser.APPROVE_OPTION) {
                    ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
                    if (chooseFile.getFileFilter().getDescription().equals("CSV file")) {
                        re.eval("utils::write.csv(mydf, \"" + ruta.replaceAll("\\\\", "/") + "/"
                                + chooseFile.getSelectedFile().getName() + ".csv\", row.names = FALSE)");
                    } else {
                        re.eval("utils::write.table(mydf, \"" + ruta.replaceAll("\\\\", "/") + "/"
                                + chooseFile.getSelectedFile().getName() + ".txt\", row.names = FALSE)");
                    }
                }
                CoDaPackConf.workingDir = ruta;
            } else {
                JOptionPane.showMessageDialog(this, "No data to export");
            }
        } else if (title.equals(jMenuBar.ITEM_EXPORT_XLS)) {
            chooseFile.resetChoosableFileFilters();
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("Excel files", "xls"));
            if (chooseFile.showSaveDialog(jSplitPane) == JFileChooser.APPROVE_OPTION) {

                try {
                    ExportData.exportXLS(chooseFile.getSelectedFile().getAbsolutePath(),
                            dataFrame.get(activeDataFrame));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
            CoDaPackConf.workingDir = ruta;
        } else if (title.equals(jMenuBar.ITEM_EXPORT_R)) {
            new ExportRDataMenu(this, new ExportRDA(re)).setVisible(true);

        } else if (title.equals(jMenuBar.ITEM_OPEN)) {
            System.out.println("ITEM_OPEN");
            if (!dataFrame.isEmpty()) {
                System.out.println("isEmpty");
                // Comprovar si hi ha canvis. si n'hi ha finestra
                boolean hasChange = false;
                Iterator<DataFrame> i = dataFrame.iterator();
                while (hasChange == false && i.hasNext()) {
                    DataFrame df = i.next();
                    if (df.getChange())
                        hasChange = true;
                }
                if (hasChange) {
                    System.out.println(" has change");
                    int response = JOptionPane.showConfirmDialog(this,
                            "<html>Your changes will be lost if you close <br/>Do you want to continue?</html>",
                            "Confirm",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
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
                        } else
                            jMenuBar.active_path = fn;
                    }
                } else {

                    System.out.println("else has change");
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
                    } else
                        jMenuBar.active_path = fn;
                }
            } else {

                System.out.println("else");
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
                } else
                    jMenuBar.active_path = fn;
            }
        } else if (title.equals(jMenuBar.ITEM_ADD)) {
            CoDaPackImporter imp = new CoDaPackImporter().setParameters(this);
            ArrayList<DataFrame> dfs = imp.importDataFrames();
            for (DataFrame df : dfs) {
                addDataFrame(df);
            }
            jMenuBar.fillRecentFiles();
            jMenuBar.saveRecentFile(imp.getParameters());
            // jMenuBar.addRecentFile(imp.getParameters());
        } else if ("format:codapack".equals(title.split("\\?")[0])) {
            CoDaPackImporter imp = new CoDaPackImporter().setParameters(title);
            ArrayList<DataFrame> dfs = imp.importDataFrames();
            for (DataFrame df : dfs) {
                addDataFrame(df);
            }
            jMenuBar.fillRecentFiles();
            jMenuBar.saveRecentFile(imp.getParameters());
            // jMenuBar.addRecentFile(imp.getParameters());
        } else if (title.equals(jMenuBar.ITEM_CLEAR_RECENT)) {
            jMenuBar.removeRecentFiles();
        } else if (title.equals(jMenuBar.ITEM_SAV)) {
            System.out.println("Workspace");
            if (jMenuBar.active_path != null) {
                String fileNameExt = ".cdp";
                String fileName = jMenuBar.active_path;
                String fn;
                if (fileName.endsWith(".xls") || fileName.endsWith(".rda") || fileName.endsWith(".cdp")
                        || fileName.endsWith(".rda") || fileName.endsWith(".txt") || fileName.endsWith(".csv")) {
                    fn = fileName.substring(0, fileName.length() - 4);
                } else if (fileName.endsWith(".xlsx"))
                    fn = fileName.substring(0, fileName.length() - 5);
                else if (fileName.endsWith(".RData"))
                    fn = fileName.substring(0, fileName.length() - 6);
                else
                    fn = fileName + fileNameExt;
                try {
                    WorkspaceIO.saveWorkspace(fn + fileNameExt, this);
                    jMenuBar.saveRecentFile(fn + fileNameExt);
                    // Posem el valor de change de tots els dataframes a false
                    Iterator<DataFrame> i = dataFrame.iterator();
                    while (i.hasNext()) {
                        DataFrame df = i.next();
                        df.setChange(false);
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("else Workspace");
                chooseFile.resetChoosableFileFilters();
                chooseFile.setFileFilter(
                        new FileNameExtensionFilter("CoDaPack Workspace", "cdp"));
                if (chooseFile.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    String filename = chooseFile.getSelectedFile().getAbsolutePath();
                    try {
                        WorkspaceIO.saveWorkspace(
                                filename.endsWith(".cdp") ? filename : filename + ".cdp", this);
                        ruta = filename + ".cdp";
                        jMenuBar.saveRecentFile(ruta);
                        // Posem el valor de change de tots els dataframes a false
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
                CoDaPackConf.workingDir = ruta;
            }
        } else if (title.equals(jMenuBar.ITEM_SAVE)) {
            System.out.println("ITEM_SAVE");
            chooseFile.resetChoosableFileFilters();
            chooseFile.setFileFilter(
                    new FileNameExtensionFilter("CoDaPack Workspace", "cdp"));
            if (chooseFile.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filename = chooseFile.getSelectedFile().getAbsolutePath();
                try {
                    WorkspaceIO.saveWorkspace(
                            filename.endsWith(".cdp") ? filename : filename + ".cdp", this);
                    ruta = filename + ".cdp";
                    jMenuBar.saveRecentFile(ruta);
                    // Posem el valor de change de tots els dataframes a false
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
            CoDaPackConf.workingDir = ruta;
        } else if (title.equals(jMenuBar.ITEM_DEL_DATAFRAME)) {
            System.out.println("item_del_Dataframe");
            if (dataFrame.size() > 0) {
                removeDataFrame(dataFrame.get(activeDataFrame));
            } else {
                JOptionPane.showMessageDialog(this, "No table available");
            }
        } else if (title.equals(jMenuBar.ITEM_DELETE_ALL_TABLES)) {
            System.out.println("Delete All Tables");
            int responseDeleteAllTables = JOptionPane.showConfirmDialog(this, "Are you sure to delete all the tables?",
                    "Delete All Tables", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (responseDeleteAllTables == JOptionPane.YES_OPTION) {
                while (dataFrame.size() > 0) {
                    DataFrame aux = dataFrame.get(0);
                    this.removeDataFrame(aux);
                }
            }
        } else if (title.equals(jMenuBar.ITEM_CLEAR_OUTPUTS)) {
            int responseCleanOutput = JOptionPane.showConfirmDialog(this, "Are you sure to clean the output?",
                    "Clean the output", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (responseCleanOutput == JOptionPane.YES_OPTION) {
                outputPanel.clearOutput();
            }
        } else if (title.equals(jMenuBar.ITEM_QUIT)) {
            jMenuBar.copyRecentFiles();
            // Comprovar si hi ha canvis. si n'hi ha finestra
            boolean hasChange = false;
            Iterator<DataFrame> i = dataFrame.iterator();
            while (hasChange == false && i.hasNext()) {
                DataFrame df = i.next();
                if (df.getChange())
                    hasChange = true;
            }
            if (hasChange) {
                int response = JOptionPane.showConfirmDialog(this,
                        "<html>Your changes will be lost if you close <br/>Do you want to exit?</html>", "Confirm",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    int responseSaveHTML = JOptionPane.showConfirmDialog(this, "Do you want to save the session?",
                            "Save the session", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (responseSaveHTML == JOptionPane.NO_OPTION) {
                        outputPanel.deleteHtml();
                    }
                    dispose();
                    this.closeApplication();
                }
            } else {
                int response = JOptionPane.showConfirmDialog(this, "Do you want to exit?", "Confirm",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    int responseSaveHTML = JOptionPane.showConfirmDialog(this, "Do you want to save the session?",
                            "Save the session", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (responseSaveHTML == JOptionPane.NO_OPTION) {
                        for (int a = 0; a < outputPanels.length; a++) {
                            outputPanels[a].deleteHtml();
                        }
                        // outputPanel.deleteHtml();
                    }
                    dispose();
                    this.closeApplication();
                }
            }
        } else if (title.equals(jMenuBar.ITEM_CONF)) {
            if (configurationMenu == null)
                configurationMenu = new ConfigurationMenu(this);
            configurationMenu.setVisible(true);
 
        // } else if (title.equals(jMenuBar.ITEM_FORCE_UPDATE)) {
        //     CoDaPackConf.refusedVersion = CoDaPackConf.CoDaVersion;
        //     UpdateConnection uc = new UpdateConnection(this);
        //     new Thread(uc).start();
        } else if (title.equals(jMenuBar.ITEM_ABOUT)) {
            new CoDaPackAbout(this).setVisible(true);
        } else if (title.equals(jMenuBar.R_TEST)) {
            // first we get the session info
            System.out.println("R_TEST");
            re.eval("a <- capture.output(sessionInfo())");
            OutputElement e = new OutputForR(re.eval("a").asStringArray());
            outputPanel.addOutput(e);

            // after we get the system variables

            re.eval("a <- capture.output(Sys.getenv())");
            e = new OutputForR(re.eval("a").asStringArray());
            outputPanel.addOutput(e);

            //  the capabilities

            re.eval("a <- capture.output(capabilities())");
            e = new OutputForR(re.eval("a").asStringArray());
            outputPanel.addOutput(e);

            // installed packages

            re.eval("ip = as.data.frame(installed.packages()[,c(1,3:4)])");
            re.eval("ip = ip[is.na(ip$Priority),1:2,drop=FALSE]");
            e = new OutputForR(re.eval("capture.output(ip)").asStringArray());
            outputPanel.addOutput(e);


        // } else if (title.equals(jMenuBar.ITEM_MODEL_S0)) {
        //     new S0(this, re).setVisible(true);
        // } else if (title.equals(jMenuBar.ITEM_MODEL_S1)) {
        //     new S1(this, re).setVisible(true);
        // } else if (title.equals(jMenuBar.ITEM_MODEL_S2)) {
        //     new S2(this, re).setVisible(true);
        // } else if (title.equals(jMenuBar.ITEM_MODEL_S3)) {
        //     new S3(this, re).setVisible(true);
        // } else if (title.equals(jMenuBar.ITEM_MODEL_S4)) {
        //     new S4(this, re).setVisible(true);
        // } else if (title.equals(jMenuBar.ITEM_MODEL_AddtoHTMLJavaScript)) {
        //     try {
        //         new AddToHTMLJavaScript(this, re).setVisible(true);
        //     } catch (FileNotFoundException e) {
        //         // TODO Auto-generated catch block
        //         e.printStackTrace();
        //     }
        } else if (title.equals(jMenuBar.ITEM_MODEL_CPM)) {
            new CrearMenuPersonal(this, re).setVisible(true);
        } else if (title.equals(jMenuBar.ITEM_MODEL_PM)) {
            new T1(this, re).setVisible(true);
        } else if (title.equals(jMenuBar.ITEM_MODEL_IPM)) {
            new AddToPersonalMenu(this, re).setVisible(true);
        } else if (title.equals(jMenuBar.ITEM_MODEL_EPM)) {
            new ExportPersonalMenu(this, re).setVisible(true);
        } else {
            for (int i = 0; i < jMenuBar.NomsMenuItems.size(); i++) {
                if (title.equals(jMenuBar.NomsMenuItems.get(i))) {
                    ArchiuSeleccionat = jMenuBar.NomsMenuItems.get(i);
                    new T1(this, re).setVisible(true);

                }
            }
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
    
        var path = System.getProperty("java.library.path");
        
        try {
            System.loadLibrary("jri");
        } catch (UnsatisfiedLinkError e) {
            System.out.println("JRI not detected");
            System.out.println("Looking for library at: " + path.toString());
            
            jriAvailable = false;
        }
        if(!jriAvailable){
            try {
            System.load("Rlibraries/rJava/jri/libjri.jnilib");
            jriAvailable = true;
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Error when loading Rlibraries/rJava/jri/libjri.jnilib");
            
            jriAvailable = false;
        }
        }
        Properties props = System.getProperties();
        props.list(System.out);
    }
    /**
     * @param args the command line arguments
     */
    static boolean is_R_available(){
        // System.out.println("R_HOME =" + System.getenv("R_HOME"));
        if(System.getenv("R_HOME") == null | !jriAvailable) return(false);
        return(Rengine.versionCheck());
        // for(String v: System.getenv().keySet()){
        //     System.out.println(v + " = " + System.getenv(v));
        // }
        // return(true);
    }
    public static boolean R_available = false;
    public static void main(String args[]) throws Exception {
        /*
         * Look and Feel: change appearence according to OS
         */
        System.out.println("Current JVM version - " + System.getProperty("java.version"));
        //Runtime.getRuntime().exec("Rscript install_packages.R");
        CoDaPackConf.workingDir = System.getProperty("user.dir");
        //re.eval("library(coda.base, lib.loc='Rlibraries')");
        //re.eval("library(zCompositions, lib.loc='Rlibraries')");
        R_available = is_R_available();
        if(R_available){
            re = new RScriptEngine(Rargs, false, null);
            re.eval("options(width=10000)");
            re.eval("Sys.setlocale('LC_NUMERIC','C')");
        }
        
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
         * Congifuration file creation if it not exists. Using static class CoDaPackConf
         */
        File f = new File(CoDaPackConf.configurationFile);
        System.out.println("Configuration file: " + f.getAbsolutePath());
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
                } catch (URISyntaxException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
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
