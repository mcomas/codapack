package coda;

import coda.ext.json.JSONArray;
import coda.ext.json.JSONException;
import coda.ext.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mcomas
 */
public class DataFrame extends HashMap<String, Variable>{
    public static final long serialVersionUID = 1L;    

    public String name;
    private ArrayList<String> varnames = new ArrayList<String>();
    //private int number_rows = 0;

    
    public DataFrame(){
        name = "default";
    }
/**
 *
 *  @return maximum number of rows
 */
    public int getMaxVariableLength(){
        int number_rows = 0;
        for(Variable v: this.values()){
            number_rows = (number_rows < v.size() ? v.size() : number_rows);
        }
        return number_rows;
    }
/**
 *  @param  col   column number
 *
 *  @return     variable name
 */
    public String getName(int col){
        return varnames.get(col);
    }
/**
 *  @return     variable name array
 */
    public String[] getNames01(){
        return (String[])varnames.toArray();
    }
    public ArrayList<String> getNames(){
        return varnames;
    }
    public Iterator getNamesIterator01(){
        return varnames.iterator();
    } 
    public Variable addData(String name, Variable data){
        
        int m = varnames.size();
        if(varnames.contains(name)){
            return addData(name+"c",data);
        }
        varnames.add(m, name);
        return this.put(name, data);
    }
    public Variable addData(String name, double[] data){

        int n = varnames.size();
        if(varnames.contains(name)){
            return addData(name+"c",data);
        }
        varnames.add(n, name);
        return this.put(name, new Variable(name, data));
    }
    public void addData(String name[], double[][] data){
        int n = varnames.size();
        int m = name.length;
        for(int i=0;i<m;i++){
            addData(name[i], data[i]);
        }
    }
    public Variable deleteData(String name){
        varnames.remove(name);
        return this.remove(name);
    }
    public Element[] getData(String name){
        Variable var = this.get(name);

        Element[] result = new Element[var.size()];

        for(int i=0;i<var.size();i++){
            result[i] = var.get(i);
        }
        return result;
    }
    public int[] getMapingToData(String[] names, boolean[] valid){
        int n = 0;
        for(int i=0; i< valid.length;i++)
            if(valid[i]) n++;
        int mapping[] = new int[n];

        int index = 0;
        for(int i=0; i<valid.length;i++){
            if(valid[i]){
                mapping[index] = i;
                index++;
            }
        }
        return mapping;
    }
    public boolean[] getValidData(String[] names){
        int n = this.get(names[0]).size();
        boolean valid[] = new boolean[n];
        double[] data;
        for(int i=0;i<n;i++) valid[i] = true;
        for(int j=0;j<names.length;j++){
            data = this.get(names[j]).getNumericalData();
            for(int i=0;i<n;i++){
                if( Double.isNaN(data[i]) ){
                    valid[i] = false;
                }
            }
        }
        return valid;
    }
    public boolean[] getValidCompositions(String[] names){
        int n = this.get(names[0]).size();
        boolean valid[] = new boolean[n];
        double[] data;
        for(int i=0;i<n;i++) valid[i] = true;
        for(int j=0;j<names.length;j++){
            data = this.get(names[j]).getNumericalData();
            for(int i=0;i<n;i++){
                if( Double.isNaN(data[i]) || data[i] <= Double.MIN_VALUE){
                    valid[i] = false;
                }
            }
        }
        return valid;
    }
    public boolean[] getValidCompositionsWithZeros(String[] names){
        int n = this.get(names[0]).size();
        boolean valid[] = new boolean[n];
        //double[] data;
        for(int i=0;i<n;i++) valid[i] = true;
        for(int j=0;j<names.length;j++){
            //data = this.get(names[j]).getNumericalData();
            for(int i=0;i<n;i++){
                Element o = this.get(names[j]).get(i);
                if(!(o instanceof Zero)){
                    Numeric o_n = (Numeric)o;
                    double data = o_n.getValue();
                    if( Double.isNaN(data) || data <= Double.MIN_VALUE){
                        valid[i] = false;
                    }
                }
            }
        }
        return valid;
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
    public double[][] getDetectionLevel(String[] names){
        int n = this.get(names[0]).size();
        int m = names.length;
        double[][] dlevel = new double[m][n];
        for(int j=0;j<names.length;j++){
            Variable data = this.get(names[j]);
            for(int i=0;i<data.size();i++){
                Element el = data.get(i);
                if(el instanceof Zero){
                    Zero zero = (Zero)el;
                    dlevel[j][i] = zero.detection;
                }else{
                    dlevel[j][i] = 0;
                }
            }            
        }
        return dlevel;
    }
    public double[][] getNumericalData(String[] names, int[] mapping){
        int n = mapping.length;
        int m = names.length;
        double[][] data = new double[m][n];
        double[] var;
        for(int j=0;j< m;j++){
            var = this.get(names[j]).getNumericalData();
            for(int i=0;i<n;i++)
                    data[j][i] = var[mapping[i]];
        }
        return data;
    }
    public double[][] getNumericalData(String[] names, boolean[] selection){
        int n = 0;
        for(int i=0;i<selection.length;i++) n += (selection[i] ? 1 : 0);

        int m = names.length;
        double[][] data = new double[m][n];
        double[] var;
        
        for(int j=0;j< m;j++){
            int k = 0;
            var = this.get(names[j]).getNumericalData();
            for(int i=0;i<selection.length;i++){
                if(selection[i]){
                    data[j][k] = var[i];
                    k++;
                }
            }
        }
        return data;
    }
    public double[] getNumericalData(String n){
        return this.get(n).getNumericalData();
    }
    public String[][] getCategoricalData(String[] names){
        int n = names.length;
        String[][] data = new String[n][];
        for(int i=0;i<n;i++){
            data[i] = this.get(names[i]).getTextData();
        }
        return data;
    }
    public int[] getDefinedGroup(String n){
        return this.get(n).getTextDataNumerized();
    }
    public String[] getCategoricalData(String n){
        return this.get(n).getTextData();
    }

    public JSONObject toJSON(){
        JSONObject dataFrame = new JSONObject();
        JSONArray variables = new JSONArray();
        try {
            dataFrame.put("name", name);
            for(int i=0;i<varnames.size();i++){
                variables.put(this.get(varnames.get(i)).toJSON());
            }
            dataFrame.put("variables", variables);
            dataFrame.put("number_rows", this.getMaxVariableLength());
        } catch (JSONException ex) {
            Logger.getLogger(DataFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataFrame;
    }
    public static DataFrame createFromJSON(JSONObject df){
        DataFrame dataFrame = new DataFrame();
        try {
            dataFrame.name = df.getString("name");
            JSONArray variables = df.getJSONArray("variables");
            Variable variable;
            for(int i=0;i<variables.length();i++){
                variable = Variable.createFromJSON(variables.getJSONObject(i));
                dataFrame.varnames.add(i, variable.getName());
                dataFrame.put(variable.getName(), variable);
            }
        } catch (JSONException ex) {
            Logger.getLogger(DataFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataFrame;
    }
}
