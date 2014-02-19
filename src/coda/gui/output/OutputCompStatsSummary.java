/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.output;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author marc
 */
public class OutputCompStatsSummary implements OutputElement{
    String names[];
    double[] center;
    int values[];
    double percentiles[][];
    public OutputCompStatsSummary(String names[], double center[], int v[], double perc[][]){
        this.names = names;
        this.center = center;
        this.values = v;
        this.percentiles = perc;
    }

    public String printHTML(String html) {
        html += "<b>Statistics</b><br><table>";
        html += "<tr><td></td>";
        if(center != null){
            html += "<th>Center</th>";
        }
        if(percentiles != null){
            for(int i=0;i<values.length;i++){
                html += "<th align='right'>" + values[i] + "</th>";
            }
        }
        html += "</tr>";
        for(int i=0;i<names.length;i++){
            html += "<tr>";
            html += "<th>" + names[i] + "</th>";
            if(center != null){
                html += "<td>" + decimalFormat.format(center[i]) + "</td>";
            }
            if(percentiles != null){
                for(int j=0;j<values.length;j++){
                    html += "<td class=\"alt\">" + decimalFormat.format(percentiles[i][j]) + "</td>";
                }
            }
            html += "</tr>";
        }
        html += "</table>";

        return html;
    }

    public void printText(Writer b) throws IOException{


  
    }

}
