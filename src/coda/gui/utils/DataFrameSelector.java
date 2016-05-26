/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió - David Gàmez
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

package coda.gui.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListModel;
/**
 *
 * @author david
 */
public class DataFrameSelector extends JPanel{
    public static final long serialVersionUID = 1L;
    int pressIndex = -1;
    int releaseIndex = -1;
    String[] variables;
    
    //Creem el constructor
    public DataFrameSelector(String[] names) {
        initComponents();
        this.setDataList(names);
    }
    
    public void setDataList(String[] names){
        int[] selected = null;
        DefaultListModel model1 = new DefaultListModel();
        DefaultListModel model2 = new DefaultListModel();
        variables = new String[names.length];
        int i = -1;
        for(String name : names){
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
        selectedList.setModel(model2);
    }
    
    public ListModel getAvailableData(){
        return toSelectList.getModel();
    }
    public void setSelectedData(ListModel list){
        selectedList.setModel(list);
    }
    
    public String[] getSelectedData(){
        //int selected[] = jList2.getModel();//getSelectedIndices();
        DefaultListModel model = (DefaultListModel)selectedList.getModel();
        String[] res = new String[model.size()];
        for(int i=0,m=model.size(); i<m; i++)
            res[i] = (String)model.getElementAt(i);

        return res;
    }
    
    private void reorder() {
        DefaultListModel model = (DefaultListModel) selectedList.getModel();
        Object dragee = model.elementAt(pressIndex);
        model.removeElementAt(pressIndex);
        model.insertElementAt(dragee, releaseIndex);
    }
    
    //Inicia els components que apareixen a la finestra
    @SuppressWarnings("unchecked")
    private void initComponents() {

        labAvailable = new javax.swing.JLabel();
        labSelected = new javax.swing.JLabel();
        toSelectScrollPane = new javax.swing.JScrollPane();
        toSelectList = new javax.swing.JList();
        selectedScrollPane = new javax.swing.JScrollPane();
        selectedList = new javax.swing.JList();
        insertButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        //groupsComboBox = new javax.swing.JComboBox();
        //labGroups = new javax.swing.JLabel();

        setBorder(BorderFactory.createTitledBorder("Selected"));

        setSize(new Dimension(360, 350));
        setMaximumSize(new Dimension(360, 350));
        setPreferredSize(new Dimension(360, 350));

        labAvailable.setText("Available dataFrame:");

        labSelected.setText("Selected dataFrame:");

        toSelectScrollPane.setPreferredSize(new Dimension(120, 280));
        toSelectScrollPane.setSize(new Dimension(120, 280));

        toSelectList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        toSelectScrollPane.setViewportView(toSelectList);

        selectedScrollPane.setPreferredSize(new java.awt.Dimension(120, 200));
        selectedScrollPane.setSize(new java.awt.Dimension(120, 200));

        selectedList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked(evt);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jList2MousePressed(evt);
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jList2MouseReleased(evt);
            }
        });
        selectedList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jList2MouseDragged(evt);
            }
        });
        selectedScrollPane.setViewportView(selectedList);

        insertButton.setText(">");
        insertButton.setPreferredSize(new Dimension(42,25));
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertActionPerformed(evt);
            }
        });

        removeButton.setText("<");
        removeButton.setPreferredSize(new Dimension(42,25));
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });

        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });
        //groupsComboBox.setPreferredSize(new Dimension(120, 24));
        //groupsComboBox.setModel(new DefaultComboBoxModel(new String[] { "<No groups>" }));

        //labGroups.setText("Groups:");


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
        spacing.setPreferredSize(new Dimension(60,100));
        
        JPanel right = new JPanel();
        right.setPreferredSize(new Dimension(140,250));
        right.setSize(new Dimension(140,250));
        right.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));//new BoxLayout(right, BoxLayout.PAGE_AXIS));


        left.add(labAvailable);
        left.add(toSelectScrollPane);

        center.add(spacing);
        center.add(insertButton);
        center.add(removeButton);

        right.add(labSelected);
        right.add(selectedScrollPane);
        //right.add(labGroups);
        //right.add(groupsComboBox);
        right.add(resetButton);

        this.add(center,BorderLayout.CENTER);
        this.add(left,BorderLayout.WEST);
        this.add(right,BorderLayout.EAST);
        //this.add(left);
        //this.add(center);
        //this.add(right);
        
    }// </editor-fold>
    
    private void jList2MousePressed(java.awt.event.MouseEvent evt) {
        pressIndex = selectedList.locationToIndex(evt.getPoint());

    }

    private void jList2MouseReleased(java.awt.event.MouseEvent evt) {
        releaseIndex = selectedList.locationToIndex(evt.getPoint());
        if (releaseIndex != pressIndex && releaseIndex != -1) {
            reorder();
        }
    }

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2){
            int index = toSelectList.locationToIndex(evt.getPoint());

            DefaultListModel model1 = (DefaultListModel) toSelectList.getModel();
            DefaultListModel model2 = (DefaultListModel) selectedList.getModel();

            Object dragee = model1.elementAt(index);
            model1.removeElementAt(index);
            model2.insertElementAt(dragee,model2.getSize());
        }
    }

    private void jList2MouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2){
            int index = selectedList.locationToIndex(evt.getPoint());

            DefaultListModel model1 = (DefaultListModel) toSelectList.getModel();
            DefaultListModel model2 = (DefaultListModel) selectedList.getModel();

            Object dragee = model2.elementAt(index);
            model2.removeElementAt(index);
            model1.insertElementAt(dragee, model1.getSize());
        }
    }

    private void jList2MouseDragged(java.awt.event.MouseEvent evt) {
        jList2MouseReleased(evt);
        pressIndex = releaseIndex;
    }

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {
//        setDataLists(df_instance, null);
        DefaultListModel model1 = new DefaultListModel();
        for(int i=0; i< variables.length; i++){
            model1.addElement(variables[i]);
        }
        toSelectList.setModel(model1);
        DefaultListModel model2 = new DefaultListModel();
        selectedList.setModel(model2);
    }

    private void insertActionPerformed(java.awt.event.ActionEvent evt) {
        int index[] = toSelectList.getSelectedIndices();

        DefaultListModel model1 = (DefaultListModel) toSelectList.getModel();
        DefaultListModel model2 = (DefaultListModel) selectedList.getModel();

        int from = model2.getSize();
        for(int i=index.length-1; i>=0; i--){
            Object ee = model1.elementAt(index[i]);
            model1.removeElementAt(index[i]);
            model2.insertElementAt(ee, from);
        }
    }

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {
        int index[] = selectedList.getSelectedIndices();

        DefaultListModel model1 = (DefaultListModel) toSelectList.getModel();
        DefaultListModel model2 = (DefaultListModel) selectedList.getModel();

        int from = model1.getSize();
        for(int i=index.length-1; i>=0; i--){
            Object ee = model2.elementAt(index[i]);
            model2.removeElementAt(index[i]);
            model1.insertElementAt(ee, from);
        }
    }


    // Variables declaration - do not modify
    private javax.swing.JButton insertButton;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton resetButton;
    //private javax.swing.JComboBox groupsComboBox;
    private javax.swing.JLabel labAvailable;
    private javax.swing.JLabel labSelected;
    //private javax.swing.JLabel labGroups;
    private javax.swing.JList toSelectList;
    private javax.swing.JList selectedList;
    private javax.swing.JScrollPane toSelectScrollPane;
    private javax.swing.JScrollPane selectedScrollPane;
    // End of variables declaration

    
}
