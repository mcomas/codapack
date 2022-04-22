/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import javax.script.*;
import java.io.FileReader;

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
        //nashornExample.example01();
        //nashornExample.example02();
        //nashornExample.example03();
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
    
    public String example04() throws Exception {

        engine.eval(new FileReader("./JavaScriptFile.js"));
        final String greetingMessage = (String)invoker.invokeFunction("returnFecha", "CoDaPAck JavaScript");
        System.out.println(greetingMessage);
        return greetingMessage;
        
    }
    
    public int example05(int width) throws Exception {

        engine.eval(new FileReader("./JavaScriptFile.js"));
        final var result = (int)invoker.invokeFunction("move", width);
        //System.out.println(result);
        return result;
    }
}
