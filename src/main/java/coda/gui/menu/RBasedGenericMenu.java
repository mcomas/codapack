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
                case "string":
                    System.out.println("string");
                    break;
                case "numeric":
                    System.out.println("numeric");
                    break;
                case "boolean":
                    System.out.println("boolean");
                    String label = null;
                    boolean checked = false;
                    if(json_obj.get(type) instanceof JSONObject){
                        label = json_obj.getJSONObject(type).getString("label");
                        checked = json_obj.getJSONObject(type).getBoolean("value");
                    }else{
                        label = json_obj.getString(type);
                    }
                    JPanel BPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                    BPanel.setMaximumSize(new Dimension(1000, 32));
                    JCheckBox B = new JCheckBox(label);
                    B.setSelected(checked);
                    Barray.add(B);
                    BPanel.add(B);
                    BPanel.setAlignmentX(0);
                    optionsPanel.add(BPanel);
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
    
    
    
