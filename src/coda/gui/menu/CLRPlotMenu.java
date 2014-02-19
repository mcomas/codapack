/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.menu;

import coda.CoDaStats;
import coda.DataFrame;
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
public class CLRPlotMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;

    public CLRPlotMenu(final CoDaPackMain mainApp){
        super(mainApp, "CLR Plot Menu", true);
    }
    @Override
    public void acceptButtonActionPerformed() {
        String selectedNames[] = ds.getSelectedData();

        if(selectedNames.length == 2 || selectedNames.length == 3){
            DataFrame df = mainApplication.getActiveDataFrame();
            boolean[] selection = getValidComposition(df, selectedNames);
            int [] mapping = df.getMapingToData(selectedNames, selection);
            double[][] data = df.getNumericalData(selectedNames, mapping);

            data = CoDaStats.transformRawCLR(data);
            String groupedBy = ds.getSelectedGroup();

            int dimension = selectedNames.length;

            String[] names = new String[dimension];
                for(int i=0;i<dimension;i++)
                    names[i] = "clr." + selectedNames[i];

            CoDaPlotWindow plotWindow = null;
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
                        "CLR Plot");
            }
            if(dimension ==3){
                RealPlot3dBuilder builder = new RealPlot3dBuilder(names,data).mapping(mapping);
                if(groupedBy != null){
                    builder.groups(coda.Utils.reduceData(
                            df.getDefinedGroup(groupedBy),
                            selection), coda.Utils.getCategories(
                            df.getCategoricalData(groupedBy),selection));
                }
                plotWindow = new RealPlot3dWindow(
                        df, builder.build(),
                        "CLR Plot 3d");

                double view[][] = {
                    {Math.sqrt(1.0/7.0), Math.sqrt(3.0/7.0),Math.sqrt(3.0/7.0)},
                    {Math.sqrt(1.0/7.0), -Math.sqrt(3.0/7.0),Math.sqrt(3.0/7.0)},
                    {-Math.sqrt(4.0/7.0), 0, Math.sqrt(3.0/7.0)}
                };
                ((RealPlot3dWindow)plotWindow).setCoordinate(view);
            }
            plotWindow.setVisible(true);

            CoDaPackMain.outputPanel.addOutput(
                    new OutputPlotHeader("CLR plot generated", selectedNames));
  
            setVisible(false);
        }else{
            JOptionPane.showMessageDialog(this, "<html>Select <b>two</b> or <b>three</b> variables</html>");
        }
    }
}
