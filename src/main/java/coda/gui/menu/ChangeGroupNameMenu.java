/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.utils.BoxDataSelector;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author Guest2
 */
public class ChangeGroupNameMenu extends AbstractMenuDialog{
    
    public static final long serialVersionUID =1L;
    
    DataFrame df;
    String [] sel_names;
    BoxDataSelector boxDataSel;
    JDialog dialog;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    
    public ChangeGroupNameMenu(final CoDaPackMain mainApp){
        super(mainApp,"Change Group Name Menu", "categoric");
    }
    
   @Override
   public void acceptButtonActionPerformed(){
       
       df = mainApplication.getActiveDataFrame();
       
       sel_names = ds.getSelectedData();
       
       if(sel_names.length == 1){
           
           String [] data = df.getCategoricalData(sel_names[0]);
           Vector<String> groups = new Vector<String>();
           
           for(String i: data){
               if(!groups.contains(i)) groups.add(i);
           }
           
           boxDataSel = new BoxDataSelector(groups.toArray(new String[groups.size()]));
           
           dialog = new JDialog();
           dialog.setSize(190,370);
           dialog.getContentPane().setLayout(new BorderLayout());
           
           dialog.getContentPane().add(boxDataSel, BorderLayout.CENTER);
           dialog.setLocation(dim.width/2-dialog.getSize().width/2, dim.height/2-dialog.getSize().height/2);
           
           JButton accept = new JButton("Accept");
           dialog.getContentPane().add(accept,BorderLayout.SOUTH);
           accept.addActionListener(new java.awt.event.ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   // aqui hem de fer la operació necessaria
               }
           });
           
           setVisible(false);
           dialog.setVisible(false);
           
       }
       else{
           JOptionPane.showMessageDialog(null, "Please select one variable");
       }
       
   }
    
   public DataFrame getDataFrame(){
       return this.df;
   }
}
