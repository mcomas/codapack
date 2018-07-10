/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.io.ImportRDA;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
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
public class AdvancedFilterMenu{
    
    Rengine re;
    
    public AdvancedFilterMenu(final CoDaPackMain mainApp, Rengine r ){
        
        if(mainApp.getActiveDataFrame() == null){  // si no hi ha cap dataframe actiu
            JOptionPane.showMessageDialog(null, "No data available");
        }
        else{ // fem el filtratge
            boolean exit = false;
            re = r;
            while(!exit){
                JTextField expressionField = new JTextField(20);
                JPanel myPanel = new JPanel();
                myPanel.add(new JLabel("Expression:"));
                myPanel.add(expressionField);
                int answer = JOptionPane.showConfirmDialog(null,myPanel, "Advanced Filter", JOptionPane.OK_CANCEL_OPTION);

                // respostes

                if(answer == JOptionPane.OK_OPTION){

                    String expression = expressionField.getText();

                    if(expression.length() != 0){
                        exit = true;
                        
                        DataFrame df = mainApp.getActiveDataFrame();
                        /*double[] auxNumeric = null;
                        String[] auxText = null;
                        
                        if(df.get(0).isNumeric()){
                            auxNumeric = df.get(0).getNumericalData();
                        }
                        else{
                            auxText = df.get(0).getTextData();
                        }*/
                
                        /*if(df.get(0).isNumeric()) re.assign("X", auxNumeric);
                        else re.assign("X", auxText);
                        
                        re.eval("X" + " <- matrix( " + "X" + " ,nc=1)");
                        for(int i=1; i < df.getNames().size(); i++){
                            if(df.get(i).isNumeric()){
                                auxNumeric = df.get(i).getNumericalData();
                            }
                            else{
                                auxText = df.get(i).getTextData();
                            }
                            if(df.get(i).isNumeric()) re.assign("tmp", auxNumeric);
                            else re.assign("tmp",auxText);
                            re.eval("X" + " <- cbind(" + "X" + ",matrix(tmp,nc=1))");
                        }*/
                        
                        // create dataframe on r
                        
                        for(int i=0; i < df.getNames().size();i++){ // totes les columnes
                            re.eval("c" + String.valueOf(i+1) + " <- NULL");
                            if(df.get(i).isNumeric()){
                                for(double j : df.get(i).getNumericalData()){
                                    re.eval("c" + String.valueOf(i+1) + " <- c(c" + String.valueOf(i+1) +"," + String.valueOf(j) + ")");
                                }
                            }
                            else{
                                for(String j : df.get(i).getTextData()){
                                    re.eval("c" + String.valueOf(i+1) + " <- c(c" + String.valueOf(i+1) +",'" + j + "')");
                                }
                            }
                        }
                        
                        String dataFrameString = "mydf <- data.frame(";
                        for(int i=0; i < df.getNames().size();i++){
                            dataFrameString += "c" + String.valueOf(i+1);
                            if(i != df.getNames().size()-1) dataFrameString += ",";
                        }
                        
                        dataFrameString +=")";
                        
                        for(int i=0; i < df.getNames().size();i++){
                            re.eval("out <- capture.output(c" + String.valueOf(i+1) + ")");
                            String[] out = re.eval("out").asStringArray();
                            for(String j : out) System.out.println(j);
                        }
                        
                        re.eval(dataFrameString);
                        re.eval("out <- capture.output(mydf)");
                        String[] out = re.eval("out").asStringArray();
                        String aux = "hola";
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Please put some expression");
                    }
                }
                else{
                    exit = true;
                }
            }
        }
    }
}
