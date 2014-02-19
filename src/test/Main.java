/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author marc
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {        
        new HelpWindow("Test", new URL("http://mcomas.net"));

    }

}
