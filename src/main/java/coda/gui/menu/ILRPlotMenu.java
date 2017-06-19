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
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputILRPartition;
import coda.gui.output.OutputPlotHeader;
import coda.gui.utils.BinaryPartitionSelect;
import coda.plot.RealPlot2dDisplay.RealPlot2dBuilder;
import coda.plot.RealPlot3dDisplay.RealPlot3dBuilder;
import coda.plot.window.CoDaPlotWindow;
import coda.plot.window.RealPlot2dWindow;
import coda.plot.window.RealPlot3dWindow;
import javax.swing.JButton;
import javax.swing.JOptionPane;
/**
 *
 * @author mcomas
 */
public class ILRPlotMenu extends AbstractMenuDialogWithILR{
    public static final long serialVersionUID = 1L;

    public ILRPlotMenu(final CoDaPackMain mainApp){
        super(mainApp, "ILR Plot Menu", true);

        JButton defaultPart = new JButton("Default Partition");
        JButton manuallyPart = new JButton("Define Manually");
        optionsPanel.add(defaultPart);
        defaultPart.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setPartition(CoDaStats.defaultPartition(ds.getSelectedData().length));
            }
        });
        optionsPanel.add(manuallyPart);
        manuallyPart.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initiatePartitionMenu();
            }
        });
        
    }
    public void initiatePartitionMenu(){
        BinaryPartitionSelect binaryMenu = new BinaryPartitionSelect(this, ds.getSelectedData() );
        binaryMenu.setVisible(true);
    }
    @Override
    public void acceptButtonActionPerformed() {
        String selectedNames[] = ds.getSelectedData();
        
        if( selectedNames.length != 4 && selectedNames.length != 3 ){
            JOptionPane.showMessageDialog(this, "<html>Select <b>three</b> or <b>four</b> variables</html>");
            return;
        }else if( areaPart.getText().trim().equals("") ){
            JOptionPane.showMessageDialog(this, "<html>A partition should be defined</html>");
            return;
        }
        DataFrame df = mainApplication.getActiveDataFrame();
        boolean[] selection = getValidComposition(df, selectedNames);
        int [] mapping = df.getMapingToData(selectedNames, selection);
        double[][] data = df.getNumericalData(selectedNames, mapping);
        
        int partition[][] = getPartition();
        

        data = CoDaStats.transformRawILR(data, partition);

        int dimension = selectedNames.length == 4 ? 3 : 2;

        String[] names = new String[dimension];
            for(int i=0;i<dimension;i++) names[i] = "ilr." + Integer.toString(i+1);
        String groupedBy = ds.getSelectedGroup();

        CoDaPlotWindow plotWindow = null;

        CoDaPackMain.outputPanel.addOutput(
                new OutputPlotHeader("ILR plot", selectedNames));

        CoDaPackMain.outputPanel.addOutput(
                    new OutputILRPartition(selectedNames, partition));
        if(dimension == 3){


            RealPlot3dBuilder builder = new RealPlot3dBuilder(names,data).mapping(mapping);

            if(groupedBy != null){
                builder.groups(coda.Utils.reduceData(
                        df.getDefinedGroup(groupedBy),
                        selection), coda.Utils.getCategories(
                        df.getCategoricalData(groupedBy),selection));
            }
            plotWindow = new RealPlot3dWindow(
                    df, builder.build(),
                    "ILR Plot 3d");
        }
        if(dimension == 2){

            RealPlot2dBuilder builder = new RealPlot2dBuilder(names,data).mapping(mapping);
            if(groupedBy != null){
                builder.groups(coda.Utils.reduceData(
                        df.getDefinedGroup(groupedBy),
                        selection), coda.Utils.getCategories(
                        df.getCategoricalData(groupedBy),selection));
            }
            plotWindow = new RealPlot2dWindow(
                    df, builder.build(),
                    "ILR Plot");                                
        }
        plotWindow.setLocationRelativeTo(mainApplication);
        plotWindow.setVisible(true);
        setVisible(false);
    }

}
