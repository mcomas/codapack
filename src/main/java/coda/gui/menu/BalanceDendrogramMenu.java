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

import coda.BasicStats;
import coda.CoDaStats;
import coda.DataFrame;
import coda.Utils;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputILRPartition;
import coda.gui.output.OutputPlotHeader;
import coda.gui.output.OutputVector;
import coda.gui.utils.BinaryPartitionSelect;
import coda.gui.utils.DataSelector1to1;
import coda.plot.DendrogramDisplay.DendrogramBuilder;
import coda.plot.window.DendrogramWindow;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

/**
 *
 * @author marc
 */
public class BalanceDendrogramMenu extends AbstractMenuDialogWithILR{
    
    JCheckBox balancesCheck;
    JCheckBox statisticsCheck;
    DataFrame df;
    ArrayList<String> names;
    private static final String yamlUrl = "Graphs.Balance Dendrogram.yaml";
    private static final String helpTitle = "Balance Dendogram Help Menu";
    
    public BalanceDendrogramMenu(final CoDaPackMain mainApp){
        super(mainApp, "Balance Dendrogram Menu", new DataSelector1to1(mainApp.getActiveDataFrame(), true));
        setHelpMenuConfiguration(yamlUrl, helpTitle);

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
        balancesCheck = new JCheckBox("Add balances", true);
        statisticsCheck = new JCheckBox("Add statistics", true);
        optionsPanel.add(balancesCheck);
        optionsPanel.add(statisticsCheck);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
    }
    public void initiatePartitionMenu(){
        BinaryPartitionSelect binaryMenu = new BinaryPartitionSelect(this, ds.getSelectedData() );
        binaryMenu.setVisible(true);
    }
    @Override
    public void acceptButtonActionPerformed() {
        int [][] partition = getPartition();
        if(partition != null){

            df = mainApplication.getActiveDataFrame();
            String selectedNames[] = ds.getSelectedData();

            int m = selectedNames.length-1;
            String[] new_names = new String[m];
            for(int i=0;i<m;i++) new_names[i] = "olr." + Integer.toString(i+1);
            
            //int [] mapping = this.getMappingToValidCompositionData(selectedNames);
            boolean[] selection = getValidComposition(df, selectedNames);
            int [] mapping = df.getMapingToData(selectedNames, selection);
            double[][] vdata = df.getNumericalData(selectedNames, mapping);//getValidCompositionData(selectedNames, mapping);
            //double data[][] = df.getNumericalData(sel_names);
            //double vdata[][] = CoDaPack.reduceData(data, selection);

            double ilr[][] = CoDaStats.transformRawILR(vdata, partition);
            if(balancesCheck.isSelected()){
                df.addData(new_names, coda.Utils.recoverData(ilr, selection));
                mainApplication.updateDataFrame(df);
            }
            
            DendrogramBuilder builder = new DendrogramBuilder(selectedNames, ilr, partition);
            builder.mapping(mapping);
            String groupedBy = ds.getSelectedGroup();
            if(groupedBy != null){
                builder.groups(Utils.reduceData(
                            df.getDefinedGroup(groupedBy),
                            selection), coda.Utils.getCategories(
                            df.getCategoricalData(groupedBy),selection));
            }
            
            DendrogramWindow plotWindow = new DendrogramWindow(
                    null, builder.build(),
                    "Dendrogram plot");
            plotWindow.setLocationRelativeTo(mainApplication);
            plotWindow.setVisible(true);

            
            CoDaPackMain.outputPanel.addOutput(
                    new OutputPlotHeader("Balance dendrogram", selectedNames));

            CoDaPackMain.outputPanel.addOutput(
                        new OutputILRPartition(selectedNames, partition));

            if(statisticsCheck.isSelected()){
                String [] ilrNames = new String[ilr.length];
                for(int i=0;i<ilr.length;i++) ilrNames[i] = "olr " + (i+1);

                double [] mean = coda.BasicStats.mean(ilr);
                double [] variance = BasicStats.variance(ilr);
                //mainApplication.outputPanel.writeVariationArray(selectedNames, varArray);

                CoDaPackMain.outputPanel.addOutput(
                        new OutputVector("Mean", ilrNames, mean));

                CoDaPackMain.outputPanel.addOutput(
                    new OutputVector("Variance", ilrNames, variance));
            }
            setVisible(false);
        }else{
            JOptionPane.showMessageDialog(this, "<html>You must define a partition</html>");
        }
        
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }

}
