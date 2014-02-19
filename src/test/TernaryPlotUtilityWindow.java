/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * WindowTernaryPlot.java
 *
 * Created on Sep 9, 2010, 12:11:02 PM
 */
package test;

import coda.plot.TernaryPlotUtilitySelector;
import coda.plot.window.CoDaPlotWindow;
import java.awt.Dimension;
import javax.swing.JFrame;


/**
 *
 * @author marc
 */
public class TernaryPlotUtilityWindow{
    public static void main(String[] args){
        TernaryPlotUtilitySelector display = new TernaryPlotUtilitySelector();
        JFrame frame = new CoDaPlotWindow(null, display, "Window");
        frame.add(display);
        frame.setSize(600,600);
        frame.setVisible(true);
    }
    

}
