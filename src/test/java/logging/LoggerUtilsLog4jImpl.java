package logging;

import org.apache.log4j.Logger;

/**
 * Ver 1.0.0
 * Author: Sparsh Sondhi, Charu Srivastava, Touqeer Subhani
 **/
public class LoggerUtilsLog4jImpl implements LoggerUtils {
    static Logger logger = Logger.getLogger(LoggerUtilsLog4jImpl.class.getName());

    public void logInfo(String message) {
        logger.info(message);
    }
    public void logInfo(String message, boolean val) {
        logger.info(message);
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