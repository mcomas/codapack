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
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputPlotHeader;
import coda.gui.utils.DataSelector1to1;
import coda.plot.window.CoDaPlotWindow;
import coda.plot.window.TernaryPlot3dWindow;
import coda.plot.TernaryPlot2dDisplay;
import coda.plot.TernaryPlot2dDisplay.TernaryPlot2dBuilder;
import coda.plot.TernaryPlot3dDisplay;
import coda.plot.TernaryPlot3dDisplay.TernaryPlot3dBuilder;
import coda.plot.window.TernaryPlot2dWindow;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author mcomas
 */
public class TernaryQuaternaryPlotMenu extends AbstractMenuDialog{
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Graphs.Ternary-Quaternary Plot.yaml";
    private static final String helpTitle = "Ternary/Quaternary Plot Help Menu";
    DataFrame df;
    ArrayList<String> names;
    
    public TernaryQuaternaryPlotMenu(final CoDaPackMain mainApp){
        super(mainApp, "Ternary/Quaternary Plot Menu", new DataSelector1to1(mainApp.getActiveDataFrame(), true));//, false, true, false);
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
    }
    @Override
    public void acceptButtonActionPerformed() {

        String selectedNames[] = ds.getSelectedData();
        if(selectedNames.length == 3 || selectedNames.length == 4){
            df = mainApplication.getActiveDataFrame();
            boolean[] selection = getValidComposition(df, selectedNames);
            int [] mapping = df.getMapingToData(selectedNames, selection);
            double[][] data = df.getNumericalData(selectedNames, mapping);
            CoDaPlotWindow plot = null;

            String groupedBy = ds.getSelectedGroup();

            CoDaPackMain.outputPanel.addOutput(
                    new OutputPlotHeader("Ternary plot", selectedNames));

            if(selectedNames.length == 3){
                TernaryPlot2dBuilder builder = new TernaryPlot2dBuilder(selectedNames, data);
                builder.mapping(mapping);
                
                if(groupedBy != null){
                    builder.groups(coda.Utils.reduceData(
                            df.getDefinedGroup(groupedBy),
                            selection), coda.Utils.getCategories(
                            df.getCategoricalData(groupedBy),selection));
                }
                TernaryPlot2dDisplay display = builder.build();
                plot = new TernaryPlot2dWindow(df, display, "Ternary Plot");
   
            }else{// selectedNames.length == 4
                TernaryPlot3dBuilder builder = new TernaryPlot3dBuilder(selectedNames, data);
                builder.mapping(mapping);
                
                if(groupedBy != null){
                    builder.groups(coda.Utils.reduceData(
                            df.getDefinedGroup(groupedBy),
                            selection), coda.Utils.getCategories(
                            df.getCategoricalData(groupedBy),selection));
                }
                TernaryPlot3dDisplay display = builder.build();
                plot = new TernaryPlot3dWindow(df, display, "Ternary Plot 3d");
            }
            plot.setLocationRelativeTo(mainApplication);
            plot.setVisible(true);
            setVisible(false);
        }else{
            JOptionPane.showMessageDialog(this, "<html>Select <b>three</b> or <b>four</b> variables</html>");
        }
        
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
}
