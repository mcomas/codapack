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

/*
 * WindowTernaryPlot.java
 *
 * Created on Sep 9, 2010, 12:11:02 PM
 */
package coda.plot.window;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.plot.TernaryPlot2dDisplay;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
/**
 *
 * @author marc
 */
public class TernaryPlot2dWindow extends CoDaPlot2dWindow{
    private double vx[] = new double[2];
    private double vy[] = new double[2];
    private final double sinA = 0.86602540378443864676372317075294;
    private final double cosA = 0.5;
    private TernaryPlot2dDisplay ternaryPlot;
    private JCheckBox checkBoxGridSelector = new JCheckBox();
    private JCheckBox checkBoxCenteredSelector = new JCheckBox();
    private JCheckBox checkBoxShowCenterSelector = new JCheckBox();
    
    private JButton rotate;
    //private JButton standard;
    private JButton inverted;
    public TernaryPlot2dWindow(DataFrame dataframe, TernaryPlot2dDisplay display, String title) {
        super(dataframe, display, title);

        ternaryPlot = display;

        
        
        checkBoxGridSelector.setText("Grid");
        checkBoxGridSelector.addActionListener(new ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxGridSelectorActionPerformed(evt);
            }
        });
        checkBoxCenteredSelector.setText("Centered");
        checkBoxCenteredSelector.addActionListener(new ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxCenteredSelectorActionPerformed(evt);
            }
        });
        
        checkBoxShowCenterSelector.setText("Show Center");
        checkBoxShowCenterSelector.addActionListener(new ActionListener(){
            
            public void actionPerformed(java.awt.event.ActionEvent evt){
                checkBoxShowCenterSelectorActionPerformed(evt);
            }
        });
        
        //
        //rotate.setBorder(null);//


        
        //standard = new JButton(new ImageIcon("resources/ternary_xyz.png"));
        inverted = new JButton(new ImageIcon(getClass().getResource(CoDaPackMain.RESOURCE_PATH + "ternary_xzy.png")));
        rotate = new JButton(new ImageIcon(getClass().getResource(CoDaPackMain.RESOURCE_PATH + "rotate.png")));

        inverted.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inverted_buttonEvent(evt);
            }
        });
        JPanel controlTernaryPlot = new JPanel();        
        controlTernaryPlot.setSize(0,40);
        
        controlTernaryPlot.add(inverted);
        rotate.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotate_buttonEvent(evt);
            }
        });
        controlTernaryPlot.add(rotate);

        controlTernaryPlot.add(new JLabel(" "));
        
        controlTernaryPlot.add(checkBoxGridSelector);
        controlTernaryPlot.add(checkBoxCenteredSelector);
        controlTernaryPlot.add(checkBoxShowCenterSelector);
        particularControls1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        particularControls1.add(controlTernaryPlot);
        
        pack();
    }
    void inverted_buttonEvent(ActionEvent ev){
        vx = ternaryPlot.getVX();
        vy = ternaryPlot.getVY();
        vx[0] = -vx[0];
        vy[0] = -vy[0];
        ternaryPlot.setVX(vx[0], vx[1]);
        ternaryPlot.setVY(vy[0], vy[1]);
        ternaryPlot.repaint();
    }void rotate_buttonEvent(ActionEvent ev){
        vx = ternaryPlot.getVX();
        vy = ternaryPlot.getVY();
        double x = vx[0];
        double y = vx[1];

        vx[0] = cosA * x - sinA *  y;
        vx[1] = sinA * x + cosA *  y;
        ternaryPlot.setVX(vx[0], vx[1]);


        x = vy[0];
        y = vy[1];

        vy[0] = cosA * x - sinA *  y;
        vy[1] = sinA * x + cosA *  y;

        ternaryPlot.setVY(vy[0], vy[1]);
        ternaryPlot.repaint();
    }

    @Override
    public void repaint(){
        super.repaint();
    }
    private void checkBoxGridSelectorActionPerformed(java.awt.event.ActionEvent evt) {
        if(checkBoxGridSelector.isSelected()){
            ternaryPlot.showGrid(true);
        }else{
            ternaryPlot.showGrid(false);
        }
        ternaryPlot.repaint();
    }
    private void checkBoxCenteredSelectorActionPerformed(java.awt.event.ActionEvent evt) {
        if(checkBoxCenteredSelector.isSelected()){
           ternaryPlot.setCentered(true);
        }else{
            ternaryPlot.setCentered(false);
        }
        ternaryPlot.repaint();
    }
    
    private void checkBoxShowCenterSelectorActionPerformed(java.awt.event.ActionEvent evt){
        if(checkBoxShowCenterSelector.isSelected()){
            ternaryPlot.showCenter(true);
        }
        else{
            ternaryPlot.showCenter(false);
        }
        ternaryPlot.repaint();
    }
}
