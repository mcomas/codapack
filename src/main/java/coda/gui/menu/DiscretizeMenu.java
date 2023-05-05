/** 
 *  Copyright 2011-2016 Marc Comas - Santiago Thió
 *
 *  This file is part of CoDaPack.
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.Variable;
import coda.gui.CoDaPackMain;
import coda.gui.CoDaPackConf;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author Guest2
 */
public class DiscretizeMenu extends AbstractMenuDialog{
    
    Rengine re;
    JPanel panel;
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Data.Manipulte.Discretize-Segment variable.yaml";
    private static final String helpTitle = "Discretize/Segment Help Menu";
    
    /** options var **/
    
    String[] options = {"interval", "frequency", "cluster","fixed"};
    JComboBox optionsList = new JComboBox(options);
    JLabel methodLabel = new JLabel("Method :");
    JLabel breaksLabel = new JLabel("# levels: ");
    JLabel optionToPutNames = new JLabel("Add group names: ");
    JCheckBox namesOptionCheckBox;
    JTextField breaksField = new JTextField(5);
    DataFrame df;
    ArrayList<String> names;
    
    public DiscretizeMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Discretize/Segment Menu", false);
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        re = r;
        
        optionsPanel.add(methodLabel);
        optionsPanel.add(optionsList);
        optionsPanel.add(breaksLabel);
        breaksField.setText("3");
        optionsPanel.add(breaksField);
        optionsPanel.add(optionToPutNames);
        namesOptionCheckBox = new JCheckBox();
        namesOptionCheckBox.setSelected(false);
        optionsPanel.add(namesOptionCheckBox);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        df = mainApplication.getActiveDataFrame();
        
        String[] sel_names = ds.getSelectedData();
        
        if(sel_names.length == 1){
            
            this.dispose();
            
            re.assign("x", df.get(sel_names[0]).getNumericalData());
            String EXP = "res <- cut(x, breaks=#BREAKS#, include.lowest = TRUE)";

            if(optionsList.getSelectedItem().toString().equals("fixed")){
                panel = new JPanel();
                panel.setLayout(new GridLayout(0,2,2,2));
                JTextField[] breaksFieldPanel = new JTextField[Integer.valueOf(breaksField.getText())-1];
                JLabel[] breaksNamesLabel = new JLabel[Integer.valueOf(breaksField.getText())-1];
                for(int i = 0; i < Integer.valueOf(breaksField.getText())-1;i++){
                    breaksFieldPanel[i] = new JTextField(20);
                    breaksNamesLabel[i] = new JLabel("Put the break value" + String.valueOf(i+1) + " :");
                    panel.add(breaksNamesLabel[i]);
                    panel.add(breaksFieldPanel[i]);
                }
                
                String[] breaksValues = new String[Integer.valueOf(breaksField.getText())-1];
                
                if(Integer.valueOf(breaksField.getText()) > 1){

                    boolean exit = false;

                    while(!exit){

                        exit = true;

                        int answer = JOptionPane.showConfirmDialog(this, panel, "Set breaks values", JOptionPane.OK_CANCEL_OPTION);

                        if(answer == JOptionPane.OK_OPTION){
                            for(int i = 0; i < Integer.valueOf(breaksField.getText())-1 && exit;i++){
                                breaksValues[i] = breaksFieldPanel[i].getText();
                                if(breaksFieldPanel[i].getText().length() == 0) exit = false;
                            }
                            if(!exit) JOptionPane.showMessageDialog(null,"Some field is empty");
                        }
                    }
                }
                EXP = EXP.replace("#BREAKS#", "sort(unique(c(min(x)," + String.join(",", breaksValues) + ", max(x))))");
            }                
            if(optionsList.getSelectedItem().toString().equals("interval")){
                EXP = EXP.replace("#BREAKS#", "seq(min(x), max(x), length = 1 + #NINT#)");
            }
            if(optionsList.getSelectedItem().toString().equals("frequency")){
                EXP = EXP.replace("#BREAKS#", "quantile(x, seq(0,1,length=1 + #NINT#))");
            }
            if(optionsList.getSelectedItem().toString().equals("cluster")){
                EXP = EXP.replace("#BREAKS#", "{cl = sort(kmeans(x,#NINT#,nstart=100)$centers[,]); c(min(x), cl[-1] - diff(cl)/2, max(x))}");
            }
            System.out.println(EXP.replace("#NINT#", breaksField.getText()));
            re.eval(EXP.replace("#NINT#", breaksField.getText()));

            
            double[] res = re.eval("as.numeric(res)").asDoubleArray();
            
            String[] resString = new String[df.get(sel_names[0]).size()]; /* creem una variable per guardar els noms */
            String[] resIntervals = re.eval("as.character(res)").asStringArray();
            for(int i=0; i < resIntervals.length;i++) resString[i] = resIntervals[i];
            
            if(namesOptionCheckBox.isSelected()){
                String[] names = new String[Integer.valueOf(breaksField.getText())]; /* array to save the names */
                
                panel = new JPanel();
                panel.setLayout(new GridLayout(0,2,2,2));
                JTextField[] namesField = new JTextField[Integer.valueOf(breaksField.getText())]; /* textFields to read the names */
                JLabel[] namesLabel = new JLabel[Integer.valueOf(breaksField.getText())]; /* labels for the names */
                
                for(int i=0; i < Integer.valueOf(breaksField.getText()); i++){
                    namesField[i] = new JTextField(20);
                    namesLabel[i] = new JLabel("Enter group name " + String.valueOf(i+1) + " :");
                    panel.add(namesLabel[i]);
                    panel.add(namesField[i]);
                }
                
                boolean exit = false;
                
                while(!exit){
                    
                    exit = true;
                
                    int answer = JOptionPane.showConfirmDialog(null, panel, "Set name groups", JOptionPane.OK_CANCEL_OPTION);

                    if(answer == JOptionPane.OK_OPTION){
                        for(int i = 0; i < Integer.valueOf(breaksField.getText()) && exit;i++){
                            names[i] = namesField[i].getText();
                            if(namesField[i].getText().length() == 0) exit = false;
                        }
                        if(exit) for(int i = 0; i < resIntervals.length; i++) resString[i] = names[(int)res[i]-1];
                        else JOptionPane.showMessageDialog(null,"Some field is empty");
                    }
                }
            }
            
            String nameOfVar = "d_" + sel_names[0];
            
            if(df.getNames().contains(nameOfVar)){
                int aux = 1;
                while(df.getNames().contains(nameOfVar + Integer.toString(aux))){
                    aux++;
                }
                nameOfVar = nameOfVar + Integer.toString(aux);
            }
            df.addData(nameOfVar, new Variable(nameOfVar,resString));
            mainApplication.updateDataFrame(df);
            this.dispose();
        }
        else{
            JOptionPane.showMessageDialog(null,"Please selecte one variable");
        }        
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
}
