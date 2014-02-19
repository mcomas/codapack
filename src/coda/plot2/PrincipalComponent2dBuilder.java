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
