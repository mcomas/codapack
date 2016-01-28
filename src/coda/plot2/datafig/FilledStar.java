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

package coda.plot2.datafig;

import coda.ext.triangle.StarPolygon;
import coda.plot.CoDaDisplayConfiguration;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 *
 * @author marc
 */
public class FilledStar implements CoDaShape{
    Color interior;
    Color contour;
    double size;
    int vertexCount;
    public FilledStar(int vertexCount){
        CoDaDisplayConfiguration config = new CoDaDisplayConfiguration();
        this.interior = config.getColor("data0");
        this.contour = Color.BLACK;
        this.size = config.getSize("data");

        this.vertexCount = vertexCount;
    }
    public FilledStar(int vertexCount, Color interior, Color contour, double size){
        this.interior = interior;
        this.contour = contour;
        this.size = size;

        this.vertexCount = vertexCount;
    }
    public void plot(Graphics2D g2, Point2D p) {        
        StarPolygon star = new StarPolygon((int)p.getX(), (int)p.getY(), (int)(1.9*size),(int)(0.9*size), vertexCount, -Math.PI/2);

        g2.setColor( interior );
        g2.fill(star);
        g2.setColor( contour );
        g2.draw(star);
    }

    public Color getColor() {
        return interior;
    }
    public void setColor(Color color) {
        interior = color;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getSize() {
        return size;
    }

}
