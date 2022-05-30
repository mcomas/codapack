/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import java.io.FileNotFoundException;
import javax.script.*;
import java.io.FileReader;
import javax.swing.JProgressBar;
/*
import com.fasterxml.jackson.databind.ObjectMapper; 
import com.fasterxml.jackson.databind.ObjectWriter;*/

/**
 *
 * @author dcano
 */
public class NashornExample {
    private static final String ENGINE_NAME = "graal.js";

    private final ScriptEngine engine;

    private Invocable invoker;
    
    public static void main(final String[] args) throws Exception {
        final NashornExample nashornExample = new NashornExample();
        nashornExample.example01();
        nashornExample.example02();
        nashornExample.example03();
    }

    public NashornExample() throws ScriptException {
        final ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName(ENGINE_NAME);
        invoker = (Invocable)engine;
        //engine.eval("print('Hello World!');");
    }

    public void example01() throws Exception {
        //this.engine.eval("Hello World!");
        engine.eval("print('Hello World!');");
    }

    public void example02() throws Exception {

        String directoryName = System.getProperty("user.dir");
        System.out.println("Current Working Directory is = " +directoryName);
        engine.eval(new FileReader("./JavaScriptFile.js"));
    }

    public void example03() throws Exception {

        engine.eval(new FileReader("./JavaScriptFile.js"));
        final String greetingMessage = (String)invoker.invokeFunction("fun1", "CoDaPAck JavaScript");
        System.out.println(greetingMessage);
    }
    
    public String example04(int width) throws Exception {

        engine.eval(new FileReader("./JavaScriptFile.js"));
        final String greetingMessage = (String)invoker.invokeFunction("returnFecha", width);
        System.out.println(greetingMessage);
        return greetingMessage;
        
    }
    
    public void example05(JProgressBar barra, int width) throws Exception {
        
        engine.eval(new FileReader(System.getProperty("java.io.tmpdir") + "JavaScriptFile.js"));
        
        //engine.eval(Files.newBufferedReader(Paths.get("C:/Scripts/Jsfunctions.js"), StandardCharsets.UTF_8));
        //org.graalvm.polyglot.Value v = Value.asValue(engine.eval(new FileReader(System.getProperty("java.io.tmpdir") + "JavaScriptFile.js")));
        //ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        //String json = ow.writeValueAsString(barra);
        //var barraJSON = new JSONObject(barra);
        //final var result = (JProgressBar)invoker.invokeFunction("move2", width);
        invoker.invokeFunction("move2", width);
        //System.out.println(result);
        //return result;
        
        //---------
        /*
        engine.eval("function move2(_width) {\n" +
            "    console.log(\"document\");\n" +
            "    console.log(document.URL);\n" +
            "    var elem = document.getElementById(\"myBar\");\n" +
            "    var width = 10;\n" +
            "    var id = setInterval(frame, 10);\n" +
            "    function frame() {\n" +
            "        if (width >= 100) {\n" +
            "            clearInterval(id);\n" +
            "        } else {\n" +
            "            width++;\n" +
            "            elem.style.width = width + '%';\n" +
            "            document.getElementById(\"label\").innerHTML = width * 1 + '%';\n" +
            "        }\n" +
            "    }\n" +
            "}");
        
        Invocable inv = (Invocable) engine;

        inv.invokeFunction("move2", "Scripting!!");  //This one works.   */ 
    }
    
    public void example06() throws ScriptException, NoSuchMethodException, FileNotFoundException {

        engine.eval(new FileReader(System.getProperty("java.io.tmpdir") + "JavaScriptFile.js"));
        System.out.println(System.getProperty("java.io.tmpdir") + "JavaScriptFile.js");
        
        invoker.invokeFunction("createGraf");
        //System.out.println(result);
        //return result;
    }
}
