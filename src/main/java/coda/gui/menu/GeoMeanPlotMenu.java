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

import javax.swing.JFrame;
import coda.DataFrame;
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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author Guest2
 */
public class GeoMeanPlotMenu extends AbstractMenuDialog{
    
    Rengine re;
    String tempDirR;
    JFrame frameGeoMean;
    DataFrame df;
    
    public static final long serialVersionUID = 1L;
    
    public GeoMeanPlotMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Geometric Mean Barplot Menu", true);
        re = r;
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        JFrame frame = new JFrame();
        frame.setTitle("Message");
        
        String selectedNames[] = ds.getSelectedData();
        
        if(selectedNames.length > 0){
            
            df = mainApplication.getActiveDataFrame();
            
            double[][] data = df.getNumericalData(selectedNames); // matriu amb les dades corresponents
                
            for(int i=0; i < df.size(); i++){
                re.eval(df.get(i).getName() + " <- NULL");
                
                if(df.get(i).isNumeric()){
                    for(double j : df.get(i).getNumericalData()){
                        re.eval(df.get(i).getName() + " <- c(" + df.get(i).getName() +"," + String.valueOf(j) + ")");
                    }
                }
                else{
                    for(String j : df.get(i).getTextData()){
                        re.eval(df.get(i).getName() + " <- c(" + df.get(i).getName() +",'" + j + "')");
                    }
                }
            }
            
            String dataFrameString = "mydf <- data.frame(";
                
            for(int i=0; i < df.size(); i++){
                dataFrameString += df.get(i).getName();
                if(i != df.size()-1) dataFrameString += ",";
            }
            
            dataFrameString += ")";
            re.eval(dataFrameString); // creem el dataframe amb R
            
            re.eval("out <- capture.output(mydf)");
            String[] out = re.eval("out").asStringArray();
            
            re.eval("mypath = tempdir()");
            tempDirR = re.eval("print(mypath)").asString();
            tempDirR += "\\out.png";
                
            re.eval("png(base::paste(tempdir(),\"out.png\",sep=\"\\\\\"),width=700,height=700)");
            //re.eval("png(mypath,width=700,height=700");
            
            if(ds.getSelectedGroup() == null){ // fem el grafic per components
                re.eval("means <- NULL");
                re.eval("names <- NULL");
                for(int i=0; i < selectedNames.length; i++){
                    re.eval("means" + " <- c(means, EnvStats::geoMean(mydf$" + selectedNames[i] + "))");
                    re.eval("names" + " <- c(names, \"" + selectedNames[i] + "\")");
                }
                re.eval("barplot(means, main = \"Geometric Mean Barplot\", names.arg=names,col=rainbow(length(means)))");
            }
            else{
                for(int i=0; i < selectedNames.length; i++){
                    re.eval(selectedNames[i] + " <- tapply(mydf$" + selectedNames[i] + ",mydf$" + ds.getSelectedGroup() + ",EnvStats::geoMean)");
                }
                re.eval("data <- NULL");
                for(int i=0; i < selectedNames.length;i++){
                    re.eval("data <- cbind(data," + selectedNames[i] +")");
                }
                re.eval("barplot(data,main = \"Geometric Mean Barplot\", beside=T,legend=rownames(data),col=rainbow(length(rownames(data))), args.legend=list(cex=0.8,x=\"topright\"))");
            }
                
                re.eval("dev.off()");
                
                setVisible(false);
                
            try {
                // jframe configuration
                
                plotGeoMean();
            } catch (IOException ex) {
                Logger.getLogger(GeoMeanPlotMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{ // no data selected
            JOptionPane.showMessageDialog(frame, "Please select data");
        }
        
    }
    
    private void plotGeoMean() throws IOException {
            Font f = new Font("Arial", Font.PLAIN,12);
            UIManager.put("Menu.font", f);
            UIManager.put("MenuItem.font",f);
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            JMenuItem menuItem = new JMenuItem("Open");
            menuBar.add(menu);
            frameGeoMean = new JFrame();
            JPanel panel = new JPanel();
            menu.add(menuItem);
            menuItem = new JMenuItem("Export");
            JMenu submenuExport = new JMenu("Export");
            menuItem = new JMenuItem("Export As PNG");
            menuItem.addActionListener(new FileChooserAction());
            submenuExport.add(menuItem);
            menuItem = new JMenuItem("Export As JPEG");
            submenuExport.add(menuItem);
            menuItem = new JMenuItem("Export As PDF");
            submenuExport.add(menuItem);
            menuItem = new JMenuItem("Export As WMF");
            submenuExport.add(menuItem);
            menuItem = new JMenuItem("Export As Postscripts");
            submenuExport.add(menuItem);
            menuItem = new JMenuItem("Quit");
            menuItem.addActionListener(new quitListener());
            menu.add(submenuExport);
            menu.add(menuItem);
            frameGeoMean.setJMenuBar(menuBar);
            panel.setSize(800,800);
            BufferedImage img = ImageIO.read(new File(tempDirR));
            ImageIcon icon = new ImageIcon(img);
            JLabel label = new JLabel(icon);
            label.setSize(700, 700);
            panel.setLayout(new GridBagLayout());
            panel.add(label);
            frameGeoMean.getContentPane().add(panel);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            frameGeoMean.setSize(800,800);
            frameGeoMean.setLocation(dim.width/2-frameGeoMean.getSize().width/2, dim.height/2-frameGeoMean.getSize().height/2);
            
            WindowListener exitListener = new WindowAdapter(){
                
                @Override
                public void windowClosing(WindowEvent e){
                    int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                    if(confirm == 0){
                        frameGeoMean.dispose();
                        File file = new File(tempDirR);
                        file.delete();
                    }
                }
            };
            
            frameGeoMean.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frameGeoMean.addWindowListener(exitListener);
            frameGeoMean.setVisible(true);
    }
    
    private class quitListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
            if(confirm == 0){
                frameGeoMean.dispose();
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
            jf.setSelectedFile(new File(".png"));
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
    
    public DataFrame getDataFrame(){
        return this.df;
    }
}
