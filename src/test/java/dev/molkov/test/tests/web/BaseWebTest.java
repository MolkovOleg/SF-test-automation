package dev.molkov.test.tests.web;

import dev.molkov.test.config.BaseConfig;
import dev.molkov.test.utils.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.time.Duration;

/**
 * Базовый класс для всех веб-тестов
 * <p>
 * Управляет инициализацией и очисткой WebDriver
 */
public abstract class BaseWebTest {

    protected static final Logger logger = LoggerFactory.getLogger(BaseWebTest.class);
    protected static WebDriver driver;
    protected static WebDriverWait wait;

    @BeforeClass(alwaysRun = true)
    public void setUpClass() {
        logger.info("Setting up web test settings for test class");

        driver = WebDriverFactory.createWebDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(BaseConfig.getWebExplicitWait()));

        logger.info("Web test settings completed - Firefox will stay open for all tests");
    }

    @AfterMethod
    public void afterTest(ITestResult result) {
        if (!result.isSuccess()) {
            logger.error("Web test failed: {}", result.getName());
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        if (driver != null) {
            logger.info("Closing browser after all tests completed...");
            driver.quit();
        }
    }

    protected void toHomePage() {
        String baseUrl = BaseConfig.getWebBaseUrl();
        driver.get(baseUrl);

        // Ожидание полной загрузки страницы (особенно важно для Safari)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected String getTitle() {
        return driver.getTitle();
    }
}
