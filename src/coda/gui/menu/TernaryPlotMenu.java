/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.menu;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputPlotHeader;
import coda.plot.window.CoDaPlotWindow;
import coda.plot.window.TernaryPlot3dWindow;
import coda.plot.TernaryPlot2dDisplay;
import coda.plot.TernaryPlot2dDisplay.TernaryPlot2dBuilder;
import coda.plot.TernaryPlot3dDisplay;
import coda.plot.TernaryPlot3dDisplay.TernaryPlot3dBuilder;
import coda.plot.window.TernaryPlot2dWindow;
import javax.swing.JOptionPane;

/**
 *
 * @author mcomas
 */
public class TernaryPlotMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    public TernaryPlotMenu(final CoDaPackMain mainApp){
        super(mainApp, "Ternary Plot Menu", true);//, false, true, false);
    }
    @Override
    public void acceptButtonActionPerformed() {

        String selectedNames[] = ds.getSelectedData();
        if(selectedNames.length == 3 || selectedNames.length == 4){
            DataFrame df = mainApplication.getActiveDataFrame();
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
            plot.setVisible(true);
            setVisible(false);
        }else{
            JOptionPane.showMessageDialog(this, "<html>Select <b>three</b> or <b>four</b> variables</html>");
        }
        
    }
}
