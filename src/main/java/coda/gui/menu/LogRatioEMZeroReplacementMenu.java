/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.BasicStats;
import coda.DataFrame;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.utils.DataSelector1to1;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
public class LogRatioEMZeroReplacementMenu extends AbstractMenuRBasedDialog{
    private static final String yamlUrl = CoDaPackConf.helpPath + "Irregular data.Logratio-EM Zero Replacement.yaml";
    private static final String helpTitle = "Logratio-EM zero Replacement Help Menu";
    Rengine re;
    
    JCheckBox P1 = new JCheckBox("Robust");
    
    String[] iniCovOptions = {"multRepl","complete.obs"};
    JComboBox<String> B1 = new JComboBox<String>(iniCovOptions);
    
    JTextField B2 = new JTextField("0.65", 5);
  
    public LogRatioEMZeroReplacementMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Logratio-EM zero Replacement Menu", new DataSelector1to1(mainApp.getActiveDataFrame(), false), r);
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        
        /* options configuration */
        this.optionsPanel.setLayout(new BoxLayout(this.optionsPanel, BoxLayout.PAGE_AXIS));

        JPanel PB1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        PB1.setMaximumSize(new Dimension(1000, 32));
        PB1.add(P1);


        JPanel PB2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        PB2.setMaximumSize(new Dimension(1000, 32));
        PB2.add(new JLabel("IniCov"));
        PB2.add(Box.createHorizontalStrut(10));
        PB2.add(B1);
        
        JPanel PB3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        PB3.setMaximumSize(new Dimension(1000, 32));        
        PB3.add(new JLabel("DL proportion"));  
        PB3.add(Box.createHorizontalStrut(10));
        PB3.add(B2);

        PB1.setAlignmentX(0);
        PB2.setAlignmentX(0);
        PB3.setAlignmentX(0);

        this.optionsPanel.add(Box.createRigidArea(new Dimension(15,15)));
        optionsPanel.add(PB1);
        optionsPanel.add(PB2);
        optionsPanel.add(PB3);

        B1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                JComboBox comboBox = (JComboBox) event.getSource();
                Object selected = comboBox.getSelectedItem();
                if(selected.toString().equals("multRepl")){
                    B2.setEnabled(true);
                    PB3.setVisible(true);
                }
                else{
                    B2.setEnabled(false);
                    PB3.setVisible(false);
                }
            }
        });

    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        // Geting options
        String rob = "FALSE";
        if(P1.isSelected()) rob = "TRUE";
        String iniCov = B1.getSelectedItem().toString();
        String fracDL = B2.getText();
        
        // configurem si es vol agafara els maxims de les columnes
        
        df = mainApplication.getActiveDataFrame();
        String[] sel_names = ds.getSelectedData();
        
        if(sel_names.length >= 2){ // dos o mes variables seleccionades
            
            String[] new_names = new String[sel_names.length];
            for(int i=0; i < sel_names.length; i++){
                new_names[i] = "z_" + sel_names[i];
            }
            
            double data[][] = df.getNumericalData(sel_names);
            int numZeros = BasicStats.nZeros(data);

            if(numZeros > 0){ // if contains zero then we do something
                
                for(int i=0; i < data.length; i++){
                    re.assign(sel_names[i], data[i]);
                }
                re.eval("X = cbind(" + String.join(",", sel_names) + ")");
                
                // configurem la matriu DL
                
                double dlevel[][] = df.getDetectionLevel(sel_names);                
                int numDlevel = BasicStats.nPositive(dlevel);

                if(numZeros == numDlevel){
                
                    for(int i=0; i < dlevel.length; i++){
                        re.assign(sel_names[i], dlevel[i]);
                    }
                    re.eval("DL = cbind(" + String.join(",", sel_names) + ")");

                    //re.eval("save.image('image.RData')");
                    String E = "RES = zCompositions::lrEM(X,label=0,dl=DL,rob=#ROB#,frac=#FRAC#,ini.cov='#INI.COV#')"
                               .replace("#FRAC#", fracDL)
                               .replace("#ROB#", rob)
                               .replace("#INI.COV#", iniCov);
                    System.out.println(E);
                    re.eval(E);
       

                    double resultat[][] = re.eval("t(as.matrix(RES))").asDoubleMatrix();
                    

                    df.addData(new_names, resultat);
                    mainApplication.updateDataFrame(df);
                    setVisible(false);
                }
                
                else{
                    JOptionPane.showMessageDialog(this, "Please set detection limit for all zeros");
                }
                
            }
            else{
                JOptionPane.showMessageDialog(this,"Please select a variable that contains some 0");
            }
        }
        else{
            JOptionPane.showMessageDialog(this,"Please select minimum two variables");
        }
        
    }

}
