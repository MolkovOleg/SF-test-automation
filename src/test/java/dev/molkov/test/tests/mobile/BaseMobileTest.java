package dev.molkov.test.tests.mobile;

import dev.molkov.test.config.BaseConfig;
import dev.molkov.test.pages.mobile.OnboardingHelper;
import dev.molkov.test.utils.AppiumDriverFactory;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * Базовый класс для всех мобильных тестов
 * Управляет инициализацией и очисткой AndroidDriver для Wikipedia приложения
 */
public abstract class BaseMobileTest {

    protected static final Logger logger = LoggerFactory.getLogger(BaseMobileTest.class);
    protected static AndroidDriver driver;
    protected static WebDriverWait wait;

    /**
     * Настройка окружения перед запуском тестов класса
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        logger.info("Setting up mobile test environment for test class");
        logger.info("Appium Server URL: {}", BaseConfig.getAppiumServerUrl());
        logger.info("App Package: {}", BaseConfig.getMobileAppPackage());

        // Создать драйвер
        try {
            driver = AppiumDriverFactory.createAndroidDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(BaseConfig.getMobileExplicitWait()));

            logger.info("Mobile test setup completed - App is ready for testing");
            pause(3000);

            try {
                OnboardingHelper onboarding = new OnboardingHelper(driver, wait);
                onboarding.dismissOnboardingIfPresent();
                logger.info("Onboarding completed - App is ready for testing");
            } catch (Exception ex) {
                logger.info("Onboarding haven't find or already dismissed");
            }

            pause(1000);

        } catch (Exception ex) {
            logger.error("Failed to setup mobile environment", ex);
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception e) {
                    logger.error("Failed to quit appium driver", e);
                }
            }
            throw new SkipException("Failed to setup appium driver", ex);
        }
    }

    /**
     * Очистка окружения после выполнения всех тестов класса
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (driver != null) {
            logger.info("Closing appium driver");
            try {
                driver.quit();
            } catch (Exception e) {
                logger.warn("Error while closing driver: {}", e.getMessage());
            }
            logger.info("Driver closed successfully");
        }
    }

    /**
     * Пауза выполнения
     *
     * @param milliseconds время паузы в миллисекундах
     */
    protected void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
