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
import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author mcomas
 */
public final class BoxDataSelector extends javax.swing.JPanel {
    public static final long serialVersionUID = 1L;
    int pressIndex = -1;
    int releaseIndex = -1;
    String variables[];

    /** Creates new form DataSelector */
    public BoxDataSelector(DataFrame dataFrame) {
        initComponents();
        if(dataFrame != null) setDataList(dataFrame);
    }
    
    public BoxDataSelector(String[] var){
        initComponents();
        setDataListString(var);
    }
    
    public void setDataListString(String[] var){
        
        DefaultListModel model = new DefaultListModel();
        variables = new String[var.length];
        //Iterator it = dataFrame.getNamesIterator();
        int i = 0;
        for(String name : var){
            variables[i] = name;
            model.addElement(variables[i++]);
        }
        /*
        while(it.hasNext()){
            variables[i] = (String) it.next();
            model.addElement(variables[i++]);
        }*/
        jList1.setModel(model);
    }
    
    public void setDataList(DataFrame dataFrame){
        DefaultListModel model = new DefaultListModel();
        variables = new String[dataFrame.size()];
        //Iterator it = dataFrame.getNamesIterator();
        int i = 0;
        for(String name : dataFrame.getNames()){
            variables[i] = name;
            model.addElement(variables[i++]);
        }
        /*
        while(it.hasNext()){
            variables[i] = (String) it.next();
            model.addElement(variables[i++]);
        }*/
        jList1.setModel(model);
    }
    public String[] getSelectedData(){
        //int selected[] = jList2.getModel();//getSelectedIndices();
        int index[] = jList1.getSelectedIndices();
        DefaultListModel model = (DefaultListModel) jList1.getModel();
        String[] res = new String[index.length];
        for(int i=0; i<index.length; i++){
            res[i] = (String) model.elementAt(index[i]);

        }
        return res;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Select"));
        setPreferredSize(new java.awt.Dimension(170, 350));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(100, 132));

        jScrollPane1.setViewportView(jList1);

        this.setLayout(new BorderLayout());
        this.add(jScrollPane1,BorderLayout.CENTER);
    }// </editor-fold>


    // Variables declaration - do not modify
    public javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration

}
