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
import coda.DataFrame.DataFrameException;
import coda.DataFrame.DataFrameListener;
import coda.Element;
import coda.Text;
import coda.Variable;
import coda.gui.CoDaPackMain;
import coda.gui.DataList;
import coda.gui.table.ExcelAdapter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author marc
 */
public final class TablePanel extends JPanel{
    public static final long serialVersionUID = 1L;
    ExcelAdapter ed;
    /**
     *
     */
    DataFrame df = new DataFrame();
    public JTable table;
    
    private JTableHeader header;
    private JPopupMenu renamePopup;
    private JTextField text;
    private TableColumn column;
    
    //private JTable rowTable;
    JScrollPane scrollPane1 = new JScrollPane();
    JPopupMenu pm = new JPopupMenu();
    CoDaPackMain main;
    /**
     *
     */
    
    public TablePanel(CoDaPackMain main){
        
        setLayout(new BorderLayout());
        this.main = main;
        table = new JTable(new DataTableModel(df));

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // allow columns and rows resizing
        table.setColumnSelectionAllowed(true); // allow selection only by columns
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setReorderingAllowed(false); // avoid column reordering
        table.setRowHeight(22);
        // create a row headers with default numbering
        RowNumberTable rowTable = new RowNumberTable(table);

        // add scrolling to table panel
        add(scrollPane1, BorderLayout.CENTER);
        scrollPane1.getViewport().add(table, null);
        scrollPane1.setRowHeaderView(rowTable);
        scrollPane1.setCorner(
                JScrollPane.UPPER_LEFT_CORNER,
                rowTable.getTableHeader());

        table.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent me) {
                showPopup(me);
            }
            @Override
            public void mouseReleased(MouseEvent me) {
                showPopup(me);
            }
        });
        /*
         * MENU
         */
        JMenuBar actions = new JMenuBar();

        // HEADER EDITABLE
        
        header = table.getTableHeader();
        header.addMouseListener(new MouseAdapter(){
          @Override
          public void mouseClicked(MouseEvent event)
          {
            if (event.getClickCount() == 2)
            {
              editColumnAt(event.getPoint());
            }
          }
        });

        text = new JTextField();
        text.setBorder(null);
        text.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e)
          {
            renameColumn();
              try {
                  changeNameDataFrame();
              } catch (DataFrameException ex) {
                  Logger.getLogger(TablePanel.class.getName()).log(Level.SEVERE, null, ex);
              }
          }
        });

        renamePopup = new JPopupMenu();
        renamePopup.setBorder(new MatteBorder(0, 1, 1, 1, Color.DARK_GRAY));
        renamePopup.add(text);
        

        // MENU ACCIONS OUTPUTS!!!!!!!
        this.add(actions, java.awt.BorderLayout.NORTH);
        
    }
    
    private void changeNameDataFrame() throws DataFrameException{
        
        ArrayList<String> namesActualDataFrame = main.getActiveDataFrame().getNames();
        int index = column.getModelIndex();
        String oldName = main.getActiveDataFrame().get(index).getName();
        String newName = text.getText();
        
        if(! namesActualDataFrame.contains(newName)){
            main.getActiveDataFrame().rename(oldName, newName);
            main.updateDataFrame(main.getActiveDataFrame());
        }
        else{
            text.setText(oldName);
            main.updateDataFrame(main.getActiveDataFrame());
            JOptionPane.showMessageDialog(null,"Name already used");
        }
    }
    
    private void editColumnAt(Point p){
        
        int columnIndex = header.columnAtPoint(p);

        if (columnIndex != -1){

          column = header.getColumnModel().getColumn(columnIndex);
          Rectangle columnRectangle = header.getHeaderRect(columnIndex);

          text.setText(column.getHeaderValue().toString());
          renamePopup.setPreferredSize(
              new Dimension(columnRectangle.width, columnRectangle.height - 1));
          renamePopup.show(header, columnRectangle.x, 0);

          text.requestFocusInWindow();
          text.selectAll();
        }
    }

  private void renameColumn(){
    column.setHeaderValue(text.getText());
    //int index = column.getModelIndex();
    //df.get(column.getModelIndex()).setName(text.getText());
    renamePopup.setVisible(false);
    header.repaint();
  }
   
    public void clearData(){
        table.setModel(new DataTableModel(new DataFrame()));
    }
    JMenuItem menuRenameVariable(DataFrame df, Variable var){
        JMenuItem item = new JMenuItem();
        item.setText("Rename variable " + var.getName() + ".");
        item.addActionListener(new VariableRename(this, df, var.getName()));
                
        return item;
    }
    JMenuItem menuFactorizeVariable(final DataFrame df, final Variable var){
        JMenuItem item = new JMenuItem();
        if(var.isText()){
            item.setText("Unfactorize " + var.getName() + ".");
            item.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                     var.toNumeric();
                     df.alertModification(var);
                }
            });
        }else{
            item.setText("Factorize " + var.getName() + ".");
            item.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                     var.toText();
                     df.alertModification(var);
                }

            });
        }
        if(!var.isNumeric() && !var.isText())
            item.setEnabled(false);
        
        return item;
    }
    class VariableRename implements ActionListener{
        String vname;
        JPanel panel;
        DataFrame df;
        public VariableRename(JPanel panel, DataFrame df, String vname){
            this.vname = vname;
            this.panel = panel;
            this.df = df;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean exit = false;
            String name = null;
            while(!exit){
                name = JOptionPane.showInputDialog(panel, "Variable name", vname);
                if(name != null){
                    try {
                        df.rename(vname, name);
                        CoDaPackMain.dataList.setData(df);
                        exit = true;
                    } catch (DataFrameException ex) {
                        JOptionPane.showMessageDialog(panel, "Name already in use!");
                        System.out.println("name not found");
                    }
                }else{
                    exit = true;
                }
            }
            System.out.println(name);
        }
    }
    private void showPopup(MouseEvent me) {
        // is this event a popup trigger?
        if (pm.isPopupTrigger(me)) {
            Point p = me.getPoint();
            int row = table.rowAtPoint(p);
            int col = table.columnAtPoint(p);
            // if we've clicked on a row in the second col
            if (col >= 0) {
                DataFrame dfloc = ((DataTableModel)table.getModel()).dataFrame;
                pm = new JPopupMenu();
                pm.add(menuRenameVariable(dfloc, dfloc.get(col)));
                pm.add(menuFactorizeVariable(dfloc, dfloc.get(col)));
                pm.show(table, p.x, p.y);
            }
        }
    }
    /**
    *
    * @param df
    */
    public void setDataFrame(DataFrame df){
        if(df == null)
            return;
        ed = new ExcelAdapter(table, df);
        table.setDefaultRenderer(Object.class, new DataRenderer(df) );
        df.removeDataFrameListener();
        df.addDataFrameListener(new DataFrameListener(){
            @Override
            public void dataFrameModified(DataFrame df) {
                table.setModel(new DataTableModel(df));
            }
        });
        table.setModel(new DataTableModel(df));
    }
    private static Color outputColor = new Color(162,193,215);
    private class DataRenderer extends DefaultTableCellRenderer{
        public static final long serialVersionUID = 1L;
        
        DataFrame dataFrame;
        
        public DataRenderer(DataFrame df){ 
            dataFrame = df;
        }
        @Override
	public Component getTableCellRendererComponent(JTable table, 
                Object value, boolean isSelected, boolean hasFocus, 
                int row, int column){
            
            setForeground(Color.black);
            setBackground(Color.white);
            setHorizontalAlignment(SwingConstants.RIGHT);
            setFont(new Font ("Monospace", Font.PLAIN, 14));
            
            Variable var = dataFrame.get(column);
            if(row < var.size()){
                Element el = var.get(row);
                //setBackground(isSelected ? outputColor :  Color.white);
                if(el instanceof Text){
                    setHorizontalAlignment(SwingConstants.LEFT);
                    setBackground(isSelected ? new Color(162,193,215):  Color.orange);
                    setBackground(Color.orange);
                }
            }
            if (isSelected){
                setFont( getFont().deriveFont(Font.BOLD) );
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    /**
    *
    */
    public class DataTableModel extends AbstractTableModel{
        DataFrame dataFrame;
        public static final long serialVersionUID = 1L;
        int COLSIZE, ROWSIZE;
        boolean is_editable = true;
        
        /**
         *
         * @param df
         */
        public DataTableModel(DataFrame df){
            dataFrame = df;
            COLSIZE = dataFrame.size();
            ROWSIZE = 0;
            for(int i=0;i<dataFrame.size();i++){
                ROWSIZE = Math.max(ROWSIZE, dataFrame.get(i).size());
            }
        }
        
        @Override
        public int getRowCount() {
            return ROWSIZE;
        }
        @Override
        public int getColumnCount() {
            return COLSIZE;
        }
        @Override
        public String getColumnName(int i){
            
            Variable var = dataFrame.get(i);
            return dataFrame.get(i).getName();   
        }
        @Override
        public boolean isCellEditable(int arg0, int arg1){
            return is_editable;
        }
        @Override
        public Object getValueAt(int row, int col) {
            Variable var = dataFrame.get(col);
            if( row < var.size()){
                return var.get(row).toString();
            }else{
                return "";
            }
        }
        @Override
        public void setValueAt(Object arg, int row, int col) {
            Variable var = dataFrame.get(col);
            Element el = dataFrame.get(col).get(row);
            Element new_el = var.setElementFromString(((String)arg).trim());
            if(new_el != null) {
                var.set(row, new_el);
                dataFrame.setChange(true);
            }
        }
    }
}