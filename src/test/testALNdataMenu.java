/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import coda.DataFrame;
import coda.gui.CoDaPackMain;
import coda.gui.menu.NormalSampleMenu;
import coda.io.ImportData;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author mcomas
 */
public class testALNdataMenu {
    public static void main(String[] args) throws FileNotFoundException, IOException, InvalidFormatException {
        DataFrame df = ImportData.importXLS("G:/Recerca/EIO_RECERCA/New CoDaPack/Halimba.xls", true);

        CoDaPackMain main = new CoDaPackMain();
        main.addDataFrame(df);
        main.setVisible(true);
        NormalSampleMenu alnMenu = new NormalSampleMenu(main);

    }
}
