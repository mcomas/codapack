/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot2;

import coda.plot.CoDa3dDisplay;
import coda.plot2.objects.Ternary3dObject;
import java.util.ArrayList;

/**
 *
 * @author mcomas
 */
public interface TernaryPlot3dBuilder {
    ArrayList<Ternary3dObject> getObjects(CoDa3dDisplay display);
}
