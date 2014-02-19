/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.utils;

import coda.gui.menu.*;
import coda.DataFrame;
import coda.gui.CoDaPackMain;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author mcomas
 */
public class AddDataManually extends CoDaPackDialog{
    public static final long serialVersionUID = 1L;
    
    JTextField textNames;
    JTextArea textData;
    public double [][] data = null;
    public AddDataManually(final Container mainApp, double data[][]){

       
        setLocationRelativeTo(mainApp);
        this.data = data;
        /*Point p = mainApp.getLocation();
        p.x = p.x + (mainApp.getWidth()-520)/2;
        p.y = p.y + (mainApp.getHeight()-430)/2;
        setLocation(p);*/

        commonInititalitzation();
    }
    public AddDataManually(final Container mainApp, double data[][], String names[]){
        

        setSize(500,370);
        setLocationRelativeTo(mainApp);
        this.data = data;
        /*Point p = mainApp.getLocation();
        p.x = p.x + (mainApp.getWidth()-520)/2;
        p.y = p.y + (mainApp.getHeight()-430)/2;
        setLocation(p);*/
                
        commonInititalitzation();
        
        String text = "";
        for(int i=0;i<names.length;i++)
            text += names[i] + " ";
        
        textNames.setText(text);
        textNames.setEnabled(false);
    }
    public void commonInititalitzation(){
        

        setTitle("Add data manually");
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
                getData(names.length);
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
    public boolean getData(int ncol){
        StringTokenizer tok = new StringTokenizer(textData.getText());
        
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
            return true;
        }else{
            return false;
        }
    }
}
