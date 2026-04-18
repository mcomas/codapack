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
 * @author mcomas
 */
public class OutputPlotHeader implements OutputElement{
    String[] data;
    String title;
    public OutputPlotHeader(String title, String[] data){
        this.title = title;
        this.data = data;
    }
    public String printHTML(String html) {
        html += "<b>" + title +":</b><br>";
        html += "<em>Data: </em>";
        for(int i=0;i<data.length;i++){
            html +=  data[i] + " ";
        }
        html += "<br>";
        return html;
    }

    public void printText(Writer b) throws IOException {
        
    }

}
