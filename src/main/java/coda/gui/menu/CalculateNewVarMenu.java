/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.CoDaPackConf;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author Guest2
 */
public class CalculateNewVarMenu extends AbstractMenuDialog{
    
    Rengine re;
    JFrame frame;
    JPanel panel;
    DataFrame df;
    ArrayList<String> names;
    double[] dataNewVar;
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Data.Manipulte.Calculate new Variable.yaml";
    private static final String helpTitle = "Calculate new Variable Help Menu";
    
    public CalculateNewVarMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Calculate new Variable Menu", false);
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        re = r;
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        df = mainApplication.getActiveDataFrame();
        
        String selectedNames[] = ds.getSelectedData();
        
        if(selectedNames.length > 0){
            
            JLabel labelMessage = new JLabel("Use x1 to x" + String.valueOf(selectedNames.length) + " names for variables");
            
            panel = new JPanel();
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
                
                boolean fieldEmpty = true, invalidName = true, invalidExpression = true;
                
                int answer = JOptionPane.showConfirmDialog(frame, panel, "Create new Variable", JOptionPane.OK_CANCEL_OPTION);
                
                while(answer == JOptionPane.OK_OPTION && (fieldEmpty || invalidName || invalidExpression)){
                
                    fieldEmpty = false; invalidName = false; invalidExpression = false;
                    
                    if(expressionField.getText().length() == 0 || nameField.getText().length() == 0){
                        fieldEmpty = true;
                    }
                    else if(mainApplication.getActiveDataFrame().getNames().contains(nameField.getText())){
                        invalidName = true;
                    }
                    else{

                        // create dataframe on R
                        
                            for(int i=0; i < selectedNames.length;i++){
                                re.eval("x" + String.valueOf(i+1) + " <- NULL");
                                if(df.get(selectedNames[i]).isNumeric()){
                                    for(double j : df.get(selectedNames[i]).getNumericalData()){
                                        re.eval("x" + String.valueOf(i+1) + " <- c(x" + String.valueOf(i+1) + "," + String.valueOf(j) + ")");
                                    }
                                }
                                else{ // categorical data
                                    for(String j : df.get(selectedNames[i]).getTextData()){
                                        re.eval("x" + String.valueOf(i+1) + " <- c(x" + String.valueOf(i+1) + ",'" + j + "')");
                                    }
                                }
                            }

                            String dataFrameString = "mydf <- data.frame(";
                            for(int i=0; i < selectedNames.length;i++){
                                dataFrameString += "x" + String.valueOf(i+1);
                                if(i != selectedNames.length-1) dataFrameString += ",";
                            }

                            dataFrameString +=")";

                            re.eval(dataFrameString); // we create the dataframe in R

                            //transform the expression

                            String expression = expressionField.getText();

                            for(int i=0; i < selectedNames.length; i++){
                               expression = expression.replaceAll("x" + String.valueOf(i+1), "mydf\\$x" + String.valueOf(i+1));
                            }

                            if(re.eval(expression) == null) invalidExpression = true;
                            else{
                                double[] res = re.eval(expression).asDoubleArray();
                                dataNewVar = res;
                                if(res == null) invalidExpression = true;
                            }
                    }
                    
                    if(fieldEmpty) JOptionPane.showMessageDialog(null, "Some field empty");
                    else if(invalidName) JOptionPane.showMessageDialog(null, "This name is not available");
                    else if(invalidExpression) JOptionPane.showMessageDialog(null, "Invalid expression");
                    
                    if(fieldEmpty || invalidName || invalidExpression) answer = JOptionPane.showConfirmDialog(frame, panel, "Create new Variable", JOptionPane.OK_CANCEL_OPTION);
                
                }
                if(answer == JOptionPane.OK_OPTION){
                    this.dispose();
                    df = mainApplication.getActiveDataFrame();
                    df.addData(nameField.getText(), dataNewVar);
                    mainApplication.updateDataFrame(df);
                }
            }
        else{
            JOptionPane.showMessageDialog(null,"Please select one variable");
        }
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
}