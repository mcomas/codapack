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

import coda.gui.CoDaPackConf;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import org.scilab.forge.jlatexmath.ParseException;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

/**
 *
 * @author mcomas
 */
public class Biplot3dDisplay extends RealPlot3dDisplay{
    public boolean decomp = true;
    private double[][] oriH;
    private double[][] H;
    //private TeXIcon decomposition = new TeXFormula("Z_{clr} = U \\left( V \\Gamma \\right)^T").//\\left(U \\, \\Gamma^{0.5}\\right) \\; \\left( V \\, \\Gamma^{0.5} \\right)^T").
    	                   // createTeXIcon(TeXConstants.STYLE_DISPLAY, 16f);
    //TeXIcon iconVectorLabel[];
    String vectorLabel[];
    public static class Biplot3dBuilder extends RealPlot3dBuilder{
        
        
        private double dataComponent[][];

        public Biplot3dBuilder(String names[], double tG[][], double tH[][]){
            super(names, tG);
            dataComponent = tH;
        }
        @Override
        public Biplot3dBuilder groups(int groups[], String[] gnames){
            return (Biplot3dBuilder) super.groups(groups, gnames);
        }
        @Override
        public Biplot3dBuilder mapping(int mapping[]){
            return (Biplot3dBuilder) super.mapping(mapping);
        }
        @Override
        public Biplot3dDisplay build(){
            return new Biplot3dDisplay(this);
        }
    }
    public Biplot3dDisplay(Biplot3dBuilder builder){
        super(builder);
        this.oriH = builder.dataComponent;

        int s = config.getSize("label").intValue();
        //iconLabel = new TeXIcon[3];
        textLabel = new String[3];
        textLabel[0] = "ilr.1";
        textLabel[1] = "ilr.2";
        textLabel[2] = "ilr.3";
        /*iconLabel[0] = new TeXFormula("1st").
                    createTeXIcon(TeXConstants.STYLE_DISPLAY, s);
        iconLabel[1] = new TeXFormula("2nd").
                    createTeXIcon(TeXConstants.STYLE_DISPLAY, s);
        iconLabel[2] = new TeXFormula("3rd").
                    createTeXIcon(TeXConstants.STYLE_DISPLAY, s);
*/
        s = config.getSize("vector_label").intValue();
        //iconVectorLabel = new TeXIcon[names.length];
        vectorLabel = new String[names.length];
        for(int i =0;i<names.length;i++){
            vectorLabel[i] = names[i];
            /*try{
                iconVectorLabel[i] = new TeXFormula(names[i]).
                    createTeXIcon(TeXConstants.STYLE_DISPLAY, s);
            }catch(ParseException e){
                iconVectorLabel[i] = new TeXFormula("Item_" + (i+1)).
                    createTeXIcon(TeXConstants.STYLE_DISPLAY, s);
            }*/
        }
        
        Z = new double[oriZ[0].length][3];
        H = new double[oriH.length][3];
        
        scaledData();
    }
    public void setAlpha(double alpha){
        try{
            CoDaPackConf.getDecimalTableFormat().format(alpha);
            String v1 = CoDaPackConf.getDecimalTableFormat().format(alpha);
            String v2 = CoDaPackConf.getDecimalTableFormat().format(1-alpha);
            String formula = "Z_{clr} = ";

            if(! v1.equals("0.00") && ! v2.equals("0.00"))
                formula += "\\left( U  \\Gamma^{" + v1 + "} \\right) \\left( V \\Gamma^{" + v2 + "} \\right)^T";
            else if(v1.equals("0.00"))
                formula += "U \\left( V \\Gamma \\right)^T";
            else
                formula += "\\left( U \\Gamma \\right)  V^T";
            
            //decomposition = new TeXFormula(formula).
	      //              createTeXIcon(TeXConstants.STYLE_DISPLAY, 16f);

        }catch(ParseException e){

        }
    }
    private void scaledData(){
        double max_g = 0, max_h = 0, temp;

        int n = oriZ[0].length;
        int p = oriH.length;

        for(int i=0;i<n;i++){
            temp = Math.abs(oriZ[0][i]);
            max_g = (max_g < temp ? temp: max_g);
            temp = Math.abs(oriZ[1][i]);
            max_g = (max_g < temp ? temp: max_g);
        }
        for(int i=0;i<p;i++){
            temp = Math.abs(oriH[i][0]);
            max_h = (max_h < temp ? temp: max_h);
            temp = Math.abs(oriH[i][1]);
            max_h = (max_h < temp ? temp: max_h);
        }
        
        for(int i=0;i<n;i++){
            oriZ[0][i] = oriZ[0][i] / max_g;
            oriZ[1][i] = oriZ[1][i] / max_g;
            oriZ[2][i] = oriZ[2][i] / max_g;
        }
        
        for(int i=0;i<p;i++){
            oriH[i][0] = oriH[i][0] / max_h;
            oriH[i][1] = oriH[i][1] / max_h;
            oriH[i][2] = oriH[i][2] / max_h;
        }
    }
    @Override
    public void transformData(){
        super.transformData();
        
        for(int i=0;i<H.length;i++){
            H[i] = transform(oriH[i][0], oriH[i][1], oriH[i][2], H[i]);
        }
    }
    public void setNewData(double tG[][], double tH[][]){
        // Comprovation of same length is needed, otherwise there is going to be
        //a G and H null pointer error
        this.oriZ = tG;
        this.oriH = tH;

        scaledData();
        repaint();
    }
    @Override
    public void paintComponent(Graphics2D g2, double width, double height){
        super.paintComponent(g2,width,height);
        drawVectors(g2);
        /*if(decomp)
            drawDecomposition(g2);*/
    }
    /*public void drawDecomposition(Graphics2D g2){
        decomposition.paintIcon(null, g2, getWidth()-200, getHeight()-50);
    }*/
    public void drawVectors(Graphics2D g2){
        
        g2.setStroke(new BasicStroke(1f ,
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND));

        Font font = new Font("Monospace", Font.PLAIN, 13);
        g2.setFont(font);
        
        FontMetrics metric = g2.getFontMetrics();
        Point2D o = null;
        int separation = 4;
        Point2D o1 = null, o2 = null;
        double sep[][] = new double[H.length][3];
        for(int i=0;i<H.length;i++){
            sep[i][0] = H[i][0] - origin[0];
            sep[i][1] = H[i][1] - origin[1];
            sep[i][2] = H[i][2] - origin[2];
            double norm = Math.sqrt(sep[i][0]*sep[i][0] + sep[i][1]*sep[i][1] +
                    sep[i][2]*sep[i][2]);
            sep[i][0] /= norm;
            sep[i][1] /= norm;
        }
        o1 = defaultTransform.transform(new Point2D.Double(origin[0],origin[1]), o1);
        for(short i =0;i<H.length;i++){
            g2.setColor(config.getColor("vector_axis"));
            o2 = defaultTransform.transform(new Point2D.Double(H[i][0],H[i][1]), o2);
            g2.draw(PlotUtils.drawLine(o1, o2));
            g2.setColor(config.getColor("vector_label"));
            double x = metric.stringWidth(vectorLabel[i]);
            double y = metric.getHeight();
            double f = Math.sqrt(x*x + y*y);
            x /= f;
            y /= f;
            
            sep[i][0] *= 0.15 * x;
            sep[i][1] *= 0.15 * y;
            o = defaultTransform.transform(new Point2D.Double(H[i][0] + sep[i][0],H[i][1] + sep[i][1]), o);
            g2.drawString(vectorLabel[i],
                    (int)o.getX() - metric.stringWidth(vectorLabel[i])/2,
                    (int)o.getY() + metric.getHeight()/2);
            

        }
        /*
        int separation = 4;
        Point2D o1 = null, o2 = null;
        o1 = defaultTransform.transform(new Point2D.Double(origin[0],origin[1]), o1);
        for(int i=0;i<H.length;i++){
            o2 = defaultTransform.transform(new Point2D.Double(H[i][0],H[i][1]), o2);
            g2.draw(PlotUtils.drawLine(o1, o2));
            //g2.fill(PlotUtils.drawPoint(o2, 4f));
            iconVectorLabel[i].paintIcon(null, g2,
                (int)o2.getX() - iconVectorLabel[0].getIconWidth()/2,
                (int)o2.getY() - iconVectorLabel[0].getIconHeight() - separation);
        }*/
    }
}
