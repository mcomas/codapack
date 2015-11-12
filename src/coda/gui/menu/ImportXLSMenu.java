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
public class ImportXLSMenu extends JDialog {
    JFileChooser chooseFile;
    DataFrame dataFrame;    
    
    JTextField absolutePath;
    JTextField dfname;
            
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JTextField naData;
    private javax.swing.JTextField ndData;
    private javax.swing.JPanel spreadsheet1;
    private javax.swing.JTextField startRow;
    
    
    public ImportXLSMenu(final CoDaPackMain mainApp, boolean modal, JFileChooser chooseFile) {
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
            public void actionPerformed(ActionEvent evt) {
                fileButtonActionPerformed(evt);
            }
        });
        
        dfname = new javax.swing.JTextField();
        dfname.setText(chooseFile.getSelectedFile().getName());
        
        spreadsheet1 = new javax.swing.JPanel();

        ndData = new javax.swing.JTextField();
        naData = new javax.swing.JTextField();
        startRow = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Import Menu");
        setResizable(false);

        okButton.setText("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        


        fileButton.setText("File...");

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        JLabel jLabel1 = new JLabel("Data Frame Name:");
        JLabel jLabel2 = new JLabel("Path:");
        JLabel jLabel3 = new JLabel("Start reading at row:");
        JLabel jLabel4 = new JLabel("Non-available data:");
        JLabel jLabel5 = new JLabel("Prefix for non-detected data:");
        
        spreadsheet1.setBorder(javax.swing.BorderFactory.createTitledBorder("Spreadsheet options"));

        ndData.setText("<");

        naData.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        naData.setText("NA");

        startRow.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        startRow.setText("1");

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("with headers");


        GroupLayout spreadsheet1Layout = new GroupLayout(spreadsheet1);
        spreadsheet1.setLayout(spreadsheet1Layout);
        spreadsheet1Layout.setHorizontalGroup(
            spreadsheet1Layout.createParallelGroup(GroupLayout.LEADING)
            .add(spreadsheet1Layout.createSequentialGroup()
                .addContainerGap()
                .add(spreadsheet1Layout.createParallelGroup(GroupLayout.LEADING)
                    .add(jLabel4)
                    .add(jLabel5)
                    .add(jLabel3))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(spreadsheet1Layout.createParallelGroup(GroupLayout.LEADING, false)
                    .add(startRow)
                    .add(ndData, 0, 0, Short.MAX_VALUE)
                    .add(naData, GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.RELATED, 50, Short.MAX_VALUE)
                .add(jCheckBox1)
                .addContainerGap())
        );
        spreadsheet1Layout.setVerticalGroup(
            spreadsheet1Layout.createParallelGroup(GroupLayout.LEADING)
            .add(spreadsheet1Layout.createSequentialGroup()
                .add(14, 14, 14)
                .add(spreadsheet1Layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(startRow, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3)
                    .add(jCheckBox1))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(spreadsheet1Layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(naData, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(spreadsheet1Layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(ndData, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(GroupLayout.LEADING)
                    .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(okButton, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(cancelButton))
                    .add(spreadsheet1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(dfname, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(absolutePath, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.UNRELATED)
                        .add(fileButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.LEADING)
            .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(dfname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(GroupLayout.LEADING)
                    .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(absolutePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .add(fileButton))
                    .add(jLabel2))
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
            if(fileName.endsWith(".xls") || fileName.endsWith(".xlsx")){
                try {
                    dataFrame = ImportData.importXLS(absolutePath.getText(),
                            jCheckBox1.isSelected(),
                            naData.getText(),
                            ndData.getText(),
                            Integer.parseInt(startRow.getText())-1);
                    dataFrame.name = dfname.getText();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ImportXLSMenu.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ImportXLSMenu.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidFormatException ex) {
                    Logger.getLogger(ImportXLSMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
