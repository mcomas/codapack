/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot2;

import coda.plot.CoDa2dDisplay;
import coda.plot2.objects.Ternary2dObject;
import java.util.ArrayList;

/**
 *
 * @author mcomas
 */
public interface TernaryPlot2dBuilder {
    ArrayList<Ternary2dObject> getObjects(CoDa2dDisplay display);
}
