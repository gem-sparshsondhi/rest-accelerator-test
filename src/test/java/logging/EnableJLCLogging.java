package logging;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EnableJLCLogging implements LoggerUtils {
    private static final Log logger = LogFactory.getLog(EnableJLCLogging.class);

    @Override
    public void logInfo(String message) {
        logger.info(message);
    }

    @Override
    public void logInfo(String message, boolean val) {
        logger.info(message);
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