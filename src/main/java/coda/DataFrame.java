/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió
 *
 *	This file is part of CoDaPack.
 *
 *  CoDaPack is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CoDaPack is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CoDaPack.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda;


import coda.ext.json.JSONArray;
import coda.ext.json.JSONException;
import coda.ext.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * DataFrame class is the main object used for storing {@link Variable} classes in CoDaPack. 
 * DataFrame is extended from {@link HashMap} using variable names as main keys.
 * The main difference from {@link HashMap} is that DataFrame keeps the 
 * variables ordered.
 * 
 * @author marc
 */
public class DataFrame extends HashMap<String, Variable>{
    /**
     *
     */
    public static final long serialVersionUID = 1L;
    /**
     * Private variable:
     *  - name: DataFrame name.
     *  - varnames: Array containing the variable names ordered.
     */
    public String name;
    public boolean change = false;
    private ArrayList<String> varnames = new ArrayList<String>();
    /**
     * Default constructor. DataFrame is initialized with name "default"
     */
    public DataFrame(){
        this.name = "default";
    }
    /**
     * Constructor used to initialize DataFrame with a name
     *
     * @param name Constructor initializing the DataFrame name
     */
    public DataFrame(String name){
        this.name = name;
    }
    /**
     * Sets the name of this DataFrame
     *
     *  @param  name Set the name for the DataFrame
     *
     */
    public void setName(String name){
        change = true;
        this.name = name;
    }
    /**
     * Returns the name of this DataFrame
     *
     *  @return  name the name
     *
     */
    public void setChange(boolean b) {
        change = b;
    }
    public String getName(){
    	return name;
    }
    public Boolean getChange() { return change; }
    public ArrayList<String> getNames(){
        return varnames;
    }
    public void rename(String oldname, String newname) throws DataFrameException{
        change = true;
        Variable var = this.get(oldname);
        if(this.containsKey(newname))
            throw new DataFrameException("Variable name already defined");
        
        var.setName(newname);
        super.put(newname, var);
        super.remove(oldname);
        int indexOf = varnames.indexOf(oldname);
        varnames.set(indexOf, newname);
        var.setName(newname);
        alertModification(var);
        
    }
    public Variable get(int index){
        return this.get(varnames.get(index));
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
     * With this method, a variable is added to DataFrame. If the variable
     * name is already defined, the method add a character 'c' at the end
     * of the name.
     * 
     * @param variable
     * @return
     */
    public Variable add(Variable variable) throws DataFrameException{
        change = true;
        if(varnames.contains(variable.name))
            throw new DataFrameException("Variable name already defined");
        int m = varnames.size();
        varnames.add(m, variable.name);
        Variable v = put(variable.name, variable);
        alertModification(v);
        return v;
    }
    public Variable replace(Variable variable) throws DataFrameException{
        change = true;
        if(!varnames.contains(variable.name))
            throw new DataFrameException("Variable does not exist");
        Variable v = put(variable.name, variable);
        alertModification(v);
        return v;
    }
    public Variable remove(String vname) throws DataFrameException{
        change = true;
        if(!varnames.contains(vname))
            throw new DataFrameException("Variable does not exist");
        varnames.remove(vname);
        Variable v = super.remove(vname);
        alertModification(v);
        return v;
    }
    public Variable remove(Variable variable) throws DataFrameException{
        change = true;
        return remove(variable.name);
    }
    public static class DataFrameException extends Exception{
        public DataFrameException(String message){
            super(message);
        }
    }
    /*
     * Delegating events
     */
    ArrayList<DataFrameListener> listeners = new ArrayList<DataFrameListener>();
    public void addDataFrameListener(DataFrameListener listener){
        listeners.add(listener);
    }
    public void removeDataFrameListener(){
        listeners.clear();
    }
    public void alertModification(Variable v){
        for(DataFrameListener e: listeners){
            e.dataFrameModified(this);
        }
    }
    public interface DataFrameListener{
        public void dataFrameModified(DataFrame v);
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
    public double[][] getNumericalData(String[] names){
        int n =this.get(names[0]).getNumericalData().length;
        int m = names.length;
        double[][] data = new double[m][n];
        for(int j=0;j<names.length;j++){
            data[j] = this.get(names[j]).getNumericalData();
        }
        return data;
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
    public Variable addData(String name, Variable data){
        change = true;
        int m = varnames.size();
        if(varnames.contains(name)){
            return addData(name+"c",data);
        }
        varnames.add(m, name);
        return this.put(name, data);
    }
    public Variable addData(String name, double[] data){
        change = true;
        int n = varnames.size();
        if(varnames.contains(name)){
            return addData(name+"c",data);
        }
        varnames.add(n, name);
        return this.put(name, new Variable(name, data));
    }
    public void addData(String name[], double[][] data){
        change = true;
        int n = varnames.size();
        int m = name.length;
        for(int i=0;i<m;i++){
            addData(name[i], data[i]);
        }
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
