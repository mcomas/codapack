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
import coda.gui.utils.DataSelector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
public class ZeroEMMenu extends AbstractMenuDialog{
    
    /**
     * ATRIBUTES
     */
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Irregular data.Logratio-EM Zero Replacement.yaml";
    private static final String helpTitle = "Logratio-EM zero Replacement Help Menu";
    JTextField DLProportion = new JTextField("0.65");
    double percentatgeDL = 0.65;
    //String[] robOptions = {"FALSE","TRUE"};
    
    JCheckBox robust = new JCheckBox("Robust");
    JPanel covPanel = new JPanel();
    JLabel iniCovOption = new JLabel("IniCov");
    String[] iniCovOptions = {"multRepl","complete.obs"};
    JComboBox<String> iniCovList = new JComboBox<String>(iniCovOptions);
    
    //JCheckBox performMax;
    //JLabel lmax = new JLabel("Use minimum on detection limit");
    JLabel l_usedPercentatgeDL = new JLabel("DL proportion");
    JTextField dlProportion;
    Rengine re;
    DataFrame df;
    ArrayList<String> names;
    
    /**
     * METODES DE CLASSE
     */
    
    public ZeroEMMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Logratio-EM zero Replacement Menu",new DataSelector(mainApp.getActiveDataFrame(), false));
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        re = r;
        
        optionsPanel.add(robust);
        optionsPanel.add(Box.createVerticalStrut(25));
        optionsPanel.add(Box.createHorizontalStrut(50));
        covPanel.add(iniCovOption);
        
        
        iniCovList.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                JComboBox<String> comboBox = (JComboBox<String>) event.getSource();
                Object selected = comboBox.getSelectedItem();
                if(selected.toString().equals("multRepl")){
                    dlProportion.setEnabled(true);
                }
                else{
                    dlProportion.setEnabled(false);
                }
            }
        });
        
        dlProportion = new JTextField(5);
        dlProportion.setText("0.65");
        dlProportion.setEnabled(true);
        covPanel.add(iniCovList);
        optionsPanel.add(covPanel);
        optionsPanel.add(l_usedPercentatgeDL);
        optionsPanel.add(dlProportion);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());

    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        // Geting options
        String rob = "FALSE";
        if(robust.isSelected()) rob = "TRUE";
        String iniCov = iniCovList.getSelectedItem().toString();
        String fracDL = dlProportion.getText();
        
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
    
    public DataFrame getDataFrame(){
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
}
