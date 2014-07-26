/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.sim;

import coda.DataFrame;
import coda.ext.jama.LUDecomposition;
import coda.ext.jama.Matrix;
import coda.gui.CoDaPackMain;
import coda.gui.menu.Biplot3dMenu;
import coda.gui.utils.DataSelector;
import java.util.Arrays;
import java.util.Random;
import javax.swing.DefaultListModel;
import org.apache.commons.math.linear.NotPositiveDefiniteMatrixException;

/**
 *
 * @author mcomas
 */
public class testLogNormalConditionalDistributionCLR {
    static boolean  withGraphics = true;
    public static void main(String args[]) throws NotPositiveDefiniteMatrixException {

        /*
         *
         *  CONFIGURATION
         *
         */
        int SIZE = 1000;
        int n = 5;

        double LOGCONTRAST[][] = {
            //{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            {1, 1,-2, 0, 0},
            {0, 0, 1,-1, 0}
        };
        double EPS[] ={
            0.05,
            0.05
        };
        double E[] = { 0.005, 0.095, 0.4, 0.3, 0.2};
        double MARGIN[] = { 0.9, 0.2, 0.2, 0.1, 0.1};
        double MIN[] = new double[n];
        double MAX[] = new double[n];//{ 0.8, 0.3, 0.5, 0.3, 0.3};

        double expected[] = new double[n];
        double sigma[] = new double[n];
        for(int i=0;i<n;i++){
            MIN[i] = Math.exp(E[i]-MARGIN[i]);
            MAX[i] = Math.exp(E[i]+MARGIN[i]);
            sigma[i] = (Math.log(MAX[i])-Math.log(MIN[i])) / 6;
        }
        //double VAR[] = { 2, 2, 4, 1, 1};

        /*
         *
         *
         */
        short m = (short) LOGCONTRAST.length;
        
        double A[][] = new double[m+1][n];
        for(short i=0;i<m;i++){
            System.arraycopy(LOGCONTRAST[i], 0, A[i], 0, n);
        }
        Arrays.fill(A[m],1);

        

        double logb[] = new double[m+1];
        for(short i=0;i<m;i++){
            double b = 1;
            for(int k=0;k<n;k++)
                b *= Math.pow(E[k], A[i][k]);
            logb[i] = Math.log(b);
        }
        logb[m] = 0;

        // Calculating rang of A
        // Calculating rang of extended matrix Ab
        if(A.length != 0){
            double AExtended[][] = new double[m+1][n+1];
            for(int i=0; i<m+1;i++){
                System.arraycopy(A[i], 0, AExtended[i], 0, n);
                AExtended[i][n] = logb[i];
            }
            int r_a = new Matrix(A).rank();
            int r_ab = new Matrix(AExtended).rank();

            if(r_a!=r_ab){
                System.out.println("System not well defined");
                return;
            }else  AExtended = null;
        }

        double base[][] = new double[n-1][n];
        double baseF[][] = getKernelBase(A, n);

        int dimF = baseF.length;
        for(int i=0;i<dimF;i++){
            System.arraycopy(baseF[i], 0, base[i], 0, n);
        }
        double baseG[][] = new double[m][n];

        int dimG = m;
        for(int i=0;i<dimG;i++){
            System.arraycopy(A[i], 0, baseG[i], 0, n);
            System.arraycopy(baseG[i], 0, base[dimF+i], 0, n);
        }   

        Matrix BASE = new Matrix(base);

        Matrix clrA = new Matrix(LOGCONTRAST);
        Matrix ilrA = clrA.times(BASE.transpose());

        double var[][] = new double[n][n];
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++)
                var[i][j] = i == j ? sigma[i]*sigma[i] : 0;

        
        Matrix Ci  = new Matrix(var);
        //Matrix C = BASE.inverse().times(Ci).times(BASE);
        Matrix C = BASE.times(Ci).times(BASE.inverse());

        Matrix C11 = C.getMatrix(0, dimF-1, 0, dimF-1);
        Matrix C12 = C.getMatrix(0, dimF-1, dimF, n-1);
        Matrix C22 = C.getMatrix(dimF, n-1, dimF, n-1);
        Matrix C21 = C.getMatrix(dimF, n-1, 0, dimF-1);

        double cov[][] = C11.minus(C12.times(C22.inverse().times(C21))).getArray();
        /*
        double newCov[][] = new double[n][n];
        for(int i =0;i<n;i++)
            for(int j=0;j<n;j++)
                newCov[i][j] = i < dimF && j < dimF ? condC11[i][j] : 0;
        
        Matrix NEWCOV = new Matrix(newCov);
        Matrix RESULT = BASE.inverse().times(NEWCOV).times(BASE);
        //Matrix RESULT = BASE.times(NEWCOV).times(BASE.inverse());
        double res[][] = RESULT.getArray();

        double variance[] = new double[n];
        */


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
        String[] cnames = new String[n];
        for(int i=0; i<n;i++) names[i] = cnames[i] = "c" + (i+1);
        //for(int i=0; i<n;i++) names[n+i] = "ln" + (i+1);

        double lambda[] = new double[dimF];
        double epsilon[] = new double[dimF];
        double [] vecF;
        double [] vecG = new double[dimG];
        for(int i=0; i< SIZE;i++){
            vecF = random.normalRandomVariable(cov);
            for(int k=0;k<dimG;k++) vecG[k] = EPS[k] * generator.nextGaussian();

            for(int c=0;c<n;c++){
                double vv = logb[c];
                for(int k=0;k<dimF;k++) vv += vecF[k] * baseF[k][c];
                for(int k=0;k<dimG;k++) vv += vecG[k] * baseG[k][c];
                data1[c][i] = Math.exp(logb[c]+vv);
                //data1[n+c][i] = clr[c]+vv;
            }
        }
        
        CoDaPackMain main = new CoDaPackMain();



        DataFrame df = new DataFrame();
        df.addData(names, data1);


        
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
            /*
            TernaryPlot3dWindow frame2 = new TernaryPlot3dWindow(
                        new TernaryPlot3dBuilder(names, data1).build(), "TEST");
            frame2.setSize(600,600);
            frame2.setVisible(true);
             * */
        }  
    }
    public static double[][] getKernelBase(double a[][], int n){
        if(a == null || a.length == 0){
            double res[][] = new double[n][n];
            for(int i=0;i<n;i++)
                for(int j=0;j<n;j++) res[i][j] = i == j ? 1 : 0;
            return res;
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
                base_result[i][pivot[j]] = base[i][j];
            }
        }
        return base_result;
    }
}
