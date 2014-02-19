    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;

/**
 *
 * @author marc
 */
public abstract class CoDa2dDisplay extends AbstractCoDaDisplay implements MouseListener, MouseMotionListener, MouseWheelListener {
    
    protected double[] origin = {0, 0};
    protected double[] v_x = {1.0, 0.0};
    protected double[] v_y = {0.0, 1.0};

    protected AffineTransform defaultTransform;
    public CoDa2dDisplay(){
        setListeners();
    }
    public final void setListeners(){
        addMouseListener(this);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
    }
    public final double[] transform(double x, double y, double[] sol){
        sol[0] = x * v_x[0] +
                    y * v_y[0]  + origin[0];
        sol[1] = x * v_x[1] +
                    y * v_y[1]  + origin[1];
        return sol;
    }
    public AffineTransform getGeometry(){
        return defaultTransform;
    }
    public void scale(double k){
        origin[0] *= k;
        origin[1] *= k;

        v_x[0] *= k; v_x[1] *= k;
        v_y[0] *= k; v_y[1] *= k;
    }
    public void translate(double x, double y){
        origin[0] += x;
        origin[1] += y;
    }
    public final void setVX(double x, double y){
        v_x[0] = x; v_x[1] = y;
    }
    public final void setVY(double x, double y){
        v_y[0] = x; v_y[1] = y;
    }
    public final double[] getVX(){
        return v_x;
    }
    public final double[] getVY(){
        return v_y;
    }
    public final double[] getOrigin(){
        return origin;
    }
    public abstract void transformData();
    // Catch different events
    
    public void mouseClicked(MouseEvent me) { }
    
    public final void mousePressed(MouseEvent me) {
        lastOffsetX = me.getX();
        lastOffsetY = me.getY();
    }
    
    public void mouseDragged(MouseEvent me) {
        double newX = me.getX() - lastOffsetX;
        double newY = me.getY() - lastOffsetY;

        // increment last offset to last processed by drag event.
        lastOffsetX += newX;
        lastOffsetY += newY;
        // update the canvas locations
        translate(newX /  factor, -newY /  factor);
        repaint();
    }
    
    public final void mouseReleased(MouseEvent me) {
        repaint();
    }

    
    public void mouseEntered(MouseEvent me) { }

    
    public void mouseExited(MouseEvent me) { }

    
    public void mouseMoved(MouseEvent me) { }

    public final void zoom(double dzoom){
        if(actualZoom * dzoom > 0.5){
            actualZoom *= dzoom;
            scale(dzoom);
        }else{
            scale(0.5/actualZoom);
            actualZoom = 0.5;
            origin[0] = 0;
            origin[1] = 0;
        }
        repaint();
    }
    
    public final void mouseWheelMoved(MouseWheelEvent mwe) {
        if(mwe.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
            double dzoom = 1 - .1 * mwe.getWheelRotation();
            zoom(dzoom);
        }
    }
}
