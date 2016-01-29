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

import coda.ext.jama.Matrix;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author marc
 */
public class Utils {
     /**
 *  @param x
 *  @param selection
 *
 *  @return subarray of x according to selection
 */
    public static String[] reduceData(String[] x, boolean selection[]){

        int n = 0;
        for(boolean item : selection) n += (item ? 1 : 0);
        String[] result = new String[n];
            for(int k=0, j=0; k<x.length; k++){
                if(selection[k]){
                    result[j++] = x[k];
                }
            }
        return result;
    }
/**
 *  @param  x  integer vector to by reduced
 *  @param selection boolean vector
 *
 *  @return subarray of x according to selection
 */
    public static int[] reduceData(int[] x, boolean selection[]){

        int n = 0;
        for(boolean item : selection) n += (item ? 1 : 0);
        int[] result = new int[n];
            for(int k=0, j=0; k<x.length; k++){
                if(selection[k]){
                    result[j++] = x[k];
                }
            }
        return result;
    }
    /**
     *
     * @param x
     * @param groups
     * @param group
     * @return
     */
    public static double[] reduceData(double[] x, int groups[], int group){

        int n = 0;
        for(int item : groups) n += (item == group ? 1 : 0);
        double[] result = new double[n];

        for(int k=0, j=0; k<x.length; k++){
            if(groups[k] == group){
                result[j++] = x[k];
            }
        }
        return result;
    }
        /**
     *
     * @param x
     * @param groups
     * @param group
     * @return
     */
    public static double[][] reduceData(double[][] x, int groups[], int group){

        int n = 0;
        int m = x.length;
        for(int item : groups) n += (item == group ? 1 : 0);
        double[][] result = new double[m][n];
        for(int i=0;i<m;i++){
            for(int k=0, j=0; k<x[0].length; k++){
                if(groups[k] == group){
                    result[i][j++] = x[i][k];
                }
            }
        }
        return result;
    }
/*    public static double[][] reduceData(double[][] x, int groups[], int group){
        int m=x.length;
        int D = 0;
        for(int item : groups) D += (item == group ? 1 : 0);
        double[][] result = new double[m][D];

        for(int i=0; i<m;i++){
            for(int k=0, j=0; k<x[0].length; k++){
                if(groups[k] == group){
                    result[i][j++] = x[i][k];
                }
            }
        }
        return result;
    }*/
    /**
     *
     * @param x
     * @param selection
     * @return
     */
    public static double[][] reduceData(double[][] x, boolean selection[]){
        int m=x.length;
        int n = 0;
        for(boolean item : selection) n += (item ? 1 : 0);
        double[][] result = new double[m][n];

        for(int i=0; i<m;i++){
            for(int k=0, j=0; k<x[0].length; k++){
                if(selection[k]){
                    result[i][j++] = x[i][k];
                }
            }
        }
        return result;
    }
       /**
     *
     * @param data
     * @param selection
     * @return
     */
    public static String[] getCategories(String data[], boolean selection[]){
        HashSet<String> set = new HashSet<String>();
        ArrayList<String> cat = new ArrayList<String>();
        for(int i=0;i<data.length;i++){
            if(selection[i]){
                if(set.add(data[i])){
                    cat.add(data[i]);
                }
            }
        }
        set = null;
        return cat.toArray(new String[cat.size()]);
    }
    /**
     *
     * @param x
     * @param selection
     * @return
     */
    public static double[][] recoverData(double[][] x, boolean selection[]){
        int m = x.length;
        int n = selection.length;

        double[][] result = new double[m][n];

        for(int i=0; i<m;i++){
            for(int j=0, k =0; j<n; j++){
                if(selection[j]){
                    result[i][j] = x[i][k++];
                }else{
                    result[i][j] = Double.NaN;
                }
            }
        }
        return result;
    }
    public static double[] recoverData(double[] x, boolean selection[]){
        int m = x.length;
        int n = selection.length;

        double[] result = new double[n];


        for(int j=0, k =0; j<n; j++){
            if(selection[j]){
                result[j] = x[k++];
            }else{
                result[j] = Double.NaN;
            }
        }

        return result;
    }
    public static Matrix getMatrixF(int D){
        Matrix F = new Matrix(D,D);
        for(int i=0;i<D;i++)
            for(int j=0;j<D;j++)
                F.set(i, j, i==j ? (double)(D-1)/D : -(double)1/D);
        return F;

    }
    public static Matrix getDiagonalMatrix(double diag[]){
        Matrix Diag = new Matrix(diag.length,diag.length);
        for(int i=0;i<diag.length;i++)
            for(int j=0;j<diag.length;j++)
                Diag.set(i, j, i==j ? diag[i] : 0);
        return Diag;

    }
}
