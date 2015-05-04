/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot2.window;

import coda.ext.jama.Matrix;
import coda.gui.menu.CoDaPackDialog;
import coda.gui.utils.AddDataManually;
import coda.plot2.ColorPanel;
import coda.plot2.PlotUtils;
import coda.plot2.TernaryPlot2dDisplay;
import coda.plot2.objects.Ternary2dCurveObject;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

/**
 *
 * @author marc
 */
public class TernaryPlot2dDialogCoDaLines extends CoDaPackDialog{

    double JTextBox [] = null;
    double data[][] = null;
    SamplePanel sample = new SamplePanel();
    ColorPanel selectedColor;
    Color selColor = Color.black;
    
    TernaryPlot2dWindow window;
    JTextField jSize = new JTextField("  1");
    public TernaryPlot2dDialogCoDaLines(TernaryPlot2dWindow window){
        super(window, "Add CoDa Straight lines");


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
        selectedColor.setBackground(selColor);
        selectedColor.setPreferredSize(new Dimension(50,20));


        propertyPanel.add(new JLabel("Color:"));
        propertyPanel.add(selectedColor);
        propertyPanel.add(new JLabel("Size:"));
        propertyPanel.add(jSize);

        JPanel samplePanel = new JPanel();
        sample.setSize(new Dimension(70,70));
        sample.setPreferredSize(new Dimension(70,70));
        samplePanel.add(sample);

        JPanel mainArea = new JPanel();
        mainArea.add(importing);
        mainArea.add(propertyPanel);
        mainArea.add(samplePanel);
        
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
    double[][] concat(double[][] A, double[][] B) {
        int aLen = A.length;
        int bLen = B.length;
        double[][] C= new double[aLen+bLen][];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
     }
    private void acceptButton(){
        TernaryPlot2dDisplay display = window.getDisplay();
        double a[][] = new Matrix(data).transpose().getArray();
        double [][] curve = PlotUtils.segment(a[0], a[1], 100);
        for(int i=1;i<a.length-1;i++){
            curve = concat(curve, PlotUtils.segment(a[i], a[i+1], 100));
        }
        Ternary2dCurveObject curveObject3 = new Ternary2dCurveObject(display, curve);
        float f = 1;
        try{
            f = Float.parseFloat(jSize.getText());
        }catch(NumberFormatException e){}
        curveObject3.setSize(f);
        curveObject3.setColor(selColor);
        display.addCoDaObject( curveObject3 );
        //Ternary2dObject dataObject = new Ternary2dDataObject(display, new Matrix(data).transpose().getArray(), shape);
        //display.addCoDaObject(dataObject);
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
//            selColor = color;
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
            //selectedShape.getShape().plot(g2, new Point2D.Double(sample.getWidth()/2, sample.getHeight()/2));
        }
    }
}
