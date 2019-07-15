/** 
 *  Copyright 2011-2016 Marc Comas - Santiago Thi�
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
import static coda.gui.CoDaPackMain.outputPanel;
import static coda.gui.CoDaPackMain.re;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;
import coda.gui.output.OutputText;
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
import org.apache.batik.swing.JSVGCanvas;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Sphere;
import org.jzy3d.plot3d.rendering.canvas.Quality;

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
        super(mainApp, "Scatterplot Menu", true);
        re = r;
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        JFrame frame = new JFrame();
        frame.setTitle("Message");
        
        String selectedNames[] = ds.getSelectedData();
        String selectedGroup = ds.getSelectedGroup();
        
        if(selectedNames.length == 2 || selectedNames.length == 3){
            
            df = mainApplication.getActiveDataFrame();
            
            for(int i=0; i < selectedNames.length; i++){
                re.eval(selectedNames[i] + " <- NULL");
                for(double j: df.get(selectedNames[i]).getNumericalData()){
                    re.eval(selectedNames[i] + " <- c(" + selectedNames[i] + "," + String.valueOf(j) + ")");
                }
            }
            
            if(selectedGroup != null){ // si em seleccionat grup
                re.eval(selectedGroup + " <- NULL");
                for(String j: df.get(selectedGroup).getTextData()){
                    re.eval(selectedGroup + " <- c(" + selectedGroup + ",'" + j + "')");
                }
            }
            
            String dataFrameString = "mydf <- data.frame(";
            for(int i=0; i < selectedNames.length; i++){
                dataFrameString += selectedNames[i];
                if(i != selectedNames.length-1 || selectedGroup != null) dataFrameString += ",";
            }
            
            if(selectedGroup != null){
                dataFrameString += selectedGroup;
            }
            
            dataFrameString += ")";
            re.eval(dataFrameString); // creem el dataframe amb R
            
            if(selectedNames.length == 2){ // printem el gr�fic en 2D
                
                    /*re.eval("mypath = tempdir()");	
                    tempDirR = re.eval("print(mypath)").asString();	
                    tempDirR += "\\out.svg";	
            	
                    re.eval("svg(base::paste(tempdir(),\"out.svg\",sep=\"\\\\\"),width=700,height=700)");	
                    re.eval("svg(mypath,width=700,height=700");*/
                    
                    this.tempDirR = re.eval(" name <- paste(tempdir(),\"Scatterplot.svg\",sep=\"\\\\\")").asString();
                    
                    re.eval("svg(name)");
                    
                    if(selectedGroup != null){
                        re.eval("plot(mydf$" + selectedNames[0] + ", mydf$" + selectedNames[1] + ", col = mydf$" + selectedGroup + ")");
                        re.eval("legend(\"topright\", levels(mydf$" + selectedGroup + "), fill = mydf$" + selectedGroup + ")");
                    }
                    else{
                        re.eval("plot(" + selectedNames[0] + "," + selectedNames[1] + ")");
                    }
                    re.eval("dev.off()");	
            	
                    try {	
                        plotScatterplot();	
                    } catch (IOException ex) {	
                        Logger.getLogger(ScatterplotMenu.class.getName()).log(Level.SEVERE, null, ex);	
                    }
            }
            else{ // printem el gr�fic en 3D
                
                /*re.eval("mypath = tempdir()");
                    tempDirR = re.eval("print(mypath)").asString();
                    tempDirR += "\\out.svg";
            
                    re.eval("svg(base::paste(tempdir(),\"out.svg\",sep=\"\\\\\"),width=700,height=700)");
                    re.eval("svg(mypath,width=700,height=700");*/
                
                    this.tempDirR = re.eval("name <- paste(tempdir(),\"Scatterplot.svg\",sep=\"\\\\\")").asString();
                    
                    re.eval("svg(name)");
                
                    if(selectedGroup != null){
                        re.eval("colors <- mydf$" + selectedGroup);
                        re.eval("scatterplot3d::scatterplot3d(mydf$" + selectedNames[0] + ",mydf$" + selectedNames[1] + ",mydf$" + selectedNames[2] +", color=as.numeric(mydf$" + selectedGroup + "), pch = 19)");
                        re.eval("legend(\"topright\", levels(mydf$" + selectedGroup + "), fill = mydf$" + selectedGroup + ")");
                    }
                    else{
                        re.eval("s3d <- scatterplot3d::scatterplot3d(" + selectedNames[0] + "," + selectedNames[1] + "," + selectedNames[2] +",pch = 16, cex.symbols = 1.5, color=\"black\")");
                    }
                    re.eval("dev.off()");
            
                    try {
                        plotScatterplot();
                    } catch (IOException ex) {
                        Logger.getLogger(ScatterplotMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            
            this.dispose();
            
        }
        else{
            JOptionPane.showMessageDialog(frame,"Please select two or three variables");
        }
    }
    
    private void plotScatterplot() throws IOException {	
            Font f = new Font("Arial", Font.PLAIN,12);	
            UIManager.put("Menu.font", f);	
            UIManager.put("MenuItem.font",f);	
            JMenuBar menuBar = new JMenuBar();	
            JMenu menu = new JMenu("File");	
            JMenuItem menuItem = new JMenuItem("Open");	
            menuBar.add(menu);	
            frameScatterplot = new JFrame();	
            menu.add(menuItem);	
            menuItem = new JMenuItem("Export");	
            JMenu submenuExport = new JMenu("Export");	
            menuItem = new JMenuItem("Export As SVG");	
            menuItem.addActionListener(new FileChooserAction());	
            submenuExport.add(menuItem);	
            menuItem = new JMenuItem("Export As JPEG");	
            //submenuExport.add(menuItem);	
            menuItem = new JMenuItem("Export As PDF");	
            //submenuExport.add(menuItem);	
            menuItem = new JMenuItem("Export As WMF");	
            //submenuExport.add(menuItem);	
            menuItem = new JMenuItem("Export As Postscripts");	
            //submenuExport.add(menuItem);	
            menuItem = new JMenuItem("Quit");	
            menuItem.addActionListener(new quitListener());	
            menu.add(submenuExport);	
            menu.add(menuItem);	
            frameScatterplot.setJMenuBar(menuBar);
            JSVGCanvas c = new JSVGCanvas();
            String uri = new File(tempDirR).toURI().toString();
            c.setURI(uri);

            frameScatterplot.getContentPane().add(c);	
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();	
            frameScatterplot.setSize(800,800);	
            frameScatterplot.setLocation(dim.width/2-frameScatterplot.getSize().width/2, dim.height/2-frameScatterplot.getSize().height/2);	
            	
            WindowListener exitListener = new WindowAdapter(){	
                	
                @Override	
                public void windowClosing(WindowEvent e){	
                    int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);	
                    if(confirm == 0){	
                        frameScatterplot.dispose();	
                        File file = new File(tempDirR);	
                        file.delete();	
                    }	
                }	
            };	
            	
            frameScatterplot.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);	
            frameScatterplot.addWindowListener(exitListener);	
            frameScatterplot.setVisible(true);	
    }
    
       private class quitListener implements ActionListener{
        public void actionPerformed(ActionEvent e){	        
            int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
            if(confirm == 0){
                frameScatterplot.dispose();	        
                File file = new File(tempDirR);
                file.delete();
            }
        }
       }
    
    private class FileChooserAction implements ActionListener{
     public void actionPerformed(ActionEvent e){
            JFrame frame = new JFrame();
            JFileChooser jf = new JFileChooser();
            frame.setSize(400,400);
            jf.setDialogTitle("Select the folder to save the file");
            jf.setApproveButtonText("Save");
            jf.setSelectedFile(new File(".svg"));
            jf.setSize(400,400);
            frame.add(jf);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);	
            boolean canExit = false;	
            while(! canExit){	
                int result = jf.showSaveDialog(frame);	
                if(JFileChooser.CANCEL_OPTION == result){	
                    frame.dispose();	
                    canExit = true;	
                }	
                if(JFileChooser.APPROVE_OPTION == result){ // guardem arxiu en el path	
                    File f = new File(tempDirR);	
                    f.deleteOnExit();	
                    String path = jf.getSelectedFile().getAbsolutePath();	
                    File f2 = new File(path);	
                    if(f2.exists()){	
                        int response = JOptionPane.showConfirmDialog(null, "Do you want to replace the existing file?", 	
                                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);	
                        if(response != JOptionPane.YES_OPTION) canExit = false;	
                        else{	
                            f.renameTo(f2);	
                            frame.dispose();	
                            canExit = true;	
                        }	
                    }	
                    else{	
                        f.renameTo(f2);	
                        frame.dispose();	
                        canExit = true;	
                    }	
                }	
            }	
        }
    }
    
    private class ScatterPlot extends AbstractAnalysis{
        
        double[][] data;
        int size;
        
        public ScatterPlot(int size, double data[][]){
            this.data = data;
            this.size = size;
        }
        
        public void plot() throws Exception{
            AnalysisLauncher.open(this);
        }
        
        @Override
        public void init(){
            float x,y,z,a;
            
            Coord3d[] points = new Coord3d[this.size];
            Color[] colors = new Color[size];
            
            for(int j=0; j < this.data[0].length;j++){
                x = (float) this.data[0][j];
                y = (float) this.data[1][j];
                a = 0.25f;
                z = (float) this.data[2][j];
                points[j] = new Coord3d(x,y,z);
                colors[j] = new Color(0,0,0);
            }
            
            Scatter scatter = new Scatter(points,colors);
            scatter.setWidth(7);
            Quality q = Quality.Advanced;
            q.setSmoothPoint(true);
            chart = AWTChartComponentFactory.chart(q,"swing");
            chart.getScene().add(scatter);
        }
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
}
