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
public abstract class CoDa3dDisplay  extends AbstractCoDaDisplay implements MouseListener, MouseMotionListener, MouseWheelListener {

    protected double[] origin = {0, 0, 0};
    protected double[] v_x = {1.0, 0.0, 0.0};
    protected double[] v_y = {0.0, 1.0, 0.0};
    protected double[] v_z = {0.0, 0.0, 1.0};

    protected AffineTransform defaultTransform;
    public CoDa3dDisplay(){
        setListeners();
    }
    public final void setListeners(){
        addMouseListener(this);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
    }
    public AffineTransform getGeometry(){
        return defaultTransform;
    }
    public final double[] transform(double x, double y, double z, double[] sol){
        sol[0] = x * v_x[0] +
                    y * v_y[0] +
                    z * v_z[0] + origin[0];
        sol[1] = x * v_x[1] +
                    y * v_y[1] +
                    z * v_z[1] + origin[1];
        sol[2] = x * v_x[2] +
                    y * v_y[2] +
                    z * v_z[2] + origin[2];
        return sol;
    }
    public final void rotateXY(double a, double b){
        
        double cosA = Math.cos(a);
        double sinA = Math.sin(a);
        double cosB = Math.cos(b);
        double sinB = Math.sin(b);

        v_x = PlotUtils.v_rot(v_x, cosA, sinA, cosB, sinB);
        v_y = PlotUtils.v_rot(v_y, cosA, sinA, cosB, sinB);
        v_z = PlotUtils.v_rot(v_z, cosA, sinA, cosB, sinB);


    }
    public final void scale(double k){
        origin[0] *= k;
        origin[1] *= k;
        origin[2] *= k;

        v_x[0] *= k; v_x[1] *= k; v_x[2] *= k;
        v_y[0] *= k; v_y[1] *= k; v_y[2] *= k;
        v_z[0] *= k; v_z[1] *= k; v_z[2] *= k;
    }
    public final void translate(double x, double y){
        origin[0] += x;
        origin[1] += y;
    }
    public final void setVX(double x, double y, double z){
        v_x[0] = x; v_x[1] = y; v_x[2] = z;
    }
    public final void setVY(double x, double y, double z){
        v_y[0] = x; v_y[1] = y; v_y[2] = z;
    }
    public final void setVZ(double x, double y, double z){
        v_z[0] = x; v_z[1] = y; v_z[2] = z;
    }
    public final double[] getVX(){
        return v_x;
    }
    public final double[] getVY(){
        return v_y;
    }
    public final double[] getVZ(){
        return v_z;
    }
    public abstract void transformData();
      // Catch different events
    
    public void mouseClicked(MouseEvent me) { }
    
    public void mousePressed(MouseEvent me) {
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
        if(me.isAltDown()){
            translate(newX /  factor, -newY /  factor);
        }else{
            rotateXY(newY /  factor, newX /  factor);
            
        }
        repaint();
    }
    
    public void mouseReleased(MouseEvent me) {
        repaint();
    }

    
    public void mouseEntered(MouseEvent me) { }

    
    public void mouseExited(MouseEvent me) { }

    
    public void mouseMoved(MouseEvent me) { }

    public final void zoom(double dzoom){
        //if(actualZoom * dzoom > 0.5){
            actualZoom *= dzoom;
            scale(dzoom);
        /*}else{
            scale(0.5/actualZoom);
            actualZoom = 0.5;
            origin[0] = 0;
            origin[1] = 0;
        }*/
        repaint();
    }
    
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        if(mwe.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
            double dzoom = 1 - .1 * mwe.getWheelRotation();
            zoom(dzoom);
        }
    }
}
