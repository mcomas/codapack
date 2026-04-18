package coda.service;

import org.rosuda.JRI.Rengine;

import coda.util.AppLogger;
import coda.util.RScriptEngine;

public final class RIntegrationService {

    private static boolean jriAvailable = true;

    private RIntegrationService() {
    }

    public static boolean initializeNativeLibrary() {
        var path = System.getProperty("java.library.path");

        try {
            System.loadLibrary("jri");
            jriAvailable = true;
        } catch (UnsatisfiedLinkError error) {
            AppLogger.warning(RIntegrationService.class, "JRI not detected on java.library.path: " + path);
            jriAvailable = false;
        }

        if (!jriAvailable) {
            try {
                System.load("Rlibraries/rJava/jri/libjri.jnilib");
                jriAvailable = true;
            } catch (UnsatisfiedLinkError error) {
                AppLogger.warning(RIntegrationService.class, "Error when loading bundled JRI native library");
                jriAvailable = false;
            }
        }

        return jriAvailable;
    }

    // Centralize the runtime gate so the UI and feature code can share a
    // single definition of "R is available".
    public static boolean isRAvailable() {
        if (System.getenv("R_HOME") == null || !jriAvailable) {
            return false;
        }
        return Rengine.versionCheck();
    }

    // Apply the global R session defaults once, right after JRI startup.
    public static RScriptEngine initializeEngine(String[] args) {
        if (!isRAvailable()) {
            return null;
        }

        RScriptEngine engine = new RScriptEngine(args, false, null);
        engine.eval("options(width=10000)");
        engine.eval("Sys.setlocale('LC_NUMERIC','C')");
        return engine;
    }
}
