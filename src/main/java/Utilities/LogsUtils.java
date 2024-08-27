package Utilities;


import org.apache.logging.log4j.LogManager;

public class LogsUtils {
    public static String LOGS_PATH = "test-outputs/Logs";

    public static void trace(String message) {
        LogManager.getLogger()
                .trace(message);
    }

    public static void debug(String message) {
        LogManager.getLogger()
                .debug(message);
    }

    public static void info(String message) {
        LogManager.getLogger()
                .info(message);
    }

    public static void warn(String message) {
        LogManager.getLogger()
                .warn(message);
    }

    public static void error(String message) {
        LogManager.getLogger()
                .error(message);
    }

    public static void fatal(String message) {
        LogManager.getLogger()
                .fatal(message);
    }
}