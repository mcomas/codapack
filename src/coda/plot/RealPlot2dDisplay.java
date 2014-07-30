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
import org.scilab.forge.jlatexmath.ParseException;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

/**
 *
 * @author mcomas
 */
public class RealPlot2dDisplay extends CoDa2dDisplay{

    protected String[] names;
    protected double[][] oriZ;
    protected int[] groups;
    protected String[] gnames;
    protected int[] mapping;

    protected boolean showData = true;
    
    //boolean withIconLabel = false;

    protected double[][] Z;
    protected boolean[] showZ;

    protected double[] oriV1 = {1,0};
    protected double[] oriV2 = {0,1};
    protected double[][] V = new double[2][2];
   
    //TeXIcon iconLabel[];
    String textLabel[];

    public static class RealPlot2dBuilder{
        private String names[];
        private double data[][];
        private int groups[] = null;
        private String gnames[] = new String[0];
        private int mapping[] = null;

        public RealPlot2dBuilder(String names[], double data[][]){
            this.names = names;
            this.data = data;
            mapping = new int[data[0].length];
            groups = new int[data[0].length];
            for(int i=0; i<data.length;i++){
                mapping[i] = i;
                groups[i] = 0;
            }
        }
        public RealPlot2dBuilder groups(int groups[], String[] gnames){
            this.groups = groups;
            this.gnames = gnames;
            return this;
        }
        public RealPlot2dBuilder mapping(int mapping[]){
            this.mapping = mapping;
            return this;
        }
        public RealPlot2dDisplay build(){
            return new RealPlot2dDisplay(this);
        }
    }
    protected RealPlot2dDisplay(RealPlot2dBuilder builder){
    //public RealPlot2dDisplay(String[] names, double tZ[][]){
        super();

        this.names = builder.names;
        this.oriZ = builder.data;
        this.groups = builder.groups;
        this.gnames = builder.gnames;
        for(int i=0;i<gnames.length;i++)
            legendNames.add(new LegendItem(gnames[i], "data".concat(Integer.toString(i))));
        this.mapping = builder.mapping;

        //iconLabel = new TeXIcon[names.length];
        int s = config.getSize("label").intValue();
        textLabel = names;

        /*if(withIconLabel){
            for(int i =0;i<names.length;i++){
                try{
                    iconLabel[i] = new TeXFormula(names[i]).
                        createTeXIcon(TeXConstants.STYLE_DISPLAY, s);
                }catch(ParseException e){
                    iconLabel[i] = new TeXFormula("Item_" + (i+1)).
                        createTeXIcon(TeXConstants.STYLE_DISPLAY, s);
                }
            }
        }*/
        int n = oriZ[0].length;
        Z = new double[n][2];
        showZ = new boolean[n];
        
        Arrays.fill(showZ, false);
        scaleData();
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
        }
        
        for(int i=0;i<n;i++){
            oriZ[0][i] = oriZ[0][i] / max_z;
            oriZ[1][i] = oriZ[1][i] / max_z ;
        }
    }
    @Override
    public void transformData(){
        V[0] = transform(oriV1[0], oriV1[1], V[0]);
        V[1] = transform(oriV2[0], oriV2[1], V[1]);
        for(int i=0;i<Z.length;i++){
            Z[i] = transform(oriZ[0][i], oriZ[1][i], Z[i]);
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
        
        if(showData)drawData(g2);
        drawAxis(g2);
        
        drawLabels(g2);
        
    }
    public void drawAxis(Graphics2D g2){
        g2.setColor( config.getColor("axisX") );
        g2.setStroke(new BasicStroke(config.getSize("axisX"),
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND));

        Point2D o1 = null, o2 = null;
        o1 = defaultTransform.transform(new Point2D.Double(V[1][0],V[1][1]), o1);
        o2 = defaultTransform.transform(new Point2D.Double(V[1][0],2*origin[1]-V[1][1]), o2);
        g2.draw( PlotUtils.drawLine(o1,  o2) );
        //g2.drawString(, TOP_ALIGNMENT, TOP_ALIGNMENT);
        g2.setColor( config.getColor("axisY") );
        g2.setStroke(new BasicStroke(config.getSize("axisY"),
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND));

        o1 = defaultTransform.transform(new Point2D.Double(V[0][0],V[0][1]), o1);
        o2 = defaultTransform.transform(new Point2D.Double(2*origin[0]-V[0][0],V[0][1]), o2);
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
    }
    public void drawLabels(Graphics2D g2){
        int separation = 4;
        Point2D o = null;
        Font font = new Font("Monospace", Font.PLAIN, 15);
        g2.setFont(font);
        g2.setColor(config.getColor("label"));
        FontMetrics metric = g2.getFontMetrics();
        o = defaultTransform.transform(new Point2D.Double(V[0][0],V[0][1]), o);
        g2.drawString(textLabel[0],
                (int)o.getX() - metric.stringWidth(textLabel[0])/2,
                (int)o.getY() - separation);

        o = defaultTransform.transform(new Point2D.Double(V[1][0],V[1][1]), o);
        g2.drawString(textLabel[1],
                (int)o.getX() - metric.stringWidth(textLabel[1])/2,
                (int)o.getY() - separation);
      
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
