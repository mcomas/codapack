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
import coda.Utils;
import coda.gui.CoDaPackMain;
import coda.gui.output.OutputAtipicality;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author mcomas
 */
public class AtipicalityIndexMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    JLabel text1 = new JLabel("Threshold:");
    JTextField  thresholdField;
    DataFrame df;

    public AtipicalityIndexMenu(final CoDaPackMain mainApp){
        super(mainApp, "Atipicality Index Menu", false);

        thresholdField  = new JTextField("     0.95");
        optionsPanel.add(text1);
        optionsPanel.add(thresholdField);
    }
    @Override
    public void acceptButtonActionPerformed() {
        double threshold = Double.parseDouble(thresholdField.getText().trim());

        df = mainApplication.getActiveDataFrame();
        String[] sel_names = ds.getSelectedData();
        int m = sel_names.length;
        String[] new_names = new String[m];
        for(int i=0;i<m;i++) new_names[i] = "clr(" + sel_names[i] + ")";
        boolean selection[] = df.getValidCompositions(sel_names);
        double data[][] = df.getNumericalData(sel_names);
        double vdata[][] = CoDaStats.transformRawILR(coda.Utils.reduceData(data, selection),CoDaStats.defaultPartition(m));

        double atip[] = Utils.recoverData(CoDaStats.atipicalityIndex(vdata), selection);

        int count = 0;
        for(int i=0;i<atip.length;i++) count += atip[i] > threshold ? 1 : 0;

        int atipData[] = new int[count];
        count = 0;
        for(int i=0;i<atip.length;i++){
            if(atip[i] > threshold) atipData[count++] = (i+1);
        }

        CoDaPackMain.outputPanel.addOutput(
                        new OutputAtipicality(threshold, atipData));

        df.addData("atip", atip);
        mainApplication.updateDataFrame(df);

        setVisible(false);
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
}

