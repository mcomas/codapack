/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coda;

/**
 *
 * @author marc
 */
public class Text implements Element<String>{
    String value;
    Variable var = null;
    
    public Text(String v){
        value = v;
    }
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String v) {
        value = v;
    }
    @Override
    public String toString(){
        return value;
    }
    @Override
    public Element variable(Variable v) {
        this.var = v;
        return(this);
    }    
}
