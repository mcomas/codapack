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

package coda.gui.menu;

import coda.gui.CoDaPackMain;
import java.util.StringTokenizer;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**
 *
 * @author mcomas
 */
public abstract class AbstractMenuDialogWithILR extends AbstractMenuDialog{
    protected int [][] partitionILR = null;
    protected double [][] basisILR = null;
    JTextArea areaPart = new JTextArea(5, 5);
    JRadioButton part;
    JRadioButton base;
    public AbstractMenuDialogWithILR(CoDaPackMain main, String text, boolean groups){
        super(main,text, groups);
        optionsPanel.add(new JLabel("Defined base:"));
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
        optionsPanel.add(part);
        optionsPanel.add(base);

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
