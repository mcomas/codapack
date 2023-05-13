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
import coda.gui.utils.DataSelector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author Guest2
 */
public class ZeroReplacementRMenu extends AbstractMenuDialog {

    /**
     * *** ATRIBUTES ****
     */
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Irregular Data. Non-Parametric Zero Replacement.yaml";
    private static final String helpTitle = "Non parametric zero replacement Help Menu";
    JTextField closure = new JTextField("1.0");
    double percentatgeDL = 0.65;
    JTextField usedPercentatgeDL;
    JLabel l_usedPercentatgeDL = new JLabel("DL proportion");
    JCheckBox performClosure;
    JLabel lclosure = new JLabel("Closure to");
    JTextField closureTo;
    //JCheckBox performMax;
    //JLabel lmax = new JLabel("Use minimum positive value observed");
    Rengine re;
    DataFrame df;
    ArrayList<String> names;

    /**
     * *** METODES DE CLASSE ****
     */
    public ZeroReplacementRMenu(final CoDaPackMain mainApp, Rengine r) {

        super(mainApp, "Non parametric zero replacement", new DataSelector(mainApp.getActiveDataFrame(), false));
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);
        re = r;

        usedPercentatgeDL = new JTextField(5);
        usedPercentatgeDL.setText("0.65");
        optionsPanel.add(l_usedPercentatgeDL);
        optionsPanel.add(usedPercentatgeDL);

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

        optionsPanel.add(performClosure);
        optionsPanel.add(lclosure);
        optionsPanel.add(closureTo);
        this.names = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
        //performMax = new JCheckBox("", false);
        //performMax.setSelected(true);
        //optionsPanel.add(lmax);
        //optionsPanel.add(performMax);
    }

    @Override
    public void acceptButtonActionPerformed() {
        
        JFrame frame = new JFrame();
        frame.setTitle("Message");

        String fracDL = usedPercentatgeDL.getText(); // we get the percentatgeDL
        String label = "label=0"; // label for default its 0
        
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
                    
                    // put the output on dataframe
                    if (performClosure.isSelected()) {
                        double vclosureTo = Double.parseDouble(closureTo.getText());
                        df.addData(new_names, CoDaStats.closure(resultat, vclosureTo));
                    } else {
                        df.addData(new_names, resultat);
                    }

                    mainApplication.updateDataFrame(df);

                    setVisible(false);
                }
                else{
                    JOptionPane.showMessageDialog(frame, "Please set detection limit for all zeros");
                }
            }
            else{
                JOptionPane.showMessageDialog(frame, "No data with zeros");
            }
        }
        else{
            JOptionPane.showMessageDialog(frame, "Please select minimum two variables");
        }
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
}
