package coda;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * DataFrame class is the main object used for storing {@link Variable} classes in CoDaPack. 
 * DataFrame is extended from {@link HashMap} using variable names as main keys.
 * The main difference from {@link HashMap} is that DataFrame keeps the 
 * variables ordered.
 * 
 * @author marc
 */
public class DataFrame{

    public String name;
    
    private final HashMap<String, Variable> vars;
    private final ArrayList<String> varnames = new ArrayList<String>();

    private int nobservations = 0;
    public DataFrame(){
        this.vars = new HashMap<String, Variable>();
        this.name = "default";
    }
    public DataFrame(String name){
        this.vars = new HashMap<String, Variable>();
        this.name = name;
    }
    public ArrayList<String> getNames(){
        return varnames;
    }
    public Variable get(int index){
        return vars.get(varnames.get(index));
    }
    public Variable get(String vname){
        return vars.get(vname);
    }
    public boolean add(Variable variable){
        int n = variable.size();
        if(nobservations == 0 | nobservations == n){
            nobservations = n;
            varnames.add(variable.name);
            vars.put(variable.name, variable);
            return true;
        }
        return false;
    }
    public Variable remove(String name){
        Variable var = null;
        if(varnames.contains(name)){
            varnames.remove(name);
            var = vars.remove(name);
        }
        return var;
    }
    public int nVariables(){
        return vars.size();
    }
    public int nObservations(){
        return nobservations;
    }
    
}
