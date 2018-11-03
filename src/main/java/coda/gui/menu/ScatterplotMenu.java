/** 
 *  Copyright 2011-2016 Marc Comas - Santiago Thió
 *
 *  This file is part of CoDaPack.
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

package coda.gui.menu;

import coda.DataFrame;
import javax.swing.JFrame;
import org.rosuda.JRI.Rengine;
import coda.gui.CoDaPackMain;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;

/**
 *
 * @author Guest2
 */
public class ScatterplotMenu extends AbstractMenuDialog{
    
    Rengine re;
    JFrame frameScatterplot;
    String tempDirR;
    DataFrame df;
    
    public static final long serialVersionUID = 1L;
    
    public ScatterplotMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Scatterplot Menu", false);
        re = r;
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        JFrame frame = new JFrame();
        frame.setTitle("Message");
        
        String selectedNames[] = ds.getSelectedData();
        
        if(selectedNames.length == 2 || selectedNames.length == 3){
            
            df = mainApplication.getActiveDataFrame();
            
            for(int i=0; i < selectedNames.length; i++){
                re.eval(selectedNames[i] + " <- NULL");
                for(double j: df.get(selectedNames[i]).getNumericalData()){
                    re.eval(selectedNames[i] + " <- c(" + selectedNames[i] + "," + String.valueOf(j) + ")");
                }
            }
            
            String dataFrameString = "mydf <- data.frame(";
            for(int i=0; i < selectedNames.length; i++){
                dataFrameString += selectedNames[i];
                if(i != selectedNames.length-1) dataFrameString += ",";
            }
            
            dataFrameString += ")";
            re.eval(dataFrameString); // creem el dataframe amb R
            
            if(selectedNames.length == 2){ // printem el gràfic en 2D
                
            }
            else{ // printem el gràfic en 3D
            }
            
            this.dispose();
            
        }
        else{
            JOptionPane.showMessageDialog(frame,"Please select two or three variables");
        }
    }
    
    private class ScatterPlot extends AbstractAnalysis{
        
        int size;
        double[][] data;
        
        public ScatterPlot(int size, double data[][]){
            this.size = size;
            this.data = data;
        }
        
        public void plot() throws Exception{
            AnalysisLauncher.open(this);
        }
        
        @Override
        public void init(){
            
        }
        
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
}
