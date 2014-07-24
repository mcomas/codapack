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
                    if(var.isFactor()){
                        dataframe.add(var.getName(), new StringArrayVector(var.getCategoricalData()));
                    }
                }
                dataframe.setAttribute("class", new StringArrayVector("data.frame"));
                double [] ind = new double[df.getRowCount()];
                for (int i=0;i < df.getRowCount();++i) ind[i] = i + 1;
              
                dataframe.setAttribute("row.names", new DoubleArrayVector(ind));

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
