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
import coda.gui.CoDaPackConf;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author mcomas
 */
public class Categoric2NumericMenu extends JDialog{
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Data.Manipulte.Categorical to Numeric.yaml";
    private static final String helpTitle = "Categorical to Numeric Help";
    
    BoxDataSelector ds;
    DataFrame df;
    public Categoric2NumericMenu(final CoDaPackMain mainApp){

        Point p = mainApp.getLocation();
        p.x = p.x + (mainApp.getWidth()-520)/2;
        p.y = p.y + (mainApp.getHeight()-430)/2;
        setLocation(p);


        setSize(190,370);
        df = mainApp.getActiveDataFrame();
        ds = new BoxDataSelector(df);
        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(ds, BorderLayout.CENTER);
        
        JPanel southPanel = new JPanel();

        JButton accept = new JButton("Accept");
        southPanel.add(accept);
        accept.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String selected[] = ds.getSelectedData();
                int n = selected.length;
                for(int i=0;i<n;i++){
                    df.get(selected[i]).toNumeric();
                }
                mainApp.updateDataFrame(df);
                setVisible(false);
            }
            
        });
        
        JButton helpButton = new JButton("Help");
        southPanel.add(helpButton);
        helpButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                JDialog dialog = new JDialog();
                HelpMenu menu;
                try {
                    menu = new HelpMenu(yamlUrl,helpTitle);
                    dialog.add(menu);
                    dialog.setSize(650, 500);
                    dialog.setTitle(helpTitle);
                    dialog.setIconImage(Toolkit.getDefaultToolkit()
                    .getImage(getClass().getResource(CoDaPackMain.RESOURCE_PATH + "logo.png")));
                    Point p = getLocation();
                    p.x = p.x + (getWidth()-520)/2;
                    p.y = p.y + (getHeight()-430)/2;
                    WindowListener exitListener = new WindowAdapter(){
                
                        @Override
                        public void windowClosing(WindowEvent e){
                                dialog.dispose();
                                menu.deleteHtml();
                        }
                    };
            
                    dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    dialog.addWindowListener(exitListener);
                    dialog.setLocation(p);
                    dialog.setVisible(true);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AbstractMenuDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        getContentPane().add(southPanel, BorderLayout.SOUTH);
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
