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
        names[0] = df.getName(0);
        names[1] = df.getName(1);
        names[2] = df.getName(2);
        names[3] = df.getName(3);
        names[4] = df.getName(4);
        names[5] = df.getName(5);

        CoDaPackMain main = new CoDaPackMain();

        main.addDataFrame(df);

        CompStatsSummaryMenu num = new CompStatsSummaryMenu(main);



        //binaryMenu.setData(names);

        num.setVisible(true);
    }
}
