/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda;

/**
 *
 * @author mcomas
 */
public class ZeroData {
    private double detection_level;
    private boolean active;
    public ZeroData(double level){
        detection_level = level;
        active = false;
    }
    public void setActive(boolean active){
        this.active = active;
    }
    public boolean isActive(){
        return active;
    }
    public Double getDetectionLevel(){
        return detection_level;
    }
    public Double getValue(){
        return (active ? (Double)detection_level : Double.MIN_VALUE);
    }
}
