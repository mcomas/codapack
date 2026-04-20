package coda.util;

import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public final class AppLogger {

    private AppLogger() {
    }

    public static Logger getLogger(Class<?> type) {
        return Logger.getLogger(type.getName());
    }

    public static void info(Class<?> type, String message) {
        getLogger(type).info(message);
    }

    public static void warning(Class<?> type, String message) {
        getLogger(type).warning(message);
    }

    public static void error(Class<?> type, String message, Throwable error) {
        getLogger(type).log(Level.SEVERE, message, error);
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "CoDaPack", JOptionPane.ERROR_MESSAGE);
    }

    public static void errorAndShow(Class<?> type, Component parent, String message, Throwable error) {
        error(type, message, error);
        showError(parent, message);
    }
}
