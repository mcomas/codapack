/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.gui.CoDaPackMain;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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
    String[] iniCovOptions = {"complete.obs","multRepl"};
    JComboBox robList = new JComboBox(robOptions);
    JComboBox iniCovList = new JComboBox(iniCovOptions);
    //JTextField usedPercentatgeDL;
    JLabel robOption = new JLabel("Rob Option");
    JLabel iniCovOption = new JLabel("IniCov Option");
    JCheckBox performMax;
    JLabel lmax = new JLabel("Use maximum on detection limit");
    JLabel l_usedPercentatgeDL = new JLabel("DL proportion");
    JTextField dlProportion;
    //JTextField closureTo;
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
        dlProportion.setEnabled(false);
        optionsPanel.add(iniCovList);
        optionsPanel.add(Box.createVerticalStrut(75));
        optionsPanel.add(Box.createHorizontalStrut(50));
        optionsPanel.add(l_usedPercentatgeDL);
        optionsPanel.add(dlProportion);
        optionsPanel.add(Box.createVerticalStrut(25));
        optionsPanel.add(Box.createHorizontalStrut(50));
        performMax = new JCheckBox("Max result", false);
        performMax.setSelected(true);
        optionsPanel.add(lmax);
        optionsPanel.add(performMax);
        
        
        /*usedPercentatgeDL = new JTextField(5);
        usedPercentatgeDL.setText("0.65");
        optionsPanel.add(l_usedPercentatgeDL);
        optionsPanel.add(usedPercentatgeDL);
        
        performClosure = new JCheckBox("Closure result",false);
        performClosure.setSelected(true);
        performClosure.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (performClosure.isSelected()) {
                    closureTo.setEnabled(true);
                } else {
                    closureTo.setEnabled(false);
                }
            }
        });

        closureTo = new JTextField(5);
        closureTo.setText("1.0");

        optionsPanel.add(performClosure);
        optionsPanel.add(lclosure);
        optionsPanel.add(closureTo);*/
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
    }
}
