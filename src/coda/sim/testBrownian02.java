/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.sim;

import coda.CoDaStats;
import coda.Composition;
import coda.sim.TernaryPlotDisplay.TernaryPlotBuilder;
import java.util.Random;

/**
 *
 * @author mcomas
 */
public class testBrownian02 {
    final static double FACTOR = 0.1;
    final static int SIZE = 10000;
    final static int STEPS = 10000;
    final static int COMPONENTS = 3;

    static double start[] = {1,1,1};
    public static void main(String args[]) {
        CoDaRandom random = new CoDaRandom();
        Random generator = new Random();

        Composition data[] = new Composition[SIZE];
        double ilr[] = new double[2];
        double dx = 0.01;
        double dy = 0.01;
        int windows = 6;
        for(int w=0;w<windows;w++){
        //for(int i=0; i< SIZE;i++){
            ilr[0] = 0; ilr[1] = 0;
            for(int j=0;j<SIZE;j++){
                switch( generator.nextInt(4) ){
                    case 0:
                        ilr[0] += dx;
                        break;
                    case 1:
                        ilr[0] -= dx;
                        break;
                    case 2:
                        ilr[1] += dy;
                        break;
                    case 3:
                        ilr[1] -= dy;
                }
                data[j] = new Composition(CoDaStats.transformALRRaw(ilr));
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
