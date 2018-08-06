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
import static coda.CoDaStats.closure;
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
public class PerturbateDataMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    String selected[];
    JTextField perturbateWith;
    JLabel text1 = new JLabel("Perturbation");
    JCheckBox performClosure;
    JLabel lclosure = new JLabel("Closure to");
    JTextField closureTo;
    public PerturbateDataMenu(final CoDaPackMain mainApp){
        super(mainApp, "Perturbate Data Menu", false);
        perturbateWith =  new JTextField("1.0 1.0 1.0", 14);
        //perturbateWith.setText();
        optionsPanel.add(text1);
        optionsPanel.add(perturbateWith);
        
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
            DataFrame dataFrame = mainApplication.getActiveDataFrame();

            String[] v = perturbateWith.getText().split(" ");

            double [] perturbation = new double[v.length];
            for(int i=0;i<v.length;i++) perturbation[i] = Double.parseDouble(v[i]);

            boolean selection[] = dataFrame.getValidCompositionsWithZeros(selectedNames);
            
            double dlevel[][] = dataFrame.getDetectionLevel(selectedNames);
            double[][] data = dataFrame.getNumericalData(selectedNames);
            
            double[][] vdata = coda.Utils.reduceData(data, selection);

            String[] new_names = new String[selectedNames.length];
            for(int i=0;i<selectedNames.length;i++) new_names[i] = Double.toString(perturbation[i]) + ".x." + selectedNames[i];

            double[][] dpert = CoDaStats.perturbateData(vdata, perturbation);
            if(performClosure.isSelected()){
                double vclosureTo = Double.parseDouble(closureTo.getText());
                double[][]d = coda.Utils.recoverData(CoDaStats.closure(dpert, vclosureTo), selection);
                
                double []total = CoDaStats.rowSum(data);
                for(int i=0;i<new_names.length;i++){
                    Variable var = new Variable(new_names[i], d[i]);
                    for(int j=0;j<data[i].length;j++){
                        if(dlevel[i][j] != 0 & data[i][j] == 0){
                            var.set(j, new Zero( (dlevel[i][j] /total[j]) * perturbation[i]  ));
                        }
                    }
                    dataFrame.addData(new_names[i], var);
                } 
            }else{
                double[][]d = coda.Utils.recoverData(dpert, selection);
                for(int i=0;i<new_names.length;i++){
                    Variable var = new Variable(new_names[i], d[i]);
                    for(int j=0;j<data[i].length;j++){
                        if(dlevel[i][j] != 0 & data[i][j] == 0){
                            var.set(j, new Zero( dlevel[i][j] * perturbation[i] ));
                        }
                    }
                    dataFrame.addData(new_names[i], var);
                } 
            }

            
            mainApplication.updateDataFrame(dataFrame);
            setVisible(false);
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Closured value must be a double");
        }
        
    }
}

