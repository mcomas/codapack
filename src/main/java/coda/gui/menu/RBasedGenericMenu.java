package coda.gui.menu;

import coda.BasicStats;
import coda.ext.json.JSONArray;
import coda.ext.json.JSONException;
import coda.ext.json.JSONObject;
import coda.gui.CoDaPackMain;
import coda.gui.utils.DataSelector;
import coda.gui.utils.DataSelector1to1;
import coda.gui.utils.DataSelector1to2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.rosuda.JRI.Rengine;
import org.rosuda.REngine.REXPNull;


public class RBasedGenericMenu extends AbstractMenuRBasedDialog{
    private static final String yamlUrl = null;
    private static final String helpTitle = null;
    private Rengine re;
    private int iVar = 0;
    private ArrayList<RConversion> cdp_lines = new ArrayList<RConversion>();
    public void addCDP_Line(RConversion cdpLine){
        CDP_Line cdpl = (CDP_Line)cdpLine;
        optionsPanel.add(cdpl);
        cdp_lines.add(cdpLine);
    }
    
    ArrayList<JCheckBox> Barray = new ArrayList<JCheckBox>();
    public RBasedGenericMenu(final CoDaPackMain mainApp, 
                             Rengine r, 
                             String title,
                             String Rscript, 
                             JSONArray controls, 
                             DataSelector dataSelector) throws JSONException{
        super(mainApp, title + " Menu", dataSelector, r); 
        build_optionPanel(r, Rscript, controls);
     }

     void build_optionPanel(Rengine r, String Rscript, JSONArray controls) throws JSONException{
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
                    System.out.println("options");
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
                                    System.out.println("string submenu");
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
                                    System.out.println("numeric submenu");
                                    break;
                                case "boolean":
                                    System.out.println("boolean submenu");
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
                        System.out.println(json_arr.getJSONObject(j).toString());
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
                    System.out.println("Menu select line");
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
                    System.out.println("Basis menu");
                    addCDP_Line(new CDP_Basis(json_obj.getJSONObject(type).getString("name")));
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
        public CDP_Basis(String vname){
            super(100);
            
            JPanel PBasis = new JPanel();
            PBasis.setBackground(Color.CYAN);
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
            JPanel p3 = new JPanel();
            p3.add(b1);
            p3.add(b2);

            PBasis.add(p3);
            PBasis.add(new Box.Filler(new Dimension(0, 0), new Dimension(MAX_SIZE, 120), new Dimension(MAX_SIZE, 120)));
            
            add(PBasis);
            
        }
        @Override
        public boolean addVariableToR() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'addVariableToR'");
        }
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

        double[][] dataX = df.getNumericalData(sel_names);
        addMatrixToR(dataX, sel_names, "X");

        if(ds instanceof DataSelector1to2){
            
            String sel_namesY[] = ((DataSelector1to2)ds).getSelectedDataB();
            double[][] dataY = df.getNumericalData(sel_namesY);
            addMatrixToR(dataY, sel_namesY, "Y");
        }
        // for(int i=0; i < data.length; i++){
        //     re.assign(sel_names[i], data[i]);
        // }
        // re.eval("X = cbind(" + String.join(",", sel_names) + ")");
        // re.eval("X[is.nan(X)] = NA_real_");

        double dlevel[][] = df.getDetectionLevel(sel_names);
        addMatrixToR(dlevel, sel_names, "DL");
        // for(int i=0; i < data.length; i++){
        //     re.assign(sel_names[i], dlevel[i]);
        // }
        // re.eval("DL = cbind(" + String.join(",", sel_names) + ")");
        if(ds.group_by){
            String sel_group = ds.getSelectedGroup();
            if(sel_group != null){
                String group[] = df.getCategoricalData(sel_group);
                re.assign("GROUP", group);
            }
        }
        
        re.eval("PLOT_WIDTH = %d/72".formatted(PLOT_WIDTH));
        re.eval("PLOT_HEIGTH = %d/72".formatted(PLOT_HEIGHT));

        captureROutput();

        setVisible(false);
        
    }
}
    
    
    
