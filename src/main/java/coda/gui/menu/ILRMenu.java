/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.CoDaStats;
import coda.gui.CoDaPackMain;
import coda.gui.utils.BinaryPartitionSelect;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.FlowLayout;
import java.util.StringTokenizer;
import javax.swing.ButtonGroup;
import javax.swing.JButton;

/**
 *
 * @author Guest2
 */
public class ILRMenu extends JDialog{
    protected int [][] partitionILR = null;
    protected double [][] basisILR = null;
    JTextArea areaPart = new JTextArea(5,5);
    JRadioButton part;
    JRadioButton base;
    JPanel panel = new JPanel();
    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    int WIDTH = 400;
    int HEIGHT = 300;
    String[] dsSelected;
    
    public ILRMenu(String[] selectedData){
        this.dsSelected = selectedData;
        inicialize();
        
        panel.add(new JLabel("Defined partition:"));
        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setPreferredSize(new Dimension(185,150));
        
        jScrollPane1.setViewportView(areaPart);
        panel.add(jScrollPane1);
        part = new JRadioButton("Partition");
        part.setSelected(true);
        base = new JRadioButton("Base");
        base.setEnabled(false);
        ButtonGroup group1 = new ButtonGroup();
        group1.add(part);
        group1.add(base);
        
        JButton defaultPart = new JButton("Default Partition");
        panel.add(defaultPart);
        defaultPart.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setPartition(CoDaStats.defaultPartition(getSelectedDs().length));
            }
        });

        JButton manuallyPart = new JButton("Define Manually");
        panel.add(manuallyPart);
        manuallyPart.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initiatePartitionMenu();
            }
        });
        
    }
    
    private String[] getSelectedDs(){
        return this.dsSelected;
    }
    
    public void setPartition(int[][] partition){
        String spart = "";
        for(short i=0;i<partition.length;i++){
            for(short j=0;j<partition[0].length;j++){
                if(partition[i][j] == -1) spart += " " + partition[i][j];
                else spart += "  " + partition[i][j];
            }
            spart += "\n";
        }
        areaPart.setText(spart);
        //partitionILR = partition;
    }
    
    public void initiatePartitionMenu(){
        BinaryPartitionSelect binaryMenu = new BinaryPartitionSelect(this, this.dsSelected);
        binaryMenu.setVisible(true);
    }
    
    private void inicialize(){
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-WIDTH/2, dim.height/2-HEIGHT/2);
        
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        setSize(WIDTH,HEIGHT);
        
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("ILR Configuration"));
        panel.setPreferredSize(new Dimension(300,200));
        
        getContentPane().add(panel);
        
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
                System.out.println("He apretat el boto help");
            }
        });
        getContentPane().add(southPanel, BorderLayout.SOUTH);
    }
    
    public void acceptButtonActionPerformed(){
        this.dispose();
    }
    
    public int getDsLength(){
        return this.dsSelected.length;
    }
    
    public double [][]  getBasis(){
        StringTokenizer tok = new StringTokenizer(areaPart.getText());
        int e = tok.countTokens();
        int n = ( 1 + (int)Math.sqrt(1 + 4 * e+0.01) ) / 2;
        if( n*n - n == e){
            basisILR = new double[n-1][n];
            for(int i=0;i<n-1;i++){
                for(int j=0;j<n;j++){
                    basisILR[i][j] = Double.valueOf(tok.nextToken());
                }
            }
            while(tok.hasMoreTokens()){
                String item = tok.nextToken();

            }
            return basisILR;
        }else{
            return null;
        }
        
    }
    
    public int [][]  getPartition(){
        StringTokenizer tok = new StringTokenizer(areaPart.getText());
        int e = tok.countTokens();
        int n = ( 1 + (int)Math.sqrt(1 + 4 * e+0.01) ) / 2;
        if( n*n - n == e){
            partitionILR = new int[n-1][n];
            for(int i=0;i<n-1;i++){
                for(int j=0;j<n;j++){
                    partitionILR[i][j] = Integer.valueOf(tok.nextToken());
                }
            }
            while(tok.hasMoreTokens()){
                String item = tok.nextToken();

            }
            return partitionILR;
        }else{
            return null;
        }
        
    }
}
