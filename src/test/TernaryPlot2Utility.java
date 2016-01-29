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
import coda.plot2.TernaryPlot2dDisplay;
import coda.plot2.objects.Ternary2dCurveObject;
import coda.plot2.objects.Ternary2dGridObject;
import coda.plot2.objects.Ternary2dObject;
import coda.plot2.window.TernaryPlot2dWindow;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author marc
 */
public class TernaryPlot2Utility {
    public static void main(String[] args) throws IOException, InterruptedException {
        String names[] = {"X", "Y", "Z"};
                TernaryPlot2dDisplay display = new TernaryPlot2dDisplay(names);

                double definedGrid[] =
            {0.01, 0.05, 0.10, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99};
                Ternary2dObject gridObject = new Ternary2dGridObject(display, definedGrid);
                display.addCoDaObject(gridObject);
                double center[] = {1,1,1};

                TernaryPlot2dWindow frame = 
                        new TernaryPlot2dWindow(null, display, "Ternary Plot -- Testing version");
                frame.setCenter(center);
                frame.setVisible(true);
                
                //display = frame.getDisplay();
                double a[] = {1,2,3};
                double b[] = {4,3,2};
                
                double c[][] = PlotUtils.segment(a, b, 100);
                display.addCoDaObject( new Ternary2dCurveObject(display, c) );
                
                ArrayList<Ternary2dObject> visObjet = display.getCoDaObjects();
                
                System.out.println("Hola");
    }
}
