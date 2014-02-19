/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot2.objects;

import coda.plot.AbstractCoDaDisplay.LegendItem;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JFrame;

/**
 *
 * @author mcomas
 */
public interface TernaryObject {
    void setColor(Color color);
    
    void setParameters(JFrame f);
    
    void setVisible(boolean visible);

    void plotObject(Graphics2D g2);

    void perturbeObject(double x[]);

    void powerObject(double t);

    double[] getCenter();

    LegendItem getLegendItem();
}
