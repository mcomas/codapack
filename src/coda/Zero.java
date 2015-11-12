/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coda;

/**
 *
 * @author marc
 */
public class Zero extends Numeric{
    public double detection = Double.NaN;
    public Zero(){
        super(0);
    }
    public Zero(double detection){
        super(0);
        this.detection = detection;
    }
    @Override
    public String toString(){
        if(detection >0)
            return("0 [" + Double.toString(detection) + "]");
        else
            return("0");
    }
    /*
     * Returns -1 if s is not a Zero representation
     *         dl otherwise
     */
    public static double isZero(String s){
        int i0 = s.indexOf("[");
        int i1 = s.indexOf("]");
        if(i0 != -1 && i1 != -1 && i0 < i1){
            try{
                double zero = Double.parseDouble(s.substring(0, i0));
                double dl = Double.parseDouble(s.substring(i0+1, i1));
                if(zero == 0)
                    return dl;
                else
                    return -1;
            }catch(NumberFormatException e2){
                return -1;
            }
        }else{
            return -1;
        }
    }
}
