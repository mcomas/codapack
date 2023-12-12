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

import coda.ext.jama.Matrix;
import coda.ext.jama.SingularValueDecomposition;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.BetaDistributionImpl;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math.distribution.FDistributionImpl;
import org.apache.commons.math.distribution.NormalDistributionImpl;
/**
 *
 * In this class different functions related with compositional
 * data analisys are implemented.
 *
 * This class, concretely the biplot function, needs of the Common Maths Library available at
 * http://commons.apache.org/math/
 * 
 *
 * @author mcomas
 *
 */

public class CoDaStats{
    // Private contructor to avoid class construction
    private CoDaStats(){ }
/**
 *  @param x vector to be closured
 *
 *  @return double[M] vector x closured to 1
 */
    public static double[] closure(double[] x){
        int m = x.length;
        double total = 0;
        for(int i=0; i<m;i++) total += x[i];
        for(int i=0; i<m;i++) x[i] /= total;

        return x;
    }
/**
 * 
     * @param x matrix 
 */
    public static double[] rowSum(double[][] x){
        
        int m = x.length;
        int n = x[0].length;
        double[] res = new double[n];
        for(int j=0;j<n;j++){
            res[j] = 0;
            for(int i=0; i<m;i++) res[j] += x[i][j];
        }
        return res;
    }
/**
 *  @param x vector to be closured
 *  @param  w   weigth to be be closured to
 *
 *  @return double[M] vector x closured to 1
 */
    public static double[] closure(double[] x, double w){
        int m = x.length;
        double total = 0;
        for(int i=0; i<m;i++) total += x[i];
        for(int i=0; i<m;i++) x[i] *= w / total;

        return x;
    }
    public static double confidenceRegion(int D, int N, double confidence){

        double n = N;
        double d = D - 1;
        double b = 0;
        try {
            FDistributionImpl fDist =
                    new FDistributionImpl(d, n-d);
            b = fDist.inverseCumulativeProbability(confidence);
            //val = Beta.regularizedBeta(7.26 / (7.26 + 22), 1, 10.5);
        } catch (MathException ex) { }

        return b * (n-1) * d / ((n-d) * n);
    }
    public static double predictiveRegion(int D, int N, double confidence){
        double d = D - 1;
        double n = N - 1;

        double b = 0;
        try {
            BetaDistributionImpl beta = 
                    new BetaDistributionImpl(0.5 * d, 0.5 * (n-d+1));
            b = beta.inverseCumulativeProbability(confidence);
            //val = Beta.regularizedBeta(7.26 / (7.26 + 22), 1, 10.5);
        } catch (MathException ex) { }
        
        return (b*n)/(1-b);
    }
/**
 *  @param  x   matrix to be closured
 *  @param  w   weigth to be be closured to
 *
 *  @return     double[M][N] matrix x closured to w
 */
    public static double[][] closure(double[][] x, double w){
        int m = x.length;
        int n = x[0].length;
        double total;
        for(int j=0;j<n;j++){
            total = 0;
            for(int i=0; i<m;i++) total += x[i][j];
            for(int i=0; i<m;i++) x[i][j] *= w / total;
        }

        return x;
    }
 /**
 *  @param  x   matrix to be closured
 *
 *  @return     double[M][N] matrix x closured to 1
 */
    public static double[][] closure(double[][] x){
        int m = x.length;
        int n = x[0].length;
        double total;
        for(int j=0;j<n;j++){
            total = 0;
            for(int i=0; i<m;i++) total += x[i][j];
            for(int i=0; i<m;i++) x[i][j] /= total;
        }
        return x;
    }
 /**
 *  @param  x   composition x
 *  @param  y   composition y
 *
 *  @return     Aitchison distance between x and y
 */
    public static double distanceAitchison(double[] x, double[] y){
        if(x.length != y.length) return -1;

        int m = x.length;
        double logx[] = new double[m];
        double logy[] = new double[m];
        for(int i=0;i<m;i++){
            logx[i] = Math.log(x[i]);
            logy[i] = Math.log(y[i]);
        }
        double sum = 0;
        double val;
        for(int i=0; i<m; i++){
            for(int j=0; j<m; j++){
                val = (logx[i]-logx[j]) - (logy[i]-logy[j]);
                sum += val * val;
            }
        }
        return Math.sqrt(0.5 * sum / (double)m );
    }

 
/**
 *  @param  data     double[M][N] matrix consisting of N vectors of size M to be centered.
 *
 *  @return centered double[M][N] matrix
 */
    public static double[][] centerData(double[][] data){

        double[] center = center(data);

        for(int i=0;i<center.length;i++) center[i] = 1 / center[i];
        return perturbateData(data, center);

    }

    /**
     *
     * @param data
     * @param k
     * @return
     */
    public static double[][] powerData(double[][] data, double k){
        int m = data.length;
        int n = data[0].length;
        double[][] result = new double[m][n];

        for(int i=0; i<m;i++){
            for(int j=0; j<n; j++){
                result[i][j] = Math.pow(data[i][j], k) ;
            }
        }
        return result;
    }
    /**
     *
     * @param a
     * @param k
     * @return
     */
    public static double[] powering(double[] a, double k){
        double[] res = new double[a.length];
        for(int i=0;i<a.length;i++){
            res[i] = Math.pow(a[i] ,k);
        }
        return res;
    }
    /**
     *
     * @param a
     * @param k
     * @param res
     * @return
     */
    public static double[] powering(double[] a, double k, double res[]){
        for(int i=0;i<a.length;i++){
            res[i] = Math.pow(a[i] ,k);
        }
        return res;
    }
    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static double[] perturbation(double[] a, double[] b){
        double[] res = new double[a.length];
        for(int i=0;i<a.length;i++){
            res[i] = a[i] * b[i];
        }
        return res;
    }
    
    /**
     *
     * @param a
     * @param b
     * @param res
     * @return
     */
    public static double[] perturbation(double[] a, double[] b, double res[]){
        for(int i=0;i<a.length;i++){
            res[i] = a[i] * b[i];
        }
        return res;
    }
/**
 *  @param  data     double[M][N] matrix consisting of N vectors of size M to be perturbated.
 *  @param perturbation double[M] perturbation to be applied to the N vectors of data
 *
 *  @return pertubated double[M][N] matrix
 */
    public static double[][] perturbateData(double[][] data, double[] perturbation){
        int m = data.length;
        int n = data[0].length;
        double[][] result = new double[m][n];

        for(int i=0; i<m;i++){
            for(int j=0; j<n; j++){
                result[i][j] = data[i][j] * perturbation[i];
            }
        }
        return result;
    }
/**
 *  @param  data     double[M][N] matrix consisting of N vectors of size M to be perturbated.
 *  @param perturbation double[M] perturbation to be applied to the N vectors of data
 *
 *  @param select
 * @return pertubated double[M][N] matrix
 */
    public static double[][] perturbateData(
            double[][] data,
            double[] perturbation,
            boolean select[]){
        int m = data.length;
        int n = data[0].length;
        double[][] result = new double[m][n];

        for(int i=0; i<m;i++){
            for(int j=0; j<n; j++){
                if(select[j]){
                    result[i][j] = data[i][j] * perturbation[i];
                }else{
                    result[i][j] = Double.NaN;
                }
            }
        }
        return result;
    }
    /**
     *
     * @param data
     * @param combination
     * @return
     */
    public static double[][] amalgamateData(
            double[][] data,
            double[] combination){
        int m = data.length;
        int n = data[0].length;
        double[][] result = new double[1][n];

        for(int i=0; i<n;i++){
            double sum = 0;
            for(int j=0; j<m; j++)
                 sum += data[j][i] * combination[j];
            result[0][i] = sum;

        }
        return result;
    }
    /**
     *
     * @param data
     * @param combination
     * @param select
     * @return
     */
    public static double[][] amalgamateData(
            double[][] data,
            double[] combination,
            boolean select[]){
        int m = data.length;
        int n = data[0].length;
        double[][] result = new double[1][n];

        for(int i=0; i<n;i++){
            if(select[i]){
                double sum = 0;
                for(int j=0; j<m; j++)
                     sum += data[j][i] * combination[j];
                result[0][i] = sum;
            }else{
                result[0][i] = Double.NaN;
            }
        }
        return result;
    }
/**
 *  @param  data     double[M][N] matrix consisting of N vectors of size M
 *
 *  @return center of data consisting of a double[M] vector
 */
    private static int fillPartition(int partition[][], int row, int left, int right){
        if(right - left <= 1)
            return 0;
        if(right - left == 2){
            for(int value: partition[row])
                value = 0;
            partition[row][left] = 1;
            partition[row][left+1] = -1;
            return 1;
        }
        int middle = left + (1 + right - left)/2;
        int next_row = row;
        for(int value: partition[row])
            value = 0;
        for(int i=left;i<middle;i++){
            partition[row][i] = 1;
        }
        for(int i=middle;i<right;i++){
            partition[row][i] = -1;
        }
        next_row++;
        next_row += fillPartition(partition, next_row, left, middle);
        next_row += fillPartition(partition, next_row, middle, right);
        return next_row-row;
    }
    /**
     *
     * @param n_components
     * @return
     */
    public static int[][] defaultPartition(int n_components){
        int partition[][] = new int[n_components-1][n_components];       
        fillPartition(partition, 0, 0, n_components);
        return partition;
    }
    /**
     *
     * @param x
     * @return
     */
    public static double[] center(double[][] x){
        int m = x.length;
        int n = x[0].length;

        double[] center = new double[m];

        for(int i=0; i<m;i++){
            center[i] = 0;
            for(int j=0; j<n; j++){
                center[i] += Math.log(x[i][j]);
            }
            center[i] = Math.exp(center[i]/n);
        }

        center = closure(center);
        return center;
    }
    /**
     *
     * @param x
     * @return
     */
    public static double totalVariance(double[][] x){
        int m = x.length;
        double totalVar = 0;
        for(int i=0;i<m;i++){
            for(int j=i+1;j<m;j++){
                totalVar += x[i][j];
            }
        }
        return totalVar/m;
    }
    /**
     *
     * @param x
     * @return
     */
    public static double[][] variationArray(double[][] x){
        int m = x.length;
        int n = x[0].length;
        double mean = 0;
        double[][] varM = new double[m][m];

        for(int i=0;i<m;i++){
            for(int j=i+1;j<m;j++){
                mean =0;
                for(int k=0;k<n;k++)
                    mean += Math.log(x[j][k]/x[i][k]);
                mean /= n;
                varM[j][i] = mean;
                varM[i][j] = 0;
                for(int k=0;k<n;k++) varM[i][j] +=
                            (Math.log(x[j][k]/x[i][k]) - mean) *
                            (Math.log(x[j][k]/x[i][k]) - mean);
                varM[i][j] /= (n);
            }
        }
        return varM;
    }
    /**
     *
     * @param data
     * @return
     */
    public static double[] mahalanobisDistance(double[][] data){
        double mahDist[] = new double[data[0].length];


        Matrix Z = (new Matrix(
                transformRawCLR(centerData(data)))).transpose();

        SingularValueDecomposition svd =
            new SingularValueDecomposition(Z);
        return mahDist;
    }

    /**
     *
     * @param svd
     * @param dim
     * @param gamma
     * @return
     */
    public static double[][][] biplot(SingularValueDecomposition svd,
            int dim, double gamma){

        double sv[] = svd.getSingularValues();

        //Matrix K = svd.getS();
        //K = K.getMatrix(0, dim-1, 0, dim-1);

        Matrix L = svd.getU();
        L = L.getMatrix(0, L.getRowDimension()-1, 0, dim-1);

        Matrix M = svd.getV().transpose();
        M = M.getMatrix(0, dim-1, 0, M.getColumnDimension()-1);

        Matrix pK  = new Matrix(dim, dim );

        for(int i=0;i<dim;i++)
            for(int j = 0;j<dim;j++)
                pK.set(i, j, i == j ? Math.pow(sv[i], gamma) : 0 );

        double GH[][][] = new double[2][][];
        GH[0] = L.times(pK).transpose().getArray();

        for(int i=0;i<dim;i++)
            pK.set(i, i, Math.pow(sv[i], 1-gamma) );

        GH[1] = pK.times(M).transpose().getArray();

        return GH;
    }
/**
 *  @param  x  double[M][N] matrix consisting of N vectors of size M
 *
 *  @return Raw to ALR transformation of matrix x
 */
    public static double[][] transformRawALR(double[][] x){
        int m = x.length;
        int n = x[0].length;

        double[][] alr = new double[m-1][n];
        for(int i=0; i<m-1; i++){
            for(int j=0; j<n; j++){
                alr[i][j] = Math.log(x[i][j]/x[m-1][j]);
            }
        }
        return alr;
    }
/**
 *  @param  x  double[M][N] matrix consisting of N vectors of size M
 *
 *  @return ALR to Raw transformation of matrix x
 */
    public static double[][] transformALRRaw(double[][] x){
        int m = x.length;
        int n = x[0].length;
        double[][] raw = new double[m+1][n];

        for(int j=0; j<n; j++){
            raw[m][j] = 1;
        }
        for(int i=0; i<m; i++){
            for(int j=0; j<n; j++){
                raw[i][j] = Math.exp(x[i][j]);
            }
        }
        return closure(raw);
    }
    /**
     *
     * @param x
     * @return
     */
    public static double[] transformALRRaw(double[] x){
        int m = x.length;
        double[] raw = new double[m+1];

        raw[m] = 1;
        for(int i=0; i<m; i++){
            raw[i] = Math.exp(x[i]);
        }
        return closure(raw);
    }
/**
 *  @param  x  double[M][N] matrix consisting of N vectors of size M
 *
 *  @return CLR to Raw transformation of matrix x
 */
    public static double[][] transformCLRRaw(double[][] x){
        int m = x.length;
        int n = x[0].length;
        double[][] raw = new double[m][n];

        for(int i=0; i<m; i++){
            for(int j=0; j<n; j++){
                raw[i][j] = Math.exp(x[i][j]);
            }
        }
        return closure(raw);
    }
    /**
     *
     * @param x
     * @return
     */
    public static double[] transformILR2Raw3(double x, double y){
        double res[] = new double[3];
        res[0] = Math.exp( 0.4082483 * x + 0.7071068 * y);
        res[1] = Math.exp( 0.4082483 * x - 0.7071068 * y);
        res[2] = Math.exp(-0.8164966 * x);
        
        return res;
    }
    public static double[] transformILR3Raw4(double x, double y, double z){
        double res[] = new double[4];
        res[0] = Math.exp( 0.2886751 * x + 0.4082483 * y + 0.7071068 * z);
        res[1] = Math.exp( 0.2886751 * x + 0.4082483 * y - 0.7071068 * z);
        res[2] = Math.exp( 0.2886751 * x - 0.8164966 * y);
        res[3] = Math.exp(-0.8660254 * x);
        return res;
    }
    public static double[] transformCLRRaw(double[] x){
        int m = x.length;
        double[] raw = new double[m];

        for(int i=0; i<m; i++){
            raw[i] = Math.exp(x[i]);
        }
        return closure(raw);
    }
/**
 *  @param  x  double[M][N] matrix consisting of N vectors of size M
 *
 *  @return Raw to CLR transformation of matrix x
 */
    public static double[][] transformRawCLR(double[][] x){
        int m = x.length;
        int n = x[0].length;
        double[][] clr = new double[m][n];
        double geometric_mean[] = new double[n];
        for(int j=0; j<n; j++){
            geometric_mean[j] = 0;
            for(int i=0; i<m; i++){
                geometric_mean[j] += Math.log(x[i][j]);
            }
            geometric_mean[j] = Math.exp(geometric_mean[j]/m);
        }
        for(int i=0; i<m; i++){
            for(int j=0; j<n; j++){
                clr[i][j] = Math.log(x[i][j]/geometric_mean[j]);
            }
        }
        return clr;
    }
/**
 *  @param  x  double[M][N] matrix consisting of N vectors of size M
 *
 *  @return Raw to CLR transformation of matrix x
 */
    public static double[] transformRawCLR(double[] x){
        int m = x.length;
        double[] clr = new double[m];
        double geometric_mean;

            geometric_mean = 0;
            for(int i=0; i<m; i++){
                geometric_mean += Math.log(x[i]);
            }
            geometric_mean = Math.exp(geometric_mean/m);

        for(int i=0; i<m; i++){
                clr[i] = Math.log(x[i]/geometric_mean);
        }
        return clr;
    }
    public static double[] clr(double x[]){
        int m = x.length;
        double[] clr = new double[m];
        double geometric_mean;
        geometric_mean = 0;
        for(int i=0; i<m; i++){
            geometric_mean += Math.log(x[i]);
        }
        geometric_mean = Math.exp(geometric_mean/m);
        for(int i=0; i<m; i++){
                clr[i] = Math.log(x[i]/geometric_mean);
        }
        return clr;
    }
    public static double[] ilr(double x[], double[][] clr_base){
        int m = x.length;
        double[] ilr = new double[m-1];
        double[] clr = clr(x);
        for(int i=0;i<m-1;i++){
            ilr[i] = 0;
            for(int j=0;j<m;j++){
                ilr[i] += clr[j] * clr_base[i][j];
            }
        }
        return ilr;
    }  
    public static double[][] transformRawILR(double x[][], double clr_base[][]){
        double [][]ilr = new double[x.length][];
        
        for(int i=0;i<x.length;i++){
            ilr[i] = ilr(x[i], clr_base);
        }
        return ilr;
    }
    /**
     *
     * @param x
     * @param partition
     * @return
     */
    public static double[][] transformRawILR(double[][] x, int[][] partition){
        int m = x.length;
        int n = x[0].length;
        double[][] ilr = new double[m-1][n];
        double[][] psi = new double[m-1][m];
        double r, s;
        double c;
        // Psi construction
        for(int i=0;i+1<m;i++){
            r = 0;
            s = 0;
            for(int j=0;j<m;j++){
                r += (partition[i][j] == 1 ? 1.0: 0);
                s += (partition[i][j] == -1 ? 1.0: 0);
            }
            c = Math.sqrt(r*s/(r+s));
            for(int j=0;j<m;j++)
                psi[i][j] = (partition[i][j] == 1? c/r :
                    (partition[i][j] == -1 ? -c/s : 0));
        }
        Matrix Psi = new Matrix(psi);
        Matrix X = new Matrix(transformRawCLR(x));
        ilr = Psi.times(X).getArray();
        return ilr;
    }
    /**
     *
     * @param x
     * @param partition
     * @return
     */
    public static double[][] transformILRRaw(double[][] x, int[][] partition){
        int m = x.length;
        int n = x[0].length;
        double[][] raw;// = new double[m+1][n];
        double[][] psi = new double[m][m+1];
        double r, s;
        double c;
        // Psi construction
        for(int i=0;i<m;i++){
            r = 0;
            s = 0;
            for(int j=0;j<m+1;j++){
                r += (partition[i][j] == 1 ? 1.0: 0);
                s += (partition[i][j] == -1 ? 1.0: 0);
            }
            c = Math.sqrt(r*s/(r+s));
            for(int j=0;j<m+1;j++)
                psi[i][j] = (partition[i][j] == 1? c/r :
                    (partition[i][j] == -1 ? -c/s : 0));
        }
        Matrix Psi = new Matrix(psi);

        Matrix X = new Matrix(x);
        raw = Psi.transpose().times(X).getArray();

        for(int i=0; i<m+1; i++){
            for(int j=0; j<n; j++){
                raw[i][j] = Math.exp(raw[i][j]);
            }
        }

        return raw;
    }
    public static double [][] principalComponents(double data[][]){

        Matrix Z = (new Matrix(CoDaStats.transformRawCLR(data))).transpose();

        SingularValueDecomposition svd =
            new SingularValueDecomposition(Z);

        double[][] ilrBase = new Matrix(svd.getV().getArray()).transpose().getArray();

        Matrix ILRBASE = new Matrix(ilrBase);

        return ILRBASE.getMatrix(0, data.length-2, 0 , data.length-1).getArray();
    }
    public static String[] pValueUniformEmpirical(double t[]){
        double p015[] = {0.576, 0.091, 0.085};
        double p010[] = {0.656, 0.104, 0.096};
        double p005[] = {0.787, 0.126, 0.116};
        double p0025[] = {0.918, 0.148, 0.136};
        double p001[] = {1.092, 0.178, 0.163};

        int N = 3;
        String pValue[] = new String[N];
        for(int i=0;i<N;i++){
            if(t[i] < p015[i]){
                pValue[i] = ">0.15";
            }else if(t[i] < p010[i]){
                pValue[i] = "[0.10, 0.15]";
            }else if(t[i] < p005[i]){
                pValue[i] = "[0.05, 0.10]";
            }else if(t[i] < p0025[i]){
                pValue[i] = "[0.025, 0.05]";
            }else if(t[i] < p001[i]){
                pValue[i] = "[0.01, 0.025]";
            }else{
                pValue[i] = "&lt;0.01";
            }
        }
        return pValue;
    }
    public static String[] pValueChiSquareEmmpirical(double t[]){
        double p015[] = {1.610, 0.284, 0.131};
        double p010[] = {1.933, 0.347, 0.152};
        double p005[] = {2.492, 0.461, 0.187};
        double p0025[] = {3.070, 0.581, 0.221};
        double p001[] = {3.857, 0.743, 0.267};

        int N = 3;
        String pValue[] = new String[N];
        for(int i=0;i<N;i++){
            if(t[i] < p015[i]){
                pValue[i] = ">0.15";
            }else if(t[i] < p010[i]){
                pValue[i] = "[0.15,  0.10]";
            }else if(t[i] < p005[i]){
                pValue[i] = "[0.10, 0.05]";
            }else if(t[i] < p0025[i]){
                pValue[i] = "[0.05, 0.025]";
            }else if(t[i] < p001[i]){
                pValue[i] = "[0.025, 0.01]";
            }else{
                pValue[i] = "&lt;0.01";
            }
        }
        return pValue;

    }
    public static double[] marginalUnivariateTest(double y[]){
        int n = y.length;
        double N = (double)n;
        
        NormalDistributionImpl normal  = new NormalDistributionImpl();
        double z[] = new double[n];
        double y_mean = BasicStats.mean(y);
        double y_var  = BasicStats.variance(y) * n / (n-1);
        double y_sd   = Math.sqrt(y_var);
        for(int r=0;r<n;r++){
            try {
                z[r] = normal.cumulativeProbability((y[r] - y_mean) / y_sd);
            } catch (MathException ex) {
                Logger.getLogger(CoDaStats.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Arrays.sort(z);
        double q[] = new double[3];
        // Anderson-Darling
        double qA = 0;
        for(int r=0;r<n;r++)
            qA += (2*r+1) * ( Math.log(z[r]) + Math.log(1 - z[n-r-1]) );
        qA = - qA / n - n;
        q[0] = qA * ( 1 + 4/N - 25/(N*N));
        // Cramer-von Mises
        double qC = 0;
        for(int r=0;r<n;r++){
            double temp =  z[r] - (2 * r + 1) / (2*N);
            qC += temp*temp;
        }
        qC += 1/(12*N);
        q[1] = qC * (1+1/(2*N));
        // Watson
        double z_mean = BasicStats.mean(z);
        double qW = qC - N *( z_mean - 0.5)*( z_mean - 0.5);
        q[2] = qW * (1+1/(2*N));
        return q;
    }
    public static double[] bivariateAngleTest(double y1[], double y2[]){
        int n = y1.length;
        double N = (double)n - 1;
        double mean_y1 = BasicStats.mean(y1);
        double mean_y2 = BasicStats.mean(y2);
        double var_y1 = BasicStats.variance(y1);
        double sd_y1 = Math.sqrt(var_y1);
        double var_y2 = BasicStats.variance(y2);
        double sd_y2 = Math.sqrt(var_y2);
        double cov = BasicStats.covariance(y1, y2);
        double delta = Math.sqrt( var_y1 * var_y2 - cov * cov);

        double z[] = new double[n];
        for(int r=0;r<n;r++){
            double u = (y1[r] - mean_y1) * sd_y2 - (y2[r] - mean_y2) * cov / sd_y2;
            u /= delta;

            double v = ( y2[r] - mean_y2) * Math.sqrt(var_y1 - cov*cov / var_y2);
            v /= delta;

            double test = Math.signum(u);
            double rho = Math.atan(v/u) + 0.5 * (1 - Math.signum(u)) * Math.PI +
                    0.5 * ( 1 + Math.signum(u)) * (1 - Math.signum(v)) * Math.PI;

            z[r] = rho / (2 * Math.PI);
        }
        Arrays.sort(z);
        
        double q[] = new double[3];
        double qA = 0;
        for(int r=0;r<n;r++)
            qA += (2*r+1) * ( Math.log(z[r]) + Math.log(1 - z[n-r-1]) );
        qA = - qA / n - n;
        q[0] = qA;
        // Cramer-von Mises
        double qC = 0;
        for(int r=0;r<n;r++){
            double temp =  z[r] - (2 * r + 1) / (2*N);
            qC += temp*temp;
        }
        qC += 1/(12*N);
        q[1] = (qC - 0.4 / N + 0.6 / (N*N)) *  ( 1 + 1/N);
        // Watson
        double z_mean = BasicStats.mean(z);
        double qW = qC - N *( z_mean - 0.5)*( z_mean - 0.5);
        q[2] = (qW - 0.1/N + 0.1/(N*N)) * (1 + 0.8/N);
        return q;
    }
    public static double[] atipicalityIndex(double x[][]){
        int n = x[0].length;
        double N = (double)n;

        double d = x.length;

        double y[][] = new Matrix(x).transpose().getArray();
        double mean[][] = new double[1][];
        mean[0] = BasicStats.mean(x);
        Matrix MEAN = new Matrix(mean);
        double cov[][] = BasicStats.covariance(x);
        for(int i=0;i<d;i++)
            for(int j=0;j<d;j++)
                cov[i][j] = cov[i][j] * N /(N-1);


        Matrix invCOV = new Matrix(cov).inverse();
        
        double z[] = new double[n];
        double yRow[][] = new double[1][];
        BetaDistributionImpl beta =
                    new BetaDistributionImpl(0.5 * d, 0.5 * (N-1-d));
        for(int r=0;r<n;r++){
            yRow[0] = y[r];
            Matrix D = new Matrix(yRow).minus(MEAN);
            Matrix RES = D.times(invCOV).times(D.transpose());
            double q = RES.get(0,0);//1/(1+1/N) * RES.get(0,0);
            try {
                z[r] = beta.cumulativeProbability(N*q/((N-1)*(N-1)));//q / (q + N -1));
            } catch (MathException ex) { }
        }
        return z;
    }
    public static double[] radiusTest(double x[][]){
        int n = x[0].length;
        double N = (double)n;

        double d = x.length;

        double y[][] = new Matrix(x).transpose().getArray();
        double mean[][] = new double[1][];
        mean[0] = BasicStats.mean(x);
        Matrix MEAN = new Matrix(mean);
        double cov[][] = BasicStats.covariance(x);
        for(int i=0;i<d;i++)
            for(int j=0;j<d;j++)
                cov[i][j] = cov[i][j] * N /(N-1);
       

        Matrix invCOV = new Matrix(cov).inverse();

        ChiSquaredDistributionImpl chiSquare = new ChiSquaredDistributionImpl(d);
        double z[] = new double[n];
        double yRow[][] = new double[1][];
        for(int r=0;r<n;r++){
            yRow[0] = y[r];
            Matrix D = new Matrix(yRow).minus(MEAN);
            Matrix RES = D.times(invCOV).times(D.transpose());
            double u = RES.get(0,0);
            try {
                z[r] = chiSquare.cumulativeProbability(u);
            } catch (MathException ex) { }
        }
        Arrays.sort(z);

        double q[] = new double[3];
        double qA = 0;
        for(int r=0;r<n;r++)
            qA += (2*r+1) * ( Math.log(z[r]) + Math.log(1 - z[n-r-1]) );
        qA = - qA / n - n;
        q[0] = qA;
        // Cramer-von Mises
        double qC = 0;
        for(int r=0;r<n;r++){
            double temp =  z[r] - (2 * r + 1) / (2*N);
            qC += temp*temp;
        }
        qC += 1/(12*N);
        q[1] = (qC - 0.4 / N + 0.6 / (N*N)) *  ( 1 + 1/N);
        // Watson
        double z_mean = BasicStats.mean(z);
        double qW = qC - N *( z_mean - 0.5)*( z_mean - 0.5);
        q[2] = (qW - 0.1/N + 0.1/(N*N)) * (1 + 0.8/N);
        return q;
    }
    /**
     *
     * @param x
     * @param basis
     * @return
     */
    public static double[] transformILRRaw(double[] x, double[][] basis){
        int m = x.length;
        double[] raw = new double[m+1];
        for(int i=0;i<m+1;i++){
            raw[i] = 0;
            for(int j=0;j<m;j++){
                raw[i] += x[j] * basis[j][i];
            }
            raw[i] = Math.exp(raw[i]);
        }
        return raw;
    }
/**
 *  @param x first coordinates of a composition
 *  @return a vector2 representing the composition (x,y,z) in a ternary diagram
 * with vertices (0,0), (0,1), (0.5,sqrt(3)2)
 *
 */
    public static double[] ternaryTransform4(double[] x){
        return ternaryTransform(x[0],x[1],x[2],x[3]);
    }
    /**
     *
     * @param x
     * @return
     */
    public static double[] ternaryTransform3(double[] x){
        return ternaryTransform(x[0],x[1],x[2]);
    }
    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static double[] ternaryTransform(double x, double y, double z){
        // Given a compositional vector (x,y,z) the function returns a
        // vector (u,v) representing the vector (x,y,z) in a simplex with
        // vertices (0, 0) (1, 0) (0.5, 0.8660254)
        double []res = new double[2];

        double k = x + y + z;
        //x = x/k; y = y/k; z = z/k;

        //k = x + y + z;
        res[0] = (0.5 * x + z) / k - 0.5;
        res[1] = 0.866025403784439 * x / k - 0.28867513459481288225457439025098;

        //res[0] = res[0] * scale;
        //res[1] = -res[1] * scale + scale;

        return res;
    }
    /**
     *
     * @param x
     * @param y
     * @param z
     * @param t
     * @return
     */
    public static double[] ternaryTransform(double x, double y, double z, double t){
        double []res = new double[3];

        double k = x + y + z + t;
        res[0] = (0.288675134594813 * y + 0.866025403784439 * z ) / k - 0.2886751347;
        res[1] = (x + 0.5 * y + 0.5 * z) / k - 0.5000000000;
        res[2] = 0.816496580927726 * y / k - 0.2041241452;

        return res;
    }
    /**
     *
     * @param data
     * @param dlevel
     * @param perc
     * @return
     */
    public static double[][] roundedZeroReplacement(double[][] data, double[][] dlevel, double perc){
        int n = data[0].length;
        int k = data.length;
        double[][] zdata = new double[k][n];        
        for(int j=0;j<n;j++){
            boolean valid = true;
            double tdata = 0;
            double tdlevel = 0;
            for(int i=0;i<k;i++){
                if(Double.isNaN(data[i][j]))
                    valid = false;
                else{
                    tdata += data[i][j];
                    tdlevel += perc * dlevel[i][j];
                }
            }
            for(int i=0;i<k;i++){
                if( !valid ) zdata[i][j] = data[i][j];
                else{
                    if(dlevel[i][j] != 0){
                        zdata[i][j] = perc * dlevel[i][j];
                    }else{
                        zdata[i][j] = data[i][j] * (tdata - tdlevel) / tdata;
                    }
                }
            }
        }
        return zdata;
    }
}
