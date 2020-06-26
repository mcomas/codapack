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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale.Builder;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.renjin.eval.Context;
import org.renjin.serialization.RDataReader;
import org.renjin.serialization.RDataWriter;
import org.renjin.sexp.AttributeMap;
import org.renjin.sexp.DoubleArrayVector;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.ListVector.NamedBuilder;
import org.renjin.sexp.NamedValue;
import org.renjin.sexp.PairList;
import org.renjin.sexp.SEXP;
import org.renjin.sexp.StringArrayVector;

/**
 *
 * @author marc
 */
public class renjinExample {
    public static void main(String[] args) throws FileNotFoundException, IOException{
        double[] xx = {1,2,3,4,5,6};


        
      
     FileInputStream in = new FileInputStream("my.RData");
        GZIPInputStream zin = new GZIPInputStream(in);
        RDataReader reader = new RDataReader(zin);
        
        Context context = Context.newTopLevelContext();
        
        SEXP exp = reader.readFile();
        SEXP a = exp.getElementAsSEXP(0);
        DoubleArrayVector v = a.getElementAsSEXP(0);
        System.out.println( Arrays.toString( v.toDoubleArray()) );
        
    }
}
