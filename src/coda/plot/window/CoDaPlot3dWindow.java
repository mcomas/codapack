/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot.window;

import coda.DataFrame;
import coda.gui.menu.SelectVariableMenu;
import coda.plot.AbstractCoDaDisplay;

/**
 *
 * @author mcomas
 */
public class CoDaPlot3dWindow extends CoDaPlotWindow{
    public CoDaPlot3dWindow(DataFrame dataframe, final AbstractCoDaDisplay plotDisplay, String title) {
        super(dataframe, plotDisplay,title);
        
    }
}
