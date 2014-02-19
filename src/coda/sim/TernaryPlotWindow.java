/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * WindowTernaryPlot.java
 *
 * Created on Sep 9, 2010, 12:11:02 PM
 */
package coda.sim;

import coda.DataFrame;
import coda.plot.window.CoDaPlotWindow;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeListener;
/**
 *
 * @author marc
 */
public class TernaryPlotWindow extends CoDaPlotWindow{
    private TernaryPlotDisplay ternaryPlot;
    private JCheckBox checkBoxGridSelector = new JCheckBox();
    private JCheckBox checkBoxCenteredSelector = new JCheckBox();
    private JPanel controlTernaryPlot = new JPanel();
    private JLabel jLabel1 = new JLabel("Zoom");
    private JSlider sliderZoom = new JSlider();
    private JButton rotate;
    private JButton standard;
    private JButton inverted;
    public TernaryPlotWindow(DataFrame dataframe, TernaryPlotDisplay display, String title) {
        super(dataframe, display, title);

        ternaryPlot = display;

        controlTernaryPlot.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        controlTernaryPlot.setSize(0,40);
        
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
        
        standard = new JButton(new ImageIcon("resources/ternary_xyz.png"));
        inverted = new JButton(new ImageIcon("resources/ternary_xzy.png"));
        rotate = new JButton(new ImageIcon("resources/rotate.png"));

        controlTernaryPlot.add(standard);
        controlTernaryPlot.add(inverted);
        controlTernaryPlot.add(rotate);
        controlTernaryPlot.add(new JLabel("   "));
        controlTernaryPlot.add(jLabel1);
        controlTernaryPlot.add(sliderZoom);
        controlTernaryPlot.add(checkBoxGridSelector);
        controlTernaryPlot.add(checkBoxCenteredSelector);
        JPanel right = new JPanel();
        

        sliderZoom.setPreferredSize(new Dimension(100, 20));
        sliderZoom.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                sliderZoomMouseReleased(evt);
            }
        });
        sliderZoom.addChangeListener(new ChangeListener() {
            
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderZoomStateChanged(evt);
            }
        });
        getContentPane().add(controlTernaryPlot, java.awt.BorderLayout.SOUTH);
        pack();
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
    private void sliderZoomMouseReleased(java.awt.event.MouseEvent evt) {
        sliderZoom.setValue(50);
    }
    private void sliderZoomStateChanged(javax.swing.event.ChangeEvent evt) {
        double dif = ((double)sliderZoom.getValue()-50)/3000.0;
        ternaryPlot.zoom(1+dif);
        ternaryPlot.repaint();
    }

}
