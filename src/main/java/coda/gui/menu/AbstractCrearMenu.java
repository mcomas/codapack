/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package coda.gui.menu;

import coda.gui.CoDaPackMain;
import coda.gui.utils.DataFrameSelector;
import coda.gui.utils.DataSelector2NumOCatNumOCat;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author dcano
 */
public abstract class AbstractCrearMenu extends JDialog{
    //final DataSelector2NumOCatNumOCat ds;
    CoDaPackMain mainApplication;
    
    DataFrameSelector dfs;
    public JPanel optionsPanel = new JPanel();
    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    
    int WIDTH = 650;//560;
    int HEIGHT = 500;//430;
    public AbstractCrearMenu(final CoDaPackMain mainApp, String title){
        super(mainApp, title);
        mainApplication = mainApp;

        dfs = null;
        //ds = new DataSelector2NumOCatNumOCat(mainApplication.getActiveDataFrame(), groups, categoric);
        initialize();
    }
    
    private void initialize(){
        Point p = mainApplication.getLocation();
        p.x = p.x + (mainApplication.getWidth()-520)/2;
        p.y = p.y + (mainApplication.getHeight()-430)/2;
        setLocation(p);

        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        setSize(WIDTH,HEIGHT);
        if (dfs!=null) getContentPane().add(dfs, BorderLayout.CENTER);

        JPanel eastPanel = new JPanel();
        optionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Options"));
        optionsPanel.setPreferredSize(new Dimension(WIDTH-20,200));
        //optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        eastPanel.add(optionsPanel);
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
        getContentPane().add(southPanel, BorderLayout.SOUTH);
    }
    @Override
    public void setVisible(boolean v){
        if(mainApplication.getActiveDataFrame() == null ){
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
}