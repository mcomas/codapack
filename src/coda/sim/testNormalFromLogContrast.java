/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.sim;

import coda.Composition;
import coda.DataFrame;
import coda.ext.jama.LUDecomposition;
import coda.ext.jama.Matrix;
import coda.ext.jama.QRDecomposition;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.menu.Biplot3dMenu;
import coda.gui.utils.DataSelector;
import coda.plot.Biplot3dDisplay.Biplot3dBuilder;
import coda.plot.TernaryPlot2dDisplay.TernaryPlot2dBuilder;
import coda.plot.TernaryPlot3dDisplay.TernaryPlot3dBuilder;
import coda.plot.window.TernaryPlot3dWindow;
import java.util.Arrays;
import java.util.Random;
import javax.swing.DefaultListModel;

/**
 *
 * @author mcomas
 */
public class testNormalFromLogContrast {

    public static void main(String args[]) {
        Random generator = new Random();
        CoDaRandom random = new CoDaRandom();
        // Data initialization
        int n  = 3;
        double a[][] = {
            {1, 1, 1}};
        /*double a[][] = {
            {1, 1, 1, 1},
            //{1, 1, -1, -1},
            {0, 0, 1, -1}};
*/
        //double b[] = {0, 2, 3};
        // Associated to composition (6,3,3,2)
        double compo_expected[] = {0.1,0.1,0.1};
        int SIZE = 100;
        double factor = 0.5 / 0.666;
        double sigma[] = {0.1,0.1,0.1};
        double epsilon = 0.002;

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

        double manifold_base[][] = new Matrix(base).transpose().qr().getQ().transpose().getArray();
        int dv = manifold_base.length;
        String vector = null;
        for(int i=0;i<dv;i++){
            vector = "Initial Manifold Base;";
            for(int j=0;j<n;j++){
                vector += CoDaPackConf.getDecimalExportFormat().format(base[i][j]) + (j + 1 == n ? "": ";");
            }
            System.out.println(vector);
        }
        /**
         * Error calculation
         */
        // Constructing a system whos kernel is the error space
        double f[][] = new double[1+dv][n];
        for(int i=0;i<dv;i++){
            System.arraycopy(manifold_base[i], 0, f[i], 0, n);
        }
        for(int j=0;j<n;j++)f[dv][j] = 1;
        Matrix F = new Matrix(f);

        base = getKernelBase(F,n);
        int de = base.length;
        // Extending the previous base to an orthonormal base
        double orthoBase[][] = new double[n-1][n];
        System.arraycopy(manifold_base, 0, orthoBase, 0, dv);
        System.arraycopy(base, 0, orthoBase, dv, de);
        orthoBase = new Matrix(orthoBase).transpose().qr().getQ().transpose().getArray();

        double error_base[][] = new double[de][n];
        for(int i=0;i<de;i++)
            error_base[i] = orthoBase[dv+i];

        vector = null;
        for(int i=0;i<a.length;i++){
            vector = "Logcontrast system;";
            for(int j=0;j<n;j++){
                vector += CoDaPackConf.getDecimalExportFormat().format(a[i][j]) + (j + 1 == n ? "": ";");
            }
            System.out.println(vector);
        }
        vector = "Logcontrast constant;";
        for(int i=0;i<b.length;i++){
            vector += CoDaPackConf.getDecimalExportFormat().format(b[i]) + (i + 1 == n ? "": ";");
        }
        System.out.println(vector);
        for(int i=0;i<dv;i++){
            vector = "Manifold Base;";
            for(int j=0;j<n;j++){
                vector += CoDaPackConf.getDecimalExportFormat().format(manifold_base[i][j]) + (j + 1 == n ? "": ";");
            }
            System.out.println(vector);
        }
        for(int i=0;i<de;i++){
            vector = "Error Base;";
            for(int j=0;j<n;j++){
                vector += CoDaPackConf.getDecimalExportFormat().format(error_base[i][j]) + (j + 1 == n ? "": ";");
            }
            System.out.println(vector);
        }
        /*
         * At this point,
         * manifol_base contains an orthnormal base from the linear manifold, and
         * error_base contains an orthnormal base from the error subspace
         */
        double u[][] = new double[n][dv+de];
        double v[][] = new double[n][1];
        for(int i=0;i<n;i++){
            v[i][0] = sigma[i]*sigma[i];
            for(int j=0;j<dv;j++){
                u[i][j] = manifold_base[j][i]*manifold_base[j][i];
            }
            for(int j=0;j<de;j++){
                u[i][dv+j] = error_base[j][i]*error_base[j][i];
            }
        }
        /*
        v[n][0] = epsilon*epsilon;
        for(int j=0;j<dv;j++){
            u[n][j] = 0;
        }
        for(int j=0;j<de;j++){
            u[n][dv+j] = 1;
        }*/
        Matrix U = new Matrix(u);
        int ru = U.rank();

        Matrix V = new Matrix(v);
        Matrix S = U.solve(V);
        /*
         * comprovation
         */
        Matrix C = U.times(S).minus(V);
        double s[][] = S.getArray();
        double ss[] = new double[s.length];
        for(int i=0;i<s.length;i++) ss[i] = s[i][0];
        /*********************************************
         *********  GENERATE and PLOT DATA  **********
         *********************************************/
         double sigma_manifold[] = new double[dv];
        Arrays.fill(sigma_manifold, 1);
        
        double[][] data = new double[n][SIZE];
     
        String[] names = new String[n];
        
        double lambda[] = new double[dv];
        double err[] = new double[de];

        for(int i=0; i<n;i++)
            names[i] = "c" + (i+1);
        for(int w=0;w<1;w++){
            for(int i=0; i< SIZE;i++){
                for(int c=0;c<dv;c++) lambda[c] = Math.sqrt(ss[c]) * generator.nextGaussian();
                for(int c=0;c<de;c++) err[c] = Math.sqrt(ss[dv+c]) * generator.nextGaussian();
                for(int k=0;k<n;k++){
                    double vv = expected[k];
                    for(int c=0;c<dv;c++) vv += lambda[c] * manifold_base[c][k];
                    for(int c=0;c<de;c++) vv += err[c] * error_base[c][k];
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

   
        double[][] cov = new double[n][n];
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++){
                cov[i][j] = 0;
                for(int k =0;k<dv;k++) cov[i][j] += ss[k] * manifold_base[k][i] * manifold_base[k][j];
                for(int k =0;k<de;k++) cov[i][j] += epsilon * epsilon * error_base[k][i] * error_base[k][j];
            }

        
        double[][] ndata = new double[n][SIZE];
        for(int i=0; i< SIZE;i++){
            double vvv[] = random.nextGaussianCLR(expected, cov);
            for(int j=0;j<n;j++)
                ndata[j][i] = vvv[j];
        }
        String[] nnames = new String[n];
        for(int i=0; i<n;i++)
            nnames[i] = "C_" + (i+1);
        df.addData(nnames, ndata);
        main.updateDataFrame(df);
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
