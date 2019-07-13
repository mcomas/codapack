/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import static coda.gui.CoDaPackMain.outputPanel;
import coda.gui.output.OutputForR;
import coda.gui.output.OutputText;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author Guest2
 */
public class ClasUniNormTestMenu extends AbstractMenuDialog{
    
    public static final long serialVersionUID = 1L;
    DataFrame df;
    Rengine re;
    
    String[] normalityOption = {"Shapiro-Wilk Test","Kolmogorov-Smirnov Test"};
    JComboBox normOptionList = new JComboBox(normalityOption);
    JLabel normOpt = new JLabel("Normality Test: ");
    
    public ClasUniNormTestMenu(final CoDaPackMain mainApp, Rengine r){
        
        super(mainApp, "Classical Univariate Normality Test Menu", false);
        re = r;
        
        optionsPanel.add(normOpt);
        optionsPanel.add(normOptionList);
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        String[] sel_names = ds.getSelectedData();
        df = mainApplication.getActiveDataFrame();
        
        if(normOptionList.getSelectedItem().toString().equals("Shapiro-Wilk Test") && sel_names.length == 1){ // shapiro-wilk test
            
            // configurem la variable en R
            re.eval(sel_names[0] + " <- NULL");
            for(double j: df.get(sel_names[0]).getNumericalData()){
                re.eval(sel_names[0] + " <- c(" + sel_names[0] + "," + String.valueOf(j) + ")");
            }
            
            //cridem la funcio
            re.eval("out <- capture.output(stats::shapiro.test(" + sel_names[0] + "))");
            outputPanel.addOutput(new OutputText("<strong>Shapiro-Wilk Test</strong>"));
            outputPanel.addOutput(new OutputForR(re.eval("out").asStringArray()));
            this.dispose();
        }
        else if(normOptionList.getSelectedItem().toString().equals("Kolmogorov-Smirnov Test") && sel_names.length == 1){ // kolmogorov-smirnov test
            
            // configurem la variable en R
            re.eval(sel_names[0] + " <- NULL");
            for(double j: df.get(sel_names[0]).getNumericalData()){
                re.eval(sel_names[0] + " <- c(" + sel_names[0] + "," + String.valueOf(j) + ")");
            }
            
            // cridem la funcio
            re.eval("out <- capture.output(stats::ks.test(" + sel_names[0] + ",pnorm,mean=mean("+sel_names[0]+"), sd=sd("+sel_names[0]+")))");
            outputPanel.addOutput(new OutputText("<strong>Kolmogorov-Smirnov Test</strong>"));
            outputPanel.addOutput(new OutputForR(re.eval("out").asStringArray()));
            this.dispose();
        }
        else{
            if(normOptionList.getSelectedItem().toString().equals("Shapiro-Wilk Test")){
                JOptionPane.showMessageDialog(null,"Select one variable for shapiro test");
            }
            else{
                JOptionPane.showMessageDialog(null, "Select one variables for Kolmogorov test");
            }
        }
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
}
