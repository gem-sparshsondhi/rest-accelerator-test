package logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ver 1.0.0
 * Author: Sparsh Sondhi, Charu Srivastava, Touqeer Subhani
 **/

public class LoggerUtilsSlf4jImpl implements LoggerUtils {
    private static final Logger logger = LoggerFactory.getLogger(LoggerUtilsSlf4jImpl.class);

    public void logInfo(String message) {
        logger.info(message);
    }
    public void logInfo(String message, boolean val) {
        logger.info(message, val);
    }
    public void logError(String message) {
        logger.error(message);
    }
    public void logDebug(String message) {
        logger.debug(message);
    }
    public void logWarning(String message) {
        logger.warn(message);
    }
    public void logTrace(String message) {
        logger.trace(message);
    }
}