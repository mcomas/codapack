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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.utils;

import coda.gui.menu.AbstractMenuDialogWithILR;

import coda.gui.menu.AbstractMenuGeneral;
import coda.gui.menu.ILRMenu;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
/**
 *
 * @author mcomas
 */
public class BinaryPartitionSelect extends JDialog{
    public static final long serialVersionUID = 1L;
    String variables[];

    int order[];
    int nvariables;
    int partition[][];
    boolean activeVars[];

    static public String Gr0 = " ";
    static public String Gr1 = "-";
    static public String Gr2 = "+";

    static public int VGr0 = 0;
    static public int VGr1 = 1;
    static public int VGr2 = 2;

    BinaryPartitionTable partitionTable = null;
    BinaryPartitionRowHeaders rowsTable = null;
    
    JButton previous;
    JButton next;
    
    AbstractMenuDialogWithILR rootMenu = null;
    ILRMenu rootILRMenu = null;
    AbstractMenuGeneral rootGeneralMenu = null;
    
    public BinaryPartitionSelect(AbstractMenuDialogWithILR dialogRoot, String vars[]){
        super(dialogRoot, "Binary Partition Menu");

        Point p = dialogRoot.getLocation();
        p.x = p.x + (dialogRoot.getWidth()-20)/2;
        p.y = p.y + (dialogRoot.getHeight()-60)/2;
        setLocation(p);

        rootMenu = dialogRoot;
        variables = vars;
        nvariables = variables.length;
        partition = new int[nvariables-1][nvariables];
        order = new int[nvariables];
        activeVars = new boolean[nvariables];
        for(int i=0;i<nvariables;i++){
            activeVars[i] = true;
            order[i] = i;
        }
        //int height = partitionTable.getRowHeight();
        //int width = partitionTable.getWidth();

        int heightRow = 20;
        int widthCol = 40;

        setSize( widthCol *(nvariables-1)+158+5,heightRow*nvariables+100);
        getContentPane().setLayout(new BorderLayout());

        

        partitionTable = new BinaryPartitionTable(rowsTable, order, activeVars, nvariables);
        rowsTable = new BinaryPartitionRowHeaders(partitionTable, order, variables, heightRow);        

        partitionTable.setRowHeight(heightRow);
        for(int vColIndex=0;vColIndex<nvariables-1;vColIndex++){
            partitionTable.getColumnModel().
                    getColumn(vColIndex).setPreferredWidth(widthCol);
        }
        partitionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        partitionTable.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(partitionTable);
        scrollPane.setRowHeaderView(rowsTable);
        scrollPane.setCorner(
                JScrollPane.UPPER_LEFT_CORNER, rowsTable.getTableHeader());
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Defining control buttons in South Area
        JPanel south = new JPanel();
        south.setLayout(new GridLayout(1,2));
        JPanel panel1 = new JPanel();

        previous = new JButton("Previous");
        previous.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });
        next = new JButton("Next");
        next.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        
        panel1.add(previous);
        panel1.add(next);



        south.add(panel1);
        getContentPane().add(south, BorderLayout.SOUTH);
    }
    
    public BinaryPartitionSelect(ILRMenu dialogRoot, String vars[]){
        super(dialogRoot, "Binary Partition Menu");

        Point p = dialogRoot.getLocation();
        p.x = p.x + (dialogRoot.getWidth()-20)/2;
        p.y = p.y + (dialogRoot.getHeight()-60)/2;
        setLocation(p);

        this.rootILRMenu = dialogRoot;
        variables = vars;
        nvariables = variables.length;
        partition = new int[nvariables-1][nvariables];
        order = new int[nvariables];
        activeVars = new boolean[nvariables];
        for(int i=0;i<nvariables;i++){
            activeVars[i] = true;
            order[i] = i;
        }
        //int height = partitionTable.getRowHeight();
        //int width = partitionTable.getWidth();

        int heightRow = 20;
        int widthCol = 40;

        setSize( widthCol *(nvariables-1)+158+5,heightRow*nvariables+100);
        getContentPane().setLayout(new BorderLayout());

        

        partitionTable = new BinaryPartitionTable(rowsTable, order, activeVars, nvariables);
        rowsTable = new BinaryPartitionRowHeaders(partitionTable, order, variables, heightRow);        

        partitionTable.setRowHeight(heightRow);
        for(int vColIndex=0;vColIndex<nvariables-1;vColIndex++){
            partitionTable.getColumnModel().
                    getColumn(vColIndex).setPreferredWidth(widthCol);
        }
        partitionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        partitionTable.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(partitionTable);
        scrollPane.setRowHeaderView(rowsTable);
        scrollPane.setCorner(
                JScrollPane.UPPER_LEFT_CORNER, rowsTable.getTableHeader());
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Defining control buttons in South Area
        JPanel south = new JPanel();
        south.setLayout(new GridLayout(1,2));
        JPanel panel1 = new JPanel();

        previous = new JButton("Previous");
        previous.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });
        next = new JButton("Next");
        next.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        
        panel1.add(previous);
        panel1.add(next);



        south.add(panel1);
        getContentPane().add(south, BorderLayout.SOUTH);
    }
    
    public BinaryPartitionSelect(AbstractMenuGeneral dialogRoot, String vars[]){
        super(dialogRoot, "Binary Partition Menu");

        Point p = dialogRoot.getLocation();
        p.x = p.x + (dialogRoot.getWidth()-20)/2;
        p.y = p.y + (dialogRoot.getHeight()-60)/2;
        setLocation(p);

        this.rootGeneralMenu = dialogRoot;
        variables = vars;
        nvariables = variables.length;
        partition = new int[nvariables-1][nvariables];
        order = new int[nvariables];
        activeVars = new boolean[nvariables];
        for(int i=0;i<nvariables;i++){
            activeVars[i] = true;
            order[i] = i;
        }
        //int height = partitionTable.getRowHeight();
        //int width = partitionTable.getWidth();

        int heightRow = 20;
        int widthCol = 40;

        setSize( widthCol *(nvariables-1)+158+5,heightRow*nvariables+100);
        getContentPane().setLayout(new BorderLayout());

        

        partitionTable = new BinaryPartitionTable(rowsTable, order, activeVars, nvariables);
        rowsTable = new BinaryPartitionRowHeaders(partitionTable, order, variables, heightRow);        

        partitionTable.setRowHeight(heightRow);
        for(int vColIndex=0;vColIndex<nvariables-1;vColIndex++){
            partitionTable.getColumnModel().
                    getColumn(vColIndex).setPreferredWidth(widthCol);
        }
        partitionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        partitionTable.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(partitionTable);
        scrollPane.setRowHeaderView(rowsTable);
        scrollPane.setCorner(
                JScrollPane.UPPER_LEFT_CORNER, rowsTable.getTableHeader());
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Defining control buttons in South Area
        JPanel south = new JPanel();
        south.setLayout(new GridLayout(1,2));
        JPanel panel1 = new JPanel();

        previous = new JButton("Previous");
        previous.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });
        next = new JButton("Next");
        next.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        
        panel1.add(previous);
        panel1.add(next);



        south.add(panel1);
        getContentPane().add(south, BorderLayout.SOUTH);
    }

    void previousButtonActionPerformed(ActionEvent evt){
        int actualStep = partitionTable.getActualStep();
        if(actualStep +1 == nvariables){
            next.setText("Next");
        }
        if(actualStep > 1){
            int actualCol = actualStep - 1;

            // Getting the values from the JTable
            for(int row=0;row<nvariables;row++){
                partition[actualCol-1][row] = VGr0;
            }
            // Calculating the ordenation
            for(int i=0;i<nvariables;i++) order[i] = i;
            for(int i=0;i<nvariables;i++){
                for(int j=i+1; j <nvariables;j++){
                    boolean sameGroup = true;
                    for(int k=0; k<actualCol; k++){
                        if(partition[k][order[i]] != partition[k][order[j]]){
                            if( partition[k][order[i]] < partition[k][order[j]] ){
                                int temp = order[i];
                                order[i] = order[j];
                                order[j] = temp;
                            }
                            sameGroup = false;
                            break;
                        }
                    }
                    if(sameGroup){
                        if(order[i] > order[j]){
                            int temp = order[i];
                            order[i] = order[j];
                            order[j] = temp;
                        }
                    }
                }
            }
            int count = 0;
            activeVars[order[0]] = true;
            for(int i=1; i<nvariables;i++){
                boolean equal = true;
                for(int k=actualCol-1; k>=0 && equal; k--)
                    if( partition[k][order[i]] != partition[k][order[i-1]] )
                        equal = false;
                // If variables are in the same group the program continue
                if( equal ){
                    activeVars[order[i]] = true;
                    count++;
                }else{
                    if(count > 0){
                        for(;i<nvariables;i++) activeVars[order[i]] = false;
                    }else{
                        activeVars[order[i-1]] = false;
                        activeVars[order[i]] = true;
                    }
                }
            }

            partitionTable.setPreviousStep(partition);
            partitionTable.updateUI();
        }
    }
    void nextButtonActionPerformed(ActionEvent evt){
        int actualStep = partitionTable.getActualStep();
        int actualCol = actualStep - 1;

        // Checking if its the last step
        if(next.getText().compareTo("Accept") == 0){

            // Modifying 2 to 1 and 1 to -1
            for(int i=0;i+1<nvariables;i++)
                for(int j=0;j<nvariables;j++)
                    partition[i][j] = (partition[i][j] == 1 ?
                        -1 : (partition[i][j] == 2 ?
                            1 : 0) );
            // Sending a copy of the partition matrix to the Menu and closing
            // the frame.
            if(rootMenu != null)rootMenu.setPartition(partition);
            if(rootILRMenu != null) rootILRMenu.setPartition(partition);
            //if(rootGeneralMenu != null) rootGeneralMenu.setPartition(partition);
            setVisible(false);
            dispose();
            return;
        }
        // Getting the values from the JTable
        TableModel model = partitionTable.getModel();
        for(int row=0;row<nvariables;row++){
            String value = (String)model.getValueAt(row, actualCol);
            if(value == null || value.compareTo(BinaryPartitionSelect.Gr0) == 0){
                partition[actualCol][order[row]] = VGr0;
            }else if(value.compareTo(BinaryPartitionSelect.Gr1) == 0){
                partition[actualCol][order[row]] = VGr1;
            }else if(value.compareTo(BinaryPartitionSelect.Gr2) == 0){
                partition[actualCol][order[row]] = VGr2;
            }
        }
        // Checking if a partition has been defined from the values we've got.
        boolean equal = true;
        int val = VGr0;
        for(int i=0;i<nvariables && equal;i++)
            if(activeVars[order[i]])
                if(val == VGr0) val = partition[actualCol][order[i]];
                else if( val != partition[actualCol][order[i]]){
                    equal = false;
                }
        if(equal){
            JOptionPane.showMessageDialog(this, "A partition must be defined.");
            return;
        }
        // From this point we assume the user need to define another
        // subpartition
        for(int i=0;i<nvariables;i++) order[i] = i;
        for(int i=0;i<nvariables;i++){
            for(int j=i+1; j <nvariables;j++){
                boolean sameGroup = true;
                for(int k=0; k<=actualCol; k++){
                    if(partition[k][order[i]] != partition[k][order[j]]){
                        if( partition[k][order[i]] < partition[k][order[j]] ){
                            int temp = order[i];
                            order[i] = order[j];
                            order[j] = temp;
                        }
                        sameGroup = false;
                        break;
                    }
                }
                if(sameGroup){
                    if(order[i] > order[j]){
                        int temp = order[i];
                        order[i] = order[j];
                        order[j] = temp;
                    }
                }
            }
        }
        int count = 0;
        activeVars[order[0]] = true;
        for(int i=1; i<nvariables;i++){
            equal = true;
            for(int k=actualCol; k>=0 && equal; k--)
                if( partition[k][order[i]] != partition[k][order[i-1]] )
                    equal = false;
            // If variables are in the same group the program continue
            if( equal ){
                activeVars[order[i]] = true;
                count++;
            }else{
                if(count > 0){
                    for(;i<nvariables;i++) activeVars[order[i]] = false;
                }else{
                    activeVars[order[i-1]] = false;
                    activeVars[order[i]] = true;
                }
            }
        }
        for(int row=0;row<nvariables;row++){
            for(int col=0;col<nvariables-1;col++){
                if(activeVars[row]){
                    if(partition[col][order[row]] == VGr0){
                        model.setValueAt(BinaryPartitionSelect.Gr0, row, col);
                    }
                    if(partition[col][order[row]] == VGr1){
                        model.setValueAt(BinaryPartitionSelect.Gr1, row, col);
                    }
                    if(partition[col][order[row]] == VGr2){
                        model.setValueAt(BinaryPartitionSelect.Gr2, row, col);
                    }
                }else{
                    model.setValueAt(BinaryPartitionSelect.Gr0, row, col);
                }
            }
        }
        partitionTable.setNextStep(partition);
        partitionTable.updateUI();
        partitionTable.setDefaultRenderer(Object.class, partitionTable.cellRend );

        if(actualStep + 1 == nvariables){
            next.setText("Accept");
        }
    }
}
