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
import coda.gui.utils.BinaryPartitionSelect;
import coda.io.ImportData;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author mcomas
 */
public class testBinaryPartition {
    public static void main(String args[]) throws FileNotFoundException, IOException, InvalidFormatException {
        //DataFrame df = ImportData.importXLS("/Users/marc/workspace/CoData.xls", true);
        DataFrame df = ImportData.importXLS("G:/Recerca/EIO_RECERCA/New CoDaPack/CoData.xls", true);

        String[] names = new String[6];
        names[0] = df.get(0).getName();
        names[1] = df.get(1).getName();
        names[2] = df.get(2).getName();
        names[3] = df.get(3).getName();
        names[4] = df.get(4).getName();
        names[5] = df.get(5).getName();

        CoDaPackMain main = new CoDaPackMain();

        main.addDataFrame(df);

        TransformationILRMenu ilr = new TransformationILRMenu(main);

        BinaryPartitionSelect binaryMenu = new BinaryPartitionSelect(ilr, names);

        //binaryMenu.setData(names);

        ilr.setVisible(true);
        //ilr.setVisible(true);
    }
}
