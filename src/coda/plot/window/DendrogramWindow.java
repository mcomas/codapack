/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot.window;

import coda.DataFrame;
import coda.plot.DendrogramDisplay;

/**
 *
 * @author mcomas
 */
public class DendrogramWindow extends CoDaPlotWindow{
    private DendrogramDisplay realPlot;
    public DendrogramWindow(DataFrame dataframe, DendrogramDisplay display, String title){
        super(dataframe, display, title);
        this.realPlot = display;
    }
}
