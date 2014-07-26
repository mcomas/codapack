/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.sim;

import coda.BasicStats;
import coda.CoDaStats;
import java.util.Arrays;

/**
 *
 * @author mcomas
 */
public class ALNGeneration {
    static int ALR = 1;
    static int CLR = 2;
    static double[][] generateALNSample(int size, double mean[], double cov[][], int method){
        int n = size;
        int m = mean.length;
        if(method == CLR) m--;

        double sample[][] = new double[m][size];
        if(method == ALR){
            for(int i=0;i<size;i++){
                double vec[] = CoDaRandom.nextGaussianALR(mean, cov);
                for(int j=0;j<m;j++)
                    sample[j][i] = vec[j];
            }
        }else if(method == CLR){
            for(int i=0;i<size;i++){
                double vec[] = CoDaRandom.nextGaussianCLR(mean, cov);
                for(int j=0;j<m;j++)
                    sample[j][i] = vec[j];
            }
        }
        return sample;
    }
    /*
     * Generate an ALN Sample from ILR statistics
     */
    static double[][] generateALNSample(int size, double mean[], double cov[][], double base[][]){
        int n = size;
        int m = mean.length-1;

        double sample[][] = new double[m][size];
        for(int i=0;i<size;i++){
            double vec[] = CoDaRandom.nextGaussianILR(mean, cov, base);
            for(int j=0;j<m;j++)
                sample[j][i] = vec[j];
        }
        return sample;
    }
    /*
     * Generate an ALN Sample from available data
     */
    static double[][] generateALNSample(int size, double data[][]){
        int n = size;
        int m = data.length;

        int partition[][] = CoDaStats.defaultPartition(m);
        double dataILR[][] = CoDaStats.transformRawILR(data, partition);

        double mean[] = BasicStats.mean(data);
        double cov[][] = BasicStats.covariance(data);
        
        double sampleILR[][] = new double[m][size];
        for(int i=0;i<size;i++){
            double vec[] = CoDaRandom.normalRandomVariable(mean, cov);
            for(int j=0;j<m;j++)
                sampleILR[j][i] = vec[j];
        }
        return CoDaStats.transformILRRaw(sampleILR, partition);
    }
    static double[][] generateALNSample(int size, double center[], double logcontrast[][], double epsilon){

        int n = center.length;
        int m = logcontrast.length;

        double a[][] = new double[m][n];
        for(short i=0;i<m;i++)
            System.arraycopy(logcontrast[i], 0, a[i], 0, n);

        double clrPlane[][] = new double[1][n];
        Arrays.fill(clrPlane[0], 1);


        return null;
    }
}
