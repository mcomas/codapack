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
import coda.Variable;
import coda.Zero;
import coda.gui.CoDaPackMain;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
/**
 *
 * @author mcomas
 */
public class PowerDataMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = "Help/Data.Operations.Power Transformation.yaml";
    private static final String helpTitle = "Power transformation Help Menu";
    
    String selected[];
    JTextField powerWith;
    JLabel text1 = new JLabel("Power");
    JCheckBox performClosure;
    JLabel lclosure = new JLabel("Closure to");
    JTextField closureTo;
    DataFrame dataFrame;
    
    public PowerDataMenu(final CoDaPackMain mainApp){
        super(mainApp, "Power transformation Menu", false);
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        
        powerWith =  new JTextField(5);
        powerWith.setText("1.0");
        optionsPanel.add(text1);
        optionsPanel.add(powerWith);
        
        performClosure = new JCheckBox("Closure result", false);
        performClosure.setSelected(true);
        performClosure.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(performClosure.isSelected()){
                    closureTo.setEnabled(true);
                }else{
                    closureTo.setEnabled(false);
                }
            }
        });
        closureTo =  new JTextField(5);
        closureTo.setText(mainApp.config.getClosureTo());
        
        optionsPanel.add(performClosure);
        optionsPanel.add(lclosure);
        optionsPanel.add(closureTo);
    }

    @Override
    public void acceptButtonActionPerformed() {
        //Comprobation that closuredTo is a double value is needed
        try{

            String selectedNames[] = ds.getSelectedData();
            dataFrame = mainApplication.getActiveDataFrame();

            boolean selection[] = dataFrame.getValidCompositionsWithZeros(selectedNames);
            
            double dlevel[][] = dataFrame.getDetectionLevel(selectedNames);
            double[][]data = dataFrame.getNumericalData(selectedNames);
            
            double[][]vdata = coda.Utils.reduceData(data, selection);

            double power = Double.parseDouble(powerWith.getText());

            String[] new_names = new String[selectedNames.length];
            for(int i=0;i<selectedNames.length;i++) new_names[i] = selectedNames[i] + "^" + Double.toString(power);

            // = CoDaPack.center(df.getNumericalDataZeroFree(sel_names));
            double[][] dpow = CoDaStats.powerData(vdata,power);
                        
            if(performClosure.isSelected()){
                double vclosureTo = Double.parseDouble(closureTo.getText());
                double[][]d = coda.Utils.recoverData(CoDaStats.closure(dpow, vclosureTo), selection);
                
                double []total = CoDaStats.rowSum(data);
                for(int i=0;i<new_names.length;i++){
                    Variable var = new Variable(new_names[i], d[i]);
                    for(int j=0;j<data[i].length;j++){
                        if(dlevel[i][j] != 0 & data[i][j] == 0){
                            var.set(j, new Zero(  Math.pow(dlevel[i][j]/total[j], power)  ));
                        }
                    }
                    dataFrame.addData(new_names[i], var);
                } 
            }else{
                double[][]d = coda.Utils.recoverData(dpow, selection);
                for(int i=0;i<new_names.length;i++){
                    Variable var = new Variable(new_names[i], d[i]);
                    for(int j=0;j<data[i].length;j++){
                        if(dlevel[i][j] != 0 & data[i][j] == 0){
                            var.set(j, new Zero(  Math.pow(dlevel[i][j], power) ));
                        }
                    }
                    dataFrame.addData(new_names[i], var);
                } 
            }

            mainApplication.updateDataFrame(dataFrame);
            setVisible(false);
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Power value must be a double");
        }
        
    }
    
    public DataFrame getDataFrame(){
        return this.dataFrame;
    }
    
}

