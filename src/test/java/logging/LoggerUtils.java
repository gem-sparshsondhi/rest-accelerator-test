package logging;

/**
 * Ver 1.0.0
 * Author: Sparsh Sondhi, Charu Srivastava, Touqeer Subhani
 **/


public interface LoggerUtils {
    void logInfo(String message);

    void logInfo(String message, boolean condition);

    void logError(String message);

    void logDebug(String message);

    void logWarning(String message);

    void logTrace(String message);
}