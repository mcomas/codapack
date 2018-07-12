/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author Guest2
 */
public class LogRatioEMMenu extends AbstractMenuDialog{
    
    /**
     * ATRIBUTES
     */
    
    public static final long serialVersionUID = 1L;
    JTextField DLProportion = new JTextField("0.65");
    double percentatgeDL = 0.65;
    String[] robOptions = {"FALSE","TRUE"};
    String[] iniCovOptions = {"multRepl","complete.obs"};
    JComboBox robList = new JComboBox(robOptions);
    JComboBox iniCovList = new JComboBox(iniCovOptions);
    JLabel robOption = new JLabel("Rob Option");
    JLabel iniCovOption = new JLabel("IniCov Option");
    JCheckBox performMax;
    JLabel lmax = new JLabel("Use minimum on detection limit");
    JLabel l_usedPercentatgeDL = new JLabel("DL proportion");
    JTextField dlProportion;
    Rengine re;
    
    /**
     * METODES DE CLASSE
     */
    
    public LogRatioEMMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Log-ratio EM Algorithm",false);
        re = r;
        
        optionsPanel.add(robOption);
        optionsPanel.add(robList);
        optionsPanel.add(Box.createVerticalStrut(25));
        optionsPanel.add(Box.createHorizontalStrut(50));
        optionsPanel.add(iniCovOption);
        
        
        iniCovList.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                JComboBox comboBox = (JComboBox) event.getSource();
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
        optionsPanel.add(iniCovList);
        optionsPanel.add(Box.createVerticalStrut(75));
        optionsPanel.add(Box.createHorizontalStrut(50));
        optionsPanel.add(l_usedPercentatgeDL);
        optionsPanel.add(dlProportion);
        optionsPanel.add(Box.createVerticalStrut(25));
        optionsPanel.add(Box.createHorizontalStrut(50));
        performMax = new JCheckBox("Min result", false);
        performMax.setSelected(true);
        optionsPanel.add(lmax);
        optionsPanel.add(performMax);
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        // configurem frame missatges
        
        JFrame frame = new JFrame();
        frame.setTitle("Message");
        
        // configurem la variable rob
        String rob = "rob=FALSE";
        Object selected = robList.getSelectedItem();
        if(selected.toString().equals("TRUE")) rob = "rob=TRUE";
        
        // configurem la variable iniCov
        
        int iniCov = 0; // 1 = "complete.obs" 0 = "multRepl"
        selected = iniCovList.getSelectedItem();
        if(selected.toString().equals("complete.obs")) iniCov = 1;
        
        // configurem la variable delta i label
        
        String delta = "delta=" + dlProportion.getText();
        String label = "label=0"; // label per defecte a 0
        
        // configurem si és vol agafara els maxims de les columnes
        
        boolean takeMin = true;
        if(!performMax.isSelected()) takeMin = false;
        
        DataFrame df = mainApplication.getActiveDataFrame();
        String[] sel_names = ds.getSelectedData();
        int m = sel_names.length;
        
        if(m >= 2){ // dos o més variables seleccionades
            
            String[] new_names = new String[m];
            for(int i=0; i < m; i++){
                new_names[i] = "z_" + sel_names[i];
            }
            
            boolean containsZero = false;
            double data[][] = df.getNumericalData(sel_names);
            double minimumsOfColumns[] = new double[m]; double minimumOfColumn;
            
            // we search the maximum number for each column
            
            for(int i =0; i < data.length;i++){
                minimumOfColumn = 0.0;
                for(int j=0;j < data[i].length;j++){
                    if(data[i][j] == 0) containsZero = true;
                    if((data[i][j] != 0 && data[i][j] < minimumOfColumn) || minimumOfColumn == 0) minimumOfColumn = data[i][j];
                }
                minimumsOfColumns[i] = minimumOfColumn;
            }
            
            if(containsZero){ // if contains zero then we do something
                
                // configurem la matriu X
                
                re.assign("X", data[0]);
                re.eval("X" + " <- matrix( " + "X" + " ,nc=1");
                for(int i=1; i < data.length;i++){
                    re.assign("tmp", data[i]);
                    re.eval("X" + " <- cbind(" + "X" + ",matrix(tmp,nc=1))");
                }
                
                // configurem la matriu DL
                
                double dlevel[][] = df.getDetectionLevel(sel_names);
                
                if(takeMin){ // si s'ha seleccionat la opció d'agafar el màxim
                    for(int i =0; i < data.length;i++){
                        for(int j=0; j < data[i].length;j++){
                            if(data[i][j] == 0 && dlevel[i][j] == 0) dlevel[i][j] = minimumsOfColumns[i];
                        }
                    }
                }
                
                re.assign("DL", dlevel[0]);
                re.eval("DL" + " <- matrix( " + "DL" + " ,nc=1");
                for(int i=1; i < dlevel.length;i++){
                    re.assign("tmp", dlevel[i]);
                    re.eval("DL" + " <- cbind(" + "DL" + ",matrix(tmp,nc=1))");
                }
                
                // fem la crida a R per obtenir resultats
                
                if(iniCov == 0){ // multRepl
                    re.eval("out <- capture.output(zCompositions::lrEM(X," + label + ",dl=DL, ini.cov=\"multRepl\"," + rob + "," + delta + "))");
                }
                else{ // complete.obs
                    re.eval("out <- capture.output(zCompositions::lrEM(X," + label + ",dl=DL, ini.cov=\"complete.obs\"," + rob + "))");
                }
                
                String[] out = re.eval("out").asStringArray();
                
                // extract the numbers of out
                double resultat[][] = new double[data.length][data[0].length];
                int aux = 0; // y
                Pattern p = Pattern.compile("(\\d+(?:\\.\\d+))");
                char coma = ',';
                for (int i = 3; i < out.length; i++) {
                    Matcher match = p.matcher(out[i].replace(coma, '.'));
                    int aux2 = 0; //x
                    while (match.find()) {
                        double d = Double.parseDouble(match.group(1));
                        resultat[aux2][aux] = d;
                        aux2++;
                    }
                    aux++;
                }
                
                df.addData(new_names, resultat);
                mainApplication.updateDataFrame(df);
                setVisible(false);
                
            }
            else{
                JOptionPane.showMessageDialog(frame,"Please select a variable that contains some 0");
            }
        }
        else{
            JOptionPane.showMessageDialog(frame,"Please select minimum two variables");
        }
        
    }
}