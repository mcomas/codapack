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
public class OutputSingleValue implements OutputElement{
    String label;
    double value;
    int type;
    public OutputSingleValue(String l, int v){
        type = 1;
        this.label = l;
        value = v;
    }
    public OutputSingleValue(String l, double v){
        type = 2;
        this.label = l;
        value = v;
    }
    public String printHTML(String html) {
        if(type == 1){
            html += "<b>" + label + ":</b><br>" + (int)value + "<br>";
        }else if(type ==2){
            html += "<b>" + label + ":</b><br>" + decimalFormat.format(value) + "<br>";
        }

        return html;
    }

    public void printText(Writer b) throws IOException{

        b.write(label + "\n" + separator);
        if (type == 1) {
            b.write( Integer.toString((int)value));
        } else if (type == 2) {
            b.write(decimalFormat.format(value));
        }
        b.write("\n");
  

    }

}
