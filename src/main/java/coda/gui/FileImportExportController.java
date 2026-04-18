package coda.gui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import coda.DataFrame;
import coda.DataFrame.DataFrameException;
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
                    AppLogger.error(FileImportExportController.class, "Unable to open R data import dialog", error);
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

        for (int i = 0; i < df.size(); i++) {
            CoDaPackMain.re.eval(df.get(i).getName() + " <- NULL");

            if (df.get(i).isNumeric()) {
                for (double value : df.get(i).getNumericalData()) {
                    CoDaPackMain.re.eval(df.get(i).getName() + " <- c(" + df.get(i).getName() + "," + value + ")");
                }
            } else {
                for (String value : df.get(i).getTextData()) {
                    CoDaPackMain.re.eval(df.get(i).getName() + " <- c(" + df.get(i).getName() + ",'" + value + "')");
                }
            }
        }

        StringBuilder dataFrameString = new StringBuilder("mydf <- data.frame(");
        for (int i = 0; i < df.size(); i++) {
            dataFrameString.append(df.get(i).getName());
            if (i != df.size() - 1) {
                dataFrameString.append(",");
            }
        }
        dataFrameString.append(")");
        CoDaPackMain.re.eval(dataFrameString.toString());

        if (chooseFile.showOpenDialog(splitPane) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooseFile.getCurrentDirectory().getAbsolutePath();
            if (chooseFile.getFileFilter().getDescription().equals("CSV file")) {
                CoDaPackMain.re.eval("utils::write.csv(mydf, \"" + ruta.replaceAll("\\\\", "/") + "/"
                        + chooseFile.getSelectedFile().getName() + ".csv\", row.names = FALSE)");
            } else {
                CoDaPackMain.re.eval("utils::write.table(mydf, \"" + ruta.replaceAll("\\\\", "/") + "/"
                        + chooseFile.getSelectedFile().getName() + ".txt\", row.names = FALSE)");
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
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        CoDaPackConf.workingDir = chooseFile.getCurrentDirectory().getAbsolutePath();
    }
}
