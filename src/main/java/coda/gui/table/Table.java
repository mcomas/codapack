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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;


/**
 *
 * @author marc
 */
public final class Table extends BorderPane{
    public static final long serialVersionUID = 1L;
    ExcelAdapter ed;
    /**
     *
     */
    DataFrame df = new DataFrame();

    JScrollPane scrollPane1 = new JScrollPane();
    JPopupMenu pm = new JPopupMenu();
    CoDaPackMain main;
    /**
     *
     */
    
    public Table(CoDaPackMain main){
        this.main = main;
        
        int rowCount = 0;
        int columnCount = 0;
        GridBase grid = new GridBase(rowCount, columnCount);

        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
//        for (int row = 0; row < grid.getRowCount(); ++row) {
//            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
//            for (int column = 0; column < grid.getColumnCount(); ++column) {
//                list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,"value"));
//            }
//            rows.add(list);
//        }
        grid.setRows(rows);

        SpreadsheetView spv = new SpreadsheetView(grid);
        this.setCenter(spv);
    }
}