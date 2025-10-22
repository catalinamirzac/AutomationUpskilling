package stepdefinitions.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseDefine {
    protected final Logger logger = LogManager.getLogger(getClass());


    protected void logf(String format, Object... args) {
        logger.info(String.format(format, args));
    }
}
