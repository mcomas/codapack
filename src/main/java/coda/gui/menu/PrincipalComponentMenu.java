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
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputPlotHeader;
import coda.gui.output.OutputTableTwoEntries;
import coda.plot.PrincipalComponent2dDisplay.PrincipalComponent2dBuilder;
import coda.plot.PrincipalComponent3dDisplay.PrincipalComponent3dBuilder;
import coda.plot.window.CoDaPlotWindow;
import coda.plot.window.TernaryPlot2dWindow;
import coda.plot.window.TernaryPlot3dWindow;
import javax.swing.JOptionPane;

/**
 *
 * @author mcomas
 */
public class PrincipalComponentMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;

    public PrincipalComponentMenu(final CoDaPackMain mainApp){
        super(mainApp, "Principal Component Menu", true);
    }
    @Override
    public void acceptButtonActionPerformed() {
        String selectedNames[] = ds.getSelectedData();
        if(selectedNames.length == 3 || selectedNames.length == 4){
            DataFrame df = mainApplication.getActiveDataFrame();
            boolean[] selection = getValidComposition(df, selectedNames);
            int [] mapping = df.getMapingToData(selectedNames, selection);
            double[][] data = df.getNumericalData(selectedNames, mapping);
            double [][]cdata = CoDaStats.centerData(data);
            cdata = CoDaStats.transformRawCLR(cdata);

            Matrix Z = (new Matrix(cdata)).transpose();

            SingularValueDecomposition svd =
                new SingularValueDecomposition(Z);

            double[][] pcomp = new Matrix(CoDaStats.transformCLRRaw(svd.getV().getArray())).transpose().getArray();
            int dim = selectedNames.length;

            String groupedBy = ds.getSelectedGroup();

            CoDaPackMain.output.addOutput(
                        new OutputPlotHeader("Principal components", selectedNames));

            double cpExp[] = coda.BasicStats.cumulativeProportionExplained(svd.getSingularValues());

            int rank = svd.rank();
            String pcnames[] = new String[rank];
            String pcheaders[] = new String[dim+1];
            double ternarypcomp[][] = new double[rank][dim+1];

            pcheaders[dim] = "Cum.Prop.Exp.";
            for(int j=0;j<dim;j++){
                pcheaders[j] = selectedNames[j];
            }
            for(int i=0;i<rank;i++){
                pcnames[i] = "PC" + (i+1);
                ternarypcomp[i][dim] = cpExp[i];
                for(int j=0;j<dim;j++)
                    ternarypcomp[i][j] = pcomp[i][j];
            }

            CoDaPackMain.output.addOutput(
                    new OutputTableTwoEntries("Principal Components", pcheaders, pcnames, ternarypcomp));

            CoDaPlotWindow window = null;
            if(selectedNames.length == 3){

                PrincipalComponent2dBuilder builder =
                        new PrincipalComponent2dBuilder(
                            selectedNames, data, pcomp[0], pcomp[1]).mapping(mapping);
                
                if(groupedBy != null){
                    builder.groups(coda.Utils.reduceData(
                            df.getDefinedGroup(groupedBy),
                            selection), coda.Utils.getCategories(
                            df.getCategoricalData(groupedBy),selection));
                }

                window = new TernaryPlot2dWindow(
                        df, builder.build(), "Principal Component Plot");

                window.setVisible(true);
                setVisible(false);                
                                
            }
            if(dim == 4){
                PrincipalComponent3dBuilder builder =
                        new PrincipalComponent3dBuilder(
                            selectedNames, data, pcomp[0], pcomp[1], pcomp[2]).mapping(mapping);

               
                if(groupedBy != null){
                    builder.groups(coda.Utils.reduceData(
                            df.getDefinedGroup(groupedBy),
                            selection), coda.Utils.getCategories(
                            df.getCategoricalData(groupedBy),selection));
                }

                window = new TernaryPlot3dWindow(df, builder.build(),
                        "Principal Component Plot 3d");
                window.setVisible(true);
                setVisible(false);
            }
        }else{
            JOptionPane.showMessageDialog(this, "<html>Select <b>three</b> or <b>four</b> variables</html>");
        }
    }

}
