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

        if(selectedNames.length == 3 || selectedNames.length == 4){
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
            plotWindow.setVisible(true);
            setVisible(false);
        }else{
            JOptionPane.showMessageDialog(this, "<html>Select <b>three</b> or <b>four</b> variables</html>");
        }
    }

}
