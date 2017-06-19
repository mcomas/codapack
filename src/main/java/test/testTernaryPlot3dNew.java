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

import coda.plot2.PlotUtils;
import coda.plot2.objects.Ternary3dDataObject;
import coda.plot2.objects.Ternary3dObject;
import coda.plot2.objects.Ternary3dSurfaceObject;
import coda.plot2.TernaryPlot3dDisplay;
import java.awt.Color;

import javax.swing.JFrame;

/**
 *
 * @author mcomas
 */
public class testTernaryPlot3dNew {
    public static void main(String args[]) {
        
        String names[] = {"X", "Y", "Z", "T"};
        
        JFrame frame = new JFrame();
        frame.setSize(600,500);
        TernaryPlot3dDisplay display = new TernaryPlot3dDisplay(names);


        double a[][] =
        {{1,1,4,3},
         {1,4,1,2},
         {4,1,2,1}};
        
        Ternary3dObject planeObject1 = new Ternary3dSurfaceObject(display,
                PlotUtils.plane(a[0], a[1], a[2], 5));
        planeObject1.setColor(new Color(200, 230, 100));
        display.addCoDaObject(planeObject1);

        /*
        double axis2[] = {0.1,0.5,1};
        Ternary3dObject ellipsoid2 = new Ternary3dSurfaceObject(display,
                PlotUtils.ellipsoid( axis2, 50));
        ellipsoid2.setColor(new Color(100, 100, 230));
        display.addCoDaObject(ellipsoid2);
        
        double axis[] = {0.2,1,2};
        Ternary3dObject ellipsoid = new Ternary3dSurfaceObject(display,
                PlotUtils.ellipsoid( axis, 50));
        ellipsoid.setColor(new Color(230, 100, 100));
        display.addCoDaObject(ellipsoid);

        double axis3[] = {0.4,2,4};
        Ternary3dObject ellipsoid3 = new Ternary3dSurfaceObject(display,
                PlotUtils.ellipsoid( axis3, 50));
        ellipsoid3.setColor(new Color(100, 230, 100));
        display.addCoDaObject(ellipsoid3);
*/
        Ternary3dObject dataObject = new Ternary3dDataObject(display, data);
        dataObject.setColor(new Color(70, 70, 200));
        display.addCoDaObject(dataObject);

        Ternary3dObject dataObject2 = new Ternary3dDataObject(display, a);
        dataObject2.setColor(Color.black);
        display.addCoDaObject(dataObject2);
        

        //CoDaObject
        frame.getContentPane().add(display);
        frame.setVisible(true);
    }
    static double definedGrid[] =
        {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};

    static double data[][] = {
        {31.7,3.8,6.4,9.3},
        {23.8,9,9.2,9.8},
        {9.100001,34.2,9.5,10.2},
        {23.8,7.2,10.1,8},
        {38.3,2.9,7.7,6.9},
        {26.2,4.2,12.5,4.8},
        {33,4.6,12.2,5.6},
        {5.2,42.9,9.600001,7.7},
        {11.7,26.7,9.600001,10.8},
        {46.6,0.7,5.6,4.5},
        {19.5,11.4,9.5,9.7},
        {37.3,2.7,5.5,9.3},
        {8.5,38.9,8,11.9},
        {12.9,23.4,15.8,6.5},
        {17.5,15.8,8.3,12.2},
        {7.3,40.9,12.9,6.6},
        {44.3,1,7.8,3.7},
        {32.3,3.1,8.7,6.3},
        {15.8,20.4,8.3,13.2},
        {11.5,23.8,11.6,8.5},
        {16.6,16.8,12,8.8},
        {25,6.8,10.9,7.4},
        {34,2.5,9.399999,5.5},
        {16.6,17.6,9.600001,10.7},
        {24.9,9.7,9.8,9.7}};
}