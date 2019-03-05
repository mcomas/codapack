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
import coda.gui.CoDaPackMain;
import static coda.gui.CoDaPackMain.outputPanel;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.rosuda.JRI.Rengine;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import javax.swing.JRadioButton;
import javax.swing.UIManager;

/**
 *
 * @author Guest2
 */
public class ZpatternsMenu extends AbstractMenuDialog{
    
    Rengine re;
    JFrame frameZPatterns;
    String tempDirR;
    DataFrame df;
    
    /* options var */
   
    JRadioButton B1 = new JRadioButton("Show means");
    JRadioButton B2 = new JRadioButton("Show percentages");
    String showMeans = "show.means = " ;
    String barLabels = "bar.labels = ";
    
    public static final long serialVersionUID = 1L;
    
    public ZpatternsMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "ZPatterns Plot Menu", false);
        re = r;
        
        /* options configuration */
        
        this.optionsPanel.add(B1);
        this.optionsPanel.add(B2);
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        JFrame frame = new JFrame();
        frame.setTitle("Message");
        
        String selectedNames[] = ds.getSelectedData();
        
        if(selectedNames.length > 0){
            df = mainApplication.getActiveDataFrame();
            double[][] data = df.getNumericalData(selectedNames); // matriu amb les dades corresponents
                
                re.assign("X", data[0]);
                re.eval("X" + " <- matrix( " + "X" + " ,nc=1)");
                for(int i=1; i < data.length; i++){
                    re.assign("tmp", data[i]);
                    re.eval("X" + " <- cbind(" + "X" + ",matrix(tmp,nc=1))");
                }
                
                /* OPTIONS CONFIGURATION */
                
                if(B1.isSelected()) showMeans += "TRUE";
                else showMeans += "FALSE";
                
                if(B2.isSelected()) barLabels += "TRUE";
                else barLabels += "FALSE";
               
                //String OS = System.getProperty("os.name").toLowerCase();
                //String tempDir = System.getProperty("java.io.tmpdir");
                re.eval("mypath = tempdir()");
                tempDirR = re.eval("print(mypath)").asString();
                tempDirR += "\\out.png";
                
                re.eval("png(base::paste(tempdir(),\"out.png\",sep=\"\\\\\"),width=700,height=700)");
                re.eval("png(mypath,width=700,height=700");
                re.eval("zCompositions::zPatterns(X,label=0," + showMeans + "," + barLabels + ")");
                re.eval("out <- capture.output(zCompositions::zPatterns(X,label=0," + showMeans + "," + barLabels + "))");
                re.eval("dev.off()");
                
                setVisible(false);
                
            try {
                // jframe configuration
                
                plotZPatterns();
            } catch (IOException ex) {
                Logger.getLogger(ZpatternsMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                // we show the output 
                
                OutputElement e = new OutputForR(re.eval("out").asStringArray());
                outputPanel.addOutput(e);
        }
        else{ // no data selected
            JOptionPane.showMessageDialog(frame, "Please select data");
        }
    }

    private void plotZPatterns() throws IOException {
            Font f = new Font("Arial", Font.PLAIN,12);
            UIManager.put("Menu.font", f);
            UIManager.put("MenuItem.font",f);
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            JMenuItem menuItem = new JMenuItem("Open");
            menuBar.add(menu);
            frameZPatterns = new JFrame();
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
            frameZPatterns.setJMenuBar(menuBar);
            panel.setSize(800,800);
            BufferedImage img = ImageIO.read(new File(tempDirR));
            ImageIcon icon = new ImageIcon(img);
            Image image = icon.getImage();
            Image newImg = image.getScaledInstance(panel.getWidth()-100, panel.getHeight()-100, Image.SCALE_SMOOTH);
            ImageIcon imageFinal = new ImageIcon(newImg);
            JLabel label = new JLabel(imageFinal);
            label.addComponentListener(new ComponentAdapter(){
                public void componentResized(ComponentEvent e){
                    JLabel label = (JLabel) e.getComponent();
                    Dimension size = label.getSize();
                    re.eval("mypath = tempdir()");
                    tempDirR = re.eval("print(mypath)").asString();
                    tempDirR += "\\out.png";
                
                    re.eval("png(base::paste(tempdir(),\"out.png\",sep=\"\\\\\"),width="+String.valueOf(size.width-100)+",height=" + String.valueOf(size.height-100) +")");
                    re.eval("png(mypath,width="+String.valueOf(size.width-100)+",height="+String.valueOf(size.height-100)+")");
                    re.eval("zCompositions::zPatterns(X,label=0," + showMeans + "," + barLabels + ")");
                    re.eval("out <- capture.output(zCompositions::zPatterns(X,label=0," + showMeans + "," + barLabels + "))");
                    re.eval("dev.off()");
                    BufferedImage img = null;
                    try {
                        img = ImageIO.read(new File(tempDirR));
                    } catch (IOException ex) {
                        Logger.getLogger(ZpatternsMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ImageIcon icon = new ImageIcon(img);
                    Image image = icon.getImage();
                    Image newImg = image.getScaledInstance(size.width-100, size.height-100, Image.SCALE_SMOOTH);
                    ImageIcon imageFinal = new ImageIcon(newImg);
                    label.setIcon(imageFinal);
                }
            });
            panel.setLayout(new GridBagLayout());
            panel.add(label);
            frameZPatterns.getContentPane().add(label);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            frameZPatterns.setSize(800,800);
            frameZPatterns.setLocation(dim.width/2-frameZPatterns.getSize().width/2, dim.height/2-frameZPatterns.getSize().height/2);
            
            WindowListener exitListener = new WindowAdapter(){
                
                @Override
                public void windowClosing(WindowEvent e){
                    int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                    if(confirm == 0){
                        frameZPatterns.dispose();
                        File file = new File(tempDirR);
                        file.delete();
                    }
                }
            };
            
            frameZPatterns.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frameZPatterns.addWindowListener(exitListener);
            frameZPatterns.setVisible(true);
    }
    
    private class quitListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            int confirm = JOptionPane.showOptionDialog(null,"Are You Sure to Close Window?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
            if(confirm == 0){
                frameZPatterns.dispose();
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
