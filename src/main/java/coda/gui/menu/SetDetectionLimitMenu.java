/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió - David Gamez
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

import coda.*;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Arrays;

import javax.swing.*;


/**
 *
 * @author david
 */
public class SetDetectionLimitMenu extends AbstractMenuDialog{
    private static final String yamlUrl = CoDaPackConf.helpPath + "Irregular data.Set Detection Limit.yaml";
    private static final String helpTitle = "Set Detection Limit Help";
      
    JRadioButton B1 = new JRadioButton("Detection Limit", true);
    JTextField P1 = new JTextField("0.01", 5);
    JRadioButton B2 = new JRadioButton("Column minimum");
    JRadioButton B3 = new JRadioButton("Overall minimum");
    
    public SetDetectionLimitMenu(CoDaPackMain mainApp) {
        super(mainApp, "Set Detection Limit", false);
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);

        this.optionsPanel.setLayout(new BoxLayout(this.optionsPanel, BoxLayout.PAGE_AXIS));

        JPanel PB1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        PB1.add(B1);
        PB1.add(P1);
        PB1.setMaximumSize(new Dimension(1000, 30));
        PB1.setAlignmentX(0);
        B2.setAlignmentX(0);
        B3.setAlignmentX(0);
        
        this.optionsPanel.add(Box.createRigidArea(new Dimension(15,15)));
        this.optionsPanel.add(PB1);
        this.optionsPanel.add(B2);
        this.optionsPanel.add(B3);

        ButtonGroup group = new ButtonGroup();
        group.add(B1);
        group.add(B2);
        group.add(B3);
        
    }

    @Override
    public void acceptButtonActionPerformed() {
        //Creem tres booleans per a saber la opció triada
        boolean manualDL = B1.isSelected();
        boolean colMinDL = B2.isSelected();
        boolean globalDL = B3.isSelected();

        String[] sel_names = ds.getSelectedData();

        double detectionLimit[] = new double[sel_names.length]; 
        if(manualDL) {
            Arrays.fill(detectionLimit, Double.parseDouble(P1.getText()));
        }
        if(colMinDL){
            detectionLimit = BasicStats.colMin(df.getNumericalData(sel_names));
        }
        if(globalDL){
            Arrays.fill(detectionLimit, BasicStats.min(df.getNumericalData(sel_names)));
        }
        for(int j = 0; j < sel_names.length; j++){
            String sel_name = sel_names[j];
            Variable var = df.get(sel_name);
            for(int i = 0; i < var.size(); i++){
                //Comprovem si es tracta d'una instància de Zero
                if (var.get(i) instanceof Zero) {
                    //Si és un Zero li afegim el Detection Limit
                    df.get(sel_name).set(i, new Zero(detectionLimit[j]));
                }
            }
        }        

        //Actualitzem el DataFrame
        mainApplication.updateDataFrame(df);
       
        setVisible(false);
    }
        
}
