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
public class testBrownian01 {
    final static double FACTOR = 0.1;
    final static int SIZE = 10000;
    final static int STEPS = 10000;
    final static int COMPONENTS = 3;

    static double start[] = {1,1,1};
    static double x1[] = {2.7182818284590452353602874713527,1,1};
    static double x2[] = {1,2.7182818284590452353602874713527,1};
    static double x3[] = {1,1,2.7182818284590452353602874713527};
    public static void main(String args[]) {
        CoDaRandom random = new CoDaRandom();
        Random generator = new Random();

        Composition[] data = new Composition[SIZE];

        Composition c1 = new Composition(x1);
        Composition c2 = new Composition(x2);
        Composition c3 = new Composition(x3);
        
        c1= c1.power(0.01/c1.norm(), c1);
        c2= c2.power(0.01/c2.norm(), c2);
        c3= c3.power(0.01/c3.norm(), c3);

        double sign = 1;

        int windows = 6;
        for(int w=0;w<windows;w++){
        //for(int i=0; i< SIZE;i++){
            Composition sim = new Composition(start);
            for(int j=0;j<SIZE;j++){
                sign *=-1;
                switch( generator.nextInt(3) ){
                    case 0:
                        sim = sim.perturbate(c1.power(sign), sim);
                        break;
                    case 1:
                        sim = sim.perturbate(c2.power(sign), sim);
                        break;
                    case 2:
                        sim = sim.perturbate(c3.power(sign), sim);
                }
                data[j] = new Composition(sim.array());
            }
            
        //}

            String[] names = new String[COMPONENTS];
            for(int i=0; i<COMPONENTS;i++)
                names[i] = "C_" + (i+1);


            TernaryPlotWindow frame = new TernaryPlotWindow(
                        null, new TernaryPlotBuilder(names, data).build(), "TEST");

            frame.setSize(700,700);
            frame.setVisible(true);
        }
        /*
        for(int i=0; i< SIZE;i++)
            System.out.println(data[i]);
        */
        System.out.println("Final");

    }
}
