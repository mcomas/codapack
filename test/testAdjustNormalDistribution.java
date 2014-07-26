/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.sim;


import coda.Composition;
import coda.DataFrame;
import coda.plot.window.TernaryPlot2dWindow;
import coda.io.ImportData;
import coda.plot.TernaryPlot2dDisplay.TernaryPlot2dBuilder;
import coda.sim.TernaryPlotDisplay.TernaryPlotBuilder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author mcomas
 */

    public class testAdjustNormalDistribution {
        public static void printMatrix(double [][]v){
        String matrix = "matrix([";
        matrix += "[";
        matrix += v[0][0];
        for(int j=1;j<v[0].length;j++){
            matrix += "," + v[0][j];
        }
        matrix += "]";
        for(int i=1;i<v.length;i++){
            matrix += ",[";
            matrix += v[i][0];
            for(int j=1;j<v[0].length;j++){
                matrix += "," + v[i][j];
            }
            matrix += "]";
        }
        matrix += "]);";
        System.out.println(matrix);
    }
    final static int SIZE = 250;
    final static int COMPONENTS = 3;
    static CoDaRandom random = new CoDaRandom();
    public static void main(String args[]) {
        try {

            //DataFrame df = ImportData.importXLS("/Users/marc/CoDa.xls", true);
            DataFrame df = ImportData.importXLS("G:/Recerca/EIO_RECERCA/New CoDaPack/Halimba.xls", true);
            String[] names = new String[3];
            names[0] = df.getName(0);
            names[1] = df.getName(1);
            names[2] = df.getName(2);
           
            double[][] data = new double[3][];
            data[0] = df.getNumericalData(names[0]);
            data[1] = df.getNumericalData(names[1]);
            data[2] = df.getNumericalData(names[2]);

            //Composition[] data = new Composition[SIZE];
      /*
            double[] mean = {0,0};
            double[][] var = {{1,0},{0,1}};
            var[0][0] *= FACTOR;
            var[1][1] *= FACTOR;
    */
/*
            double basis[][] = new double[2][3];
            basis[0][0] = Math.sqrt(1.0/6.0);
            basis[0][1] = Math.sqrt(1.0/6.0);
            basis[0][2] = -Math.sqrt(2.0/3.0);
            basis[1][0] = Math.sqrt(1.0/2.0);
            basis[1][1] = -Math.sqrt(1.0/2.0);
            basis[1][2] = 0;
*/
            int size = data[0].length;
/*
            double data[][] = new double[3][size];

            Composition[] compo = new Composition[size];
            double[] m = {0,0};
            double[][] v = {{1,-0.8},{-0.8,1}};
            printMatrix(v);
            for(int i=0; i< size;i++)
                compo[i] = random.nextGaussianALR(m, v);
            for(int i=0;i<size;i++){
                for(int j=0;j<3;j++){
                    data[j][i] = compo[i].get(j);
                }
            }
            */
            TernaryPlot2dWindow frame = new TernaryPlot2dWindow(
                    df, new TernaryPlot2dBuilder(names, data).build(), "TEST");
            frame.setSize(600,600);
            frame.setVisible(true);

            double cdata[][] = new double[3][size];

            // Calculate mean and covariance
            for(int i=0;i<size;i++){
                for(int j=0;j<3;j++){
                    cdata[j][i] = Math.log(data[j][i]);
                }
            }
            double mean[] = {0,0,0};
            double cov[][] = new double[3][3];
            
            for(int j=0;j<3;j++){
                for(int k=0;k<size;k++){
                    mean[j] += cdata[j][k];
                }
                mean[j] /= size;
            }
            for(int j=0;j<3;j++){
                for(int i=j;i<3;i++){
                    cov[i][j] = 0;
                    for(int k=0;k<size;k++){
                        cov[i][j] += (cdata[i][k] - mean[i]) * (cdata[j][k] - mean[j]);
                    }
                    cov[i][j] /= size;
                    cov[j][i] = cov[i][j];
                }
            }
            printMatrix(cov);
            Composition[] new_data = new Composition[SIZE];
            int windows = 1;
            for(int w=0;w<windows;w++){
                for(int i=0; i< SIZE;i++)
                    new_data[i] = random.nextGaussianBasis(mean, cov);
                
                TernaryPlotWindow frame2 = new TernaryPlotWindow(
                            df, new TernaryPlotBuilder(names, new_data).build(), "TEST");

                frame2.setSize(600,600);
                frame2.setVisible(true);
            }



            //TernaryPlot2dWindow frame = new TernaryPlot2dWindow(new TernaryPlot2dDisplay(names, data), "TEST");
            //TernaryPlot2dWindow frame = new TernaryPlot2dWindow(
                    //new TernaryPlot2dBuilder(names, data).build(), "TEST");
            //TernaryPlotDisplay ternaryPlotDisplay = new TernaryPlotDisplay(frame.getContentPane(), names,data);
            //frame.getContentPane().add(ternaryPlotDisplay);
            //frame.setSize(600,600);
            //frame.setVisible(true);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(testAdjustNormalDistribution.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(testAdjustNormalDistribution.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(testAdjustNormalDistribution.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}