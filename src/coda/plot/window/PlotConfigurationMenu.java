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
import javax.swing.border.BevelBorder;

/**
 *
 * @author marc
 */
public class PlotConfigurationMenu extends JDialog{
    JComboBox comboItems;
    ColorPanel selectedColor;
    
    String itemName[];
    Color color[];
    float size[];

    CoDaPlotWindow window;
    int actualItem;
    public PlotConfigurationMenu(final CoDaPlotWindow window){
        super(window, "Configuration menu");
        setLocationRelativeTo(null);
        this.window = window;
        
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        setSize(360,230);
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel data = new JPanel();
        data.setLayout(new BoxLayout(data, BoxLayout.PAGE_AXIS));
        tabbedPane.addTab("Data", data);        

        Set<String> items = CoDaDisplayConfiguration.getColorKeySet();

        int N = items.size();
        itemName = items.toArray(new String[N]);
        Arrays.sort(itemName, String.CASE_INSENSITIVE_ORDER);

        color = new Color[N];
        size = new float[N];
        for(int i=0;i<N;i++){
            color[i] = CoDaDisplayConfiguration.getColor(itemName[i]);
        }

        JPanel selectProperty = new JPanel();
        selectProperty.setSize(new Dimension(350,50));
        selectProperty.setPreferredSize(new Dimension(350,50));
        comboItems = new JComboBox(itemName);
        comboItems.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                actualItem = cb.getSelectedIndex();
                selectedColor.setBackground(color[actualItem]);
            }
        });
        selectProperty.add(comboItems);
        data.add(selectProperty);

        JPanel propertyPanel = new JPanel();
        propertyPanel.setSize(new Dimension(350,150));
        propertyPanel.setPreferredSize(new Dimension(350,150));
        selectedColor = new ColorPanel();
        selectedColor.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        selectedColor.addMouseListener(selectedColor);
        actualItem = 0;
        selectedColor.setBackground(color[actualItem]);
        selectedColor.setPreferredSize(new Dimension(50,20));
        propertyPanel.add(new JLabel("Color:"));
        propertyPanel.add(selectedColor);

        data.add(propertyPanel);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        JPanel south = new JPanel();


        JButton apply = new JButton("Apply");
        apply.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                for(int i=0;i<color.length;i++){
                    CoDaDisplayConfiguration.setColor(itemName[i],color[i]);
                }
                window.repaint();
            }
        });
        south.add(apply);

        JButton saveDefault = new JButton("Save as default");
        saveDefault.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                CoDaDisplayConfiguration.saveConfiguration();
            }
        });
        south.add(saveDefault);

        JButton gDefault = new JButton("Default");
        gDefault.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                CoDaDisplayConfiguration.loadConfiguration();
                Set<String> items = CoDaDisplayConfiguration.getColorKeySet();

                int N = items.size();
                for(int i=0;i<N;i++){
                    color[i] = CoDaDisplayConfiguration.getColor(itemName[i]);
                }
                selectedColor.setBackground(color[actualItem]);
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
              color[actualItem] = colorSelected;
              selectedColor.setBackground(colorSelected);
            }
        }
        public void mousePressed(MouseEvent me) { }

        public void mouseReleased(MouseEvent me) { }

        public void mouseEntered(MouseEvent me) { }

        public void mouseExited(MouseEvent me) { }

    }
}
