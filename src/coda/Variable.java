package coda;

import coda.ext.json.JSONArray;
import coda.ext.json.JSONException;
import coda.ext.json.JSONObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * Class containing the data related to some variable. CoDaPack admits
 * two type of variables: numeric and factor.
 * 
 * @author mcomas
 */
public class Variable extends ArrayList<Object>{
    public static final long serialVersionUID = 1L;
/**
 * Constant defining a variable as factor.
 * VAR_FACTOR = 1
 */
    public static final int VAR_FACTOR  = 1;
/**
 * Constant defining a variable as numeric.
 * VAR_NUMERIC = 2
 */
    public static final int VAR_NUMERIC = 2;
/**
 * Variable containing the name of a variable. The name of a variable
 * must be unique.
 */
    private String name;
/**
 * Variable type: factor or numeric.
 */
    private int dtype;
    public Variable(){ }
/**
 * Variable type is defined to numeric by default
 * @param n variable name
 */
    public Variable(String n){
        name = n;
        dtype = VAR_NUMERIC;
    }
/**
 *
 * @param n variable name
 * @param t variable type
 */
    public Variable(String n, int t){
        name = n;
        dtype = t;
    }
/**
 * The variable is set to numeric type
 * @param n variable name
 * @param data array containing the varible values
 */
    public Variable(String n, double[] data){
        name = n;
        dtype = VAR_NUMERIC;
        for(int i=0;i<data.length;i++) this.add(i , data[i]);
    }
/**
 * The variable is set to factor type
 * @param n variable name
 * @param data array containing the varible values
 */
    public Variable(String n, String[] data){
        name = n;
        dtype = VAR_FACTOR;
        for(int i=0;i<data.length;i++) this.add(i , data[i]);
    }
/**
 *
 * @param data
 */
    
    public void setData(double[] data){
        dtype = VAR_NUMERIC;
        for(int i=0;i<data.length;i++) this.add(i , data[i]);
    }     
/**
 *
 * @param data
 */
    public void setData(String[] data){
        dtype = VAR_FACTOR;
        for(int i=0;i<data.length;i++) this.add(i , data[i]);
    }
/**
 *
 * @param data
 */
    public void setData(Object[] data){
        this.clear();
        for(int i=0;i<data.length;i++) this.add(i , data[i]);
    }
/**
 *
 * @param data
 * @param t
 */
    public void setData(Object[] data, int t){
        this.clear();        
        for(int i=0;i<data.length;i++) this.add(i , data[i]);
        dtype = t;
    }
    @Override
    public Object get(int index){
        if(index < this.size()){
            Object o = super.get(index);
            if(!(o instanceof ZeroData))
                return o;
            ZeroData z = (ZeroData)o;
            return z;
        }
        return null;
    }
    public double getValue(int index){
        if(index < this.size()){
            Object o = super.get(index);
            if(!(o instanceof ZeroData))
                return (Double)o;
            ZeroData z = (ZeroData)o;
            return z.getValue();
        }
        return Double.NaN;
    }
/**
 *
 * @param n
 */
    public void setName(String n){
        name = n;
    }
/**
 *
 * @return
 */
    public String getName(){
        return name;
    }
/**
 *
 * @param type
 */
    public void setType(int type){
        dtype = type;
    }
/**
 *
 * @return
 */
    public int getType(){
        return dtype;
    }
/**
 *
 * @return
 */
    public double[] getNumericalData(){
        if( dtype == VAR_NUMERIC ){
            int n = size();
            double[] data = new double[n];
            for(int i=0; i<n; i++){
                if(get(i) instanceof ZeroData)
                    data[i] = Double.MIN_VALUE;
                else
                    data[i] = (Double)get(i);
            }
            return data;
        }
        return null;
    }
/**
 *
 * @return
 */
    public String[] getCategoricalData(){
        if( dtype == VAR_FACTOR ){
            int n = size();
            String[] data = new String[n];
            for(int i=0; i<n; i++) data[i] = (String)get(i);
            return data;
        }else{
            int n = size();
            String[] data = new String[n];
            for(int i=0; i<n; i++) data[i] = ((Double)this.getValue(i)).toString();
            return data;
        }
    }
    public int[] getCategoricalDataNumerized(){
        if( dtype == VAR_FACTOR ){
            int n = size();
            int[] data = new int[n];
            for(int i=0; i<n; i++) data[i] = 0;
            String actual;
            int k = 1;
            for(int i=0; i<n; i++){
                actual = (String)get(i);
                if(data[i] == 0){
                    for(int j=i; j<n;j++){
                        if(actual.equals((String)get(j))){
                            data[j] = k;
                        }
                    }
                    k++;
                }
            }
            for(int i=0; i<n; i++) data[i]--;
            //for(int i=0; i<n; i++) data[i] = (String)get(i);
            return data;
        }
        return null;
    }
/**
 *
 */
    public void categorize(){
        String value;
        setType(VAR_FACTOR);
        for(int i=0;i<this.size();i++){
            value = ((Double)this.getValue(i)).toString();
            this.set(i, value);
        }        
    }
    /**
 *
 */
    public void numerize(){
        double value;
        setType(VAR_NUMERIC);
        for(int i=0;i<this.size();i++){
            value = Double.parseDouble((String)this.get(i));
            this.set(i, value);
        }
    }
/**
 *
 * @return
 */
    public boolean isNumeric(){
        if(dtype == VAR_NUMERIC ) return true;
        return false;
    }
/**
 * 
 * @return
 */
    public boolean isFactor(){
        if(dtype == VAR_FACTOR ) return true;
        return false;
    }
    public JSONObject toJSON(){
        JSONObject variable = new JSONObject();
        JSONArray values = new JSONArray();

        try {
            variable.put("n", name);
            variable.put("t", dtype);
            if( dtype == VAR_NUMERIC ){               
                for(int i=0;i<size();i++){
                    JSONObject obj = new JSONObject();
                    Object o = get(i);
                    if( o instanceof ZeroData){
                        ZeroData zero = (ZeroData)o;
                        obj.put("l", zero.getDetectionLevel());
                    }else{
                        Double value = (Double)o;
                        obj.put("v", value.toString());
                        /*
                        if(value.isNaN()){
                            obj.put("value", "NaN");
                        }else{
                            obj.put("value", value);
                        }
                         * */
                    }
                    values.put(obj);
                }
            }else
                for(int i=0;i<size();i++) values.put((String)get(i));
            variable.put("a", values);
        } catch (JSONException ex) {
            Logger.getLogger(Variable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return variable;
    }
    public static Variable createFromJSON(JSONObject var){
        Variable variable = new Variable();
        try {
            variable.name = var.getString("n");
            variable.dtype = var.getInt("t");
            JSONArray values = var.getJSONArray("a");
            if(variable.dtype == VAR_NUMERIC){
                for(int i=0;i<values.length();i++){
                    JSONObject object = values.getJSONObject(i);
                    if(object.has("l")){
                        variable.add(i, new ZeroData(Double.parseDouble(object.getString("l"))));//Double.parseDouble(object.getString("value")));
                    }else{
                        variable.add(i, Double.parseDouble(object.getString("v")));
                    }
                }
            }else{
                for(int i=0;i<values.length();i++){
                    variable.add(i, values.getString(i));
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(Variable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return variable;
    }
}
