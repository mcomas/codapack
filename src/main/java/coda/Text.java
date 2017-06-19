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

/**
 *
 * @author marc
 */
public class Text implements Element<String>{
    String value;
    Variable var = null;
    
    public Text(String v){
        value = v;
    }
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String v) {
        value = v;
    }
    @Override
    public String toString(){
        return value;
    }
    @Override
    public Element variable(Variable v) {
        this.var = v;
        return(this);
    }    
}
