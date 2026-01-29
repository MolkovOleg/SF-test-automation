package dev.molkov.test.pages.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Page Object for Wikipedia article page
 */
public class WikiArticlePage extends BasePage {

    // Main article elements using @FindBy annotations
    @FindBy(id = "firstHeading")
    private WebElement articleTitle;

    @FindBy(id = "mw-content-text")
    private WebElement articleContent;

    @FindBy(id = "toc")
    private WebElement tableOfContents;

    @FindBy(className = "infobox")
    private WebElement infobox;

    @FindBy(id = "ca-edit")
    private WebElement editButton;

    @FindBy(css = ".catlinks")
    private WebElement categoryLinks;

    @FindBy(css = ".mw-parser-output img")
    private List<WebElement> articleImages;

    @FindBy(css = ".external")
    private List<WebElement> externalLinks;

    @FindBy(className = "references")
    private WebElement references;

    public WikiArticlePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public WikiArticlePage(WebDriver driver) {
        super(driver);
    }


    /**
     * Get article content text
     *
     * @return main article content
     */
    public String getArticleContent() {
        try {
            return waitForElementVisible(articleContent).getText();
        } catch (Exception e) {
            logger.warning("Could not get article content: " + e.getMessage());
            return "";
        }
    }

    /**
     * Get current URL
     *
     * @return page URL
     */
    public String getUrl() {
        return getCurrentUrl();
    }

    /**
     * Check if URL contains specific text
     *
     * @param urlPart partial URL to check
     * @return true if URL contains the text
     */
    public boolean urlContains(String urlPart) {
        return getCurrentUrl().contains(urlPart);
    }

    /**
     * Check if article title is displayed
     *
     * @return true if title element exists and is displayed
     */
    public boolean isArticleTitleDisplayed() {
        try {
            return articleTitle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if article content is displayed
     *
     * @return true if content element exists and is displayed
     */
    public boolean isArticleContentDisplayed() {
        try {
            return articleContent.isDisplayed() && !getArticleContent().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get count of references in article
     *
     * @return number of references found
     */
    public int getReferencesCount() {
        try {
            return externalLinks.size();
        } catch (Exception e) {
            logger.warning("Could not count references: " + e.getMessage());
            return 0;
        }
    }
}
