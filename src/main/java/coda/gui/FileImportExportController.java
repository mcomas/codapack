package coda.gui;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import coda.DataFrame;
import coda.DataFrame.DataFrameException;
import coda.Variable;
import coda.gui.menu.ExportRDataMenu;
import coda.gui.menu.ImportCSVMenu;
import coda.gui.menu.ImportRDAMenu;
import coda.gui.menu.ImportXLSMenu;
import coda.gui.utils.FileNameExtensionFilter;
import coda.io.ExportData;
import coda.io.ExportRDA;
import coda.io.Importer;
import coda.service.RDataFileService;
import coda.util.AppLogger;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

final class FileImportExportController {

    private FileImportExportController() {
    }

    static boolean handleAction(String title, CoDaPackMain mainApp, CoDaPackMenu menuBar, JSplitPane splitPane)
            throws javax.script.ScriptException {
        String ruta = CoDaPackConf.workingDir;
        JFileChooser chooseFile = new JFileChooser(ruta);

        if (title.equals(menuBar.ITEM_IMPORT_XLS)) {
            handleImportXls(mainApp, splitPane, chooseFile);
            return true;
        }
        if (title.equals(menuBar.ITEM_IMPORT_RDA)) {
            handleImportRData(mainApp, splitPane, chooseFile);
            return true;
        }
        if (title.equals(menuBar.ITEM_IMPORT_CSV)) {
            handleImportCsv(mainApp, splitPane, chooseFile);
            return true;
        }
        if (title.equals(menuBar.ITEM_EXPORT_CSV)) {
            handleExportCsv(mainApp, splitPane, chooseFile);
            return true;
        }
        if (title.equals(menuBar.ITEM_EXPORT_XLS)) {
            handleExportXls(mainApp, splitPane, chooseFile);
            return true;
        }
        if (title.equals(menuBar.ITEM_EXPORT_R)) {
            new ExportRDataMenu(mainApp, new ExportRDA(CoDaPackMain.re)).setVisible(true);
            return true;
        }

        return false;
    }

    private static void handleImportXls(CoDaPackMain mainApp, JSplitPane splitPane, JFileChooser chooseFile) {
        chooseFile.resetChoosableFileFilters();
        chooseFile.setFileFilter(new FileNameExtensionFilter("Excel files", "xls", "xlsx"));
        if (chooseFile.showOpenDialog(splitPane) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
            ImportXLSMenu importMenu = new ImportXLSMenu(mainApp, true, chooseFile);
            importMenu.setVisible(true);
            DataFrame df = importMenu.getDataFrame();
            if (df != null) {
                mainApp.addDataFrame(df);
            }
            importMenu.dispose();
            CoDaPackConf.workingDir = ruta;
        }
    }

    private static void handleImportRData(CoDaPackMain mainApp, JSplitPane splitPane, JFileChooser chooseFile) {
        chooseFile.resetChoosableFileFilters();
        chooseFile.setFileFilter(new FileNameExtensionFilter("R data file", "RData", "rda"));

        if (chooseFile.showOpenDialog(splitPane) == JFileChooser.APPROVE_OPTION) {
            if (CoDaPackMain.is_R_available()) {
                Importer importRDA = RDataFileService.createImporter(
                        chooseFile.getSelectedFile().getAbsolutePath(),
                        CoDaPackMain.re);
                try {
                    ImportRDAMenu imprdam = new ImportRDAMenu(mainApp, importRDA);
                    imprdam.setVisible(true);
                } catch (DataFrameException | javax.script.ScriptException error) {
                    AppLogger.errorAndShow(
                            FileImportExportController.class,
                            mainApp,
                            "Unable to open the selected R data file.",
                            error);
                }
            } else {
                JOptionPane.showMessageDialog(mainApp, "R is not available");
                return;
            }
        }
        CoDaPackConf.workingDir = chooseFile.getCurrentDirectory().getAbsolutePath();
    }

    private static void handleImportCsv(CoDaPackMain mainApp, JSplitPane splitPane, JFileChooser chooseFile) {
        JOptionPane.showMessageDialog(mainApp,
                "Be aware that data must have valid variable names. A valid variable name consists of letters, numbers and the dot or underline characters.\nThe variable name must start with a letter or the dot not followed by a number.\nIf your variable names don't follow this rule, you can correct it on the data table of CoDaPack.",
                "Warning", JOptionPane.WARNING_MESSAGE);
        chooseFile.resetChoosableFileFilters();
        chooseFile.setFileFilter(new FileNameExtensionFilter("Text file", "txt"));
        chooseFile.setFileFilter(new FileNameExtensionFilter("CSV file", "csv"));

        if (chooseFile.showOpenDialog(splitPane) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
            ImportCSVMenu importMenu = new ImportCSVMenu(mainApp, true, chooseFile);
            importMenu.setVisible(true);
            DataFrame df = importMenu.getDataFrame();
            if (df != null) {
                mainApp.addDataFrame(df);
            }
            importMenu.dispose();
            CoDaPackConf.workingDir = ruta;
        }
    }

    private static void handleExportCsv(CoDaPackMain mainApp, JSplitPane splitPane, JFileChooser chooseFile)
            throws javax.script.ScriptException {
        DataFrame df = mainApp.getActiveDataFrame();
        if (df == null) {
            JOptionPane.showMessageDialog(mainApp, "No data to export");
            return;
        }
        if (CoDaPackMain.re == null) {
            JOptionPane.showMessageDialog(mainApp, "R is not available");
            return;
        }

        chooseFile.resetChoosableFileFilters();
        chooseFile.setFileFilter(new FileNameExtensionFilter("Text file", "txt"));
        chooseFile.setFileFilter(new FileNameExtensionFilter("CSV file", "csv"));

        if (chooseFile.showOpenDialog(splitPane) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
            try {
                String extension = chooseFile.getFileFilter().getDescription().equals("CSV file") ? ".csv" : ".txt";
                String targetPath = chooseFile.getSelectedFile().getAbsolutePath() + extension;
                exportDataFrameToDelimitedFile(CoDaPackMain.re, df, targetPath, ".csv".equals(extension));
            } catch (RuntimeException ex) {
                AppLogger.errorAndShow(
                        FileImportExportController.class,
                        mainApp,
                        "Unable to export the current table.",
                        ex);
                return;
            }
            CoDaPackConf.workingDir = ruta;
        }
    }

    private static void handleExportXls(CoDaPackMain mainApp, JSplitPane splitPane, JFileChooser chooseFile) {
        chooseFile.resetChoosableFileFilters();
        chooseFile.setFileFilter(new FileNameExtensionFilter("Excel files", "xls"));
        if (chooseFile.showSaveDialog(splitPane) == JFileChooser.APPROVE_OPTION) {
            try {
                ExportData.exportXLS(chooseFile.getSelectedFile().getAbsolutePath(), mainApp.getActiveDataFrame());
            } catch (IOException ex) {
                AppLogger.errorAndShow(
                        FileImportExportController.class,
                        mainApp,
                        "Unable to export the current table to Excel.",
                        ex);
            }
        }
        CoDaPackConf.workingDir = chooseFile.getCurrentDirectory().getAbsolutePath();
    }

    private static void exportDataFrameToDelimitedFile(Rengine re, DataFrame df, String targetPath, boolean csvOutput) {
        re.eval(".cdp_export <- list()");
        String[] columnNames = new String[df.size()];

        for (int i = 0; i < df.size(); i++) {
            Variable variable = df.get(i);
            String tempName = ".cdp_export_col_" + i;
            columnNames[i] = variable.getName();

            if (variable.isNumeric()) {
                re.assign(tempName, variable.getNumericalData());
            } else {
                re.assign(tempName, variable.getTextData());
            }
            re.eval(String.format(".cdp_export[[%d]] <- %s", i + 1, tempName));
        }

        re.assign(".cdp_export_names", columnNames);
        re.eval("names(.cdp_export) <- .cdp_export_names");
        re.eval(".cdp_export <- as.data.frame(.cdp_export, stringsAsFactors = FALSE)");

        String quotedPath = REXP.quoteString(targetPath.replace("\\", "/"));
        if (csvOutput) {
            re.eval("utils::write.csv(.cdp_export, " + quotedPath + ", row.names = FALSE)");
        } else {
            re.eval("utils::write.table(.cdp_export, " + quotedPath + ", row.names = FALSE)");
        }
        re.eval("rm(list = c('.cdp_export', '.cdp_export_names', ls(pattern='^\\\\.cdp_export_col_', all.names=TRUE)), envir = .GlobalEnv)");
    }
}
