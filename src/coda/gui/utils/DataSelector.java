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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.SwingConstants;

/**
 *
 * @author mcomas
 */
public final class DataSelector extends JPanel {
    public static final long serialVersionUID = 1L;
    int pressIndex = -1;
    int releaseIndex = -1;
    String variables[];
    boolean groupedBy = true;
    /** Creates new form DataSelector */
    public DataSelector(DataFrame dataFrame){
        initComponents();
        //jList2.setModel(new DefaultListModel());
        if(dataFrame != null)
            setDataLists(dataFrame, null);

    }
        public DataSelector(DataFrame dataFrame, boolean groups, boolean all) {
        initComponents();
        groupsComboBox.setVisible(groups);
        labGroups.setVisible(groups);

        //jList2.setModel(new DefaultListModel());
        //jList2.setModel(new DefaultListModel());
        if (all == true){
            if(dataFrame != null)
                setAllDataLists(dataFrame, null);
        }else{
            if(dataFrame != null)
                setDataLists(dataFrame, null);
        }
    }
    public DataSelector(DataFrame dataFrame, int[]selected, boolean groups) {
        initComponents();
        groupsComboBox.setVisible(groups);
        labGroups.setVisible(groups);
        //jList2.setModel(new DefaultListModel());
        if(dataFrame != null)
            setDataLists(dataFrame, selected);

    }
    public DataSelector(DataFrame dataFrame, boolean groups) {
        initComponents();
        groupsComboBox.setVisible(groups);
        labGroups.setVisible(groups);

        //jList2.setModel(new DefaultListModel());
        //jList2.setModel(new DefaultListModel());
        if(dataFrame != null)
            setDataLists(dataFrame, null);
    }
    private void reorder() {
        DefaultListModel model = (DefaultListModel) selectedList.getModel();
        Object dragee = model.elementAt(pressIndex);
        model.removeElementAt(pressIndex);
        model.insertElementAt(dragee, releaseIndex);
    }
    public void setAllDataLists(DataFrame dataFrame, int[] selected){
        DefaultListModel model1 = new DefaultListModel();
        DefaultListModel model2 = new DefaultListModel();
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
        selectedList.setModel(model2);
    }
    public void setDataLists(DataFrame dataFrame, int[] selected){
        DefaultListModel model1 = new DefaultListModel();
        DefaultListModel model2 = new DefaultListModel();
        variables = new String[dataFrame.size()];
        //Iterator it = dataFrame.getNamesIterator();
        int i = -1;
        for(String name : dataFrame.getNames()){
            variables[++i] = name;
            if(((Variable)dataFrame.get(variables[i])).isFactor())
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
        selectedList.setModel(model2);
    }
    public ListModel getAvailableData(){
        return toSelectList.getModel();
    }
    public void setSelectedData(ListModel list){
        selectedList.setModel(list);
    }
    public String getGroupData(){
        if(groupsComboBox.getSelectedIndex() == 0) return null;
        else return (String) groupsComboBox.getSelectedItem();
    }
    public String getSelectedGroup(){
        if(groupsComboBox.getSelectedIndex() == 0) return null;

        return (String)groupsComboBox.getSelectedItem();
    }
    public String[] getSelectedData(){
        //int selected[] = jList2.getModel();//getSelectedIndices();
        DefaultListModel model = (DefaultListModel)selectedList.getModel();
        String[] res = new String[model.size()];
        for(int i=0,m=model.size(); i<m; i++)
            res[i] = (String)model.getElementAt(i);

        return res;

        /*
        System.out.println(selected.length);
        int n = selected.length;

        String sel[] = new String[n];
        ListModel lm = jList2.getModel();

        for(int i=0;i<n;i++)
            sel[i] = (String)lm.getElementAt(selected[i]);
        return sel;
         * */
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
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
        groupsComboBox = new javax.swing.JComboBox();
        labGroups = new javax.swing.JLabel();

        setBorder(BorderFactory.createTitledBorder("Selected"));

        setSize(new Dimension(360, 350));
        setMaximumSize(new Dimension(360, 350));
        setPreferredSize(new Dimension(360, 350));

        labAvailable.setText("Available data:");
        //labAvailable.setPreferredSize(new Dimension(120, 20));
        //labAvailable.setSize(new Dimension(120, 20));
        //labAvailable.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        labSelected.setText("Selected data:");
        //labSelected.setPreferredSize(new Dimension(120, 20));
        //labSelected.setSize(new Dimension(120, 20));
        //labSelected.setAlignmentX(JLabel.LEFT_ALIGNMENT);

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
        groupsComboBox.setPreferredSize(new Dimension(120, 24));
        groupsComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<No groups>" }));

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
        right.add(labGroups);
        right.add(groupsComboBox);
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
    private javax.swing.JComboBox groupsComboBox;
    private javax.swing.JLabel labAvailable;
    private javax.swing.JLabel labSelected;
    private javax.swing.JLabel labGroups;
    private javax.swing.JList toSelectList;
    private javax.swing.JList selectedList;
    private javax.swing.JScrollPane toSelectScrollPane;
    private javax.swing.JScrollPane selectedScrollPane;
    // End of variables declaration

}
