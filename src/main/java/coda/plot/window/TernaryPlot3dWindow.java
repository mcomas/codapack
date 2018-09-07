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
import coda.plot.TernaryPlot3dDisplay;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;


/**
 *
 * @author marc
 */
public class TernaryPlot3dWindow extends CoDaPlot3dWindow{
    private TernaryPlot3dDisplay ternaryPlot;
    
    private JCheckBox checkBoxCenteredSelector = new JCheckBox();
    private JCheckBox checkBoxShowCenterSelector = new JCheckBox();
    
    private JLabel jLabel1 = new JLabel("Zoom");
    

    public TernaryPlot3dWindow(DataFrame dataframe, TernaryPlot3dDisplay display, String title) {
        super(dataframe, display, title);

        this.ternaryPlot = display;

        

        checkBoxCenteredSelector.setText("Centered");
        checkBoxCenteredSelector.addActionListener(new ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxCenteredSelectorActionPerformed(evt);
            }
        });
        
        checkBoxShowCenterSelector.setText("Show the center");
        checkBoxShowCenterSelector.addActionListener(new ActionListener(){
            
            public void actionPerformed(java.awt.event.ActionEvent evt){
                checkBoxShowCenterSelectorActionPerformed(evt);
            }
        });

        JPanel controlTernaryPlot = new JPanel();
        controlTernaryPlot.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        controlTernaryPlot.setSize(0,40);
        controlTernaryPlot.add(checkBoxCenteredSelector);
        controlTernaryPlot.add(checkBoxShowCenterSelector);

        particularControls1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        particularControls1.add(controlTernaryPlot);
        pack();
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
    //GEN-LAST:event_sliderZoomStateChanged
    /*
    @Override
    public void keyTyped(KeyEvent ke) {
        System.out.println(ke.getKeyCode() + " " + KeyEvent.VK_CONTROL);
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        System.out.println(ke.getKeyCode() + " " + KeyEvent.VK_CONTROL);
        if(KeyEvent.VK_CONTROL == ke.getKeyCode()) ternaryPlot.control_pressed = true;
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        if(KeyEvent.VK_CONTROL == ke.getKeyCode()){
            ternaryPlot.zoomH = ternaryPlot.zoomW = ternaryPlot.zoomX = ternaryPlot.zoomY = 0;
            ternaryPlot.control_pressed = false;
            repaint();
        }
    }*/


}
