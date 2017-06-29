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

/*
 * WindowTernaryPlot.java
 *
 * Created on Sep 9, 2010, 12:11:02 PM
 */

package coda.plot.window;

import coda.DataFrame;
import coda.Variable;
import coda.ext.eps.ColorMode;
import coda.ext.eps.EpsGraphics;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.menu.SelectVariableMenu;
import coda.gui.utils.FileNameExtensionFilter;
import coda.plot.AbstractCoDaDisplay;
import coda.plot.CoDaDisplayConfiguration;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//import org.freehep.graphicsio.latex.LatexGraphics2D;

/**
 *
 * @author marc
 */
public class CoDaPlotWindow extends javax.swing.JFrame{
    public static final long serialVersionUID = 1L;
    
    protected AbstractCoDaDisplay display;
    protected JPanel commonControls = new JPanel();
    protected JPanel particularControls1 = new JPanel();
    private JLabel jLabel1 = new JLabel("Zoom");
    private JSlider sliderZoom = new JSlider();
    private String title;
    //private JButton confButton = new JButton("Configure");
    //private JButton saveButton = new JButton("Save..");
    static public JFileChooser fc = new JFileChooser();
    static{
        fc.setFileFilter(new FileNameExtensionFilter("EPS file", "eps"));
        //fc.setFileFilter(new FileNameExtensionFilter("LaTeX file", "tex"));
        fc.setFileFilter(new FileNameExtensionFilter("PNG file", "png"));
        fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
        fc.setFileFilter(new FileNameExtensionFilter("Bitmap file", "bmp"));
        fc.setFileFilter(new FileNameExtensionFilter("JPEG file", "jpg"));
        //fc.setFileFilter(new FileNameExtensionFilter("PostScript file", "eps"));
    }
    protected JMenuBar jMenuBar;
        protected JMenu menuFile;
        private final String ITEM_FILE = "File";
            private JMenuItem itemImage;
            private final String ITEM_IMAGE = "Snapshot...";
            private JMenuItem itemConf;
            private final String ITEM_CONF = "Configuration...";
            private JMenuItem itemQuit;
            private final String ITEM_QUIT = "Quit Plot Window";
        protected JMenu menuData;
        protected final String ITEM_DATA = "Data";
            private final String ITEM_OBS_NAME = "Add observation names...";
            protected JMenuItem itemObsName;
            private final String ITEM_SHOW_NAMES = "Show observation names";
            protected JCheckBoxMenuItem itemShowObsName;
    DataFrame dataframe;
    public CoDaPlotWindow(DataFrame dataframe, final AbstractCoDaDisplay plotDisplay, String title) {
        
        this.dataframe = dataframe;
        jMenuBar = new JMenuBar();
        menuFile = new JMenu();
        menuFile.setText(ITEM_FILE);
        itemImage = new JMenuItem();
        itemImage.setText(ITEM_IMAGE);
        itemConf = new JMenuItem();
        itemConf.setText(ITEM_CONF);
        itemQuit = new JMenuItem();
        itemQuit.setText(ITEM_QUIT);
        menuData = new JMenu();
        menuData.setText(ITEM_DATA);
        itemObsName = new JMenuItem();
        itemObsName.setText(ITEM_OBS_NAME);
        itemObsName.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectingNames();
            }
        });
        itemShowObsName = new JCheckBoxMenuItem();
        itemShowObsName.setText(ITEM_SHOW_NAMES);
        itemShowObsName.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                display.showAllData(itemShowObsName.isSelected());
                display.repaint();
            }
        });

        this.setIconImage(Toolkit.getDefaultToolkit()
                .getImage(getClass().getResource(CoDaPackConf.RESOURCE_PATH + "logo.png")));

        menuFile.add(itemImage);
        menuFile.add(itemConf);
        menuFile.add(itemQuit);
        jMenuBar.add(menuFile);

        menuData.add(itemObsName);
        menuData.add(itemShowObsName);
        if(dataframe == null)
            itemObsName.setEnabled(false);
        jMenuBar.add(menuData);

        setJMenuBar(jMenuBar);
        this.title = title;
        setTitle(title);
        setPreferredSize(new Dimension(800, 700));
        display = plotDisplay;
        getContentPane().add(display);

        commonControls.setLayout(new BoxLayout(commonControls, BoxLayout.LINE_AXIS));
        commonControls.setPreferredSize(new Dimension(60,60));
        commonControls.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        particularControls1.setLayout(new BoxLayout(particularControls1, BoxLayout.LINE_AXIS));


        JPanel southPane = new JPanel();
        southPane.setPreferredSize(new Dimension(200,60));
        southPane.setLayout(new BoxLayout(southPane, BoxLayout.LINE_AXIS));
        southPane.add( commonControls );
        southPane.add( particularControls1 );
        
        //General controls
        JPanel zoomPane = new JPanel();
        zoomPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        zoomPane.add(jLabel1);
        sliderZoom.setPreferredSize(new Dimension(100,40));
        zoomPane.add(sliderZoom);
        
        commonControls.add(zoomPane);

        sliderZoom.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                sliderZoomMouseReleased(evt);
            }
        });
        sliderZoom.addChangeListener(new ChangeListener() {
            
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderZoomStateChanged(evt);
            }
        });

        getContentPane().add(southPane, java.awt.BorderLayout.SOUTH);
        
        itemConf.addActionListener(new ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initiateConfigurationMenu();

            }
        });
        itemImage.addActionListener(new ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveImage();
            }
        });
        itemQuit.addActionListener(new ActionListener(){
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setVisible(false);
                dispose();                
            }
        });
        pack();
    }
    public void selectingNames(){
        SelectVariableMenu svm = new SelectVariableMenu(this, dataframe);
        svm.setVisible(true);
        Variable var = dataframe.get(svm.selectedVariable);
        svm.dispose();
        display.setObservationNames(var.getTextData());
        display.repaint();
    }
    private void sliderZoomMouseReleased(java.awt.event.MouseEvent evt) {
        sliderZoom.setValue(50);
    }
    private void sliderZoomStateChanged(javax.swing.event.ChangeEvent evt) {
        double dif = ((double)sliderZoom.getValue()-50)/3000.0;
        display.zoom(1+dif);
        display.repaint();
    }
    public void initiateConfigurationMenu(){
        PlotConfigurationMenu menu = new PlotConfigurationMenu(this, display.config);
        menu.setVisible(true);
    }
    public void saveImage(){
        if( fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
            FileNameExtensionFilter ff = (FileNameExtensionFilter)fc.getFileFilter();
            File file = fc.getSelectedFile();
            String file_name = file.getAbsolutePath();
            String extension = ff.getExtensions()[0];
            if( ! file_name.endsWith("." + extension) ){
                file_name += "." + extension;
            }
            try {
                if(extension.equalsIgnoreCase("eps")){
                    int width = display.getWidth();
                    int height = display.getHeight();

                    EpsGraphics g =
				new EpsGraphics(
					title,
					//(OutputStream)(new FileOutputStream(file)), 0,0,width ,height , ColorMode.COLOR_RGB);
                    (OutputStream)(new FileOutputStream(new File(file_name))), 0,0,width ,height , ColorMode.COLOR_RGB);
                    display.paintComponent(g);//, width, height);
                    g.close();
                }/*else if(extension.equalsIgnoreCase("tex")){
                    int width = display.getWidth();
                    int height = display.getHeight();

                    LatexGraphics2D g =
                        new LatexGraphics2D(new File(file_name), new Dimension(width ,height));
                        display.paintComponent(g, width, height);
                        g.dispose();
                }*/else{
                    int width = display.getWidth();
                    int height = display.getHeight();
                    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    Graphics g = image.getGraphics();
                    Graphics2D graphics = (Graphics2D) g;
                    display.paintComponent(graphics);//, width, height);
                    ImageIO.write(image, extension, new File(file_name));
                    graphics.dispose();
                    image.flush();
                }
                
            } catch (IOException ex) {
                Logger.getLogger(CoDaPlotWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
    }
    public class ExtensionFileFilter implements FileFilter{
        String extension;
        public ExtensionFileFilter(String extension){
            super();
            this.extension = extension;
        }
        public boolean accept(File file) {
            return (file.getName().toLowerCase().endsWith("." + extension) ? true:false );
        }
        public String getExtension() {
            return extension;
        }
    }
}
