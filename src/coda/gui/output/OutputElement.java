/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.output;

import coda.gui.CoDaPackConf;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;

/**
 *
 * @author marc
 */
public interface OutputElement {
    String separator = ";";
    public static DecimalFormat decimalFormat = CoDaPackConf.getDecimalOutputFormat();
    public String printHTML(String html);
    
    public void printText(Writer b) throws IOException;
    
}
