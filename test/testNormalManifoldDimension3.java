/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.sim;

import coda.DataFrame;
import coda.ext.jama.LUDecomposition;
import coda.ext.jama.Matrix;
import coda.ext.jama.QRDecomposition;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.menu.Biplot3dMenu;
import coda.gui.utils.DataSelector;
import coda.plot.TernaryPlot3dDisplay.TernaryPlot3dBuilder;
import coda.plot.window.TernaryPlot3dWindow;
import java.util.Arrays;
import java.util.Random;
import javax.swing.DefaultListModel;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.NotPositiveDefiniteMatrixException;
import org.apache.commons.math.random.CorrelatedRandomVectorGenerator;
import org.apache.commons.math.random.GaussianRandomGenerator;
import org.apache.commons.math.random.JDKRandomGenerator;

/**
 *
 * @author mcomas
 */
public class testNormalManifoldDimension3 {

    public static void main(String args[]) throws NotPositiveDefiniteMatrixException {
        Random generator = new Random();
        CoDaRandom random = new CoDaRandom();
        // Data initialization
        int SIZE = 10000;
        boolean withGraphics = false  ;

        double a[][] = {
            {1,  1,  1,  1,  1,  1},
            {1,  1, -2,  0,  0,  0}};
        
        int n  = a[0].length;
        //double b[] = {0, 2, 3};
        // Associated to composition (6,3,3,2)
        double compo_expected[] = new double[n];
      /*compo_expected[0] = 1;
        compo_expected[1] = 2;
        compo_expected[2] = 4;
        compo_expected[3] = 8*/
        Arrays.fill(compo_expected, 1);
        
        double sigma_expected[] = {2,  2, 4,  1,  1,  1};
        

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


        String vector = "";
        System.out.println("Manifold Base");
        for(int j=0;j<n;j++){
            vector = "";
            for(int i=0;i<dv;i++){
                vector += CoDaPackConf.getDecimalExportFormat().format(manifold_base[i][j]) + (j + 1 == n ? ";": ";");
            }
            System.out.println(vector);
        }
        System.out.println("Error Base");
        for(int j=0;j<n;j++){
            vector = "";
            for(int i=0;i<de;i++){
                vector += CoDaPackConf.getDecimalExportFormat().format(error_base[i][j]) + (j + 1 == n ? ";": ";");
            }
            System.out.println(vector);
        }
        /*
         * At this point,
         * manifol_base contains an orthnormal base from the linear manifold, and
         * error_base contains an orthnormal base from the error subspace
         */
        int dd1 = (dv * (dv+1))/2; // <-------- n = dd1 needed

        double u[][] = new double[n][dd1+de];
        double v[][] = new double[n][1];

        for(int l=0,m;l<n;l++){
            m = 0;
            v[l][0] = sigma_expected[l]*sigma_expected[l];
            for(int i=0;i<dv;i++){
                u[l][m++] = manifold_base[i][l]*manifold_base[i][l];
                for(int j=i+1;j<dv;j++){
                    u[l][m++] = 2 * manifold_base[i][l]*manifold_base[j][l];
                }
            }
            for(int i=0;i<de;i++){
                u[l][m++] = error_base[i][l]*error_base[i][l];
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
        QRDecomposition qr = new QRDecomposition(U);
        Matrix V = new Matrix(v);

        Matrix S = qr.solve(V);

        double cov[][] = new double[dv][dv];
        int m = 0;
        for(int i=0;i<dv;i++){
            for(int j=i;j<dv;j++){
                cov[j][i] = cov[i][j] = S.get(m++, 0);
            }
        }
        double verror[] = new double[de];
        for(int i=0;i<de;i++){
            verror[i] = S.get(m++,0);
        }
        /*
         * comprovation
         */
        Matrix C = U.times(S).minus(V);

        /*********************************************
         *********  GENERATE and PLOT DATA  **********
         *********************************************/
        double sigma_manifold[] = new double[dv];

        Arrays.fill(sigma_manifold, 1);
        
        double[][] data = new double[n][SIZE];
     
        String[] names = new String[n];
        
        double lambda[] = new double[dv];
        double err[] = new double[de];
        double data_lambda[][] = new double[dv][SIZE];

        for(int i=0; i<n;i++)
            names[i] = "c" + (i+1);

        CorrelatedRandomVectorGenerator gen = new CorrelatedRandomVectorGenerator(
                new Array2DRowRealMatrix(cov),
                0.001,
                new GaussianRandomGenerator(new JDKRandomGenerator()));

        for(int w=0;w<1;w++){
            for(int i=0; i< SIZE;i++){
                //for(int c=0;c<dv;c++) lambda[c] = Math.sqrt(sigma_manifold[c]) * generator.nextGaussian();
                
                //lambda = random.normalRandomVariable(cov);
                lambda = gen.nextVector();
                for(int k=0;k<dv;k++) data_lambda[k][i] = lambda[k];
                /*vector = "";
                for(int g=0;g<dv;g++){
                    vector += CoDaPackConf.getDecimalExportFormat().format(lambda[g]) + ";";
                }
                System.out.println(vector);
*/
                for(int c=0;c<de;c++)
                    err[c] = Math.sqrt(verror[c]) * generator.nextGaussian();
                for(int k=0;k<n;k++){
                    double vv = expected[k];
                    for(int c=0;c<dv;c++) vv += lambda[c] * manifold_base[c][k];
                    for(int c=0;c<de;c++) vv += err[c] * error_base[c][k];
                    data[k][i] = Math.exp(vv);
                }
            }
        }
        String lambda_names[]= new String[dv];
        for(int i=0;i<dv;i++) lambda_names[i] = "l" + (i+1);
        DataFrame df = new DataFrame();
        df.addData(lambda_names, data_lambda);
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
        for(int l=0;l<n;l++)
            for(int j=0;j<n;j++){
                cov[l][j] = 0;
                for(int k =0;k<dv;k++) cov[l][j] += sigma_manifold[k] * manifold_base[k][l] * manifold_base[k][j];
            }

        
        double[][] ndata = new double[n][SIZE];
        for(int l=0; l< SIZE;l++){
            double vvv[] = random.nextGaussianCLR(expected, cov).array();
            for(int j=0;j<n;j++)
                ndata[j][l] = vvv[j];
        }
        String[] nnames = new String[n];
        for(int l=0; l<n;l++)
            nnames[l] = "C_" + (l+1);
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
