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
import java.io.FileWriter;
import java.text.Normalizer;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author mcomas
 */
public class ExportMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    JTextField dfname;
    JLabel text1 = new JLabel("Dataframe names:");
    public ExportMenu(final CoDaPackMain mainApp){
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
                    new FileNameExtensionFilter("R source", "R"));
         if(chooseFile.showSaveDialog(this) ==
                    JFileChooser.APPROVE_OPTION){
            try{
  // Create file 
                String fname = chooseFile.getSelectedFile().getAbsolutePath();
                fname = fname.endsWith(".R") ? fname : fname.concat(".R");
                FileWriter fstream = new FileWriter(fname);
                BufferedWriter out = new BufferedWriter(fstream);
                String code = cleanName(dfname.getText()) + " = data.frame(";
                
                DataFrame df = mainApplication.getActiveDataFrame();
                String[] sel_names = ds.getSelectedData();
                Variable var = df.get(sel_names[0]);
                code += cleanName(var.getName()) + " = c(";
                
                if(var.isNumeric()){
                    double vals[] = var.getNumericalData();
                    for(int i=0;i<vals.length-1;i++){
                        code += NumToString(vals[i]) + ", ";
                    }

                    code += NumToString(vals[vals.length-1]) + "));\n";
                }else{
                    String vals[] = var.getCategoricalData();
                    for(int i=0;i<vals.length-1;i++){
                        code += "\"" + vals[i] + "\"" + ", ";
                    }

                    code += "\"" + vals[vals.length-1] + "\"" + "));\n";
                }
                for(int j=1;j<sel_names.length;j++){                    
                    var = df.get(sel_names[j]);
                    code += cleanName(dfname.getText()) + "$" + cleanName(var.getName()) + " = c(";
                    if(var.isNumeric()){
                        double vals2[] = var.getNumericalData();
                        for(int i=0;i<vals2.length-1;i++){
                            code += NumToString(vals2[i]) + ", ";
                        }
                        code += NumToString(vals2[vals2.length-1]) + ");\n";
                    }else{
                        String vals2[] = var.getCategoricalData();
                        for(int i=0;i<vals2.length-1;i++){
                            code += "\"" + vals2[i] + "\"" + ", ";
                        }

                        code += "\"" + vals2[vals2.length-1] + "\"" + ");\n";
                    }
                }
                out.write(code);
                out.close();
                setVisible(false);
            }catch (Exception e){//Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
