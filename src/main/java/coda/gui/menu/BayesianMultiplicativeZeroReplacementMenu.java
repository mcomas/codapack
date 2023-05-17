/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.BasicStats;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.utils.DataSelector1to1;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.rosuda.JRI.Rengine;

/**
 * BayesianMultRepMenu -> X numerica i positiva amb opcio de retornar text, crear dataframe, afegir variables i  mostrar grafics
 * @author Guest2
 */
public class BayesianMultiplicativeZeroReplacementMenu extends AbstractMenuRBasedDialog{
    private static final String yamlUrl = CoDaPackConf.helpPath + "Irregular data.Bayesian-multiplicative zero replacement.yaml";
    private static final String helpTitle = "Bayesian Multiplicative zero Replacement Help Menu";
    
        
    String[] P1Options = {"GBM","SQ","BL","CZM"};
    String[] P2Options = {"prop","p-counts"};
    
    JComboBox<String> P1ComboOptions = new JComboBox<String>(P1Options);
    JComboBox<String> P2ComboOptions = new JComboBox<String>(P2Options);
    
    
    public static final long serialVersionUID = 1L;
    
    public BayesianMultiplicativeZeroReplacementMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Bayesian Multiplicative zero Replacement Menu", new DataSelector1to1(mainApp.getActiveDataFrame(), false), r);
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);

        this.optionsPanel.setLayout(new BoxLayout(this.optionsPanel, BoxLayout.PAGE_AXIS));
        
        /* options configuration */
        JPanel PB1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        PB1.setMaximumSize(new Dimension(1000, 32));        
        PB1.add(new JLabel("Method"));  
        PB1.add(Box.createHorizontalStrut(10));
        PB1.add(P1ComboOptions);

        JPanel PB2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        PB2.setMaximumSize(new Dimension(1000, 32));        
        PB2.add(new JLabel("Output"));  
        PB2.add(Box.createHorizontalStrut(10));
        PB2.add(P2ComboOptions);

        this.optionsPanel.add(PB1);
        this.optionsPanel.add(PB2);
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        // Geting options
        String method = P1ComboOptions.getSelectedItem().toString();
        String resultFormat = P2ComboOptions.getSelectedItem().toString();

        df = mainApplication.getActiveDataFrame();
        String[] sel_names = ds.getSelectedData();
        
        if(sel_names.length >= 2){
            
            String[] new_names = new String[sel_names.length];
            for(int i=0; i < sel_names.length; i++){
                new_names[i] = "z_" + sel_names[i];
            }

            double data[][] = df.getNumericalData(sel_names);
            int numZeros = BasicStats.nZeros(data);

            if(numZeros > 0){
                for(int i=0; i < data.length; i++){
                    re.assign(sel_names[i], data[i]);
                }
                re.eval("X = cbind(" + String.join(",", sel_names) + ")");

                //re.eval("save.image('image.RData')");
                String E = "RES = zCompositions::cmultRepl(X,method='#METHOD#', output='#OUTPUT#')"
                            .replace("#METHOD#", method)
                            .replace("#OUTPUT#", resultFormat);
                System.out.println(E);
                re.eval(E);


                double resultat[][] = re.eval("t(as.matrix(RES))").asDoubleMatrix();
                

                df.addData(new_names, resultat);
                mainApplication.updateDataFrame(df);
                setVisible(false);

            }else{
                JOptionPane.showMessageDialog(this,"Please select a variable that contains some 0");
            }

        }else{
            JOptionPane.showMessageDialog(this,"Please select minimum two variables");
        }
    }
    
}