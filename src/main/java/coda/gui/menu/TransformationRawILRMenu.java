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
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputILRPartition;
import coda.gui.utils.BinaryPartitionSelect;
import coda.gui.utils.DataSelector1to1;

import java.util.ArrayList;

import javax.swing.*;

/**
 * Created by david on 2/06/16.
 */
public class TransformationRawILRMenu extends AbstractMenuDialogWithILR{
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = "Data.Transformations.Raw-ILR.yaml";
    private static final String helpTitle = "Raw-ILR Transform Help Menu";
    
    DataFrame df;
    ArrayList<String> names;

    public TransformationRawILRMenu(final CoDaPackMain mainApp){
        super(mainApp, "Raw-ILR Transform Menu", new DataSelector1to1(mainApp.getActiveDataFrame(), false));
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
        
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());

    }

    public void initiatePartitionMenu(){
        BinaryPartitionSelect binaryMenu = new BinaryPartitionSelect(this, ds.getSelectedData() );
        binaryMenu.setVisible(true);
    }

    @Override
    public void acceptButtonActionPerformed() {
        int[][] partition = getPartition();
        if (partition != null) {
            df = mainApplication.getActiveDataFrame();
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
    
    public DataFrame getDataFrame(){
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
}
