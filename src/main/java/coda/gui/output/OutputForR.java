/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coda.gui.output;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Guest2
 */
public class OutputForR implements OutputElement{
    String[] label;
    
    // constructor
    public OutputForR(String[] l){
        this.label = l;
        System.out.println("HOla");
    }
    
    public String printHTML(String html){
        html += "<pre>";
        for(String i: label) html += "" + i + "\n";
        html += "</pre>";
       
        return html;
    }
    
    public void printText(Writer b) throws IOException{
        
        for(String i: label) b.write(i + "\n");
    }
}
