/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.sim;

import coda.BasicStats;
import coda.CoDaStats;
import coda.DataFrame;
import coda.ext.jama.Matrix;
import coda.gui.CoDaPackMain;
import coda.sim.TernaryPlotDisplay.TernaryPlotBuilder;

/**
 *
 * @author mcomas
 */
public class SimulateNewIdea {
/*    static double E[] = {12.38554091, 25.17032576, 11.59358333};

    static double COV[][] = {
        { 2.838459206,  0.006489224, -2.689902869},
        { 0.006489224,  0.461800715, -0.362505938},
        {-2.689902869, -0.362505938,  3.000580869}};
*/
    //static double E[] = {0.6, 1, 0.4};
    static double E[] = {0.5, 1, 0.5};
    static double a[][] = {
        {0.40824829, -0.816496581, 0.40824829},
        {-1,  0, 1}
    };

    
    public static void main(String args[]){

        int m = a.length;
        int n = a[0].length;

        Matrix X = new Matrix(data);
        Matrix A = new Matrix(a).transpose();
        
        double center[] = E;//CoDaStats.center(X.transpose().getArrayCopy());
        double var[] = new double[2];
        var[0] = 0.001; // Per logcontrast unitari.
        //var[1] = Math.pow( (Math.log(1/0.2) - Math.log(center[2]/center[0])) / 3 , 2);
        var[1] = Math.pow( (Math.log(1.5) - Math.log(center[2]/center[0])) / 3 , 2);

        Matrix invA = A.inverse();

        double clr_mean[][] = new double[1][];
        clr_mean[0] = CoDaStats.transformRawCLR(center);
        Matrix CLR_MEAN = new Matrix(clr_mean).transpose();

        Matrix ILR_MEAN = invA.times(CLR_MEAN).transpose();
        Matrix ILR_COV = new Matrix(var);
        //ILR_COV.set(0,1,-0.8*Math.sqrt(var[0])*Math.sqrt(var[1]));
        //ILR_COV.set(1,0,-0.8*Math.sqrt(var[0])*Math.sqrt(var[1]));
        
        int SIZE = 66;
        double generatedData[][] = new double[n][SIZE];
        for(int i=0; i< SIZE;i++){
                double vec[] = CoDaRandom.nextGaussianILR(
                ILR_MEAN.getArray()[0],
                ILR_COV.getArray(), a);
                for(int j =0;j<n;j++)
                    generatedData[j][i] = vec[j];
        }

        String[] names = new String[n];
            for(int i=0; i<n;i++)
                names[i] = "C_" + (i+1);

        CoDaPackMain main = new CoDaPackMain();
        DataFrame df = new DataFrame();
        df.addData(names, generatedData);
        main.addDataFrame(df);
        main.setVisible(true);

            TernaryPlotWindow frame = new TernaryPlotWindow(
                        null, new TernaryPlotBuilder(names, generatedData).build(), "TEST");

            frame.setSize(600,600);
            frame.setVisible(true);


    }
    static double data[][] = {
        {13.8026,25.1957,9.9935},
        {13.8125,25.1736,10.0463},
        {13.9359,25.423,9.8296},
        {13.81,25.2008,9.9785},
        {14.5042,24.9122,10.1385},
        {13.8376,25.0766,10.1154},
        {14.4493,24.6775,10.3222},
        {13.8429,25.4384,10.277},
        {14.0183,24.1767,10.5138},
        {13.9704,24.905,10.6622},
        {14.2558,25.2716,9.9266},
        {13.8151,25.4665,10.0946},
        {13.7785,25.2379,10.539},
        {12.6756,24.7199,10.9719},
        {13.7852,24.6804,9.6864},
        {12.3542,24.5873,12.3426},
        {12.0769,24.8418,11.9158},
        {12.4759,25.3083,11.6195},
        {10.1885,25.1166,12.9056},
        {13.0504,24.5551,11.9102},
        {12.269,24.1397,13.3036},
        {12.1603,25.4857,11.8469},
        {12.4908,25.0117,11.9357},
        {11.2957,25.3947,12.6783},
        {12.1761,25.0484,12.007},
        {12.2834,25.0826,12.0889},
        {10.9913,24.188,12.9801},
        {11.4638,24.9744,12.6794},
        {11.5446,24.8623,13.2276},
        {10.4261,25.5429,12.8981},
        {11.0274,24.7961,13.4613},
        {11.3014,24.8046,13.5979},
        {10.1385,23.8791,14.1725},
        {10.742,24.5999,13.611},
        {11.0571,24.7406,13.5702},
        {10.049,25.465,13.0675},
        {11.584,24.9745,12.7126},
        {11.8568,23.6827,13.2005},
        {11.3256,24.727,13.6503},
        {11.2034,24.7563,13.0323},
        {11.1323,23.9975,13.202},
        {12.9203,25.3313,10.7816},
        {12.7747,25.9918,10.4744},
        {13.0834,24.4846,11.9573},
        {14.1269,26.2277,8.6574},
        {14.1211,26.1574,8.8132},
        {12.2718,25.6833,11.5518},
        {12.7991,26.1663,10.8269},
        {9.7492,25.5141,14.0169},
        {8.9652,24.7371,15.1738},
        {8.7458,25.1346,15.1837},
        {14.6146,25.3294,9.263},
        {13.8069,25.5179,10.1824},
        {13.4476,25.849,10.4563},
        {15.3171,24.5544,8.6357},
        {13.6732,25.9355,9.1417},
        {13.6809,25.8191,9.9078},
        {14.4927,26.3335,8.6147},
        {10.5342,24.6777,14.1951},
        {15.6013,26.0269,8.1423},
        {12.918,25.2548,10.7932},
        {12.5149,25.5184,11.3986},
        {13.4573,24.5078,10.3697},
        {8.682,27.358,13.4695},
        {9.1566,26.0199,12.8078},
        {9.0325,26.9924,13.6488}};
}
