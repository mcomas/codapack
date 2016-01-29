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
import coda.gui.CoDaPackMain;
import coda.gui.utils.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.Normalizer;
import java.util.zip.GZIPOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.renjin.eval.Context;
import org.renjin.primitives.io.serialization.RDataWriter;
import org.renjin.sexp.IntArrayVector;
import org.renjin.sexp.DoubleArrayVector;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.PairList;
import org.renjin.sexp.StringArrayVector;

/**
 *
 * @author mcomas
 */
public class ExportRDataMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    JTextField dfname;
    JLabel text1 = new JLabel("Dataframe names:");
    public ExportRDataMenu(final CoDaPackMain mainApp){
        super(mainApp, "Export Menu", false, false, true);
        dfname = new JTextField("data", 14);
        optionsPanel.add(text1);
        optionsPanel.add(dfname);
    }
    public String cleanName(String name){
        name = Normalizer.normalize(name, Normalizer.Form.NFD);
        name = name.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        name = name.replaceAll("\\s","");
        name = name.replaceAll("-","_");
        name = name.replaceAll("-","_");
        name = name.replaceAll("/","");
        return name;
    }
    public String NumToString(double val){
        if(Double.isNaN(val)){
            return "NA";
        }else{
            return Double.toString(val);
        }
    }
    @Override
    public void acceptButtonActionPerformed() {
        JFileChooser chooseFile = new JFileChooser();
        chooseFile.setFileFilter(
                    new FileNameExtensionFilter("R data file", "RData"));
         if(chooseFile.showSaveDialog(this) ==
                    JFileChooser.APPROVE_OPTION){
            try{ 
                String fname = chooseFile.getSelectedFile().getAbsolutePath();
                fname = fname.endsWith(".RData") ? fname : fname.concat(".RData");

                
                DataFrame df = mainApplication.getActiveDataFrame();             
                ListVector.NamedBuilder dataframe = new ListVector.NamedBuilder();
                
                String[] sel_names = ds.getSelectedData();
                for(int j=0;j<sel_names.length;j++){                    
                    Variable var = df.get(sel_names[j]);
                    
                    if(var.isNumeric()){
                        dataframe.add(var.getName(), new DoubleArrayVector(var.getNumericalData()));
                    }
                    if(var.isText()){
                        dataframe.add(var.getName(), new StringArrayVector(var.getTextData()));
                    }
                }
                dataframe.setAttribute("class", new StringArrayVector("data.frame"));
                int [] ind = new int[df.getMaxVariableLength()];
                for (int i=0;i < df.getMaxVariableLength();++i) ind[i] = i + 1;
              
                dataframe.setAttribute("row.names", new IntArrayVector(ind));

                PairList.Builder list = new PairList.Builder();
                list.add(dfname.getText(), dataframe.build());

                Context context = Context.newTopLevelContext(); 
                FileOutputStream fos = new FileOutputStream(fname);
                GZIPOutputStream zos = new GZIPOutputStream(fos);
                RDataWriter writer = new RDataWriter(context, zos);
                writer.save(list.build());
                zos.close();
                fos.close();                
                
                setVisible(false);
            }catch (Exception e){ //Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
