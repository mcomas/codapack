/**
 *	Copyright 2011-2016 Marc Comas - Santiago Thió - David Gàmez
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

package coda.gui.menu;

import coda.CoDaStats;
import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputILRPartition;
import coda.gui.utils.BinaryPartitionSelect;

import javax.swing.*;

/**
 * Created by david on 2/06/16.
 */
public class TransformationRawILRMenu extends AbstractMenuDialogWithILR{
    public static final long serialVersionUID = 1L;

    public TransformationRawILRMenu(final CoDaPackMain mainApp){
        super(mainApp, "Raw-ILR Transform Menu", false);

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
        int[][] partition = getPartition();
        if (partition != null) {
            DataFrame df = mainApplication.getActiveDataFrame();
            String[] sel_names = ds.getSelectedData();
            int m = sel_names.length-1;
            String[] new_names = new String[m];

            for(int i=0;i<m;i++) new_names[i] = "ilr." + Integer.toString(i+1);
            boolean selection[] = df.getValidCompositions(sel_names);
            double data[][] = df.getNumericalData(sel_names);
            double vdata[][] = coda.Utils.reduceData(data, selection);

            double ilr[][] = CoDaStats.transformRawILR(vdata, partition);
            df.addData(new_names, coda.Utils.recoverData(ilr, selection));
            CoDaPackMain.outputPanel.addOutput(
                    new OutputILRPartition(sel_names, partition));
            mainApplication.updateDataFrame(df);
            setVisible(false);
        }else{
            JOptionPane.showMessageDialog(this, "<html>You must define a partition</html>");
        }
    }
}
