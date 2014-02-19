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
public class OutputAtipicality implements OutputElement{
    double threshold;
    int[] atipic;
    
    public OutputAtipicality(double threshold, int atipic[]){
        this.threshold = threshold;
        this.atipic = atipic;
    }
    public String printHTML(String html) {
        html += "<b>Atipicality index if a composition</b><br>";
        if(atipic.length > 0){
            html += "Indices greater than " + decimalFormat.format(threshold) + ": <table><tr>";
            for(int i=0;i<atipic.length;i++){
                html += "<td>" + atipic[i] + " </td>";
            }
            html += "</tr></table>";
        }else{
            html += "No atipicality found<br>";
        }
        return html;
    }
    public void printText(Writer b) throws IOException{


    }
}
