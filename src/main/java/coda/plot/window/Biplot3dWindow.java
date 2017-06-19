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

package coda.plot.window;

import coda.CoDaStats;
import coda.DataFrame;
import coda.ext.jama.SingularValueDecomposition;
import coda.plot.Biplot3dDisplay;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author mcomas
 */
public class Biplot3dWindow extends RealPlot3dWindow{
    private Biplot3dDisplay biplot;
    SingularValueDecomposition svd;
    
    JSlider biplotType;
    JTextField gammaField;
    
    public Biplot3dWindow(DataFrame dataframe, Biplot3dDisplay display, String title, SingularValueDecomposition s){
        super(dataframe, display, title);
        biplot = display;
        svd = s;

        JPanel covFormPane = new JPanel();
        covFormPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        covFormPane.add(new JLabel("Cov."));

        biplotType = new JSlider(JSlider.HORIZONTAL,0, 100, 0);
        biplotType.setPreferredSize(new Dimension(50,40));
        //biplotType.setPreferredSize(new Dimension(50,30));
        biplotType.addChangeListener(new ChangeListener(){
            
            public void stateChanged(ChangeEvent e) {
                double gamma = (double)biplotType.getValue()/100.0;

                double GH[][][] = CoDaStats.biplot(svd, 3, gamma);
                biplot.setAlpha(gamma);
                biplot.setNewData(GH[0], GH[1]);
                gammaField.setEnabled(false);
                gammaField.setText(Double.toString(gamma));
                gammaField.setEnabled(true);
            }
        });
        covFormPane.add(biplotType);

        covFormPane.add(new JLabel("Form"));

        gammaField = new JTextField("0", 5);
        gammaField.addActionListener( new ActionListener(){
            
            public void actionPerformed(ActionEvent e) {
                double gamma = Double.parseDouble(gammaField.getText());

                double GH[][][] = CoDaStats.biplot(svd, 3, gamma);
                biplot.setAlpha(gamma);
                biplot.setNewData(GH[0], GH[1]);
                biplotType.setEnabled(false);
                biplotType.setValue((int) (gamma * 100));
                biplotType.setEnabled(true);
            }
        });
        final JCheckBox showData = new JCheckBox("Observations");
        showData.setSelected(true);
        showData.addActionListener(new ActionListener(){
            
            public void actionPerformed(ActionEvent e) {
                biplot.showData(showData.isSelected());
            }
        });
        covFormPane.add(gammaField);
        particularControls1.add(showData);
        particularControls1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        particularControls1.add(covFormPane);
    }

}
