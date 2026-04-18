package coda.util;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

public class RScriptEngine extends Rengine {

    public RScriptEngine(String[] args, boolean runMainLoop, RMainLoopCallbacks initialCallbacks) {
        super(args, runMainLoop, initialCallbacks);
    }

    // Keep a narrow wrapper around JRI instead of advertising the broader
    // javax.script contract, which the engine does not fully implement.
    @Override
    public synchronized REXP eval(String script) {
        return super.eval(script);
    }
}
