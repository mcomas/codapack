package coda.service;

import java.nio.file.Paths;

import coda.gui.CoDaPackConf;
import coda.util.RScriptEngine;

public final class RAnalysisScriptService {

    private RAnalysisScriptService() {
    }

    // Keep the script orchestration in one place so dialog classes can focus on
    // collecting inputs and rendering outputs.
    public static String prepareAnalysis(RScriptEngine engine, String scriptFile, int plotWidth, int plotHeight) {
        String sourceTemplate = "error = tryCatch(source('%s'), error = function(e) e$message)";

        String helperScript = String.format(sourceTemplate,
                Paths.get(CoDaPackConf.rScriptPath, ".cdp_helper_functions.R").toString().replace("\\", "/"));
        engine.eval(helperScript);

        String analysisScript = String.format(sourceTemplate,
                Paths.get(CoDaPackConf.rScriptPath, scriptFile).toString().replace("\\", "/"));
        engine.eval(analysisScript);

        String[] errorMessage = engine.eval("error").asStringArray();
        if (errorMessage != null) {
            return String.format("Error when reading R script file: %s", scriptFile);
        }

        String checkError = engine.eval("cdp_check()").asString();
        if (checkError != null) {
            return checkError;
        }

        engine.eval(String.format("PLOT_WIDTH = %d/72", plotWidth));
        engine.eval(String.format("PLOT_HEIGTH = %d/72", plotHeight));
        return null;
    }

    public static String runAnalysis(RScriptEngine engine) {
        engine.eval("error = tryCatch(cdp_res <- cdp_analysis(), error = function(e) e$message)");
        String[] errorMessage = engine.eval("error").asStringArray();
        if (errorMessage != null) {
            return String.format("Error when running the analysis: %s", String.join("\n", errorMessage));
        }
        return null;
    }
}
