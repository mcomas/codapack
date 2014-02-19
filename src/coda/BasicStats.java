/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda;

import java.util.Arrays;
/**
 *
 * @author marc
 */
public class BasicStats {
        /**
     *
     * @param X
     * @return
     */
    public static double mean(double[] X){
        double s = 0;
        for(double x: X)s += x;
        return s / X.length;
    }
    /**
     *
     * @param x
     * @return
     */
    public static double[] mean(double[][] x){
        int m = x.length;
        int n = x[0].length;

        double[] mean = new double[m];

        for(int i=0; i<m;i++){
            mean[i] = 0;
            for(int j=0; j<n; j++){
                mean[i] += x[i][j];
            }
            mean[i] /= n;
        }
        return mean;
    }
    /**
     *
     * @param X
     * @return
     */
    public static double sd(double[] X){
        return Math.sqrt(variance(X));
    }
    /**
     *
     * @param X
     * @return
     */
    public static double[] sd(double[][] X){
        int m = X.length;
        double result[] = new double[m];
        for(int i=0;i<m;i++){
            result[i] = sd(X[i]);
        }
        return result;
    }
    /**
     *
     * @param X
     * @return
     */
    public static double variance(double[] X){
        double s = 0, mean = mean(X);
        for(double x: X) s += (x-mean)*(x-mean);
        return s / (X.length);
    }
    /**
     *
     * @param X
     * @return
     */
    public static double[] variance(double[][] X){
        int m = X.length;
        double result[] = new double[m];
        for(int i=0;i<m;i++){
            result[i] = variance(X[i]);
        }
        return result;
    }
    /**
     *
     * @param X
     * @return
     */
    public static double[][] covariance(double[][] X){
        int m = X.length;
        int n = X[0].length;
        double cov[][] = new double[m][m];
        for(int i=0;i<m;i++){
            cov[i][i] = variance(X[i]);
            for(int j=i+1;j<m;j++){
                cov[j][i] = cov[i][j] = covariance(X[i],X[j]);

            }
        }
        return cov;
    }
    /**
     *
     * @param X
     * @return
     */
    public static double[][] correlation(double[][] X){
        int m = X.length;
        double sd[] = sd(X);
        double cov[][] = covariance(X);
        double cor[][] = new double[m][m];
        for(int i=0;i<m;i++){
            cor[i][i] = 1;
            for(int j=i+1;j<m;j++){
                cor[j][i] = cor[i][j] = cov[i][j] / (sd[i] * sd[j]);
            }
        }
        return cor;
    }
    /**
     *
     * @param X
     * @param Y
     * @return
     */
    public static double covariance(double[] X, double[] Y){
        if(X.length != Y.length) return Double.NaN;
        int n = X.length;
        double s = 0, mean_x = mean(X), mean_y = mean(Y);
        for(int k=0;k<n;k++)
                s += (X[k]-mean_x)*(Y[k]-mean_y);
        return s / (double)(n);
    }
    /**
     *
     * @param data
     * @param p
     * @return
     */
    public static double[] percentile(double[] data, int[] p){
        int n = data.length;
        int k = p.length;
        double[] percentile = new double[k];
        int limit;
        double[] orderedData = new double[n];

        System.arraycopy(data, 0, orderedData, 0, n);
        Arrays.sort(orderedData);
        for(int j=0;j<k;j++){
            limit = (int)(((double)p[j]/100)*n);
            percentile[j] = orderedData[limit==n?limit-1:limit];
        }
        return percentile;
    }
    /**
     *
     * @param data
     * @param p
     * @return
     */
    public static double[][] percentile(double[][] data, int[] p){
        int n = data[0].length;
        int m = data.length;
        int k = p.length;
        double[][] percentile = new double[m][k];
        int limit;
        double[] orderedData = new double[n];
        for(int i=0;i<m;i++){
            System.arraycopy(data[i], 0, orderedData, 0, n);
            Arrays.sort(orderedData);
            for(int j=0;j<k;j++){
                limit = (int)(((double)p[j]/100)*n);
                percentile[i][j] = orderedData[limit==n?limit-1:limit];
            }
        }
        return percentile;
    }
        /**
     *
     * @param sv
     * @return
     */
    public static double[] cumulativeProportionExplained(double sv[]){
        double cpe[] = new double[sv.length];
        double total = 0;
            double parcial = 0;

            for(double v : sv) total += v*v;

            for(int i=0;i<sv.length;i++){
                parcial += sv[i]*sv[i];
                cpe[i] = parcial/total;
            }
        return cpe;
    }
}
