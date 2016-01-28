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
package coda;

import coda.gui.CoDaPackConf;

/**
 *
 * @author marc
 */
public class Numeric implements Element<Double>{
    double v;
    Variable var = null;
    
    public Numeric(double v){
        this.v = v;
    }
    @Override
    public Double getValue() {
        return v;
    }

    @Override
    public void setValue(Double v) {
        this.v = v;
    }
    
    public void setValue(double v) {
        this.v = v;
    }    
    @Override
    public String toString(){
        return(CoDaPackConf.getDecimalTableFormat().format(v));
    }

    @Override
    public Element variable(Variable v) {
        this.var = v;
        return(this);
    }
}
