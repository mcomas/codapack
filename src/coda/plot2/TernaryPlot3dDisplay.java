/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot2;

import coda.plot2.objects.Ternary3dObject;
import coda.ext.triangle.Triangle2D;
import coda.ext.triangle.Vertex2D;
import coda.plot.CoDa3dDisplay;
import coda.plot.CoDaDisplayConfiguration;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author marc
 */
public class TernaryPlot3dDisplay extends CoDa3dDisplay{

    protected String textLabel[];
    protected ArrayList<Ternary3dObject> visualObject = new ArrayList<Ternary3dObject>();

    protected boolean isCentered = false;
    protected double[] center;
    
    private final double[] oriV1 = {-0.2886751347, 0.5, -0.2041241452};
    private final double[] oriV2 = {0.0, 0.0, 0.612372435727726};
    private final double[] oriV3 = {0.577350269084439, 0.0,-0.2041241452};
    private final double[] oriV4 = {-0.2886751347,-0.5,-0.2041241452};
    private double[][] V = new double[4][3];



    public TernaryPlot3dDisplay(Ternary3dObject objects[]){
        super();
        visualObject.addAll(Arrays.asList(objects));
    }
    public TernaryPlot3dDisplay(String names[]){
        super();
        this.textLabel = names;
    }
    public TernaryPlot3dDisplay(String names[], Ternary3dObject objects[]){
        super();
        this.textLabel = names;
        visualObject.addAll(Arrays.asList(objects));
    }
    public void addCoDaObject(Ternary3dObject codaobject){
        visualObject.add(codaobject);
    }
    public void addCoDaObject(ArrayList<Ternary3dObject> codaobjects){
        for(Ternary3dObject codaobject: codaobjects)
            visualObject.add(codaobject);
    }
    @Override
    public void transformData(){
        V[0] = transform(oriV1[0], oriV1[1], oriV1[2], V[0]);
        V[1] = transform(oriV2[0], oriV2[1], oriV2[2], V[1]);
        V[2] = transform(oriV3[0], oriV3[1], oriV3[2], V[2]);
        V[3] = transform(oriV4[0], oriV4[1], oriV4[2], V[3]);

        if(! visualObject.isEmpty()){
            for(Ternary3dObject vo : visualObject){
                vo.transformObject(this);
            }
        }
    }
    @Override
    public void paintComponent(Graphics2D g2, double width, double height){
        super.paintComponent(g2, width, height);

        factor = 0.70 * (height < width ? height : width);

        defaultTransform = new AffineTransform(
                factor, 0 ,
                0, -factor,
                width/2.3, height/1.9);

        drawAreas(g2);               

        if(! visualObject.isEmpty()){
            for(Ternary3dObject vo : visualObject){
                vo.plotObject(g2);
            }
        }

        drawAxis(g2);

        if(textLabel != null)
            drawLabels(g2);
    }
    private double crossProduct(double x1, double x2, double y1, double y2){
        return  x1 * y2 - x2 * y1;
    }
    private boolean sameSide(double p1, double p2, double q1, double q2,
            double a1, double a2, double b1, double b2){

        return crossProduct(b1-a1, b2-a2, p1 -a1, p2-a2) *
                crossProduct(b1-a1, b2-a2, q1 -a1, q2-a2) < 0 ? false : true;
    }
    private boolean isInside(double p1, double p2, double a1, double a2,
            double b1, double b2, double c1, double c2){
        return sameSide(p1, p2, a1, a2, b1, b2, c1, c2) &&
               sameSide(p1, p2, b1, b2, a1, a2, c1, c2) &&
               sameSide(p1, p2, c1, c2, a1, a2, b1, b2);
    }
    private void drawAxis(Graphics2D g2){
        g2.setColor( config.getColor("axis") );
        BasicStroke normal = new BasicStroke(config.getSize("axis"),
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND);
        float[] dash1 = { 2f };
        BasicStroke dashed = new BasicStroke(config.getSize("axis"),
            BasicStroke.JOIN_MITER, BasicStroke.CAP_ROUND,
            1.0f, dash1, 2f);

        g2.setStroke(normal);
        int temp;
        int indexOrder[] = {0,1,2,3};
        for(int i=0;i<4;i++){
            for(int j=i+1;j<4;j++){
                if(V[indexOrder[i]][2] > V[indexOrder[j]][2]){
                    temp = indexOrder[i];
                    indexOrder[i] = indexOrder[j];
                    indexOrder[j] = temp;
                }
            }
        }        
        Point2D o[] = new Point2D[4];// = null, o1 = null, o2 = null, o3 = null;
        o[0] = defaultTransform.transform(new Point2D.Double(V[0][0], V[0][1]), o[0]);
        o[1] = defaultTransform.transform(new Point2D.Double(V[1][0], V[1][1]), o[1]);
        o[2] = defaultTransform.transform(new Point2D.Double(V[2][0], V[2][1]), o[2]);
        o[3] = defaultTransform.transform(new Point2D.Double(V[3][0], V[3][1]), o[3]);

        if(isInside(V[indexOrder[3]][0], V[indexOrder[3]][1],
                     V[indexOrder[2]][0], V[indexOrder[2]][1],
                     V[indexOrder[1]][0], V[indexOrder[1]][1],
                     V[indexOrder[0]][0], V[indexOrder[0]][1]) ){
            g2.draw(PlotUtils.drawLine(o[0], o[1]));
            g2.draw(PlotUtils.drawLine(o[0], o[2]));
            g2.draw(PlotUtils.drawLine(o[0], o[3]));
            g2.draw(PlotUtils.drawLine(o[1], o[2]));
            g2.draw(PlotUtils.drawLine(o[1], o[3]));
            g2.draw(PlotUtils.drawLine(o[2], o[3]));
        }else if(isInside(V[indexOrder[0]][0], V[indexOrder[0]][1],
                     V[indexOrder[2]][0], V[indexOrder[2]][1],
                     V[indexOrder[1]][0], V[indexOrder[1]][1],
                     V[indexOrder[3]][0], V[indexOrder[3]][1]) ){
            g2.setStroke(dashed);
            g2.draw(PlotUtils.drawLine(o[indexOrder[0]], o[indexOrder[1]]));
            g2.draw(PlotUtils.drawLine(o[indexOrder[0]], o[indexOrder[2]]));
            g2.draw(PlotUtils.drawLine(o[indexOrder[0]], o[indexOrder[3]]));
            g2.setStroke(normal);
            g2.draw(PlotUtils.drawLine(o[indexOrder[1]], o[indexOrder[2]]));
            g2.draw(PlotUtils.drawLine(o[indexOrder[1]], o[indexOrder[3]]));
            g2.draw(PlotUtils.drawLine(o[indexOrder[2]], o[indexOrder[3]]));
        }else{
            g2.setStroke(dashed);
            g2.draw(PlotUtils.drawLine(o[indexOrder[0]], o[indexOrder[1]]));
            g2.setStroke(normal);
            g2.draw(PlotUtils.drawLine(o[indexOrder[0]], o[indexOrder[2]]));
            g2.draw(PlotUtils.drawLine(o[indexOrder[0]], o[indexOrder[3]]));
            g2.draw(PlotUtils.drawLine(o[indexOrder[1]], o[indexOrder[2]]));
            g2.draw(PlotUtils.drawLine(o[indexOrder[1]], o[indexOrder[3]]));
            g2.draw(PlotUtils.drawLine(o[indexOrder[2]], o[indexOrder[3]]));
        }
    }
    private void drawAreas(Graphics2D g2){
        g2.setPaint(new Color(150,150,150,30) );//CoDaDisplayConfiguration.getColor("area"));
        Point2D o1 = null, o2 = null, o3 = null, o4 = null;
        o1 = defaultTransform.transform(new Point2D.Double(V[0][0], V[0][1]), o1);
        o2 = defaultTransform.transform(new Point2D.Double(V[1][0], V[1][1]), o2);
        o3 = defaultTransform.transform(new Point2D.Double(V[2][0], V[2][1]), o3);
        o4 = defaultTransform.transform(new Point2D.Double(V[3][0], V[3][1]), o4);
        Triangle2D border = new Triangle2D(
                new Vertex2D(o1),
                new Vertex2D(o2),
                new Vertex2D(o3));
        g2.fill(border);

        border = new Triangle2D(
                new Vertex2D(o2),
                new Vertex2D(o3),
                new Vertex2D(o4));
        g2.fill(border);

        border = new Triangle2D(
                new Vertex2D(o3),
                new Vertex2D(o4),
                new Vertex2D(o1));
        g2.fill(border);

        border = new Triangle2D(
                new Vertex2D(o4),
                new Vertex2D(o1),
                new Vertex2D(o2));
        g2.fill(border);
    }    
    private void drawLabels(Graphics2D g2){
        Font font = new Font("Monospace", Font.PLAIN, 15);
        g2.setFont(font);
        g2.setColor( Color.black );
        FontMetrics metric = g2.getFontMetrics();
        Point2D o = null;
        int separation = 4;
        double sep[][] = new double[4][3];
        for(int i=0;i<4;i++){
            sep[i][0] = V[i][0] - origin[0];
            sep[i][1] = V[i][1] - origin[1];
            sep[i][2] = V[i][2] - origin[2];
            double norm = Math.sqrt(sep[i][0]*sep[i][0] + sep[i][1]*sep[i][1] +
                    sep[i][2]*sep[i][2]);
            sep[i][0] /= norm;
            sep[i][1] /= norm;
        }
        for(short i =0;i<4;i++){
            String text = isCentered ? "c(" + textLabel[i] +")" : textLabel[i];
            double x = metric.stringWidth(text);
            double y = metric.getHeight();
            double f = Math.sqrt(x*x + y*y);
            x /= f;
            y /= f;
            
            sep[i][0] *= 0.1 * x;
            sep[i][1] *= 0.1 * y;
            o = defaultTransform.transform(new Point2D.Double(V[i][0] + sep[i][0],V[i][1] + sep[i][1]), o);
            g2.drawString(text,
                    (int)o.getX() - metric.stringWidth(text)/2,
                    (int)o.getY() + metric.getHeight()/2 );

        }
    }
}
