package stepdefinitions.ui;

import java.util.logging.Logger;

public class BaseDefine {
    protected final Logger logger = Logger.getLogger(getClass().getName());

    /** printf-style logging: use %s in the message */
    protected void logf(String format, Object... args) {
        logger.info(String.format(format, args));
    }
}
