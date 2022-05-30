/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import coda.gui.OutputPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.Dimension;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

/**
 *
 * @author dcano
 */
public class HTMLTabs {
    public static void main(String[] args){
        new HTMLTabs();
    }
    
    public HTMLTabs(){
        JFrame window = new JFrame("HTML TABS");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(800,600);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFocusable(false);
        tabbedPane.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override
            public void mousePressed( java.awt.event.MouseEvent evt){
                if(SwingUtilities.isRightMouseButton(evt)){
                    int index = tabbedPane.getSelectedIndex();
                    
                    OutputPanel outputPanel = new OutputPanel();
                    tabbedPane.add(outputPanel);
                
                    if(index != 0)
                    {
                        JPopupMenu popupMenu = new JPopupMenu();
                        JMenuItem delete = new JMenuItem("DELETE");
                        delete.addActionListener(new java.awt.event.ActionListener(){
                           @Override
                           public void actionPerformed(java.awt.event.ActionEvent evt){
                               tabbedPane.remove(index);
                           }
                        });
                        popupMenu.add(delete);
                        popupMenu.show(window, evt.getX(), evt.getY());
                    }
                }
            }
        });
        
        //button
        JButton addButton = new JButton("+");
        addButton.setBorder(null);
        addButton.setFocusPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setPreferredSize(new Dimension(30,30));
        addButton.addActionListener(new java.awt.event.ActionListener(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt){
                JLabel tabTitleLabel = new JLabel("TAB");
                JTextArea textArea = new JTextArea();
                
                tabbedPane.addTab("Tab", textArea);
                tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1,tabTitleLabel);
            }
        });
        
        tabbedPane.addTab("",null);
        
        tabbedPane.setTabComponentAt(0,addButton);
        
        
        window.add(tabbedPane);
        window.setVisible(true);
    }
}
