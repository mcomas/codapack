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

package coda.plot;

import coda.CoDaStats;
import coda.ext.triangle.Triangle2D;
import coda.ext.triangle.Vertex2D;

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
 * @author marc
 */
public class TernaryPlot2dDisplay extends CoDa2dDisplay{    

    // Constructor parameters
    protected String[] names;
    protected double[][] data;
    protected String[] gnames;
    protected int[] groups;
    protected int[] mapping;
    protected double[] definedGrid;
    protected double[] centerCalculated = new double[2];

    protected final double[][] origZ;
    protected double[][] Z;
    protected boolean[] showZ;

    protected boolean isCentered = false;
    protected boolean showCenter = false;
    protected double[] center;

    // Simple vertices    
    private final double[] oriV1 = {0, 0.57735026918962576450914878050196};
    private final double[] oriV2 = {-0.5, -0.28867513459481288225457439025098};
    private final double[] oriV3 = {0.5, -0.28867513459481288225457439025098};
    private double[][] V = new double[3][2];

    private final double[][][] origGrid;
    private double[][][] grid;
    private boolean showGrid = false;

    //private TeXIcon iconLabel[];
    String textLabel[];
    /*
     * Builder constructor for TernaryPlot2Display
     */
    public static class TernaryPlot2dBuilder{
        private String names[];
        private double data[][];
        private int groups[] = null;
        private String gnames[] = new String[0];
        private int mapping[] = null;
        private double definedGrid[] = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
        public TernaryPlot2dBuilder(String names[], double data[][]){
            this.names = names;
            this.data = data;
            mapping = new int[data[0].length];
            groups = new int[data[0].length];
            for(int i=0; i<data.length;i++){
                mapping[i] = i;
                groups[i] = 0;
            }
        }
        public String[] getNames(){
            return names;
        }
        public TernaryPlot2dBuilder groups(int groups[], String gnames[]){
            this.groups = groups;
            this.gnames = gnames;
            return this;
        }
        public TernaryPlot2dBuilder mapping(int mapping[]){
            this.mapping = mapping;
            return this;
        }
        public TernaryPlot2dBuilder grid(double definedGrid[]){
            this.definedGrid = definedGrid;
            return this;
        }
        public TernaryPlot2dDisplay build(){
            return new TernaryPlot2dDisplay(this);
        }
    }
    protected TernaryPlot2dDisplay(TernaryPlot2dBuilder builder){
        super();
        // Builder parameters copy
        names = builder.names;
        data = builder.data;
        groups = builder.groups;
        gnames = builder.gnames;
        for(int i=0;i<gnames.length;i++)
            legendNames.add(new LegendItem(gnames[i], "data".concat(Integer.toString(i))));
        mapping = builder.mapping;
        definedGrid = builder.definedGrid;

        // Initialization variables
        textLabel = names;
        int s = config.getSize("label").intValue();

        center = CoDaStats.center(data);

        origGrid = new double[3*definedGrid.length][2][];
        grid = new double[3*definedGrid.length][2][2];
        setGrid(null);


        int n = data[0].length;
        showZ = new boolean[n];
        origZ = new double[n][];
        Z = new double[n][2];
        Arrays.fill(showZ, false);
        for(int i=0;i<n;i++){
            origZ[i] = CoDaStats.ternaryTransform(
                    data[0][i], data[1][i], data[2][i]);
        }
    }
    public void showGrid(boolean show){
        showGrid = show;
    }
    public final void setGrid(double[] center){
        for(int i=0;i<definedGrid.length;i++){
            origGrid[3*i][0] = CoDaStats.ternaryTransform(
                    (center==null ? definedGrid[i] : definedGrid[i]/center[0]),
                    (center==null ? 1-definedGrid[i] : (1-definedGrid[i])/center[1]),
                    0);
            origGrid[3*i][1] = CoDaStats.ternaryTransform(
                    (center==null ? definedGrid[i] : definedGrid[i]/center[0]),
                    0,
                    (center==null ? 1-definedGrid[i] : (1-definedGrid[i])/center[2]));

            origGrid[3*i+1][0] = CoDaStats.ternaryTransform(
                    (center==null ? 1-definedGrid[i] : (1-definedGrid[i])/center[0]),
                    (center==null ? definedGrid[i] : definedGrid[i]/center[1]),
                    0);
            origGrid[3*i+1][1] = CoDaStats.ternaryTransform(
                    0,
                    (center==null ? definedGrid[i] : definedGrid[i]/center[1]),
                    (center==null ? 1-definedGrid[i] : (1-definedGrid[i])/center[2]));

            origGrid[3*i+2][0] = CoDaStats.ternaryTransform(
                    0,
                    (center==null ? 1-definedGrid[i] : (1-definedGrid[i])/center[1]),
                    (center==null ? definedGrid[i] : definedGrid[i]/center[2]));
            origGrid[3*i+2][1] = CoDaStats.ternaryTransform(
                    (center==null ? 1-definedGrid[i] : (1-definedGrid[i])/center[0]),
                    0,
                    (center==null ? definedGrid[i] : definedGrid[i]/center[2]));
        }
    }
    public void setCentered(boolean centered){
        isCentered = centered;
        if(centered){
            double [][]cdata = CoDaStats.centerData(data);

            setGrid(center);
            for(int i=0;i<origZ.length;i++){
            origZ[i] = CoDaStats.ternaryTransform(
                    cdata[0][i], cdata[1][i], cdata[2][i]);
            }
        }else{
            setGrid(null);
            for(int i=0;i<origZ.length;i++){
            origZ[i] = CoDaStats.ternaryTransform(
                    data[0][i], data[1][i], data[2][i]);
            }
        }
    }
    
    public void showCenter(boolean showCenter){
        this.showCenter = showCenter;
        double totalX = 0, totalY = 0;
        for(int i = 0; i < Z.length;i++){
            totalX += Z[i][0];
            totalY += Z[i][1];
        }
        centerCalculated[0] = totalX/Z.length;
        centerCalculated[1] = totalY/Z.length;
    }
    
    @Override
    public void transformData(){
        V[0] = transform(oriV1[0], oriV1[1], V[0]);
        V[1] = transform(oriV2[0], oriV2[1], V[1]);
        V[2] = transform(oriV3[0], oriV3[1], V[2]);

        for(int i=0;i<grid.length;i++){
            grid[i][0] = transform(origGrid[i][0][0], origGrid[i][0][1], grid[i][0]);
            grid[i][1] = transform(origGrid[i][1][0], origGrid[i][1][1], grid[i][1]);
        }
        for(int i=0;i<Z.length;i++){
            Z[i] = transform(origZ[i][0], origZ[i][1], Z[i]);
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
        if( showGrid ) drawGrid(g2);
        drawAxis(g2);        
        drawData(g2);
        drawCenter(g2);
        drawLabels(g2);
    }
    public void drawGrid(Graphics2D g2){
        g2.setColor( config.getColor("grid") );
        g2.setStroke(new BasicStroke( config.getSize("grid"),
                   BasicStroke.JOIN_MITER,
                   BasicStroke.CAP_ROUND));
        Point2D o1 = null, o2 = null;
        for(int i=0;i<grid.length;i++){
            o1 = defaultTransform.transform(new Point2D.Double(grid[i][0][0], grid[i][0][1]), o1);
            o2 = defaultTransform.transform(new Point2D.Double(grid[i][1][0], grid[i][1][1]), o2);
            g2.draw(PlotUtils.drawLine(o1, o2));
        }
    }
    public void drawAxis(Graphics2D g2){
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
    public void drawData(Graphics2D g2){
        boolean withPaths = false;

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
    }
    
    public void drawCenter(Graphics2D g2){
        if(this.showCenter){
            Point2D o = null;
            o = defaultTransform.transform(new Point2D.Double(centerCalculated[0],centerCalculated[1]),o);
            g2.setColor(Color.RED);
            g2.fillRect((int)o.getX(), (int)o.getY(), 10, 10);
            g2.setColor(Color.RED);
            g2.drawRect((int)o.getX(),(int)o.getY(), 10, 10);
        }
    }
    
    public void drawAreas(Graphics2D g2){
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
    public void drawLabels(Graphics2D g2){
        Font font = new Font("Monospace", Font.PLAIN, 
                config.getSize("label").intValue());
        g2.setFont(font);
        FontMetrics metric = g2.getFontMetrics();
        g2.setColor(config.getColor("label"));
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
