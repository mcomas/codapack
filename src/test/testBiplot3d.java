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

package test;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.menu.Biplot3dMenu;
import coda.gui.utils.DataSelector;
import coda.io.ImportData;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.DefaultListModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author mcomas
 */
public class testBiplot3d {
    public static void main(String args[]) throws FileNotFoundException, IOException, InvalidFormatException {
        //DataFrame df = ImportData.importXLS("/Users/marc/CoDa.xls", true);
        DataFrame df = ImportData.importXLS("/home/marc/software/codapack-dev/data/halimba.xls", true);

        CoDaPackMain main = new CoDaPackMain();

        main.addDataFrame(df);

        Biplot3dMenu biplot = new Biplot3dMenu(main);

        DataSelector dataSel = biplot.getDataSelector();

        DefaultListModel list = (DefaultListModel)dataSel.getAvailableData();
        // Prenem les 5 primeres dades
        int n = 5;
        while(list.getSize() > n)
            list.removeElementAt(list.getSize()-1);

        dataSel.setSelectedData(list);

        biplot.acceptButtonActionPerformed();
        //binaryMenu.setData(names);

        
        //ilr.setVisible(true);
    }
}
