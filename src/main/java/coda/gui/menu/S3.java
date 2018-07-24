/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author Guest2
 */
public class S3 extends AbstractMenuDialog2NumCatONum{
    
    Rengine re;
    DataFrame df;
    JFrame frameZPatterns;
    String tempDirR;
    
    public static final long serialVersionUID = 1L;
    
    public S3(final CoDaPackMain mainApp, Rengine r){
        super(mainApp,"S3 menu",false,false,true);
        re = r;
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        String selectedNames1[] = super.ds.getSelectedData1();
        String selectedNames2[] = super.ds.getSelectedData2();
        Vector<String> vSelectedNames2 = new Vector<String>(Arrays.asList(selectedNames2));
        
        if(selectedNames1.length > 0 && selectedNames2.length > 0){
            
            df = mainApplication.getActiveDataFrame();
            double[][] numericData = df.getNumericalData(selectedNames1);
            
            // Create X matrix
            
            re.assign("X", numericData[0]);
            re.eval("X" + " <- matrix( " + "X" + " ,nc=1)");
            for(int i=0; i < numericData.length; i++){
                re.assign("tmp", numericData[i]);
                re.eval("X" + " <- cbind(" + "X" + ",matrix(tmp,nc=1))");
            }
            
            // Create Y matrix
            int aux = 0;
            for(int i=0; i < df.size(); i++){
                if(vSelectedNames2.contains(df.get(i).getName())){
                    if(aux == 0){
                        if(df.get(i).isNumeric()){
                            re.assign("Y", df.get(i).getNumericalData());
                            re.eval("Y" + " <- matrix( " + "X" + " ,nc=1))");
                        }
                        else{
                            re.assign("Y", df.get(i).getTextData());
                            re.eval("Y" + " <- matrix( " + "X" + " ,nc=1))");
                        }
                    }
                    else{
                        if(df.get(i).isNumeric()){
                            re.assign("tmp", df.get(i).getNumericalData());
                            re.eval("Y" + " <- cbind(" + "X" + ",matrix(tmp,nc=1))");
                        }
                        else{
                            re.assign("tmp", df.get(i).getTextData());
                            re.eval("Y" + " <- cbind(" + "X" + ",matrix(tmp,nc=1))");
                        }
                    }
                }
            }
            
            re.eval("out <- capture.output(Y)");
            String[] out = re.eval("out").asStringArray();
            
            String auximilar = " ";
            
        }
        else{
            if(selectedNames1.length == 0) JOptionPane.showMessageDialog(null, "No data selected in data 1");
            else JOptionPane.showMessageDialog(null, "No data selected in data 2");
        }
    }
    
}
