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

package coda.plot;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JComponent;

/**
 *
 * @author marc
 */
public abstract class AbstractCoDaDisplay extends JComponent{
    public static final long serialVersionUID = 1L;
    
    //Visualization parameters
    protected double factor;
    protected double actualZoom = 1;
    protected double lastOffsetX, lastOffsetY;
    protected double displayWidth;
    protected double displayHeight;
    protected ArrayList<LegendItem> legendNames = new ArrayList<LegendItem>();
    protected String[] obsNames;
    protected boolean showAllZ = false;
    public CoDaDisplayConfiguration config;
    public AbstractCoDaDisplay(){
        setOpaque(true);
        setDoubleBuffered(true);
        setFocusable(true);
        config = new CoDaDisplayConfiguration();
    }
    
    public abstract void scale(double k);

    public abstract void zoom(double dzoom);
    
    public abstract void translate(double x, double y);

    public abstract void transformData();
    public void setObservationNames(String names[]){
        obsNames = names;
    }
    public void showAllData(boolean showAll){
        showAllZ = showAll;
    }
    @Override
    public final void paintComponent(Graphics g){
        displayWidth = getWidth();
        displayHeight = getHeight();
        transformData();
        paintComponent((Graphics2D) g,
                displayWidth,
                displayHeight);

        if(!legendNames.isEmpty())
            drawLegend((Graphics2D) g);
    }
    public void paintComponent(Graphics2D g2, double width, double height){
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                                        RenderingHints.VALUE_RENDER_QUALITY);
        
        g2.setColor( config.getColor("background") );
        g2.fill(new Rectangle2D.Double(0, 0, width, height));
        
    }
    private void drawLegend(Graphics2D g2){
        
        int el = 0;
        float s = config.getSize("data");

        int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
        int fontSize = (int)Math.round(11.0 * screenRes / 72.0);

        Font font = new Font("Arial", Font.PLAIN, fontSize);
        g2.setFont(font);

        Iterator<LegendItem> it = legendNames.iterator();
        while(it.hasNext()){
            el++;
            LegendItem li = it.next();
            g2.setColor( config.getColor(li.code) );
            if(li.form == li.DOT){
                g2.fill(PlotUtils.drawPoint(new Point2D.Double(15, 15 * el), 1.5*s));
                g2.setColor( Color.black );
                g2.draw(PlotUtils.drawPoint(new Point2D.Double(15, 15 * el), 1.5*s));
            }
            if(li.form == li.LINE){
                g2.setStroke(new BasicStroke(2*config.getSize("Prin.Comp.") ,
                    BasicStroke.JOIN_MITER,
                    BasicStroke.CAP_ROUND));
                g2.draw(PlotUtils.drawLine(new Point2D.Double(10, 15 * el), new Point2D.Double(20, 15 * el)));
            }
            g2.setColor(Color.BLACK);
            g2.drawString(li.text, 30 , 5 + 15 * el);
        }
    }
    public class LegendItem{
        public short DOT = 0;
        public short LINE = 1;
        public String text;
        public String code;
        public short form;
        public LegendItem(String text, String code){
            this.text = text;
            this.code = code;
            this.form = DOT;
        }
        public LegendItem(String text, String code, short form){
            this.text = text;
            this.code = code;
            this.form = form;
        }
    }
}