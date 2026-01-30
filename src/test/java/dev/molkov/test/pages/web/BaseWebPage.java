package dev.molkov.test.pages.web;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

/**
 * Базовый класс Page Object, универсальный для всех веб-страниц
 */
public class BaseWebPage {

    protected final Logger logger = Logger.getLogger(getClass().getName());
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BaseWebPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public BaseWebPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    protected void click(WebElement element) {
        waitForElementClickable(element).click();
    }

    protected void sendKeys(WebElement element, String text) {
        sendKeysWithRetry(element, text, 3);
    }

    /**
     * Отправка текста с механизмом повторных попыток для совместимости с Safari
     * 
     * @param element    Элемент для ввода текста
     * @param text       Текст для ввода
     * @param maxRetries Максимальное количество попыток
     */
    protected void sendKeysWithRetry(WebElement element, String text, int maxRetries) {
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                WebElement visibleElement = waitForElementVisible(element);
                visibleElement.clear();
                visibleElement.sendKeys(text);
                return; // Успешно - выход
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                attempts++;
                logger.warning("StaleElementReferenceException caught, retry attempt " + attempts + "/" + maxRetries);
                if (attempts >= maxRetries) {
                    throw e; // Повторный выброс исключения при достижении максимума попыток
                }
                try {
                    Thread.sleep(500); // Небольшая задержка перед повторной попыткой
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    protected void scrollByPixels(int pixels) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, " + pixels + ");");
    }

    /**
     * Ожидание видимости веб-элемента
     *
     * @param element Веб-элемент
     * @return видимый элемент
     */
    protected WebElement waitForElementVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Ожидание кликабельности веб-элемента
     *
     * @param element Веб-элемент
     * @return кликабельный элемент
     */
    protected WebElement waitForElementClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
