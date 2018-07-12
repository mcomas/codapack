/**	
 *	Copyright 2011-2016 Marc Comas - Santiago ThiÃ³
 *
 *	This file is part of CoDaPack.
 *
 *  CoDaPack is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CoDaPack is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CoDaPack.  If not, see <http://www.gnu.org/licenses/>.
 */

package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.utils.BoxDataSelector;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Guest2
 */
public class FilterMenu extends AbstractMenuDialog{
    
    public static final long serialVersionUID = 1L;
    BoxDataSelector boxdataSel;
    DataFrame df;
    JDialog dialog;
    String[] sel_names;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    
    public FilterMenu(final CoDaPackMain mainApp){
       
        super(mainApp,"Filter Menu","categoric");
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        // frame message
        
        JFrame frame2 = new JFrame();
        frame2.setTitle("Message");
        
        df = mainApplication.getActiveDataFrame();
        sel_names = ds.getSelectedData();
        int m = sel_names.length;
        if(m == 1){
           
            // ara cal obtenir els grups de la variable
            
            String[] data = df.getCategoricalData(sel_names[0]);
            Vector<String> groups = new Vector<String>();
            
            for(String i: data){
                if(!groups.contains(i)) groups.add(i);
            }
            
            String[] groupsRes = groups.toArray(new String[groups.size()]);
            
            boxdataSel = new BoxDataSelector(groupsRes);
            
            dialog = new JDialog();
            dialog.setSize(190,370);
            dialog.getContentPane().setLayout(new BorderLayout());

            dialog.getContentPane().add(boxdataSel, BorderLayout.CENTER);
            dialog.setLocation(dim.width/2-dialog.getSize().width/2, dim.height/2-dialog.getSize().height/2);
            
            JButton accept = new JButton("Accept");
            dialog.getContentPane().add(accept, BorderLayout.SOUTH);
            accept.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String selected[] = boxdataSel.getSelectedData(); //variables que volem fer subframe
                int n = selected.length;
                dialog.setVisible(false);
                
                // començem el filtratge
                
                // Creem tots els subframes que ens facin falta
                
                DataFrame[] dataframes = new DataFrame[selected.length]; // array de dataframes
                
                for(int i=0; i < selected.length;i++){
                    // cal obtenir les files que hem d'eliminar
                    int[] rowsToDelete = df.getRowsToDelete(sel_names[0],selected[i]); // obtenim posicions que em de borrar
                    dataframes[i] = new DataFrame(df);
                    dataframes[i].subFrame(rowsToDelete);
                    dataframes[i].setName(df.name + "_" + selected[i]);
                }
                
                for(int i=0; i < dataframes.length;i++) mainApplication.addDataFrame(dataframes[i]);
            }
            
            });
            
              setVisible(false);
              dialog.setVisible(true);
        }
        else{
            JOptionPane.showMessageDialog(frame2, "Please select one variable");
        }
    }
}