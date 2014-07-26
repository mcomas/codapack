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
public class testDirichlet {
    final static double FACTOR = 0.004;
    final static int SIZE = 1000;
    final static int COMPONENTS = 3;
    public static void main(String args[]) {
        CoDaRandom random = new CoDaRandom();

        Composition[] data = new Composition[SIZE];

        double [] alpha = {4.5,1.5,0.5};
        for(int i=0; i< SIZE;i++)
            data[i] = random.nextDirichlet(alpha);
            //data[i] = random.nextUniform(COMPONENTS);

        String[] names = new String[COMPONENTS];
        for(int i=0; i<COMPONENTS;i++)
            names[i] = "C_" + (i+1);


        TernaryPlotWindow frame = new TernaryPlotWindow(
                    null, new TernaryPlotBuilder(names, data).build(), "TEST");

        frame.setSize(600,600);
        frame.setVisible(true);

        /*
        for(int i=0; i< SIZE;i++)
            System.out.println(data[i]);
*/
        System.out.println("Final");

    }
}
