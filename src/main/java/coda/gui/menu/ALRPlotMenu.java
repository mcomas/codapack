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

import coda.CoDaStats;
import coda.DataFrame;
import coda.Utils;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputPlotHeader;
import coda.plot.RealPlot2dDisplay.RealPlot2dBuilder;
import coda.plot.RealPlot3dDisplay.RealPlot3dBuilder;
import coda.plot.window.CoDaPlotWindow;
import coda.plot.window.RealPlot2dWindow;
import coda.plot.window.RealPlot3dWindow;
import javax.swing.JOptionPane;
/**
 *
 * @author mcomas
 */
public class ALRPlotMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;

    public ALRPlotMenu(final CoDaPackMain mainApp){
        super(mainApp, "ALR Plot Menu", true);
    }
    @Override
    public void acceptButtonActionPerformed() {
        String selectedNames[] = ds.getSelectedData();


        if(selectedNames.length == 3 || selectedNames.length == 4){
            String groupedBy = ds.getSelectedGroup();

            DataFrame df = mainApplication.getActiveDataFrame();
            boolean[] selection = getValidComposition(df, selectedNames);
            int [] mapping = df.getMapingToData(selectedNames, selection);
            double[][] data = df.getNumericalData(selectedNames, mapping);


            data = CoDaStats.transformRawALR(data);

            int dimension = selectedNames.length == 3 ? 2 : 3;
            String[] names = new String[dimension];
            for(int i=0;i<dimension;i++)
                    names[i] = "alr." + selectedNames[i]
                            + "_" + selectedNames[dimension];
            CoDaPlotWindow plotWindow = null;
            if(dimension == 2){
                RealPlot2dBuilder builder = new RealPlot2dBuilder(names,data).mapping(mapping);
                if(groupedBy != null){
                    builder.groups(Utils.reduceData(
                            df.getDefinedGroup(groupedBy),
                            selection), coda.Utils.getCategories(
                            df.getCategoricalData(groupedBy),selection));
                }
                plotWindow = new RealPlot2dWindow(
                        df, builder.build(),
                        "ALR Plot");

            }
            if(dimension == 3){
                RealPlot3dBuilder builder = new RealPlot3dBuilder(names,data).mapping(mapping);
                if(groupedBy != null){
                    builder.groups(Utils.reduceData(
                            df.getDefinedGroup(groupedBy),
                            selection), coda.Utils.getCategories(
                            df.getCategoricalData(groupedBy),selection));
                }
                plotWindow = new RealPlot3dWindow(
                        df, builder.build(),
                        "ALR Plot 3d");
            }
            plotWindow.setLocationRelativeTo(mainApplication);
            plotWindow.setVisible(true);
            
            CoDaPackMain.outputPane.addOutput(
                    new OutputPlotHeader("ALR plot generated", selectedNames));
            /*if(groupedBy != null) CoDaPackMain.outputPanel.addOutput(
                    new OutputColorGroups(CoDaPack.reduceData(
                            df.getCategoricalData(groupedBy),
                            selection)));*/
            setVisible(false);
        }else{
            JOptionPane.showMessageDialog(this, "<html>Select <b>three</b> or <b>four</b> variables</html>");
        }
        
    }
}
