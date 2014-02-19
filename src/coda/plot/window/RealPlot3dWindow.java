/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot.window;

import coda.DataFrame;
import coda.plot.RealPlot3dDisplay;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author mcomas
 */
public class RealPlot3dWindow extends CoDaPlot3dWindow{
    private RealPlot3dDisplay realPlot;

    private JButton xy_view;
    private JButton yz_view;
    private JButton zx_view;
    public RealPlot3dWindow(DataFrame dataframe, RealPlot3dDisplay display, String title){
        super(dataframe, display, title);
        this.realPlot = display;

        xy_view = new JButton("XY");
        yz_view = new JButton("YZ");
        zx_view = new JButton("XZ");

        xy_view.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xy_buttonEvent(evt);
            }
        });
        yz_view.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yz_buttonEvent(evt);
            }
        });
        zx_view.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zx_buttonEvent(evt);
            }
        });
        JPanel view = new JPanel();
        view.add(xy_view);
        view.add(yz_view);
        view.add(zx_view);
        particularControls1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        particularControls1.add(view);
    }
    void xy_buttonEvent(ActionEvent ev){
        realPlot.setVX(1, 0, 0);
        realPlot.setVY(0, 1, 0);
        realPlot.setVZ(0, 0, 1);
        realPlot.repaint();
    }
    void yz_buttonEvent(ActionEvent ev){
        realPlot.setVX(0, 0, 1);
        realPlot.setVY(1, 0, 0);
        realPlot.setVZ(0, 1, 0);
        realPlot.repaint();
    }
    void zx_buttonEvent(ActionEvent ev){
        realPlot.setVX(1, 0, 0);
        realPlot.setVY(0, 0, 1);
        realPlot.setVZ(0, 1, 0);
        realPlot.repaint();
    }
    public void setCoordinate(double coord[][]){
        realPlot.setVX(coord[0][0], coord[0][1], coord[0][2]);
        realPlot.setVY(coord[1][0], coord[1][1], coord[1][2]);
        realPlot.setVZ(coord[2][0], coord[2][1], coord[2][2]);
        realPlot.repaint();
    }
}
