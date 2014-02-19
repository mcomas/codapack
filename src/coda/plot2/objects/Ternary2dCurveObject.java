/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot2.objects;

import coda.plot.AbstractCoDaDisplay.LegendItem;
import coda.plot2.*;
import coda.CoDaStats;
import coda.ext.jama.Matrix;
import coda.plot.CoDa2dDisplay;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
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
public class Ternary2dCurveObject implements Ternary2dObject{
    private double data[][];
    private double data0[][];
    private double dataset[][];

    double center[];
    private CoDa2dDisplay display;
    private Color color = Color.BLACK;
    private float size = 1;
    public Ternary2dCurveObject(CoDa2dDisplay display, double dataset[][]){
        this.display = display;
        this.dataset = dataset;

        center = CoDaStats.center(new Matrix(dataset).transpose().getArray());
        int n = dataset.length;
        data0 = new double[n][];
        data = new double[n][2];
        for(int i=0;i<n;i++){
            data0[i] = CoDaStats.ternaryTransform(
                    dataset[i][0], dataset[i][1], dataset[i][2]);
        }
    }

    public void plotObject(Graphics2D g2) {
        g2.setColor( color );
        g2.setStroke(new BasicStroke(1.0f ,
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND));
        
        Point2D from = null, to = null;
        AffineTransform affine = display.getGeometry();
        

        for(int i=1;i<data.length;i++){
            from = affine.transform(new Point2D.Double(data[i-1][0],data[i-1][1]), from);
            to = affine.transform(new Point2D.Double(data[i][0],data[i][1]), to);
            g2.setStroke(new BasicStroke(size));
            g2.draw(PlotUtils.drawLine(from, to));

        }
    }
    public void setSize(float s){
        this.size = s;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public void transformObject(coda.plot.CoDa2dDisplay display) {
        for(int i=0;i<data.length;i++)
            data[i] = display.transform(data0[i][0], data0[i][1], data[i]);
    }

    public void perturbeObject(double[] x) {
        for(int i=0;i<dataset.length;i++)
            data0[i] = CoDaStats.ternaryTransform(
                    x[0] * dataset[i][0],
                    x[1] * dataset[i][1],
                    x[2] * dataset[i][2]);
    }

    public void powerObject(double t) {
        for(int i=0;i<dataset.length;i++)
            data0[i] = CoDaStats.ternaryTransform(
                    Math.pow(dataset[i][0], t),
                    Math.pow(dataset[i][1], t),
                    Math.pow(dataset[i][2], t));
    }

    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public LegendItem getLegendItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double[] getCenter() {
        return center;
    }
    public void setParameters(JFrame f){
        final JDialog jd = new JDialog(f,  true);
        final ColorPanel selectedColor = new ColorPanel();
        final JTextField jSize = new JTextField(Float.toString(size));
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
                setSize(Float.parseFloat(jSize.getText()));
                setColor(selectedColor.getBackground());
                display.repaint();
                jd.dispose();
            }
        });
        propertyPanel.add(jSize);
        propertyPanel.add(jb);
        jd.getContentPane().add(propertyPanel);
        jd.setVisible(true);
    }
 
    @Override
    public String toString() {
        return("Curve");
    }


  
}
