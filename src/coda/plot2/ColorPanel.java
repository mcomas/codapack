/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.plot2;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

/**
 *
 * @author marc
 */
public class ColorPanel extends JPanel implements MouseListener{

     public ColorPanel(){
         this.setBackground(Color.black);
     }
     public ColorPanel(Color initialBackground){
         this.setBackground(initialBackground);
     }
     public void mouseClicked(MouseEvent me) {
         Color colorSelected = JColorChooser.showDialog(this,
             "Choose a color", this.getBackground());
         if (colorSelected != null) {
           this.setBackground(colorSelected);
         }
     }
     public void mousePressed(MouseEvent me) { }

     public void mouseReleased(MouseEvent me) { }

     public void mouseEntered(MouseEvent me) { }

     public void mouseExited(MouseEvent me) { }

 }
    
