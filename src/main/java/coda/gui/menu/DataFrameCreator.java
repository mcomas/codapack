/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.Variable;
import coda.gui.CoDaPackMain;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.StringTokenizer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.codehaus.plexus.util.StringUtils;

/**
 *
 * @author Guest2
 */
public class DataFrameCreator extends JFrame{
    
    JTable table;
    JPanel panel;
    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    
    public DataFrameCreator(final CoDaPackMain mainApp){
                panel = new JPanel();
                panel.add(new JLabel("Use the first row to set the variable names"));
                panel.add(new JLabel(""));
                panel.setLayout(new GridLayout(0,2,2,2));
                JTextField columnsField, rowsField;
                columnsField = new JTextField(20);
                rowsField = new JTextField(20);
                panel.add(new JLabel("Put the number of columns: "));
                panel.add(columnsField);
                panel.add(new JLabel("Put the number of rows: "));
                panel.add(rowsField);
                
        int answer = JOptionPane.showConfirmDialog(this, panel, "Create new dataframe", JOptionPane.OK_CANCEL_OPTION);
        
        if(answer == JOptionPane.OK_OPTION){
            int numberOfColumns = Integer.valueOf(columnsField.getText());
            int numberOfRows = Integer.valueOf(rowsField.getText());
            this.setTitle("Create new dataFrame");
            panel = new JPanel();
            this.setSize(800,650);
            panel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10,10,10,10);
            table = new JTable(numberOfRows,numberOfColumns);
            table.setRowHeight(30);
            ExcelAdapter ex = new ExcelAdapter(table);
            table = ex.getJTable();
            table.setSize(panel.getWidth(),panel.getHeight());
            panel.add(table,c);
            JButton acceptButton = new JButton("Accept");
            southPanel.add(acceptButton);
            acceptButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    createANewDataFrame(mainApp);
                }
            });
            JButton cancelButton = new JButton("Cancel");
            southPanel.add(cancelButton);
            cancelButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dispose();
                }
            });

            this.getContentPane().add( new JScrollPane(panel));
            this.getContentPane().add(southPanel,BorderLayout.SOUTH);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
            this.setVisible(true);
        }
    }
    
    private void createANewDataFrame(CoDaPackMain mainApp){
        
        String[] varNames = new String[table.getColumnCount()]; // names data
        
        DataFrame df = new DataFrame();
        
        for (int count = 0; count < table.getColumnCount(); count++){
            double[] auxNum = new double[table.getRowCount()-1];
            String[] auxCat = new String[table.getRowCount()-1];
            boolean isNumeric = false;
            boolean isCategoric = false;
            
            for(int j = 0; j < table.getRowCount(); j++){
                if(j == 0){
                    varNames[count] = table.getValueAt(j,count).toString();
                }
                else if(StringUtils.isNumeric(table.getValueAt(j,count).toString())){
                    isNumeric = true;
                    auxNum[j-1] = Double.valueOf(table.getValueAt(j, count).toString());
                }
                else{
                    isCategoric = true;
                    auxCat[j-1] = table.getValueAt(j,count).toString();
                }
            }
            if(isNumeric) df.addData(varNames[count], auxNum);
            else if(isCategoric) df.addData(varNames[count], new Variable(varNames[count],auxCat));
        }
        
        df.setName("prova");
        
        mainApp.addDataFrame(df);
    }
    
}


class ExcelAdapter implements ActionListener{
   private String rowstring,value;
   private Clipboard system;
   private StringSelection stsel;
   private JTable jTable1 ;
   
   /**
    * The Excel Adapter is constructed with a
    * JTable on which it enables Copy-Paste and acts
    * as a Clipboard listener.
    */
public ExcelAdapter(JTable myJTable){
    jTable1 = myJTable;
    KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK,false);
    // Identifying the copy KeyStroke user can modify this
    // to copy on some other Key combination.
    KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK,false);
    // Identifying the Paste KeyStroke user can modify this
    //to copy on some other Key combination.
    jTable1.registerKeyboardAction(this,"Copy",copy,JComponent.WHEN_FOCUSED);
    jTable1.registerKeyboardAction(this,"Paste",paste,JComponent.WHEN_FOCUSED);
    system = Toolkit.getDefaultToolkit().getSystemClipboard();
}
   /**
    * Public Accessor methods for the Table on which this adapter acts.
    */
public JTable getJTable() {return jTable1;}

public void setJTable(JTable jTable1) {this.jTable1=jTable1;}

   /**
    * This method is activated on the Keystrokes we are listening to
    * in this implementation. Here it listens for Copy and Paste ActionCommands.
    * Selections comprising non-adjacent cells result in invalid selection and
    * then copy action cannot be performed.
    * Paste is done by aligning the upper left corner of the selection with the
    * 1st element in the current selection of the JTable.
    */
public void actionPerformed(ActionEvent e){
    
      if (e.getActionCommand().compareTo("Copy")==0){
          
         StringBuffer sbf=new StringBuffer();
         // Check to ensure we have selected only a contiguous block of
         // cells
         int numcols=jTable1.getSelectedColumnCount();
         int numrows=jTable1.getSelectedRowCount();
         int[] rowsselected=jTable1.getSelectedRows();
         int[] colsselected=jTable1.getSelectedColumns();
         if (!((numrows-1==rowsselected[rowsselected.length-1]-rowsselected[0] && numrows==rowsselected.length) && (numcols-1==colsselected[colsselected.length-1]-colsselected[0] && numcols==colsselected.length))){
            JOptionPane.showMessageDialog(null, "Invalid Copy Selection","Invalid Copy Selection",JOptionPane.ERROR_MESSAGE);
            return;
         }
         
         for (int i=0;i<numrows;i++){
            for (int j=0;j<numcols;j++){
                sbf.append(jTable1.getValueAt(rowsselected[i],colsselected[j]));
                if (j<numcols-1) sbf.append("\t");
            }
            sbf.append("\n");
         }
         
         stsel  = new StringSelection(sbf.toString());
         system = Toolkit.getDefaultToolkit().getSystemClipboard();
         system.setContents(stsel,stsel);
      }
      
      if (e.getActionCommand().compareTo("Paste")==0){
          
          int startRow=(jTable1.getSelectedRows())[0];
          int startCol=(jTable1.getSelectedColumns())[0];
          try{
              
             String trstring= (String)(system.getContents(this).getTransferData(DataFlavor.stringFlavor));
             StringTokenizer st1=new StringTokenizer(trstring,"\n");
             
             for(int i=0;st1.hasMoreTokens();i++){
                 
                rowstring=st1.nextToken();
                StringTokenizer st2=new StringTokenizer(rowstring,"\t");
                for(int j=0;st2.hasMoreTokens();j++){
                   value=(String)st2.nextToken();
                   if (startRow+i< jTable1.getRowCount()  && startCol+j< jTable1.getColumnCount()) jTable1.setValueAt(value,startRow+i,startCol+j);
               }
            }
         }
         catch(Exception ex){ex.printStackTrace();}
      }
   }
}