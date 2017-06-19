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

package coda.plot.window;

import coda.DataFrame;
import coda.plot.RealPlot2dDisplay;

/**
 *
 * @author mcomas
 */
public class RealPlot2dWindow extends CoDaPlot2dWindow{
    private RealPlot2dDisplay realPlot;
    public RealPlot2dWindow(DataFrame dataframe, RealPlot2dDisplay display, String title){
        super(dataframe, display, title);
        this.realPlot = display;
    }
}
