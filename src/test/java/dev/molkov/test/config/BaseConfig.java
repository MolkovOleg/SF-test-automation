package dev.molkov.test.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration class for setting test configuration properties
 */
public class BaseConfig {

    private final static Logger logger = LoggerFactory.getLogger(BaseConfig.class);
    private static final String CONFIG_PATH = "src/test/resources/config.properties";
    private static Properties properties;

    static {
        loadProperties();
    }

    /**
     * Load configuration properties from src/test/resources/config.properties file
     */
    private static void loadProperties() {
        properties = new Properties();
        logger.info("Loading properties from {}", CONFIG_PATH);
        try {
            FileInputStream fis = new FileInputStream(CONFIG_PATH);
            properties.load(fis);
            logger.info("Successfully loaded properties from config file with path: {}", CONFIG_PATH);
            fis.close();
        } catch (IOException ex) {
            logger.error("Error while loading properties from {}", CONFIG_PATH, ex);
        }
    }

    /**
     * Configuration of Web Testing Wikipedia
     */
    public static String getWebBaseUrl() {
        return getProperty("web.base.url", "https://en.wikipedia.org");
    }

    public static String getWebBrowser() {
        return getProperty("web.browser", "safari");
    }

    public static boolean headlessMode() {
        return Boolean.parseBoolean(
                System.getProperty("headless", getProperty("web.headless", "false")));
    }

    public static int getWebImplicitWait() {
        return Integer.parseInt(getProperty("web.implicit.wait", "10"));
    }

    public static int getWebExplicitWait() {
        return Integer.parseInt(getProperty("web.explicit.wait", "15"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("web.page.load.timeout", "30"));
    }

    /**
     * Configuration для Mobile Testing (Appium)
     */
    public static String getAppiumServerUrl() {
        return getProperty("appium.server.url", "http://127.0.0.1:4723");
    }

    public static String getMobilePlatformName() {
        return getProperty("mobile.platform.name", "Android");
    }

    public static String getMobileDeviceName() {
        return getProperty("mobile.device.name", "Android_Emulator");
    }

    public static String getMobileAppPackage() {
        return getProperty("mobile.app.package", "org.wikipedia");
    }

    public static String getMobileAppActivity() {
        return getProperty("mobile.app.activity", "org.wikipedia.main.MainActivity");
    }

    public static String getMobileAutomationName() {
        return getProperty("mobile.automation.name", "UiAutomator2");
    }

    public static int getMobileImplicitWait() {
        return Integer.parseInt(getProperty("mobile.implicit.wait", "10"));
    }

    public static int getMobileExplicitWait() {
        return Integer.parseInt(getProperty("mobile.explicit.wait", "15"));
    }

    public static int getMobileCommandTimeout() {
        return Integer.parseInt(getProperty("mobile.new.command.timeout", "300"));
    }

    /**
     * Get property value from src/test/resources/config.properties
     *
     * @param key          property key
     * @param defaultValue default value if key not found
     * @return property value
     */
    private static String getProperty(String key, String defaultValue) {
        if (properties == null) {
            logger.warn("No properties loaded, default value {} will be used", key);
            return defaultValue;
        }
        String value = properties.getProperty(key, defaultValue);
        logger.debug("Property key={{}}, value={{}}", key, value);
        return value;
    }
}
