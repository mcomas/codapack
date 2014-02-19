/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.output;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

/**
 *
 * @author mcomas
 */
public class OutputPlotStart {
    private String plotType;
    private HashMap<String,Object> plotLegend;
    private OutputPlotStart(String plotType, HashMap<String,Object> plotLegend){
        this.plotType = plotType;
        this.plotLegend = plotLegend;
    }
    public String printHTML(String html) {

        html += "<b>" + plotType +" generated</b><br><b>Legend:</b><br>";
        for(String item: plotLegend.keySet()){
            html += item + " " + plotLegend.get(item);
        }
        return html;
    }
    public void printText(Writer b) throws IOException {
        b.write(plotType +" generated\n");
        b.write("Legend:\n");
        for(String item: plotLegend.keySet()){
            b.write(item + " " + plotLegend.get(item) );
        }
    }
}
