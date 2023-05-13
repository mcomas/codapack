/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.utils.DataSelector;
import coda.gui.CoDaPackConf;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class AdvancedFilterMenu extends AbstractMenuDialog{
    
    Rengine re;
    JFrame frame;
    JPanel pane;
    DataFrame df;
    ArrayList<String> names;
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Data.Filters.Advanced Filter.yaml";
    private static final String helpTitle = "Advanced Filter Help Menu";
    
    public AdvancedFilterMenu(final CoDaPackMain mainApp, Rengine r ){
        
        super(mainApp,
              "Advanced Filter Menu",
              new DataSelector(mainApp.getActiveDataFrame(), false));
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        re = r;
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        df = mainApplication.getActiveDataFrame();
        
        String selectedNames[] = ds.getSelectedData();
        
        if(selectedNames.length > 0){ // minimum one variable
            
            boolean exit = false;
            JLabel labelMessage = new JLabel("Following selected order, use x1 to x" + String.valueOf(selectedNames.length) + " instead of variable names to build the expression to subset");
            boolean goodExpression = false, goodName = false;
            String expression = null, dataFrameNewName = null;
            
            while(!exit){
                
                pane = new JPanel();
                pane.setLayout(new GridLayout(0,2,2,2));
                pane.add(labelMessage);
                pane.add(new JLabel(""));
                JTextField expressionField, dataFrameName;
                if(goodExpression) expressionField = new JTextField(expression,20);
                else expressionField = new JTextField(20);
                if(goodName) dataFrameName = new JTextField(dataFrameNewName,20);
                else dataFrameName = new JTextField(20);
                pane.add(new JLabel("R expression to subset: "));
                pane.add(expressionField);
                pane.add(new JLabel("New table name: "));
                pane.add(dataFrameName);
                
                int answer = JOptionPane.showConfirmDialog(frame, pane, "Advanced Filter Menu", JOptionPane.OK_CANCEL_OPTION);

                // respostes

                if(answer == JOptionPane.OK_OPTION){

                    expression = expressionField.getText();
                    dataFrameNewName = dataFrameName.getText();

                    if(expression.length() != 0 && dataFrameNewName.length() != 0){
                        
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
                        
                        // cal fer el subset
                        
                        re.eval ("newdata <- subset(mydf," + expression + ")");
                        
                       REXP rexp = re.eval("out <- capture.output(newdata)");
                       REXP rexp2 = re.eval("is.logical(" + expression +")");
                        
                        if(rexp != null && rexp2 != null){
                            
                            goodExpression = true;
                        
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
                                
                                // put name
                                
                                if(mainApplication.isDataFrameNameAvailable(dataFrameNewName)){
                                    goodName = true;
                                    goodExpression = true;
                                    exit = true;
                                    filtredDataFrame.setName(dataFrameNewName);
                                    mainApplication.addDataFrame(filtredDataFrame);
                                    this.dispose();
                                }
                                else{
                                    goodName = false;
                                    goodExpression = true;
                                    JOptionPane.showMessageDialog(null, "This table name is not available");
                                }
                            }
                            else{
                                JOptionPane.showMessageDialog(null, "No data for this expression");
                            }
                        }
                        else{
                            goodExpression = false;
                            goodName = true;
                            JOptionPane.showMessageDialog(null, "Invalid expression");
                        }
                       
                    }
                    else{
                        if(expression.length() == 0){
                            if(dataFrameNewName.length() != 0 && mainApplication.isDataFrameNameAvailable(dataFrameNewName)){
                                goodName = true;
                            }
                            JOptionPane.showMessageDialog(null, "Please put some expression");
                        }
                        else{
                            goodExpression = true;
                            JOptionPane.showMessageDialog(null, "Please put some table name");
                        }
                    }
                }
                else{
                    exit = true;
                }
            }
            
        }
        else{
            JOptionPane.showMessageDialog(null, "No data available");
        }
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
}
