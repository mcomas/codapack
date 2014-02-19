/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import coda.CoDaStats;
import org.apache.commons.math.random.GaussianRandomGenerator;
import org.apache.commons.math.random.JDKRandomGenerator;

/**
 *
 * @author mcomas
 */
public class testSimulateAndersonDarling {
    public static void main(String args[]){
        GaussianRandomGenerator normal = new GaussianRandomGenerator(new JDKRandomGenerator());
        double total = 0;
        double q = 2;
        int N = 10;
        double n = (double)N;
        int SIM = 1000;
        double z[] = new double[N];

        for(int i=0;i<SIM;i++){
            for(int r=0;r<N;r++) z[r] = normal.nextNormalizedDouble();
            double []res = CoDaStats.marginalUnivariateTest(z);
            

            total += res[0] < q ? 1 : 0;
        }
        double mean = total / (double)SIM;
        System.out.println(mean);
    }
}
