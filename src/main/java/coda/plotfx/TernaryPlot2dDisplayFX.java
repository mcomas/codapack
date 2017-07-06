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

package coda.plotfx;

import coda.plotfx.objects.Ternary2dObjectFX;
import coda.plotfx.objects.Ternary2dGridObjectFX;
import coda.ext.triangle.Triangle2D;
import coda.ext.triangle.Vertex2D;
import coda.plot.CoDaDisplayConfiguration;
import coda.plot2.PlotUtils;

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
public class TernaryPlot2dDisplayFX extends coda.plotfx.CoDa2dDisplayFX{

    // Constructor parameters
    protected String textLabel[] = null;
    protected ArrayList<Ternary2dObjectFX> visualObject = new ArrayList<Ternary2dObjectFX>();
    
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
    public TernaryPlot2dDisplayFX(Ternary2dObjectFX objects[]){
        super();
        visualObject.addAll(Arrays.asList(objects));        
    }
    public TernaryPlot2dDisplayFX(String names[]){
        super();
        // Builder parameters copy
        this.textLabel = names;
    }
    public TernaryPlot2dDisplayFX(String names[], Ternary2dObjectFX objects[]){
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
    public void addCoDaObject(Ternary2dObjectFX codaobject){
        visualObject.add(codaobject);
    }
    public void addCoDaObject(ArrayList<Ternary2dObjectFX> codaobjects){
        for(Ternary2dObjectFX codaobject: codaobjects)
            visualObject.add(codaobject);
    }
    public ArrayList<Ternary2dObjectFX> getCoDaObjects(){
        return this.visualObject;
    }
    public ArrayList<Ternary2dGridObjectFX> getGrid(){
        ArrayList<Ternary2dGridObjectFX> grids = new ArrayList<Ternary2dGridObjectFX>();

        for(Ternary2dObjectFX obj :visualObject){
            if((obj instanceof Ternary2dGridObjectFX)){
                grids.add((Ternary2dGridObjectFX)obj);
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
            for(Ternary2dObjectFX vo : visualObject){
                vo.transformObject(this);
            }
        }
    }
    @Override
    public void paintComponent(double width, double height){
        super.paintComponent(width, height);
        

        factor = 0.90 * (height < width ? height : width);
        defaultTransform = new AffineTransform(
                factor, 0 ,
                0, -factor,
                width/2, height/1.6);
        
        drawAreas();    
        if(! visualObject.isEmpty()){
            for(Ternary2dObjectFX vo : visualObject){
                vo.plotObject(g2);
            }
        }
        if(textLabel != null)
            drawLabels();

        drawAxis();
    }
    public void drawAxis(){
        g2.setColor( config.getColor("axis") );
        g2.setStroke(new BasicStroke(config.getSize("axis"),
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
    public void drawAreas(){
        g2.setPaint( config.getColor("area"));
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
    public void drawLabels(){
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
        for(Ternary2dObjectFX object :visualObject){
            object.perturbeObject(perturbation);
        }
    }
}
