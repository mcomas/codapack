/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.CoDaPackConf;
import coda.gui.utils.BoxDataSelector;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Guest2
 */
public class ChangeGroupNameMenu extends AbstractMenuDialog{
    
    public static final long serialVersionUID =1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Data.Manipulte.Change Categorical Label.yaml";
    private static final String helpTitle = "Change Categorical Label Help Menu";
    
    DataFrame df;
    ArrayList<String> names;
    String [] sel_names;
    BoxDataSelector boxDataSel;
    JDialog dialog;
    JPanel panel;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    
    public ChangeGroupNameMenu(final CoDaPackMain mainApp){
        super(mainApp,"Change Categorical Label Menu", "categoric");
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
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
                   
                   String [] selected = boxDataSel.getSelectedData();
                   int n = selected.length; // numero de variables
                   if(n > 0){
                        dialog.setVisible(false);

                        panel = new JPanel();
                        panel.setLayout(new GridLayout(0,2,2,2));
                        JTextField[] namesFieldPanel = new JTextField[n];
                        JLabel[] namesLabel = new JLabel[n];
                        for(int i= 0; i <n; i++){
                            namesFieldPanel[i] = new JTextField(20);
                            namesLabel[i] = new JLabel("Enter label to replace \"" + selected[i] + "\" :");
                            panel.add(namesLabel[i]);
                            panel.add(namesFieldPanel[i]);
                        }

                        String [] namesValues = new String[n];
                        boolean exit = false;

                        while(!exit){

                            exit = true;
                            int answer = JOptionPane.showConfirmDialog(null,panel,"Set new labels/set new group names", JOptionPane.OK_CANCEL_OPTION);

                            if(answer == JOptionPane.OK_OPTION){
                                for(int i=0; i <n && exit; i++){
                                    namesValues[i] = namesFieldPanel[i].getText();
                                    if(namesFieldPanel[i].getText().length() == 0) exit = false;
                                }
                                if(!exit) JOptionPane.showMessageDialog(null,"Some field is empty");
                            }
                        }

                        // aqui ja tenim els noms q volem canviar

                        for(int i=0; i<n; i++){
                            for(int j=0; j <df.get(sel_names[0]).size(); j++){
                                if(df.get(sel_names[0]).get(j).toString().equals(selected[i])) df.get(sel_names[0]).get(j).setValue(namesValues[i]);
                            }
                        }

                        mainApplication.updateDataFrame(df);
                   }
                   else JOptionPane.showMessageDialog(null, "Please select one name");
               }
           });
           
           setVisible(false);
           dialog.setVisible(true);
       }
       else{
           JOptionPane.showMessageDialog(null, "Please select one variable");
       }
       
   }
    
   public DataFrame getDataFrame(){
       return this.df;
   }
   
   public ArrayList<String> getDataFrameNames(){
       return this.names;
   }
}
