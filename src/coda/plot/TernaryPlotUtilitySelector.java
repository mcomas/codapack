/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot;

import coda.CoDaStats;
import coda.ext.triangle.Triangle2D;
import coda.ext.triangle.Vertex2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author marc
 */
public class TernaryPlotUtilitySelector extends CoDa2dDisplay{
    // Constructor parameters
    private double definedGrid[] = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
    private double[][][] grid;
    String textLabel[] = {"C1", "C2", "C3"};
    // Simple vertices
    private final double[] oriV1 = {0, 0.57735026918962576450914878050196};
    private final double[] oriV2 = {-0.5, -0.28867513459481288225457439025098};
    private final double[] oriV3 = {0.5, -0.28867513459481288225457439025098};
    private double[][] V = new double[3][2];

    public TernaryPlotUtilitySelector(){
        grid = new double[3*definedGrid.length][2][2];
        setGrid(null);
    }
    public final void setGrid(double[] center){
        for(int i=0;i<definedGrid.length;i++){
            grid[3*i][0] = CoDaStats.ternaryTransform(
                    (center==null ? definedGrid[i] : definedGrid[i]/center[0]),
                    (center==null ? 1-definedGrid[i] : (1-definedGrid[i])/center[1]),
                    0);
            grid[3*i][1] = CoDaStats.ternaryTransform(
                    (center==null ? definedGrid[i] : definedGrid[i]/center[0]),
                    0,
                    (center==null ? 1-definedGrid[i] : (1-definedGrid[i])/center[2]));

            grid[3*i+1][0] = CoDaStats.ternaryTransform(
                    (center==null ? 1-definedGrid[i] : (1-definedGrid[i])/center[0]),
                    (center==null ? definedGrid[i] : definedGrid[i]/center[1]),
                    0);
            grid[3*i+1][1] = CoDaStats.ternaryTransform(
                    0,
                    (center==null ? definedGrid[i] : definedGrid[i]/center[1]),
                    (center==null ? 1-definedGrid[i] : (1-definedGrid[i])/center[2]));

            grid[3*i+2][0] = CoDaStats.ternaryTransform(
                    0,
                    (center==null ? 1-definedGrid[i] : (1-definedGrid[i])/center[1]),
                    (center==null ? definedGrid[i] : definedGrid[i]/center[2]));
            grid[3*i+2][1] = CoDaStats.ternaryTransform(
                    (center==null ? 1-definedGrid[i] : (1-definedGrid[i])/center[0]),
                    0,
                    (center==null ? definedGrid[i] : definedGrid[i]/center[2]));
        }
    }
    public void drawAxis(Graphics2D g2){
        g2.setColor( CoDaDisplayConfiguration.getColor("axis") );
        g2.setStroke(new BasicStroke(CoDaDisplayConfiguration.getSize("axis"),
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND));

        Point2D o1 = null, o2 = null, o3 = null;
        o1 = defaultTransform.transform(new Point2D.Double(V[0][0], V[0][1]), o1);
        o2 = defaultTransform.transform(new Point2D.Double(V[1][0], V[1][1]), o2);
        o3 = defaultTransform.transform(new Point2D.Double(V[2][0], V[2][1]), o3);
        g2.draw(PlotUtils.drawLine(o1, o2));
        g2.draw(PlotUtils.drawLine(o2, o3));
        g2.draw(PlotUtils.drawLine(o3, o1));
    }

    public void drawAreas(Graphics2D g2){
        g2.setPaint( CoDaDisplayConfiguration.getColor("area") );
        Point2D o1 = null, o2 = null, o3 = null;
        o1 = defaultTransform.transform(new Point2D.Double(V[0][0], V[0][1]), o1);
        o2 = defaultTransform.transform(new Point2D.Double(V[1][0], V[1][1]), o2);
        o3 = defaultTransform.transform(new Point2D.Double(V[2][0], V[2][1]), o3);
        Triangle2D border = new Triangle2D(
                new Vertex2D(o1),
                new Vertex2D(o2),
                new Vertex2D(o3));
        g2.fill(border);
    }


    public void drawCoDaLine(Graphics2D g2, double a[], double b[]){
        double x[] = new double[3];
        System.arraycopy(a, 0, x, 0, 3);

        //Path2D.Double line = new Path2D.Double();
        Point2D o = null;


        double y[] = CoDaStats.ternaryTransform(x[0],x[1], x[2]);
        o = defaultTransform.transform(new Point2D.Double(y[0],y[1]), o);
        //line.moveTo(o.getX(), o.getY());
        for(double step=0; step<1; step += 0.01){
            x[0] = Math.pow(a[0],1-step) * Math.pow(b[0], step);
            x[1] = Math.pow(a[1],1-step) * Math.pow(b[1], step);
            x[2] = Math.pow(a[2],1-step) * Math.pow(b[2], step);
            y = CoDaStats.ternaryTransform(x[0],x[1], x[2]);
            o = defaultTransform.transform(new Point2D.Double(y[0],y[1]), o);
            //line.lineTo(o.getX(), o.getY());
        }
        //g2.draw(line);
    }
    public void drawTriangle(Graphics2D g2, double a[], double b[], double c[]){
        g2.setColor( Color.BLACK );
        g2.setStroke(new BasicStroke(1.5f,
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND));
        drawCoDaLine(g2, a, b);
        drawCoDaLine(g2, b, c);
        drawCoDaLine(g2, c, a);
    }
    @Override
    public void paintComponent(Graphics2D g2, double width, double height){
        super.paintComponent(g2, width, height);


        factor = 0.90 * (height < width ? height : width);

        defaultTransform = new AffineTransform(
                factor, 0 ,
                0, -factor,
                width/2, height/1.6);

        drawAreas(g2);
        drawAxis(g2);

        //drawGrid(g2);
        drawLabels(g2);
        /*double a[] = {Math.E,1,1};
        double b[] = {1,Math.E,1};
        double c[] = {1,1,Math.E};

        double per[] = {1,3,1};
        Composition P = new Composition(per);
        Composition A = new Composition(a);
        Composition B = new Composition(b);
        Composition C = new Composition(c);
        A = A.power(2).perturbate(P);
        B = B.power(2).perturbate(P);
        C = C.power(2).perturbate(P);*/
        double a[] = {0.04,0.48,0.48};
        double b[] = {0.3,0.3,0.4};
        double c[] = {0.3,0.4,0.3};
        double d[] = {0.1,0.7,0.2};
        double e[] = {0.1,0.2,0.7};
        //drawTriangle(g2, a, b, c);
        double t1[] = CoDaStats.ternaryTransform3(b);
        double t2[] = CoDaStats.ternaryTransform3(c);
        double t3[] = CoDaStats.ternaryTransform3(d);
        double t4[] = CoDaStats.ternaryTransform3(e);
        double t5[] = CoDaStats.ternaryTransform3(a);
        Point2D o1 = null, o2 = null;

        o1 = defaultTransform.transform(new Point2D.Double(t1[0], t1[1]), o1);
        o2 = defaultTransform.transform(new Point2D.Double(t2[0], t2[1]), o2);
        g2.draw(PlotUtils.drawLine(o1, o2));

        o1 = defaultTransform.transform(new Point2D.Double(t2[0], t2[1]), o1);
        o2 = defaultTransform.transform(new Point2D.Double(t3[0], t3[1]), o2);
        g2.draw(PlotUtils.drawLine(o1, o2));

        o1 = defaultTransform.transform(new Point2D.Double(t3[0], t3[1]), o1);
        o2 = defaultTransform.transform(new Point2D.Double(t5[0], t5[1]), o2);
        g2.draw(PlotUtils.drawLine(o1, o2));

        o1 = defaultTransform.transform(new Point2D.Double(t5[0], t5[1]), o1);
        o2 = defaultTransform.transform(new Point2D.Double(t4[0], t4[1]), o2);
        g2.draw(PlotUtils.drawLine(o1, o2));

        o1 = defaultTransform.transform(new Point2D.Double(t4[0], t4[1]), o1);
        o2 = defaultTransform.transform(new Point2D.Double(t1[0], t1[1]), o2);
        g2.draw(PlotUtils.drawLine(o1, o2));

        //drawTriangle(g2, a, c, d);
        //drawTriangle(g2, a, b, e);

    }
    public void drawLabels(Graphics2D g2){
        Font font = new Font("Monospace", Font.PLAIN, 15);
        g2.setFont(font);
        FontMetrics metric = g2.getFontMetrics();

        int separation = 4;
        double sep[][] = new double[3][2];
        for(int i=0;i<3;i++){
            sep[i][1] = V[i][1] - origin[1];
        }
        for(short i=0;i<3;i++){
            Point2D o = defaultTransform.transform(new Point2D.Double(V[i][0],V[i][1]), null);
            if(V[i][1] - origin[1] > 0)
                g2.drawString(textLabel[i],
                    (int)o.getX() - metric.stringWidth(textLabel[i])/2,
                    (int)o.getY() - separation );

            else
                g2.drawString(textLabel[i],
                    (int)o.getX() - metric.stringWidth(textLabel[i])/2,
                    (int)o.getY() + metric.getHeight() + separation);

        }
    }
    public void drawGrid(Graphics2D g2){
        g2.setColor( CoDaDisplayConfiguration.getColor("grid") );
        g2.setStroke(new BasicStroke( CoDaDisplayConfiguration.getSize("grid"),
                   BasicStroke.JOIN_MITER,
                   BasicStroke.CAP_ROUND));
        Point2D o1 = null, o2 = null;
        for(int i=0;i<grid.length;i++){
            o1 = defaultTransform.transform(new Point2D.Double(grid[i][0][0], grid[i][0][1]), o1);
            o2 = defaultTransform.transform(new Point2D.Double(grid[i][1][0], grid[i][1][1]), o2);
            g2.draw(PlotUtils.drawLine(o1, o2));
        }
    }
    @Override
    public void transformData() {
        V[0] = transform(oriV1[0], oriV1[1], V[0]);
        V[1] = transform(oriV2[0], oriV2[1], V[1]);
        V[2] = transform(oriV3[0], oriV3[1], V[2]);

    }
    public class CODATriangle implements Shape{

        public Rectangle getBounds() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Rectangle2D getBounds2D() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean contains(double arg0, double arg1) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean contains(Point2D arg0) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean intersects(double arg0, double arg1, double arg2, double arg3) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean intersects(Rectangle2D arg0) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean contains(double arg0, double arg1, double arg2, double arg3) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean contains(Rectangle2D arg0) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public PathIterator getPathIterator(AffineTransform arg0) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public PathIterator getPathIterator(AffineTransform arg0, double arg1) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
}
