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
public class OutputText implements OutputElement{
    String label;
    public OutputText(String l){
        this.label = l;
    }
    public String printHTML(String html) {
        
            html += "<b>" + label + "</b><br>";
        

        return html;
    }

    public void printText(Writer b) throws IOException{
        b.write(label + "\n");

    }

}
