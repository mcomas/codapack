/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.sim;

import java.util.Random;
import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.menu.Biplot3dMenu;
import coda.gui.utils.DataSelector;
import javax.swing.DefaultListModel;
/**
 *
 * @author mcomas
 */
public class testNormalSimulation {
    final static double FACTOR = 0.1;
    final static int SIZE = 200;
    final static int COMPONENTS = 5;
    public static void main(String args[]) {
        CoDaRandom random = new CoDaRandom();
        Random generator = new Random();


        double[][] data = new double[COMPONENTS][SIZE];

        double[] mean = {0,0,0,0,0};
        //double[] sigma = {0.1339746,1.8660254,1};
        //double[] sigma = {0.1,0.1,0.1};
        double perturbation[] = {0.29,0.3,0.01,0.2,0.2};
        double s[] = new double[5];
        s[0] = 0.1;
        s[1] = 0.1;
        s[2] = 0.1;
        s[3] = 0.1;
        s[4] = 0.1;
        
        double cor[][] = new double[5][5];
        cor[0][0] = cor[1][1] = cor[2][2] = cor[3][3] = cor[4][4] = 1;

        cor[0][1] = cor[1][0] = 0.90;
        cor[0][2] = cor[2][0] = 0.90;
        cor[0][3] = cor[3][0] = 0;
        cor[0][4] = cor[4][0] = 0;

        cor[1][2] = cor[2][1] = 0.90;
        cor[1][3] = cor[3][1] = 0;
        cor[1][4] = cor[4][1] = 0;

        cor[2][3] = cor[3][2] = 0;
        cor[2][4] = cor[4][2] = 0;

        cor[3][4] = cor[4][3] = 0;

        double cov[][] = new double[5][5];
        for(int i=0;i<COMPONENTS;i++)
            for(int j=0;j<COMPONENTS;j++) cov[i][j] = s[i]*s[j]*cor[i][j];
        
        for(int i=0; i< SIZE;i++){
            double z [] = random.normalRandomVariable(mean, cov);

            data[0][i] = Math.exp( z[0] );
            data[1][i] = Math.exp( z[1] );
            data[2][i] = Math.exp( z[2] );
            data[3][i] = Math.exp( z[3] );
            data[4][i] = Math.exp( z[4] );
        }
        /*
        for(int i=0;i<SIZE;i++){
            String out = data[0][i] + ";";
            out += data[1][i] + ";";
            out += data[2][i] + ";";
            out += data[3][i] + ";";
            out += data[4][i];
            System.out.println(out);
        }*/
        String[] names = new String[COMPONENTS];
        for(int i=0; i<COMPONENTS;i++)
            names[i] = "c" + (i+1);

        DataFrame df = new DataFrame();
        df.addData(names, data);
        
        CoDaPackMain main = new CoDaPackMain();
        main.addDataFrame(df);
        main.setVisible(true);
        Biplot3dMenu biplot = new Biplot3dMenu(main);

        DataSelector dataSel = biplot.getDataSelector();
        dataSel.setSelectedData((DefaultListModel)dataSel.getAvailableData());

        biplot.acceptButtonActionPerformed();
        /*
        for(int i=0; i< SIZE;i++){
            System.out.println(data[i]);
        }
         * */

  
    }
}
