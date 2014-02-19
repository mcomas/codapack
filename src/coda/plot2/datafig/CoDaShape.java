/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot2.datafig;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 *
 * @author marc
 */
public interface CoDaShape {
    public void plot(Graphics2D g2, Point2D p);

    public void setColor(Color color);

    public Color getColor();
    
    public void setSize(double size);

    public double getSize();
}
