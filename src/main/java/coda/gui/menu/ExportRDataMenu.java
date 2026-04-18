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

import coda.DataFrame;
import coda.Variable;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.utils.DataSelector;
import coda.gui.utils.DataSelector1to1;
import coda.gui.utils.FileNameExtensionFilter;
import coda.io.ExportRDA;

import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author mcomas
 */
public class ExportRDataMenu extends AbstractMenuDialog {
    public static final long serialVersionUID = 1L;
    JTextField dfname;
    JLabel text1 = new JLabel("Data Frame name:");
    ExportRDA exportRDA;
    public ExportRDataMenu(final CoDaPackMain mainApp, ExportRDA expRDA) {
        super(mainApp, "Export Menu", new DataSelector1to1(mainApp.getActiveDataFrame(), false, DataSelector.ALL_VARIABLES));
        exportRDA = expRDA;
        dfname = new JTextField("data", 14);
        optionsPanel.add(text1);
        optionsPanel.add(dfname);
    }

    @Override
    public void acceptButtonActionPerformed() {
        String ruta = CoDaPackConf.workingDir;
        JFileChooser chooseFile = new JFileChooser(ruta);
        chooseFile.setFileFilter(
                new FileNameExtensionFilter("R data file", "RData"));
                
        if (chooseFile.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Rengine re = CoDaPackMain.re;
                if (re == null) {
                    JOptionPane.showMessageDialog(this, "R is not available");
                    return;
                }
                String fname = chooseFile.getSelectedFile().getAbsolutePath();
                fname = fname.endsWith(".RData") ? fname : fname.concat(".RData");

                DataFrame df = mainApplication.getActiveDataFrame();
                String[] sel_names = ds.getSelectedData();
                re.eval(".cdp_export <- list()");
                for (int j = 0; j < sel_names.length; j++) {
                    Variable var = df.get(sel_names[j]);
                    String tmpName = ".cdp_export_col_" + j;

                    if (var.isNumeric()) {
                        re.assign(tmpName, var.getNumericalData());
                    }
                    if (var.isText()) {
                        re.assign(tmpName, var.getTextData());
                    }
                    re.eval(String.format(".cdp_export[[%d]] <- %s", j + 1, tmpName));
                }
                re.assign(".cdp_export_names", sel_names);
                re.assign(".cdp_export_df_name", dfname.getText());
                re.eval("names(.cdp_export) <- .cdp_export_names");
                re.eval(".cdp_export <- as.data.frame(.cdp_export, stringsAsFactors = FALSE)");
                re.eval("assign(.cdp_export_df_name, .cdp_export, envir = .GlobalEnv)");
                re.eval("save(list = .cdp_export_df_name, file = " + REXP.quoteString(fname.replace("\\", "/")) + ")");
                re.eval("rm(list = c('.cdp_export', '.cdp_export_names', '.cdp_export_df_name', ls(pattern='^\\\\.cdp_export_col_', all.names=TRUE)), envir = .GlobalEnv)");
                ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
                CoDaPackConf.workingDir = ruta;

                setVisible(false);
            } catch (Exception e) { // Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
