/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió
 *
 *	This file is part of CoDaPack.
 *
 *  CoDaPack is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CoDaPack is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CoDaPack.  If not, see <http://www.gnu.org/licenses/>.
 */

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
