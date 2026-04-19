package coda.service;

import org.rosuda.JRI.REXP;

import coda.util.RScriptEngine;

public final class RMatrixTransferService {

    private RMatrixTransferService() {
    }

    // Build matrices in R from assigned column vectors so callers do not need
    // to repeat the same temporary-variable choreography.
    public static void assignMatrix(RScriptEngine engine, double[][] data, String[] columnNames, String name) {
        String[] tempNames = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            tempNames[i] = ".cdp_x" + i;
            engine.assign(tempNames[i], data[i]);
        }
        engine.eval(String.format("%s = cbind(%s)", name, String.join(",", tempNames)));
        assignColumnNames(engine, columnNames, name);
        engine.eval(String.format("%s[is.nan(%s)] = NA_real_", name, name));
    }

    public static void assignMatrix(RScriptEngine engine, String[][] data, String[] columnNames, String name) {
        String[] tempNames = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            tempNames[i] = ".cdp_x" + i;
            engine.assign(tempNames[i], data[i]);
        }
        engine.eval(String.format("%s = cbind(%s)", name, String.join(",", tempNames)));
        assignColumnNames(engine, columnNames, name);
    }

    private static void assignColumnNames(RScriptEngine engine, String[] columnNames, String name) {
        String[] quotedNames = new String[columnNames.length];
        for (int i = 0; i < columnNames.length; i++) {
            quotedNames[i] = REXP.quoteString(columnNames[i]);
        }
        engine.eval(String.format("colnames(%s) = c(%s)", name, String.join(",", quotedNames)));
    }
}
