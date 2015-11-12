/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coda;

/**
 *
 * @author marc
 */
public class NonAvailable extends Numeric{
    public NonAvailable(){
        super(Double.NaN);
    }
    @Override
    public String toString(){
        return("na");
    }
}
