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
import coda.ext.jama.Matrix;
import coda.ext.jama.SingularValueDecomposition;
import coda.plot2.window.TernaryPlot2dWindow;

import coda.plot2.PrincipalComponent2dBuilder;
import coda.plot2.objects.Ternary2dObject;
import coda.plot2.objects.Ternary2dGridObject;
import coda.plot2.TernaryPlot2dDisplay;

/**
 *
 * @author mcomas
 */
public class testBuildPrincipalComponents {
    public static void main(String args[]) {
        String names[] = {"X", "Y", "Z"};
        
        double dataset1[][] = new Matrix(data).transpose().getArray();
        //double dataset2[][] = new Matrix(data1).transpose().getArray();
        TernaryPlot2dDisplay display = new TernaryPlot2dDisplay(names);

        Ternary2dObject gridObject = new Ternary2dGridObject(display, definedGrid);
        display.addCoDaObject(gridObject);

        double [][]cdata = CoDaStats.centerData(dataset1);
            cdata = CoDaStats.transformRawCLR(cdata);
        Matrix Z = (new Matrix(cdata)).transpose();

        SingularValueDecomposition svd =
            new SingularValueDecomposition(Z);

        double[][] pcomp = new Matrix(CoDaStats.transformCLRRaw(svd.getV().getArray())).transpose().getArray();

        PrincipalComponent2dBuilder build = new PrincipalComponent2dBuilder(dataset1, pcomp[0], pcomp[1]);

        display.addCoDaObject(build.getObjects(display));
        
        //CoDaObject
        TernaryPlot2dWindow frame = new TernaryPlot2dWindow(null, display, "Test");
        frame.setCenter(CoDaStats.center(dataset1));
        //JFrame frame = new JFrame();
        //frame.setSize(600,500);
        //frame.getContentPane().add(display);
        frame.setVisible(true);
    }
    static double definedGrid[] =
        {0.01, 0.05, 0.10, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99};
        //{0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};

    static double data[][] = {
            {31.7,3.8,6.4},
            {23.8,9,9.2},
            {9.100001,34.2,9.5},
            {23.8,7.2,10.1},
            {38.3,2.9,7.7},
            {26.2,4.2,12.5},
            {33,4.6,12.2},
            {5.2,42.9,9.600001},
            {11.7,26.7,9.600001},
            {46.6,0.7,5.6},
            {19.5,11.4,9.5},
            {37.3,2.7,5.5},
            {8.5,38.9,8},
            {12.9,23.4,15.8},
            {17.5,15.8,8.3},
            {7.3,40.9,12.9},
            {44.3,1,7.8},
            {32.3,3.1,8.7},
            {15.8,20.4,8.3},
            {11.5,23.8,11.6},
            {16.6,16.8,12},
            {25,6.8,10.9},
            {34,2.5,9.399999},
            {16.6,17.6,9.600001},
            {24.9,9.7,9.8}};
}