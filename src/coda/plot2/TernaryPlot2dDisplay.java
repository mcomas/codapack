/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot2;

import coda.plot2.objects.Ternary2dObject;
import coda.plot2.objects.Ternary2dGridObject;
import coda.ext.triangle.Triangle2D;
import coda.ext.triangle.Vertex2D;
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
public class TernaryPlot2dDisplay extends coda.plot.CoDa2dDisplay{

    // Constructor parameters
    protected String textLabel[] = null;
    protected ArrayList<Ternary2dObject> visualObject = new ArrayList<Ternary2dObject>();
    
    protected boolean isCentered = false;
    protected double[] center;

    // Simple vertices    
    private final double[] oriV1 = {0, 0.57735026918962576450914878050196};
    private final double[] oriV2 = {-0.5, -0.28867513459481288225457439025098};
    private final double[] oriV3 = {0.5, -0.28867513459481288225457439025098};
    private double[][] V = new double[3][2];

    /*
     * Builder constructor for TernaryPlot2Display
     */
    public TernaryPlot2dDisplay(Ternary2dObject objects[]){
        super();
        visualObject.addAll(Arrays.asList(objects));        
    }
    public TernaryPlot2dDisplay(String names[]){
        super();
        // Builder parameters copy
        this.textLabel = names;
    }
    public TernaryPlot2dDisplay(String names[], Ternary2dObject objects[]){
        super();
        // Builder parameters copy
        this.textLabel = names;
        visualObject.addAll(Arrays.asList(objects));
    }
    public double[] getCenter(){
        double[] ones = {1, 1, 1};
        if(visualObject.size() > 1)
            return visualObject.get(1).getCenter();
        else
            return ones;
    }
    public void addCoDaObject(Ternary2dObject codaobject){
        visualObject.add(codaobject);
    }
    public void addCoDaObject(ArrayList<Ternary2dObject> codaobjects){
        for(Ternary2dObject codaobject: codaobjects)
            visualObject.add(codaobject);
    }
    public ArrayList<Ternary2dObject> getCoDaObjects(){
        return this.visualObject;
    }
    public ArrayList<Ternary2dGridObject> getGrid(){
        ArrayList<Ternary2dGridObject> grids = new ArrayList<Ternary2dGridObject>();

        for(Ternary2dObject obj :visualObject){
            if((obj instanceof Ternary2dGridObject)){
                grids.add((Ternary2dGridObject)obj);
            }
        }
        return grids;
    }
    @Override
    public void transformData(){

        V[0] = transform(oriV1[0], oriV1[1], V[0]);
        V[1] = transform(oriV2[0], oriV2[1], V[1]);
        V[2] = transform(oriV3[0], oriV3[1], V[2]);
        if(! visualObject.isEmpty()){
            for(Ternary2dObject vo : visualObject){
                vo.transformObject(this);
            }
        }
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
        if(! visualObject.isEmpty()){
            for(Ternary2dObject vo : visualObject){
                vo.plotObject(g2);
            }
        }
        if(textLabel != null)
            drawLabels(g2);

        drawAxis(g2);
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
        g2.setPaint( CoDaDisplayConfiguration.getColor("area"));
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
    public void drawLabels(Graphics2D g2){
        Font font = new Font("Monospace", Font.PLAIN, 15);
        g2.setFont(font);
        g2.setColor(Color.black);
        FontMetrics metric = g2.getFontMetrics();

        int separation = 4;
        double sep[][] = new double[3][2];
        for(int i=0;i<3;i++){
            sep[i][1] = V[i][1] - origin[1];
        }
        for(short i=0;i<3;i++){
            String text = isCentered ? "c(" + textLabel[i] +")" : textLabel[i];
            Point2D o = defaultTransform.transform(new Point2D.Double(V[i][0],V[i][1]), null);
            if(V[i][1] - origin[1] > 0)
                g2.drawString(text,
                    (int)o.getX() - metric.stringWidth(text)/2,
                    (int)o.getY() - separation );

            else
                g2.drawString(text,
                    (int)o.getX() - metric.stringWidth(text)/2,
                    (int)o.getY() + metric.getHeight() + separation);
    
        }
    }
    public void perturbate(double perturbation[]){
        for(Ternary2dObject object :visualObject){
            object.perturbeObject(perturbation);
        }
    }
}
