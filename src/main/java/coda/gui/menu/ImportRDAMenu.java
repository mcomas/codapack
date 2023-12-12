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

import javax.script.ScriptException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import coda.DataFrame;
import coda.DataFrame.DataFrameException;
import coda.gui.CoDaPackMain;
import coda.gui.utils.DataSelector1to1;
import coda.io.Importer;

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
    // ArrayList<DataFrame> sel_dfs = new ArrayList<DataFrame>();
    DataFrame df = null;
    Importer imp_df;
    //Guardem la ruta a l'arxiu en un string
    
    
    public ImportRDAMenu(CoDaPackMain mainApp, Importer importRDA) throws ScriptException, DataFrameException {
        super(mainApp, "Import R Data File", new DataSelector1to1(importRDA.getAvailableDataFramesNames()).setBorderTitle("Select Tables"));
        
        
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
        imp_df = importRDA;
        
    }

    @Override
    public void acceptButtonActionPerformed() {
        //Guardem el nom de les columnes seleccionades en una taula d'strings
       
        sel_names = ds.getSelectedData();
        if (sel_names.length == 1) {
            //Copiem els valors de prefix i suffix
            prefix = usedPrefix.getText();
            suffix = usedSuffix.getText();

            try {
                df = imp_df.importDataFrame(sel_names[0]);
            } catch (DataFrameException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            setVisible(false);
            //Carreguem els dataframes seleccionats
            df.setName(prefix + sel_names[0] + suffix);
            mainApplication.addDataFrameRDR(df);
            // for (DataFrame dataf : sel_dfs) {
            //     mainApplication.addDataFrameRDR(dataf);
            // }
        }else{
            JOptionPane.showMessageDialog(this, "Select one Data Frame");
        }
    }

    
}
