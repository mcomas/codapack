/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coda;

/**
 *
 * @author marc
 */
public interface Element<E>{
    
    public E getValue();
    
    public Element variable(Variable v);
    
    public void setValue(E v);
        
    @Override
    public String toString();
    
}
