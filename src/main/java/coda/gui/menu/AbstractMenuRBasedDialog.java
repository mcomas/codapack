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

package coda.gui.menu;

import coda.gui.utils.DataSelector;

import org.rosuda.JRI.Rengine;

import coda.gui.CoDaPackMain;
/**
 *
 * @author mcomas
 */
public abstract class AbstractMenuRBasedDialog extends AbstractMenuDialog{
    Rengine re;

    public AbstractMenuRBasedDialog(final CoDaPackMain mainApp, String title, DataSelector dataSelector, Rengine r){
        super(mainApp, title, dataSelector);
        re = r;
        super.initialize();
    }
    /*
    public AbstractMenuRBasedDialog(final CoDaPackMain mainApp,String title,  Rengine r, boolean groups, int variable_type){
        super(mainApp, title, groups, variable_type);
        mainApplication = mainApp;
        re = r;
        this.df = mainApplication.getActiveDataFrame();              
        this.ds = new DataSelector(df, groups, variable_type);
        initialize();
    }
    */
}


