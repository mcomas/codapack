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
import coda.DataFrame.DataFrameException;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import org.apache.commons.lang3.ArrayUtils;
/**
 *
 * @author mcomas
 */
public final class DataSelector1to2 extends DataSelector {

    private JList<String> vars_available = new JList<String>();
    private JList<String> varsA_selected = new JList<String>();
    private JList<String> varsB_selected = new JList<String>();
    private JComboBox<String> var_group = new JComboBox<String>();

    public static int ONLY_NUMERIC = 1;
    public static int ALL_VARIABLES = 2;
    public static int ONLY_CATEGORIC = 3;

    public static final long serialVersionUID = 1L;
    int pressIndex = -1;
    int releaseIndex = -1;

    JLabel label_varA = new JLabel("A variables");
    JLabel label_varB = new JLabel("B variables");
    int selection_type = ONLY_NUMERIC;
    public DataSelector1to2(String names[]) throws DataFrameException{
        initComponents(false);
        ds_dataFrame = new DataFrame();
        for(String name: names) ds_dataFrame.add(new Variable(name));
        selection_type = ONLY_NUMERIC;

        setDataLists(ds_dataFrame);
    }
    public DataSelector1to2(DataFrame dataFrame, boolean groups) {
        initComponents(groups);
        ds_dataFrame = dataFrame;
        selection_type = ONLY_NUMERIC;

        if(dataFrame != null)
            setDataLists(dataFrame);            
    }    
    public DataSelector1to2(DataFrame dataFrame, boolean groups, int variable_type) {
        initComponents(groups);
        ds_dataFrame = dataFrame;
        selection_type = variable_type;

        if(dataFrame != null)
            setDataLists(dataFrame);
    }
    public void setTextA(String label){
        label_varA.setText(label);
    }
    public void setTextB(String label){
        label_varB.setText(label);
    }
    private void reorderA() {
        DefaultListModel<String> model = (DefaultListModel<String>) varsA_selected.getModel();
        String dragee = model.elementAt(pressIndex);
        model.removeElementAt(pressIndex);
        model.insertElementAt(dragee, releaseIndex);
    }
    private void reorderB() {
        DefaultListModel<String> model = (DefaultListModel<String>) varsB_selected.getModel();
        String dragee = model.elementAt(pressIndex);
        model.removeElementAt(pressIndex);
        model.insertElementAt(dragee, releaseIndex);
    }
    public void setDataLists(DataFrame dataFrame){
        DefaultListModel<String> model1 = new DefaultListModel<String>();
        DefaultListModel<String> model2A = new DefaultListModel<String>();
        DefaultListModel<String> model2B = new DefaultListModel<String>();

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
        varsA_selected.setModel(model2A);
        varsB_selected.setModel(model2B);
    }
    public void updateDataLists(DataFrame dataFrame){        
        if(ds_dataFrame == null){
            setDataLists(dataFrame);
        } 
        List<String> selected_varsA = Arrays.asList(this.getSelectedDataA());
        List<String> selected_varsB = Arrays.asList(this.getSelectedDataB());
        String select_group_by = this.getSelectedGroup();

        DefaultListModel<String> model1 = new DefaultListModel<String>();
        DefaultListModel<String> model2A = new DefaultListModel<String>();
        DefaultListModel<String> model2B = new DefaultListModel<String>();
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
                    if(selected_varsA.contains(name)){
                        model2A.addElement(name);
                    } else if(selected_varsB.contains(name)){
                        model2B.addElement(name);
                    }
                    else{
                        model1.addElement(name);
                    }
                }
            }else{
                if(selection_type != ONLY_CATEGORIC){
                    if(selected_varsA.contains(name)){
                        model2A.addElement(name);
                    } else if(selected_varsB.contains(name)){
                        model2B.addElement(name);
                    }
                    else{
                        model1.addElement(name);
                    }
                }
            }
        }
        
        vars_available.setModel(model1);
        varsA_selected.setModel(model2A);
        varsB_selected.setModel(model2B);
        ds_dataFrame = dataFrame;
    }
    public ListModel<String> getAvailableData(){
        return vars_available.getModel();
    }
    public void setSelectedData(ListModel<String> list){
        
    }
    public String getGroupData(){
        if(var_group.getSelectedIndex() == 0) return null;
        else return (String) var_group.getSelectedItem();
    }
    public String getSelectedGroup(){
        if(var_group.getSelectedIndex() == 0) return null;

        return (String)var_group.getSelectedItem();
    }
    public String[] concat(String[] array1, String[] array2) {
        String[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }
    public String[] getSelectedData(){     
        return(getSelectedDataA());
        //return concat(getSelectedDataA(), getSelectedDataB());
    }
    public String[] getSelectedDataA(){
        DefaultListModel<String> modelA = (DefaultListModel<String>)varsA_selected.getModel();
        String[] resA = new String[modelA.size()];
        for(int i=0,m=modelA.size(); i<m; i++)
            resA[i] = (String)modelA.getElementAt(i);      
        return resA;
    }
    public String[] getSelectedDataB(){
        DefaultListModel<String> modelB = (DefaultListModel<String>)varsB_selected.getModel();
            String[] resB = new String[modelB.size()];
        for(int i=0,m=modelB.size(); i<m; i++)
            resB[i] = (String)modelB.getElementAt(i);        
        return resB;
    }
    private void initComponents(boolean groups) {
        
        //setLayout(new GridBagLayout());
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        //GridBagConstraints c = new GridBagConstraints(0,0,1,1,0.5,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(0,0,0,0), 40, 0);
      
        setBorder(BorderFactory.createTitledBorder("Select variables"));
    
        JPanel p_vars_available = new JPanel(new BorderLayout());
        p_vars_available.setBorder(BorderFactory.createTitledBorder("Available"));
        p_vars_available.setAlignmentX(Component.LEFT_ALIGNMENT);
        p_vars_available.setPreferredSize(new Dimension(250, 500));
        p_vars_available.setMaximumSize(new Dimension(400, 500));

        JPanel p_varsG_controls = new JPanel();
        p_varsG_controls.setLayout(new BoxLayout(p_varsG_controls, BoxLayout.PAGE_AXIS));
        JPanel p_varsA_controls = new JPanel();
        JPanel p_varsB_controls = new JPanel();
        p_varsA_controls.setLayout(new BoxLayout(p_varsA_controls, BoxLayout.Y_AXIS));
        p_varsB_controls.setLayout(new BoxLayout(p_varsB_controls, BoxLayout.Y_AXIS));
        p_varsG_controls.add(p_varsA_controls);
        p_varsG_controls.add(p_varsB_controls);

        JPanel p_vars_selected = new JPanel();
        p_vars_selected.setBorder(BorderFactory.createTitledBorder("Selected"));
        p_vars_selected.setLayout(new BoxLayout(p_vars_selected, BoxLayout.PAGE_AXIS));
        p_vars_selected.setPreferredSize(new Dimension(250, 500));
        p_vars_selected.setMaximumSize(new Dimension(400, 500));

        add(p_vars_available);
        add(p_varsG_controls);
        add(p_vars_selected);

        JScrollPane sp_vars_available = new JScrollPane();
        sp_vars_available.setViewportView(vars_available);
        p_vars_available.add(sp_vars_available, BorderLayout.CENTER);


        JScrollPane sp_varsA_selected = new JScrollPane();
        sp_varsA_selected.setViewportView(varsA_selected);

        JScrollPane sp_varsB_selected = new JScrollPane();
        sp_varsB_selected.setViewportView(varsB_selected);

        label_varA.setAlignmentX(Component.LEFT_ALIGNMENT);
        p_vars_selected.add(label_varA);
        p_vars_selected.add(sp_varsA_selected);
        p_vars_selected.add(Box.createRigidArea(new Dimension(0,5)));
        label_varB.setAlignmentX(Component.LEFT_ALIGNMENT);
        p_vars_selected.add(label_varB);
        p_vars_selected.add(sp_varsB_selected);

        if(groups){
            JPanel groupPanel = new JPanel(new BorderLayout());
            groupPanel.setBorder(BorderFactory.createTitledBorder("Group by"));
            var_group.setPrototypeDisplayValue("XXXXXXXXXXXX");
            groupPanel.add(var_group);
            p_vars_selected.add(groupPanel, BorderLayout.SOUTH);
        }

        p_varsA_controls.add(Box.createRigidArea(new Dimension(75, 40)));
        p_varsB_controls.add(Box.createRigidArea(new Dimension(75, 40)));

        JButton insertButtonA = new JButton(">");
        insertButtonA.setAlignmentX(Component.CENTER_ALIGNMENT);
        p_varsA_controls.add(insertButtonA);

        JButton removeButtonA = new JButton("<");
        removeButtonA.setAlignmentX(Component.CENTER_ALIGNMENT);
        p_varsA_controls.add(removeButtonA);

        JButton insertButtonB = new JButton(">");
        insertButtonB.setAlignmentX(Component.CENTER_ALIGNMENT);
        p_varsB_controls.add(insertButtonB);

        JButton removeButtonB = new JButton("<");
        removeButtonB.setAlignmentX(Component.CENTER_ALIGNMENT);
        p_varsB_controls.add(removeButtonB);

        p_varsB_controls.add(Box.createRigidArea(new Dimension(100, 40)));

        JButton resetButton = new JButton("Reset");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        p_varsB_controls.add(resetButton);

        /*
        EVENTS
        */
        // vars_available.addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseClicked(MouseEvent evt) {
        //         vars_available_MouseClicked(evt);
        //     }
        // });

        varsA_selected.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                varsA_selected_MouseClicked(evt);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                varsA_selected_MousePressed(evt);
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                varsA_selected_MouseReleased(evt);
            }
        });
        varsB_selected.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                varsB_selected_MouseClicked(evt);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                varsB_selected_MousePressed(evt);
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                varsB_selected_MouseReleased(evt);
            }
        });
        varsA_selected.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                varsA_selected_MouseDragged(evt);
            }
        });
        varsB_selected.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                varsB_selected_MouseDragged(evt);
            }
        });
        insertButtonA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertActionPerformedA(evt);
            }
        });
        removeButtonA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformedA(evt);
            }
        });        
        insertButtonB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertActionPerformedB(evt);
            }
        });
        removeButtonB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformedB(evt);
            }
        });
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });
    }

    private void varsA_selected_MousePressed(java.awt.event.MouseEvent evt) {
        pressIndex = varsA_selected.locationToIndex(evt.getPoint());

    }
    private void varsB_selected_MousePressed(java.awt.event.MouseEvent evt) {
        pressIndex = varsB_selected.locationToIndex(evt.getPoint());

    }
    private void varsA_selected_MouseReleased(java.awt.event.MouseEvent evt) {
        releaseIndex = varsA_selected.locationToIndex(evt.getPoint());
        if (releaseIndex != pressIndex && releaseIndex != -1) {
            reorderA();
        }
    }
    private void varsB_selected_MouseReleased(java.awt.event.MouseEvent evt) {
        releaseIndex = varsB_selected.locationToIndex(evt.getPoint());
        if (releaseIndex != pressIndex && releaseIndex != -1) {
            reorderB();
        }
    }
    // private void vars_available_MouseClicked(java.awt.event.MouseEvent evt) {
    //     if (evt.getClickCount() == 2){
    //         int index = vars_available.locationToIndex(evt.getPoint());

    //         DefaultListModel<String> model1 = (DefaultListModel<String>) vars_available.getModel();
    //         DefaultListModel<String> model2 = (DefaultListModel<String>) vars_selected.getModel();

    //         String dragee = model1.elementAt(index);
    //         model1.removeElementAt(index);
    //         model2.insertElementAt(dragee,model2.getSize());
    //     }
    // }

    private void varsA_selected_MouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2){
            int index = varsA_selected.locationToIndex(evt.getPoint());

            DefaultListModel<String> model1 = (DefaultListModel<String>) vars_available.getModel();
            DefaultListModel<String> model2A = (DefaultListModel<String>) varsA_selected.getModel();

            String dragee = model2A.elementAt(index);
            model2A.removeElementAt(index);
            model1.insertElementAt(dragee, model1.getSize());
        }
    }
    private void varsB_selected_MouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2){
            int index = varsB_selected.locationToIndex(evt.getPoint());

            DefaultListModel<String> model1 = (DefaultListModel<String>) vars_available.getModel();
            DefaultListModel<String> model2B = (DefaultListModel<String>) varsB_selected.getModel();

            String dragee = model2B.elementAt(index);
            model2B.removeElementAt(index);
            model1.insertElementAt(dragee, model1.getSize());
        }
    }
    private void varsA_selected_MouseDragged(java.awt.event.MouseEvent evt) {
        varsA_selected_MouseReleased(evt);
        pressIndex = releaseIndex;
    }
    private void varsB_selected_MouseDragged(java.awt.event.MouseEvent evt) {
        varsB_selected_MouseReleased(evt);
        pressIndex = releaseIndex;
    }
    private void resetActionPerformed(java.awt.event.ActionEvent evt) {
        setDataLists(ds_dataFrame);
    }

    private void insertActionPerformedA(java.awt.event.ActionEvent evt) {
        int index[] = vars_available.getSelectedIndices();

        DefaultListModel<String> model1 = (DefaultListModel<String>) vars_available.getModel();
        DefaultListModel<String> model2A = (DefaultListModel<String>) varsA_selected.getModel();

        int from = model2A.getSize();
        for(int i=index.length-1; i>=0; i--){
            String ee = model1.elementAt(index[i]);
            model1.removeElementAt(index[i]);
            model2A.insertElementAt(ee, from);
        }
    }
    private void insertActionPerformedB(java.awt.event.ActionEvent evt) {
        int index[] = vars_available.getSelectedIndices();

        DefaultListModel<String> model1 = (DefaultListModel<String>) vars_available.getModel();
        DefaultListModel<String> model2B = (DefaultListModel<String>) varsB_selected.getModel();

        int from = model2B.getSize();
        for(int i=index.length-1; i>=0; i--){
            String ee = model1.elementAt(index[i]);
            model1.removeElementAt(index[i]);
            model2B.insertElementAt(ee, from);
        }
    }
    private void removeActionPerformedA(java.awt.event.ActionEvent evt) {
        int index[] = varsA_selected.getSelectedIndices();

        DefaultListModel<String> model1 = (DefaultListModel<String>) vars_available.getModel();
        DefaultListModel<String> model2A = (DefaultListModel<String>) varsA_selected.getModel();

        int from = model1.getSize();
        for(int i=index.length-1; i>=0; i--){
            String ee = model2A.elementAt(index[i]);
            model2A.removeElementAt(index[i]);
            model1.insertElementAt(ee, from);
        }
    }
    private void removeActionPerformedB(java.awt.event.ActionEvent evt) {
        int index[] = varsB_selected.getSelectedIndices();

        DefaultListModel<String> model1 = (DefaultListModel<String>) vars_available.getModel();
        DefaultListModel<String> model2B = (DefaultListModel<String>) varsB_selected.getModel();

        int from = model1.getSize();
        for(int i=index.length-1; i>=0; i--){
            String ee = model2B.elementAt(index[i]);
            model2B.removeElementAt(index[i]);
            model1.insertElementAt(ee, from);
        }
    }
}
