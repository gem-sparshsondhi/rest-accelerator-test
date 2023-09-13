package logging;

public interface LoggerUtils {
    void logInfo(String message, boolean condition);

    void logInfo(String message);


    void logError(String message);


    void logDebug(String message);


    void logWarning(String message);


    void logTrace(String message);


}