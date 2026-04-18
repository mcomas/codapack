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

import coda.plot2.datafig.CoDaShape;
import coda.plot2.datafig.FilledCircle;
import coda.plot2.datafig.FilledSquare;
import coda.plot2.datafig.FilledStar;
import coda.plot2.datafig.FilledTriangleDown;
import coda.plot2.datafig.FilledTriangleUp;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;

/**
 *
 * @author marc
 */
public class ShapePanel extends JComboBox implements ItemListener{ 
       CoDaShape shape = new FilledCircle();
        public ShapePanel(){
            this.addItem("Circle");
            this.addItem("Square");
            this.addItem("Star");
            this.addItem("Triangle down");
            this.addItem("Triangle up");
        }
        public ShapePanel(CoDaShape shape){
            this.shape = shape;
            this.addItem("Circle");
            if(shape instanceof FilledCircle)
                this.setSelectedItem("Circle");
            this.addItem("Square");
            if(shape instanceof FilledSquare)
                this.setSelectedItem("Square");
            this.addItem("Star");
            if(shape instanceof FilledStar)
                this.setSelectedItem("Star");
            this.addItem("Triangle down");
            if(shape instanceof FilledTriangleDown)
                this.setSelectedItem("Triangle down");
            this.addItem("Triangle up");
            if(shape instanceof FilledTriangleUp)
                this.setSelectedItem("Triangle up");
        }
        public CoDaShape getShape(){
            return shape;
        }
        public void itemStateChanged(ItemEvent arg0) {
            int selection = getSelectedIndex();
            Color color = shape.getColor();
            switch (selection) {
                case 0:  shape = new FilledCircle(); break;
                case 1:  shape = new FilledSquare(); break;
                case 2:  shape = new FilledStar(5); break;
                case 3:  shape = new FilledTriangleDown(); break;
                case 4:  shape = new FilledTriangleUp(); break;
            }
            shape.setColor(color);
        }
    }
