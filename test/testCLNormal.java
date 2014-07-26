/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.sim;

import coda.Composition;
import coda.sim.TernaryPlotDisplay.TernaryPlotBuilder;

/**
 *
 * @author mcomas
 */
public class testCLNormal {
    final static double FACTOR = 0.1;
    final static int SIZE = 1000;
    final static int COMPONENTS = 3;
    public static void main(String args[]) {
        CoDaRandom random = new CoDaRandom();

        Composition[] data = new Composition[SIZE];
  /*
        double[] mean = {0,0};
        double[][] var = {{1,0},{0,1}};
        var[0][0] *= FACTOR;
        var[1][1] *= FACTOR;
*/
        
        double[] mean = {0,0,0};
        double[][] var = {{1,0,0},{0,1,0},{0,0,1}};
        var[0][0] *= FACTOR;
        var[1][1] *= FACTOR;
        var[2][2] *= FACTOR;
        int windows = 6;
        for(int w=0;w<windows;w++){

            for(int i=0; i< SIZE;i++)
                data[i] = random.nextGaussianCLRCoDa(mean, var);

            String[] names = new String[COMPONENTS];
            for(int i=0; i<COMPONENTS;i++)
                names[i] = "C_" + (i+1);


            TernaryPlotWindow frame = new TernaryPlotWindow(
                        null, new TernaryPlotBuilder(names, data).build(), "TEST");

            frame.setSize(600,600);
            frame.setVisible(true);
        }
        /*
        for(int i=0; i< SIZE;i++)
            System.out.println(data[i]);
*/
        System.out.println("Final");
         
    }
}
