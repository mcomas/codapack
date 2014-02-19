/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import coda.CoDaStats;
import coda.DataFrame;
import coda.plot.TernaryPlot2dDisplay;
import coda.gui.CoDaPackMain;
import coda.plot.window.TernaryPlot3dWindow;
import coda.plot.window.TernaryPlot2dWindow;
import coda.io.ImportData;
import coda.plot.TernaryPlot3dDisplay;
import coda.plot.TernaryPlot3dDisplay.TernaryPlot3dBuilder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author mcomas
 */
public class testTernaryPlot3d {
    //try {
        ///
    public static void main(String args[]) {
        System.out.println(CoDaStats.ternaryTransform(1,0,0,0)[0]+","+CoDaStats.ternaryTransform(1,0,0,0)[1]+","+CoDaStats.ternaryTransform(1,0,0,0)[2]);
        System.out.println(CoDaStats.ternaryTransform(0,1,0,0)[0]+","+CoDaStats.ternaryTransform(0,1,0,0)[1]+","+CoDaStats.ternaryTransform(0,1,0,0)[2]);
        System.out.println(CoDaStats.ternaryTransform(0,0,1,0)[0]+","+CoDaStats.ternaryTransform(0,0,1,0)[1]+","+CoDaStats.ternaryTransform(0,0,1,0)[2]);
        System.out.println(CoDaStats.ternaryTransform(0,0,0,1)[0]+","+CoDaStats.ternaryTransform(0,0,0,1)[1]+","+CoDaStats.ternaryTransform(0,0,0,1)[2]);
        try {
            DataFrame df = ImportData.importXLS("/home/marc/software/codapack-dev/data/halimba.xls", true);
            //DataFrame df = ImportData.importXLS("G:/Recerca/EIO_RECERCA/New CoDaPack/CoData.xls", true);
            String[] names = new String[4];
            names[0] = df.getName(0);
            names[1] = df.getName(1);
            names[2] = df.getName(2);
            names[3] = df.getName(3);
            double[][] data = new double[4][];
            data[0] = df.getNumericalData(names[0]);
            data[1] = df.getNumericalData(names[1]);
            data[2] = df.getNumericalData(names[2]);
            data[3] = df.getNumericalData(names[3]);

            TernaryPlot3dWindow frame = new TernaryPlot3dWindow(
                    df, new TernaryPlot3dBuilder(names, data).build(), "TEST3d");
            frame.setSize(600,600);
            frame.setVisible(true);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(testTernaryPlot3d.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(testTernaryPlot3d.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(testTernaryPlot3d.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
