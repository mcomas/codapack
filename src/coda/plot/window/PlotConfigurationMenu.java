/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.plot.window;

import coda.plot.CoDaDisplayConfiguration;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

/**
 *
 * @author marc
 */
public class PlotConfigurationMenu extends JDialog{
    JComboBox comboColorItems;
    JComboBox comboSizeItems;
    
    ColorPanel selectedColor;
    JTextField selectedSize;
    
    String itemColorName[];
    String itemSizeName[];
    
    Color color[];
    float size[];

    CoDaPlotWindow window;
    int actualColorItem;
    int actualSizeItem;
    CoDaDisplayConfiguration config;
    
    public PlotConfigurationMenu(final CoDaPlotWindow window, final CoDaDisplayConfiguration config){        
        super(window, "Configuration menu");
        setLocationRelativeTo(window);
        this.window = window;
        this.config = config;
        
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        setSize(360,230);
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.PAGE_AXIS));
        tabbedPane.addTab("Colors", colorPanel);        

        JPanel sizePanel = new JPanel();
        colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.PAGE_AXIS));
        tabbedPane.addTab("Sizes", sizePanel);  
        
        Set<String> colorItems = config.color.keySet();
        int N_color = colorItems.size();
        itemColorName = colorItems.toArray(new String[N_color]);
        Arrays.sort(itemColorName, String.CASE_INSENSITIVE_ORDER);
        
        Set<String> sizeItems = config.size.keySet();
        int N_size = sizeItems.size();
        itemSizeName = sizeItems.toArray(new String[N_size]);
        Arrays.sort(itemSizeName, String.CASE_INSENSITIVE_ORDER);
        
        color = new Color[N_color];
        size = new float[N_size];
        
        for(int i=0;i<N_color;i++){
            color[i] = config.getColor(itemColorName[i]);
        }
        for(int i=0;i<N_size;i++){
            size[i] = config.getSize(itemSizeName[i]);
        }
        /*
         * Color panel
         */
        JPanel selectColorProperty = new JPanel();
        selectColorProperty.setSize(new Dimension(350,50));
        selectColorProperty.setPreferredSize(new Dimension(350,50));
        comboColorItems = new JComboBox(itemColorName);
        comboColorItems.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                actualColorItem = cb.getSelectedIndex();
                selectedColor.setBackground(color[actualColorItem]);
            }
        });
        selectColorProperty.add(comboColorItems);
        colorPanel.add(selectColorProperty);

        JPanel colorPropertyPanel = new JPanel();
        colorPropertyPanel.setSize(new Dimension(350,150));
        colorPropertyPanel.setPreferredSize(new Dimension(350,150));
        selectedColor = new ColorPanel();
        selectedColor.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        selectedColor.addMouseListener(selectedColor);
        actualColorItem = 0;
        selectedColor.setBackground(color[actualColorItem]);
        selectedColor.setPreferredSize(new Dimension(50,20));
        colorPropertyPanel.add(new JLabel("Color:"));
        colorPropertyPanel.add(selectedColor);
        colorPanel.add(colorPropertyPanel);
        
        /*
         * Size Panel
         */
        JPanel selectSizeProperty = new JPanel();
        selectSizeProperty.setSize(new Dimension(350,50));
        selectSizeProperty.setPreferredSize(new Dimension(350,50));
        comboSizeItems = new JComboBox(itemSizeName);
        comboSizeItems.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                actualSizeItem = cb.getSelectedIndex();
                selectedSize.setText(Float.toString(size[actualSizeItem]));
            }
        });
        selectSizeProperty.add(comboSizeItems);
        sizePanel.add(selectSizeProperty);

        JPanel sizePropertyPanel = new JPanel();
        JButton setSize = new JButton("set");
        sizePropertyPanel.setSize(new Dimension(350,150));
        sizePropertyPanel.setPreferredSize(new Dimension(350,150));
        selectedSize = new JTextField(10);
        actualSizeItem = 0;
        selectedSize.setText(Float.toString(size[actualSizeItem]));
        setSize.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try{
                    float value = Float.parseFloat(selectedSize.getText());
                    size[actualSizeItem] = value;
                }catch(NumberFormatException err){ }
            }
        });
        
        sizePropertyPanel.add(selectedSize);
        sizePropertyPanel.add(setSize);
        sizePanel.add(sizePropertyPanel);
        
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        JPanel south = new JPanel();


        JButton apply = new JButton("Apply");
        apply.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                for(int i=0;i<color.length;i++){
                    config.setColor(itemColorName[i],color[i]);
                }
                for(int i=0;i<size.length;i++){
                    config.setSize(itemSizeName[i],size[i]);
                }
                window.repaint();
            }
        });
        south.add(apply);

        JButton saveDefault = new JButton("Save as default");
        saveDefault.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                config.saveConfiguration();
            }
        });
        south.add(saveDefault);

        JButton gDefault = new JButton("Default");
        gDefault.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                config.loadConfiguration();
                
                Set<String> colorItems = config.color.keySet();
                int N_color = colorItems.size();
                for(int i=0;i<N_color;i++){
                    color[i] = config.getColor(itemColorName[i]);
                }
                selectedColor.setBackground(color[actualColorItem]);
                
                Set<String> sizeItems = config.size.keySet();
                int N_size = sizeItems.size();
                for(int i=0;i<N_size;i++){
                    size[i] = config.getSize(itemSizeName[i]);
                }
                selectedSize.setText(Float.toString(size[actualSizeItem]));
            }
        });
        south.add(gDefault);

        getContentPane().add(south, BorderLayout.SOUTH);

    }
    private class ColorPanel extends JPanel implements MouseListener{
        public void mouseClicked(MouseEvent me) {
            Color initialBackground = selectedColor.getBackground();
            Color colorSelected = JColorChooser.showDialog(null,
                "Choose a color", initialBackground);
            if (colorSelected != null) {
              color[actualColorItem] = colorSelected;
              selectedColor.setBackground(colorSelected);
            }
        }
        public void mousePressed(MouseEvent me) { }

        public void mouseReleased(MouseEvent me) { }

        public void mouseEntered(MouseEvent me) { }

        public void mouseExited(MouseEvent me) { }

    }
}
