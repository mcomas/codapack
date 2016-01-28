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
package coda.io;


import coda.DataFrame;
import java.awt.Component;

/**
 *
 * @author marc
 */
public interface Importer {
    /*
     * It creates an importer from parameters manually choosen
     * by a user. The menu are located according to frame.
     */
    /**
     *
     * @param frame
     * @return
     */
    public Importer setParameters(Component frame);
    /*
     * It creates an importer from parameters passed through
     * string pars.
     */
    public Importer setParameters(String pars);
    /*
     * Get the current parameters of an importer codified as 
     * a single string. Values are separated with '?' character.
     */
    public String getParameters();
    /*
     * import data if the importer is well defined otherwise
     * the function should return null
     */
    public DataFrame importDataFrame();
    

}
