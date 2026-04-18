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

package coda.plot2.window;

import coda.ext.jama.Matrix;
import coda.gui.menu.CoDaPackDialog;
import coda.gui.utils.AddDataManually;
import coda.plot2.ColorPanel;
import coda.plot2.ShapePanel;
import coda.plot2.datafig.CoDaShape;
import coda.plot2.datafig.FilledCircle;
import coda.plot2.objects.Ternary2dDataObject;
import coda.plot2.objects.Ternary2dObject;
import coda.plot2.TernaryPlot2dDisplay;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author marc
 */
public class TernaryPlot2dDialogDataSet extends CoDaPackDialog{

    double JTextBox [] = null;
    double data[][] = null;
    SamplePanel sample = new SamplePanel();
    ColorPanel selectedColor;
    ShapePanel selectedShape;

    CoDaShape shape = new FilledCircle();
    
    TernaryPlot2dWindow window;
    public TernaryPlot2dDialogDataSet(TernaryPlot2dWindow window){
        super(window, "Add Data Set");

        shape.setSize(3f);
        shape.setColor(Color.gray);
        
        this.window = window;
        setSize(560,430);
        setLocationRelativeTo(window);
        
        JButton manually = new JButton("Add data manually");
        manually.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manuallyButton();
            }
        });
        JButton fromTable = new JButton("Add data from table");
        fromTable.setEnabled(false);
        fromTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromTableButton();
            }
        });
        JPanel importing = new JPanel();
        importing.add(manually);
        importing.add(fromTable);

        JPanel propertyPanel = new JPanel();
        propertyPanel.setSize(new Dimension(350,150));
        propertyPanel.setPreferredSize(new Dimension(350,150));
        selectedColor = new ColorPanel();
        selectedColor.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        selectedColor.addMouseListener(selectedColor);
        selectedColor.setBackground(shape.getColor());
        selectedColor.setPreferredSize(new Dimension(50,20));


        selectedShape = new ShapePanel();
        selectedShape.addItemListener(selectedShape);
        propertyPanel.add(new JLabel("Color:"));
        propertyPanel.add(selectedColor);
        propertyPanel.add(new JLabel("Shape:"));
        propertyPanel.add(selectedShape);
        
        /*
         * JPanel samplePanel = new JPanel();
        sample.setSize(new Dimension(70,70));
        sample.setPreferredSize(new Dimension(70,70));
        samplePanel.add(sample);
        * */

        JPanel mainArea = new JPanel();
        mainArea.add(importing);
        mainArea.add(propertyPanel);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainArea, BorderLayout.CENTER);

        JPanel south = new JPanel();
        JButton accept = new JButton("Accept");
        accept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButton();
            }
        });
        south.add(accept);
        getContentPane().add(south, BorderLayout.SOUTH);
        sample.repaint();
    }
    private void acceptButton(){
        TernaryPlot2dDisplay display = window.getDisplay();
        Ternary2dDataObject dataObject = new Ternary2dDataObject(display, new Matrix(data).transpose().getArray(), shape);
        dataObject.setShape(selectedShape.getShape());
        dataObject.setColor(selectedColor.getBackground());
        display.addCoDaObject(dataObject);
        display.repaint();
        setVisible(false);
    }

    private void manuallyButton(){
        String labels[] = {"X","Y","Z"};
        AddDataManually dialog = new AddDataManually(this, data, labels);
        dialog.setModal(true);
        dialog.setVisible(true);
        data = dialog.data;
    }
    
    private void fromTableButton(){ }

//    private class ColorPanel extends JPanel implements MouseListener{
//        Color color = new Color(70, 70, 200);
//        public void mouseClicked(MouseEvent me) {
//            Color initialBackground = selectedColor.getBackground();
//            Color colorSelected = JColorChooser.showDialog(this,
//                "Choose a color", initialBackground);
//            if (colorSelected != null) {
//              color = colorSelected;
//              selectedColor.setBackground(colorSelected);
//            }
//            shape.setColor(color);
//            sample.repaint();
//        }
//        public void mousePressed(MouseEvent me) { }
//
//        public void mouseReleased(MouseEvent me) { }
//
//        public void mouseEntered(MouseEvent me) { }
//
//        public void mouseExited(MouseEvent me) { }
//
//    }
 
    class SamplePanel extends JComponent{
        public SamplePanel(){
            this.setBackground(Color.white);
        }
        @Override
        public final void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            selectedShape.getShape().plot(g2, new Point2D.Double(sample.getWidth()/2, sample.getHeight()/2));
        }
    }
}
