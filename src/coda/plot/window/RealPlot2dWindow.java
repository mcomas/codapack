/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot.window;

import coda.DataFrame;
import coda.plot.RealPlot2dDisplay;

/**
 *
 * @author mcomas
 */
public class RealPlot2dWindow extends CoDaPlot2dWindow{
    private RealPlot2dDisplay realPlot;
    public RealPlot2dWindow(DataFrame dataframe, RealPlot2dDisplay display, String title){
        super(dataframe, display, title);
        this.realPlot = display;
    }
}
