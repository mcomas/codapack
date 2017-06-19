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
public class OutputCenter implements OutputElement{
    String names[];
    double[] center;
    
    private OutputCenter(String n[], double[] c){
        names = new String[n.length];
        System.arraycopy(n, 0, names, 0, n.length);

        center = new double[c.length];
        System.arraycopy(c, 0, center, 0, c.length);
    }
    public String printHTML(String html) {
        html += "<b>Center:</b><br><table><tr>";
        for(int i=0;i<center.length;i++){
            html += "<th>" + names[i] + "</th>";
        }
        html += "</tr><tr>";
        for(int i=0;i<center.length;i++){
            html += "<td>" + decimalFormat.format(center[i]) + "</td>";
        }
        html += "</tr></table>";

        return html;
    }
    public void printText(Writer b) throws IOException{

        b.write("Center\n");
        for(int i=0;i<center.length;i++){
            b.write(separator + names[i]);
        }
        b.write("\n");
        for(int i=0;i<center.length;i++){
            b.write(separator + decimalFormat.format(center[i]));
        }
        b.write("\n");

    }
}
