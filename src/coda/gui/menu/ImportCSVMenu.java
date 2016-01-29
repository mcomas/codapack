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

/*
 * ImportMenu.java
 *
 * Created on 28/12/2010, 16:14:16
 */

package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.io.ImportData;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

/**
 *
 * @author mcomas
 */
public class ImportCSVMenu extends JDialog {
    JFileChooser chooseFile;
    DataFrame dataFrame;    
    
    JTextField absolutePath;
    JTextField dfname;
            
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JTextField separator;
    private javax.swing.JTextField naData;
    private javax.swing.JTextField ndData;
    private javax.swing.JPanel spreadsheet1;
    
    
    public ImportCSVMenu(final CoDaPackMain mainApp, boolean modal, JFileChooser chooseFile) {
        super(mainApp, modal);
        
        this.chooseFile = chooseFile;
        
        initComponents();

        setLocationRelativeTo(mainApp);  
        
    }
    public DataFrame getDataFrame(){
        return dataFrame;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        JButton okButton = new JButton();
        
        okButton.requestFocusInWindow();
        
        JButton cancelButton = new JButton();
        
        absolutePath = new JTextField();
        absolutePath.setText(chooseFile.getSelectedFile().getAbsolutePath());
        
        JButton fileButton = new JButton();
        fileButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt) {
                fileButtonActionPerformed(evt);
            }
        });
        
        dfname = new javax.swing.JTextField();
        dfname.setText(chooseFile.getSelectedFile().getName());
        
        spreadsheet1 = new javax.swing.JPanel();

        separator = new javax.swing.JTextField();
        ndData = new javax.swing.JTextField();
        naData = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Import Menu");
        setResizable(false);

        okButton.setText("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        


        fileButton.setText("File...");

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        JLabel l_dataframe = new JLabel("Data Frame Name:");
        JLabel l_path = new JLabel("Path:");
        JLabel l_separator = new JLabel("Separator:");
        JLabel l_nonavail = new JLabel("Non-available data:");
        JLabel l_prefix = new JLabel("Prefix for non-detected data:");
        
        spreadsheet1.setBorder(javax.swing.BorderFactory.createTitledBorder("Spreadsheet options"));

        ndData.setText(ImportData.NON_DETECTED);

        naData.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        naData.setText(ImportData.NON_AVAILABLE);
        
        separator.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        separator.setText(String.valueOf(ImportData.separator));

        jCheckBox1.setSelected(ImportData.header);
        jCheckBox1.setText("with headers");


        GroupLayout spreadsheet1Layout = new GroupLayout(spreadsheet1);
        spreadsheet1.setLayout(spreadsheet1Layout);
        spreadsheet1Layout.setHorizontalGroup(spreadsheet1Layout.createParallelGroup(GroupLayout.LEADING)
            .add(spreadsheet1Layout.createSequentialGroup()
                .addContainerGap()
                .add(spreadsheet1Layout.createParallelGroup(GroupLayout.LEADING)
                    .add(l_separator)
                    .add(l_nonavail)
                    .add(l_prefix))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(spreadsheet1Layout.createParallelGroup(GroupLayout.LEADING, false)
                    .add(separator)
                    .add(ndData, 0, 0, Short.MAX_VALUE)
                    .add(naData, GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.RELATED, 50, Short.MAX_VALUE)
                .add(jCheckBox1)
                .addContainerGap())
        );
        spreadsheet1Layout.setVerticalGroup(spreadsheet1Layout.createParallelGroup(GroupLayout.LEADING)
            .add(spreadsheet1Layout.createSequentialGroup()
                .add(14, 14, 14)
                .add(spreadsheet1Layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .add(l_separator)
                    .add(jCheckBox1))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(spreadsheet1Layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(l_nonavail)
                    .add(naData, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(spreadsheet1Layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(ndData, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .add(l_prefix))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(GroupLayout.LEADING)
                    .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(okButton, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(cancelButton))
                    .add(spreadsheet1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(l_dataframe)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(dfname, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(l_path)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(absolutePath, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.UNRELATED)
                        .add(fileButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.LEADING)
            .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(l_dataframe)
                    .add(dfname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(GroupLayout.LEADING)
                    .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(absolutePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .add(fileButton))
                    .add(l_path))
                .add(27, 27, 27)
                .add(spreadsheet1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(cancelButton)
                    .add(okButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>
    private void fileButtonActionPerformed(ActionEvent evt){
        if(chooseFile.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            absolutePath.setText(chooseFile.getSelectedFile().getAbsolutePath());
        }
    }
    private void okButtonActionPerformed(ActionEvent evt) {
        String fileName = absolutePath.getText();
        String nameDataFrame = dfname.getText();

        if(((CoDaPackMain)getParent()).
                isDataFrameNameAvailable(nameDataFrame)){

                    ImportData.header = jCheckBox1.isSelected();
                    ImportData.NON_AVAILABLE = naData.getText();
                    ImportData.NON_DETECTED = ndData.getText();
                    ImportData.separator = separator.getText().charAt(0);
                    if("\\t".equals(separator.getText()))
                        ImportData.separator = '\t';
                    dataFrame = ImportData.importText( fileName  );
                            
                    dataFrame.name = dfname.getText();
               

            setVisible(false);
        }else{
            JOptionPane.showMessageDialog(this, "Data Frame Name is already in use", "Name already in use", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        dispose();
    }

}
