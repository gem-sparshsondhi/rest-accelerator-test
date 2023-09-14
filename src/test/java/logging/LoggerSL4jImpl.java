package logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerSL4jImpl implements LoggerUtils {
    private static final Logger logger = LoggerFactory.getLogger(LoggerSL4jImpl.class);

    @Override
    public void logInfo(String message) {
        logger.info(message);
    }

    @Override
    public void logInfo(String message, boolean val) {
        logger.info(message, val);
    }

    @Override
    public void logError(String message) {
        logger.error(message);
    }
    @Override
    public void logDebug(String message) {
        logger.debug(message);
    }
    @Override
    public void logWarning(String message) {
        logger.warn(message);
    }
    @Override
    public void logTrace(String message) {
        logger.trace(message);
    }
}