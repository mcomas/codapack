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
import coda.gui.CoDaPackConf;
import coda.gui.CoDaPackMain;
import coda.gui.utils.DataSelector;

import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author mcomas
 */
public class TransformationCLRMenu extends AbstractMenuDialog{
    public static final long serialVersionUID = 1L;
    private static final String yamlUrl = CoDaPackConf.helpPath + "Data.Transformations.CLR.yaml";
    private static final String helpTitle = "CLR Transform Help Menu";
    
    JRadioButton rc;
    JRadioButton cr;
    JTextField closure = new JTextField("1.0");
    DataFrame df;
    ArrayList<String> names;

    public TransformationCLRMenu(final CoDaPackMain mainApp){
        super(mainApp, "CLR Transform Menu", new DataSelector(mainApp.getActiveDataFrame(), false));
        super.setHelpMenuConfiguration(yamlUrl, helpTitle);

        JPanel opt = new JPanel();
        rc = new JRadioButton("Raw-CLR");
        rc.setSelected(true);
        cr = new JRadioButton("CLR-Raw");
        ButtonGroup group = new ButtonGroup();
        group.add(cr);
        group.add(rc);

        opt.add(rc);
        opt.add(cr);
        optionsPanel.add(opt);
        this.names  = new ArrayList<String>(mainApplication.getActiveDataFrame().getNames());
    }

    @Override
    public void acceptButtonActionPerformed() {
        if(rc.isSelected()){
            df = mainApplication.getActiveDataFrame();
            String[] sel_names = ds.getSelectedData();
            int m = sel_names.length;
            String[] new_names = new String[m];
            for(int i=0;i<m;i++) new_names[i] = "clr." + sel_names[i];
            boolean selection[] = df.getValidCompositions(sel_names);
            double data[][] = df.getNumericalData(sel_names);
            double vdata[][] = coda.Utils.reduceData(data, selection);

            double clr[][] = CoDaStats.transformRawCLR(vdata);
            df.addData(new_names, coda.Utils.recoverData(clr, selection));
            mainApplication.updateDataFrame(df);
        }else{
            df = mainApplication.getActiveDataFrame();
            String[] sel_names = ds.getSelectedData();
            int m = sel_names.length;
            String[] new_names = new String[m];
            for(int i=0;i<m;i++) new_names[i] = "inv.clr." + (i+1);
            df.addData(new_names, CoDaStats.closure(
                    CoDaStats.transformCLRRaw(df.getNumericalData(sel_names)), 1));
            mainApplication.updateDataFrame(df);
        }
        setVisible(false);
    }
    
    public DataFrame getDataFrame(){
        return this.df;
    }
    
    public ArrayList<String> getDataFrameNames(){
        return this.names;
    }
}

