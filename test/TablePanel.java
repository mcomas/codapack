package  coda.gui;

import coda.io.ExcelAdapter;
import coda.DataFrame;
import coda.Element;
import coda.Numeric;
import coda.Zero;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


public final class TablePanel extends JPanel{
  public static final long serialVersionUID = 1L;
  final int maxfil= 2003, maxcol= 1000;
  int nColumnas= 2;
  
  JTable table;
  JScrollPane scrollPane1 = new JScrollPane();
  DataFrame dataFrame;

  public TablePanel(){
    setLayout(new BorderLayout());
    table = new JTable(250,1000);

    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    table.setColumnSelectionAllowed(true);
    table.setRowSelectionAllowed(false);
    table.getTableHeader().setReorderingAllowed(false);
    //table.setShowGrid(true);
    table.setShowVerticalLines(true);    
    JTable rowTable = new RowNumberTable(table);

    add(scrollPane1, BorderLayout.CENTER);
    scrollPane1.getViewport().add(table, null);
    scrollPane1.setRowHeaderView(rowTable);
    scrollPane1.setCorner(JScrollPane.UPPER_LEFT_CORNER,
    rowTable.getTableHeader());

    int vColIndex = 0;
    for(vColIndex=0;vColIndex<250;vColIndex++){
        TableColumn col = table.getColumnModel().getColumn(vColIndex);
        int width = 60;
        col.setPreferredWidth(width);
    }

  }
  public void setDataFrame(DataFrame df){
    ExcelAdapter ed = new ExcelAdapter(table, df);
    
    table.setModel(new DataTableModel(df));
    table.setDefaultRenderer(Object.class, new DataRenderer(df) );

  }
  public void clearData(){
      table.setModel(new DefaultTableModel());
  }
  private static class DataRenderer extends DefaultTableCellRenderer{
        public static final long serialVersionUID = 1L;
        DataFrame dataFrame;
		public DataRenderer(DataFrame df){ dataFrame = df; }

        @Override
		public Component getTableCellRendererComponent(JTable table, 
                Object value, boolean isSelected, boolean hasFocus, 
                int row, int column){
            
            String name = dataFrame.get(column).getName();
            if( dataFrame.get(name).isNumeric()){
                this.setHorizontalAlignment(RIGHT);
                this.setFont(new Font ("Monospace", Font.PLAIN, 12));
                setForeground(Color.black);
            }else{
                this.setHorizontalAlignment(LEFT);
                this.setFont(new Font ("Monospace", Font.PLAIN, 12));
                //setForeground(Color.gray);
                setForeground(Color.black);
            }
            setBackground(isSelected ? CoDaPackConf.getOutputColor() :  Color.white);
            
            setText((value == null) ? "" : value.toString());
			return this;
		}
	}

  public class DataTableModel extends AbstractTableModel{
    public static final long serialVersionUID = 1L;
    
    private int MAX_COLUMNS = 1000;
    private int MAX_ROWS = 5000;
    DataFrame dataFrame;


    public DataTableModel(DataFrame df){
        dataFrame = df;
    }
    
    public int getRowCount() {
        return dataFrame.getMaxVariableLength();
    }

    @Override
    public String getColumnName(int i){
        //AbstractTableModel.class.getColumnName(i);
        return dataFrame.get(i).getName();
    }
    
    public int getColumnCount() {
        //System.out.println(dataFrame.varnames.size());
        return dataFrame.size();
    }
    
    public Object getValueAt(int arg0, int arg1) {
        String name = dataFrame.get(arg1).getName();
        if( arg0 < dataFrame.get(name).size()){
            if( dataFrame.get(name).isText() ){
                return (String)dataFrame.get(name).get(arg0).getValue();
            }else{
                if(dataFrame.get(name).get(arg0) instanceof Zero){
                    Zero zero = (Zero)dataFrame.get(name).get(arg0);
                    return "<[" + CoDaPackConf.getDecimalTableFormat().format(zero.detection) + "]";
                }
                Numeric value = (Numeric)dataFrame.get(name).get(arg0);
                if(Double.isNaN(value.getValue()))
                    return "NA";
                return CoDaPackConf.getDecimalTableFormat().format(value.getValue());
            }
        }else{
            return "";
        }
    }
  }
}