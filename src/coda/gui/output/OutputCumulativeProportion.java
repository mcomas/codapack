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

import java.io.Writer;

/**
 *
 * @author mcomas
 */
public class OutputCumulativeProportion implements OutputElement{

    double[] svd;
    int rank;
    
    public OutputCumulativeProportion(double[] svd, int rank){
        this.svd = svd;
        this.rank = rank;
    }
    public String printHTML(String html) {
        double total = 0;
        for(double v : svd) total += v*v;        
        html += "<em>Cumulative proportion explained:</em><br>";
        double parcial = 0;
        for(int i=0;i<Math.min(rank,svd.length);i++){
            html += "<tr>" + "<td>" + Integer.toString(i+1) + " component: " + "</td>";
            parcial += svd[i]*svd[i];
            html += "<td align='right'>" + decimalFormat.format(parcial/total) + "</td>";

            html += "</tr>";
        }
        html += "</table>";
        return html;
    }

    public void printText(Writer b) {
        
    }

}
