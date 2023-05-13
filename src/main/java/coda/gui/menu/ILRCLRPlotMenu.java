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
import coda.ext.jama.Matrix;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputPlotHeader;
import coda.gui.utils.BinaryPartitionSelect;
import coda.gui.utils.DataSelector;
import coda.plot.Biplot2dDisplay;
import coda.plot.Biplot3dDisplay;
import coda.plot.window.CoDaPlotWindow;
import coda.plot.window.RealPlot2dWindow;
import coda.plot.window.RealPlot3dWindow;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
/**
 *
 * @author mcomas
 */
public class ILRCLRPlotMenu extends AbstractMenuDialogWithILR{
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Graphs.ILR-CLR Plot.yaml";
    private static final String helpTitle = "ILR/CLR Plot Help Menu";
    
    JCheckBox coordinates;
    DataFrame df;
    ArrayList<String> names;
    
    public ILRCLRPlotMenu(final CoDaPackMain mainApp){
        super(mainApp, "ILR/CLR Plot Menu", new DataSelector(mainApp.getActiveDataFrame(), true));
        super.setHelpConfig(yamlUrl, helpTitle);

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
        
        coordinates = new JCheckBox("Add coordinates", false);
        optionsPanel.add(coordinates);
        
        base.setEnabled(true);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
        
    }
    public void initiatePartitionMenu(){
        BinaryPartitionSelect binaryMenu = new BinaryPartitionSelect(this, ds.getSelectedData() );
        binaryMenu.setVisible(true);
    }
    @Override
    public void acceptButtonActionPerformed() {
            String selectedNames[] = ds.getSelectedData();
        
        if(selectedNames.length > 2){
            int m = selectedNames.length;
            String plotNames[] = new String[m];
            
            for(int i=0;i<m;i++){
                plotNames[i] = "clr."+ selectedNames[i];
                
            }
            df = mainApplication.getActiveDataFrame();
            boolean[] selection = getValidComposition(df, selectedNames);
            int [] mapping = df.getMapingToData(selectedNames, selection);
            double[][] data = df.getNumericalData(selectedNames, mapping);
            //double[][] data = CoDaStats.centerData(df.getNumericalData(selectedNames, mapping));
            //double[][] data = df.getNumericalData(selectedNames, mapping);
            double basis[][] = new double[m][m];
            double vdata[][] = null;
            for(int i=0;i<m;i++)
                for(int j=0;j<m;j++)
                    basis[i][j] = i == j ? Math.E : 1;
            if(part.isSelected()){
                int partition[][] = getPartition();
                data = CoDaStats.transformRawILR(data, partition);
                vdata = new Matrix(CoDaStats.transformRawILR(basis, partition)).transpose().getArray();
            }else{
                double bas[][] = getBasis();
                data = CoDaStats.transformRawILR(data, bas);
                vdata = new Matrix(CoDaStats.transformRawILR(basis, bas)).transpose().getArray();
            }
            
            
            //data = CoDaStats.transformRawCLR(data);

            //Matrix Z = (new Matrix(data)).transpose();
            //SingularValueDecomposition svd =
            //    new SingularValueDecomposition(Z);

            //int rank = svd.rank();
            //String pcnames[] = new String[rank];
            //String pcheaders[] = new String[m+1];
//            double pcomp[][] = new double[rank][m+1];
//
//            Matrix V = svd.getV().getMatrix(0, m-1, 0, rank-1);
//
//            Matrix UD = new Matrix(data).transpose().times(V.inverse().transpose());
//            double cpExp[] = coda.BasicStats.cumulativeProportionExplained(svd.getSingularValues());
//
//            pcheaders[m] = "Cum.Prop.Exp.";
//            for(int j=0;j<m;j++){
//                pcheaders[j] = plotNames[j];
//            }
//            for(int i=0;i<rank;i++){
//                pcnames[i] = "PC" + (i+1);
//                pcomp[i][m] = cpExp[i];
//                for(int j=0;j<m;j++)
//                    pcomp[i][j] = svd.getV().get(j,i);
//            }

            if(coordinates.isSelected()){
                String unames[] = new String[m-1];
                for(int i=0;i<m-1;i++)
                    unames[i] = "ilr." + (i+1);
                df.addData(unames, coda.Utils.recoverData(data,selection));
                mainApplication.updateDataFrame(df);
            }
            CoDaPackMain.outputPanel.addOutput(
                    new OutputPlotHeader("ILR/CLR plot generated", selectedNames));
            
            //CoDaPackMain.outputPanel.addOutput(
            //        new OutputTableTwoEntries("Principal Components", pcheaders, pcnames, pcomp));

            int dim = m > 3 ? 3 : 2;
            String[] names = new String[dim];
            for(int i=0;i<dim;i++)
                names[i] = "g" + Integer.toString(i);

            String groupedBy = ds.getSelectedGroup();

            //double GH[][][] = CoDaStats.biplot(svd, dim, 0);
            CoDaPlotWindow biplotWindow;
            if(m > 3){
                
                Biplot3dDisplay.Biplot3dBuilder builder =
                        new Biplot3dDisplay.Biplot3dBuilder(plotNames, data, vdata).mapping(mapping);

                if(groupedBy != null){
                    builder.groups(coda.Utils.reduceData(
                            df.getDefinedGroup(groupedBy),
                            selection), coda.Utils.getCategories(
                            df.getCategoricalData(groupedBy),selection));
                }
                Biplot3dDisplay dp = builder.build();
                dp.decomp = false;
                biplotWindow =
                        new RealPlot3dWindow(df, dp, "ILR/CLR plot");
                
                double view[][] = {
                       {1, 0, 0},
                       {0, 1, 0},
                       {0, 0, 1}};
                //((Biplot3dWindow)biplotWindow).setCoordinate(view);                
                
            }else{

                Biplot2dDisplay.Biplot2dBuilder builder =
                        new Biplot2dDisplay.Biplot2dBuilder(plotNames, data, vdata).mapping(mapping);             
                if(groupedBy != null){
                    builder.groups(coda.Utils.reduceData(
                            df.getDefinedGroup(groupedBy),
                            selection), coda.Utils.getCategories(
                            df.getCategoricalData(groupedBy),selection));
                }
                
                biplotWindow =
                        new RealPlot2dWindow(df, builder.build(), "ILR/CLR plot");
                               
            }
            biplotWindow.setLocationRelativeTo(mainApplication);
            biplotWindow.setVisible(true);
            setVisible(false);
        }else{
            JOptionPane.showMessageDialog(this, "<html>Select at least <b>three</b> variables</html>");
        }
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }

}
