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
public class OutputPercentiles implements OutputElement{
    String names[];
    int values[];
    double percentiles[][];
    
    public OutputPercentiles(String n[], int v[], double p[][]){
        names = new String[n.length];
        System.arraycopy(n, 0, names, 0, n.length);

        values = new int[v.length];
        System.arraycopy(v, 0, values, 0, v.length);
        
        percentiles = new double[p.length][p[0].length];
        for(int i=0;i<p.length;i++)
            System.arraycopy(p[i], 0, percentiles[i], 0, p[i].length);
    }

    public String printHTML(String html) {
        html += "<b>Percentiles table:</b><br><table>";
        html += "<tr><th>Percentile</th>";
        for(int i=0;i<names.length;i++){
            html += "<th>" + names[i] + "</th>";
        }
        html += "</tr>";
        for(int i=0;i<percentiles[0].length;i++){
            html += "<tr " + ((i % 2) == 1 ? "class=\"alt\"" : "") + ">" + "<th>" + values[i] + "</th>";
            for(int j=0;j<percentiles.length;j++){
                html += "<td align='right'>" + decimalFormat.format(percentiles[j][i]) + "</td>";
            }
            html += "</tr>";
        }
        html += "</table>";

        return html;
    }

    public void printText(Writer b) throws IOException{
       
        b.write("Percentiles table\n");
        b.write("Percentile");
        for(int i=0;i<names.length;i++){
            b.write(separator + names[i]);
        }
        b.write("\n");
        for(int i=0;i<percentiles[0].length;i++){
            b.write(Integer.toString(values[i]));
            for(int j=0;j<percentiles.length;j++){
                b.write(separator + decimalFormat.format(percentiles[j][i]));
            }
            b.write("\n");
        }
   
    }

}
