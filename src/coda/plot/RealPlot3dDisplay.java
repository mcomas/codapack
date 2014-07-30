/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Arrays;

/**
 *
 * @author mcomas
 */
public class RealPlot3dDisplay extends CoDa3dDisplay{

    protected String[] names;
    protected double[][] oriZ;
    protected int[] groups;
    protected String[] gnames;
    protected int[] mapping;

    protected boolean showData = true;
    
    protected double[][] Z;
    protected boolean[] showZ;

    protected double[] oriV1 = {1,0,0};
    protected double[] oriV2 = {0,1,0};
    protected double[] oriV3 = {0,0,1};
    protected double[][] V = new double[3][3];
    
    String textLabel[];
    public static class RealPlot3dBuilder{
        protected String names[];
        protected double data[][];
        protected int groups[] = null;
        protected String gnames[] = new String[0];
        protected int mapping[] = null;

        public RealPlot3dBuilder(String names[], double data[][]){
            this.names = names;
            this.data = data;
            int n = data[0].length;
            mapping = new int[n];
            groups = new int[n];
            for(int i=0; i<n;i++){
                mapping[i] = i;
                groups[i] = 0;
            }
        }
        public RealPlot3dBuilder groups(int groups[], String gnames[]){
            this.groups = groups;
            this.gnames = gnames;
            return this;
        }
        public RealPlot3dBuilder mapping(int mapping[]){
            this.mapping = mapping;
            return this;
        }
        public RealPlot3dDisplay build(){
            return new RealPlot3dDisplay(this);
        }
    }
    public RealPlot3dDisplay(RealPlot3dBuilder builder){
    //public RealPlot3dDisplay(String[] names, double tZ[][]){
        super();

        this.names = builder.names;
        this.oriZ = builder.data;
        this.groups = builder.groups;
        this.gnames = builder.gnames;
        for(int i=0;i<gnames.length;i++)
            legendNames.add(new LegendItem(gnames[i], "data".concat(Integer.toString(i))));
        this.mapping = builder.mapping;

        
        int s = config.getSize("label").intValue();
        textLabel = names;
 
        int n = oriZ[0].length;
        Z = new double[n][3];
        showZ = new boolean[n];

        Arrays.fill(showZ, false);
        scaleData();

        this.rotateXY(0, -0.2);
        this.rotateXY(0.2, 0);
        
    }
    public String[] getNames(){
            return names;
    }
    public void showData(boolean showData){
        this.showData = showData;
        repaint();
    }
    private void scaleData(){
        double max_z = 0, temp;

        int n = oriZ[0].length;

        for(int i=0;i<n;i++){
            temp = Math.abs(oriZ[0][i]);
            max_z = (max_z < temp ? temp: max_z);
            temp = Math.abs(oriZ[1][i]);
            max_z = (max_z < temp ? temp: max_z);
            temp = Math.abs(oriZ[2][i]);
            max_z = (max_z < temp ? temp: max_z);
        }
        
        for(int i=0;i<n;i++){
            oriZ[0][i] = oriZ[0][i] / max_z;
            oriZ[1][i] = oriZ[1][i] / max_z ;
            oriZ[2][i] = oriZ[2][i] / max_z ;
        }
    }
    @Override
    public void transformData() {
        V[0] = transform(oriV1[0], oriV1[1], oriV1[2], V[0]);
        V[1] = transform(oriV2[0], oriV2[1], oriV2[2], V[1]);
        V[2] = transform(oriV3[0], oriV3[1], oriV3[2], V[2]);
        for(int i=0;i<Z.length;i++){
            Z[i] = transform(oriZ[0][i], oriZ[1][i], oriZ[2][i], Z[i]);
        }
    }
    @Override
    public void paintComponent(Graphics2D g2, double width, double height){
        super.paintComponent(g2, width, height);
        
        factor = 0.45 * (height < width ? height : width);        
        defaultTransform = new AffineTransform(
                factor, 0 ,
                0, -factor,
                width/2, height/2);                

        if(showData) drawData(g2);
        drawAxis(g2);
        drawLabels(g2);
    }
    public void drawAxis(Graphics2D g2){
        g2.setColor( config.getColor("axisX") );
        g2.setStroke(new BasicStroke(config.getSize("axisX"),
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND));

        Point2D o1 = null, o2 = null;
        o1 = defaultTransform.transform(new Point2D.Double(V[0][0],V[0][1]), o1);
        o2 = defaultTransform.transform(new Point2D.Double(2*origin[0]-V[0][0],2*origin[1]-V[0][1]), o2);
        g2.draw(PlotUtils.drawLine(o1,  o2));

        g2.setColor( config.getColor("axisY") );
        g2.setStroke(new BasicStroke(config.getSize("axisY"),
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND));

        o1 = defaultTransform.transform(new Point2D.Double(V[1][0],V[1][1]), o1);
        o2 = defaultTransform.transform(new Point2D.Double(2*origin[0]-V[1][0],2*origin[1]-V[1][1]), o2);
        g2.draw(PlotUtils.drawLine(o1, o2));

        g2.setColor( config.getColor("axisZ") );
        g2.setStroke(new BasicStroke(config.getSize("axisZ"),
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND));

        o1 = defaultTransform.transform(new Point2D.Double(V[2][0],V[2][1]), o1);
        o2 = defaultTransform.transform(new Point2D.Double(2*origin[0]-V[2][0],2*origin[1]-V[2][1]), o2);
        g2.draw(PlotUtils.drawLine(o1, o2));
    }
    public void drawData(Graphics2D g2){
        g2.setStroke(new BasicStroke(0.5f ,
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND));

        float s = config.getSize("data");
        Point2D o = null;
        for(int i=0;i<Z.length;i++){
            g2.setColor( config.getColor("data", groups[i]) );
            o = defaultTransform.transform(new Point2D.Double(Z[i][0],Z[i][1]), o);
            g2.fill(PlotUtils.drawPoint(o, s));
            g2.setColor( Color.black );
                g2.draw(PlotUtils.drawPoint(o, s));
            if(showZ[i] | showAllZ){
                g2.setColor( Color.BLACK );
                if(this.obsNames == null) g2.drawString(Integer.toString(mapping[i]+1), (float)o.getX()+s, (float)o.getY()-s);
                else g2.drawString(obsNames[mapping[i]], (float)o.getX()+s, (float)o.getY()-s);
            }
        }
        //g2.draw(PlotUtils.drawPoint(Z[i], size.get("data") / (float) factor));
    }
    public void drawLabels(Graphics2D g2){
        Font font = new Font("Monospace", Font.PLAIN, 15);
        g2.setFont(font);
        g2.setColor(config.getColor("label"));
        FontMetrics metric = g2.getFontMetrics();

        int separation = 4;
        Point2D o = null;
        double sep[][] = new double[3][3];
        for(int i=0;i<3;i++){
            sep[i][0] = V[i][0] - origin[0];
            sep[i][1] = V[i][1] - origin[1];
            sep[i][2] = V[i][2] - origin[2];
            double norm = Math.sqrt(sep[i][0]*sep[i][0] + sep[i][1]*sep[i][1] +
                    sep[i][2]*sep[i][2]);
            sep[i][0] /= norm;
            sep[i][1] /= norm;
        }

        if(v_x[0] != 0 || v_x[1] != 0){
            double x = metric.stringWidth(textLabel[0]);
            double y = metric.getHeight();
            double f = Math.sqrt(x*x + y*y);
            x /= f;
            y /= f;
            sep[0][0] *= 0.1 * x;
            sep[0][1] *= 0.1 * y;
            o = defaultTransform.transform(new Point2D.Double(V[0][0] + sep[0][0],V[0][1] + sep[0][1]), o);
            g2.drawString(textLabel[0],
                    (int)o.getX() - metric.stringWidth(textLabel[0])/2,
                    (int)o.getY() + metric.getHeight()/2);
        }
        if(v_y[0] != 0 || v_y[1] != 0){
            double x = metric.stringWidth(textLabel[1]);
            double y = metric.getHeight();
            double f = Math.sqrt(x*x + y*y);
            x /= f;
            y /= f;
            sep[1][0] *= 0.1 * x;
            sep[1][1] *= 0.1 * y;
            o = defaultTransform.transform(new Point2D.Double(V[1][0] + sep[1][0],V[1][1] + sep[1][1]), o);
            g2.drawString(textLabel[1],
                    (int)o.getX() - metric.stringWidth(textLabel[1])/2,
                    (int)o.getY() + metric.getHeight()/2);
        }
        if(v_z[0] != 0 || v_z[1] != 0){
            double x = metric.stringWidth(textLabel[2]);
            double y = metric.getHeight();
            double f = Math.sqrt(x*x + y*y);
            x /= f;
            y /= f;
            sep[2][0] *= 0.1 * x;
            sep[2][1] *= 0.1 * y;
            o = defaultTransform.transform(new Point2D.Double(V[2][0] + sep[2][0],V[2][1] + sep[2][1]), o);
            g2.drawString(textLabel[2],
                    (int)o.getX() - metric.stringWidth(textLabel[2])/2,
                    (int)o.getY() + metric.getHeight()/2);
        }

    }
    @Override
    public void mouseClicked(MouseEvent me) {
        if(me.getClickCount() == 2){
            Point2D o = null;
            int minIndex = -1;
            double minValue = 10000;
            double x;
            double y;
            for(int i=0;i<Z.length;i++){
                o = defaultTransform.transform(
                        new Point2D.Double(Z[i][0],Z[i][1]), o);
                x = o.getX() - me.getX();
                y = o.getY() - me.getY();
                if(x*x + y*y < minValue){
                    minIndex = i;
                    minValue = x*x + y*y;
                }
            }
            if(minValue < 15) showZ[minIndex] = !showZ[minIndex];
            //System.out.println(minIndex + 1);
            //System.out.println(minValue);
            //System.out.println(me.getX() + " " + me.getY());
        }
    }
}
