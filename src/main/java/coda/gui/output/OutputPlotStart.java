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
