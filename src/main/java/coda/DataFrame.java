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
        this.vars = new HashMap<>();
        this.name = name;
    }
    public ArrayList<String> getNames(){
        return varnames;
    }
    public ArrayList<String> getNames(int type){
        ArrayList<String> names = new ArrayList<>();
        if(Variable.VAR_TEXT == type){
            for(String vname : varnames){
                Variable var = vars.get(vname);
                if(var.isText()){
                    names.add(var.name);
                }
            }
        }
        if(Variable.VAR_NUMERIC == type){
            for(String vname : varnames){
                Variable var = vars.get(vname);
                if(var.isNumeric()){
                    names.add(var.name);
                }
            }
        }
        return names;
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
    public double[][] getNumericalData(String[] names){
        int n =this.get(names[0]).getNumericalData().length;
        int m = names.length;
        double[][] data = new double[m][n];
        for(int j=0;j<names.length;j++){
            data[j] = this.get(names[j]).getNumericalData();
        }
        return data;
    }
    public void addNumericalData(String name[], double[][] data){
        int n = varnames.size();
        int m = name.length;
        for(int i=0;i<m;i++){
            addNumericalData(name[i], data[i]);
        }
    }
    public void addNumericalData(String name, double[] data){
        int n = varnames.size();
        if(varnames.contains(name)){
            addNumericalData(name+"c",data);
        }else{
            add(new Variable(name, data));
        }
    }
}
