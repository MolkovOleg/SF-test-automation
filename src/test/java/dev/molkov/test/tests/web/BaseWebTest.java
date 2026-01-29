package dev.molkov.test.tests.web;

import dev.molkov.test.config.BaseConfig;
import dev.molkov.test.utils.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * Base test class for all web dev.molkov.test.tests
 * Handles WebDriver initialization and cleanup functionality
 */
public abstract class BaseWebTest {

    protected static final Logger logger = LoggerFactory.getLogger(BaseWebTest.class);
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        logger.info("Setting up web test settings");

        driver = WebDriverFactory.createWebDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(BaseConfig.getWebExplicitWait()));
        String baseUrl = BaseConfig.getWebBaseUrl();

        logger.info("Setting up web test URL: {}", baseUrl);

        driver.get(baseUrl);

        logger.info("Web test settings completed");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (driver != null) {
            if (!result.isSuccess()) {
                logger.error("Web test failed: {}", result.getName());
            }
        }
        logger.info("Closing browser...");
        driver.quit();
    }

    protected void toHomePage() {
        String baseUrl = BaseConfig.getWebBaseUrl();
        driver.get(baseUrl);
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected String getTitle() {
        return driver.getTitle();
    }
}
