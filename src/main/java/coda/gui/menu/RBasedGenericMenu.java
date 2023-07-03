package coda.gui.menu;

import coda.BasicStats;
import coda.CoDaStats;
import coda.DataFrame;
import coda.Variable;
import coda.ext.json.JSONArray;
import coda.ext.json.JSONException;
import coda.ext.json.JSONObject;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.menu.RBasedGenericMenu_renjin.RConversion;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;
import coda.gui.output.OutputText;
import coda.gui.utils.BinaryPartitionRowHeaders;
import coda.gui.utils.BinaryPartitionSelect;
import coda.gui.utils.BinaryPartitionTable;
import coda.gui.utils.DataSelector;
import coda.gui.utils.DataSelector1to1;
import coda.gui.utils.DataSelector1to2;
import coda.gui.utils.FileNameExtensionFilter;
import coda.util.RScriptEngine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.MenuBar;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import org.apache.batik.swing.JSVGCanvas;


public class RBasedGenericMenu extends AbstractMenuDialog{
  
    private int iVar = 0;
    private ArrayList<RConversion> cdp_lines = new ArrayList<RConversion>();
    public void addCDP_Line(RConversion cdpLine){
        CDP_Line cdpl = (CDP_Line)cdpLine;
        optionsPanel.add(cdpl);
        cdp_lines.add(cdpLine);
    }
    RBasedGenericMenu mainClass = null;
    ArrayList<JCheckBox> Barray = new ArrayList<JCheckBox>();
    RScriptEngine re;

    int PLOT_WIDTH = 650;
    int PLOT_HEIGHT = 400;

    String script_file;

    String analysisTitle = "";
    public RBasedGenericMenu(final CoDaPackMain mainApp, 
                             RScriptEngine r, 
                             String title,
                             String Rscript, 
                             JSONArray controls, 
                             DataSelector dataSelector) throws JSONException{
        super(mainApp, title + " Menu", dataSelector); 
        analysisTitle = title;
        // System.out.println("Controls: " + controls.toString());
        mainClass = this;
        build_optionPanel(r, Rscript, controls);
    }
    @Override
    public void acceptButtonActionPerformed(){
        re.eval("rm(list = ls())");

        iVar = 0;
        for(RConversion cdpLine: cdp_lines){
            cdpLine.addVariableToR();
        }
        df = mainApplication.getActiveDataFrame();
        String sel_names[] = super.ds.getSelectedData();
        
        if(sel_names.length > 0){
            if(ds.selection_type == DataSelector.ONLY_NUMERIC){
                double[][] dataX = df.getNumericalData(sel_names);
                addMatrixToR(dataX, sel_names, "X");
            }
            if(ds.selection_type == DataSelector.ONLY_CATEGORIC){
                String[][] dataX = df.getCategoricalData(sel_names);
                addMatrixToR(dataX, sel_names, "X");
            }
            double dlevel[][] = df.getDetectionLevel(sel_names);


            addMatrixToR(dlevel, sel_names, "DL");

            if(ds.group_by){
                String sel_group = ds.getSelectedGroup();
                if(sel_group != null){
                    String group[] = df.getCategoricalData(sel_group);
                    re.assign("GROUP", group);
                }
            }

        }
        if(ds instanceof DataSelector1to2){
            
            String sel_namesY[] = ((DataSelector1to2)ds).getSelectedDataB();
            if(sel_namesY.length > 0){
                double[][] dataY = df.getNumericalData(sel_namesY);
                addMatrixToR(dataY, sel_namesY, "Y");
            }
        }

        String SOURCE = "error = tryCatch(source('%s'), error = function(e) e$message)";
        SOURCE = SOURCE.formatted(Paths.get(CoDaPackConf.getRScriptDefaultPath(), this.script_file).toString().replace("\\","/"));
        System.out.println(SOURCE);
        re.eval(SOURCE);

        String errorMessage[] = re.eval("error").asStringArray();
        if(errorMessage != null){
            JOptionPane.showMessageDialog(this, "Error when reading R script file: %s".formatted(this.script_file));
            return;
        }
       

        re.eval("source('Rscripts/.cdp_helper_functions.R')");
        String error_in = re.eval("cdp_check()").asString();
        if(error_in != null){
            JOptionPane.showMessageDialog(this, error_in);
            return;
        }


        System.out.println("PLOT_WIDTH = %d/72".formatted(PLOT_WIDTH));
        System.out.println("PLOT_HEIGTH = %d/72".formatted(PLOT_HEIGHT));
        re.eval("PLOT_WIDTH = %d/72".formatted(PLOT_WIDTH));
        re.eval("PLOT_HEIGTH = %d/72".formatted(PLOT_HEIGHT));
        
        
        re.eval("error = tryCatch(cdp_res <- cdp_analysis(), error = function(e) e$message)");
        errorMessage = re.eval("error").asStringArray();
        if(errorMessage != null){
            JOptionPane.showMessageDialog(this, "Error when running the analysis: %s".formatted(error_in));
            return;
        }
        

        showText();
        createVariables();
        showGraphics();
        createDataFrame();


        setVisible(false);
        
    }

    public void addMatrixToR(double data[][], String col_names[], String name){
        String vnames[] = new String[data.length];
        for(int i=0; i < data.length; i++){
            vnames[i] = ".cdp_x"+i;
            re.assign(vnames[i], data[i]);
        }
        re.eval("%s = cbind(%s)".formatted(name, String.join(",", vnames)));

        String col_names_ext[] = new String[col_names.length];
        for(int i = 0; i < col_names.length; i++) col_names_ext[i] = "'" + col_names[i] + "'";
        re.eval("colnames(%s) = c(%s)".formatted(name , String.join(",", col_names_ext)));
        re.eval("%s[is.nan(%s)] = NA_real_".formatted(name, name));
    }
    public void addMatrixToR(String data[][], String col_names[], String name){
        String vnames[] = new String[data.length];
        for(int i=0; i < data.length; i++){
            vnames[i] = ".cdp_x"+i;
            re.assign(vnames[i], data[i]);
        }
        re.eval("%s = cbind(%s)".formatted(name, String.join(",", vnames)));

        String col_names_ext[] = new String[col_names.length];
        for(int i = 0; i < col_names.length; i++) col_names_ext[i] = "'" + col_names[i] + "'";
        re.eval("colnames(%s) = c(%s)".formatted(name , String.join(",", col_names_ext)));
        re.eval("%s[is.nan(%s)] = NA_real_".formatted(name, name));
    }
    void showText(){    
        String outputString[] = re.eval("unlist(cdp_res[['text']])").asStringArray();
        if(outputString.length > 1 | (outputString.length == 1 & outputString[0].compareTo("") != 0) ){
            CoDaPackMain.outputPanel.addOutput(new OutputText(analysisTitle));
            //System.out.println(Arrays.toString(outputString));
            CoDaPackMain.outputPanel.addOutput(new OutputForR(outputString));
        }
    }
    void createDataFrame(){
        int nDataFrames = re.eval("length(cdp_res$dataframe)").asInt();
        for(int i=0; i < nDataFrames; i++){
            int nVariables = re.eval("length(cdp_res$dataframe[[" + String.valueOf(i+1) + "]])").asInt();
            DataFrame newDataFrame = new DataFrame();
            for(int j=0; j < nVariables; j++){
                String varName = re.eval("names(cdp_res$dataframe[[" + String.valueOf(i+1) + "]][" + String.valueOf(j+1) + "])").asString();
                String isNumeric = re.eval("class(unlist(cdp_res$dataframe[[" + String.valueOf(i+1) + "]][" + String.valueOf(j+1) + "]))").asString();
                if(isNumeric.equals("numeric")){ /* crear una variable numerica */
                    double[] data = re.eval("as.numeric(unlist(cdp_res$dataframe[[" + String.valueOf(i+1) + "]][" + String.valueOf(j+1) + "]))").asDoubleArray();
                    newDataFrame.addData(varName, data);
                }
                else{ /* crear una variable categorica */
                    String[] data = re.eval("as.character(unlist(cdp_res$dataframe[[" + String.valueOf(i+1) + "]][" + String.valueOf(j+1) + "]))").asStringArray();
                    newDataFrame.addData(varName, new Variable(varName,data));
                }
            }
            
            String dataFrameName = re.eval("names(cdp_res$dataframe)[" + String.valueOf(i+1) + "]").asString();
            
            while(!mainApplication.isDataFrameNameAvailable(dataFrameName)){
                dataFrameName += "c";
            }
            
            newDataFrame.setName(dataFrameName);
            DataFrame current = mainApplication.getActiveDataFrame();
            mainApplication.addDataFrame(newDataFrame);
            mainApplication.updateDataFrame(current);
        }
    }

    void createVariables(){
        
        int numberOfNewVar = re.eval("length(cdp_res$new_data)").asInt(); /* numero de columnes nomes*/
        
        for(int i=0; i < numberOfNewVar; i++){
            String varName = re.eval("names(cdp_res$new_data)[" + String.valueOf(i+1) + "]").asString();
            String isNumeric = re.eval("as.character(is.numeric(cdp_res$new_data[["+ String.valueOf(i+1) +"]]))").asString();
            if(isNumeric.equals("TRUE")){
                double[] data = re.eval("as.numeric(cdp_res$new_data[[" + String.valueOf(i+1) + "]])").asDoubleArray();
                df.addData(varName,data);
            }
            else{ // categoric
                String[] data = re.eval("as.character(cdp_res$new_data[[" + String.valueOf(i+1) + "]])").asStringArray();
                df.addData(varName, new Variable(varName,data));
            }
            mainApplication.updateDataFrame(df);
        }
    }

    void showGraphics(){
        
        String fnames[] = re.eval("cdp_res$graph").asStringArray();
        String pnames[] = re.eval("names(cdp_res$graph)").asStringArray();
        System.out.println(Arrays.toString(fnames));
        System.out.println(Arrays.toString(pnames));
        if(fnames != null){
            RPlotWindow jf = new RPlotWindow(fnames, pnames);
                jf.setSize(PLOT_WIDTH, PLOT_HEIGHT);
                jf.setVisible(true);
        }
        /*
        for(int i=0; i < numberOfGraphics; i++){
            tempDirR = re.eval("cdp_res$graph[[" + String.valueOf(i+1) + "]]").asString();
            tempsDirR.add(tempDirR);
            plotZpatternsMenu(this.framesZpatternsMenu.size());
        }  
        */
    }
     void build_optionPanel(RScriptEngine r, String Rscript, JSONArray controls) throws JSONException{
        script_file = Rscript;

        // super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        re = r;
        
        this.optionsPanel.setLayout(new BoxLayout(this.optionsPanel, BoxLayout.PAGE_AXIS));
        optionsPanel.add(Box.createRigidArea(new Dimension(15,15)));
        for(int i=0;i<controls.length();i++){
            JSONObject json_obj = controls.getJSONObject(i);
            String type = (String) json_obj.sortedKeys().next();
            switch(type){
                case "options":
                    // System.out.println("options");
                    JSONObject obj = json_obj.getJSONObject(type);
                    String lbl = obj.getString("label");

                    JSONArray json_arr = obj.getJSONArray("options_controls");
                    String str1[] = new String[json_arr.length()];
                    JPanel jps[] = new JPanel[json_arr.length()];

                    for(int j=0; j < json_arr.length(); j++){
                        JSONObject o1 = json_arr.getJSONObject(j);
                        String l1 = (String) o1.sortedKeys().next();
                        str1[j] = l1;
                        jps[j] = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                        jps[j].setMaximumSize(new Dimension(1000, 32));
                        JSONArray sub_controls = obj.getJSONArray("options_controls").getJSONObject(j).getJSONArray(l1);
                        for(int i2=0;i2<sub_controls.length();i2++){
                            JSONObject json_obj2 = sub_controls.getJSONObject(i2);
                            String type2 = (String) json_obj2.sortedKeys().next();
                            switch(type2){
                                case "string":
                                    // System.out.println("string submenu");
                                    break;
                                case "numeric":
                                    JTextField T2 = new JTextField("", 5);
                                    JPanel PB3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                                    PB3.setMaximumSize(new Dimension(1000, 32));
                                    String label3 = json_obj2.getString(type2);        
                                    PB3.add(new JLabel(label3));  
                                    PB3.add(Box.createHorizontalStrut(10));
                                    PB3.add(T2);
                                    jps[j].add(PB3);
                                    // System.out.println("numeric submenu");
                                    break;
                                case "boolean":
                                    // System.out.println("boolean submenu");
                                    String label2 = null;
                                    boolean checked2 = false;
                                    if(json_obj2.get(type2) instanceof JSONObject){
                                        label2 = json_obj2.getJSONObject(type2).getString("label");
                                        checked2 = json_obj2.getJSONObject(type2).getBoolean("value");
                                    }else{
                                        label2 = json_obj.getString(type2);
                                    }
                                    JPanel BPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                                    BPanel2.setMaximumSize(new Dimension(1000, 32));
                                    JCheckBox B2 = new JCheckBox(label2);
                                    B2.setSelected(checked2);
                                    Barray.add(B2);
                                    BPanel2.add(B2);
                                    BPanel2.setAlignmentX(0);
                                    jps[j].add(BPanel2);
                                    break;
                            }
                        }
                        // System.out.println(json_arr.getJSONObject(j).toString());
                    }
                    JComboBox<String> jcb_options = new JComboBox<String>(str1);
                    JPanel PB = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                    PB.setMaximumSize(new Dimension(1000, 32));
                    PB.add(new JLabel(lbl));
                    //PB.add(Box.createHorizontalStrut(10));
                    PB.add(jcb_options);
                    optionsPanel.add(PB);
                    for(int j =0;j<jps.length;j++){
                        optionsPanel.add(jps[j]);
                    }

                    break;
                case "select":
                    // System.out.println("Menu select line");
                    String name = json_obj.getJSONObject(type).getString("name");
                    JSONArray values = json_obj.getJSONObject(type).getJSONArray("values");
                    String str_v[] = new String[values.length()];
                    for(int j =0;j<values.length();j++) str_v[j] = values.getString(j);
                    //System.out.println(Arrays.toString(str_v));
                    //System.out.println(json_obj.get(type).toString());
                    addCDP_Line(new CDP_Select(name, str_v));
                    break;
                case "string":
                    String str_label = null;
                    String str_value = "";
                    if(json_obj.get(type) instanceof JSONObject){
                        str_label = json_obj.getJSONObject(type).getString("name");
                        str_value = json_obj.getJSONObject(type).getString("value");
                    }else{
                        str_label = json_obj.getString(type);
                    }
                    addCDP_Line(new CDP_String(str_label, str_value));
                    break;
                case "label":
                    addCDP_Line(new CDP_Label(json_obj.getString(type)));                    
                    break;
                case "numeric":
                    String num_label = null;
                    String num_value = "";
                    if(json_obj.get(type) instanceof JSONObject){
                        num_label = json_obj.getJSONObject(type).getString("name");
                        num_value = json_obj.getJSONObject(type).getString("value");
                    }else{
                        num_label = json_obj.getString(type);
                    }
                    addCDP_Line(new CDP_Numeric(num_label, num_value));
                    break;
                case "basis":
                    // System.out.println("Basis menu");
                    addCDP_Line(new CDP_Basis(json_obj.getJSONObject(type).getString("name"), 
                                              json_obj.getJSONObject(type).getString("selector")));
                    // addCDP_Line(new CDP_Basis(json_obj.getJSONObject(type).getString("name")));
                    // panel.add(new JLabel("Defined partition:"));
                    // JScrollPane jScrollPane1 = new JScrollPane();
                    // jScrollPane1.setPreferredSize(new Dimension(185,150));
                    
                    // jScrollPane1.setViewportView(areaPart);
                    // panel.add(jScrollPane1);
                    break;
                case "boolean":
                    String label = null;
                    boolean checked = false;
                    if(json_obj.get(type) instanceof JSONObject){
                        label = json_obj.getJSONObject(type).getString("label");
                        checked = json_obj.getJSONObject(type).getBoolean("value");
                    }else{
                        label = json_obj.getString(type);
                    }
                    addCDP_Line(new CDP_Boolean(label, checked));
                    break;
            }
        }
        optionsPanel.add(new Box.Filler(new Dimension(0, 0), new Dimension(850, 500), new Dimension(1200, 1000)));
        /*
        JPanel B1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        B1Panel.setMaximumSize(new Dimension(1000, 32));
        B1Panel.add(B1);

        JPanel B2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        B2Panel.setMaximumSize(new Dimension(1000, 32));
        B2Panel.add(B2);

        JPanel B3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        B3Panel.setMaximumSize(new Dimension(1000, 32));
        B3Panel.add(B3);

        B1Panel.setAlignmentX(0);
        B2Panel.setAlignmentX(0);
        B3Panel.setAlignmentX(0);
        //pP1.setAlignmentX(0);

        this.optionsPanel.add(Box.createRigidArea(new Dimension(15,15)));
        this.optionsPanel.add(B1Panel);
        this.optionsPanel.add(B2Panel);
        this.optionsPanel.add(B3Panel);
        */

    }

    private class RPlotWindow extends javax.swing.JFrame{
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenuItem menuSaveSVG = new JMenuItem("Export SVG");
        
        
        //JMenuItem menuItem = new JMenuItem("Open");
            // framesScatterplotMenu.add(new JFrame());
            // JPanel panel = new JPanel();
            // menu.add(menuItem);
            // menuItem = new JMenuItem("Export");
            // JMenu submenuExport = new JMenu("Export");
            // menuItem = new JMenuItem("Export As SVG");
            // menuItem.addActionListener(new ScatterplotMenu.FileChooserAction(position));
            // submenuExport.add(menuItem);
            // menuItem = new JMenuItem("Export As JPEG");
            // //submenuExport.add(menuItem);
            // menuItem = new JMenuItem("Export As PDF");
            // //submenuExport.add(menuItem);
            // menuItem = new JMenuItem("Export As WMF");
            // //submenuExport.add(menuItem);
            // menuItem = new JMenuItem("Export As Postscripts");
            // //submenuExport.add(menuItem);
            // menuItem = new JMenuItem("Quit");

        public RPlotWindow(String[] fnames, String[] pnames){
            if(fnames.length == 1){
                String fname = fnames[0];
                JSVGCanvas c = new JSVGCanvas();
                String uri = new File(fname).toURI().toString();
                c.setURI(uri);
                getContentPane().add(c);
                if(pnames != null){
                    this.setTitle(pnames[0]);
                }else{
                    this.setTitle(analysisTitle);
                }
                
                menuSaveSVG.addActionListener((ActionEvent e) -> {
                    System.out.println("Button was clicked");
                    JFileChooser jf = new JFileChooser();
                    jf.setCurrentDirectory(new File(CoDaPackConf.workingDir));
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "SVG graphic", "svg");
                    jf.setFileFilter(filter);
                    int returnVal = jf.showSaveDialog(this);
                    if(returnVal == JFileChooser.APPROVE_OPTION){
                        File f = new File(fname);
                        String path = jf.getSelectedFile().getAbsolutePath();
                        File f2 = new File(path);
                        f.renameTo(f2);
                        CoDaPackConf.workingDir = jf.getCurrentDirectory().getAbsolutePath();
                    }
                });
            }else{
                JTabbedPane tabbedPane = new JTabbedPane();                
                for(int i=0;i<fnames.length;i++){
                    String pname = "plot " + i+1;
                    if(pnames.length == fnames.length){
                        pname = pnames[i];
                    }
                    String fname = fnames[i];
                    JSVGCanvas c = new JSVGCanvas();
                    String uri = new File(fname).toURI().toString();
                    c.setURI(uri);
                    getContentPane().add(c);
                    tabbedPane.addTab(pname, null, c);
                    getContentPane().add(tabbedPane);
                }
                menuSaveSVG.addActionListener((ActionEvent e) -> {
                    JFileChooser jf = new JFileChooser();
                    jf.setCurrentDirectory(new File(CoDaPackConf.workingDir));
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "SVG graphic", "svg");
                    jf.setFileFilter(filter);
                    int returnVal = jf.showSaveDialog(this);
                    if(returnVal == JFileChooser.APPROVE_OPTION){
                        int sel_ind = tabbedPane.getSelectedIndex();
                        File f = new File(fnames[sel_ind]);
                        String path = jf.getSelectedFile().getAbsolutePath();
                        File f2 = new File(path);
                        // f.renameTo(f2);
                        try {
                            Files.copy(f.toPath(), f2.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        CoDaPackConf.workingDir = jf.getCurrentDirectory().getAbsolutePath();
                    }
                });
            }            
            menuFile.add(menuSaveSVG);
            menuBar.add(menuFile);
            setJMenuBar(menuBar);
        }
        // public RPlotWindow(String[] fnames){
        //     JSVGCanvas c = new JSVGCanvas();
        //     for(fname in fnames)
        //     String uri = new File(fname).toURI().toString();
        //     c.setURI(uri);
        //     getContentPane().add(c);

        //     //
        //     menuSaveSVG.addActionListener((ActionEvent e) -> {
        //         System.out.println("Button was clicked");
        //         JFileChooser jf = new JFileChooser();
        //         jf.setCurrentDirectory(new File(CoDaPackConf.workingDir));
        //         FileNameExtensionFilter filter = new FileNameExtensionFilter(
        //             "SVG graphic", "svg");
        //         jf.setFileFilter(filter);
        //         int returnVal = jf.showSaveDialog(this);
        //         if(returnVal == JFileChooser.APPROVE_OPTION){
        //             File f = new File(fname);
        //             String path = jf.getSelectedFile().getAbsolutePath();
        //             File f2 = new File(path);
        //             f.renameTo(f2);
        //             CoDaPackConf.workingDir = jf.getCurrentDirectory().getAbsolutePath();
        //         }
        //     });
        //     menuFile.add(menuSaveSVG);
        //     menuBar.add(menuFile);
        //     setJMenuBar(menuBar);
        // }

    }

    interface RConversion{
        public boolean addVariableToR();
    }

    private abstract class CDP_Line extends JPanel{
        static int MAX_SIZE = 300;
        public CDP_Line(){            
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            setMaximumSize(new Dimension(MAX_SIZE, 35));
            //setBackground(new Color(100,100,100));
            //add(Box.createRigidArea(new Dimension(MAX_SIZE, 5)));
            add(Box.createHorizontalStrut(10));
            
        }
        public CDP_Line(int VSIZE){            
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            setMaximumSize(new Dimension(MAX_SIZE, VSIZE));
            //setBackground(new Color(100,100,100));
            //add(Box.createRigidArea(new Dimension(MAX_SIZE, 5)));
            add(Box.createHorizontalStrut(10));
            
        }
    }
    private class CDP_Label extends CDP_Line implements RConversion{        
        public CDP_Label(String label){  
            JPanel Plab = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                        
            JLabel L  = new JLabel(label);                    
            Plab.add(L);
            add(Plab);
        }

        @Override
        public boolean addVariableToR(){
            System.out.println("# Nothing to include");
            return(true);
        }
    }
    private class CDP_Select extends CDP_Line implements RConversion{   
        JComboBox<String> cb;
        public CDP_Select(String label, String values[]){  
            JPanel Plab = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            JPanel Pcb = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

            cb = new JComboBox<String>(values);
            
            Plab.add(new JLabel(label));
            Pcb.add(cb);
            add(Plab);  
            add(Box.createHorizontalStrut(5));
            add(Pcb);
        }

        @Override
        public boolean addVariableToR() {
            String V = (String) cb.getSelectedItem();

            String EXP = "V%d = '%s'".formatted(++iVar, V);
            System.out.println(EXP);
            re.eval(EXP);
            return(true);
        }
    }
    private class CDP_Numeric extends CDP_Line implements RConversion{
        JTextField Tnum;
        public CDP_Numeric(String vname, String value){
            JPanel Plab = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            JPanel Pnum = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

            Tnum = new JTextField(value, 7);
            Plab.add(new JLabel(vname));
            Pnum.add(Tnum);
            add(Plab);  
            add(Box.createHorizontalStrut(5));
            add(Pnum);
            //add(new Box.Filler(new Dimension(0, 35), new Dimension(MAX_SIZE, 35), new Dimension(MAX_SIZE, 35)));
        }
        @Override
        public boolean addVariableToR() {
            String V = Tnum.getText();

            String EXP = "V%d = as.numeric(%s)".formatted(++iVar, V);
            System.out.println(EXP);
            re.eval(EXP);
            return(true);
        }

    }
    private class CDP_String extends CDP_Line implements RConversion{
        JTextField Tstr;
        public CDP_String(String vname, String value){
            JPanel Plab = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            JPanel Pstr = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

            Tstr = new JTextField(value, 7);
            Plab.add(new JLabel(vname));
            Pstr.add(Tstr);
            add(Plab);  
            add(Box.createHorizontalStrut(5));
            add(Pstr);
            //add(new Box.Filler(new Dimension(0, 35), new Dimension(MAX_SIZE, 35), new Dimension(MAX_SIZE, 35)));
        }
        @Override
        public boolean addVariableToR() {
            String V = Tstr.getText();

            String EXP = "V%d = '%s'".formatted(++iVar, V);
            System.out.println(EXP);
            re.eval(EXP);
            return(true);
        }

    }
    private class CDP_Boolean extends CDP_Line implements RConversion{
        JCheckBox Bbool;
        public CDP_Boolean(String vname, boolean checked){
            JPanel Pbool = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            Bbool = new JCheckBox(vname);
            Bbool.setSelected(checked);
            Pbool.add(Bbool);
            add(Pbool);

        }
        @Override
        public boolean addVariableToR() {
            String V = "FALSE";
            if(Bbool.isSelected()) V = "TRUE";

            String EXP = "V%d = %s".formatted(++iVar, V);
            System.out.println(EXP);
            re.eval(EXP);
            return(true);
        }
    }
    private class CDP_Basis extends CDP_Line implements RConversion{
        JTextArea basis;
        String selector;
        public CDP_Basis(String vname, String selector_){
            super(100);
            selector = selector_;
            JPanel PBasis = new JPanel();
            // PBasis.setBackground(Color.CYAN);
            PBasis.setLayout(new BoxLayout(PBasis, BoxLayout.PAGE_AXIS));
            // Plab.add(new JLabel(vname));

            basis = new JTextArea(20, 20);

            JScrollPane sp = new JScrollPane();
            sp.setSize(new java.awt.Dimension(185,40));
            sp.setMaximumSize(new Dimension(MAX_SIZE, 40));
            sp.setViewportBorder(BorderFactory.createLineBorder(Color.black));
            sp.setViewportView(basis);
            
            JLabel lbl1 = new JLabel(vname);
            JPanel p1 = new JPanel();
            p1.setOpaque(false);
            p1.setLayout(new BorderLayout(0, 0));
            p1.add(lbl1, BorderLayout.CENTER);

            // JLabel l = new JLabel(vname, SwingConstants.LEFT);
            // l.setOpaque(true);
            // l.setBackground(Color.MAGENTA);
            // l.setAlignmentX();
            // l.setHorizontalAlignment(0);
            PBasis.add(p1);
            PBasis.add(sp);

            JButton b1 = new JButton("Default");
            JButton b2 = new JButton("Manual");
            b1.addActionListener(new java.awt.event.ActionListener() {
                
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    String sel_names[] = null;
                    if(selector.equals("X")){
                        sel_names = ((DataSelector1to2)ds).getSelectedDataA();
                    }
                    if(selector.equals("Y")){
                        sel_names = ((DataSelector1to2)ds).getSelectedDataB();
                    }
                    
                    int partition[][] = CoDaStats.defaultPartition(sel_names.length);
                    String spart = "";
                    for(short i=0;i<partition.length;i++){
                        for(short j=0;j<partition[0].length;j++){
                            if(partition[i][j] == -1) spart += " " + partition[i][j];
                            else spart += "  " + partition[i][j];
                        }
                        spart += "\n";
                    }
                    basis.setText(spart);
                }
            });

            b2.addActionListener(new java.awt.event.ActionListener() {
                
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    // System.out.println("Selector: " + selector);
                    String sel_names[] = null;
                    if(selector.equals("X")){
                        sel_names = ((DataSelector1to2)ds).getSelectedDataA();
                    }
                    if(selector.equals("Y")){
                        sel_names = ((DataSelector1to2)ds).getSelectedDataB();
                    }
                    System.out.println(Arrays.toString(sel_names));
                    BuildSBP binaryMenu = new BuildSBP(mainClass, sel_names);
                    binaryMenu.setVisible(true);    
                }
            });

            JPanel p3 = new JPanel();
            p3.add(b1);
            p3.add(b2);

            PBasis.add(p3);
            PBasis.add(new Box.Filler(new Dimension(0, 0), new Dimension(MAX_SIZE, 120), new Dimension(MAX_SIZE, 120)));
            
            add(PBasis);
            
        }
        @Override
        public boolean addVariableToR() {
            String str_basis = basis.getText();

            String EXP1 = "Basis%s = scan(text = '%s', quiet=TRUE)".formatted(selector, str_basis.replace("\n", "").replace("\r", "")); 
            String EXP2 = "nr = (1+sqrt(1+4*length(Basis%s)))/2".formatted(selector);
            String EXP3 = "dim(Basis%s) = c(nr,nr-1)".formatted(selector);
            System.out.println(EXP1);
            System.out.println(EXP2);
            System.out.println(EXP3);
            re.eval(EXP1);
            re.eval(EXP2);
            re.eval(EXP3);
            return(true);
        }
        public class BuildSBP extends JDialog{
            public static final long serialVersionUID = 1L;
            String variables[];
        
            int order[];
            int nvariables;
            int partition[][];
            boolean activeVars[];
        
            static public String Gr0 = " ";
            static public String Gr1 = "-";
            static public String Gr2 = "+";
        
            static public int VGr0 = 0;
            static public int VGr1 = 1;
            static public int VGr2 = 2;
        
            BinaryPartitionTable partitionTable = null;
            BinaryPartitionRowHeaders rowsTable = null;
            
            JButton previous;
            JButton next;
            
            AbstractMenuDialogWithILR rootMenu = null;
            ILRMenu rootILRMenu = null;
            AbstractMenuGeneral rootGeneralMenu = null;
            
            public BuildSBP(RBasedGenericMenu dialogRoot, String vars[]){
                super(dialogRoot, "Binary Partition Menu");
        
                Point p = dialogRoot.getLocation();
                p.x = p.x + (dialogRoot.getWidth()-20)/2;
                p.y = p.y + (dialogRoot.getHeight()-60)/2;
                setLocation(p);
        
                variables = vars;
                nvariables = variables.length;
                partition = new int[nvariables-1][nvariables];
                order = new int[nvariables];
                activeVars = new boolean[nvariables];
                for(int i=0;i<nvariables;i++){
                    activeVars[i] = true;
                    order[i] = i;
                }
                //int height = partitionTable.getRowHeight();
                //int width = partitionTable.getWidth();
        
                int heightRow = 20;
                int widthCol = 40;
        
                setSize( widthCol *(nvariables-1)+158+5,heightRow*nvariables+100);
                getContentPane().setLayout(new BorderLayout());
        
                
        
                partitionTable = new BinaryPartitionTable(rowsTable, order, activeVars, nvariables);
                rowsTable = new BinaryPartitionRowHeaders(partitionTable, order, variables, heightRow);        
        
                partitionTable.setRowHeight(heightRow);
                for(int vColIndex=0;vColIndex<nvariables-1;vColIndex++){
                    partitionTable.getColumnModel().
                            getColumn(vColIndex).setPreferredWidth(widthCol);
                }
                partitionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                partitionTable.setEnabled(false);
        
                JScrollPane scrollPane = new JScrollPane(partitionTable);
                scrollPane.setRowHeaderView(rowsTable);
                scrollPane.setCorner(
                        JScrollPane.UPPER_LEFT_CORNER, rowsTable.getTableHeader());
                getContentPane().add(scrollPane, BorderLayout.CENTER);
        
                // Defining control buttons in South Area
                JPanel south = new JPanel();
                south.setLayout(new GridLayout(1,2));
                JPanel panel1 = new JPanel();
        
                previous = new JButton("Previous");
                previous.addActionListener(new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        previousButtonActionPerformed(evt);
                    }
                });
                next = new JButton("Next");
                next.addActionListener(new java.awt.event.ActionListener() {
                    
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        nextButtonActionPerformed(evt);
                    }
                });
                
                panel1.add(previous);
                panel1.add(next);
        
        
        
                south.add(panel1);
                getContentPane().add(south, BorderLayout.SOUTH);
            }
        
            void previousButtonActionPerformed(ActionEvent evt){
                int actualStep = partitionTable.getActualStep();
                if(actualStep +1 == nvariables){
                    next.setText("Next");
                }
                if(actualStep > 1){
                    int actualCol = actualStep - 1;
        
                    // Getting the values from the JTable
                    for(int row=0;row<nvariables;row++){
                        partition[actualCol-1][row] = VGr0;
                    }
                    // Calculating the ordenation
                    for(int i=0;i<nvariables;i++) order[i] = i;
                    for(int i=0;i<nvariables;i++){
                        for(int j=i+1; j <nvariables;j++){
                            boolean sameGroup = true;
                            for(int k=0; k<actualCol; k++){
                                if(partition[k][order[i]] != partition[k][order[j]]){
                                    if( partition[k][order[i]] < partition[k][order[j]] ){
                                        int temp = order[i];
                                        order[i] = order[j];
                                        order[j] = temp;
                                    }
                                    sameGroup = false;
                                    break;
                                }
                            }
                            if(sameGroup){
                                if(order[i] > order[j]){
                                    int temp = order[i];
                                    order[i] = order[j];
                                    order[j] = temp;
                                }
                            }
                        }
                    }
                    int count = 0;
                    activeVars[order[0]] = true;
                    for(int i=1; i<nvariables;i++){
                        boolean equal = true;
                        for(int k=actualCol-1; k>=0 && equal; k--)
                            if( partition[k][order[i]] != partition[k][order[i-1]] )
                                equal = false;
                        // If variables are in the same group the program continue
                        if( equal ){
                            activeVars[order[i]] = true;
                            count++;
                        }else{
                            if(count > 0){
                                for(;i<nvariables;i++) activeVars[order[i]] = false;
                            }else{
                                activeVars[order[i-1]] = false;
                                activeVars[order[i]] = true;
                            }
                        }
                    }
        
                    partitionTable.setPreviousStep(partition);
                    partitionTable.updateUI();
                }
            }
            void nextButtonActionPerformed(ActionEvent evt){
                int actualStep = partitionTable.getActualStep();
                int actualCol = actualStep - 1;
        
                // Checking if its the last step
                if(next.getText().compareTo("Accept") == 0){
        
                    // Modifying 2 to 1 and 1 to -1
                    for(int i=0;i+1<nvariables;i++)
                        for(int j=0;j<nvariables;j++)
                            partition[i][j] = (partition[i][j] == 1 ?
                                -1 : (partition[i][j] == 2 ?
                                    1 : 0) );
                    // Sending a copy of the partition matrix to the Menu and closing
                    // the frame.
                    //if(rootMenu != null)rootMenu.setPartition(partition);
                    //if(rootILRMenu != null) rootILRMenu.setPartition(partition);
                    String spart = "";
                    for(short i=0;i<partition.length;i++){
                        for(short j=0;j<partition[0].length;j++){
                            if(partition[i][j] == -1) spart += " " + partition[i][j];
                            else spart += "  " + partition[i][j];
                        }
                        spart += "\n";
        }
                    basis.setText(spart);
                    //if(rootGeneralMenu != null) rootGeneralMenu.setPartition(partition);
                    setVisible(false);
                    dispose();
                    return;
                }
                // Getting the values from the JTable
                TableModel model = partitionTable.getModel();
                for(int row=0;row<nvariables;row++){
                    String value = (String)model.getValueAt(row, actualCol);
                    if(value == null || value.compareTo(BinaryPartitionSelect.Gr0) == 0){
                        partition[actualCol][order[row]] = VGr0;
                    }else if(value.compareTo(BinaryPartitionSelect.Gr1) == 0){
                        partition[actualCol][order[row]] = VGr1;
                    }else if(value.compareTo(BinaryPartitionSelect.Gr2) == 0){
                        partition[actualCol][order[row]] = VGr2;
                    }
                }
                // Checking if a partition has been defined from the values we've got.
                boolean equal = true;
                int val = VGr0;
                for(int i=0;i<nvariables && equal;i++)
                    if(activeVars[order[i]])
                        if(val == VGr0) val = partition[actualCol][order[i]];
                        else if( val != partition[actualCol][order[i]]){
                            equal = false;
                        }
                if(equal){
                    JOptionPane.showMessageDialog(this, "A partition must be defined.");
                    return;
                }
                // From this point we assume the user need to define another
                // subpartition
                for(int i=0;i<nvariables;i++) order[i] = i;
                for(int i=0;i<nvariables;i++){
                    for(int j=i+1; j <nvariables;j++){
                        boolean sameGroup = true;
                        for(int k=0; k<=actualCol; k++){
                            if(partition[k][order[i]] != partition[k][order[j]]){
                                if( partition[k][order[i]] < partition[k][order[j]] ){
                                    int temp = order[i];
                                    order[i] = order[j];
                                    order[j] = temp;
                                }
                                sameGroup = false;
                                break;
                            }
                        }
                        if(sameGroup){
                            if(order[i] > order[j]){
                                int temp = order[i];
                                order[i] = order[j];
                                order[j] = temp;
                            }
                        }
                    }
                }
                int count = 0;
                activeVars[order[0]] = true;
                for(int i=1; i<nvariables;i++){
                    equal = true;
                    for(int k=actualCol; k>=0 && equal; k--)
                        if( partition[k][order[i]] != partition[k][order[i-1]] )
                            equal = false;
                    // If variables are in the same group the program continue
                    if( equal ){
                        activeVars[order[i]] = true;
                        count++;
                    }else{
                        if(count > 0){
                            for(;i<nvariables;i++) activeVars[order[i]] = false;
                        }else{
                            activeVars[order[i-1]] = false;
                            activeVars[order[i]] = true;
                        }
                    }
                }
                for(int row=0;row<nvariables;row++){
                    for(int col=0;col<nvariables-1;col++){
                        if(activeVars[row]){
                            if(partition[col][order[row]] == VGr0){
                                model.setValueAt(BinaryPartitionSelect.Gr0, row, col);
                            }
                            if(partition[col][order[row]] == VGr1){
                                model.setValueAt(BinaryPartitionSelect.Gr1, row, col);
                            }
                            if(partition[col][order[row]] == VGr2){
                                model.setValueAt(BinaryPartitionSelect.Gr2, row, col);
                            }
                        }else{
                            model.setValueAt(BinaryPartitionSelect.Gr0, row, col);
                        }
                    }
                }
                partitionTable.setNextStep(partition);
                partitionTable.updateUI();
                partitionTable.setDefaultRenderer(Object.class, partitionTable.cellRend );
        
                if(actualStep + 1 == nvariables){
                    next.setText("Accept");
                }
            }
        }
    }

    
}
    
    
    
