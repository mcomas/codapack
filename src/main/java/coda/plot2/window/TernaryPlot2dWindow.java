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
 * WindowTernaryPlot.java
 *
 * Created on Sep 9, 2010, 12:11:02 PM
 */
package coda.plot2.window;

import coda.CoDaStats;
import coda.DataFrame;
import coda.plot.window.*;
import coda.gui.CoDaPackMain;
import coda.gui.menu.AbstractMenuDialog;
import coda.gui.menu.HelpMenu;
import coda.plot2.objects.Ternary2dGridObject;
import coda.plot2.TernaryPlot2dDisplay;
import coda.plot2.objects.Ternary2dObject;
import coda.plot2.objects.TernaryObject;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
/**
 *
 * @author marc
 */
public class TernaryPlot2dWindow extends CoDaPlot2dWindow{
    private double vx[] = new double[2];
    private double vy[] = new double[2];
    private final double sinA = 0.86602540378443864676372317075294;
    private final double cosA = 0.5;
    private TernaryPlot2dDisplay ternaryPlot;
    private JCheckBox checkBoxGridSelector = new JCheckBox();
    private JCheckBox checkBoxCenteredSelector = new JCheckBox();

    private double invCenter[] = null;
    
    private JButton rotate;
    //private JButton standard;
    private JButton inverted;

    protected JMenu menuEdit;
        private final String ITEM_EDIT = "Edit";
            private JMenuItem itemAddDataSet;
            private final String ITEM_DATA_SET = "Add data set";
            private JMenuItem itemAddCoDaStraight;
            private final String ITEM_CODA_STRAIGHT = "Add coda straight lines";
            private JMenuItem itemAddCurve;
            private final String ITEM_CURVE = "Add curve";
            private JMenuItem itemElements;
            private final String ITEM_ELEMENTS = "Elements";
            private static final String yamlUrl = "Help/Graphs.Ternary-Quaternary Plot-Empty.yaml";
            private static final String helpTitle = "Ternary-Quaternary Plot-Empty Help";

    public TernaryPlot2dWindow(DataFrame dataframe, TernaryPlot2dDisplay display, String title) {
        super(dataframe, display, title);

        this.menuData.setVisible(false);
        menuEdit = new JMenu();
        menuEdit.setText(ITEM_EDIT);
        itemAddDataSet = new JMenuItem();
        itemAddDataSet.setText(ITEM_DATA_SET);
        itemAddDataSet.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDataSet();
            }
        });
        menuEdit.add(itemAddDataSet);
        itemAddCoDaStraight = new JMenuItem();
        itemAddCoDaStraight.setText(ITEM_CODA_STRAIGHT);
        itemAddCoDaStraight.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStraight();
            }
        });
        menuEdit.add(itemAddCoDaStraight);
        itemAddCurve = new JMenuItem();
        itemAddCurve.setText(ITEM_CURVE);
        itemAddCurve.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCurve();
            }
        });
        menuEdit.add(itemAddCurve);
        
        itemElements = new JMenuItem();
        itemElements.setText(ITEM_ELEMENTS);
        itemElements.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elements();
            }
        });
        menuEdit.add(itemElements);
        jMenuBar.add(menuEdit);


        ternaryPlot = display;        
        checkBoxGridSelector.setText("Grid");
        checkBoxGridSelector.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxGridSelectorActionPerformed(evt);
            }
        });
        checkBoxCenteredSelector.setText("Centered");
        checkBoxCenteredSelector.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxCenteredSelectorActionPerformed(evt);
            }
        });
        //standard = new JButton(new ImageIcon("resources/ternary_xyz.png"));
        inverted = new JButton(new ImageIcon(getClass().getResource(CoDaPackMain.RESOURCE_PATH + "ternary_xzy.png")));
        rotate = new JButton(new ImageIcon(getClass().getResource(CoDaPackMain.RESOURCE_PATH + "rotate.png")));

        inverted.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inverted_buttonEvent(evt);
            }
        });
        JPanel controlTernaryPlot = new JPanel();        
        controlTernaryPlot.setSize(0,40);
        
        controlTernaryPlot.add(inverted);
        rotate.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotate_buttonEvent(evt);
            }
        });
        controlTernaryPlot.add(rotate);
        
        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                JDialog dialog = new JDialog();
                HelpMenu menu;
                try {
                    menu = new HelpMenu(yamlUrl,helpTitle);
                    dialog.add(menu);
                    dialog.setSize(650, 500);
                    dialog.setTitle(helpTitle);
                    dialog.setIconImage(Toolkit.getDefaultToolkit()
                    .getImage(getClass().getResource(CoDaPackMain.RESOURCE_PATH + "logo.png")));
                    Point p = getLocation();
                    p.x = p.x + (getWidth()-520)/2;
                    p.y = p.y + (getHeight()-430)/2;
                    WindowListener exitListener = new WindowAdapter(){
                
                        @Override
                        public void windowClosing(WindowEvent e){
                                dialog.dispose();
                                menu.deleteHtml();
                        }
                    };
            
                    dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    dialog.addWindowListener(exitListener);
                    dialog.setLocation(p);
                    dialog.setVisible(true);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AbstractMenuDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        controlTernaryPlot.add(new JLabel(" "));
        
        controlTernaryPlot.add(checkBoxGridSelector);
        controlTernaryPlot.add(checkBoxCenteredSelector);
        controlTernaryPlot.add(helpButton);
        particularControls1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        particularControls1.add(controlTernaryPlot);
        
        pack();
    }
    JList dataList;
    JPopupMenu pm = new JPopupMenu();
    void elements(){
        TernaryPlot2dDisplay td = this.getDisplay();
        final ArrayList<Ternary2dObject> to = td.getCoDaObjects();
        final JDialog jd = new JDialog(this);
        JPanel ds = new JPanel();
        jd.setSize(190,370);
        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        dataList = new javax.swing.JList();
        
        final DefaultListModel model = new DefaultListModel();
        //Iterator it = dataFrame.getNamesIterator();
        int i = 0;
        for(Ternary2dObject obj : to){
            model.addElement(obj);
        }
        
        dataList.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2){
                        element();
                    }
                    
                    if ( evt.getButton() == 3 ){
                        final int num = dataList.locationToIndex(evt.getPoint());
                        dataList.setSelectedIndex(num); //select the item
                        pm = new JPopupMenu();
                        JMenuItem jmi = new JMenuItem("Delete");
                        jmi.addActionListener(new ActionListener(){
                            public void actionPerformed(ActionEvent e) {
                                Ternary2dObject obj = (Ternary2dObject) 
                                        dataList.getSelectedValue(); //To change body of generated methods, choose Tools | Templates.
                                if(obj instanceof Ternary2dGridObject){
                                } else {
                                    to.remove(obj);
                                    display.repaint();
                                    model.remove(num);
                                }
                            }
                        });
                        pm.add(jmi);
                        //one.setText("Rename variable " + df.get(col).getName() + ".");
                        //two.setText("Eliminate variable " + df.get(col).getName() + ".");
                        pm.show(dataList, evt.getX(), evt.getY());
                    }
                }
            });
        
        /*
        while(it.hasNext()){
            variables[i] = (String) it.next();
            model.addElement(variables[i++]);
        }*/
        dataList.setModel(model);
        
        ds.setLayout(new java.awt.BorderLayout());
                
        jScrollPane1.setViewportView(dataList);
        ds.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        
        jd.getContentPane().add(ds, BorderLayout.CENTER);
        jd.setVisible(true);
    }
    void element(){
        TernaryObject obj = (TernaryObject) dataList.getModel().getElementAt(dataList.getSelectedIndex());
        obj.setParameters(this);
    }
    void addDataSet(){
        new TernaryPlot2dDialogDataSet(this).setVisible(true);
    }
    void addStraight(){
        new TernaryPlot2dDialogCoDaLines(this).setVisible(true);
    }
    void addCurve(){
        new TernaryPlot2dDialogCurve(this).setVisible(true);
    }
    void inverted_buttonEvent(ActionEvent ev){
        vx = ternaryPlot.getVX();
        vy = ternaryPlot.getVY();
        vx[0] = -vx[0];
        vy[0] = -vy[0];
        ternaryPlot.setVX(vx[0], vx[1]);
        ternaryPlot.setVY(vy[0], vy[1]);
        ternaryPlot.repaint();
    }void rotate_buttonEvent(ActionEvent ev){
        vx = ternaryPlot.getVX();
        vy = ternaryPlot.getVY();
        double x = vx[0];
        double y = vx[1];

        vx[0] = cosA * x - sinA *  y;
        vx[1] = sinA * x + cosA *  y;
        ternaryPlot.setVX(vx[0], vx[1]);


        x = vy[0];
        y = vy[1];

        vy[0] = cosA * x - sinA *  y;
        vy[1] = sinA * x + cosA *  y;

        ternaryPlot.setVY(vy[0], vy[1]);
        ternaryPlot.repaint();
    }
    @Override
    public void repaint(){
        super.repaint();
    }
    public void setCenter(double center[]){
        this.invCenter = CoDaStats.powering(center, -1);
    }
    public TernaryPlot2dDisplay getDisplay(){
        return ternaryPlot;
    }
    private void checkBoxGridSelectorActionPerformed(java.awt.event.ActionEvent evt) {
        ArrayList<Ternary2dGridObject> grids = ternaryPlot.getGrid();
        if(checkBoxGridSelector.isSelected()){
            for(Ternary2dGridObject grid : grids)
                grid.setVisible(true);
        }else{
            for(Ternary2dGridObject grid : grids)
                grid.setVisible(false);
        }
        ternaryPlot.repaint();
    }
    private void checkBoxCenteredSelectorActionPerformed(java.awt.event.ActionEvent evt) {
        if(checkBoxCenteredSelector.isSelected()){
           double[] center = ternaryPlot.getCenter();
           invCenter[0] = 1 / center[0];
           invCenter[1] = 1 / center[1];
           invCenter[2] = 1 / center[2];
           ternaryPlot.perturbate(invCenter);
        }else{
            double[] ones = {1, 1, 1};
            ternaryPlot.perturbate(ones);
        }
        ternaryPlot.repaint();
    }
}
