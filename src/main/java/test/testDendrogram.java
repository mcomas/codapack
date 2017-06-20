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
import coda.io.ImportData;
import coda.plot.DendrogramDisplay;
import coda.plot.DendrogramDisplay.DendrogramBuilder;
import coda.plot.window.DendrogramWindow;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author mcomas
 */
public class testDendrogram {
    public static void main(String args[]) throws InvalidFormatException {
        try {
            DataFrame df = ImportData.importXLS("/Users/marc/CoDa.xls", true);
            //DataFrame df = ImportData.importXLS("G:/Recerca/EIO_RECERCA/New CoDaPack/CoData.xls", true);
            String[] names = new String[6];
            names[0] = df.get(0).getName();
            names[1] = df.get(1).getName();
            names[2] = df.get(2).getName();
            names[3] = df.get(3).getName();
            names[4] = df.get(4).getName();
            names[5] = df.get(5).getName();
            double[][] data = new double[6][];
            data[0] = df.getNumericalData(names[0]);
            data[1] = df.getNumericalData(names[1]);
            data[2] = df.getNumericalData(names[2]);
            data[3] = df.getNumericalData(names[3]);
            data[4] = df.getNumericalData(names[4]);
            data[5] = df.getNumericalData(names[5]);
            int partition[][] = {{ 1,  1, -1, -1, -1, -1},
                                 { 1, -1,  0,  0,  0,  0},
                                 { 0,  0,  1, -1, -1, -1},
                                 { 0,  0,  0,  1,  1, -1},
                                 { 0,  0,  0,  1, -1, 0}};
            data = CoDaStats.transformRawILR(data, partition);
            DendrogramBuilder builder = new DendrogramBuilder(names, data, partition);

            DendrogramDisplay display = new DendrogramDisplay(builder);
            //display.printBalancesStatistics();
            DendrogramWindow frame = new DendrogramWindow(null, display, "TEST");
            //TernaryPlotDisplay ternaryPlotDisplay = new TernaryPlotDisplay(frame.getContentPane(), names,data);
            //frame.getContentPane().add(ternaryPlotDisplay);
            frame.setSize(600,600);
            frame.setVisible(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(testTernaryPlot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(testTernaryPlot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
