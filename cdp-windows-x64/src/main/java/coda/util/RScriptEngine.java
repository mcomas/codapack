package coda.util;

import java.io.Reader;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

public class RScriptEngine extends Rengine implements ScriptEngine{

    public RScriptEngine(java.lang.String[] args, boolean runMainLoop, RMainLoopCallbacks initialCallbacks){
        super(args, runMainLoop, initialCallbacks);
    }

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eval'");
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eval'");
    }

    @Override
    public REXP eval(String script) {
        return(super.eval(script));
    }

    @Override
    public Object eval(Reader reader) throws ScriptException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eval'");
    }

    @Override
    public Object eval(String script, Bindings n) throws ScriptException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eval'");
    }

    @Override
    public Object eval(Reader reader, Bindings n) throws ScriptException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eval'");
    }

    @Override
    public void put(String key, Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'put'");
    }

    @Override
    public Object get(String key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public Bindings getBindings(int scope) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBindings'");
    }

    @Override
    public void setBindings(Bindings bindings, int scope) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBindings'");
    }

    @Override
    public Bindings createBindings() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createBindings'");
    }

    @Override
    public ScriptContext getContext() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getContext'");
    }

    @Override
    public void setContext(ScriptContext context) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setContext'");
    }

    @Override
    public ScriptEngineFactory getFactory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFactory'");
    }
    
}
