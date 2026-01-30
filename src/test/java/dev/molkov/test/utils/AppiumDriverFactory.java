package dev.molkov.test.utils;

import dev.molkov.test.config.BaseConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Factory pattern для создания AndroidDriver с необходимыми capabilities
 */
public class AppiumDriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(AppiumDriverFactory.class);

    /**
     * Создание AndroidDriver для тестирования мобильного приложения
     *
     * @return инициализированный AndroidDriver
     */
    public static AndroidDriver createAndroidDriver() {
        logger.info("Creating AndroidDriver for platform: {}", BaseConfig.getMobilePlatformName());

        UiAutomator2Options options = new UiAutomator2Options();

        // Основные capabilities
        options.setPlatformName(BaseConfig.getMobilePlatformName());
        options.setDeviceName(BaseConfig.getMobileDeviceName());
        options.setAutomationName(BaseConfig.getMobileAutomationName());

        // App configuration
        options.setAppPackage(BaseConfig.getMobileAppPackage());
        options.setAppActivity(BaseConfig.getMobileAppActivity());

        // Дополнительные настройки для стабильности
        options.setNoReset(false); // Сбрасывать состояние для чистых тестов
        options.setFullReset(false); // Не переустанавливать приложение целиком
        options.setAutoGrantPermissions(true); // Автоматически давать разрешения
        options.setNewCommandTimeout(Duration.ofSeconds(BaseConfig.getMobileCommandTimeout()));

        // Настройки для работы с активностями
        options.setCapability("appWaitActivity", "*"); // Ждать любую активность
        options.setCapability("appWaitDuration", 30000); // Таймаут ожидания активности

        // Опции для стабильности ввода текста
        options.setCapability("unicodeKeyboard", true);
        options.setCapability("resetKeyboard", true);

        logger.info("UiAutomator2Options configured: package={}, activity={}",
                BaseConfig.getMobileAppPackage(),
                BaseConfig.getMobileAppActivity());

        try {
            URL appiumServerUrl = new java.net.URI(BaseConfig.getAppiumServerUrl()).toURL();
            AndroidDriver driver = new AndroidDriver(appiumServerUrl, options);

            // Настройка таймаутов
            driver.manage().timeouts().implicitlyWait(
                    Duration.ofSeconds(BaseConfig.getMobileImplicitWait()));

            logger.info("AndroidDriver successfully created and connected to {}", appiumServerUrl);
            return driver;

        } catch (MalformedURLException e) {
            logger.error("Invalid Appium server URL: {}", BaseConfig.getAppiumServerUrl(), e);
            throw new RuntimeException("Failed to create AndroidDriver due to invalid URL", e);
        } catch (Exception e) {
            logger.error("Failed to initialize AndroidDriver", e);
            throw new RuntimeException("Failed to create AndroidDriver", e);
        }
    }
}
