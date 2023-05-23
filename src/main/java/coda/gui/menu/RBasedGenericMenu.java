package coda.gui.menu;

import coda.BasicStats;
import coda.DataFrame;
import coda.Variable;
import coda.ext.json.JSONArray;
import coda.ext.json.JSONException;
import coda.ext.json.JSONObject;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import static coda.gui.CoDaPackMain.outputPanel;
import coda.gui.output.OutputForR;
import coda.gui.output.OutputText;
import coda.gui.utils.DataSelector1to1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import org.apache.batik.swing.JSVGCanvas;


public class RBasedGenericMenu extends AbstractMenuRBasedDialog{
    private static final String yamlUrl = null;
    private static final String helpTitle = null;
    Rengine re;
    
    // JCheckBox B1 = new JCheckBox("Include means");
    // JCheckBox B2 = new JCheckBox("Include %");
    // JCheckBox B3 = new JCheckBox("Add pattern", true);
    
    // It should be common to all R calling AbstractRBasedMenuDialog
    
    ArrayList<JCheckBox> Barray = new ArrayList<JCheckBox>();
    public RBasedGenericMenu(final CoDaPackMain mainApp, 
                             Rengine r, 
                             String title,
                             String Rscript, 
                             JSONArray controls) throws JSONException{
        super(mainApp, title + " Menu", new DataSelector1to1(mainApp.getActiveDataFrame(), false), r);
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
                    optionsPanel.add(new CDP_Select(name, str_v));
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
                    optionsPanel.add(new CDP_String(str_label, str_value));
                    break;
                case "label":
                    optionsPanel.add(new CDP_Label(json_obj.getString(type)));                    
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
                    optionsPanel.add(new CDP_Numeric(num_label, num_value));
                    // JTextField Tnum = new JTextField("", 5);
                    // JPanel Pnum = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                    // Pnum.setMaximumSize(new Dimension(1000, 32));
                    // String Lnum = json_obj.getString(type);        
                    // Pnum.add(new JLabel(Lnum));  
                    // Pnum.add(Box.createHorizontalStrut(10));
                    // Pnum.add(Tnum);
                    // optionsPanel.add(Pnum);
                    // System.out.println("numeric submenu");
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
                    optionsPanel.add(new CDP_Boolean(label, checked));
                    break;
            }
        }
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
    private class CDP_Line extends JPanel{
        static int MAX_SIZE = 300;
        String R_expression_value;
        public CDP_Line(){            
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            setMaximumSize(new Dimension(MAX_SIZE, 35));
            //setBackground(new Color(100,100,100));
            //add(Box.createRigidArea(new Dimension(MAX_SIZE, 5)));
            add(Box.createHorizontalStrut(10));
            
        }
    }
    private class CDP_Label extends CDP_Line{        
        public CDP_Label(String label){  
            JPanel Plab = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                        
            JLabel L  = new JLabel(label);                    
            Plab.add(L);
            add(Plab);
        }
    }
    private class CDP_Select extends CDP_Line{        
        public CDP_Select(String label, String values[]){  
            JPanel Plab = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            JPanel Pcb = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

            JComboBox cb = new JComboBox(values);
            
            Plab.add(new JLabel(label));
            Pcb.add(cb);
            add(Plab);  
            add(Box.createHorizontalStrut(5));
            add(Pcb);
        }
    }
    private class CDP_Numeric extends CDP_Line{

        public CDP_Numeric(String vname){
            JPanel Plab = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            JPanel Pnum = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

            JTextField Tnum = new JTextField("", 7);
            Plab.add(new JLabel(vname));
            Pnum.add(Tnum);
            add(Plab);  
            add(Box.createHorizontalStrut(5));
            add(Pnum);
            //add(new Box.Filler(new Dimension(0, 35), new Dimension(MAX_SIZE, 35), new Dimension(MAX_SIZE, 35)));
        }
        public CDP_Numeric(String vname, String value){
            JPanel Plab = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            JPanel Pnum = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

            JTextField Tnum = new JTextField(value, 7);
            Plab.add(new JLabel(vname));
            Pnum.add(Tnum);
            add(Plab);  
            add(Box.createHorizontalStrut(5));
            add(Pnum);
            //add(new Box.Filler(new Dimension(0, 35), new Dimension(MAX_SIZE, 35), new Dimension(MAX_SIZE, 35)));
        }

    }
    private class CDP_String extends CDP_Line{

        public CDP_String(String vname){
            JPanel Plab = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            JPanel Pnum = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

            JTextField Tnum = new JTextField("", 7);
            Plab.add(new JLabel(vname));
            Pnum.add(Tnum);
            add(Plab);  
            add(Box.createHorizontalStrut(5));
            add(Pnum);
            //add(new Box.Filler(new Dimension(0, 35), new Dimension(MAX_SIZE, 35), new Dimension(MAX_SIZE, 35)));
        }
        public CDP_String(String vname, String value){
            JPanel Plab = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            JPanel Pnum = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

            JTextField Tstr = new JTextField(value, 7);
            Plab.add(new JLabel(vname));
            Pnum.add(Tstr);
            add(Plab);  
            add(Box.createHorizontalStrut(5));
            add(Pnum);
            //add(new Box.Filler(new Dimension(0, 35), new Dimension(MAX_SIZE, 35), new Dimension(MAX_SIZE, 35)));
        }

    }
    private class CDP_Boolean extends CDP_Line{
        public CDP_Boolean(String vname, boolean checked){
            JPanel Pbool = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            JCheckBox Bbool = new JCheckBox(vname);
            Bbool.setSelected(checked);
            Pbool.add(Bbool);
            add(Pbool);

        }
    }
    @Override
    public void acceptButtonActionPerformed(){
        
        df = mainApplication.getActiveDataFrame();
        String sel_names[] = super.ds.getSelectedData();

        double[][] data = df.getNumericalData(sel_names);
        for(int i=0; i < data.length; i++){
            re.assign(sel_names[i], data[i]);
        }
        re.eval("X = cbind(" + String.join(",", sel_names) + ")");
        re.eval("X[is.nan(X)] = NA_real_");

        for(int i = 0; i < Barray.size(); i++){
            JCheckBox B = Barray.get(i);
            re.eval("B%d = %s".formatted(i+1, B.isSelected() ? "TRUE": "FALSE"));
        }

        re.eval("PLOT_WIDTH = %d/72".formatted(PLOT_WIDTH));
        re.eval("PLOT_HEIGTH = %d/72".formatted(PLOT_HEIGHT));

        captureROutput();

        setVisible(false);
        
    }
}
    
    
    
