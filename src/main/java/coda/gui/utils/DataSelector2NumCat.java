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

/*
 * DataSelector.java
 *
 * Created on 23/09/2010, 10:25:14
 */

package coda.gui.utils;

import coda.DataFrame;
import coda.Variable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListModel;
/**
 *
 * @author mcomas
 */
public final class DataSelector2NumCat extends JPanel {
    DataFrame df_instance;
    public static final long serialVersionUID = 1L;
    int pressIndex = -1;
    int releaseIndex = -1;
    String variables[];
    boolean groupedBy = true;
    
    public DataSelector2NumCat(DataFrame dataFrame){
        initComponents();
        df_instance = dataFrame;
        if(dataFrame != null)
            this.setDataLists(dataFrame, null);
    }
    public DataSelector2NumCat(DataFrame dataFrame, boolean groups, boolean all) {
        initComponents();
        groupsComboBox.setVisible(groups);
        labGroups.setVisible(groups);
        df_instance = dataFrame;
        if (all == true){
            if(dataFrame != null)
                this.setAllDataLists(dataFrame, null);
        }else{
            if(dataFrame != null)
                this.setDataLists(dataFrame, null);
        }
    }
    public DataSelector2NumCat(DataFrame dataFrame, int[]selected, boolean groups) {
        initComponents();
        groupsComboBox.setVisible(groups);
        labGroups.setVisible(groups);
        df_instance = dataFrame;
        //jList2.setModel(new DefaultListModel());
        if(dataFrame != null)
            setDataLists(dataFrame, selected);

    }
    public DataSelector2NumCat(DataFrame dataFrame, boolean groups) {
        initComponents();
        groupsComboBox.setVisible(groups);
        labGroups.setVisible(groups);
        df_instance = dataFrame;
        //jList2.setModel(new DefaultListModel());
        //jList2.setModel(new DefaultListModel());
        if(dataFrame != null)
            setDataLists(dataFrame, null);
    }

    public DataSelector2NumCat(DataFrame activeDataFrame, int[] selectedData, String categoric) {
        initComponents();
        groupsComboBox.setVisible(false);
        labGroups.setVisible(false);
        df_instance = activeDataFrame;
        if(activeDataFrame != null) setDataListsCat(activeDataFrame,null);
    }
    private void reorder1() {
        DefaultListModel model = (DefaultListModel) selectedList1.getModel();
        Object dragee = model.elementAt(pressIndex);
        model.removeElementAt(pressIndex);
        model.insertElementAt(dragee, releaseIndex);
    }
    private void reorder2() {
        DefaultListModel model = (DefaultListModel) selectedList2.getModel();
        Object dragee = model.elementAt(pressIndex);
        model.removeElementAt(pressIndex);
        model.insertElementAt(dragee, releaseIndex);
    }
    public void setAllDataLists(DataFrame dataFrame, int[] selected){
        DefaultListModel model1 = new DefaultListModel();
        DefaultListModel model2 = new DefaultListModel();
        DefaultListModel model3 = new DefaultListModel();
        variables = new String[dataFrame.size()];
        //Iterator it = dataFrame.getNamesIterator();
        int i = -1;
        for(String name : dataFrame.getNames()){
            variables[++i] = name;
            
            boolean isSelected = false;
            if(selected != null)
                for(int j=0;j<selected.length;j++)
                if(i == selected[j]){
                    isSelected = true;
                    break;
                }
            if(!isSelected)
                model1.addElement(variables[i]);
            else
                model2.addElement(variables[i]);
            
        }
        toSelectList.setModel(model1);
        selectedList1.setModel(model2);
        selectedList2.setModel(model3);
    }
    public void setDataLists(DataFrame dataFrame, int[] selected){
        DefaultListModel model1 = new DefaultListModel();
        DefaultListModel model2 = new DefaultListModel();
        DefaultListModel model3 = new DefaultListModel();
        variables = new String[dataFrame.size()];
        //Iterator it = dataFrame.getNamesIterator();
        int i = -1;
        for(String name : dataFrame.getNames()){
            variables[++i] = name;
            if(((Variable)dataFrame.get(variables[i])).isText())
                groupsComboBox.addItem(variables[i]);
            else{
                boolean isSelected = false;
                if(selected != null)
                    for(int j=0;j<selected.length;j++)
                    if(i == selected[j]){
                        isSelected = true;
                        break;
                    }
                if(!isSelected)
                    model1.addElement(variables[i]);
                else
                    model2.addElement(variables[i]);
            }
        }
        toSelectList.setModel(model1);
        selectedList1.setModel(model2);
        selectedList2.setModel(model3);
    }
    
    public void setDataListsCat(DataFrame dataFrame, int[] selected){
        DefaultListModel model1 = new DefaultListModel();
        DefaultListModel model2 = new DefaultListModel();
        DefaultListModel model3 = new DefaultListModel();
        variables = new String[dataFrame.size()];
        //Iterator it = dataFrame.getNamesIterator();
        int i = -1;
        for(String name : dataFrame.getNames()){
            variables[++i] = name;
            if(((Variable)dataFrame.get(variables[i])).isNumeric())
                groupsComboBox.addItem(variables[i]);
            else{
                boolean isSelected = false;
                if(selected != null)
                    for(int j=0;j<selected.length;j++)
                    if(i == selected[j]){
                        isSelected = true;
                        break;
                    }
                if(!isSelected)
                    model1.addElement(variables[i]);
                else
                    model2.addElement(variables[i]);
            }
        }
        toSelectList.setModel(model1);
        selectedList1.setModel(model2);
        selectedList2.setModel(model3);
    }
    
    public ListModel getAvailableData(){
        return toSelectList.getModel();
    }
    public void setSelectedData1(ListModel list){
        selectedList1.setModel(list);
    }
    public void setSelectedData2(ListModel list){
        selectedList2.setModel(list);
    }
    public String getGroupData(){
        if(groupsComboBox.getSelectedIndex() == 0) return null;
        else return (String) groupsComboBox.getSelectedItem();
    }
    public String getSelectedGroup(){
        if(groupsComboBox.getSelectedIndex() == 0) return null;

        return (String)groupsComboBox.getSelectedItem();
    }
    public String[] getSelectedData1(){
        //int selected[] = jList2.getModel();//getSelectedIndices();
        DefaultListModel model = (DefaultListModel)selectedList1.getModel();
        String[] res = new String[model.size()];
        for(int i=0,m=model.size(); i<m; i++)
            res[i] = (String)model.getElementAt(i);

        return res;
    }
    public String[] getSelectedData2(){
        //int selected[] = jList2.getModel();//getSelectedIndices();
        DefaultListModel model = (DefaultListModel)selectedList2.getModel();
        String[] res = new String[model.size()];
        for(int i=0,m=model.size(); i<m; i++)
            res[i] = (String)model.getElementAt(i);

        return res;
    }
    @SuppressWarnings("unchecked")
    private void initComponents() {

        labAvailable = new javax.swing.JLabel();
        labSelected1 = new javax.swing.JLabel();
        toSelectScrollPane = new javax.swing.JScrollPane();
        toSelectList = new javax.swing.JList();
        selectedScrollPane1 = new javax.swing.JScrollPane();
        selectedScrollPane2 = new javax.swing.JScrollPane();
        selectedList1 = new javax.swing.JList();
        insertButton1 = new javax.swing.JButton();
        removeButton1 = new javax.swing.JButton();
        resetButton1 = new javax.swing.JButton();
        labSelected2 = new javax.swing.JLabel();
        selectedList2 = new javax.swing.JList();
        insertButton2 = new javax.swing.JButton();
        removeButton2 = new javax.swing.JButton();
        resetButton2 = new javax.swing.JButton();
        groupsComboBox = new javax.swing.JComboBox();
        labGroups = new javax.swing.JLabel();

        setBorder(BorderFactory.createTitledBorder("Selected"));

        setSize(new Dimension(360, 350));
        setMaximumSize(new Dimension(360, 350));
        setPreferredSize(new Dimension(360, 350));

        labAvailable.setText("Available data:");

        labSelected1.setText("Selected data 1:");
        labSelected2.setText("Selected data 2:");

        toSelectScrollPane.setPreferredSize(new Dimension(120, 280));
        toSelectScrollPane.setSize(new Dimension(120, 280));

        toSelectList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        toSelectScrollPane.setViewportView(toSelectList);

        selectedScrollPane1.setPreferredSize(new java.awt.Dimension(120, 125));
        selectedScrollPane1.setSize(new java.awt.Dimension(120, 125));
        selectedScrollPane2.setPreferredSize(new java.awt.Dimension(120, 125));
        selectedScrollPane2.setSize(new java.awt.Dimension(120, 125));

        selectedList1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked1(evt);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jList2MousePressed1(evt);
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jList2MouseReleased1(evt);
            }
        });
        
        selectedList2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked2(evt);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jList2MousePressed2(evt);
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jList2MouseReleased2(evt);
            }
        });
        
        selectedList1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jList2MouseDragged1(evt);
            }
        });
        selectedList2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jList2MouseDragged2(evt);
            }
        });
        selectedScrollPane1.setViewportView(selectedList1);
        selectedScrollPane2.setViewportView(selectedList2);

        insertButton1.setText(">");
        insertButton1.setPreferredSize(new Dimension(50,30));
        insertButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertActionPerformed1(evt);
            }
        });
        insertButton2.setText(">");
        insertButton2.setPreferredSize(new Dimension(50,30));
        insertButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertActionPerformed2(evt);
            }
        });

        removeButton1.setText("<");
        removeButton1.setPreferredSize(new Dimension(50,30));
        removeButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed1(evt);
            }
        });
        
        removeButton2.setText("<");
        removeButton2.setPreferredSize(new Dimension(50,30));
        removeButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed2(evt);
            }
        });

        resetButton1.setText("Reset");
        resetButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });
        
        resetButton2.setText("Reset");
        resetButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed2(evt);
            }
        });
        
        groupsComboBox.setPreferredSize(new Dimension(120, 24));
        groupsComboBox.setModel(new DefaultComboBoxModel(new String[] { "<No groups>" }));

        labGroups.setText("Groups:");


        //this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setLayout(new BorderLayout());

        JPanel left = new JPanel();
        left.setPreferredSize(new Dimension(140,250));
        left.setSize(new Dimension(140,250));
        left.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));//new BoxLayout(left, BoxLayout.PAGE_AXIS));
        JPanel center = new JPanel();        

        center.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));        
        //center.setLayout(new FlowLayout(FlowLayout.CENTER));
        center.setPreferredSize(new Dimension(60,250));
        center.setSize(new Dimension(60,250));

        JPanel spacing = new JPanel();
        JPanel spacing2 = new JPanel();
        spacing.setPreferredSize(new Dimension(60,100));
        spacing2.setPreferredSize(new Dimension(60,50));
        
        JPanel right = new JPanel();
        right.setPreferredSize(new Dimension(140,250));
        right.setSize(new Dimension(140,250));
        right.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));//new BoxLayout(right, BoxLayout.PAGE_AXIS));


        left.add(labAvailable);
        left.add(toSelectScrollPane);

        //center.add(spacing);
        center.add(spacing2);
        center.add(insertButton1);
        center.add(removeButton1);

        right.add(labSelected1);
        right.add(selectedScrollPane1);
        right.add(labGroups);
        right.add(groupsComboBox);
        right.add(resetButton1);
        
        center.add(spacing);
        center.add(insertButton2);
        center.add(removeButton2);
        
        right.add(labSelected2);
        right.add(selectedScrollPane2);
        right.add(resetButton2);

        this.add(center,BorderLayout.CENTER);
        this.add(left,BorderLayout.WEST);
        this.add(right,BorderLayout.EAST);
        //this.add(left);
        //this.add(center);
        //this.add(right);
        
    }// </editor-fold>

    private void jList2MousePressed1(java.awt.event.MouseEvent evt) {
        pressIndex = selectedList1.locationToIndex(evt.getPoint());

    }
    
    private void jList2MousePressed2 (java.awt.event.MouseEvent evt) {
        pressIndex = selectedList2.locationToIndex(evt.getPoint());

    }

    private void jList2MouseReleased1(java.awt.event.MouseEvent evt) {
        releaseIndex = selectedList1.locationToIndex(evt.getPoint());
        if (releaseIndex != pressIndex && releaseIndex != -1) {
            reorder1();
        }
    }
    
    private void jList2MouseReleased2(java.awt.event.MouseEvent evt) {
        releaseIndex = selectedList2.locationToIndex(evt.getPoint());
        if (releaseIndex != pressIndex && releaseIndex != -1) {
            reorder2();
        }
    }

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2){
            int index = toSelectList.locationToIndex(evt.getPoint());

            DefaultListModel model1 = (DefaultListModel) toSelectList.getModel();
            DefaultListModel model2 = (DefaultListModel) selectedList1.getModel();

            Object dragee = model1.elementAt(index);
            if(df_instance.get(dragee).isNumeric()){
                model1.removeElementAt(index);
                model2.insertElementAt(dragee,model2.getSize());
            }
            else{
                JOptionPane.showMessageDialog(null,"Sorry the data is not numeric");
            }
        }
    }

    private void jList2MouseClicked1(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2){
            int index = selectedList1.locationToIndex(evt.getPoint());

            DefaultListModel model1 = (DefaultListModel) toSelectList.getModel();
            DefaultListModel model2 = (DefaultListModel) selectedList1.getModel();

            Object dragee = model2.elementAt(index);
            model2.removeElementAt(index);
            model1.insertElementAt(dragee, model1.getSize());
        }
    }
    
    private void jList2MouseClicked2(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2){
            int index = selectedList2.locationToIndex(evt.getPoint());

            DefaultListModel model1 = (DefaultListModel) toSelectList.getModel();
            DefaultListModel model2 = (DefaultListModel) selectedList2.getModel();

            Object dragee = model2.elementAt(index);
            model2.removeElementAt(index);
            model1.insertElementAt(dragee, model1.getSize());
        }
    }

    private void jList2MouseDragged1(java.awt.event.MouseEvent evt) {
        jList2MouseReleased1(evt);
        pressIndex = releaseIndex;
    }
    
    private void jList2MouseDragged2(java.awt.event.MouseEvent evt) {
        jList2MouseReleased2(evt);
        pressIndex = releaseIndex;
    }

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {
        //setDataLists(df_instance, null);
        DefaultListModel model1 = (DefaultListModel) toSelectList.getModel();
        DefaultListModel model2 = (DefaultListModel) selectedList1.getModel();

        int from = model1.getSize();
        for(int i=model2.getSize()-1; i>=0; i--){
            Object ee = model2.elementAt(i);
            model2.removeElementAt(i);
            model1.insertElementAt(ee, from);
        }
//        DefaultListModel model1 = new DefaultListModel();
//        for(int i=0; i< variables.length; i++){
//            model1.addElement(variables[i]);
//        }
//        toSelectList.setModel(model1);
//        DefaultListModel model2 = new DefaultListModel();
//        selectedList.setModel(model2);
    }
    
    private void resetActionPerformed2(java.awt.event.ActionEvent evt) {
        //setDataListsCat(df_instance, null);

        DefaultListModel model1 = (DefaultListModel) toSelectList.getModel();
        DefaultListModel model2 = (DefaultListModel) selectedList2.getModel();

        int from = model1.getSize();
        for(int i=model2.getSize()-1; i>=0; i--){
            Object ee = model2.elementAt(i);
            model2.removeElementAt(i);
            model1.insertElementAt(ee, from);
        }
//        DefaultListModel model1 = new DefaultListModel();
//        for(int i=0; i< variables.length; i++){
//            model1.addElement(variables[i]);
//        }
//        toSelectList.setModel(model1);
//        DefaultListModel model2 = new DefaultListModel();
//        selectedList.setModel(model2);
    }

    private void insertActionPerformed1(java.awt.event.ActionEvent evt) {
        int index[] = toSelectList.getSelectedIndices();

        DefaultListModel model1 = (DefaultListModel) toSelectList.getModel();
        DefaultListModel model2 = (DefaultListModel) selectedList1.getModel();

        int from = model2.getSize();
        for(int i=index.length-1; i>=0; i--){
            Object ee = model1.elementAt(index[i]);
            if(df_instance.get(ee).isNumeric()){
                model1.removeElementAt(index[i]);
                model2.insertElementAt(ee, from);
            }
            else{
                JOptionPane.showMessageDialog(null, "Sorry the data is not numeric");
            }
        }
    }
    
    private void insertActionPerformed2(java.awt.event.ActionEvent evt) {
        int index[] = toSelectList.getSelectedIndices();

        DefaultListModel model1 = (DefaultListModel) toSelectList.getModel();
        DefaultListModel model2 = (DefaultListModel) selectedList2.getModel();

        int from = model2.getSize();
        for(int i=index.length-1; i>=0; i--){
            Object ee = model1.elementAt(index[i]);
            if(df_instance.get(ee).isText()){
                model1.removeElementAt(index[i]);
                model2.insertElementAt(ee, from);
            }
            else{
                JOptionPane.showMessageDialog(null, "Sorry the data is not categoric");
            }
        }
    }

    private void removeActionPerformed1(java.awt.event.ActionEvent evt) {
        int index[] = selectedList1.getSelectedIndices();

        DefaultListModel model1 = (DefaultListModel) toSelectList.getModel();
        DefaultListModel model2 = (DefaultListModel) selectedList1.getModel();

        int from = model1.getSize();
        for(int i=index.length-1; i>=0; i--){
            Object ee = model2.elementAt(index[i]);
            model2.removeElementAt(index[i]);
            model1.insertElementAt(ee, from);
        }
    }

    private void removeActionPerformed2(java.awt.event.ActionEvent evt) {
        int index[] = selectedList2.getSelectedIndices();

        DefaultListModel model1 = (DefaultListModel) toSelectList.getModel();
        DefaultListModel model2 = (DefaultListModel) selectedList2.getModel();

        int from = model1.getSize();
        for(int i=index.length-1; i>=0; i--){
            Object ee = model2.elementAt(index[i]);
            model2.removeElementAt(index[i]);
            model1.insertElementAt(ee, from);
        }
    }

    // Variables declaration - do not modify
    private javax.swing.JButton insertButton1;
    private javax.swing.JButton removeButton1;
    private javax.swing.JButton insertButton2;
    private javax.swing.JButton removeButton2;
    private javax.swing.JButton resetButton1;
    private javax.swing.JButton resetButton2;
    private javax.swing.JComboBox groupsComboBox;
    private javax.swing.JLabel labAvailable;
    private javax.swing.JLabel labSelected1;
    private javax.swing.JLabel labSelected2;
    private javax.swing.JLabel labGroups;
    private javax.swing.JList toSelectList;
    private javax.swing.JList selectedList1;
    private javax.swing.JList selectedList2;
    private javax.swing.JScrollPane toSelectScrollPane;
    private javax.swing.JScrollPane selectedScrollPane1;
    private javax.swing.JScrollPane selectedScrollPane2;
    // End of variables declaration

}


