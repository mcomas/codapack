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
import coda.gui.utils.DataSelector;
import coda.gui.CoDaPackMain;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 *
 * @author mcomas
 */
public abstract class AbstractMenuDialog extends JDialog{
    final DataSelector ds;
    public JPanel optionsPanel = new JPanel();;
    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    CoDaPackMain mainApplication;
    boolean allowEmpty = false;
    public AbstractMenuDialog(final CoDaPackMain mainApp, String title, boolean groups, boolean allowEmpty, boolean categoric){
        super(mainApp, title);
        mainApplication = mainApp;

        ds = new DataSelector(mainApplication.getActiveDataFrame(), groups, categoric);
        initialize();
    }
    public AbstractMenuDialog(final CoDaPackMain mainApp, String title, boolean groups, boolean allowEmpty){
        super(mainApp, title);
        mainApplication = mainApp;

        this.allowEmpty = allowEmpty;
        ds = new DataSelector(mainApplication.getActiveDataFrame(), CoDaPackMain.dataList.getSelectedData(), groups);
        initialize();
    }
    public AbstractMenuDialog(final CoDaPackMain mainApp, String title, boolean groups){
        super(mainApp, title);
        mainApplication = mainApp;
        ds = new DataSelector(mainApplication.getActiveDataFrame(), CoDaPackMain.dataList.getSelectedData(), groups);
        initialize();
    }
    private void initialize(){
        Point p = mainApplication.getLocation();
        p.x = p.x + (mainApplication.getWidth()-520)/2;
        p.y = p.y + (mainApplication.getHeight()-430)/2;
        setLocation(p);

        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        setSize(560,430);
        getContentPane().add(ds, BorderLayout.CENTER);

        JPanel eastPanel = new JPanel();
        optionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Options"));
        optionsPanel.setPreferredSize(new Dimension(200,200));
        //optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        //eastPanel.add(optionsPanel);
        getContentPane().add(optionsPanel, BorderLayout.EAST);

        JButton acceptButton = new JButton("Accept");
        southPanel.add(acceptButton);
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed();
            }
        });
        JButton cancelButton = new JButton("Cancel");
        southPanel.add(cancelButton);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });
        getContentPane().add(southPanel, BorderLayout.SOUTH);
    }
    @Override
    public void setVisible(boolean v){
        if(mainApplication.getActiveDataFrame() == null && !allowEmpty){
            JOptionPane.showMessageDialog(this, "No data available");
            this.dispose();
        }else{
            super.setVisible(v);
        }
    }
    public abstract void acceptButtonActionPerformed();

    public boolean[] getValidComposition(DataFrame df, String[] selectedNames){
        boolean selection[] = df.getValidCompositions(selectedNames);
        String invalid = "";
        int count = 0;
        for(int i=0;i<selection.length;i++){
            if(!selection[i]){
                if(count == 5){
                    count = 0;
                    invalid += "<br>";
                }
                invalid += Integer.toString(i+1) + ", ";
                count++;
            }

        }
        int len = invalid.length();
        if(len != 0)JOptionPane.showMessageDialog(this,
                    "<html><p>Rows number <center><i>" + invalid.substring(0,len-2) +
                    "</i></center>are going to be ignored due to zero <br>"
                    + "data in some of its components.</p></html>");

        return selection;        
    }
    public boolean[] getValidData(DataFrame df, String[] selectedNames){
        boolean selection[] = df.getValidData(selectedNames);
        String invalid = "";
        int count = 0;
        for(int i=0;i<selection.length;i++){
            if(!selection[i]){
                if(count == 5){
                    count = 0;
                    invalid += "<br>";
                }
                invalid += Integer.toString(i+1) + ", ";
                count++;
            }

        }
        int len = invalid.length();
        if(len != 0)JOptionPane.showMessageDialog(this,
                    "<html><p>Rows number <center><i>" + invalid.substring(0,len-2) +
                    "</i></center>are going to be ignored due to<br>"
                    + "missing data in some of its components.</p></html>");

        return selection;
    }

    public void closeMenuDialog(){        
        setVisible(false);
    }
    public DataSelector getDataSelector(){
        return ds;
    }
    public String[] getSelectedData(){
        return ds.getSelectedData();
    }
}


