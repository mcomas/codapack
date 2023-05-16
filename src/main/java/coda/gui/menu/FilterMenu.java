/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió
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
import coda.gui.CoDaPackConf;
import coda.gui.utils.BoxDataSelector;
import coda.gui.utils.DataSelector1to1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Guest2
 */
public class FilterMenu extends AbstractMenuDialog{
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Data.Filters.Categorical Filter.yaml";
    private static final String helpTitle = "Categorical Filter Help Menu";
    BoxDataSelector boxdataSel;
    DataFrame df;
    ArrayList<String> names;
    JDialog dialog;
    String[] sel_names;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    
    public FilterMenu(final CoDaPackMain mainApp){
       
        super(mainApp,"Categorical Filter Menu",new DataSelector1to1(mainApp.getActiveDataFrame(), false, DataSelector1to1.ONLY_CATEGORIC));
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
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
                
                // comencem el filtratge
                
                // Creem tots els subframes que ens facin falta
                
                DataFrame newDataFrame = new DataFrame(df); // array de dataframes
                int [] rowsToDelete = df.getRowsToDelete(sel_names[0], selected);
                newDataFrame.subFrame(rowsToDelete);
                newDataFrame.setName(df.name + "_Fil_" + sel_names[0]);
                mainApplication.addDataFrame(newDataFrame);
            }
            
            });
            
              setVisible(false);
              dialog.setVisible(true);
        }
        else{
            JOptionPane.showMessageDialog(frame2, "Please select one variable");
        }
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
    
}
