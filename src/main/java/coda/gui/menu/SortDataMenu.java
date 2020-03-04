/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.Variable;
import coda.gui.CoDaPackMain;
import coda.gui.CoDaPackConf;
import static coda.gui.CoDaPackMain.outputPanel;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;
import coda.gui.output.OutputText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author Guest2
 */
public class SortDataMenu extends AbstractMenuDialog{
    
    Rengine re;
    DataFrame df;
    JCheckBox decSort;
    ArrayList<String> names;
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Data.Manipulte.Sort Data.yaml";
    private static final String helpTitle = "Sort Data Help Menu";
    
    public SortDataMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Sort Data Menu", false, false, true);
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        
        re = r;
        decSort = new JCheckBox("Decreasing sort",false);
        this.optionsPanel.add(decSort);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        String selectedNames[] = ds.getSelectedData(); // we take the selected data
        Vector<String> vSelectedNames = new Vector<String>(Arrays.asList(selectedNames));
        
        if(selectedNames.length > 0){
            
            df = mainApplication.getActiveDataFrame();
            
            // create dataframe on r
            
            int auxPos = 0;
            
            for(int i=0; i < df.size(); i++){
                if(vSelectedNames.contains(df.get(i).getName())){
                    re.eval("x" + String.valueOf(auxPos+1) + " <- NULL");
                    if(df.get(i).isNumeric()){
                        for(double j: df.get(i).getNumericalData()){
                            re.eval("x" + String.valueOf(auxPos+1) + " <- c(x" + String.valueOf(auxPos+1) + "," + String.valueOf(j) + ")");
                        }
                    }
                    else{
                        for(String j: df.get(i).getTextData()){
                            re.eval("x" + String.valueOf(auxPos+1) + " <- c(x" + String.valueOf(auxPos+1) + ",'" + j + "')");
                        }
                    }
                    auxPos++;
                }
            }
            
            String dataFrameString = "mydf <- data.frame(";
            for(int i=0; i < selectedNames.length; i++){
                dataFrameString += "x" + String.valueOf(i+1);
                if(i != selectedNames.length-1) dataFrameString += ",";
            }
            
            dataFrameString += ")";
            re.eval(dataFrameString); // here we create the dataframe in R
            
            // the dataframe was created on R with the name mydf
            // now we do the sort in R
            String orderInstruction = "tryCatch({error <- \"NULL\";order(";
            
            if(this.decSort.isSelected()){
                for(int i=0; i < selectedNames.length;i++){
                    orderInstruction += "-mydf$x" + String.valueOf(i+1);
                    if(i != selectedNames.length-1) orderInstruction += ",";
                }
            }
            else{
                for(int i=0; i < selectedNames.length;i++){
                    orderInstruction += "mydf$x" + String.valueOf(i+1);
                    if(i != selectedNames.length-1) orderInstruction += ",";
                }
            }
            
            orderInstruction += ")}, error = function(e){error <<- e$message})";
            
            int [] orderSort = re.eval(orderInstruction).asIntArray();
            
            String[] errorMessage = re.eval("error").asStringArray();
            
            // new we create the new dataframe with the new order
            
            if(errorMessage[0].equals("NULL")){
            
                DataFrame newDf = new DataFrame();

                // we add the variables ordereds by the orderSort

                for(int i=0; i < df.size(); i++){
                    newDf.addData(df.get(i).getName(), new Variable(df.get(i),orderSort));
                }

                String nameDf = df.getName();
                mainApplication.removeDataFrame(df);

                newDf.setName(nameDf);
                mainApplication.addDataFrame(newDf);
            }
            else{ // s'ha produit un error
                OutputElement type = new OutputText("Error in R:");
                outputPanel.addOutput(type);
                OutputElement outElement = new OutputForR(errorMessage);
                outputPanel.addOutput(outElement);
            }
            
            this.dispose();
        }
        else{
            if(selectedNames.length == 0) JOptionPane.showMessageDialog(null, "Please select some data");
        }
        
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
    
}
