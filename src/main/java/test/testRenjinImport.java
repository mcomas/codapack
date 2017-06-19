package test;



import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.renjin.sexp.DoubleVector;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.LogicalVector;
import org.renjin.sexp.SEXP;
import org.renjin.sexp.StringVector;


public class testRenjinImport {
    public static void main(String args[]) throws ScriptException{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("Renjin");
        if(engine == null) {
            throw new RuntimeException("Renjin Script Engine not found on the classpath.");
        }
        String fname = "Myfile.RData";
        engine.eval("load('" + fname + "')");
        engine.eval("x = sapply(sapply(ls(), get), is.data.frame)");
        StringVector df_names = (StringVector)engine.eval("names(x)[(x==TRUE)]");
        for (String name : df_names.toArray()) {
            System.out.println(name);
            ListVector df = (ListVector)engine.eval(name);
            System.out.println("Llargada " + df.length());
            
            for(int j=0; j < df.length();j++){
                SEXP var = df.getElementAsSEXP(j);
                if(var.isNumeric()){
                    System.out.println(df.getName(j) + " is numeric");
               
                    DoubleVector dv = (DoubleVector)engine.eval("as.double(" + name + "[['" + df.getName(j) + "']])");
                    
                    for(double v :dv.toDoubleArray()){
                        System.out.println(v);
                    }
                }else{
                    System.out.println(df.getName(j) + " is character");
                    StringVector sv = (StringVector)engine.eval("as.character(" + name + "[['" + df.getName(j) + "']])");
                    for(String v : sv.toArray()){
                        System.out.println(v);
                    }
                }
            }
        }
    }
}
