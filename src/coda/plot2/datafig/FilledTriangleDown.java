/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot2.datafig;

import coda.ext.triangle.Triangle2D;
import coda.plot.CoDaDisplayConfiguration;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author marc
 */
public class FilledTriangleDown implements CoDaShape{
    Color interior;
    Color contour;
    double size;
    public FilledTriangleDown(){
        CoDaDisplayConfiguration config = new CoDaDisplayConfiguration();
        this.interior = config.getColor("data0");
        this.contour = Color.BLACK;
        this.size = config.getSize("data");
    }
    public FilledTriangleDown(Color interior, Color contour, double size){
        this.interior = interior;
        this.contour = contour;
        this.size = size;
    }
    public void plot(Graphics2D g2, Point2D p) {
        double cx = p.getX();
        double cy = p.getY();
        //0.28867513459481288225457439025098
        double left = size ;
        double down = 2 * size * 0.28867513459481288225457439025098;
        Triangle2D tri = new Triangle2D(cx-size,cy-down,cx+size,cy-down,cx,cy+2*down);

        g2.setColor( interior );
        g2.fill(tri);
        g2.setColor( contour );
        g2.draw(tri);
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
