package coda.sim;


import coda.DataFrame;
import coda.ext.jama.LUDecomposition;
import coda.ext.jama.Matrix;
import coda.gui.CoDaPackMain;
import coda.gui.menu.Biplot3dMenu;
import coda.gui.output.OutputTableTwoEntries;
import coda.gui.utils.DataSelector;
import java.util.Random;
import javax.swing.DefaultListModel;
import org.apache.commons.math.linear.NotPositiveDefiniteMatrixException;

/**
 *
 * @author mcomas
 */
public class testLogNormalConditionalDistribution {
    static boolean  withGraphics = false;

    static double LOGCONTRAST[][] = {
            //{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            {2.5, 1, -3.5, 0, 0},
            {0, 0,  2.5, 1, -3.5}
        };
    static double EPS[] ={
            0.01,
            0.01
        };

   static double E[] = { 0.005, 0.095, 0.4, 0.3, 0.2};
   static double VAR[] = {2, 1, 1, 1, 10};

    public static void main(String args[]) throws NotPositiveDefiniteMatrixException {
        /*
         *
         *  CONFIGURATION
         *
         */
        int SIZE = 1000;
        int n = 5;

        String[] names = new String[3*n];
        String[] cnames = new String[n];
        for(int i=0; i<n;i++) names[2*n+i] = cnames[i] = "c" + (i+1);
        for(int i=0; i<n;i++) names[n+i] = "log" + (i+1);
        for(int i=0; i<n;i++) names[i] = "norm" + (i+1);
        /*
         *
         *
         */
        CoDaPackMain main = new CoDaPackMain();
        short m = (short) LOGCONTRAST.length;

        double a[][] = new double[m][n];
        for(short i=0;i<m;i++){
            System.arraycopy(LOGCONTRAST[i], 0, a[i], 0, n);
        }

        // Calculate constants according to clr value
        double b[] = new double[m];
        for(int i=0;i<m;i++){
            b[i] = 0;
            for(int j=0;j<n;j++) b[i] += Math.log(E[i]) * a[i][j];
        }
        // Calculating rang of A
        // Calculating rang of extended matrix Ab
        if(a.length != 0){
            double ab[][] = new double[m][n+1];
            for(int i=0; i<b.length;i++){
                System.arraycopy(a[i], 0, ab[i], 0, a[0].length);
                ab[i][a[0].length] = b[i];
            }
            int r_a = new Matrix(a).rank();
            int r_ab = new Matrix(ab).rank();

            if(r_a!=r_ab){
                System.out.println("System not well defined");
                return;
            }else  ab = null;
        }
        double baseF[][] = getKernelBase(a, n);//new Matrix(getKernelBase(a, n)).transpose().qr().getQ().transpose().getArray();
        double baseG[][] = a;        
        int dimF = baseF.length;
        int dimG = baseG.length;

        double base[][] = new double [n][];
        System.arraycopy(baseF, 0, base, 0, dimF);
        System.arraycopy(baseG, 0, base, dimF, dimG);

        String[] base_name = new String[n];
        for(int i=0;i<dimF;i++)
            base_name[i] = "f" + Integer.toString(i+1);
        for(int i=0;i<dimG;i++)
            base_name[dimF+i] = "g" + Integer.toString(i+1);

        CoDaPackMain.outputPanel.addOutput(new OutputTableTwoEntries(
                "Base B",cnames, base_name, base));


        


        Matrix T = new Matrix(base).transpose();
        Matrix invT = T.inverse();
/*
        double v[] = new double[n];
        double vvv[];
        System.arraycopy(VAR, 0, v, 0, n);
        for(int i=0;i<1;i++){
            //for(int c=0;c<n;c++){
                int c = 0;
                for(int j=0;j<1000;j++){
                    vvv = function(v, T, invT, dimF);
                    v[c] = vvv[c];
                }
                c = 2;
                v[c] = 2;
                for(int j=0;j<1000;j++){
                    vvv = function(v, T, invT, dimF);
                    v[c] = vvv[c];
                }
            //}
        }
*/
        double var[][] = new double[n][n];
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++)
                var[i][j] = i == j ? VAR[i] : 0;

        CoDaPackMain.outputPanel.addOutput(new OutputTableTwoEntries(
                "Matrix to coordinates respect B", null, null, invT.getArray()));

        CoDaPackMain.outputPanel.addOutput(new OutputTableTwoEntries(
                "Matrix to canonic", null, null, T.getArray()));

        Matrix C = invT.times(new Matrix(var)).times(invT.transpose());

        CoDaPackMain.outputPanel.addOutput(new OutputTableTwoEntries(
                "Covariance matrix respect B", base_name, base_name, C.getArray()));


        Matrix C11 = C.getMatrix(0, dimF-1, 0, dimF-1);
        Matrix C12 = C.getMatrix(0, dimF-1, dimF, n-1);
        Matrix C22 = C.getMatrix(dimF, n-1, dimF, n-1);
        Matrix C21 = C.getMatrix(dimF, n-1, 0, dimF-1);

        Matrix cov = new Matrix(n,n);
        cov.times(0);
        cov.setMatrix(0, dimF-1, 0, dimF-1, C11.minus(C12.times(C22.inverse().times(C21))));
        for(int i=0;i<dimG;i++)
            cov.set(dimF+i, dimF+i, EPS[i]);
        //Matrix covF = C11.minus(C12.times(C22.inverse().times(C21)));
        //Matrix covF = C.getMatrix(0, dimF-1, 0, dimF-1);
        //Matrix covG = C.getMatrix(dimF, n-1, dimF, n-1);

        CoDaPackMain.outputPanel.addOutput(new OutputTableTwoEntries(
                " matrix in F",null,null, cov.getMatrix(0, dimF-1, 0, dimF-1).getArray()));

        CoDaPackMain.outputPanel.addOutput(new OutputTableTwoEntries(
                " matrix in G",null,null, cov.getMatrix(dimF, n-1, dimF, n-1).getArray()));

        CoDaPackMain.outputPanel.addOutput(new OutputTableTwoEntries(
                "Expected covariance", cnames, cnames, T.times(cov).times(T.transpose()).getArray()));
        //Matrix covEst =
        /*
         *
         *  orthoF and orthoError initialized.
         *  Proceed to generate random variables
         *
         */
        Random generator = new Random();
        CoDaRandom random = new CoDaRandom();

        double[][] data1 = new double[3*n][SIZE];

        double lambda[] = new double[dimF];
        double epsilon[] = new double[dimF];
        double [] vec;
        //double [] vecG = new double[dimG];
        for(int i=0; i< SIZE;i++){
            vec = random.normalRandomVariable(cov.getArray());
            //vecG = random.normalRandomVariable(covG.getArray());
            //for(int c=0;c<dimG;c++)vecG[c] = EPS[c] * generator.nextGaussian();
            for(int c=0;c<n;c++){
                double vv = Math.log(E[c]);
                for(int k=0;k<n;k++) vv += vec[k] * base[k][c];
                //for(int k=0;k<dimG;k++) vv += vecG[k] * baseG[k][c];
                data1[2*n+c][i] = Math.exp(vv);
                data1[n+c][i] = vv;
                data1[c][i] = vec[c];
            }
        }

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
    public static double[] function(double x[], Matrix B, Matrix invB, int dimF){
        int n = x.length;
        int dimG = n - dimF;
        Matrix C = invB.times(new Matrix(x)).times(invB.transpose());

        Matrix C11 = C.getMatrix(0, dimF-1, 0, dimF-1);
        Matrix C12 = C.getMatrix(0, dimF-1, dimF, n-1);
        Matrix C22 = C.getMatrix(dimF, n-1, dimF, n-1);
        Matrix C21 = C.getMatrix(dimF, n-1, 0, dimF-1);

        Matrix cov = new Matrix(n,n);
        cov.times(0);
        cov.setMatrix(0, dimF-1, 0, dimF-1, C11.minus(C12.times(C22.inverse().times(C21))));
        for(int i=0;i<dimG;i++)
            cov.set(dimF+i, dimF+i, EPS[i]);

        Matrix COV = B.times(cov).times(B.transpose());
        double res[] = new double[n];
        for(int i=0;i<n;i++){
            res[i] = x[i] + (VAR[i]-COV.get(i, i));
        }
        return res;
    }
    public static double[][] unitaryVector(double a[][]){
        double vec[][] = new double[a.length][a[0].length];
        for(int i=0;i<a.length;i++){
            double norm = 0;
            for(int j=0;j<a[0].length;j++){
                norm += a[i][j]*a[i][j];
            }
            norm = Math.sqrt(norm);
            for(int j=0;j<a[0].length;j++){
                vec[i][j] = a[i][j] / norm;
            }
        }
        return vec;
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

