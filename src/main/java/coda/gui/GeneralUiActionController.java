package coda.gui;

import javax.swing.JOptionPane;

import coda.DataFrame;
import coda.gui.menu.AddToPersonalMenu;
import coda.gui.menu.ConfigurationMenu;
import coda.gui.menu.CrearMenuPersonal;
import coda.gui.menu.ExportPersonalMenu;
import coda.gui.menu.T1;
import coda.gui.output.OutputElement;
import coda.gui.output.OutputForR;

final class GeneralUiActionController {

    private GeneralUiActionController() {
    }

    static boolean handleAction(String title, CoDaPackMain mainApp, CoDaPackMenu menuBar) {
        if (title.equals(menuBar.ITEM_DEL_DATAFRAME)) {
            handleDeleteDataFrame(mainApp);
            return true;
        }
        if (title.equals(menuBar.ITEM_DELETE_ALL_TABLES)) {
            handleDeleteAllTables(mainApp);
            return true;
        }
        if (title.equals(menuBar.ITEM_CLEAR_OUTPUTS)) {
            handleClearOutputs(mainApp);
            return true;
        }
        if (title.equals(menuBar.ITEM_QUIT)) {
            handleQuit(mainApp, menuBar);
            return true;
        }
        if (title.equals(menuBar.ITEM_CONF)) {
            handleConfiguration(mainApp);
            return true;
        }
        if (title.equals(menuBar.ITEM_ABOUT)) {
            new CoDaPackAbout(mainApp).setVisible(true);
            return true;
        }
        if (title.equals(menuBar.R_TEST)) {
            handleRTest(mainApp);
            return true;
        }
        if (title.equals(menuBar.ITEM_MODEL_CPM)) {
            new CrearMenuPersonal(mainApp, CoDaPackMain.re).setVisible(true);
            return true;
        }
        if (title.equals(menuBar.ITEM_MODEL_PM)) {
            new T1(mainApp, CoDaPackMain.re).setVisible(true);
            return true;
        }
        if (title.equals(menuBar.ITEM_MODEL_IPM)) {
            new AddToPersonalMenu(mainApp, CoDaPackMain.re).setVisible(true);
            return true;
        }
        if (title.equals(menuBar.ITEM_MODEL_EPM)) {
            new ExportPersonalMenu(mainApp, CoDaPackMain.re).setVisible(true);
            return true;
        }

        for (int i = 0; i < menuBar.NomsMenuItems.size(); i++) {
            if (title.equals(menuBar.NomsMenuItems.get(i))) {
                mainApp.ArchiuSeleccionat = menuBar.NomsMenuItems.get(i);
                new T1(mainApp, CoDaPackMain.re).setVisible(true);
                return true;
            }
        }

        return false;
    }

    private static void handleDeleteDataFrame(CoDaPackMain mainApp) {
        if (mainApp.getAllDataFrames().size() > 0) {
            mainApp.removeDataFrame(mainApp.getAllDataFrames().get(mainApp.getActiveDataFrameIndex()));
        } else {
            JOptionPane.showMessageDialog(mainApp, "No table available");
        }
    }

    private static void handleDeleteAllTables(CoDaPackMain mainApp) {
        int responseDeleteAllTables = JOptionPane.showConfirmDialog(mainApp, "Are you sure to delete all the tables?",
                "Delete All Tables", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (responseDeleteAllTables == JOptionPane.YES_OPTION) {
            mainApp.clearAllDataFrames();
        }
    }

    private static void handleClearOutputs(CoDaPackMain mainApp) {
        int responseCleanOutput = JOptionPane.showConfirmDialog(mainApp, "Are you sure to clean the output?",
                "Clean the output", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (responseCleanOutput == JOptionPane.YES_OPTION) {
            CoDaPackMain.outputPanel.clearOutput();
        }
    }

    private static void handleQuit(CoDaPackMain mainApp, CoDaPackMenu menuBar) {
        menuBar.copyRecentFiles();
        if (mainApp.hasUnsavedChanges()) {
            int response = JOptionPane.showConfirmDialog(mainApp,
                    "<html>Your changes will be lost if you close <br/>Do you want to exit?</html>", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                closeAfterHtmlPrompt(mainApp, false);
            }
        } else {
            int response = JOptionPane.showConfirmDialog(mainApp, "Do you want to exit?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                closeAfterHtmlPrompt(mainApp, true);
            }
        }
    }

    private static void closeAfterHtmlPrompt(CoDaPackMain mainApp, boolean deleteAllPanels) {
        int responseSaveHTML = JOptionPane.showConfirmDialog(mainApp, "Do you want to save the session?",
                "Save the session", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (responseSaveHTML == JOptionPane.NO_OPTION) {
            if (deleteAllPanels) {
                for (int a = 0; a < CoDaPackMain.outputPanels.length; a++) {
                    CoDaPackMain.outputPanels[a].deleteHtml();
                }
            } else {
                CoDaPackMain.outputPanel.deleteHtml();
            }
        }
        mainApp.dispose();
        mainApp.closeApplication();
    }

    private static void handleConfiguration(CoDaPackMain mainApp) {
        if (mainApp.getConfigurationMenu() == null) {
            mainApp.setConfigurationMenu(new ConfigurationMenu(mainApp));
        }
        mainApp.getConfigurationMenu().setVisible(true);
    }

    private static void handleRTest(CoDaPackMain mainApp) {
        CoDaPackMain.re.eval("a <- capture.output(sessionInfo())");
        OutputElement e = new OutputForR(CoDaPackMain.re.eval("a").asStringArray());
        CoDaPackMain.outputPanel.addOutput(e);

        CoDaPackMain.re.eval("a <- capture.output(Sys.getenv())");
        e = new OutputForR(CoDaPackMain.re.eval("a").asStringArray());
        CoDaPackMain.outputPanel.addOutput(e);

        CoDaPackMain.re.eval("a <- capture.output(capabilities())");
        e = new OutputForR(CoDaPackMain.re.eval("a").asStringArray());
        CoDaPackMain.outputPanel.addOutput(e);

        CoDaPackMain.re.eval("ip = as.data.frame(installed.packages()[,c(1,3:4)])");
        CoDaPackMain.re.eval("ip = ip[is.na(ip$Priority),1:2,drop=FALSE]");
        e = new OutputForR(CoDaPackMain.re.eval("capture.output(ip)").asStringArray());
        CoDaPackMain.outputPanel.addOutput(e);
    }
}
