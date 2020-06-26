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
import java.util.ArrayList;

import javax.swing.*;

/**
 * Created by david on 3/06/16.
 */
public class TransformationILRRawMenu extends AbstractMenuDialogWithILR{
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Data.Transformations.ILR-Raw.yaml";
    private static final String helpTitle = "ILR-Raw Transform Help Menu";
    
    DataFrame df;
    ArrayList<String> names;

    public TransformationILRRawMenu(final CoDaPackMain mainApp){
        super(mainApp, "ILR-Raw Transform Menu", false);
        super.setHelpConfig(yamlUrl, helpTitle);

        JButton defaultPart = new JButton("Default Partition");
        //JButton manuallyPart = new JButton("Define Manually");
        optionsPanel.add(defaultPart);
        defaultPart.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setPartition(CoDaStats.defaultPartition(ds.getSelectedData().length+1));
            }
        });
        /*optionsPanel.add(manuallyPart);
        manuallyPart.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initiatePartitionMenu();
            }
        });*/
        
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());

    }

    /*public void initiatePartitionMenu(){
        System.out.println("Data seleccionats: "+ds.getSelectedData().length);
        System.out.println("Data seleccionats més 1: "+ds.getSelectedData().length+1);
        BinaryPartitionSelect binaryMenu = new BinaryPartitionSelect(this, ds.getSelectedData() );
        binaryMenu.setVisible(true);
    }*/

    @Override
    public void acceptButtonActionPerformed() {
        int[][] partition = getPartition();
        if (partition != null) {
            df = mainApplication.getActiveDataFrame();
            String[] sel_names = ds.getSelectedData();
            int m = sel_names.length+1;
            String[] new_names = new String[m];

            for(int i=0;i<m;i++) new_names[i] = "inv.ilr." + Integer.toString(i+1);

            df.addData(new_names, CoDaStats.closure(
                    CoDaStats.transformILRRaw(df.getNumericalData(sel_names), partition), 1));
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
