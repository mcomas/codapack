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
    
