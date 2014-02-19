/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot2.objects;

import coda.plot.AbstractCoDaDisplay.LegendItem;
import coda.plot2.*;
import coda.CoDaStats;
import coda.plot.CoDa3dDisplay;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import javax.swing.JFrame;

/**
 *
 * @author marc
 */
public class Ternary3dSurfaceObject implements Ternary3dObject{
    private double data[][][];
    private double data0[][][];
    private double dataset[][][];

    private CoDa3dDisplay display;
    private Color color;
    public Ternary3dSurfaceObject(CoDa3dDisplay display, double dataset[][][]){
        this.display = display;
        this.dataset = dataset;
        int nx = dataset.length;
        int ny = dataset[0].length;
        data0 = new double[nx][ny][];
        data = new double[nx][ny][3];
        for(int i=0;i<nx;i++){
            for(int j=0;j<ny;j++){
                data0[i][j] = CoDaStats.ternaryTransform(
                        dataset[i][j][0], dataset[i][j][1], dataset[i][j][2], dataset[i][j][3]);
            }
        }
    }

    public void plotObject(Graphics2D g2) {
        g2.setColor( color );
        g2.setStroke(new BasicStroke(1.0f ,
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND));
        //Path2D.Double path = new Path2D.Double();
        Point2D from = null, to = null;
        AffineTransform affine = display.getGeometry();

        for(int i=0;i<data.length;i++){
            for(int j=1;j<data[0].length;j++){
                from = affine.transform(new Point2D.Double(data[i][j-1][0],data[i][j-1][1]), from);
                to = affine.transform(new Point2D.Double(data[i][j][0],data[i][j][1]), to);
                g2.draw(PlotUtils.drawLine(from, to));
            }
        }


        for(int j=0;j<data[0].length;j++){
            for(int i=1;i<data.length;i++){
                from = affine.transform(new Point2D.Double(data[i-1][j][0],data[i-1][j][1]), from);
                to = affine.transform(new Point2D.Double(data[i][j][0],data[i][j][1]), to);
                g2.draw(PlotUtils.drawLine(from, to));
            }
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void transformObject(CoDa3dDisplay display) {
        for(int i=0;i<data.length;i++)
            for(int j=0;j<data[0].length;j++)
                data[i][j] = display.transform(data0[i][j][0], data0[i][j][1], data0[i][j][2], data[i][j]);
    }

    public void perturbeObject(double[] x) {
        for(int i=0;i<dataset.length;i++)
            for(int j=0;j<dataset[0].length;j++)
                data0[i][j] = CoDaStats.ternaryTransform(
                    x[0] * dataset[i][j][0],
                    x[1] * dataset[i][j][1],
                    x[2] * dataset[i][j][2],
                    x[3] * dataset[i][j][3]);
    }

    public void powerObject(double t) {
        for(int i=0;i<dataset.length;i++)
            for(int j=0;j<dataset[0].length;j++)
                data0[i][j] = CoDaStats.ternaryTransform(
                    Math.pow(dataset[i][j][0], t),
                    Math.pow(dataset[i][j][1], t),
                    Math.pow(dataset[i][j][2], t),
                    Math.pow(dataset[i][j][3], t));
    }

    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public LegendItem getLegendItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double[] getCenter() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setParameters(JFrame f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
