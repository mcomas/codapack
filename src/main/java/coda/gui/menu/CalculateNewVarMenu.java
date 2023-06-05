/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.utils.DataSelector1to1;
import coda.gui.CoDaPackConf;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.renjin.script.RenjinScriptEngineFactory;
import org.renjin.sexp.DoubleVector;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author Guest2
 */
public class CalculateNewVarMenu extends AbstractMenuDialog{
    
    ScriptEngine re;
    
    
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Data.Manipulte.Calculate new Variable.yaml";
    private static final String helpTitle = "Calculate new Variable Help Menu";
    
    public CalculateNewVarMenu(final CoDaPackMain mainApp){
    //public CalculateNewVarMenu(final ParaProbarMiMenu mainApp, Rengine r){
        super(mainApp, "Calculate new Variable Menu", new DataSelector1to1(mainApp.getActiveDataFrame(), false));//----?
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        re = (new RenjinScriptEngineFactory()).getScriptEngine();
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        DataFrame df = mainApplication.getActiveDataFrame();
        
        String selectedNames[] = ds.getSelectedData();
        
        if(selectedNames.length > 0){
            
            JLabel labelMessage = new JLabel("Use x1 to x" + String.valueOf(selectedNames.length) + " names for variables");
            
            JPanel panel = new JPanel();
            panel.add(labelMessage);
            panel.add(new JLabel(""));
            panel.add(new JLabel("Enter expression: "));
            JTextField expressionField, nameField;
            expressionField = new JTextField(20);
            nameField = new JTextField(20);
            panel.add(expressionField);
            panel.setLayout(new GridLayout(0,2,2,2));
            panel.add(new JLabel("Enter new variable name: "));
            panel.add(nameField);
                
               
                boolean ask_expresion = true;
                while(ask_expresion){

                    int answer = JOptionPane.showConfirmDialog(this, panel, "Create new Variable", JOptionPane.OK_CANCEL_OPTION);                    
                    if(answer == JOptionPane.OK_OPTION){
                        if(expressionField.getText().length() == 0 || nameField.getText().length() == 0){
                            JOptionPane.showMessageDialog(null, "Some field empty");
                        }else if(mainApplication.getActiveDataFrame().getNames().contains(nameField.getText())){
                            JOptionPane.showMessageDialog(null, "This name is not available");
                        }else{
                            for(int i=0; i < selectedNames.length;i++){                                
                                if(df.get(selectedNames[i]).isNumeric()){
                                    re.put("x" + String.valueOf(i+1), df.get(selectedNames[i]).getNumericalData());
                                }
                                else{ // categorical data
                                    re.put("x" + String.valueOf(i+1), df.get(selectedNames[i]).getTextData());
                                }
                            }
                            String expression = expressionField.getText();
                            try {
                                if(re.eval(expression) instanceof DoubleVector){
                                    double[] res = ((DoubleVector)re.eval(expression)).toDoubleArray();
                                    df.addData(nameField.getText(), res);
                                    mainApplication.updateDataFrame(df);
                                    ask_expresion = false;
                                }else{
                                    JOptionPane.showMessageDialog(null, "Result should be numeric");
                                }
                                
                            } catch (ScriptException e) {
                                JOptionPane.showMessageDialog(null, "Invalid expression");
                            }
                        }
                    }else{
                        ask_expresion = false;
                    }                
                }
                this.dispose();
            }
        else{
            JOptionPane.showMessageDialog(null,"Please select one variable");
        }
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
    

}