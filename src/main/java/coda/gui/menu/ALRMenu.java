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
import java.awt.TextField;
import javafx.collections.ObservableList;
import javafx.scene.control.RadioButton;

/**
 *
 * @author mcomas
 */
public class ALRMenu extends MenuDialog{
    public static final long serialVersionUID = 1L;

    TextField closure = new TextField("1.0");
    public ALRMenu(final CoDaPackMain mainApp, String title, ALRMenuOptions options){
        super(mainApp, title, options);
    }
    
    @Override
    public void acceptButtonActionPerformed() {
        ALRMenuOptions opt = (ALRMenuOptions)options;
        if(opt.ra.isSelected()){
            ObservableList<String> selectedItems = selector.getTargetItems();
            int K = selectedItems.size();
            int k = K-1;
            System.out.println();
            String[] sel_names = new String[K];
            for(int i =0; i < K; i++) sel_names[i] = (String)selectedItems.get(i);
            String[] new_names = new String[k];
            for(int i=0;i<k;i++){
                new_names[i] = "alr." + sel_names[i] + "_" + sel_names[k];
            }
            double X[][] = dataframe.getNumericalData(sel_names);
            double alr[][] = CoDaStats.transformRawALR(X);
            dataframe.addNumericalData(new_names, alr);
        }
        this.close();
    }    
}

