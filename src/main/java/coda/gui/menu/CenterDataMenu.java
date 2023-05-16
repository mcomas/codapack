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

package coda.gui.menu;

import coda.CoDaStats;
import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.CoDaPackConf;
import coda.gui.output.OutputVector;
import coda.gui.utils.DataSelector1to1;

import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;


/**
 *
 * @author mcomas
 */
public class CenterDataMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Data.Operations.Centering.yaml";
    private static final String helpTitle = "Center Data Help Menu";
    
    String selected[];
    JCheckBox centerCheck;
    DataFrame dataFrame;
    ArrayList<String> names;
    
    public CenterDataMenu(final CoDaPackMain mainApp){
        super(mainApp, "Center Data Menu", new DataSelector1to1(mainApp.getActiveDataFrame(), false));
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        
        centerCheck = new JCheckBox("Show Center", true);
        optionsPanel.add(centerCheck);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
    }

    @Override
    public void acceptButtonActionPerformed() {
        //Comprobation that closuredTo is a double value is needed
        try{
            
            String selectedNames[] = ds.getSelectedData();
            dataFrame = mainApplication.getActiveDataFrame();

            boolean selection[] = dataFrame.getValidCompositions(selectedNames);
            double[][] data = dataFrame.getNumericalData(selectedNames);
            double[][] vData = coda.Utils.reduceData(data, selection);
            
            double [] center = CoDaStats.center(vData);
                         
            if(centerCheck.isSelected()){
                //mainApplication.outputPanel.writeCenter(selectedNames, center);
                //CoDaPackMain.outputPanel.writeCenter(selectedNames, center);
                CoDaPackMain.outputPanel.addOutput(
                        new OutputVector("Center", selectedNames, center));
            }
            for(int i=0;i<center.length;i++) center[i] = 1 / center[i];

            String[] new_names = new String[selectedNames.length];
            for(int i=0; i<selectedNames.length;i++)
                new_names[i] = "c_" + selectedNames[i];

            double [][] cData = CoDaStats.closure(
                    CoDaStats.perturbateData(vData, center),1);
            dataFrame.addData(new_names, 
                    coda.Utils.recoverData(cData, selection));
            mainApplication.updateDataFrame(dataFrame);
            setVisible(false);
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Closured value must be a double");
        }
        
    }
    
    public DataFrame getDataFrame(){
        return this.dataFrame;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
}

