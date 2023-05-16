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

import coda.BasicStats;
import coda.CoDaStats;
import coda.DataFrame;
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.utils.DataSelector1to1;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author Guest2
 */
public class ZeroReplacementRMenu extends AbstractMenuRBasedDialog {
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Irregular Data. Non-Parametric Zero Replacement.yaml";
    private static final String helpTitle = "Non parametric zero replacement Help Menu";
    //JTextField closure = new JTextField("1.0");

    JTextField B1 = new JTextField("0.65", 5);
    //JCheckBox performClosure;
    //JLabel lclosure = new JLabel("Closure to");
    //JTextField closureTo;


    /**
     * *** METODES DE CLASSE ****
     */
    public ZeroReplacementRMenu(final CoDaPackMain mainApp, Rengine r) {

        super(mainApp, "Non parametric zero replacement", new DataSelector1to1(mainApp.getActiveDataFrame(), false), r);
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
      
        this.optionsPanel.setLayout(new BoxLayout(this.optionsPanel, BoxLayout.PAGE_AXIS));

        JPanel PB1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        PB1.setMaximumSize(new Dimension(1000, 32));
        PB1.add(new JLabel("DL proportion"));  
        PB1.add(Box.createHorizontalStrut(10));
        PB1.add(B1);

        optionsPanel.add(PB1);

/* 
        performClosure = new JCheckBox("Closure result", false);
        performClosure.setSelected(true);
        performClosure.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (performClosure.isSelected()) {
                    closureTo.setEnabled(true);
                } else {
                    closureTo.setEnabled(false);
                }
            }
        });

        closureTo = new JTextField(5);
        closureTo.setText(mainApp.config.getClosureTo());

        this.optionsPanel.add(Box.createRigidArea(new Dimension(15,15)));
        optionsPanel.add(performClosure);
        optionsPanel.add(lclosure);
        optionsPanel.add(closureTo);
 */
    }

    @Override
    public void acceptButtonActionPerformed() {

        String fracDL = B1.getText(); // we get the percentatgeDL
        
        // configurem si es vol agafara els maxims de les columnes
        
        //boolean takeMin = true;
        //if(!performMax.isSelected()) takeMin = false;

        df = mainApplication.getActiveDataFrame();
        String[] sel_names = ds.getSelectedData(); // we get the names of selected variables
        int m = sel_names.length; // number of selected variables
        

        if (m >= 2) { // minimum two variables selected to do something
                       
            String[] new_names = new String[m];
            for (int i = 0; i < m; i++) {
                new_names[i] = "z_" + sel_names[i];
            }                        

            double data[][] = df.getNumericalData(sel_names);
            int numZeros = BasicStats.nZeros(data);
            
            if(numZeros > 0){ // if contains zero then we do something
                
                for(int i=0; i < data.length; i++){
                    re.assign(sel_names[i], data[i]);
                }
                re.eval("X = cbind(" + String.join(",", sel_names) + ")");
                
                double dlevel[][] = df.getDetectionLevel(sel_names);
                int numDlevel = BasicStats.nPositive(dlevel);
                
                if(numZeros == numDlevel){ // si tenim detection limit per tots els zeros llavors tot be
                         
                    for(int i=0; i < dlevel.length; i++){
                        re.assign(sel_names[i], dlevel[i]);
                    }
                    re.eval("DL = cbind(" + String.join(",", sel_names) + ")");

                   
                    String E = "RES = zCompositions::multRepl(X,label=0,dl=DL,frac=#FRAC#)";
                    re.eval(E.replace("#FRAC#", fracDL));

                   
                    double resultat[][] = re.eval("t(as.matrix(RES))").asDoubleMatrix();
                    df.addData(new_names, resultat);

                    // put the output on dataframe
                    /* 
                    if (performClosure.isSelected()) {
                        double vclosureTo = Double.parseDouble(closureTo.getText());
                        df.addData(new_names, CoDaStats.closure(resultat, vclosureTo));
                    } else {
                        df.addData(new_names, resultat);
                    }
                    */
                    mainApplication.updateDataFrame(df);

                    setVisible(false);
                }
                else{
                    JOptionPane.showMessageDialog(this, "Please set detection limit for all zeros");
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "No data with zeros");
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "Please select minimum two variables");
        }
    }
 
}
