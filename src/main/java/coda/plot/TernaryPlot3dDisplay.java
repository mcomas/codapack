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

/**
 *
 * @author marc
 */
public class TernaryPlot3dDisplay extends CoDa3dDisplay{

    protected String[] names;
    protected double[][] data;
    protected int[] groups;
    protected String[] gnames;
    protected int[] mapping;
    protected double[] centerCalculated = new double[2];

    protected final double[][] origZ;
    protected double[][] Z;
    protected boolean[] showZ;

    protected boolean isCentered = false;
    protected boolean showCenter = false;
    protected double[] center;
    
    private final double[] oriV1 = {-0.2886751347, 0.5, -0.2041241452};
    private final double[] oriV2 = {0.0, 0.0, 0.612372435727726};
    private final double[] oriV3 = {0.577350269084439, 0.0,-0.2041241452};
    private final double[] oriV4 = {-0.2886751347,-0.5,-0.2041241452};
    private double[][] V = new double[4][3];
    
    //TeXIcon iconLabel[];
    String textLabel[];
    /*
     * Builder constructor for TernaryPlot3Display
     */
    public static class TernaryPlot3dBuilder{
        private String names[];
        private double data[][];
        private int groups[] = null;
        private String gnames[] = new String[0];
        private int mapping[] = null;
        public TernaryPlot3dBuilder(String names[], double data[][]){
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
        public TernaryPlot3dBuilder groups(int groups[], String gnames[]){
            this.groups = groups;
            this.gnames = gnames;
            return this;
        }
        public TernaryPlot3dBuilder mapping(int mapping[]){
            this.mapping = mapping;
            return this;
        }
        public TernaryPlot3dDisplay build(){
            return new TernaryPlot3dDisplay(this);
        }
    }
    protected TernaryPlot3dDisplay(TernaryPlot3dBuilder builder){
        super();
        // Builder parameters copy
        names = builder.names;
        data = builder.data;
        groups = builder.groups;
        gnames = builder.gnames;
        for(int i=0;i<gnames.length;i++)
            legendNames.add(new LegendItem(gnames[i], "data".concat(Integer.toString(i))));
        mapping = builder.mapping;

        // Initialization variables
        //iconLabel = new TeXIcon[names.length];
        textLabel = names;
        int s = config.getSize("label").intValue();
        /*for(int i =0;i<names.length;i++){
            try{
                iconLabel[i] = new TeXFormula(names[i])
                    .createTeXIcon(TeXConstants.STYLE_DISPLAY, s);
            }catch(ParseException e){
                iconLabel[i] = new TeXFormula("Item_" + (i+1))
                    .createTeXIcon(TeXConstants.STYLE_DISPLAY, s);
            }
        }*/
        center = CoDaStats.center(data);

        int n = data[0].length;
        showZ = new boolean[n];
        origZ = new double[n][];
        Z = new double[n][3];
        for(int i=0;i<n;i++){
            showZ[i] = false;
            origZ[i] = CoDaStats.ternaryTransform(
                    data[0][i], data[1][i], data[2][i], data[3][i]);
        }
    }
    public void setCentered(boolean centered){
        isCentered = centered;
        if(centered){
            double [][]cdata = CoDaStats.centerData(data);

            for(int i=0;i<origZ.length;i++){
            origZ[i] = CoDaStats.ternaryTransform(
                    cdata[0][i], cdata[1][i], cdata[2][i], cdata[3][i]);
            }
        }else{
            for(int i=0;i<origZ.length;i++){
            origZ[i] = CoDaStats.ternaryTransform(
                    data[0][i], data[1][i], data[2][i], data[3][i]);
            }
        }
    }
    
    public void showCenter(boolean center){
        this.showCenter = center;
        this.centerCalculated = CoDaStats.ternaryTransform(this.center[0], this.center[1], this.center[2], this.center[3]);
    }
    
    @Override
    public void transformData(){
        V[0] = transform(oriV1[0], oriV1[1], oriV1[2], V[0]);
        V[1] = transform(oriV2[0], oriV2[1], oriV2[2], V[1]);
        V[2] = transform(oriV3[0], oriV3[1], oriV3[2], V[2]);
        V[3] = transform(oriV4[0], oriV4[1], oriV4[2], V[3]);

        for(int i=0;i<Z.length;i++){
            Z[i] = transform(origZ[i][0], origZ[i][1], origZ[i][2], Z[i]);
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
        drawData(g2);
        drawAxis(g2);
        this.showCenter(this.showCenter);
        drawCenter(g2);
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
        //float[] dash1 = { 2f, 0f, 2f };
        float dash1[] = { 2.0f };
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
        /*
        String output = "";
        for(int i=0;i<4;i++) output += V[indexOrder[i]][2] + ", ";
        System.out.println(output);*/
        //System.out.println(index);
        //System.out.println(names[index]);
        /*
        boolean behind = false;
        if( index == 0){
            behind = isInside(V[0][0], V[0][1],
                     V[1][0], V[1][1],
                     V[2][0], V[2][1],
                     V[3][0], V[3][1]);
        }else if(index == 1){
            behind = isInside(V[1][0], V[1][1],
                     V[0][0], V[0][1],
                     V[2][0], V[2][1],
                     V[3][0], V[3][1]);
        }else if(index == 2){
            behind = isInside(V[2][0], V[2][1],
                     V[0][0], V[0][1],
                     V[1][0], V[1][1],
                     V[3][0], V[3][1]);
        }else{
            behind = isInside(V[3][0], V[3][1],
                     V[0][0], V[0][1],
                     V[2][0], V[2][1],
                     V[1][0], V[1][1]);
        }*/

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
    private void drawData(Graphics2D g2){
        g2.setStroke(new BasicStroke(0.5f,
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
    
    private void drawCenter(Graphics2D g2){
        if(this.showCenter){
            Point2D o = null;
            o = defaultTransform.transform(new Point2D.Double(centerCalculated[0],centerCalculated[1]), o);
            g2.setColor(Color.RED);
            g2.fillRect((int)o.getX(),(int)o.getY(),10,10);
            g2.setColor(Color.RED);
            g2.drawRect((int)o.getX(),(int)o.getY(),10,10);
        }
    }
    
    private void drawAreas(Graphics2D g2){
        g2.setPaint( config.getColor("area"));
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
    private void drawLabels(Graphics2D g2){
        Font font = new Font("Monospace", Font.PLAIN, 
                config.getSize("label").intValue());
        g2.setFont(font);
        g2.setColor(config.getColor("label"));
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
