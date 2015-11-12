/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coda;

import coda.gui.CoDaPackConf;

/**
 *
 * @author marc
 */
public class Numeric implements Element<Double>{
    double v;
    Variable var = null;
    
    public Numeric(double v){
        this.v = v;
    }
    @Override
    public Double getValue() {
        return v;
    }

    @Override
    public void setValue(Double v) {
        this.v = v;
    }
    
    public void setValue(double v) {
        this.v = v;
    }    
    @Override
    public String toString(){
        return(CoDaPackConf.getDecimalTableFormat().format(v));
    }

    @Override
    public Element variable(Variable v) {
        this.var = v;
        return(this);
    }
}
