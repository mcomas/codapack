/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot2.objects;

import coda.plot.AbstractCoDaDisplay.LegendItem;
import coda.plot2.datafig.FilledCircle;
import coda.plot2.datafig.CoDaShape;
import coda.CoDaStats;
import coda.ext.jama.Matrix;
import coda.plot.CoDa2dDisplay;
import coda.plot.CoDaDisplayConfiguration;
import coda.plot2.ColorPanel;
import coda.plot2.ShapePanel;
import coda.plot2.window.TernaryPlot2dDialogDataSet;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
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
public final class Ternary2dDataObject implements Ternary2dObject{
    private double data[][];
    private double data0[][];
    private double dataset[][];
    private String labels[] = null;
    private CoDa2dDisplay display;
    private boolean showData[];
    double center[];
    float size = 1;
    private CoDaShape shape;
    ShapePanel selectedShape;
    CoDaDisplayConfiguration config;
    public void commonIni(CoDa2dDisplay display, double dataset[][]){
        config = new CoDaDisplayConfiguration();
        this.display = display;
        this.dataset = dataset;
        this.shape = new FilledCircle();

        center = CoDaStats.center(new Matrix(dataset).transpose().getArray());
        int n = dataset.length;
        data0 = new double[n][];
        data = new double[n][2];
        showData = new boolean[n];
        for(int i=0;i<n;i++){
            data0[i] = CoDaStats.ternaryTransform(
                    dataset[i][0], dataset[i][1], dataset[i][2]);
        }
    }
    public Ternary2dDataObject(CoDa2dDisplay display, double dataset[][]){
        commonIni(display, dataset);
    }
    public Ternary2dDataObject(CoDa2dDisplay display, double dataset[][], CoDaShape shape){
        commonIni(display, dataset);
        this.shape = shape;
    }
    public void setLabels(String labels[]){
        this.labels = labels;
    }
    public void setColor(Color color){
        shape.setColor(color);
    }
    public void setShape(CoDaShape shape){
        this.shape = shape;
    }
    public void setSize(float size){
        this.size = size;
    }
    public void plotObject(Graphics2D g2) {        
        g2.setStroke(new BasicStroke(0.5f ,
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND));
        float s = config.getSize("data");
        Point2D o = null;
        for(int i=0;i<data.length;i++){            
            o = display.getGeometry().transform(new Point2D.Double(data[i][0], data[i][1]), o);
            shape.plot(g2, o);
            //g2.fill(PlotUtils.drawPoint(o, s));
            //g2.setColor( Color.black );
            //g2.draw(PlotUtils.drawPoint(o, s));
        }
    }
    public void transformObject(coda.plot.CoDa2dDisplay display) {
        for(int i=0;i<data.length;i++){
                data[i] = display.transform(data0[i][0], data0[i][1], data[i]);
        }
    }

    public void perturbeObject(double[] x) {
        for(int i=0;i<dataset.length;i++)
            data0[i] = CoDaStats.ternaryTransform(
                    x[0] * dataset[i][0],
                    x[1] * dataset[i][1],
                    x[2] * dataset[i][2]);
    }

    public void powerObject(double t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public void showLabel(coda.plot.CoDa2dDisplay display, int X ,int Y){
        Point2D o = null;
            int minIndex = -1;
            double minValue = 10000;
            double x;
            double y;
            AffineTransform transform = display.getGeometry();
            for(int i=0;i<data.length;i++){
                o = transform.transform(
                        new Point2D.Double(data[i][0],data[i][1]), o);
                x = o.getX() - X;
                y = o.getY() - Y;
                if(x*x + y*y < minValue){
                    minIndex = i;
                    minValue = x*x + y*y;
                }
            }
            if(minValue < 15) showData[minIndex] = !showData[minIndex];
    }

    public LegendItem getLegendItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double[] getCenter() {
        return center;
    }

    @Override
    public String toString() {
        return("Data");
    }

    public void setParameters(JFrame f){
        final JDialog jd = new JDialog(f,  true);
        final ColorPanel selectedColor = new ColorPanel();
        final ShapePanel selectedPanel = new ShapePanel();
        jd.setSize(300,300);
        JPanel propertyPanel = new JPanel();
        propertyPanel.add(new JLabel("Color:"));
        
        selectedColor.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        selectedColor.addMouseListener(selectedColor);
        selectedColor.setBackground(shape.getColor());
        selectedColor.setPreferredSize(new Dimension(50,20));
        propertyPanel.add(selectedColor);
        
        selectedShape = new ShapePanel(shape);
        selectedShape.addItemListener(selectedShape);
        propertyPanel.add(selectedShape);
        
        JButton jb = new JButton("Accept");
        
        jb.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                
                setColor(selectedColor.getBackground());
                CoDaShape sh = selectedShape.getShape();
                sh.setColor(selectedColor.getBackground());
                setShape(sh);
                display.repaint();
                jd.dispose();
            }
        });
        
        propertyPanel.add(jb);
        jd.getContentPane().add(propertyPanel);
        jd.setVisible(true);
    }
}
