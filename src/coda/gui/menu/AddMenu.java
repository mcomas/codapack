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

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import java.awt.BorderLayout;
import java.awt.Point;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author mcomas
 */
public class AddMenu extends CoDaPackDialog{
    public static final long serialVersionUID = 1L;
    DataFrame df;
    JTextField textNames;
    JTextArea textData;
    public AddMenu(final CoDaPackMain mainApp){
        super(mainApp, "Add numeric variables");
        Point p = mainApp.getLocation();
        p.x = p.x + (mainApp.getWidth()-520)/2;
        p.y = p.y + (mainApp.getHeight()-430)/2;
        setLocation(p);
        
        setSize(500,370);
        df = mainApp.getActiveDataFrame();
       
        getContentPane().setLayout(new BorderLayout());

        JPanel north = new JPanel();
        north.setLayout(new BorderLayout());
        north.add(new JLabel(" Variable names "), BorderLayout.WEST);
        textNames = new JTextField();
        north.add(textNames, BorderLayout.CENTER);
        getContentPane().add(north, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        center.add(new JLabel(" Data "), BorderLayout.NORTH);
        textData = new JTextArea(5, 5);
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(textData);
        center.add(scroll, BorderLayout.CENTER);
        getContentPane().add(center, BorderLayout.CENTER);

        JPanel south = new JPanel();
        JButton accept = new JButton("Accept");
        south.add(accept);
        getContentPane().add(south, BorderLayout.SOUTH);
        accept.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String names[] = getNames();
                double[][] data = getData(names.length);

                if(df == null){
                    df = new DataFrame();
                    mainApp.addDataFrame(df);
                }
                df.addData(names, data);
                mainApp.updateDataFrame(df);
                setVisible(false);
            }
            
        });
    }
    public String[] getNames(){
        StringTokenizer tok = new StringTokenizer(textNames.getText());
        int size = tok.countTokens();
        String names[] = new String[size];
        int i = 0;
        while( tok.hasMoreTokens()){
            names[i++] = tok.nextToken();
        }
        return names;
    }
    public double[][] getData(int ncol){
        StringTokenizer tok = new StringTokenizer(textData.getText());
        double[][] data;
        int e = tok.countTokens();        
        if( e % ncol == 0 ){
            int nrow = e / ncol;
            data = new double[ncol][nrow];
            for(int i=0;i<nrow;i++){
                for(int j=0;j<ncol;j++){
                    String str = tok.nextToken();
                    str = str.replace(",", ".");
                    data[j][i] = Double.valueOf(str);
                }
            }
            while(tok.hasMoreTokens()){
                String item = tok.nextToken();
            }
            return data;
        }else{
            return null;
        }
    }
}
