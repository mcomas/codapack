/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.menu;

import coda.DataFrame;
import coda.Variable;
import coda.gui.CoDaPackMain;
import coda.gui.CoDaPackConf;
import static coda.gui.CoDaPackMain.outputPanel;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;
import coda.gui.output.OutputText;
import coda.gui.utils.DataSelector;
import coda.gui.utils.DataSelector1to1;
import coda.util.AppLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author Guest2
 */
public class SortDataMenu extends AbstractMenuDialog{
    
    DataFrame df;
    JCheckBox decSort;
    ArrayList<String> names;
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = "Data.Manipulte.Sort Data.yaml";
    private static final String helpTitle = "Sort Data Help Menu";
    
    public SortDataMenu(final CoDaPackMain mainApp){
        super(mainApp, "Sort Data Menu", new DataSelector1to1(mainApp.getActiveDataFrame(), false, DataSelector.ALL_VARIABLES));
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        
        decSort = new JCheckBox("Decreasing sort",false);
        this.optionsPanel.add(decSort);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        String selectedNames[] = ds.getSelectedData(); // we take the selected data
        Vector<String> vSelectedNames = new Vector<String>(Arrays.asList(selectedNames));
        
        if(selectedNames.length > 0){
            
            df = mainApplication.getActiveDataFrame();
            Rengine re = CoDaPackMain.re;
            if(re == null){
                JOptionPane.showMessageDialog(null, "R is not available");
                return;
            }
            
            int auxPos = 1;
            String[] tempNames = new String[selectedNames.length];
            for(int i = 0; i < selectedNames.length; i++, auxPos++){
                String vname = selectedNames[i];
                tempNames[i] = ".cdp_sort_col_" + auxPos;
                if(df.get(vname).isNumeric()){
                    re.assign(tempNames[i], df.get(vname).getNumericalData());
                }else{
                    re.assign(tempNames[i], df.get(vname).getTextData());
                }
            }
            int[] orderSort;
            re.assign(".cdp_sort_names", tempNames);
            re.eval(".cdp_sort_args <- mget(.cdp_sort_names, envir = .GlobalEnv)");
            re.eval(".cdp_sort_args$decreasing <- " + (decSort.isSelected() ? "TRUE" : "FALSE"));
            REXP result = re.eval("do.call(order, .cdp_sort_args)");
            if(result != null && result.asIntArray() != null){
                orderSort = result.asIntArray();
                DataFrame newDf = new DataFrame();

                // we add the variables ordereds by the orderSort

                for(int i=0; i < df.size(); i++){
                    newDf.addData(df.get(i).getName(), new Variable(df.get(i),orderSort));
                }

                String nameDf = df.getName();
                mainApplication.removeDataFrame(df);

                newDf.setName(nameDf);
                mainApplication.addDataFrame(newDf);

            } else {
                JOptionPane.showMessageDialog(null, "Invalid sort selection");
            }
            re.eval("rm(list = c('.cdp_sort_names', '.cdp_sort_args', ls(pattern='^\\\\.cdp_sort_col_', all.names=TRUE)), envir = .GlobalEnv)");
            
            this.dispose();
        }
        else{
            if(selectedNames.length == 0) JOptionPane.showMessageDialog(null, "Please select some data");
        }
        
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
    
}
