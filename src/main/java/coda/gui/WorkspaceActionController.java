package coda.gui;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import coda.DataFrame;
import coda.ext.json.JSONException;
import coda.gui.utils.FileNameExtensionFilter;
import coda.io.CoDaPackImporter;
import coda.io.WorkspaceIO;
import coda.util.AppLogger;

final class WorkspaceActionController {

    private WorkspaceActionController() {
    }

    static boolean handleAction(String title, CoDaPackMain mainApp, CoDaPackMenu menuBar) {
        String ruta = CoDaPackConf.workingDir;
        JFileChooser chooseFile = new JFileChooser(ruta);

        if (title.equals(menuBar.ITEM_OPEN)) {
            handleOpenWorkspace(mainApp, menuBar);
            return true;
        }
        if (title.equals(menuBar.ITEM_ADD)) {
            importWorkspace(mainApp, menuBar, new CoDaPackImporter().setParameters(mainApp));
            return true;
        }
        if ("format:codapack".equals(title.split("\\?")[0])) {
            importWorkspace(mainApp, menuBar, new CoDaPackImporter().setParameters(title));
            return true;
        }
        if (title.equals(menuBar.ITEM_CLEAR_RECENT)) {
            menuBar.removeRecentFiles();
            return true;
        }
        if (title.equals(menuBar.ITEM_SAV)) {
            handleSaveWorkspace(mainApp, menuBar, chooseFile);
            return true;
        }
        if (title.equals(menuBar.ITEM_SAVE)) {
            handleSaveAsWorkspace(mainApp, menuBar, chooseFile);
            return true;
        }

        return false;
    }

    private static void handleOpenWorkspace(CoDaPackMain mainApp, CoDaPackMenu menuBar) {
        if (!mainApp.getAllDataFrames().isEmpty()) {
            if (mainApp.hasUnsavedChanges()) {
                int response = JOptionPane.showConfirmDialog(mainApp,
                        "<html>Your changes will be lost if you close <br/>Do you want to continue?</html>",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            mainApp.clearAllDataFrames();
        }
        importWorkspace(mainApp, menuBar, new CoDaPackImporter().setParameters(mainApp));
    }

    private static void importWorkspace(CoDaPackMain mainApp, CoDaPackMenu menuBar, CoDaPackImporter importer) {
        ArrayList<DataFrame> dfs = importer.importDataFrames();
        for (DataFrame df : dfs) {
            mainApp.addDataFrame(df);
        }
        menuBar.fillRecentFiles();
        menuBar.saveRecentFile(importer.getParameters());
        String fn = importer.getParameters();
        if (fn.startsWith("format:codapack?")) {
            menuBar.active_path = fn.substring(16);
        } else {
            menuBar.active_path = fn;
        }
    }

    private static void handleSaveWorkspace(CoDaPackMain mainApp, CoDaPackMenu menuBar, JFileChooser chooseFile) {
        if (menuBar.active_path != null) {
            String fileNameExt = ".cdp";
            String fileName = menuBar.active_path;
            String fn;
            if (fileName.endsWith(".xls") || fileName.endsWith(".rda") || fileName.endsWith(".cdp")
                    || fileName.endsWith(".rda") || fileName.endsWith(".txt") || fileName.endsWith(".csv")) {
                fn = fileName.substring(0, fileName.length() - 4);
            } else if (fileName.endsWith(".xlsx")) {
                fn = fileName.substring(0, fileName.length() - 5);
            } else if (fileName.endsWith(".RData")) {
                fn = fileName.substring(0, fileName.length() - 6);
            } else {
                fn = fileName + fileNameExt;
            }
            try {
                WorkspaceIO.saveWorkspace(fn + fileNameExt, mainApp);
                menuBar.saveRecentFile(fn + fileNameExt);
                mainApp.markAllDataFramesSaved();
            } catch (IOException | JSONException ex) {
                AppLogger.errorAndShow(
                        WorkspaceActionController.class,
                        mainApp,
                        "Unable to save the current workspace.",
                        ex);
            }
            return;
        }

        chooseFile.resetChoosableFileFilters();
        chooseFile.setFileFilter(new FileNameExtensionFilter("CoDaPack Workspace", "cdp"));
        if (chooseFile.showSaveDialog(mainApp) == JFileChooser.APPROVE_OPTION) {
            String filename = chooseFile.getSelectedFile().getAbsolutePath();
            try {
                WorkspaceIO.saveWorkspace(filename.endsWith(".cdp") ? filename : filename + ".cdp", mainApp);
                menuBar.saveRecentFile(filename + ".cdp");
                mainApp.markAllDataFramesSaved();
            } catch (IOException | JSONException ex) {
                AppLogger.errorAndShow(
                        WorkspaceActionController.class,
                        mainApp,
                        "Unable to save the current workspace.",
                        ex);
            }
        }
        CoDaPackConf.workingDir = chooseFile.getCurrentDirectory().getAbsolutePath();
    }

    private static void handleSaveAsWorkspace(CoDaPackMain mainApp, CoDaPackMenu menuBar, JFileChooser chooseFile) {
        chooseFile.resetChoosableFileFilters();
        chooseFile.setFileFilter(new FileNameExtensionFilter("CoDaPack Workspace", "cdp"));
        if (chooseFile.showSaveDialog(mainApp) == JFileChooser.APPROVE_OPTION) {
            String filename = chooseFile.getSelectedFile().getAbsolutePath();
            try {
                WorkspaceIO.saveWorkspace(filename.endsWith(".cdp") ? filename : filename + ".cdp", mainApp);
                menuBar.saveRecentFile(filename + ".cdp");
                mainApp.markAllDataFramesSaved();
            } catch (IOException | JSONException ex) {
                AppLogger.errorAndShow(
                        WorkspaceActionController.class,
                        mainApp,
                        "Unable to save the current workspace.",
                        ex);
            }
        }
        CoDaPackConf.workingDir = chooseFile.getCurrentDirectory().getAbsolutePath();
    }
}
