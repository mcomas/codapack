/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.sim;

import coda.CoDaStats;
import coda.Composition;
import coda.ext.triangle.Triangle2D;
import coda.ext.triangle.Vertex2D;

import coda.plot.CoDa2dDisplay;
import coda.plot.CoDaDisplayConfiguration;
import coda.plot.PlotUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import java.awt.geom.AffineTransform;

import java.awt.geom.Point2D;
import java.util.Arrays;
import org.scilab.forge.jlatexmath.ParseException;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

/**
 *
 * @author marc
 */
public class TernaryPlotDisplay extends CoDa2dDisplay{    

    // Constructor parameters
    protected String[] names;
    protected double[][] data;
    protected int[] groups;
    protected int[] mapping;
    protected double[] definedGrid;

    protected final double[][] origZ;
    protected double[][] Z;
    protected boolean[] showZ;

    protected boolean isCentered = false;
    protected double[] center;

    // Simple vertices    
    private final double[] oriV1 = {0, 0.57735026918962576450914878050196};
    private final double[] oriV2 = {-0.5, -0.28867513459481288225457439025098};
    private final double[] oriV3 = {0.5, -0.28867513459481288225457439025098};
    private double[][] V = new double[3][2];

    private final double[][][] origGrid;
    private double[][][] grid;
    private boolean showGrid = false;

    private TeXIcon iconLabel[];
    /*
     * Builder constructor for TernaryPlot2Display
     */
    public static class TernaryPlotBuilder{
        private String names[];
        private double data[][];
        private int groups[] = null;
        private int mapping[] = null;
        private double definedGrid[] = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
        public TernaryPlotBuilder(String names[], double data[][]){
            this.names = names;
            this.data = data;
            mapping = new int[data.length];
            groups = new int[data.length];
            for(int i=0; i<data.length;i++){
                mapping[i] = i;
                groups[i] = 0;
            }
        }
        public TernaryPlotBuilder(String names[], Composition data[]){
            this.names = names;
            this.data = new double[3][data.length];
            for(int i=0,m=data.length;i<m;i++){
                this.data[0][i] = data[i].get(0);
                this.data[1][i] = data[i].get(1);
                this.data[2][i] = data[i].get(2);
            }
            mapping = new int[data.length];
            groups = new int[data.length];
            for(int i=0; i<data.length;i++){
                mapping[i] = i;
                groups[i] = 0;
            }
        }
        public TernaryPlotBuilder groups(int groups[]){
            this.groups = groups;
            return this;
        }
        public TernaryPlotBuilder mapping(int mapping[]){
            this.mapping = mapping;
            return this;
        }
        public TernaryPlotBuilder grid(double definedGrid[]){
            this.definedGrid = definedGrid;
            return this;
        }
        public TernaryPlotDisplay build(){
            return new TernaryPlotDisplay(this);
        }
    }
    protected TernaryPlotDisplay(TernaryPlotBuilder builder){
        super();
        // Builder parameters copy
        names = builder.names;
        data = builder.data;
        groups = builder.groups;
        mapping = builder.mapping;
        definedGrid = builder.definedGrid;

        // Initialization variables
        iconLabel = new TeXIcon[names.length];
        int s = CoDaDisplayConfiguration.getSize("label").intValue();
        for(int i =0;i<names.length;i++){
            try{
                iconLabel[i] = new TeXFormula(names[i])
                    .createTeXIcon(TeXConstants.STYLE_DISPLAY, s);
            }catch(ParseException e){
                // LaTeX instruction not compiled
                iconLabel[i] = new TeXFormula("Item_" + (i+1))
                    .createTeXIcon(TeXConstants.STYLE_DISPLAY, s);
            }
        }
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
        drawLabels(g2);
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
    public void drawData(Graphics2D g2){
        boolean withPaths = false;
        /*
        if(withPaths){
            g2.setColor( Color.BLACK );
            g2.setStroke(new BasicStroke(1.0f ,
                    BasicStroke.JOIN_MITER,
                    BasicStroke.CAP_ROUND));
            Path2D.Double path = new Path2D.Double();
            Point2D o = null;
            o = defaultTransform.transform(new Point2D.Double(Z[0][0],Z[0][1]), o);
            path.moveTo(o.getX(), o.getY());
            for(int i=1;i<Z.length;i++){
                o = defaultTransform.transform(new Point2D.Double(Z[i][0],Z[i][1]), o);
                path.lineTo(o.getX(), o.getY());
            }
            g2.draw(path);
        }else{*/
            Color c1 = CoDaDisplayConfiguration.getColor("data");
            g2.setColor( c1 );
            g2.setStroke(new BasicStroke(0.5f ,
                    BasicStroke.JOIN_MITER,
                    BasicStroke.CAP_ROUND));
            float s = CoDaDisplayConfiguration.getSize("data");
            Point2D o = null;
            for(int i=0;i<Z.length;i++){
                o = defaultTransform.transform(new Point2D.Double(Z[i][0],Z[i][1]), o);
                g2.draw(PlotUtils.drawPoint(o, s));

                if(showZ[i]){
                    g2.setColor( Color.BLACK );
                    g2.drawString(Integer.toString(mapping[i]+1), (float)o.getX(), (float)o.getY());
                    g2.setColor( c1 );
                }
            }
        //}
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
        int separation = 4;
        Point2D o = null;
        o = defaultTransform.transform(new Point2D.Double(V[0][0],V[0][1]), o);
        iconLabel[0].paintIcon(null, g2,
                (int)o.getX() - iconLabel[0].getIconWidth()/2,
                (int)o.getY() - iconLabel[0].getIconHeight() - separation);

        o = defaultTransform.transform(new Point2D.Double(V[1][0],V[1][1]), o);
        iconLabel[1].paintIcon(null, g2,
                (int)o.getX() - 3 * iconLabel[1].getIconWidth()/4,
                (int)o.getY() + separation);

        o = defaultTransform.transform(new Point2D.Double(V[2][0],V[2][1]), o);
        iconLabel[2].paintIcon(null, g2,
                (int)o.getX() - iconLabel[2].getIconWidth()/4,
                (int)o.getY() + separation);
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
