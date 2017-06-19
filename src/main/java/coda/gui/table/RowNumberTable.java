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

import java.awt.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/*
 *	Use a JTable as a renderer for row numbers of a given main table.
 *  This table must be added to the row header of the scrollpane that
 *  contains the main table.
 */
/**
 *
 * @author marc
 */
public class RowNumberTable extends JTable
        implements ChangeListener, PropertyChangeListener{

    String names[] = null;

    public static final long serialVersionUID = 1L;
    private JTable main;

    public RowNumberTable(JTable table){
        
        main = table;
        main.addPropertyChangeListener( this );

        setFocusable( false );
        setAutoCreateColumnsFromModel( false );
        setModel( main.getModel() );
        setSelectionModel( main.getSelectionModel() );
        
        
        setRowSelectionAllowed(false);
        TableColumn column = new TableColumn();
        column.setHeaderValue(" ");
        addColumn( column );
        column.setCellRenderer(new RowNumberRenderer());

        getColumnModel().getColumn(0).setPreferredWidth(50);
        setPreferredScrollableViewportSize(getPreferredSize());
    }
    public void setObservationsNames(String names[]){
        this.names= names;
    }
    public void removeObservationsNames(){
        this.names = null;
    }
    /**
     *
     */
    @Override
    public void addNotify(){
        super.addNotify();
        Component c = getParent();
        //  Keep scrolling of the row table in sync with the main table.
        if (c instanceof JViewport){
            JViewport viewport = (JViewport)c;
            viewport.addChangeListener( this );
        }
    }
    /*
     *  Delegate method to main table
     */
    /**
     *
     * @return
     */
    @Override
    public int getRowCount(){
        return main.getRowCount();
    }
    /**
     *
     * @param row
     * @return
     */
    @Override
    public int getRowHeight(int row){
        return main.getRowHeight(row);
    }
    /*
     *  This table does not use any data from the main TableModel,
     *  so just return a value based on the row parameter.
     */
    /**
     *
     * @param row
     * @param column
     * @return
     */
    @Override
    public Object getValueAt(int row, int column){
         String obs = Integer.toString(row + 1);
         if(names != null)
             obs = names[row];
            return obs;
    }
    /*
     *  Don't edit data in the main TableModel by mistake
     */
    /**
     *
     * @param row
     * @param column
     * @return
     */
    @Override
    public boolean isCellEditable(int row, int column){
            return false;
    }
//
//  Implement the ChangeListener
//
    /**
     *
     * @param e
     */
    @Override
    public void stateChanged(ChangeEvent e){
        //  Keep the scrolling of the row table in sync with main table
        JViewport viewport = (JViewport) e.getSource();
        JScrollPane scrollPane = (JScrollPane)viewport.getParent();
        scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
    }
    //
    //  Implement the PropertyChangeListener
    //
    /**
     *
     * @param e
     */
    @Override
    public void propertyChange(PropertyChangeEvent e){
        //  Keep the row table in sync with the main table
        if ("selectionModel".equals(e.getPropertyName())){
                setSelectionModel( main.getSelectionModel() );
        }
        if ("model".equals(e.getPropertyName())){
                setModel( main.getModel() );
        }
    }

    /*
     *  Borrow the renderer from JDK1.4.2 table header
     */
    private static class RowNumberRenderer extends DefaultTableCellRenderer{
        public static final long serialVersionUID = 1L;
        public RowNumberRenderer(){
            setHorizontalAlignment(JLabel.CENTER);
        }
        @Override
        public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column){
            if (table != null){
                JTableHeader header = table.getTableHeader();
                if (header != null){
                    setForeground(header.getForeground());
                    setBackground(header.getBackground());
                    setFont(header.getFont());
                }
            }

            if (isSelected){
                setFont( getFont().deriveFont(Font.BOLD) );
            }
            setText((value == null) ? "" : value.toString());
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));

            return this;
        }
    }
}