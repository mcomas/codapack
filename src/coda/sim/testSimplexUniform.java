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
public class testSimplexUniform {
    final static double L[] = {0.1,0.2,0.01};
    final static double U[] = {0.3,0.4,0.6};
    final static int SIZE = 1000;
    final static int COMPONENTS = 3;
    public static void main(String args[]) {
        CoDaRandom random = new CoDaRandom();
        //L[2] = + U[0] - U[1];
        //U[2] = + L[0] - L[1];
        Composition[] data = new Composition[SIZE];
        int windows = 1;

        for(int i=0;i<COMPONENTS;i++){
            L[i] = Math.log(L[i]);
            U[i] = Math.log(U[i]);
        }
        double a[] = {1,3,1};
        Composition A = new Composition(a);
        for(int w=0;w<windows;w++){
            for(int i=0; i< SIZE;i++)
                data[i] = random.nextSimplex3Uniform().power(2).perturbate(A);

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

