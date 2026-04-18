package coda.service;

import coda.io.ImportRDA;
import coda.io.Importer;
import coda.util.RScriptEngine;

public final class RDataFileService {

    private RDataFileService() {
    }

    // Keep RData importer creation out of the main window class so import
    // flow can evolve without growing CoDaPackMain further.
    public static Importer createImporter(String filePath, RScriptEngine engine) {
        if (engine == null) {
            return null;
        }
        return new ImportRDA(filePath, engine);
    }
}
