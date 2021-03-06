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

package coda.plot2.objects;

import coda.plot.AbstractCoDaDisplay.LegendItem;
import coda.plot2.*;
import coda.CoDaStats;
import coda.plot.CoDa3dDisplay;
import coda.plot.CoDaDisplayConfiguration;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import javax.swing.JFrame;

/**
 *
 * @author marc
 */
public class Ternary3dDataObject implements Ternary3dObject{
    private double data[][];
    private double data0[][];
    private double dataset[][];

    private CoDa3dDisplay display;
    private Color color;
    CoDaDisplayConfiguration config;
    public Ternary3dDataObject(CoDa3dDisplay display, double dataset[][]){
        config = new CoDaDisplayConfiguration();
        this.display = display;
        this.dataset = dataset;
        int n = dataset.length;
        data0 = new double[n][];
        data = new double[n][3];
        for(int i=0;i<n;i++){
            data0[i] = CoDaStats.ternaryTransform(
                    dataset[i][0], dataset[i][1], dataset[i][2], dataset[i][3]);
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void plotObject(Graphics2D g2) {
        g2.setStroke(new BasicStroke(0.5f,
                BasicStroke.JOIN_MITER,
                BasicStroke.CAP_ROUND));

        float s = config.getSize("data");
        Point2D o = null;
        for(int i=0;i<data.length;i++){
            g2.setColor( color );
            o = display.getGeometry().transform(new Point2D.Double(data[i][0],data[i][1]), o);
            g2.fill(PlotUtils.drawPoint(o, s));
            g2.setColor( Color.black );
            g2.draw(PlotUtils.drawPoint(o, s));
        }
    }

    public void transformObject(CoDa3dDisplay display) {
        for(int i=0;i<data.length;i++){
            data[i] = display.transform(data0[i][0], data0[i][1], data0[i][2], data[i]);
        }
    }

    public void perturbeObject(double[] x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void powerObject(double t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public LegendItem getLegendItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double[] getCenter() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setParameters(JFrame f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
