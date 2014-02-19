/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.sim;

import coda.DataFrame;
import coda.Utils;
import coda.ext.jama.LUDecomposition;
import coda.ext.jama.Matrix;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputTableTwoEntries;
import java.util.Arrays;
import java.util.Random;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.linear.NotPositiveDefiniteMatrixException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.MultiStartMultivariateRealOptimizer;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.direct.MultiDirectional;
import org.apache.commons.math.random.GaussianRandomGenerator;
import org.apache.commons.math.random.JDKRandomGenerator;
import org.apache.commons.math.random.UncorrelatedRandomVectorGenerator;

/**
 *
 * @author mcomas
 */
public class SimALNbyLogcontrasts {
    static int SIZE = 66;

    static double LOGCONTRAST[][] = {
        {1, -2, 1 }
    };
    static double E[] = { 25.19544886, 51.21187175,	23.59267939 };
    //static double VAR[] = { 136.6339254, 2.941496607, 163.557604 };
    static double VAR[] = { 11.68905152, 1.715079184, 12.78896415 };

    static Matrix B;
    static Matrix invB;
    static Matrix F = Utils.getMatrixF(VAR.length);
    static int dimF;
    static int dimG;
    static double []logVAR = new double[VAR.length];
    static double []sd_err;
    static double []alpha = new double[VAR.length];
    static Matrix ALPHA;
    public static void main(String args[]) throws NotPositiveDefiniteMatrixException, FunctionEvaluationException, OptimizationException {
        int n = E.length;
        int m = LOGCONTRAST.length;

        for(short i=0;i<n;i++){
            logVAR[i] = Math.log(1+ VAR[i]/(E[i]*E[i]));
        }
        // Defining system
        double a[][] = new double[m][n];
        for(short i=0;i<m;i++){
            System.arraycopy(LOGCONTRAST[i], 0, a[i], 0, n);
        }
        double b[] = new double[m];
        sd_err = new double[m];
        for(int i=0;i<m;i++){
            b[i] = 0;
            for(int j=0;j<n;j++){
                b[i] += Math.log(E[j]) * a[i][j];
            }
            sd_err[i] = 0.05 * Math.abs(b[i]);
        }

        // Defining bases
        double [][]baseF = getBaseF(a);
        double [][]baseG = LOGCONTRAST;
        double base[][] = new double [n-1][];
        dimF = baseF.length;
        dimG = baseG.length;
        System.arraycopy(baseF, 0, base, 0, dimF);
        System.arraycopy(baseG, 0, base, dimF, dimG);

        B = new Matrix(base).transpose();
        invB = B.inverse();

        double var[] = new double[n];
        System.arraycopy(VAR, 0, var, 0, n);

        Matrix clrCov = F.times(Utils.getDiagonalMatrix(logVAR)).times(F);
        for(int i=0;i<n;i++){
            alpha[i] = (logVAR[i] - clrCov.get(i, i)) * 0.5;
        }
        ALPHA = new Matrix(n,n);
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                ALPHA.set(i,j, alpha[i]+alpha[j]);
            }
        }
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair solution = optimizer.optimize(new OptimizeFunction(), GoalType.MINIMIZE, var);
        
        double bestVAR[] = solution.getPoint();
        
        double vvv[] = {1,1,1};
        CoDaPackMain main = new CoDaPackMain();
        Matrix COV = getCovariance(invB, F.times(Utils.getDiagonalMatrix(vvv)).times(F));
        Matrix COVexp = B.times(COV).times(B.transpose()).plus(ALPHA);
        
        CoDaPackMain.outputPanel.addOutput(new OutputTableTwoEntries(
            "Expected Covariance", null, null, COVexp.getArrayCopy()));

        Random generator = new Random();
        CoDaRandom random = new CoDaRandom();
        double[][] data1 = new double[2*n][SIZE];
        double [] vec;
        for(int i=0; i< SIZE;i++){
            vec = random.normalRandomVariable(COV.getArray());
            for(int c=0;c<n;c++){
                double vv = Math.log(E[c]);
                for(int k=0;k<n-1;k++) vv += vec[k] * base[k][c];
                data1[c][i] = Math.exp(vv);
                data1[n+c][i] = Math.exp(Math.log(E[c]) + Math.sqrt(bestVAR[c]) * generator.nextGaussian());
            }
        }
        DataFrame df = new DataFrame();
        String[] names = new String[2*n];

        for(int i=0; i<n;i++) names[i] = "c" + (i+1);
        for(int i=0; i<n;i++) names[n+i] = "logc" + (i+1);
        df.addData(names, data1);
        main.addDataFrame(df);

        main.setVisible(true);


    }

    private static double[][] getBaseF(double[][] a){
        int m = a.length;
        if(m>0){
            int n = a[0].length;
            double clrPlane[][] = new double[1][n];
            Arrays.fill(clrPlane[0], 1);
            double Aclr[][] = new double[m+1][];
            for(int i=0;i<m;i++)
                Aclr[i] = a[i];
            Aclr[m] = clrPlane[0];

            return getKernelBase(Aclr, n);
        }
        return new double[0][0];
    }

    private static double[][] getKernelBase(double a[][], int n){
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

    public static Matrix getCovariance(Matrix invB, Matrix clrCov){
        int n = clrCov.getRowDimension();
        Matrix C = invB.times(clrCov).times(invB.transpose());

        Matrix C11 = C.getMatrix(0, dimF-1, 0, dimF-1);
        Matrix C12 = C.getMatrix(0, dimF-1, dimF, n-2);
        Matrix C22 = C.getMatrix(dimF, n-2, dimF, n-2);
        Matrix C21 = C.getMatrix(dimF, n-2, 0, dimF-1);

        Matrix COV = new Matrix(n-1,n-1);
        COV.times(0);
        COV.setMatrix(0, dimF-1, 0, dimF-1, C11.minus(C12.times(C22.inverse().times(C21))));
        for(int i=0;i<n-dimF-1;i++)
            COV.set(dimF+i, dimF+i, sd_err[i]*sd_err[i]);

        return COV;
    }

    public static class OptimizeFunction implements MultivariateRealFunction{
        public double value(double[] var) throws FunctionEvaluationException, IllegalArgumentException {

            int n = var.length;

            boolean negative = false;
            for(int i=0;i<n;i++)
                negative = var[i] < 0 ? true : negative;
            if(negative)
                return 100000;

            Matrix CLR_SIGMA = F.times(Utils.getDiagonalMatrix(var)).times(F);
            Matrix COV  = getCovariance(invB, CLR_SIGMA);
            Matrix expectedCOV = B.times(COV).times(B.transpose()).plus(ALPHA);

            double totalError = 0;
            for(int i=0; i<n; i++){
                double dist = Math.abs(logVAR[i]- expectedCOV.get(i, i));
                totalError += dist / (logVAR[i]);
            }
            return totalError;
        }
    }
}
