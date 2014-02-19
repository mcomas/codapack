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

        JPanel controlTernaryPlot = new JPanel();
        controlTernaryPlot.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        controlTernaryPlot.setSize(0,40);
        controlTernaryPlot.add(checkBoxCenteredSelector);

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
