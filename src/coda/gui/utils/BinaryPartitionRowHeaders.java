package coda.gui.utils;

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
public class BinaryPartitionRowHeaders extends JTable implements ChangeListener, PropertyChangeListener{
    public static final long serialVersionUID = 1L;



    private String variables[];
    private int order[];
    private int nvariables;

    private BinaryPartitionTable main;

    public BinaryPartitionRowHeaders(BinaryPartitionTable main, int order[], String variables[], int heightRow){
        this.main = main;
        this.order = order;
        this.variables = variables;
        nvariables = variables.length;

        setRowHeight(heightRow);
        setFocusable( false );
        this.setEnabled(false);
        setAutoCreateColumnsFromModel( false );
        setModel( main.getModel() );
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableColumn column = new TableColumn();
        column.setHeaderValue(" ");
        addColumn( column );
        column.setCellRenderer(new BinaryParitionRowHeaderCellRenderer());

        getColumnModel().getColumn(0).setPreferredWidth(150);
        setPreferredScrollableViewportSize(getPreferredSize());

    }
    
    /*
     *  This table does not use any data from the main TableModel,
     *  so just return a value based on the row parameter.
     */
    @Override
    public Object getValueAt(int row, int column){
        if(row < variables.length) return variables[order[row]];
        else return Integer.toString(row + 1);
            //return Integer.toString(row + 1);
    }

    /*
     *  Don't edit data in the main TableModel by mistake
     */
    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
    //
    //  Implement the ChangeListener
    //
    
    public void stateChanged(ChangeEvent e){
        //  Keep the scrolling of the row table in sync with main table

        JViewport viewport = (JViewport) e.getSource();
        JScrollPane scrollPane = (JScrollPane)viewport.getParent();
        scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
    }
    //
    //  Implement the PropertyChangeListener
    //
    
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
    @Override
    public void mousePressed(MouseEvent arg0){ }
    @Override
    public void mouseClicked(MouseEvent arg0){
        int actualCol = actualStep -1;
        if(active_vars[order_vars[getSelectedRow()]]){
            String val = (String) main.getModel().getValueAt(getSelectedRow(), actualCol);
            if(val == null || val.equals(BinaryPartitionSelect.Gr1) ){
                main.getModel().setValueAt(BinaryPartitionSelect.Gr2, getSelectedRow(), actualCol);
            }else{
                main.getModel().setValueAt(BinaryPartitionSelect.Gr1, getSelectedRow(), actualCol);
            }
        }
    }
    @Override
    public void mouseReleased(MouseEvent arg0) {}

    @Override
    public void mouseEntered(MouseEvent arg0) {}

    @Override
    public void mouseExited(MouseEvent arg0) {}

    /*
     *  Borrow the renderer from JDK1.4.2 table header
     */
    private static class BinaryParitionRowHeaderCellRenderer extends DefaultTableCellRenderer{
        public static final long serialVersionUID = 1L;
        public BinaryParitionRowHeaderCellRenderer(){
            //setHorizontalAlignment(JLabel.CENTER);
            setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
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
