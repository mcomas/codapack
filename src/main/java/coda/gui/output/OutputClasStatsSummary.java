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
public class OutputClasStatsSummary implements OutputElement{
    String names[];
    double[] mean;
    double[] sd;
    int values[];
    double percentiles[][];
    public OutputClasStatsSummary(String names[], double mean[], double sd[], int v[], double perc[][]){
        this.names = names;
        this.mean = mean;
        this.sd = sd;
        this.values = v;
        this.percentiles = perc;
    }

    public String printHTML(String html) {
        html += "<b>Statistics</b><br><table>";
        html += "<tr><td></td>";
        if(mean != null){
            html += "<th>Mean</th>";
        }
        if(sd != null){
            html += "<th>Std.Dev</th>";
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
            if(mean != null){
                html += "<td>" + decimalFormat.format(mean[i]) + "</td>";
            }
            if(sd != null){
                html += "<td>" + decimalFormat.format(sd[i]) + "</td>";
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
