/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda;


/**
 *
 * @author mcomas
 */
public class Composition {
    private double[] components;

    public Composition(int size){
        components = new double[size];
    }
    public Composition(double[] components){
        this.components = new double[components.length];
        System.arraycopy(components, 0, this.components, 0, components.length);
    }
    public double[] components(){ return components; }

    public double norm(){
        int m = components.length;
        double norm = 0;
        double ln[] = new double[m];
        for(int i=0;i<m;i++)
            ln[i] = Math.log(components[i]);

        double temp;
        for(int i=0;i<m;i++){
            for(int j=0;j<m;j++){
                temp = (ln[i]-ln[j]);
                norm += temp*temp;
            }
        }
        return Math.sqrt( norm/(2.0*m) );
    }
    public void set(int i, double value){ components[i] = value; }

    public double get(int i){ return components[i]; }

    public double[] array(){
        return components;
    }
    public double[] arrayCopy(){
        double res[] = new double[components.length];
        System.arraycopy(components, 0, res, 0, components.length);
        return res;
    }
    public int size(){ return components.length; }

    public Composition perturbate(Composition p){
        int m = p.size();
        if(components.length != m) return null;

        Composition result = new Composition(m);
        for(int i=0;i<m;i++) 
            result.set(i, components[i] * p.get(i));
        
        return result;
    }
    public Composition perturbate(Composition p, Composition result){
        int m = p.size();
        if(components.length != m) return null;

        for(int i=0;i<m;i++)
            result.set(i, components[i] * p.get(i));

        return result;
    }
    public Composition power(double p){

        Composition result = new Composition(components.length);
        for(int i=0;i<components.length;i++)
            result.set(i, Math.pow(components[i], p) );
        
        return result;
    }
    public Composition power(double p, Composition result){

        for(int i=0;i<components.length;i++)
            result.set(i, Math.pow(components[i], p) );

        return result;
    }
    @Override
    public String toString(){
        String output = "";
        int m = components.length - 1;
        for(int i=0;i<m;i++)
            output += components[i] + ";";
        output += components[m];
        return output;
    }
}
