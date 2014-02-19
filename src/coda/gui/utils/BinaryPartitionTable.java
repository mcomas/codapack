/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author marc
 */
public class BinaryPartitionTable extends JTable implements MouseListener{
    public static final long serialVersionUID = 1L;

    int actualColumn;
    private int actualStep = 1;
    private String variables[];

    private int nvariables;
    private boolean active_vars[];
    private int order[];
    BinaryPartitionRowHeaders rowHeaders;
    public BinaryPartitionCellRenderer cellRend;
    public BinaryPartitionTable(BinaryPartitionRowHeaders rowHeaders, int order_vars[], boolean active_vars[], int nvariables){
        super(nvariables,nvariables-1);

        this.rowHeaders = rowHeaders;
        this.nvariables = nvariables;
        
        cellRend = new BinaryPartitionCellRenderer((DefaultTableModel) this.getModel());
        setDefaultRenderer(Object.class, cellRend );
        
        this.active_vars = active_vars;
        this.order = order_vars;
        for(int i=0;i<nvariables;i++){              
            getModel().setValueAt(BinaryPartitionSelect.Gr1, i, 0);
        }        
        this.addMouseListener(this);
    }
    public int getActualStep(){
        return actualStep;
    }
    public void setPreviousStep(int partition[][]){
        actualStep--;
        actualColumn--;
        if(actualColumn < 0 ){
            actualStep = 1;
            actualColumn = 0;
        }
        for(int i=0;i < nvariables;i++){
            for(int j=0;j < nvariables-1; j++){
                if(partition[j][order[i]] == BinaryPartitionSelect.VGr0){
                    getModel().setValueAt(BinaryPartitionSelect.Gr0, i, j);
                }
                if(partition[j][order[i]] == BinaryPartitionSelect.VGr1){
                    getModel().setValueAt(BinaryPartitionSelect.Gr1, i, j);
                }
                if(partition[j][order[i]] == BinaryPartitionSelect.VGr2){
                    getModel().setValueAt(BinaryPartitionSelect.Gr2, i, j);
                }
            }
            if(active_vars[order[i]]){
                getModel().setValueAt(BinaryPartitionSelect.Gr1, i, actualColumn);
            }else{
                getModel().setValueAt(BinaryPartitionSelect.Gr0, i, actualColumn);
            }

        }
        this.updateUI();
        
    }
    public void setNextStep(int partition[][]){
        for(int i=0;i < nvariables;i++){
            for(int j=0;j < actualStep; j++){
                if(partition[j][order[i]] == BinaryPartitionSelect.VGr0){
                    getModel().setValueAt(BinaryPartitionSelect.Gr0, i, j);
                }
                if(partition[j][order[i]] == BinaryPartitionSelect.VGr1){
                    getModel().setValueAt(BinaryPartitionSelect.Gr1, i, j);
                }
                if(partition[j][order[i]] == BinaryPartitionSelect.VGr2){
                    getModel().setValueAt(BinaryPartitionSelect.Gr2, i, j);
                }
            }
            if(actualStep + 1< nvariables){
                if(active_vars[order[i]]){
                    getModel().setValueAt(BinaryPartitionSelect.Gr1, i, actualStep);
                }else{
                    getModel().setValueAt(BinaryPartitionSelect.Gr0, i, actualStep);
                }
            }
        }
        this.updateUI();
        actualStep++;
        actualColumn++;
        //if(actualColumn == this.getColumnCount()) actualColumn = -1;
    }
    public void mouseClicked(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        int row = table.rowAtPoint(e.getPoint());
        int actualCol = actualStep -1;

        if(active_vars[order[row]]){
            String val = (String) getModel().getValueAt(row, actualCol);
            if(val == null || val.equals(BinaryPartitionSelect.Gr1) ){
                getModel().setValueAt(BinaryPartitionSelect.Gr2, row, actualCol);
            }else{
                getModel().setValueAt(BinaryPartitionSelect.Gr1, row, actualCol);
            }
        }
    }

    public void mousePressed(MouseEvent arg0) {
       ;
    }

    public void mouseReleased(MouseEvent arg0) {

    }

    public void mouseEntered(MouseEvent arg0) {

    }

    public void mouseExited(MouseEvent arg0) {
        
    }

 
    public class BinaryPartitionCellRenderer extends JLabel implements TableCellRenderer{
        public static final long serialVersionUID = 1L;
        private Border noFocusBorder;
        DefaultTableModel model;
        Color foreground;
        Color background;

        int size;

        public BinaryPartitionCellRenderer(DefaultTableModel mod){
            model = mod;
            size = model.getRowCount();


            noFocusBorder = new EmptyBorder(1, 2, 1, 2);
            setOpaque(true);
            setBorder(noFocusBorder);
        }
        
        public Component getTableCellRendererComponent(JTable table,  Object value,  boolean isSelected,  boolean hasFocus,  int row,  int col) {
            foreground = Color.BLACK;//Constants.orderForeground;
            //background = new Color(200,230,230);//Color.blue;//Constants.orderBackground;
            if(actualColumn < 0 || actualColumn + 1 == nvariables){
                background = Color.white;
            }else{
                if(model.getValueAt(row, actualColumn) != BinaryPartitionSelect.Gr0){// ==1 && col == 1){
                    background = new Color(100,200,100);//Color.white;
                }else{
                    background = new Color(200,100,100);
                }
            }
            this.setHorizontalAlignment(CENTER);

            setForeground(foreground);
            setBackground(background);
            setText((value == null) ? "" : value.toString());
            return this;
        }

        /*
        @Override
        public Component getTableCellRendererComponent(JTable table, Object values, boolean isSelected, boolean hasFocus, int row, int column) {
            throw new UnsupportedOperationException("Not supported yet.");
        }*/
    }
}

