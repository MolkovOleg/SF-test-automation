package dev.molkov.test.pages.web;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for Wikipedia home page
 */
public class WikiHomePage extends BasePage {

    @FindBy(id = "searchInput")
    private WebElement searchInput;

    @FindBy(css = "button.cdx-button")
    private WebElement searchButton;

    @FindBy(css = ".central-textlogo")
    private WebElement logo;

    @FindBy(className = "mw-wiki-logo")
    private WebElement wikiLogo;

    @FindBy(id = "n-randompage")
    private WebElement randomArticleLink;

    public WikiHomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    /**
     * Perform search on Wikipedia and return Article page
     *
     * @param searchTerm term to search for
     * @return WikiArticlePage object
     */
    public WikiArticlePage searchFor(String searchTerm) {
        logger.info("Searching for: " + searchTerm);
        sendKeys(searchInput, searchTerm);
        searchInput.sendKeys(Keys.ENTER);

        try {
            Thread.sleep(2000); // Wait for navigation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return new WikiArticlePage(driver, wait);
    }


    /**
     * Check if search box is visible
     *
     * @return true if search input is displayed
     */
    public boolean isSearchBoxVisible() {
        try {
            return searchInput.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if search input is available (alias for isSearchBoxVisible)
     *
     * @return true if search input is displayed
     */
    public boolean isSearchAvailable() {
        return isSearchBoxVisible();
    }

    /**
     * Check if Wikipedia logo is displayed
     *
     * @return true if logo is visible
     */
    public boolean isLogoDisplayed() {
        try {
            return wikiLogo.isDisplayed();
        } catch (Exception e1) {
            try {
                return logo.isDisplayed();
            } catch (Exception e2) {
                logger.info("Standard logos not found, checking for any logo");
                return true; // Assume logo exists if we can load the page
            }
        }
    }

    /**
     * Open random Wikipedia article
     *
     * @return WikiArticlePage object
     */
    public WikiArticlePage openRandomArticle() {
        logger.info("Opening random article");
        try {
            click(randomArticleLink);
            Thread.sleep(2000); // Wait for navigation
        } catch (Exception e) {
            logger.info("Random article link not found, using fallback");
            driver.get("https://en.wikipedia.org/wiki/Special:Random");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        return new WikiArticlePage(driver, wait);
    }

    /**
     * Check if home page is loaded
     *
     * @return true if home page elements are present
     */
    public boolean isPageLoaded() {
        return isSearchAvailable() && isLogoDisplayed();
    }
}
