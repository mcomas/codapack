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

package coda.plotfx;


import java.awt.geom.AffineTransform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author marc
 */
public abstract class CoDa2dDisplayFX extends AbstractCoDaDisplayFX {
    
    protected double[] origin = {0, 0};
    protected double[] v_x = {1.0, 0.0};
    protected double[] v_y = {0.0, 1.0};

    protected AffineTransform defaultTransform;
    public CoDa2dDisplayFX(){
        
        addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            mouseDragged(e);
        });
        addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            mousePressed(e);
        });
        addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            mouseReleased(e);
        });
        setOnScroll((ScrollEvent event) -> {
            mouseWheelMoved(event);
            event.consume();
        });
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
    @Override
    public void scale(double k){
        origin[0] *= k;
        origin[1] *= k;

        v_x[0] *= k; v_x[1] *= k;
        v_y[0] *= k; v_y[1] *= k;
    }
    @Override
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
    @Override
    public abstract void transformData();
    // Catch different events
    
    
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
        paintComponent(getWidth(), getHeight());
    }
    
    public final void mouseReleased(MouseEvent me) {
        paintComponent(getWidth(), getHeight());//repaint();
    }


    @Override
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
        //repaint();
    }
    
    public final void mouseWheelMoved(ScrollEvent mwe) {
        double dzoom = 1 - .1 * mwe.getDeltaY();
        zoom(dzoom);
        paintComponent(getWidth(), getHeight());
    }
}
