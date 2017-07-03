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

import coda.BasicStats;
import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputClasStatsSummary;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputSingleValue;
import coda.gui.output.OutputTableTwoEntries;
import coda.gui.output.OutputText;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author mcomas
 */
public class ClasStatsSummaryMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    JCheckBox meanCheck;
    JCheckBox stdDevCheck;
    JCheckBox covarianceCheck;
    JCheckBox correlationCheck;
    JTextField  percentileField;
    JCheckBox percentileCheck;
    public ClasStatsSummaryMenu(final CoDaPackMain mainApp){
        super(mainApp, "Numerical Summary", true);


        meanCheck = new JCheckBox("Mean", true);

        percentileCheck  = new JCheckBox("Percentile", true);
        percentileCheck.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent ce) {
                if(percentileCheck.isSelected()){
                    percentileField.setEnabled(true);
                }else{
                    percentileField.setEnabled(false);
                }
            }
        });
        percentileField = new JTextField("0 25 50 75 100",14);
        
        stdDevCheck = new JCheckBox("Standard Deviation", true);

        

        correlationCheck = new JCheckBox("Correlation Matrix", true);
        covarianceCheck = new JCheckBox("Covariance Matrix", false);
        
        
        optionsPanel.add(meanCheck);
        optionsPanel.add(percentileCheck);
        optionsPanel.add(percentileField);
        
        optionsPanel.add(stdDevCheck);
        optionsPanel.add(correlationCheck);
        optionsPanel.add(covarianceCheck);

        
    }

    @Override
    public void acceptButtonActionPerformed() {
        String selectedNames[] = ds.getSelectedData();
        DataFrame df = mainApplication.getActiveDataFrame();
        
        boolean[] selection = getValidData(df, selectedNames);
        int sizeBefore = selection.length;
        int [] mapping = df.getMapingToData(selectedNames, selection);
        double[][] data = df.getNumericalData(selectedNames, mapping);
        int sizeAfter = data[0].length;

        String groupedBy = ds.getSelectedGroup();
        int[] groups = null;
        String[] categories = null;

        ArrayList<OutputElement> outputs = new ArrayList<OutputElement>();
        outputs.add(new OutputText("Clasical statistics summary:"));
        outputs.add(new OutputSingleValue("NA's", sizeBefore-sizeAfter));
        if(groupedBy != null){
            groups  = coda.Utils.reduceData(df.getDefinedGroup(groupedBy), selection);
            categories =  coda.Utils.getCategories(df.getCategoricalData(groupedBy), selection);
            for(int gr=0;gr<categories.length;gr++){
                outputs.add(new OutputText("-- Group " + categories[gr] + " --"));
                double [][] xdata = coda.Utils.reduceData(data, groups, gr);

                outputs.add(new OutputSingleValue("Sample size", xdata[0].length));
                if(meanCheck.isSelected() || stdDevCheck.isSelected() || percentileCheck.isSelected()){
                    double [] mean = null;
                    if(meanCheck.isSelected()){
                        mean = coda.BasicStats.mean(xdata);
                    }
                    double [] sd = null;
                    if(meanCheck.isSelected()){
                        sd = BasicStats.sd(xdata);
                    }
                    double percentile[][] = null;
                    int [] values = null;
                    if(percentileCheck.isSelected()){
                        String[] s = percentileField.getText().split(" ");
                        values = new int[s.length];
                        for(int i=0;i<s.length;i++)
                            values[i] = Integer.parseInt(s[i]);
                        percentile  = BasicStats.percentile(xdata,values);
                    }
                    outputs.add(new OutputClasStatsSummary(selectedNames,mean,sd,values, percentile));
                }
                if(correlationCheck.isSelected()){
                    double[][] correlation = BasicStats.correlation(xdata);
                    outputs.add(new OutputTableTwoEntries("Correlation",
                                selectedNames, selectedNames, correlation));
                }
                if(covarianceCheck.isSelected()){
                    double[][] covariance = BasicStats.covariance(xdata);
                    outputs.add(new OutputTableTwoEntries("Covariance",
                                selectedNames, selectedNames, covariance));
                }
            }
        }else{
            outputs.add(new OutputSingleValue("Sample size", data[0].length));
            if(meanCheck.isSelected() || stdDevCheck.isSelected() || percentileCheck.isSelected()){
                double [] mean = null;
                if(meanCheck.isSelected()){
                    mean = coda.BasicStats.mean(data);
                }
                double [] sd = null;
                if(meanCheck.isSelected()){
                    sd = BasicStats.sd(data);
                }
                double percentile[][] = null;
                int [] values = null;
                if(percentileCheck.isSelected()){
                    String[] s = percentileField.getText().split(" ");
                    values = new int[s.length];
                    for(int i=0;i<s.length;i++)
                        values[i] = Integer.parseInt(s[i]);
                    percentile  = BasicStats.percentile(data,values);
                }
                outputs.add(new OutputClasStatsSummary(selectedNames,mean,sd,values, percentile));
            }
            if(correlationCheck.isSelected()){
                    double[][] correlation = BasicStats.correlation(data);
                    outputs.add(new OutputTableTwoEntries("Correlation",
                                selectedNames, selectedNames, correlation));
                }
            if(covarianceCheck.isSelected()){
                double[][] covariance = BasicStats.covariance(data);
                outputs.add(new OutputTableTwoEntries("Covariance",
                            selectedNames, selectedNames, covariance));
            }
        }
        CoDaPackMain.output.addOutput(outputs);
        setVisible(false);
    }
}
