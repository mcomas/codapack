/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui;

import coda.DataFrame;
import coda.gui.menu.ALRMenu;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.renjin.primitives.matrix.Matrix;
import org.renjin.sexp.DoubleArrayVector;
import org.renjin.sexp.Vector;

/**
 *
 * @author marcc
 */
public class Reval {
    static ScriptEngine engine = new ScriptEngineManager().getEngineByName("Renjin");
    public Reval(){
        
    }
    public double[][] alr(DataFrame dataframe, String vnames[]){
        int K = vnames.length;
        StringJoiner joiner = new StringJoiner(",");
        if(engine == null) {
            throw new RuntimeException("Renjin Script Engine not found on the classpath.");
        }
        Vector res = null;
        try {
            for(int i = 0; i < K; i++){
                engine.put("x" + (i+1), new DoubleArrayVector(dataframe.get(vnames[i]).getNumericalData()));
                joiner.add("x" + (i+1));
            }
            InputStream in = getClass().getResourceAsStream(CoDaPackConf.RESOURCE_PATH + "alr.R");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            engine.eval(reader);
            res = (Vector)engine.eval("alr(cbind(" + joiner.toString() + "))");
        } catch (ScriptException ex) {
            Logger.getLogger(ALRMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        Matrix matrix = null;
        try {
            matrix = new Matrix(res);
        } catch(IllegalArgumentException e) {
            System.out.println("Result is not a matrix: " + e);
        }
        double result[][] = new double[matrix.getNumCols()][matrix.getNumRows()];
        for(int i = 0; i < matrix.getNumRows(); i++){
            for(int j = 0; j < matrix.getNumCols(); j++){
                result[j][i] = matrix.getElementAsDouble(i, j);
            }
        }
        return result;
    }
    public double[][] alrInv(DataFrame dataframe, String vnames[]){
        int K = vnames.length;
        StringJoiner joiner = new StringJoiner(",");
        if(engine == null) {
            throw new RuntimeException("Renjin Script Engine not found on the classpath.");
        }
        Vector res = null;
        try {
            for(int i = 0; i < K; i++){
                engine.put("x" + (i+1), new DoubleArrayVector(dataframe.get(vnames[i]).getNumericalData()));
                joiner.add("x" + (i+1));
            }
            InputStream in = getClass().getResourceAsStream(CoDaPackConf.RESOURCE_PATH + "alr.R");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            engine.eval(reader);
            res = (Vector)engine.eval("alrInv(cbind(" + joiner.toString() + "))");
        } catch (ScriptException ex) {
            Logger.getLogger(ALRMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        Matrix matrix = null;
        try {
            matrix = new Matrix(res);
        } catch(IllegalArgumentException e) {
            System.out.println("Result is not a matrix: " + e);
        }
        double result[][] = new double[matrix.getNumCols()][matrix.getNumRows()];
        for(int i = 0; i < matrix.getNumRows(); i++){
            for(int j = 0; j < matrix.getNumCols(); j++){
                result[j][i] = matrix.getElementAsDouble(i, j);
            }
        }
        return result;
    }
}
