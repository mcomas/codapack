/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.menu.CompStatsSummaryMenu;
import coda.io.ImportData;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author mcomas
 */
public class testNumericalStats {
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

        CompStatsSummaryMenu num = new CompStatsSummaryMenu(main);



        //binaryMenu.setData(names);

        num.setVisible(true);
    }
}
