/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.sim;

import coda.CoDaStats;
import coda.DataFrame;
import coda.ext.jama.LUDecomposition;
import coda.ext.jama.Matrix;
import coda.gui.CoDaPackMain;
import coda.gui.menu.Biplot3dMenu;
import coda.gui.utils.DataSelector;
import coda.plot.TernaryPlot3dDisplay.TernaryPlot3dBuilder;
import coda.plot.window.TernaryPlot3dWindow;
import java.util.Arrays;
import java.util.Random;
import javax.swing.DefaultListModel;
import org.apache.commons.math.linear.NotPositiveDefiniteMatrixException;

/**
 *
 * @author mcomas
 */
public class testLogNormalWithManifolds {
    static boolean  withGraphics = false;
    public static void main(String args[]) throws NotPositiveDefiniteMatrixException {

        /*
         *
         *  CONFIGURATION
         *
         */
        int SIZE = 31000;
        int n = 7;
        double LOGCONTRAST[][] = {
            {2.5, 1, -3.5, 0, 0, 0, 0},
            {0, 0, 0, 2.5, 1, -3.5, 0}
        };
        double EPS[] ={
            0.004,
            0.004
        };
        double E[] = new double[n];
        E[0] = 1;
        E[1] = 1;
        E[2] = 1;
        E[3] = 1;
        E[4] = 1;
        E[5] = 1;
        E[6] = 1;
        double VAR[] = new double[n];
        VAR[0] = 0.05;
        VAR[1] = 0.05;
        VAR[2] = 0.05;
        VAR[3] = 0.05;
        VAR[4] = 0.05;
        VAR[5] = 0.05;
        VAR[6] = 0.05;

        /*
         *
         *
         */
        short m = (short) LOGCONTRAST.length;
        
        
        double a[][] = new double[m][n];
        for(short i=0;i<m;i++){
            System.arraycopy(LOGCONTRAST[i], 0, a[i], 0, n);
        }
        double clr[] = new double[n];
        double gmean = 1;
        for(int i=0;i<n;i++) gmean *= E[i];
        gmean = Math.pow(gmean, 1f/(double)n);
        for(int i=0;i<n;i++) clr[i] = Math.log(E[i] / gmean);
        // Calculate constants according to clr value
        double b[] = new double[m];
        for(int i=0;i<m;i++){
            b[i] = 0;
            for(int j=0;j<n;j++) b[i] += clr[j] * a[i][j];
        }
        
        // Calculating rang of A
        
        // Calculating rang of extended matrix Ab
        if(m != 0){
            double ab[][] = new double[m][n+1];
            for(int i=0; i<m;i++){
                System.arraycopy(a[i], 0, ab[i], 0, n);
                ab[i][n] = b[i];
            }
            int r_a = new Matrix(a).rank();
            int r_ab = new Matrix(ab).rank();

            if(r_a!=r_ab){
                System.out.println("System not well defined");
                return;
            }else  ab = null;
        }
        
        double base[][] = getKernelBase(a, n);
        double orthoF[][] = new double [0][0];
        if(base.length != 0)
            orthoF = new Matrix(base).transpose().qr().getQ().transpose().getArray();
        int dimF = orthoF.length;
        int dimG = n-dimF;
        base = null; 
        
        a = new double[1][n];
        double orthoError[][] = new double[m][n];
        for(int i=0;i<m;i++){
            System.arraycopy(LOGCONTRAST[i], 0, a[0], 0, n);
            orthoError[i] = new Matrix(a).transpose().qr().getQ().transpose().getArray()[0];
        }


        a = new double[dimF][n];
        for(short i=0;i<dimF;i++){
            System.arraycopy(orthoF[i], 0, a[i], 0, n);
        }
        base = getKernelBase(a,n);
        double orthoBase[][] = new double[n][n];
        System.arraycopy(orthoF, 0, orthoBase, 0, dimF);
        System.arraycopy(base, 0, orthoBase, dimF, dimG);
        orthoBase = new Matrix(orthoBase).transpose().qr().getQ().transpose().getArray();

        double orthoG[][] = new double[dimG][n];
        for(int i=0;i<dimG;i++)
            orthoG[i] = orthoBase[dimF+i];

        double totalError[] = new double[n];
        for(int i=0;i<n;i++){
            totalError[i]=0;
            for(int k=0;k<m;k++) totalError[i] += EPS[k] * orthoError[k][i];
        }

        /*
         *
         *  orthoF and orthoError initialized.
         *  Proceed to generate random variables
         *
         */
        Random generator = new Random();
        CoDaRandom random = new CoDaRandom();

        double[][] data1 = new double[n][SIZE];
        String[] names = new String[n];
        for(int i=0; i<n;i++) names[i] = "c" + (i+1);

        double lambda[] = new double[dimF];
        double epsilon[] = new double[m];

        double sigma[] = new double[dimF];
        for(int i=0;i<dimF;i++)
            sigma[i] = (double)0.5;


        for(int i=0; i< SIZE;i++){
            for(int k=0;k<dimF;k++) lambda[k] = sigma[k] * generator.nextGaussian();
            for(int k=0;k<m;k++) epsilon[k] = totalError[k] * generator.nextGaussian();
            for(int c=0;c<n;c++){
                double vv = clr[c];
                for(int k=0;k<dimF;k++) vv += lambda[k] * orthoF[k][c];
                for(int k=0;k<m;k++) vv += epsilon[k] * orthoError[k][c];
                data1[c][i] = Math.exp(vv);
            }
        }

/*
        double[][] data1 = new double[n][SIZE];
        String[] names = new String[n];
        for(int i=0; i<n;i++) names[i] = "c" + (i+1);

        double lambda[] = new double[dimF];
        double epsilon[] = new double[m];
        double sd[] = new double[m];
        for(int i=0;i<m;i++)
            sd[i] = Math.sqrt(EPS[i]);

        double sigma[] = new double[dimF];
        for(int i=0;i<dimF;i++)
            sigma[i] = 1;

        for(int i=0; i< SIZE;i++){
            for(int k=0;k<dimF;k++) lambda[k] = sigma[k] * generator.nextGaussian();
            for(int k=0;k<m;k++) epsilon[k] = sd[k] * generator.nextGaussian();
            for(int c=0;c<n;c++){
                double vv = clr[c];
                for(int k=0;k<dimF;k++) vv += lambda[k] * orthoF[k][c];
                for(int k=0;k<m;k++) vv += epsilon[k] * orthoError[k][c];
                data1[c][i] = Math.exp(vv);
            }
        }
        */

        DataFrame df = new DataFrame();
        df.addData(names, data1);

       /* double[][] data2 = new double[n][SIZE];
        String[] names2 = new String[n];
        for(int i=0; i<n;i++) names2[i] = "d" + (i+1);
        for(int i=0; i< SIZE;i++){
            double z [] = random.normalRandomVariable(E, cov);
            for(int k=0;k<n;k++) data2[k][i] = Math.exp( z[k] );
        }
        df.addData(names, data2);*/

        CoDaPackMain main = new CoDaPackMain();
        main.addDataFrame(df);
        main.setVisible(true);

        
        
        if(withGraphics){
            /*
             * BIPLOT
             */
            Biplot3dMenu biplot = new Biplot3dMenu(main);
            DataSelector dataSel = biplot.getDataSelector();
            dataSel.setSelectedData((DefaultListModel)dataSel.getAvailableData());
            biplot.acceptButtonActionPerformed();

            /*
             * TERNARY PLOT
             */
            TernaryPlot3dWindow frame2 = new TernaryPlot3dWindow(
                        df, new TernaryPlot3dBuilder(names, data1).build(), "TEST");
            frame2.setSize(600,600);
            frame2.setVisible(true);
        }  
    }
    public static double[][] getKernelBase(double[][] a, int n){

        if(a.length ==0){
            double[][] result = new double[n][n];
            for(int i=0;i<n;i++)
                for(int j=0;j<n;j++) result[i][j] = i == j ? 1 : 0;

            return result;
        }

        Matrix A = new Matrix(a);
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
