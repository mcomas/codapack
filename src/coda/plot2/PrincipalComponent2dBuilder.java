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

package coda.plot2;

import coda.CoDaStats;
import coda.ext.jama.Matrix;
import coda.plot.CoDa2dDisplay;
import coda.plot2.datafig.FilledCircle;
import coda.plot2.objects.Ternary2dCurveObject;
import coda.plot2.objects.Ternary2dDataObject;
import coda.plot2.objects.Ternary2dObject;
import java.util.ArrayList;

/**
 *
 * @author marc
 */
public class PrincipalComponent2dBuilder implements TernaryPlot2dBuilder{

    private double[] pc1, pc2;
    double data[][] = null;

    public ArrayList<Ternary2dObject> getObjects(CoDa2dDisplay display) {
        ArrayList<Ternary2dObject> objects = new ArrayList<Ternary2dObject>();
        double center[] = CoDaStats.center(data);
        Ternary2dCurveObject PC1 = new Ternary2dCurveObject(display, PlotUtils.line(center, pc1,100) );
        objects.add( PC1 );
        Ternary2dCurveObject PC2 = new Ternary2dCurveObject(display, PlotUtils.line(center, pc2,100) );
        objects.add( PC2 );
        objects.add( new Ternary2dDataObject(display, new Matrix(data).transpose().getArray(), new FilledCircle()) );

        return objects;
    }
    public PrincipalComponent2dBuilder(double[][] data, double[] pc1, double[] pc2){
        this.pc1 = pc1;
        this.pc2 = pc2;

        this.data = data;
    }
}
