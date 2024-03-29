/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thi�
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

package coda.gui.menu;

import coda.DataFrame;
import coda.gui.utils.DataSelectorGeneral;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.utils.DataFrameSelector;
import coda.io.ImportRDA;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 *
 * @author dcano98
 */
public abstract class AbstractMenuGeneral extends JDialog{
    //-- 2 selects
    int pressIndex = -1;
    int releaseIndex = -1;
    boolean groupedBy = true;
    //--
    DataSelectorGeneral ds;
    DataFrameSelector dfs;
    DataFrame df_init;
    public JPanel optionsPanel = new JPanel();;
    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    CoDaPackMain mainApplication;
    ImportRDA imp_df;
    boolean allowEmpty = false;
    String variables[];
    String yamlFile; // variable que serveix per el path del fitxer yaml
    String helpTitle; // variable per el titol del menu
    int WIDTH = 650;//560;
    int HEIGHT = 500;//430;
    
    boolean groups = false;
    boolean twoVar = false;
    public AbstractMenuGeneral(final CoDaPackMain mainApp, String title, boolean groups, boolean allowEmpty, boolean categoric){
        super(mainApp, title);
        mainApplication = mainApp;

        dfs = null;
        ds = new DataSelectorGeneral(mainApplication.getActiveDataFrame(), groups, categoric);
        initialize();
    }
    
    public AbstractMenuGeneral(final CoDaPackMain mainApp, String title, boolean groups, boolean allowEmpty){
        super(mainApp, title);
        mainApplication = mainApp;
        dfs = null;
        this.allowEmpty = allowEmpty;
        ds = new DataSelectorGeneral(mainApplication.getActiveDataFrame(), CoDaPackMain.dataList.getSelectedData(), groups);
        initialize();
    }
    public AbstractMenuGeneral(final CoDaPackMain mainApp, String title, boolean groups){
        super(mainApp, title);
        mainApplication = mainApp;
        dfs = null;
        ds = new DataSelectorGeneral(mainApplication.getActiveDataFrame(), CoDaPackMain.dataList.getSelectedData(), groups);
        initialize();
    }
    public AbstractMenuGeneral(final CoDaPackMain mainApp, String title, String categoric){
        super(mainApp, title);
        mainApplication = mainApp;
        dfs = null;
        ds = new DataSelectorGeneral(mainApplication.getActiveDataFrame(), CoDaPackMain.dataList.getSelectedData(), categoric);
        initialize();
    }
    public AbstractMenuGeneral(final CoDaPackMain mainApp, String title){
        super(mainApp, title);
        //mainApplication = mainApp;
        this.allowEmpty = true;
        //initialize();
    }
    public void setHelpMenuConfiguration(String yamlUrl, String helpTitle){
        
        this.yamlFile = Paths.get(CoDaPackConf.helpPath, yamlUrl).toString();
        this.helpTitle = helpTitle;
    }
    
    public void activeGroups(final CoDaPackMain mainApp, boolean groups2, boolean twoVar2 ,DataFrame dataFrame){
        
        groups = groups2;
        twoVar = twoVar2;
        
        mainApplication = mainApp;
        dfs = null;
        ds = new DataSelectorGeneral(mainApplication.getActiveDataFrame(), CoDaPackMain.dataList.getSelectedData(), groups2, twoVar);
        initialize();
        
    }
    
    public void activeGroups2(final CoDaPackMain mainApp, boolean groups2, boolean twoVar2 ,DataFrame dataFrame){
        
        groups = groups2;
        twoVar = twoVar2;
        
        mainApplication = mainApp;
        dfs = null;
        ds = new DataSelectorGeneral(mainApplication.getActiveDataFrame(), CoDaPackMain.dataList.getSelectedData(), groups2, twoVar);
        initialize2();
        
    }
    
    private void initialize(){

        Point p = mainApplication.getLocation();
        p.x = p.x + (mainApplication.getWidth()-520)/2;
        p.y = p.y + (mainApplication.getHeight()-430)/2;
        setLocation(p);

        setResizable(true);
        getContentPane().setLayout(new BorderLayout());
        setSize(WIDTH,HEIGHT);
        if (ds!=null) getContentPane().add(ds, BorderLayout.CENTER);
        else if (dfs!=null) getContentPane().add(dfs, BorderLayout.CENTER);

        JPanel eastPanel = new JPanel();
        optionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Options"));
        optionsPanel.setPreferredSize(new Dimension(250,200));
        //optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        //eastPanel.add(optionsPanel);
        getContentPane().add(optionsPanel, BorderLayout.EAST);

        JButton acceptButton = new JButton("Accept");
        southPanel.add(acceptButton);
        acceptButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed();
            }
        });
        JButton cancelButton = new JButton("Cancel");
        southPanel.add(cancelButton);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });
        JButton helpButton = new JButton("Help");
        southPanel.add(helpButton);
        helpButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                JDialog dialog = new JDialog();
                HelpMenu menu;
                try {
                    menu = new HelpMenu(yamlFile, helpTitle);
                    dialog.add(menu);
                    dialog.setSize(650, 500);
                    dialog.setTitle(helpTitle);
                    dialog.setIconImage(Toolkit.getDefaultToolkit()
                    .getImage(getClass().getResource(CoDaPackMain.RESOURCE_PATH + "logo.png")));
                    Point p = mainApplication.getLocation();
                    p.x = p.x + (mainApplication.getWidth()-520)/2;
                    p.y = p.y + (mainApplication.getHeight()-430)/2;
                    WindowListener exitListener = new WindowAdapter(){

                        @Override
                        public void windowClosing(WindowEvent e){
                                dialog.dispose();
                                menu.deleteHtml();
                        }
                    };

                    dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    dialog.addWindowListener(exitListener);
                    dialog.setLocation(p);
                    dialog.setVisible(true);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AbstractMenuDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        getContentPane().add(southPanel, BorderLayout.SOUTH);

    }
    
    private void initialize2(){

        Point p = mainApplication.getLocation();
        p.x = p.x + (mainApplication.getWidth()-520)/2;
        p.y = p.y + (mainApplication.getHeight()-430)/2;
        setLocation(p);

        setResizable(true);
        getContentPane().setLayout(new BorderLayout());
        setSize(WIDTH,HEIGHT);
        if (ds!=null) getContentPane().add(ds, BorderLayout.CENTER);
        else if (dfs!=null) getContentPane().add(dfs, BorderLayout.CENTER);

        JPanel eastPanel = new JPanel();
        optionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Options"));
        optionsPanel.setPreferredSize(new Dimension(250,200));
        getContentPane().add(optionsPanel, BorderLayout.EAST);

        
        JButton helpButton = new JButton("Help");
        southPanel.add(helpButton);
        helpButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                JDialog dialog = new JDialog();
                HelpMenu menu;
                try {
                    menu = new HelpMenu(yamlFile, helpTitle);
                    dialog.add(menu);
                    dialog.setSize(650, 500);
                    dialog.setTitle(helpTitle);
                    dialog.setIconImage(Toolkit.getDefaultToolkit()
                    .getImage(getClass().getResource(CoDaPackMain.RESOURCE_PATH + "logo.png")));
                    Point p = mainApplication.getLocation();
                    p.x = p.x + (mainApplication.getWidth()-520)/2;
                    p.y = p.y + (mainApplication.getHeight()-430)/2;
                    WindowListener exitListener = new WindowAdapter(){

                        @Override
                        public void windowClosing(WindowEvent e){
                                dialog.dispose();
                                menu.deleteHtml();
                        }
                    };

                    dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    dialog.addWindowListener(exitListener);
                    dialog.setLocation(p);
                    dialog.setVisible(true);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AbstractMenuDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        getContentPane().add(southPanel, BorderLayout.SOUTH);

    }
    @Override
    public void setVisible(boolean v){
        if(mainApplication.getActiveDataFrame() == null && !allowEmpty){
            JOptionPane.showMessageDialog(this, "No data available");
            this.dispose();
        }else{
            super.setVisible(v);
        }
    }
    public void setVisible(boolean v, boolean b){
        if (b==true && v==true) super.setVisible(v);
        else if (b==false && v==false) super.setVisible(v);
    }
    public abstract void acceptButtonActionPerformed();
    public DataFrameSelector getDFS() {
        return dfs;
    }
    
    public void setSelectedDataName(String data1){
        ds.setSelectedName(data1);
    }

    public boolean[] getValidComposition(DataFrame df, String[] selectedNames){
        boolean selection[] = df.getValidCompositions(selectedNames);
        String invalid = "";
        int count = 0;
        for(int i=0;i<selection.length;i++){
            if(!selection[i]){
                if(count == 5){
                    count = 0;
                    invalid += "<br>";
                }
                invalid += Integer.toString(i+1) + ", ";
                count++;
            }

        }
        int len = invalid.length();
        if(len != 0)JOptionPane.showMessageDialog(this,
                    "<html><p>Rows number <center><i>" + invalid.substring(0,len-2) +
                    "</i></center>are going to be ignored due to zero <br>"
                    + "data in some of its components.</p></html>");

        return selection;        
    }
    public boolean[] getValidData(DataFrame df, String[] selectedNames){
        boolean selection[] = df.getValidData(selectedNames);
        String invalid = "";
        int count = 0;
        for(int i=0;i<selection.length;i++){
            if(!selection[i]){
                if(count == 5){
                    count = 0;
                    invalid += "<br>";
                }
                invalid += Integer.toString(i+1) + ", ";
                count++;
            }

        }
        int len = invalid.length();
        if(len != 0)JOptionPane.showMessageDialog(this,
                    "<html><p>Rows number <center><i>" + invalid.substring(0,len-2) +
                    "</i></center>are going to be ignored due to<br>"
                    + "missing data in some of its components.</p></html>");

        return selection;
    }

    public void closeMenuDialog(){        
        setVisible(false);
    }
    public DataSelectorGeneral getDataSelector(){
        return ds;
    }
    public String[] getSelectedData(){
        return ds.getSelectedData();
    }
    public ImportRDA getRDA() {
        return imp_df;
    }
    
    
}


