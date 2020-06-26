/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import java.util.StringTokenizer;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import coda.gui.CoDaPackMain;


/**
 *
 * @author Guest2
 */
public abstract class AbstractMenuDialog2NumCatWithILR extends AbstractMenuDialog2NumCat{
    
    protected int [][] partitionILR = null;
    protected double [][] basisILR = null;
    JTextArea areaPart = new JTextArea(5, 5);
    JRadioButton part;
    JRadioButton base;
    public AbstractMenuDialog2NumCatWithILR(CoDaPackMain main, String text, boolean groups, boolean allowEmpty, boolean categoric){
        super(main,text, groups, allowEmpty, categoric);
        optionsPanel.add(new JLabel("Defined partition:"));
        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setPreferredSize(new java.awt.Dimension(185, 150));

        jScrollPane1.setViewportView(areaPart);
        optionsPanel.add(jScrollPane1);
        part = new JRadioButton("Partition");
        part.setSelected(true);
        base = new JRadioButton("Base");
        base.setEnabled(false);
        ButtonGroup group1 = new ButtonGroup();
        group1.add(part);
        group1.add(base);
        //optionsPanel.add(part);
        //optionsPanel.add(base);

    }
    public void setPartition(int partition[][]){
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
