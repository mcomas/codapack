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
public class FilledCircle implements CoDaShape{
    Color interior;
    Color contour;
    double size;
    public FilledCircle(){
        CoDaDisplayConfiguration config = new CoDaDisplayConfiguration();
        this.interior = config.getColor("data0");
        this.contour = Color.BLACK;
        this.size = config.getSize("data");
    }
    public FilledCircle(Color interior, Color contour, double size){
        this.interior = interior;
        this.contour = contour;
        this.size = size;
    }
    public void plot(Graphics2D g2, Point2D p) {
        g2.setColor( interior );
        g2.fill(PlotUtils.drawPoint(p, size));
        g2.setColor( contour );
        g2.draw(PlotUtils.drawPoint(p, size));
    }

    public Color getColor() {
        return interior;
    }

    public void setColor(Color color) {
        interior = color;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getSize() {
        return size;
    }

}
