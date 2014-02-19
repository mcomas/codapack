/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author mcomas
 */
public class testFileWriter extends FileWriter{
    
    public static void main(String args[]) throws IOException{
            testFileWriter fileWriter = new testFileWriter("L:/out.txt");

            BufferedWriter out = fileWriter.openFileWriter();

            fileWriter.writePatata(out);
            fileWriter.writeCommand(out, "1231");

            fileWriter.closeFileWriter(out);
    }
    public testFileWriter(String filename) throws IOException {
        super(filename);
    }
    public void writePatata(BufferedWriter out) throws IOException{
        out.write("Patata");
        out.newLine();
    }
    public void writeCommand(BufferedWriter out, String cmd) throws IOException{
        out.write(cmd);
    }
    public BufferedWriter openFileWriter(){
        return new BufferedWriter(this);
    }
    public void closeFileWriter(BufferedWriter out) throws IOException{
        out.close();

    }
}
