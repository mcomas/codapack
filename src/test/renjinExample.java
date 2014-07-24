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
import org.renjin.primitives.io.serialization.RDataReader;
import org.renjin.primitives.io.serialization.RDataWriter;
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
