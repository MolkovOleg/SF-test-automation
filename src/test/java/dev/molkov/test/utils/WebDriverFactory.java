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
 * Фабрика для создания экземпляров WebDriver
 */
public class WebDriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);

    public static WebDriver createWebDriver() {
        String browserName = BaseConfig.getWebBrowser().toLowerCase();
        boolean headless = BaseConfig.headlessMode();

        logger.info("Creating web driver {}, headlessMode={} ", browserName, headless);

        WebDriver driver;

        switch (browserName) {
            case "firefox":
                driver = createFirefoxDriver(headless);
                break;
            case "safari":
            default:
                driver = createSafariDriver(headless);
                break;
        }

        configureDriver(driver);
        return driver;

    }

    // Настройка драйвера для браузера Safari
    private static WebDriver createSafariDriver(boolean headless) {
        WebDriverManager.safaridriver().setup();
        SafariOptions options = new SafariOptions();

        if (headless) {
            logger.warn("Safari driver doesn't support headless mode. The browser will be visible");
        }

        return new SafariDriver(options);
    }

    // Настройка драйвера для браузера Firefox
    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("--headless");
        }

        return new FirefoxDriver(options);
    }

    // Дополнительные настройки для WebDriver
    private static void configureDriver(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(BaseConfig.getWebImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(BaseConfig.getPageLoadTimeout()));
        driver.manage().window().maximize();
    }
}
