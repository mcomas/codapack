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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
/**
 *
 * @author mcomas
 */
public class ClosureDataMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    String selected[];
    JTextField closureTo;
    JLabel text1 = new JLabel("Closure");
    public ClosureDataMenu(final CoDaPackMain mainApp){
        super(mainApp, "Closure Data Menu", false);
        closureTo =  new JTextField(5);
        closureTo.setText("1.0");
        optionsPanel.add(text1);
        optionsPanel.add(closureTo);
    }

    @Override
    public void acceptButtonActionPerformed() {
        //Comprobation that closuredTo is a double value is needed
        try{

            String selectedNames[] = ds.getSelectedData();
            DataFrame dataFrame = mainApplication.getActiveDataFrame();

            boolean selection[] = dataFrame.getValidCompositionsWithZeros(selectedNames);
            double dlevel[][] = dataFrame.getDetectionLevel(selectedNames);
            double[][]data = dataFrame.getNumericalData(selectedNames);
            
            double[][]vdata = coda.Utils.reduceData(data, selection);

            double closure = Double.parseDouble(closureTo.getText());
            if(closure < 0){
                JOptionPane.showMessageDialog(this, "Closure value must be positive");
                return;
            }
            String[] new_names = new String[selectedNames.length];
            for(int i=0;i<selectedNames.length;i++) new_names[i] = "clo_" + selectedNames[i];

            // = CoDaPack.center(df.getNumericalDataZeroFree(sel_names));
            double[][]d = coda.Utils.recoverData(CoDaStats.closure(vdata,closure), selection);
            
            double []total = CoDaStats.rowSum(data);
            for(int i=0;i<new_names.length;i++){
                Variable v = new Variable(new_names[i], d[i]);
                for(int j=0;j<data[i].length;j++){
                    if(dlevel[i][j] != 0 & data[i][j] == 0){
                        v.set(j, new Zero( dlevel[i][j] * closure / total[j] ));
                    }
                }
                dataFrame.addData(new_names[i], v);
            }
            
            
            mainApplication.updateDataFrame(dataFrame);
            setVisible(false);
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Closure value must be a real number");
        }
    }
}

