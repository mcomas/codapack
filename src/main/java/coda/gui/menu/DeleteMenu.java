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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.menu;

import coda.gui.utils.BoxDataSelector;
import coda.DataFrame;
import coda.gui.CoDaPackMain;
import java.awt.BorderLayout;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author mcomas
 */
public class DeleteMenu extends JDialog{
    public static final long serialVersionUID = 1L;
    BoxDataSelector ds;
    DataFrame df;
    public DeleteMenu(final CoDaPackMain mainApp){
        super(mainApp, "Delete variables");

        Point p = mainApp.getLocation();
        p.x = p.x + (mainApp.getWidth()-520)/2;
        p.y = p.y + (mainApp.getHeight()-430)/2;
        setLocation(p);
        
        setSize(190,370);
        df = mainApp.getActiveDataFrame();
        ds = new BoxDataSelector(df);
        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(ds, BorderLayout.CENTER);

        JButton accept = new JButton("Accept");
        getContentPane().add(accept, BorderLayout.SOUTH);
        accept.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String selected[] = ds.getSelectedData();
                int n = selected.length;
                for(int i=0;i<n;i++){
                    try {
                        df.remove(selected[i]);
                    } catch (DataFrame.DataFrameException ex) {
                        Logger.getLogger(DeleteMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                mainApp.updateDataFrame(df);
                setVisible(false);
            }
            
        });
    }
    @Override
    public void setVisible(boolean v){
        if(df == null){
            JOptionPane.showMessageDialog(this, "No data available");
            this.dispose();
        }else{
            super.setVisible(v);
        }
    }
}
