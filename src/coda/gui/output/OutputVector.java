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
public class OutputVector implements OutputElement{

    String label;
    String names[];
    double[] values;

    public OutputVector(String l, String n[], double[] v){
        label = l;
        
        names = new String[n.length];
        System.arraycopy(n, 0, names, 0, n.length);

        values = new double[v.length];
        System.arraycopy(v, 0, values, 0, v.length);
    }
    public String printHTML(String html) {
        html += "<b>" + label +":</b><br><table><tr><th></th>";
        for(int i=0;i<values.length;i++){
            html += "<th>" + names[i] + "</th>";
        }
        html += "</tr><tr><td></td>";
        for(int i=0;i<values.length;i++){
            html += "<td>" + decimalFormat.format(values[i]) + "</td>";
        }
        html += "</tr></table>";

        return html;
    }

    public void printText(Writer b) throws IOException{

        b.write(label + "\n");
        for(int i=0;i<values.length;i++){
            b.write(separator + names[i]);
        }
        b.write("\n");
        for(int i=0;i<values.length;i++){
            b.write(separator + decimalFormat.format(values[i]));
        }
        b.write("\n");

    }

}
