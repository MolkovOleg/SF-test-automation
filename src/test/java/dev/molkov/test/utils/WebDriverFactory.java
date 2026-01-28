package dev.molkov.test.utils;


import dev.molkov.test.config.BaseConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Factory for creating WebDriver instances
 */
public class WebDriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);

    public static WebDriver createWebDriver() {
        String browserName = BaseConfig.getWebBrowser().toLowerCase();
        boolean headless = BaseConfig.headlessMode();

        logger.info("Creating web driver {}, headlessMode={} ", browserName, headless);

        WebDriver driver;

        switch (browserName) {
            case "safari":
                driver = createSafariDriver(headless);
                break;
            case "firefox":
                driver = createFirefoxDriver(headless);
                break;
        }

        configureDriver(driver);
        return driver;

    }

    // Configuration driver for Safari browser
    private static WebDriver createSafariDriver(boolean headless) {
        WebDriverManager.safaridriver().setup();
        SafariOptions options = new SafariOptions();

        if (headless) {
            logger.warn("Safari driver doesn't support headless mode. The browser will be visible");
        }

        return new SafariDriver(options);
    }

    // Configuration driver for Firefox browser
    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("--headless");
        }

        return new FirefoxDriver(options);
    }


    // Other configurations for WebDriver
    private static void configureDriver(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(BaseConfig.getWebImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(BaseConfig.getPageLoadTimeout()));
        driver.manage().window().maximize();
    }
}
