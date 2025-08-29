package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads config.properties from the classpath (src/test/resources or src/main/resources).
 */
public class PropertiesUtil {

    private static final Properties props = new Properties();

    static {
        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("config.properties")) {

            if (is == null) {
                throw new RuntimeException("config.properties not found on classpath");
            }
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    public static boolean getBooleanProperty(String key) {
        return "true".equalsIgnoreCase(props.getProperty(key));
    }

    public static boolean isHeadless() {
        return getBooleanProperty("headless");
    }
}
