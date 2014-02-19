package coda;

import coda.ext.json.JSONArray;
import coda.ext.json.JSONException;
import coda.ext.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mcomas
 */
public class DataFrame extends HashMap<String, Variable>{
    public static final long serialVersionUID = 1L;    
    /**
     * Dataframe name
     */
    public String name;
    /**
     * Variable names
     */
    private ArrayList<String> varnames = new ArrayList<String>();
    private int number_rows = 0;
    /**
     * Data values
     */
    public DataFrame(){
        name = "default";
    }
/**
 *
 *  @return maximum number of rows
 */
    public int getRowCount(){
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
    /*
    public void setNewVariable(int pos, String name, String type){
        varnames.add(pos, name);
        put(name, new Variable(name, type));
    }*/    
    public Variable addData(String name, Variable data){
        number_rows = (data.size() > number_rows ? data.size() : number_rows);
        
        int m = varnames.size();
        if(varnames.contains(name)){
            return addData(name+"c",data);
        }
        varnames.add(m, name);
        return this.put(name, data);
    }
    public Variable addData(String name, double[] data){
        number_rows = (data.length > number_rows ? data.length : number_rows);

        int n = varnames.size();
        if(varnames.contains(name)){
            return addData(name+"c",data);
        }
        varnames.add(n, name);
        return this.put(name, new Variable(name, data));
    }
    public void addData(String name[], Composition[] data){
        int n = varnames.size();
        int m = name.length;
        for(int i=0;i<m;i++){
            addData(name[i], data[i].components());
        }
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
    public Object[] getData(String name){
        Variable var = this.get(name);

        Object[] result = new Object[var.size()];

        for(int i=0;i<var.size();i++){
            result[i] = var.get(i);
            /*
            if(var.isNumeric()){
                result[i] = (Double)var.get(i);
            }else{
                result[i] = (String)var.get(i);
            }
             * */
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
                Object o = this.get(names[j]).get(i);
                if(!(o instanceof ZeroData)){
                    double data = (Double)o;
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
            Object[] data = this.get(names[j]).toArray();
            for(int i=0;i<data.length;i++){
                if(data[i] instanceof ZeroData){
                    ZeroData zero = (ZeroData)data[i];
                    dlevel[j][i] = zero.getDetectionLevel();
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
            data[i] = this.get(names[i]).getCategoricalData();
        }
        return data;
    }
    public int[] getDefinedGroup(String n){
        return this.get(n).getCategoricalDataNumerized();
    }
    public String[] getCategoricalData(String n){
        return this.get(n).getCategoricalData();
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
            dataFrame.put("number_rows", number_rows);
        } catch (JSONException ex) {
            Logger.getLogger(DataFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataFrame;
    }
    public static DataFrame createFromJSON(JSONObject df){
        DataFrame dataFrame = new DataFrame();
        try {
            dataFrame.name = df.getString("name");
            dataFrame.number_rows = df.getInt("number_rows");
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
