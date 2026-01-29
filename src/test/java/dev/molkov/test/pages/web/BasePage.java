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
 * Base Page Object class which is universal for all web pages
 */
public class BasePage {

    protected final Logger logger = Logger.getLogger(getClass().getName());
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public BasePage(WebDriver driver) {
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
     * Send keys with retry mechanism for Safari compatibility
     * 
     * @param element    WebElement to send keys to
     * @param text       Text to send
     * @param maxRetries Maximum number of retry attempts
     */
    protected void sendKeysWithRetry(WebElement element, String text, int maxRetries) {
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                WebElement visibleElement = waitForElementVisible(element);
                visibleElement.clear();
                visibleElement.sendKeys(text);
                return; // Success - exit
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                attempts++;
                logger.warning("StaleElementReferenceException caught, retry attempt " + attempts + "/" + maxRetries);
                if (attempts >= maxRetries) {
                    throw e; // Re-throw if max retries reached
                }
                try {
                    Thread.sleep(500); // Small delay before retry
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
     * Wait for visibility of web element
     *
     * @param element WebElement
     * @return visible element
     */
    protected WebElement waitForElementVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for clickability of web element
     *
     * @param element WebElement
     * @return clickable element
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
