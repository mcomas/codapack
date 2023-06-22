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

package coda.gui.menu;

import coda.gui.utils.DataSelector;
import coda.gui.utils.DataSelector1to1;
import coda.util.RScriptEngine;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.swing.JFrame;

import org.apache.batik.swing.JSVGCanvas;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

import coda.DataFrame;
import coda.Variable;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.DataList;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;
import coda.gui.output.OutputText;
/**
 *
 * @author mcomas
 */
public abstract class AbstractMenuRBasedDialog extends AbstractMenuDialog{
    RScriptEngine re;

    int PLOT_WIDTH = 850;
    int PLOT_HEIGHT = 500;

    String script_file;
    public AbstractMenuRBasedDialog(final CoDaPackMain mainApp, String title, DataSelector dataSelector, RScriptEngine r){
        super(mainApp, title, dataSelector);
        re = r;
    }
    public void addMatrixToR(double data[][], String col_names[], String name){
        String vnames[] = new String[data.length];
        for(int i=0; i < data.length; i++){
            vnames[i] = ".cdp_x"+i;
            re.assign(vnames[i], data[i]);
        }
        re.eval("%s = cbind(%s)".formatted(name, String.join(",", vnames)));

        String col_names_ext[] = new String[col_names.length];
        for(int i = 0; i < col_names.length; i++) col_names_ext[i] = "'" + col_names[i] + "'";
        re.eval("colnames(%s) = c(%s)".formatted(name , String.join(",", col_names_ext)));
        re.eval("%s[is.nan(%s)] = NA_real_".formatted(name, name));
    }
    public void addMatrixToR(String data[][], String col_names[], String name){
        String vnames[] = new String[data.length];
        for(int i=0; i < data.length; i++){
            vnames[i] = ".cdp_x"+i;
            re.assign(vnames[i], data[i]);
        }
        re.eval("%s = cbind(%s)".formatted(name, String.join(",", vnames)));

        String col_names_ext[] = new String[col_names.length];
        for(int i = 0; i < col_names.length; i++) col_names_ext[i] = "'" + col_names[i] + "'";
        re.eval("colnames(%s) = c(%s)".formatted(name , String.join(",", col_names_ext)));
        re.eval("%s[is.nan(%s)] = NA_real_".formatted(name, name));
    }
    void showText(){
        
        REXP result;
        String[] sortida;
        
        /* header output */
        
        //CoDaPackMain.outputPanel.addOutput(new OutputText("Zpatterns Plot:"));
        
        /* R output */        
        String outputString[] = re.eval("unlist(cdp_res[['text']])").asStringArray();
        System.out.println(Arrays.toString(outputString));
        CoDaPackMain.outputPanel.addOutput(new OutputForR(outputString));

    }
    void captureROutput(){
        String url = Paths.get(CoDaPackConf.getRScriptDefaultPath(), 
                               this.script_file).toString();

        String SOURCE = "error = tryCatch(source('%s'), error = function(e) e$message)".formatted(url.replace("\\","/"));
        System.out.println(SOURCE);

        re.eval(SOURCE);
        String errorMessage[] = re.eval("error").asStringArray();
        if(errorMessage == null){
            showText();
            createVariables();
            showGraphics();
            createDataFrame();
        }else{
            OutputElement type = new OutputText("Error in R:");
            CoDaPackMain.outputPanel.addOutput(type);
            OutputElement outElement = new OutputForR(errorMessage);
            CoDaPackMain.outputPanel.addOutput(outElement);
        }
    }
    void createDataFrame(){
        int nDataFrames = re.eval("length(cdp_res$dataframe)").asInt();
        for(int i=0; i < nDataFrames; i++){
            int nVariables = re.eval("length(cdp_res$dataframe[[" + String.valueOf(i+1) + "]])").asInt();
            DataFrame newDataFrame = new DataFrame();
            for(int j=0; j < nVariables; j++){
                String varName = re.eval("names(cdp_res$dataframe[[" + String.valueOf(i+1) + "]][" + String.valueOf(j+1) + "])").asString();
                String isNumeric = re.eval("class(unlist(cdp_res$dataframe[[" + String.valueOf(i+1) + "]][" + String.valueOf(j+1) + "]))").asString();
                if(isNumeric.equals("numeric")){ /* crear una variable numerica */
                    double[] data = re.eval("as.numeric(unlist(cdp_res$dataframe[[" + String.valueOf(i+1) + "]][" + String.valueOf(j+1) + "]))").asDoubleArray();
                    newDataFrame.addData(varName, data);
                }
                else{ /* crear una variable categorica */
                    String[] data = re.eval("as.character(unlist(cdp_res$dataframe[[" + String.valueOf(i+1) + "]][" + String.valueOf(j+1) + "]))").asStringArray();
                    newDataFrame.addData(varName, new Variable(varName,data));
                }
            }
            
            String dataFrameName = re.eval("names(cdp_res$dataframe)[" + String.valueOf(i+1) + "]").asString();
            
            while(!mainApplication.isDataFrameNameAvailable(dataFrameName)){
                dataFrameName += "c";
            }
            
            newDataFrame.setName(dataFrameName);
            DataFrame current = mainApplication.getActiveDataFrame();
            mainApplication.addDataFrame(newDataFrame);
            mainApplication.updateDataFrame(current);
        }
    }

    void createVariables(){
        
        int numberOfNewVar = re.eval("length(cdp_res$new_data)").asInt(); /* numero de columnes nomes*/
        
        for(int i=0; i < numberOfNewVar; i++){
            String varName = re.eval("names(cdp_res$new_data)[" + String.valueOf(i+1) + "]").asString();
            String isNumeric = re.eval("as.character(is.numeric(cdp_res$new_data[["+ String.valueOf(i+1) +"]]))").asString();
            if(isNumeric.equals("TRUE")){
                double[] data = re.eval("as.numeric(cdp_res$new_data[[" + String.valueOf(i+1) + "]])").asDoubleArray();
                df.addData(varName,data);
            }
            else{ // categoric
                String[] data = re.eval("as.character(cdp_res$new_data[[" + String.valueOf(i+1) + "]])").asStringArray();
                df.addData(varName, new Variable(varName,data));
            }
            mainApplication.updateDataFrame(df);
        }
    }

    void showGraphics(){
        
        String fnames[] = re.eval("cdp_res$graph").asStringArray();
        if(fnames != null){
            System.out.println(Arrays.toString(fnames));
            for(String fname: fnames){
                JSVGCanvas c = new JSVGCanvas();
                String uri = new File(fname).toURI().toString();
                c.setURI(uri);
                JFrame jf = new JFrame();
                jf.setSize(PLOT_WIDTH,PLOT_HEIGHT);
                jf.getContentPane().add(c);
                jf.setVisible(true);
            }
        }
        /*
        for(int i=0; i < numberOfGraphics; i++){
            tempDirR = re.eval("cdp_res$graph[[" + String.valueOf(i+1) + "]]").asString();
            tempsDirR.add(tempDirR);
            plotZpatternsMenu(this.framesZpatternsMenu.size());
        }  
        */
    }
    /*
    public AbstractMenuRBasedDialog(final CoDaPackMain mainApp,String title,  Rengine r, boolean groups, int variable_type){
        super(mainApp, title, groups, variable_type);
        mainApplication = mainApp;
        re = r;
        this.df = mainApplication.getActiveDataFrame();              
        this.ds = new DataSelector(df, groups, variable_type);
        initialize();
    }
    */
}


