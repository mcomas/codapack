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
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.rosuda.JRI.REXP;
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
            DataFrame df = mainApp.getActiveDataFrame();
            boolean exit = false;
            re = r;
            while(!exit){
                JTextField expressionField = new JTextField(20);
                JPanel myPanel = new JPanel();
                JLabel labelMessage = new JLabel("Use x1 to x" + String.valueOf(df.getNames().size()) + " names values for variables");
                JOptionPane.showMessageDialog(null, labelMessage);
                myPanel.add(new JLabel("R expression to subset:"));
                myPanel.add(expressionField);
                int answer = JOptionPane.showConfirmDialog(null,myPanel, "Advanced Filter", JOptionPane.OK_CANCEL_OPTION);

                // respostes

                if(answer == JOptionPane.OK_OPTION){

                    String expression = expressionField.getText();

                    if(expression.length() != 0){
                        
                        // create dataframe on r
                        
                        for(int i=0; i < df.getNames().size();i++){ // totes les columnes
                            re.eval("x" + String.valueOf(i+1) + " <- NULL");
                            if(df.get(i).isNumeric()){
                                for(double j : df.get(i).getNumericalData()){
                                    re.eval("x" + String.valueOf(i+1) + " <- c(x" + String.valueOf(i+1) +"," + String.valueOf(j) + ")");
                                }
                            }
                            else{
                                for(String j : df.get(i).getTextData()){
                                    re.eval("x" + String.valueOf(i+1) + " <- c(x" + String.valueOf(i+1) +",'" + j + "')");
                                }
                            }
                        }
                        
                        String dataFrameString = "mydf <- data.frame(";
                        for(int i=0; i < df.getNames().size();i++){
                            dataFrameString += "x" + String.valueOf(i+1);
                            if(i != df.getNames().size()-1) dataFrameString += ",";
                        }
                        
                        dataFrameString +=")";
                        
                        re.eval(dataFrameString); // we create the dataframe in R
                        
                        // cal fer el subset
                        
                        re.eval ("newdata <- subset(mydf," + expression + ")");
                        
                       REXP rexp = re.eval("out <- capture.output(newdata)");
                        
                        if(rexp != null){
                            
                            exit = true;
                        
                            String[] out = re.eval("out").asStringArray();
                            
                            if(!out[1].equals("<0 rows> (or 0-length row.names)")){
                        
                                int numberofdata  = df.getMaxVariableLength(); // numero de files
                                Vector<Integer> validRows = new Vector<Integer>();
                                Vector<Integer> rowsToDelete = new Vector<Integer>();

                                for(int i = 1; i < out.length; i++){
                                    Matcher matcher = Pattern.compile("\\d+").matcher(out[i]);
                                    matcher.find();
                                    validRows.add(Integer.valueOf(matcher.group())-1);
                                }

                                for(int i=0; i < numberofdata; i++){
                                    if(!validRows.contains(i)) rowsToDelete.add(i);
                                }

                                int[] resRowsToDelete = new int[rowsToDelete.size()];
                                for(int i=0; i < rowsToDelete.size();i++) resRowsToDelete[i] = rowsToDelete.elementAt(i);

                                DataFrame filtredDataFrame = new DataFrame(df);
                                filtredDataFrame.subFrame(resRowsToDelete);
                                int nameNum = 0;
                                String newDataFrameName = df.name + "advFilt" + String.valueOf(nameNum);
                                while(!mainApp.isDataFrameNameAvailable(newDataFrameName)){
                                    nameNum++;
                                    newDataFrameName = df.name + "advFilt" + String.valueOf(nameNum);
                                }
                                filtredDataFrame.setName(newDataFrameName);
                                mainApp.addDataFrame(filtredDataFrame);
                            }
                            else{
                                JOptionPane.showMessageDialog(null, "No data for this expression");
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Invalid expression");
                        }
                       
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