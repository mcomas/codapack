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
import coda.CoDaStats;
import coda.DataFrame;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputCompStatsSummary;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputSingleValue;
import coda.gui.output.OutputText;
import coda.gui.output.OutputVariationArray;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author mcomas
 */
public class CompStatsSummaryMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Statistics.Compositional Statistics Summary.yaml";
    private static final String helpTitle = "Numerical Summary Help";
    JCheckBox centerCheck;
    JCheckBox vararrayCheck;
    JCheckBox totalvarCheck;
    JTextField  percentileField;
    JCheckBox percentileCheck;
    JButton vararrayOptions;
    DataFrame df;
    ArrayList<String> names;
    
    public CompStatsSummaryMenu(final CoDaPackMain mainApp){
        super(mainApp, "Numerical Summary", true);
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);

        centerCheck = new JCheckBox("Center", true);

        percentileCheck  = new JCheckBox("Percentile", false);
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
        
        vararrayCheck = new JCheckBox("Variation Array", true);
        vararrayOptions = new JButton("Options", new ImageIcon(getClass().getResource(CoDaPackMain.RESOURCE_PATH + "options.png")));
        //vararrayOptions.setSize(new Dimension(16,16));
        vararrayOptions.setAlignmentX(RIGHT_ALIGNMENT);

        totalvarCheck = new JCheckBox("Total Variance", true);
        
        optionsPanel.add(centerCheck);
        optionsPanel.add(percentileCheck);
        optionsPanel.add(percentileField);
        
        optionsPanel.add(vararrayCheck);              
        optionsPanel.add(totalvarCheck);
        //optionsPanel.add(vararrayOptions);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
        

        
    }

    @Override
    public void acceptButtonActionPerformed() {
        String selectedNames[] = ds.getSelectedData();
        df = mainApplication.getActiveDataFrame();
        
        boolean[] selection = getValidComposition(df, selectedNames);
        int sizeBefore = selection.length;
        int [] mapping = df.getMapingToData(selectedNames, selection);
        double[][] data = CoDaStats.closure(df.getNumericalData(selectedNames, mapping));
        int sizeAfter = data[0].length;

        String groupedBy = ds.getSelectedGroup();
        int[] groups = null;
        String[] categories = null;

        ArrayList<OutputElement> outputs = new ArrayList<OutputElement>();
        outputs.add(new OutputText("Compositional statistics summary:"));
        outputs.add(new OutputSingleValue("NA's", sizeBefore-sizeAfter));
        if(groupedBy != null){
            /*
             * Obtaining the groups and the categories for non lost data
             */
            groups  = coda.Utils.reduceData(df.getDefinedGroup(groupedBy), selection);
            categories =  coda.Utils.getCategories(df.getCategoricalData(groupedBy), selection);
            //if(sizeAfter != sizeBefore) outputs.add(new OutputSingleValue("NA", sizeAfter-sizeBefore));
            for(int gr=0;gr<categories.length;gr++){
                outputs.add(new OutputText("-- Group " + categories[gr] + " --"));
                double [][] xdata = coda.Utils.reduceData(data, groups, gr);

                outputs.add(new OutputSingleValue("Sample size", xdata[0].length));                
                if(centerCheck.isSelected() || percentileCheck.isSelected()){
                    double [] center = null;
                    if(centerCheck.isSelected()){
                        center = CoDaStats.center(xdata);
                        //outputs.add(new OutputVector("Center", selectedNames, center));
                    }
                    double percentile[][] = null;
                    int [] values = null;
                    if(percentileCheck.isSelected()){
                        String[] s = percentileField.getText().split(" ");
                        values = new int[s.length];
                        for(int i=0;i<s.length;i++)
                            values[i] = Integer.parseInt(s[i]);
                        percentile  = BasicStats.percentile(xdata,values);
                        //outputs.add(new OutputPercentiles(selectedNames, values, percentile));
                    }
                    outputs.add(
                            new OutputCompStatsSummary(selectedNames, center, values, percentile));
                }
                if(vararrayCheck.isSelected()){
                    double [][] varArray = CoDaStats.variationArray(xdata);
                    double clrVar[] = BasicStats.variance(CoDaStats.transformRawCLR(xdata));
                    outputs.add(new OutputVariationArray(selectedNames, varArray, clrVar, totalvarCheck.isSelected()));
                }else if(totalvarCheck.isSelected()){
                    double [][] varArray = CoDaStats.variationArray(xdata);
                    //double clrVar[] = BasicStats.variance(CoDaStats.transformRawCLR(xdata));
                    double totalVariance = CoDaStats.totalVariance(varArray);
                    outputs.add(new OutputSingleValue("Total variance", totalVariance));
                }
            }
        }else{
            outputs.add(new OutputSingleValue("Sample size", data[0].length));
            if(centerCheck.isSelected() || percentileCheck.isSelected()){
                double [] center = null;
                if(centerCheck.isSelected()){
                    center = CoDaStats.center(data);
                    //outputs.add(new OutputVector("Center", selectedNames, center));
                }
                double percentile[][] = null;
                int [] values = null;
                if(percentileCheck.isSelected()){
                    String[] s = percentileField.getText().split(" ");
                    values = new int[s.length];
                    for(int i=0;i<s.length;i++)
                        values[i] = Integer.parseInt(s[i]);
                    percentile  = BasicStats.percentile(data,values);
                    //outputs.add(new OutputPercentiles(selectedNames, values, percentile));
                }
                outputs.add(
                   new OutputCompStatsSummary(selectedNames, center, values, percentile));
            }
            /*
            if(centerCheck.isSelected()){
                double [] center = CoDaStats.center(data);
                outputs.add(new OutputVector("Center", selectedNames, center));
            }
            if(percentileCheck.isSelected()){
                String[] s = percentileField.getText().split(" ");
                int [] values = new int[s.length];
                for(int i=0;i<s.length;i++) values[i] = Integer.parseInt(s[i]);
                double percentile[][]  = BasicStats.percentile(data,values);

                outputs.add(new OutputPercentiles(selectedNames, values, percentile));
            }
            */
            if(vararrayCheck.isSelected()){
                double [][] varArray = CoDaStats.variationArray(data);
                double clrVar[] = BasicStats.variance(CoDaStats.transformRawCLR(data));
                outputs.add(new OutputVariationArray(selectedNames, varArray, clrVar, totalvarCheck.isSelected()));
            }else if(totalvarCheck.isSelected()){
                double [][] varArray = CoDaStats.variationArray(data);
                double totalVariance = CoDaStats.totalVariance(varArray);
                
                outputs.add(new OutputSingleValue("Total variance", totalVariance));
            }
        }
        CoDaPackMain.outputPanel.addOutput(outputs);
        setVisible(false);
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
}
