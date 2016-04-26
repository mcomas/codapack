/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió - David Gàmez
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
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.utils.DataFrameSelector;
import coda.io.ImportData;
import coda.io.ImportRDA;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.renjin.sexp.DoubleVector;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.LogicalVector;
import org.renjin.sexp.SEXP;
import org.renjin.sexp.StringVector;

/**
 *
 * @author david
 */
public class ImportRDAMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    //Fixem el valor per defecte de prefix i suffix
    String prefix = null;
    String suffix = null;
    //Creem els quadres de text per a prefix i suffix
    JTextField usedPrefix;
    JTextField usedSuffix;
    //Creem el nom que tindran els quades de text
    JLabel l_usedPrefix = new JLabel("Prefix");
    JLabel l_usedSuffix = new JLabel("Suffix");
    //Creem el vector d'strings que contindrà el nom dels dataframes seleccionats
    String[] sel_names;
    ArrayList<DataFrame> sel_dfs = new ArrayList<DataFrame>();
    ImportRDA imp_df;
    
    
    public ImportRDAMenu(CoDaPackMain mainApp, JFileChooser chooseFile, ImportRDA impdf) throws ScriptException {
        super(mainApp, "Import RDA File", false, chooseFile, impdf);
        
        
        //assignem mida als quadres de text
        usedPrefix = new JTextField(8);
        usedSuffix = new JTextField(8);
        //definim el nom del quadre de text prefix
        optionsPanel.add(l_usedPrefix);
        //posem el quadre de text prefix
        optionsPanel.add(usedPrefix);
        //definim el nom del quadre de text suffix
        optionsPanel.add(l_usedSuffix);
        //posem el quadre de text suffix
        optionsPanel.add(usedSuffix);
        //Guardem un punter al ImportRDA
        imp_df = impdf;
        
    }

    @Override
    public void acceptButtonActionPerformed() {
        //Copiem els valors de prefix i suffix
        prefix = usedPrefix.getText();
        suffix = usedSuffix.getText();
        
        //Guardem el nom de les columnes seleccionades en una taula d'strings
        dfs = super.getDFS();
        sel_names = dfs.getSelectedData();
        try {
            sel_dfs = imp_df.getDfSelected(sel_names, prefix, suffix);
                    } catch (ScriptException ex) {
            Logger.getLogger(ImportRDAMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataFrame.DataFrameException ex) {
            Logger.getLogger(ImportRDAMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        setVisible(false, false);
        //Carreguem els dataframes seleccionats
        for (DataFrame dataf : sel_dfs) {
            mainApplication.addDataFrame(dataf);
        }
    }
    
}
    /*JFileChooser chooseFile;
    ArrayList<DataFrame> df_actual;
    ArrayList<DataFrame> dataFrames = new ArrayList<DataFrame>();
    
    JTextField absolutePath;
    JTextField filename;
            
    private javax.swing.JPanel dataframes1;
    
    
    public ImportRDAMenu(final CoDaPackMain mainApp, boolean modal, JFileChooser chooseFile, ArrayList<DataFrame> df_a) throws ScriptException {
        super(mainApp, modal);
        
        this.chooseFile = chooseFile;
        //Guardem els dataFrames que ja hi ha actius en una variable local
        this.df_actual = df_a;
        
        initComponents(df_a);

        setLocationRelativeTo(mainApp);  */
    
    
    /*@SuppressWarnings("unchecked")
    private void initComponents(ArrayList<DataFrame> df_a) throws ScriptException {
        //Creem els botons ok i cancel
        JButton okButton = new JButton();
        
        okButton.requestFocusInWindow();
        
        JButton cancelButton = new JButton();
        
        //Obtenim la ruta completa del arxiu RDA
        absolutePath = new JTextField();
        absolutePath.setText(chooseFile.getSelectedFile().getAbsolutePath());
        
        //Creem el botó File que ens permetrà canviar el Path
        JButton fileButton = new JButton();
        fileButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt) {
                fileButtonActionPerformed(evt);
            }
        });
        
        //Assignem el nom de l'arxiu a filename
        filename = new javax.swing.JTextField();
        filename.setText(chooseFile.getSelectedFile().getName());
        
        //Assignem el titol de la finestra
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Import RDA Menu");
        setResizable(false);
        
        //Assignem el nom al botó OK i l'acció a realitzar
        okButton.setText("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        
        //Posem el text del botó fileButton
        fileButton.setText("File...");
        
        //Assignem el nom al botó Cancel i l'acció a realitzar
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        
        //Definim els noms dels diferents components de la finestra
        JLabel jLabel1 = new JLabel("File Name:");
        JLabel jLabel2 = new JLabel("Path:");
        
        dataframes1 = new javax.swing.JPanel();
        
        dataframes1.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Frames in "+chooseFile.getSelectedFile().getName()));
        
        //Obtenim el nom dels dataframes que hi ha a l'arxiu
        String fname = chooseFile.getSelectedFile().getName();
        StringVector df_names = (StringVector)ImportRDA.getDataFramesNames(fname);
        System.out.println("El nom dels dataframes que conté l'arxiu "+fname+" són: ");
        for (String name : df_names.toArray()) {
            System.out.println(name);
        }
        //Recorren l'array de dataframes actius per obtenir el nom
        System.out.println("El nom dels dataframes que tenim oberts són: ");
        Iterator it = df_actual.iterator();
        
        while (it.hasNext()) {
            DataFrame df = (DataFrame)it.next();
            System.out.println(df.getName());
        }
        
        
        //Creem el JCheckBox que contindrà el dataframes1Layout
        
        
        
        //Creem el layout que contindrà el nom dels dataframes que te el filename
        
        
        
        /*GroupLayout dataframes1Layout = new GroupLayout(dataframes1);
        dataframes1.setLayout(dataframes1Layout);
        dataframes1Layout.setAutocreateGaps(true);
        dataframes1Layout.setAutocreateContainerGaps(true);*/
        
        //Creem la resta de la finestra
        /*GroupLayout layout = new GroupLayout(getContentPane());
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
                    .add(dataframes1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(filename, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE))
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
                    .add(filename, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(GroupLayout.LEADING)
                    .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(absolutePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .add(fileButton))
                    .add(jLabel2))
                .add(27, 27, 27)
                .add(dataframes1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.RELATED)
                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                    .add(cancelButton)
                    .add(okButton))
                .addContainerGap())
        );
        
        pack();
    }
    
    //Aquest mètode canviarà l'arxiu a importar quan es seleccioni des del File button
    private void fileButtonActionPerformed(ActionEvent evt){
        if(chooseFile.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            absolutePath.setText(chooseFile.getSelectedFile().getAbsolutePath());
        }
    }
    
    private void okButtonActionPerformed(ActionEvent evt) {
        //Defineix les accions a realitzar quan es clica el botó OK
        System.out.println("S'ha clicat OK");
    }
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        dispose();
    }*/
