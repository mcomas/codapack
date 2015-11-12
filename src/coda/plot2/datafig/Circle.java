/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot2.datafig;

import coda.plot2.*;
import coda.plot.CoDaDisplayConfiguration;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 *
 * @author marc
 */
public class Circle implements CoDaShape{    
    Color contour;
    double size;
    public Circle(){        
        CoDaDisplayConfiguration config = new CoDaDisplayConfiguration();
        this.contour = config.getColor("data0");
        this.size = config.getSize("data");
    }
    public Circle(Color contour, double size){
        this.contour = contour;
        this.size = size;
    }
    public void plot(Graphics2D g2, Point2D p) {        
        g2.setColor( contour );
        g2.draw(PlotUtils.drawPoint(p, size));
    }

    public Color getColor() {
        return contour;
    }

    public void setColor(Color color) {
        contour = color;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getSize() {
        return size;
    }

}
