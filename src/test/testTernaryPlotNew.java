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
import coda.plot2.window.TernaryPlot2dWindow;
import coda.plot2.datafig.Circle;
import coda.plot2.datafig.FilledCircle;
import coda.plot2.datafig.FilledSquare;
import coda.plot2.datafig.FilledStar;
import coda.plot2.datafig.FilledTriangleDown;
import coda.plot2.datafig.FilledTriangleUp;
import coda.plot2.PlotUtils;

import coda.plot2.objects.Ternary2dObject;
import coda.plot2.objects.Ternary2dCurveObject;
import coda.plot2.objects.Ternary2dDataObject;
import coda.plot2.objects.Ternary2dGridObject;
import coda.plot2.TernaryPlot2dDisplay;
import java.awt.Color;

/**
 *
 * @author mcomas
 */
public class testTernaryPlotNew {
    public static void main(String args[]) {
        System.out.println(CoDaStats.ternaryTransform(1,0,0,0)[0]+","+CoDaStats.ternaryTransform(1,0,0,0)[1]+","+CoDaStats.ternaryTransform(1,0,0,0)[2]);
        System.out.println(CoDaStats.ternaryTransform(0,1,0,0)[0]+","+CoDaStats.ternaryTransform(0,1,0,0)[1]+","+CoDaStats.ternaryTransform(0,1,0,0)[2]);
        System.out.println(CoDaStats.ternaryTransform(0,0,1,0)[0]+","+CoDaStats.ternaryTransform(0,0,1,0)[1]+","+CoDaStats.ternaryTransform(0,0,1,0)[2]);
        System.out.println(CoDaStats.ternaryTransform(0,0,0,1)[0]+","+CoDaStats.ternaryTransform(0,0,0,1)[1]+","+CoDaStats.ternaryTransform(0,0,0,1)[2]);

        String names[] = {"X", "Y", "Z"};
        
        double dataset1[][] = new Matrix(data).transpose().getArray();
        //double dataset2[][] = new Matrix(data1).transpose().getArray();
        TernaryPlot2dDisplay display = new TernaryPlot2dDisplay(names);

        Ternary2dObject gridObject = new Ternary2dGridObject(display, definedGrid);
        display.addCoDaObject(gridObject);

        Ternary2dObject dataObject = new Ternary2dDataObject(display, data, new FilledCircle());
        dataObject.setColor(new Color(70, 70, 200));
        display.addCoDaObject(dataObject);
        /*
        Ternary2dObject dataObject2 = new Ternary2dDataObject(display, dataset2);
        dataObject2.setColor(new Color(200, 70, 0));
        display.addCoDaObject(dataObject2);
        */
        double a[] = {1,1,1};
        double v[] = {4,1.2,1};
        Ternary2dObject lineObject1 = new Ternary2dCurveObject(display,
                PlotUtils.line(a, v, 100));
        lineObject1.setColor(Color.CYAN);
        display.addCoDaObject(lineObject1);

/*
        CoDaObject curveObject1 = new CurveObject(display,
                PlotUtils.segment(data[0], data[2], 200));
        curveObject1.setColor(new Color(70, 200, 70));
        display.addCoDaObject(curveObject1);
 */
/*
        double o1[] = {2,1};
        double a1[] = {2,1};
        CoDaObject curveObject2 = new CurveObject(display,
                PlotUtils.ellipse(o1, a1, 0, 1000));
        curveObject2.setColor(new Color(200, 70, 70));
        display.addCoDaObject(curveObject2);
*/
        double o2[] = {2,1};
        double a2[] = new double[2];
        for(int i=0;i<20;i++){
            a2[0] = 0.15 * i;
            a2[1] = 0.1 * i;
            Ternary2dObject curveObject3 = new Ternary2dCurveObject(display,
                    PlotUtils.ellipse(o2, a2, Math.PI, 200));
            curveObject3.setColor(new Color(100, 70, 70));
            display.addCoDaObject(curveObject3);
        }
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