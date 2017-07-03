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
 * Class containing the data related to some variable. CoDaPack admits
 * two type of variables: numeric and factor.
 * 
 * @author mcomas
 */
public class Variable extends ArrayList<Element>{
/**
 * Constants defining variable type.
 */
    public static final int VAR_TEXT  = 1;
    public static final int VAR_NUMERIC = 2;

    protected String name; // Variable name
    
    private int dtype;   // Variable type (either text or numeric)
    
    public Variable(){ }
/**
 * Variable type is defined to numeric by default
 * @param n variable name
 */
    public Variable(String n){
        name = n;
        dtype = VAR_NUMERIC; // type is numeric by default
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
 * @param data array containing the variable values
 */
    public Variable(String n, double[] data){
        name = n;
        dtype = VAR_NUMERIC;
        for(int i=0;i<data.length;i++){
            if(data[i] == 0)
                this.add(i, new Zero());
            else if(Double.isNaN(data[i]))
                this.add(i, new NonAvailable());
            else
                this.add(i , new Numeric(data[i]));
        }
    }
/**
 * The variable is set to factor type
 * @param n variable name
 * @param data array containing the variable values
 */
    public Variable(String n, String[] data){
        name = n;
        dtype = VAR_TEXT;
        for(int i=0;i<data.length;i++) 
            this.add(i , new Text(data[i]));
    }
     public Element setElementFromString(String v) {
        if(this.isText()){
            return new Text(v).variable(this);
        }
        if("na".equals(v.toLowerCase())){
            return new NonAvailable().variable(this);
        }
        try{
            double vd = Double.parseDouble(v);
            if(Double.isNaN(vd))
                return new NonAvailable().variable(this);
            if(vd == 0)
                return new Zero(0).variable(this);
            return new Numeric(vd).variable(this);
        }catch(NumberFormatException e){
            double dl = Zero.isZero(v);
            if(dl >= 0)
                return new Zero(dl).variable(this);
            else
                return new Text(v).variable(this);
        }
    }
/**
 *
 * @param data
 */
//    public void setData(Object[] data){
//        this.clear();
//        for(int i=0;i<data.length;i++) this.add(i , data[i]);
//    }
    public void add(String data){
        super.add(new Text(data));
    }
    public void add(double data){
        if(data == 0)
            add(new Zero());
        else
            add(new Numeric(data));
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
                data[i] = ((Numeric)get(i)).getValue();
            }
            return data;
        }
        return null;
    }
/**
 *
 * @return
 */
    public String[] getTextData(){
        if( dtype == VAR_TEXT ){
            int n = size();
            String[] data = new String[n];
            for(int i=0; i<n; i++) data[i] = ((Text)get(i)).getValue();
            return data;
        }else{
            int n = size();
            String[] data = new String[n];
            for(int i=0; i<n; i++){                
                data[i] = ((Numeric)get(i)).getValue().toString();
            }
            return data;
        }
    }
/**
 * @return an integer array with a different integer for each different
 * text contained inside the variable
 */
    public int[] getTextDataNumerized(){
        if( dtype == VAR_TEXT ){
            HashMap<String, Integer> hash = new HashMap<String, Integer>();
            int n = size();
            int[] data = new int[n];
            int k = 0;
            for(int i=0; i<n; i++){
                String actual = ((Text)get(i)).getValue();
                if(!hash.containsKey(actual)){
                    hash.put(actual, k++);
                }
                data[i] = hash.get(actual);
            }
            return data;
        }
        return null;
    }
/**
 *
 */
    public void toText(){
        String value = null;
        setType(VAR_TEXT);
        for(int i=0;i<this.size();i++){
            Element el = this.get(i);
            if(el instanceof Numeric){
                Numeric el_n = (Numeric)el;
                value = (el_n.getValue()).toString();
                this.set(i, new Text(value));
            }else{
                this.set(i, el);
            }
        }        
    }
    /**
 *
 */
    public void toNumeric(){
        double value;
        setType(VAR_NUMERIC);
        for(int i=0;i<this.size();i++){
            Element el = this.get(i);
            if(el instanceof Text){
                Text el_t = (Text)el;
                value = Double.parseDouble(el_t.getValue());
                if(value == 0)
                    this.set(i, new Zero());
                else
                    this.set(i, new Numeric(value));
            }else{
                this.set(i, el);
            }
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
    public boolean isText(){
        if(dtype == VAR_TEXT ) return true;
        return false;
    }
}
