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
public class OutputCenter implements OutputElement{
    String names[];
    double[] center;
    
    private OutputCenter(String n[], double[] c){
        names = new String[n.length];
        System.arraycopy(n, 0, names, 0, n.length);

        center = new double[c.length];
        System.arraycopy(c, 0, center, 0, c.length);
    }
    public String printHTML(String html) {
        html += "<b>Center:</b><br><table><tr>";
        for(int i=0;i<center.length;i++){
            html += "<th>" + names[i] + "</th>";
        }
        html += "</tr><tr>";
        for(int i=0;i<center.length;i++){
            html += "<td>" + decimalFormat.format(center[i]) + "</td>";
        }
        html += "</tr></table>";

        return html;
    }
    public void printText(Writer b) throws IOException{

        b.write("Center\n");
        for(int i=0;i<center.length;i++){
            b.write(separator + names[i]);
        }
        b.write("\n");
        for(int i=0;i<center.length;i++){
            b.write(separator + decimalFormat.format(center[i]));
        }
        b.write("\n");

    }
}
