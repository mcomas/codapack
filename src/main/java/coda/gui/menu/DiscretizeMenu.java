/** 
 *  Copyright 2011-2016 Marc Comas - Santiago Thió
 *
 *  This file is part of CoDaPack.
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

import coda.DataFrame;
import coda.Variable;
import coda.gui.CoDaPackMain;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.rosuda.JRI.RList;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author Guest2
 */
public class DiscretizeMenu extends AbstractMenuDialog{
    
    Rengine re;
    
    public static final long serialVersionUID = 1L;
    
    /** options var **/
    
    String[] options = {"interval", "frequency", "cluster"};
    JComboBox optionsList = new JComboBox(options);
    JLabel methodLabel = new JLabel("Method :");
    JLabel breaksLabel = new JLabel("Nº breaks: ");
    JTextField breaksField = new JTextField(5);
    
    public DiscretizeMenu(final CoDaPackMain mainApp, Rengine r){
        super(mainApp, "Discretize/Segment Menu", false);
        re = r;
        
        optionsPanel.add(methodLabel);
        optionsPanel.add(optionsList);
        optionsPanel.add(breaksLabel);
        breaksField.setText("3");
        optionsPanel.add(breaksField);
    }
    
    @Override
    public void acceptButtonActionPerformed(){
        
        DataFrame df = mainApplication.getActiveDataFrame();
        
        String[] sel_names = ds.getSelectedData();
        
        if(sel_names.length == 1){
            
            re.assign("x", df.get(sel_names[0]).getNumericalData());
            
            re.eval("res <- as.numeric(arules::discretize(x, method = \"" + optionsList.getSelectedItem().toString() + "\", breaks = " + breaksField.getText() + "))");
            double[] res = re.eval("res").asDoubleArray();
            String[] resString = new String[res.length];
            for(int i=0; i < res.length;i++) resString[i] = String.valueOf((int)res[i]);
            String nameOfVar = "discret " + sel_names[0];
            if(df.getNames().contains(nameOfVar)){
                int aux = 1;
                while(df.getNames().contains(nameOfVar + Integer.toString(aux))){
                    aux++;
                }
                nameOfVar = nameOfVar + Integer.toString(aux);
            }
            df.addData(nameOfVar, new Variable(nameOfVar,resString));
            mainApplication.updateDataFrame(df);
            this.dispose();
        }
        else{
            JOptionPane.showMessageDialog(null,"Please selecte one variable");
        }        
    }
}
