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
import coda.gui.output.OutputTableTwoEntries;
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
public class testLogNormalApproach {
    static boolean  withGraphics = false;
    public static void main(String args[]) throws NotPositiveDefiniteMatrixException {

        /*
         *
         *  CONFIGURATION
         *
         */
        int SIZE = 1000;
        int n = 4;

        double LOGCONTRAST[][] = {
           {1,1,-2,0},
           {0,1,1,-2}
        };
        double EPS[] ={
            0.000002,
            0.000002
        };
        double E[] = { 1, 1, 1, 1};
        double VAR[] = {1, 1, 1, 1};
        double sigma[] = {1, 1, 1, 1};

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
        double base[][] = getKernelBase(a, n);
        double orthoF[][] = new double [0][0];
        if(base.length != 0)
            orthoF = new Matrix(base).transpose().qr().getQ().transpose().getArray();
        int dimF = orthoF.length;
        int dimG = n-dimF;
        base = null;         


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

        double error[] = new double[n];
        for(int i=0;i<n;i++){
            error[i]=0;
            for(int k=0;k<dimG;k++) error[i] += EPS[k] * orthoG[k][i];
        }
        /*
         *
         *  orthoF and orthoError initialized.
         *  Proceed to generate random variables
         *
         */
        Random generator = new Random();
        CoDaRandom random = new CoDaRandom();

        double[][] data1 = new double[4*n][SIZE];
        String[] names = new String[4*n];
        String[] cnames = new String[n];
        for(int i=0; i<n;i++) names[i] = cnames[i] = "c" + (i+1);
        for(int i=0; i<n;i++) names[n+i] = "ortho" + (i+1);
        for(int i=0; i<n;i++) names[2*n+i] = "ln" + (i+1);
        for(int i=0; i<n;i++) names[3*n+i] = "lnOrtho" + (i+1);

        

        CoDaPackMain main = new CoDaPackMain();
        Matrix B = new Matrix(orthoBase);


        double desiredCov[][] = new double[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(i == j){
                    desiredCov[i][j] =  VAR[i];
                }else{
                    desiredCov[i][j] = 0;
                }
            }
        }
        Matrix N = B.times(new Matrix(desiredCov)).times(B.transpose());



        double[][] cov = new double[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(i == j){
                    cov[i][j] =  VAR[i];
                }else{
                    cov[i][j] = 0;
                }
            }
        }
        main.outputPanel.addOutput(new OutputTableTwoEntries(
                "Covariància respecte ortho",cnames,cnames,cov));
        
        Matrix COV = B.transpose().times(new Matrix(cov).times(B));
        cov = COV.getArray();
        main.outputPanel.addOutput(new OutputTableTwoEntries(
                "Covariància 1",cnames,cnames,COV.getArray()));
        COV = B.transpose().times(COV.times(B));
        main.outputPanel.addOutput(new OutputTableTwoEntries(
                "Covariància 2",cnames,cnames,COV.getArray()));

/*
        double cor[][] = new double[n][n];
        double s[] = new double[n];
        for(int i=0;i<n;i++) s[i] = Math.sqrt(cov[i][i]);
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++)
                cor[i][j] = cov[i][j] / (s[i]*s[j]);
        main.outputPanel.addOutput(new OutputTableTwoEntries(
                "Correlacions respecte base canonica",cnames,cnames,cor));
        double invVar[][] = new double[n][n];
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++){
                invVar[i][j] = cor[i][j] * Math.sqrt(VAR[i])* Math.sqrt(VAR[j]);
            }
        main.outputPanel.addOutput(new OutputTableTwoEntries(
                "Covariància desitjada",cnames,cnames,invVar));
        */
        double lambda[] = new double[dimF];
        double epsilon[] = new double[dimF];
        for(int i=0; i< SIZE;i++){
            double [] vec = random.normalRandomVariable(cov); // He canviat per invVar
            for(int k=0;k<dimF;k++) lambda[k] = generator.nextGaussian();
            //for(int k=0;k<dimG;k++) epsilon[k] = EPS[k] * generator.nextGaussian();
            for(int c=0;c<n;c++){
                double vv = clr[c];
                for(int k=0;k<dimF;k++) vv += vec[k] * orthoBase[k][c];
                //for(int k=0;k<dimF;k++) vv += lambda[k] * orthoF[k][c];
                //for(int k=0;k<dimG;k++) vv += epsilon[k] * orthoG[k][c];
                //data1[c][i] = Math.exp(vv);
                data1[c][i] = Math.exp(clr[c]+vec[c]);
                data1[n+c][i] = Math.exp(vv);
                data1[2*n+c][i] = vec[c];
                data1[3*n+c][i] = vv;
            }

        }



        DataFrame df = new DataFrame();
        df.addData(names, data1);


        
        main.addDataFrame(df);
        main.setVisible(true);
        
        //main.outputPanel.addOutput(new OutputTableTwoEntries("Base B",cnames,cnames,orthoBase));
        
        //main.outputPanel.addOutput(new OutputTableTwoEntries("Transpose B",cnames,cnames,B.getArray()));
        /*double[][] cov = new double[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(i == j){
                    cov[i][j] = i < dimF ? sigma[i] * sigma[i] : error[i]*error[i];
                }else{
                    cov[i][j] = 0;
                }
            }
        }*/
        //main.outputPanel.addOutput(new OutputTableTwoEntries("Covariancies respecte B",cnames,cnames,cov));
        //Matrix COV = B.times(new Matrix(cov).times(B.transpose()));
        //main.outputPanel.addOutput(new OutputTableTwoEntries(
         //       "Covariancies respecte base canonica",cnames,cnames,COV.getArray()));

        ///////////////////////////////////
        //double cov[][] = COV.getArray();
        //double corr[][] = new double[n][n];
        /*
        main.outputPanel.addOutput(new OutputTableTwoEntries(
                "Correlacions respecte base canonica",cnames,cnames,corr));
        double invVar[][] = new double[n][n];
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++){
                invVar[i][j] = corr[i][j] * Math.sqrt(VAR[i])* Math.sqrt(VAR[j]);
            }
        invVar = new Matrix(invVar).inverse().getArray();
        Matrix BASE = new Matrix(orthoBase);
        Matrix S = BASE.times(new Matrix(invVar)).times(BASE.transpose()).inverse();
        main.outputPanel.addOutput(new OutputTableTwoEntries(
                "Nova covariancia",cnames,cnames,S.getArray()));
        Matrix SCOV = B.times(S.times(B.transpose()));
        main.outputPanel.addOutput(new OutputTableTwoEntries(
                "Covariancia Resultant",cnames,cnames,SCOV.getArray()));
*/
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
                base_result[i][j] = base[i][pivot[j]];
            }
        }
        return base_result;
    }
}
