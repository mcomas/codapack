/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package coda.gui.output;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author mcomas
 */
public class OutputPlotHeader implements OutputElement{
    String[] data;
    String title;
    public OutputPlotHeader(String title, String[] data){
        this.title = title;
        this.data = data;
    }
    public String printHTML(String html) {
        html += "<b>" + title +":</b><br>";
        html += "<em>Data: </em>";
        for(int i=0;i<data.length;i++){
            html +=  data[i] + " ";
        }
        html += "<br>";
        return html;
    }

    public void printText(Writer b) throws IOException {
        
    }

}
