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
public class OutputILRPartition implements OutputElement{

    int[][] partition;
    String[] names;
    public OutputILRPartition(String n[], int[][] p){
        names = new String[n.length];
        System.arraycopy(n, 0, names, 0, n.length);
        
        partition = new int[p.length][p[0].length];
        for(int i=0;i<p.length;i++)
            System.arraycopy(p[i], 0, partition[i], 0, p[i].length);
    }
    public String printHTML(String html) {
        html += "<b>ILR binary partition:</b><br><table>";
        html += "<tr>";
        for(int i=0;i<names.length;i++){
            html += "<th>" + names[i] + "</th>";
        }
        html += "</tr>";
        for(int i=0;i<partition.length;i++){
            html += "<tr>";
            for(int j=0;j<partition[0].length;j++){
                html += "<td align='right'>" +
                        Integer.toString(partition[i][j]) + "</td>";
            }
            html += "</tr>";
        }
        html += "</table>";

        return html;
    }
    public void printText(Writer b) throws IOException{

        b.write("ILR binary partition\n");
        for(int i=0;i<names.length;i++){
            b.write(separator + names[i]);
        }
        b.write("\n");
        for(int i=0;i<partition.length;i++){
            for(int j=0;j<partition[0].length;j++){
                b.write(separator + Integer.toString(partition[i][j]));
            }
            b.write("\n");
        }

    }
}
