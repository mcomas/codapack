package coda.util;

import java.util.logging.Level;
import java.util.logging.Logger;

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
}
