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

import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.plot.CoDaDisplayConfiguration;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author marc
 */
public class ConfigurationMenu extends CoDaPackDialog{
    CoDaPackMain window;
    JTextField decimal;
    JTextField output;
    JTextField table;
    JTextField export;
    JTextField closure;
    JCheckBox menuDevelopment;
    
    public ConfigurationMenu(CoDaPackMain window){
        super(window, "Configuration Menu");

        JPanel panel = new JPanel();

        //shape.setSize(3f);
        this.window = window;
        setSize(560,430);
        setLocationRelativeTo(window);

        JLabel label1 = new JLabel("Decimal character");
        decimal = new JTextField(10);
        decimal.setText(String.valueOf(CoDaPackConf.getDecimalFormat()));
        add(label1);
        add(decimal);

        JLabel label2 = new JLabel("Output format");
        output = new JTextField(10);
        output.setText(CoDaPackConf.decimalOutputFormat);
        add(label2);
        add(output);

        JLabel label3 = new JLabel("Table format");
        table = new JTextField(10);
        table.setText(CoDaPackConf.decimalTableFormat);
        add(label3);
        add(table);
        
        JLabel label4 = new JLabel("Exportation format");
        export = new JTextField(10);
        export.setText(CoDaPackConf.decimalExportFormat);
        add(label4);
        add(export);
        
        JLabel label5 = new JLabel("Closure To");
        closure = new JTextField(10);
        closure.setText(CoDaPackConf.closureTo);
        add(label5);
        add(closure);
        
        JLabel label6 = new JLabel("Show Dev Menu");
        menuDevelopment = new JCheckBox();
        menuDevelopment.setSelected(CoDaPackConf.showDev);
        add(label6);
        add(menuDevelopment);
        

        panel.add(label1);
        panel.add(decimal);
        panel.add(label2);
        panel.add(output);
        panel.add(label3);
        panel.add(table);
        panel.add(label4);
        panel.add(export);
        panel.add(label5);
        panel.add(closure);
        panel.add(label6);
        panel.add(menuDevelopment);
        
        JPanel south = new JPanel();
        JButton apply = new JButton("Apply");
        apply.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                CoDaPackConf.setDecimalTableFormat(table.getText());
                CoDaPackConf.setDecimalFormat(decimal.getText().charAt(0));
                CoDaPackConf.setDecimalOutputFormat(output.getText());
                CoDaPackConf.setDecimalExportFormat(export.getText());
                CoDaPackConf.setClosureTo(closure.getText());
                CoDaPackConf.setShowDev(menuDevelopment.isSelected());
                if(CoDaPackConf.showDev) CoDaPackMain.jMenuBar.activeDevMenu();
                else CoDaPackMain.jMenuBar.disableDevMenu();
                CoDaPackMain.tablePanel.updateUI();
            }
        });
        south.add(apply);

        JButton saveDefault = new JButton("Save as default");
        saveDefault.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                CoDaPackConf.saveConfiguration();
                CoDaPackMain.tablePanel.updateUI();
            }
        });
        south.add(saveDefault);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(south, BorderLayout.SOUTH);
    }
}
