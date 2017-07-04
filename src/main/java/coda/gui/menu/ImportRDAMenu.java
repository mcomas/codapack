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
public class ImportRDAMenu extends MenuDialog{
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
    //Guardem la ruta a l'arxiu en un string
    String rf;
    
    
    public ImportRDAMenu(CoDaPackMain mainApp, JFileChooser chooseFile, ImportRDA impdf) throws ScriptException {
        super(mainApp, "Import R Data File", false, chooseFile, impdf);
        
        //Assignem el path a l'string rf
        rf = chooseFile.getCurrentDirectory().getAbsolutePath();
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
        //Guardem el nom de les columnes seleccionades en una taula d'strings
        dfs = super.getDFS();
        sel_names = dfs.getSelectedData();

        if (sel_names.length > 0) {
            //Copiem els valors de prefix i suffix
            prefix = usedPrefix.getText();
            suffix = usedSuffix.getText();

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
                mainApplication.addDataFrameRDR(dataf);
            }
        }else{
            JOptionPane.showMessageDialog(this, "Select at least one Data Frame");
        }
    }

    
}
