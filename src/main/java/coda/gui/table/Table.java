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

package coda.gui.table;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import javafx.scene.layout.Pane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;


/**
 *
 * @author marc
 */
public final class Table extends Pane{
    public static final long serialVersionUID = 1L;
    ExcelAdapter ed;
    /**
     *
     */
    DataFrame df = new DataFrame();
    public JTable table;
    //private JTable rowTable;
    JScrollPane scrollPane1 = new JScrollPane();
    JPopupMenu pm = new JPopupMenu();
    CoDaPackMain main;
    /**
     *
     */
    
    public Table(){
        
   
    }
}