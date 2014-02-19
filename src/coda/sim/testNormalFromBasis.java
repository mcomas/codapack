/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.sim;

import coda.Composition;
import coda.sim.TernaryPlotDisplay.TernaryPlotBuilder;
import java.util.Random;

/**
 *
 * @author mcomas
 */
public class testNormalFromBasis {
    final static double FACTOR = 0.1;
    final static int SIZE = 20;
    final static int COMPONENTS = 3;
    public static void main(String args[]) {
        CoDaRandom random = new CoDaRandom();
        Random generator = new Random();

        
        Composition[] data = new Composition[SIZE];
        
        double[] mean = {0,0,0};
        //double[] sigma = {0.1339746,1.8660254,1};
        double[] sigma = {0.1,0.1,0.1};
        double link = 1;
        int windows = 1;
        for(int w=0;w<windows;w++){

            for(int i=0; i< SIZE;i++){
                double lnk = link * generator.nextGaussian();
                double vector[] = new double[COMPONENTS];
                vector[0] = Math.exp(sigma[0] * generator.nextGaussian() );
                vector[1] = Math.exp(sigma[1] * generator.nextGaussian() + lnk);
                vector[2] = Math.exp(sigma[2] * generator.nextGaussian() - lnk);
               
                data[i] = new Composition(vector);
            }
            String[] names = new String[COMPONENTS];
            for(int i=0; i<COMPONENTS;i++)
                names[i] = "C_" + (i+1);


            TernaryPlotWindow frame = new TernaryPlotWindow(
                        null, new TernaryPlotBuilder(names, data).build(), "TEST");

            frame.setSize(600,600);
            frame.setVisible(true);
        }
    }
}
