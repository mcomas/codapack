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
import coda.ext.jama.SingularValueDecomposition;
import coda.gui.CoDaPackConf;
import coda.plot.window.Biplot3dWindow;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputPlotHeader;
import coda.gui.output.OutputTableTwoEntries;
import coda.plot.Biplot2dDisplay.Biplot2dBuilder;
import coda.plot.Biplot3dDisplay.Biplot3dBuilder;
import coda.plot.window.Biplot2dWindow;
import coda.plot.window.CoDaPlotWindow;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

/**
 *
 * @author mcomas
 */
public class Biplot3dMenu extends AbstractMenuDialog{
    
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Graphs.Biplot.yaml";
    private static final String helpTitle = "CLR Biplot Help Menu";
    JCheckBox coordinates;
    DataFrame df;
    
    public Biplot3dMenu(final CoDaPackMain mainApp){
        super(mainApp, " CLR Biplot Menu", true);
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        
        coordinates = new JCheckBox("Add coordinates", false);
        optionsPanel.add(coordinates);
    }
    @Override
    @SuppressWarnings("empty-statement")
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
            double[][] data = CoDaStats.centerData(df.getNumericalData(selectedNames, mapping));
            //double[][] data = df.getNumericalData(selectedNames, mapping);

            data = CoDaStats.transformRawCLR(data);

            Matrix Z = (new Matrix(data)).transpose();
            SingularValueDecomposition svd =
                new SingularValueDecomposition(Z);



            int rank = svd.rank();
            String pcnames[] = new String[rank];
            String pcheaders[] = new String[m+1];
            double pcomp[][] = new double[rank][m+1];

            Matrix V = svd.getV().getMatrix(0, m-1, 0, rank-1);

            Matrix UD = new Matrix(data).transpose().times(V.inverse().transpose());
            double cpExp[] = coda.BasicStats.cumulativeProportionExplained(svd.getSingularValues());

            pcheaders[m] = "Cum.Prop.Exp.";
            for(int j=0;j<m;j++){
                pcheaders[j] = plotNames[j];
            }
            for(int i=0;i<rank;i++){
                pcnames[i] = "PC" + (i+1);
                pcomp[i][m] = cpExp[i];
                for(int j=0;j<m;j++)
                    pcomp[i][j] = svd.getV().get(j,i);
            }

            if(coordinates.isSelected()){
                String unames[] = new String[rank];
                for(int i=0;i<rank;i++)
                    unames[i] = "UD" + (i+1);
                df.addData(unames, coda.Utils.recoverData(UD.transpose().getArray(),selection));
                mainApplication.updateDataFrame(df);
            }
            CoDaPackMain.outputPanel.addOutput(
                    new OutputPlotHeader("Biplot generated", selectedNames));
            
            CoDaPackMain.outputPanel.addOutput(
                    new OutputTableTwoEntries("Principal Components", pcheaders, pcnames, pcomp));

            int dim = m > 3 ? 3 : 2;
            String[] names = new String[dim];
            for(int i=0;i<dim;i++)
                names[i] = "g" + Integer.toString(i);

            String groupedBy = ds.getSelectedGroup();

            double GH[][][] = CoDaStats.biplot(svd, dim, 0);
            CoDaPlotWindow biplotWindow;
            if(m > 3){
                Biplot3dBuilder builder =
                        new Biplot3dBuilder(plotNames, GH[0], GH[1]).mapping(mapping);

                if(groupedBy != null){
                    builder.groups(coda.Utils.reduceData(
                            df.getDefinedGroup(groupedBy),
                            selection), coda.Utils.getCategories(
                            df.getCategoricalData(groupedBy),selection));
                }
                biplotWindow =
                        new Biplot3dWindow(df, builder.build(), "Biplot3d", svd);

                double view[][] = {
                       {1, 0, 0},
                       {0, 1, 0},
                       {0, 0, 1}};
                ((Biplot3dWindow)biplotWindow).setCoordinate(view);                
                
            }else{

                Biplot2dBuilder builder =
                        new Biplot2dBuilder(plotNames, GH[0], GH[1]).mapping(mapping);
                if(groupedBy != null){
                    builder.groups(coda.Utils.reduceData(
                            df.getDefinedGroup(groupedBy),
                            selection), coda.Utils.getCategories(
                            df.getCategoricalData(groupedBy),selection));
                }

                biplotWindow =
                        new Biplot2dWindow(df, builder.build(), "Biplot", svd);
                               
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

}
