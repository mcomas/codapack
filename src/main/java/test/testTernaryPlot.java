/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió
 *
 *	This file is part of CoDaPack.
 *
 *  CoDaPack is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CoDaPack is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CoDaPack.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import coda.CoDaStats;
import coda.DataFrame;
import coda.plot.window.TernaryPlot2dWindow;
import coda.io.ImportData;
import coda.plot.TernaryPlot2dDisplay.TernaryPlot2dBuilder;
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
public class testTernaryPlot {
    //try {
        ///
    public static void main(String args[]) {
        System.out.println(CoDaStats.ternaryTransform(1,0,0,0)[0]+","+CoDaStats.ternaryTransform(1,0,0,0)[1]+","+CoDaStats.ternaryTransform(1,0,0,0)[2]);
        System.out.println(CoDaStats.ternaryTransform(0,1,0,0)[0]+","+CoDaStats.ternaryTransform(0,1,0,0)[1]+","+CoDaStats.ternaryTransform(0,1,0,0)[2]);
        System.out.println(CoDaStats.ternaryTransform(0,0,1,0)[0]+","+CoDaStats.ternaryTransform(0,0,1,0)[1]+","+CoDaStats.ternaryTransform(0,0,1,0)[2]);
        System.out.println(CoDaStats.ternaryTransform(0,0,0,1)[0]+","+CoDaStats.ternaryTransform(0,0,0,1)[1]+","+CoDaStats.ternaryTransform(0,0,0,1)[2]);
        try {
            DataFrame df = ImportData.importXLS("/Users/marc/CoDa.xls", true);
            //DataFrame df = ImportData.importXLS("G:/Recerca/EIO_RECERCA/New CoDaPack/CoData.xls", true);
            String[] names = new String[3];
            names[0] = df.get(0).getName();
            names[1] = df.get(1).getName();
            names[2] = df.get(2).getName();
            double[][] data = new double[3][];
            data[0] = df.getNumericalData(names[0]);
            data[1] = df.getNumericalData(names[1]);
            data[2] = df.getNumericalData(names[2]);

            //TernaryPlot2dWindow frame = new TernaryPlot2dWindow(new TernaryPlot2dDisplay(names, data), "TEST");
            TernaryPlot2dWindow frame = new TernaryPlot2dWindow(
                    df, new TernaryPlot2dBuilder(names, data).build(), "TEST");
            //TernaryPlotDisplay ternaryPlotDisplay = new TernaryPlotDisplay(frame.getContentPane(), names,data);
            //frame.getContentPane().add(ternaryPlotDisplay);
            frame.setSize(600,600);
            frame.setVisible(true);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(testTernaryPlot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(testTernaryPlot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(testTernaryPlot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}