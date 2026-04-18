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

package coda.plot2.objects;

import coda.plot.AbstractCoDaDisplay.LegendItem;
import coda.plot2.*;
import coda.CoDaStats;
import coda.plot.CoDa2dDisplay;
import coda.plot.CoDaDisplayConfiguration;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

/**
 *
 * @author marc
 */
public class Ternary2dGridObject implements Ternary2dObject{
    protected double[] definedGrid;

    private final double[][][] origGrid;
    private double[][][] grid;
    private CoDa2dDisplay display;
    private Color color;
    double center[] = {1,1,1};
    boolean isVisible = false;
    CoDaDisplayConfiguration config;
    public Ternary2dGridObject(CoDa2dDisplay display, double definedGrid[]){
        config = new CoDaDisplayConfiguration();
        this.display = display;
        this.definedGrid = definedGrid;
        
        origGrid = new double[3*definedGrid.length][2][];
        grid = new double[3*definedGrid.length][2][2];
        setGrid(1,1,1);
    }

    public final void setGrid(double x, double y, double z){
        for(int i=0;i<definedGrid.length;i++){

            origGrid[3*i][0] = CoDaStats.ternaryTransform(
                    x*definedGrid[i], y-y*definedGrid[i], 0);

            origGrid[3*i][1] = CoDaStats.ternaryTransform(
                    x*definedGrid[i], 0, z-z*definedGrid[i]);

            origGrid[3*i+1][0] = CoDaStats.ternaryTransform(
                    0, y*definedGrid[i], z-z*definedGrid[i]);

            origGrid[3*i+1][1] = CoDaStats.ternaryTransform(
                    x-x*definedGrid[i], y*definedGrid[i], 0);

            origGrid[3*i+2][0] = CoDaStats.ternaryTransform(
                    x-x*definedGrid[i], 0, z*definedGrid[i]);

            origGrid[3*i+2][1] = CoDaStats.ternaryTransform(
                    0, y-y*definedGrid[i], z*definedGrid[i]);
        }
    }
    public void plotObject(Graphics2D g2) {
        if(isVisible){
            g2.setColor( config.getColor("grid") );
            g2.setStroke(new BasicStroke( config.getSize("grid"),
                       BasicStroke.JOIN_MITER,
                       BasicStroke.CAP_ROUND));
            Point2D o1 = null, o2 = null, o3 = null;
            AffineTransform affine = display.getGeometry();
            for(int i=0;i<grid.length;i++){
                o1 = affine.transform(new Point2D.Double(grid[i][0][0], grid[i][0][1]), o1);
                o2 = affine.transform(new Point2D.Double(grid[i][1][0], grid[i][1][1]), o2);
                g2.draw(PlotUtils.drawLine(o1, o2));
            }
            Font font = new Font("Monospace", Font.PLAIN, 10);
            g2.setFont(font);
            g2.setColor( Color.black );
            FontMetrics metric = g2.getFontMetrics();


            double x[] = display.getVX();
            double y[] = display.getVY();
            double det = x[0] * y[1] - x[1] * y[0];



            int index = 0;

            if(Math.abs(x[1]) < 0.001) index = 0;
            else if(x[0] / x[1] > 0) index = det > 0 ? 1 : 2;
            else index = det > 0 ? 2 : 1;


            double a = grid[index][1][0] - grid[index][0][0];
            double b = grid[index][1][1] - grid[index][0][1];

            double norm = Math.sqrt(a*a + b*b);

            double sep = a/norm;
            for(int j=0;j<definedGrid.length;j++){
                String val1 = Double.toString(definedGrid[j]);
                String val2 = Double.toString(definedGrid[definedGrid.length-j-1]);

                    o1 = affine.transform(new Point2D.Double(grid[3*j+index][0][0] - 0.05*sep, grid[3*j+index][0][1]), o1);
                    o2 = affine.transform(new Point2D.Double(grid[3*j+index][1][0] + 0.05*sep, grid[3*j+index][1][1]), o2);


                    g2.drawString(val1,
                        (int)o1.getX() - metric.stringWidth(val2)/2,
                        (int)o1.getY());
                    g2.drawString(val2,
                        (int)o2.getX() - metric.stringWidth(val2)/2,
                        (int)o2.getY());
            }
        }
    }
    
    public void setColor(Color color) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void transformObject(coda.plot.CoDa2dDisplay display) {
        for(int i=0;i<grid.length;i++){
            grid[i][0] = display.transform(origGrid[i][0][0], origGrid[i][0][1], grid[i][0]);
            grid[i][1] = display.transform(origGrid[i][1][0], origGrid[i][1][1], grid[i][1]);
        }
    }

    public void perturbeObject(double[] x) {
        center = x;
        setGrid(x[0], x[1], x[2]);
    }

    public void powerObject(double t) {
       
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public LegendItem getLegendItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double[] getCenter() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return("Grid");
    }

    public void setParameters(JFrame f){
        final JDialog jd = new JDialog(f,  true);
        final ColorPanel selectedColor = new ColorPanel(
                config.getColor("grid"));
        final JTextField jSize = new JTextField(Float.toString(
                config.getSize("grid")));
        jd.setSize(300,300);
        JPanel propertyPanel = new JPanel();
        propertyPanel.add(new JLabel("Color:"));
        
        selectedColor.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        selectedColor.addMouseListener(selectedColor);
        selectedColor.setBackground(this.color);
        selectedColor.setPreferredSize(new Dimension(50,20));
        propertyPanel.add(selectedColor);
        propertyPanel.add(new JLabel("Size:"));
        JButton jb = new JButton("Accept");
        
        jb.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                config.setColor("grid", selectedColor.getBackground());
                config.setSize("grid", Float.parseFloat(jSize.getText()));
 
                display.repaint();
                jd.dispose();
            }
        });
        propertyPanel.add(jSize);
        propertyPanel.add(jb);
        jd.getContentPane().add(propertyPanel);
        jd.setVisible(true);
    }

}
