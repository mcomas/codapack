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
import static coda.gui.CoDaPackMain.outputPanel;
import static coda.gui.CoDaPackMain.re;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    JTextField closure = new JTextField("1.0");
    double percentatgeDL = 0.65;
    JTextField usedPercentatgeDL;
    JLabel l_usedPercentatgeDL = new JLabel("DL proportion");
    JCheckBox performClosure;
    JLabel lclosure = new JLabel("Closure to");
    JTextField closureTo;
    Rengine re;

    /**
     * *** METODES DE CLASSE ****
     */
    public ZeroReplacementRMenu(final CoDaPackMain mainApp, Rengine r) {

        super(mainApp, "Zero Replacement R Menu", false);
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
        closureTo.setText("1.0");

        optionsPanel.add(performClosure);
        optionsPanel.add(lclosure);
        optionsPanel.add(closureTo);
    }

    @Override
    public void acceptButtonActionPerformed() {
        
        JFrame frame = new JFrame();
        frame.setTitle("Message");

        String percentatgeDL = "delta=" + usedPercentatgeDL.getText(); // we get the percentatgeDL
        String label = "label=0"; // label for default its 0

        DataFrame df = mainApplication.getActiveDataFrame();
        String[] sel_names = ds.getSelectedData(); // we get the names of selected variables
        int m = sel_names.length; // number of selected variables
        

        if (m >= 2) { // minimum two variables selected to do something
            
           
            String[] new_names = new String[m];
            for (int i = 0; i < m; i++) {
                new_names[i] = "z_" + sel_names[i];
            }
            
            boolean containsZero = false;

            double data[][] = df.getNumericalData(sel_names);
            
            for(int i =0; (i < data.length) && (!containsZero);i++){
                for(int j=0; (j < data[i].length) && (!containsZero);j++){
                    if(data[i][j] == 0) containsZero = true;
                }
            }
            
            if(containsZero){ // if contains zero then we do something
            
                // transform the data to string for R
                String dataR = "X <- matrix(c(";
                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < data[i].length; j++) {
                        dataR += String.valueOf(data[i][j]) + ",";
                    }
                }
                dataR = dataR.substring(0, dataR.length() - 1); // we delete the last ,
                dataR += "),byrow=FALSE,ncol=" + String.valueOf(m) + ")";

                re.eval(dataR);
                re.eval("DL = matrix(as.numeric(X == 0), ncol=" + String.valueOf(m) + ")");
                re.eval("out <- capture.output(zCompositions::multRepl(X,label=0,dl=DL," + percentatgeDL + "))");
                //OutputElement e;
                String[] out = re.eval("out").asStringArray();

                // extract the numbers of out
                double resultat[][] = new double[data.length][data[0].length];
                int aux = 0; // y
                Pattern p = Pattern.compile("(\\d+(?:\\.\\d+))");
                for (int i = 1; i < out.length; i++) {
                    Matcher match = p.matcher(out[i]);
                    int aux2 = 0; //x
                    while (match.find()) {
                        double d = Double.parseDouble(match.group(1));
                        resultat[aux2][aux] = d;
                        aux2++;
                    }
                    aux++;
                }

                /*e = new OutputForR(re.eval("out").asStringArray());

                outputPanel.addOutput(e);*/
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
                JOptionPane.showMessageDialog(frame, "Please select a variable that contains some 0");
            }
        }
        else{
            JOptionPane.showMessageDialog(frame, "Please select minimum two variables");
        }
    }
}
