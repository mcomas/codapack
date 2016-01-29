/**	
 *	Copyright 2011-2016 Marc Comas - Santiago Thió
 *
 *	This file is part of CoDaPack.
 *
 *  CoDaPack is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CoDaPack is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CoDaPack.  If not, see <http://www.gnu.org/licenses/>.
 */

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
