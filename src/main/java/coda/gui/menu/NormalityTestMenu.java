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
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputNormalityTest;
import coda.gui.utils.BinaryPartitionSelect;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;

/**
 *
 * @author mcomas
 */
public class NormalityTestMenu extends AbstractMenuDialogWithILR{
    
    DataFrame df;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Statistics.Additive Logistic Normality Tests.yaml";
    private static final String helpTitle = "Log-Ratio Normality Test Help";

    public NormalityTestMenu(final CoDaPackMain mainApp){
        super(mainApp, "Log-Ratio Normality Test", false);
        super.setHelpConfig(yamlUrl, helpTitle);

        JButton defaultPart = new JButton("Default Partition");
        JButton manuallyPart = new JButton("Define Manually");
        
        this.optionsPanel.add(defaultPart);
        defaultPart.addActionListener(new ActionListener(){
            
            public void actionPerformed(ActionEvent evt){
                setPartition(CoDaStats.defaultPartition(ds.getSelectedData().length));
            }
        });
        
        this.optionsPanel.add(manuallyPart);
        manuallyPart.addActionListener(new ActionListener(){
            
            public void actionPerformed(ActionEvent evt){
                initiatePartitionMenu();
            }
        });
        
    }
    
    public void initiatePartitionMenu(){
        BinaryPartitionSelect binaryMenu = new BinaryPartitionSelect(this, ds.getSelectedData());
        binaryMenu.setVisible(true);
    }
    
    
    @Override
    public void acceptButtonActionPerformed() {
        String selectedNames[] = ds.getSelectedData();
        df = mainApplication.getActiveDataFrame();

        boolean[] selection = getValidComposition(df, selectedNames);
        int [] mapping = df.getMapingToData(selectedNames, selection);
        double[][] data = df.getNumericalData(selectedNames, mapping);

        double[][] ilr = super.getBasis();
        String names[] = new String[selectedNames.length-1];
        for(int i=0;i<names.length;i++)
            //names[i] = "ilr(" + selectedNames[i] + "," + selectedNames[names.length] + ")";
            names[i] = "ilr " + String.valueOf(i+1);

        int d = ilr.length;
        int n = ilr[0].length;
        double marginal[][] = new double[d][3];
        double bivariate[][][] = new double[d][d][3];


        for(int i=0;i<d;i++)
            marginal[i] = CoDaStats.marginalUnivariateTest(ilr[i]);

        for(int i=0;i<d;i++)
            for(int j=i+1;j<d;j++)
                bivariate[i][j] = CoDaStats.bivariateAngleTest(ilr[i], ilr[j]);

        double radius[] = CoDaStats.radiusTest(ilr);

        ArrayList<OutputElement> outputs = new ArrayList<OutputElement>();
        outputs.add(new OutputNormalityTest(names, marginal, bivariate, radius));

        CoDaPackMain.outputPanel.addOutput(outputs);
        setVisible(false);
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }

}
