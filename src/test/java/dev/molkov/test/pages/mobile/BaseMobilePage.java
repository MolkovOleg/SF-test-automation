package dev.molkov.test.pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Базовый класс для всех Page Object мобильного приложения
 * <p>
 * Содержит общие методы для взаимодействия с элементами
 */
public abstract class BaseMobilePage {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected AndroidDriver driver;
    protected WebDriverWait wait;

    /**
     * Конструктор с драйвером и wait
     *
     * @param driver AndroidDriver
     * @param wait   WebDriverWait
     */
    public BaseMobilePage(AndroidDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    /**
     * Конструктор только с драйвером (wait создается автоматически)
     *
     * @param driver AndroidDriver
     */
    public BaseMobilePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    /**
     * Ожидание кликабельности
     *
     * @param element WebElement
     */
    protected WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Ожидание отображения
     *
     * @param element WebElement
     */
    protected WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Клик с ожиданием
     *
     * @param element WebElement
     */
    protected void safeClick(WebElement element) {
        try {
            waitForClickable(element).click();
        } catch (Exception ex) {
            logger.error("Click failed, attempted one more click", ex);
            element.click();
        }
    }

    /**
     * Сохранение и отправка текста в поле заполнения
     *
     * @param element WebElement
     * @param text    String
     */
    protected void safeSendKeys(WebElement element, String text) {
        WebElement visibleElement = waitForVisibility(element);
        visibleElement.clear();
        visibleElement.sendKeys(text);
    }

    /**
     * Проверка видимости элемента
     *
     * @param element WebElement
     * @return true если элемент видим
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Получения текста из элемента
     *
     * @param element WebElement
     * @return String текст элемента
     */
    protected String getTextFromElement(WebElement element) {
        try {
            return waitForVisibility(element).getText();
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Небольшая пауза (для стабильности)
     *
     * @param milliseconds миллисекунды
     */
    protected void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Wait interrupted", e);
        }
    }

    /**
     * Отключение всплывающих дополнительных окон
     */
    protected void dismissPopupsIfPresent() {
        try {
            var closeButton = driver.findElement(
                    By.xpath("//*[@content-desc='Close']")
            );
            if (closeButton.isDisplayed()) {
                logger.info("Popup close button is found");
                closeButton.click();
                pause(500);
            }
        } catch (Exception ex) {
            logger.info("All popups closed");
        }
    }
}
