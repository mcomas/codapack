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

import coda.CoDaStats;
import coda.DataFrame;
import coda.gui.CoDaPackMain;
import java.awt.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javax.swing.ButtonGroup;

/**
 *
 * @author mcomas
 */
public class ALRMenu extends MenuDialog{
    public static final long serialVersionUID = 1L;
    RadioButton ra;
    RadioButton ar;
    TextField closure = new TextField("1.0");

    public ALRMenu(final CoDaPackMain mainApp, String title, OptionPane options){
        super(mainApp, title, options);
    }
    
    @Override
    public void acceptButtonActionPerformed() {
        System.out.println();
    }    
}

