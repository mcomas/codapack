/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.sim;

import coda.DataFrame;
import coda.ext.jama.LUDecomposition;
import coda.ext.jama.Matrix;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.menu.Biplot3dMenu;
import coda.gui.utils.DataSelector;
import coda.plot.TernaryPlot3dDisplay.TernaryPlot3dBuilder;
import coda.plot.window.TernaryPlot3dWindow;
import java.util.Arrays;
import java.util.Random;
import javax.swing.DefaultListModel;

/**
 *
 * @author mcomas
 */
public class testNormalIndependentGrowing {

    public static void main(String args[]) {
        Random generator = new Random();
        CoDaRandom random = new CoDaRandom();
        // Data initialization
        int SIZE = 1000;
        boolean withGraphics = false  ;
        int n  = 3;
        double a[][] = new double[1][n];
        Arrays.fill(a[0], 1);
        /*

        double a[][] = {
            {1,  1,  1,  1,  1,  1,  1},
            {2,  1, -1, -1, -1,  0,  0 },
            {0,  0,  0,  1,  1, -1, -1}};
            */
        //int n  = a[0].length;
        //double b[] = {0, 2, 3};
        // Associated to composition (6,3,3,2)
        double compo_expected[] = new double[n];
        Arrays.fill(compo_expected, 1);
        
        double sigma[] = {0.1,0.1,0.1};
        

/*****************************************************************************/
        // Expected value on R^n
        double expected[] = new double[n];
        double gmean = 1;
        for(int i=0;i<n;i++) gmean *= compo_expected[i];
        gmean = Math.pow(gmean, 1f/(double)n);
        for(int i=0;i<n;i++) expected[i] = Math.log(compo_expected[i] / gmean);
        // Calculate constants according to expected value
        double b[] = new double[a.length];
        for(int i=0;i<a.length;i++){
            b[i] = 0;
            for(int j=0;j<n;j++) b[i] += expected[j] * a[i][j];
        }
        // Calculating rang of A
        Matrix A = new Matrix(a);
        int r = A.rank();
        // Calculating rang of extended matrix Ab
        double ab[][] = new double[b.length][n+1];
        for(int i=0; i<b.length;i++){
            System.arraycopy(a[i], 0, ab[i], 0, a[0].length);
            ab[i][a[0].length] = b[i];
        }
        int r_ab = new Matrix(ab).rank();
        
        if(r!=r_ab){
            System.out.println("System not well defined");
            return;
        }
        double base[][] = getKernelBase(A,n);
        double manifold_base[][] = null;
        
        if(base.length == 0) manifold_base = new double[0][0];
        else manifold_base = new Matrix(base).transpose().qr().getQ().transpose().getArray();
        
        int dv = manifold_base.length;
        String vector = null;
        for(int i=0;i<dv;i++){
            vector = "Initial Manifold Base;";
            for(int j=0;j<n;j++){
                vector += CoDaPackConf.getDecimalExportFormat().format(base[i][j]) + (j + 1 == n ? "": ";");
            }
            System.out.println(vector);
        }   
        /*
         * At this point,
         * manifol_base contains an orthnormal base from the linear manifold
         */

        /*********************************************
         *********  GENERATE and PLOT DATA  **********
         *********************************************/
        double sigma_manifold[] = new double[dv];

        Arrays.fill(sigma_manifold, 1);
        
        double[][] data = new double[n][SIZE];
     
        String[] names = new String[n];
        
        double lambda[] = new double[dv];

        for(int i=0; i<n;i++)
            names[i] = "c" + (i+1);
        for(int w=0;w<1;w++){
            for(int i=0; i< SIZE;i++){
                for(int c=0;c<dv;c++) lambda[c] = Math.sqrt(sigma_manifold[c]) * generator.nextGaussian();
                for(int k=0;k<n;k++){
                    double vv = expected[k];
                    for(int c=0;c<dv;c++) vv += lambda[c] * manifold_base[c][k];
                    data[k][i] = Math.exp(vv);
                }
            }
        }
        DataFrame df = new DataFrame();
        df.addData(names, data);
        CoDaPackMain main = new CoDaPackMain();
        main.addDataFrame(df);
        main.setVisible(true);

        /*
         * Plot biplot
         */
        
        if(withGraphics){
            Biplot3dMenu biplot = new Biplot3dMenu(main);
            DataSelector dataSel = biplot.getDataSelector();
            dataSel.setSelectedData((DefaultListModel)dataSel.getAvailableData());
            biplot.acceptButtonActionPerformed();
   
        /*
         * Plot biplot
         */

            TernaryPlot3dWindow frame2 = new TernaryPlot3dWindow(
                        df, new TernaryPlot3dBuilder(names, data).build(), "TEST");
            frame2.setSize(600,600);
            frame2.setVisible(true);
        }
 /*
        double[][] cov = new double[n][n];
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++){
                cov[i][j] = 0;
                for(int k =0;k<dv;k++) cov[i][j] += sigma_manifold[k] * manifold_base[k][i] * manifold_base[k][j];
            }

        
        double[][] ndata = new double[n][SIZE];
        for(int i=0; i< SIZE;i++){
            double vvv[] = random.nextGaussianCLR(expected, cov).array();
            for(int j=0;j<n;j++)
                ndata[j][i] = vvv[j];
        }
        String[] nnames = new String[n];
        for(int i=0; i<n;i++)
            nnames[i] = "C_" + (i+1);
        df.addData(nnames, ndata);
        main.updateDataFrame(df);
  * */
        /*
        TernaryPlot3dWindow frame3 = new TernaryPlot3dWindow(
                new TernaryPlot3dBuilder(nnames, ndata).build(), "TEST");
        frame3.setSize(600,600);
        frame3.setVisible(true);*/
        
    }
    public static double[][] getKernelBase(Matrix A, int n){
        int r = A.rank();
        int d = n-r;
        double base[][] = new double[d][n];
        LUDecomposition ludecomp = new LUDecomposition(A.transpose());
        double l[][] = A.transpose().lu().getL().transpose().getArray();

        // Matrix diagonalization
        for(int i=r-1;i>=0;i--){
            for(int k=0;k<i;k++){
                double piv = l[k][i];
                for(int j=0;j<n;j++){
                    l[k][j] = l[k][j] - piv * l[i][j];
                }
            }
        }

        // Extracting a base from diagonalized matrix
        for(int k=0;k<d;k++){
            for(int i=0;i<r;i++){
                base[k][i] = l[i][r+k];
            }
            for(int i=0;i<d;i++){
                base[k][r+i] = (i==k ? -1 : 0);
            }
        }
        int []pivot = ludecomp.getPivot();
        double base_result[][] = new double[d][n];
        for(int i=0;i<d;i++){
            for(int j=0;j<n;j++){
                base_result[i][j] = base[i][pivot[j]];
            }
        }
        return base_result;
    }
}
