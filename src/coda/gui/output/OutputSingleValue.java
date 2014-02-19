/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.output;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author marc
 */
public class OutputSingleValue implements OutputElement{
    String label;
    double value;
    int type;
    public OutputSingleValue(String l, int v){
        type = 1;
        this.label = l;
        value = v;
    }
    public OutputSingleValue(String l, double v){
        type = 2;
        this.label = l;
        value = v;
    }
    public String printHTML(String html) {
        if(type == 1){
            html += "<b>" + label + ":</b><br>" + (int)value + "<br>";
        }else if(type ==2){
            html += "<b>" + label + ":</b><br>" + decimalFormat.format(value) + "<br>";
        }

        return html;
    }

    public void printText(Writer b) throws IOException{

        b.write(label + "\n" + separator);
        if (type == 1) {
            b.write( Integer.toString((int)value));
        } else if (type == 2) {
            b.write(decimalFormat.format(value));
        }
        b.write("\n");
  

    }

}
