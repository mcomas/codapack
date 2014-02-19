/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot2.datafig;

import coda.plot.CoDaDisplayConfiguration;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author marc
 */
public class FilledSquare implements CoDaShape{
    Color interior;
    Color contour;
    double size;
    public FilledSquare(){
        this.interior = CoDaDisplayConfiguration.getColor("data0");
        this.contour = Color.BLACK;
        this.size = CoDaDisplayConfiguration.getSize("data");
    }
    public FilledSquare(Color interior, Color contour, double size){
        this.interior = interior;
        this.contour = contour;
        this.size = size;
    }
    public void plot(Graphics2D g2, Point2D p) {
        double cx = p.getX();
        double cy = p.getY();

        Rectangle2D.Double rect = new Rectangle2D.Double(cx-size, cy-size, 2 * size, 2 * size);
        g2.setColor( interior );
        g2.fill(rect);
        g2.setColor( contour );
        g2.draw(rect);
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
