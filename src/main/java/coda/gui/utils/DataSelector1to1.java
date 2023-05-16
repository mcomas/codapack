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


package coda.gui.utils;

import coda.DataFrame;
import coda.Variable;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
/**
 *
 * @author mcomas
 */
public final class DataSelector1to1 extends DataSelector {

    private JList<String> vars_available = new JList<String>();
    private JList<String> vars_selected = new JList<String>();
    private JComboBox<String> var_group = new JComboBox<String>();

    public static int ONLY_NUMERIC = 1;
    public static int ALL_VARIABLES = 2;
    public static int ONLY_CATEGORIC = 3;

    DataFrame df_instance;
    public static final long serialVersionUID = 1L;
    int pressIndex = -1;
    int releaseIndex = -1;
    boolean groupedBy = true;

    int selection_type = ONLY_NUMERIC;
    public DataSelector1to1(DataFrame dataFrame, boolean groups) {
        initComponents(groups);
        df_instance = dataFrame;
        if(dataFrame != null)
            setDataLists(dataFrame);
    }
    public DataSelector1to1(DataFrame dataFrame, boolean groups, int variable_type) {
        initComponents(groups);
        selection_type = variable_type;
        df_instance = dataFrame;
        if(dataFrame != null)
            setDataLists(dataFrame);
    }
    private void reorder() {
        DefaultListModel<String> model = (DefaultListModel<String>) vars_selected.getModel();
        String dragee = model.elementAt(pressIndex);
        model.removeElementAt(pressIndex);
        model.insertElementAt(dragee, releaseIndex);
    }
    public void setDataLists(DataFrame dataFrame){
        DefaultListModel<String> model1 = new DefaultListModel<String>();
        DefaultListModel<String> model2 = new DefaultListModel<String>();

        var_group.addItem("");
        int i = 0;
        for(String name : dataFrame.getNames()){
            Variable variable = dataFrame.get(name);
            if(variable.isText()){
                var_group.addItem(name);
                if(selection_type != ONLY_NUMERIC){
                    model1.addElement(name);
                }
            }else{
                if(selection_type != ONLY_CATEGORIC){
                    model1.addElement(name);
                }
            }
            
        }
        vars_available.setModel(model1);
        vars_selected.setModel(model2);
    }
    public void updateDataLists(DataFrame dataFrame){
        List<String> selected_vars = Arrays.asList(this.getSelectedData());
        String select_group_by = this.getSelectedGroup();

        DefaultListModel<String> model1 = new DefaultListModel<String>();
        DefaultListModel<String> model2 = new DefaultListModel<String>();
        var_group.removeAllItems();
        var_group.addItem("");
        for(String name : dataFrame.getNames()){
            Variable variable = dataFrame.get(name);
            if(variable.isText()){
                var_group.addItem(name);

                if(select_group_by != null && select_group_by.equals(name)){
                    var_group.setSelectedItem(name);
                }
                if(selection_type != ONLY_NUMERIC){
                    if(selected_vars.contains(name)){
                        model2.addElement(name);
                    }else{
                        model1.addElement(name);
                    }
                }
            }else{
                if(selection_type != ONLY_CATEGORIC){
                    if(selected_vars.contains(name)){
                        model2.addElement(name);
                    }else{
                        model1.addElement(name);
                    }
                }
            }
        }
        
        vars_available.setModel(model1);
        vars_selected.setModel(model2);
    }
    public ListModel<String> getAvailableData(){
        return vars_available.getModel();
    }
    public void setSelectedData(ListModel<String> list){
        vars_selected.setModel(list);
    }
    public String getGroupData(){
        if(var_group.getSelectedIndex() == 0) return null;
        else return (String) var_group.getSelectedItem();
    }
    public String getSelectedGroup(){
        if(var_group.getSelectedIndex() == 0) return null;

        return (String)var_group.getSelectedItem();
    }
    public String[] getSelectedData(){
        DefaultListModel<String> model = (DefaultListModel<String>)vars_selected.getModel();
        String[] res = new String[model.size()];
        for(int i=0,m=model.size(); i<m; i++)
            res[i] = (String)model.getElementAt(i);

        return res;
    }

    private void initComponents(boolean groups) {
        
        //setLayout(new GridBagLayout());
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        //GridBagConstraints c = new GridBagConstraints(0,0,1,1,0.5,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(0,0,0,0), 40, 0);
      
        setBorder(BorderFactory.createTitledBorder("Select variables"));
    
        JPanel p_vars_available = new JPanel(new BorderLayout());
        p_vars_available.setBorder(BorderFactory.createTitledBorder("Available"));
        p_vars_available.setAlignmentX(Component.LEFT_ALIGNMENT);
        p_vars_available.setMaximumSize(new Dimension(200, 500));

        JPanel p_vars_controls = new JPanel();
        p_vars_controls.setLayout(new BoxLayout(p_vars_controls, BoxLayout.Y_AXIS));

        JPanel p_vars_selected = new JPanel(new BorderLayout());
        p_vars_selected.setBorder(BorderFactory.createTitledBorder("Selected"));
        p_vars_selected.setAlignmentX(Component.RIGHT_ALIGNMENT);
        p_vars_selected.setMaximumSize(new Dimension(200, 500));

        add(p_vars_available);
        add(p_vars_controls);
        add(p_vars_selected);
        /* 
        add(p_vars_available, c);
        c.weightx = 0.2;
        c.gridx = 1;
        add(p_vars_controls, c);
        c.weightx = 0.5;
        c.gridx = 2;
        add(p_vars_selected, c);
        */

        JScrollPane sp_vars_available = new JScrollPane();
        sp_vars_available.setViewportView(vars_available);
        p_vars_available.add(sp_vars_available, BorderLayout.CENTER);


        JScrollPane sp_vars_selected = new JScrollPane();
        sp_vars_selected.setViewportView(vars_selected);
        p_vars_selected.add(sp_vars_selected, BorderLayout.CENTER);

        if(groups){
            JPanel groupPanel = new JPanel(new BorderLayout());
            groupPanel.setBorder(BorderFactory.createTitledBorder("Group by"));
            var_group.setPrototypeDisplayValue("XXXXXXXXXXXX");
            groupPanel.add(var_group);
            p_vars_selected.add(groupPanel, BorderLayout.SOUTH);
        }

        p_vars_controls.add(Box.createRigidArea(new Dimension(75, 100)));

        JButton insertButton = new JButton(">");
        insertButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        p_vars_controls.add(insertButton);

        JButton removeButton = new JButton("<");
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        p_vars_controls.add(removeButton);

        p_vars_controls.add(Box.createRigidArea(new Dimension(100, 100)));

        JButton resetButton = new JButton("Reset");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        p_vars_controls.add(resetButton);

        /*
        EVENTS
        */
        vars_available.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                vars_available_MouseClicked(evt);
            }
        });

        vars_selected.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vars_selected_MouseClicked(evt);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                vars_selected_MousePressed(evt);
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                vars_selected_MouseReleased(evt);
            }
        });
        vars_selected.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                vars_selected_MouseDragged(evt);
            }
        });
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertActionPerformed(evt);
            }
        });
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });
    }

    private void vars_selected_MousePressed(java.awt.event.MouseEvent evt) {
        pressIndex = vars_selected.locationToIndex(evt.getPoint());

    }

    private void vars_selected_MouseReleased(java.awt.event.MouseEvent evt) {
        releaseIndex = vars_selected.locationToIndex(evt.getPoint());
        if (releaseIndex != pressIndex && releaseIndex != -1) {
            reorder();
        }
    }

    private void vars_available_MouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2){
            int index = vars_available.locationToIndex(evt.getPoint());

            DefaultListModel<String> model1 = (DefaultListModel<String>) vars_available.getModel();
            DefaultListModel<String> model2 = (DefaultListModel<String>) vars_selected.getModel();

            String dragee = model1.elementAt(index);
            model1.removeElementAt(index);
            model2.insertElementAt(dragee,model2.getSize());
        }
    }

    private void vars_selected_MouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2){
            int index = vars_selected.locationToIndex(evt.getPoint());

            DefaultListModel<String> model1 = (DefaultListModel<String>) vars_available.getModel();
            DefaultListModel<String> model2 = (DefaultListModel<String>) vars_selected.getModel();

            String dragee = model2.elementAt(index);
            model2.removeElementAt(index);
            model1.insertElementAt(dragee, model1.getSize());
        }
    }

    private void vars_selected_MouseDragged(java.awt.event.MouseEvent evt) {
        vars_selected_MouseReleased(evt);
        pressIndex = releaseIndex;
    }

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {
        setDataLists(df_instance);
    }

    private void insertActionPerformed(java.awt.event.ActionEvent evt) {
        int index[] = vars_available.getSelectedIndices();

        DefaultListModel<String> model1 = (DefaultListModel<String>) vars_available.getModel();
        DefaultListModel<String> model2 = (DefaultListModel<String>) vars_selected.getModel();

        int from = model2.getSize();
        for(int i=index.length-1; i>=0; i--){
            String ee = model1.elementAt(index[i]);
            model1.removeElementAt(index[i]);
            model2.insertElementAt(ee, from);
        }
    }

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {
        int index[] = vars_selected.getSelectedIndices();

        DefaultListModel<String> model1 = (DefaultListModel<String>) vars_available.getModel();
        DefaultListModel<String> model2 = (DefaultListModel<String>) vars_selected.getModel();

        int from = model1.getSize();
        for(int i=index.length-1; i>=0; i--){
            String ee = model2.elementAt(index[i]);
            model2.removeElementAt(index[i]);
            model1.insertElementAt(ee, from);
        }
    }

    /*public void setSelectedName(String data1) {
        this.labSelected.setText(data1);
    }*/

}
