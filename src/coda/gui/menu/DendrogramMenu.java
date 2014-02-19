/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.menu;

import coda.BasicStats;
import coda.CoDaStats;
import coda.DataFrame;
import coda.Utils;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputILRPartition;
import coda.gui.output.OutputPlotHeader;
import coda.gui.output.OutputVector;
import coda.gui.utils.BinaryPartitionSelect;
import coda.plot.DendrogramDisplay.DendrogramBuilder;
import coda.plot.window.DendrogramWindow;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

/**
 *
 * @author marc
 */
public class DendrogramMenu extends AbstractMenuDialogWithILR{
    JCheckBox balancesCheck;
    JCheckBox statisticsCheck;
    public DendrogramMenu(final CoDaPackMain mainApp){
        super(mainApp, "Dendrogram Menu", true);

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
    }
    public void initiatePartitionMenu(){
        BinaryPartitionSelect binaryMenu = new BinaryPartitionSelect(this, ds.getSelectedData() );
        binaryMenu.setVisible(true);
    }
    @Override
    public void acceptButtonActionPerformed() {
        int [][] partition = getPartition();
        if(partition != null){

            DataFrame df = mainApplication.getActiveDataFrame();
            String selectedNames[] = ds.getSelectedData();

            int m = selectedNames.length-1;
            String[] new_names = new String[m];
            for(int i=0;i<m;i++) new_names[i] = "ilr." + Integer.toString(i+1);
            
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
            plotWindow.setVisible(true);

            
            CoDaPackMain.outputPanel.addOutput(
                    new OutputPlotHeader("Balance dendrogram", selectedNames));

            CoDaPackMain.outputPanel.addOutput(
                        new OutputILRPartition(selectedNames, partition));

            if(statisticsCheck.isSelected()){
                String [] ilrNames = new String[ilr.length];
                for(int i=0;i<ilr.length;i++) ilrNames[i] = "Balance " + (i+1);

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

}
