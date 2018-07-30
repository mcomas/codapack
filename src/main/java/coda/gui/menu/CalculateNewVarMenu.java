/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author Guest2
 */
public class CalculateNewVarMenu extends AbstractMenuDialog{
    
    Rengine re;
    JFrame frame;
    JPanel pane;
    
    public static final long serialVersionUID = 1L;
    
    public CalculateNewVarMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Calculate new Variable Menu", false);
        re = r;
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        String selectedNames[] = ds.getSelectedData();
        
        if(selectedNames.length > 0){
            
            DataFrame df = mainApplication.getActiveDataFrame();
            boolean exit = false;
            JLabel labelMessage = new JLabel("Use x1 to x" + String.valueOf(selectedNames.length) + " names values for variables");
            JOptionPane.showMessageDialog(null, labelMessage);
            boolean goodExpression = false, goodName = false;
            
            while(!exit){
                
                pane = new JPanel();
                
                
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"Please select one variable");
        }
    }
}
