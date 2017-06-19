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
import coda.Element;
import coda.Numeric;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.datatransfer.*;
import java.util.StringTokenizer;
/**
 * ExcelAdapter enables Copy-Paste Clipboard functionality on JTables.
 * The clipboard data format used by the adapter is compatible with
 * the clipboard format used by Excel. This provides for clipboard
 * interoperability between enabled JTables and Excel.
 */
public class ExcelAdapter implements ActionListener{
   private String rowstring, value;
   private Clipboard system;
   private StringSelection stsel;
   private JTable jTable1 ;
   private DataFrame dataFrame;
   /**
    * The Excel Adapter is constructed with a
    * JTable on which it enables Copy-Paste and acts
    * as a Clipboard listener.
    * @param myJTable 
    * @param df
    */
    public ExcelAdapter(JTable myJTable, DataFrame df){
        jTable1 = myJTable;
        dataFrame = df;
        KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK,false);
        KeyStroke copy_mac = KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.META_MASK,false);
        // Identifying the copy KeyStroke user can modify this
        // to copy on some other Key combination.
        KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK,false);
        // Identifying the Paste KeyStroke user can modify this
        //to copy on some other Key combination.
        jTable1.registerKeyboardAction(this,"Copy",copy,JComponent.WHEN_FOCUSED);
        jTable1.registerKeyboardAction(this,"Copy_Mac",copy_mac,JComponent.WHEN_FOCUSED);
        jTable1.registerKeyboardAction(this,"Paste",paste,JComponent.WHEN_FOCUSED);
        system = Toolkit.getDefaultToolkit().getSystemClipboard();
    }
    /**
    * Public Accessor methods for the Table on which this adapter acts.
     * @return
     */
    public JTable getJTable() {return jTable1;}
    /**
     *
     * @param jTable1
     */
    public void setJTable(JTable jTable1) {this.jTable1=jTable1;}
    /**
    * This method is activated on the Keystrokes we are listening to
    * in this implementation. Here it listens for Copy and Paste ActionCommands.
    * Selections comprising non-adjacent cells result in invalid selection and
    * then copy action cannot be performed.
    * Paste is done by aligning the upper left corner of the selection with the
    * 1st element in the current selection of the JTable.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e){
        //System.out.println(e.getActionCommand());
        if (e.getActionCommand().compareTo("Copy")==0 ||
                e.getActionCommand().compareTo("Copy_Mac")==0){
            StringBuilder sbf =new StringBuilder();
            // Check to ensure we have selected only a contiguous block of
            // cells
            int numcols=jTable1.getSelectedColumnCount();
            int nrows = jTable1.getRowCount();
            int numrows=jTable1.getSelectedRowCount();
            int[] rowsselected=jTable1.getSelectedRows();
            int[] colsselected=jTable1.getSelectedColumns();
            /*
            if (!((numrows-1==rowsselected[rowsselected.length-1]-rowsselected[0] &&
                numrows==rowsselected.length) &&
            (numcols-1==colsselected[colsselected.length-1]-colsselected[0] &&
                numcols==colsselected.length))){
                JOptionPane.showMessageDialog(this, "Invalid Copy Selection",
                                              "Invalid Copy Selection",
                                              JOptionPane.ERROR_MESSAGE);
                return;
            }*/
            String names[] = new String[numcols];
            for (int j=0;j<numcols;j++){
                names[j] = jTable1.getColumnName(colsselected[j]);
                sbf.append(names[j]);
                if (j<numcols-1) sbf.append("\t");
            }
            sbf.append("\n");

            for (int i=0;i<nrows;i++){
                for (int j=0;j<numcols;j++){
                    //sbf.append(jTable1.getValueAt(i,colsselected[j]));
                    Element el = dataFrame.get(names[j]).get(i);
                    if(el != null){
                        if(el instanceof Numeric){
                            Numeric elnum = (Numeric)el;
                            sbf.append(Double.toString(elnum.getValue()));
                        }else
                            sbf.append(dataFrame.get(names[j]).get(i).toString());
                    }else{
                        sbf.append("");
                    }
                    if (j<numcols-1) sbf.append("\t");
                }
                sbf.append("\n");
            }
            stsel  = new StringSelection(sbf.toString());
            system = Toolkit.getDefaultToolkit().getSystemClipboard();
            system.setContents(stsel, stsel);
        }
        /*
        if (e.getActionCommand().compareTo("Paste")==0){
            //System.out.println("Trying to Paste");
            int startRow=(jTable1.getSelectedRows())[0];
            int startCol=(jTable1.getSelectedColumns())[0];
            try{
                String trstring= (String)(system.getContents(this).getTransferData(DataFlavor.stringFlavor));
                //System.out.println("String is:"+trstring);
                StringTokenizer st1=new StringTokenizer(trstring,"\n");
                for(int i=0;st1.hasMoreTokens();i++){
                    rowstring=st1.nextToken();
                    StringTokenizer st2=new StringTokenizer(rowstring,"\t");
                    for(int j=0;st2.hasMoreTokens();j++){
                        value = st2.nextToken();
                        if (startRow+i< jTable1.getRowCount()  &&
                           startCol+j< jTable1.getColumnCount())
                            jTable1.setValueAt(value,startRow+i,startCol+j);
                        //System.out.println("Putting "+ value+"atrow="+startRow+i+"column="+startCol+j);
                    }
                }
            }catch(Exception ex){ex.printStackTrace();}
        }
         */
    }
}
