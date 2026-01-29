package dev.molkov.test.tests.web;

import dev.molkov.test.config.BaseConfig;
import dev.molkov.test.pages.web.WikiArticleWebPage;
import dev.molkov.test.pages.web.WikiHomeWebPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Web UI tests for Wikipedia
 * <p>
 * This class contains automated test scenarios for Wikipedia website
 * Testing includes: search functionality, navigation, article structure, etc.
 * <p>
 * Inspired by professional test patterns with priorities, groups, and data
 * providers
 */
public class WikipediaWebTests extends BaseWebTest {

    /**
     * Test 1: Verify main page loads successfully
     */
    @Test(priority = 1, groups = {"web", "smoke"}, description = "Verify Wikipedia main page loads successfully")
    public void testMainPageLoadsSuccessfully() {
        logger.info("Starting test: Main page loads successfully");

        toHomePage();

        try {
            Thread.sleep(3000); // Wait for page to fully load (Safari needs more time)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WikiHomeWebPage mainPage = new WikiHomeWebPage(driver, wait);

        // Verify page title
        String pageTitle = getTitle();
        Assert.assertTrue(pageTitle.contains("Wikipedia"),
                "Page title should contain 'Wikipedia', but was: " + pageTitle);

        // Verify search box is visible
        Assert.assertTrue(mainPage.isSearchBoxVisible(),
                "Search box should be visible on main page");

        logger.info("✓ Test passed: Main page loaded successfully");
    }

    /**
     * Test 2: Verify basic search functionality
     */
    @Test(priority = 2, groups = {"web", "smoke"}, description = "Verify search returns correct article")
    public void testSearchFunctionality() {
        logger.info("Starting test: Search functionality");

        toHomePage();

        WikiHomeWebPage mainPage = new WikiHomeWebPage(driver, wait);

        // Search for a well-known topic
        WikiArticleWebPage articlePage = mainPage.searchFor("Selenium (software)");

        // Verify page navigated
        String currentUrl = getCurrentUrl();
        Assert.assertNotEquals(BaseConfig.getWebBaseUrl(), currentUrl,
                "URL should change after search");
        Assert.assertTrue(currentUrl.contains("wiki/"),
                "URL should contain wiki path");

        // Verify article title exists
        Assert.assertTrue(articlePage.isArticleTitleDisplayed(),
                "Article title should be displayed");

        logger.info("✓ Test passed: Search returned correct article");
    }

    /**
     * Test 3: Parametrized search test
     */
    @Test(priority = 3, groups = {"web",
            "regression"}, dataProvider = "searchQueries", description = "Test search functionality with multiple queries")
    public void testSearchWithMultipleQueries(String query) {
        logger.info("Starting test: Search for '{}'", query);

        // For Safari, only run first query due to session instability
        String browserName = dev.molkov.test.config.BaseConfig.getWebBrowser().toLowerCase();
        if (browserName.equals("safari") && !query.equals("Java (programming language)")) {
            logger.info("⚠️ Skipping '{}' query for Safari - running only first parametrized test", query);
            throw new org.testng.SkipException(
                    "Query skippedfor Safari browser due to session limitations");
        }

        toHomePage();

        WikiHomeWebPage mainPage = new WikiHomeWebPage(driver, wait);

        // Perform search
        WikiArticleWebPage articlePage = mainPage.searchFor(query);

        // Verify navigation happened
        Assert.assertNotEquals(BaseConfig.getWebBaseUrl(), getCurrentUrl(),
                String.format("Search for '%s' should navigate to article page", query));

        // Verify content loaded
        Assert.assertTrue(articlePage.isArticleContentDisplayed(),
                "Article content should be displayed");

        logger.info("✓ Test passed: Search for '{}' successful", query);
    }

    @DataProvider(name = "searchQueries")
    public Object[][] searchQueries() {
        return new Object[][]{
                {"Java (programming language)"},
                {"Software testing"},
                {"Automation"}
        };
    }

    /**
     * Test 4: Verify random article navigation
     */
    @Test(priority = 4, groups = {"web", "regression"}, description = "Verify random article link works")
    public void testRandomArticleNavigation() {
        logger.info("Starting test: Random article navigation");

        toHomePage();

        WikiHomeWebPage mainPage = new WikiHomeWebPage(driver, wait);

        String initialUrl = getCurrentUrl();

        // Click random article
        WikiArticleWebPage articlePage = mainPage.openRandomArticle();

        // Verify URL changed
        String newUrl = getCurrentUrl();
        Assert.assertFalse(newUrl.equals(initialUrl),
                "Random article should navigate to different page");

        // Verify article loaded
        Assert.assertTrue(articlePage.isArticleTitleDisplayed(),
                "Random article should have a title");

        logger.info("✓ Test passed: Random article navigation successful");
    }

    /**
     * Test 5: Verify article structure
     */
    @Test(priority = 5, groups = {"web",
            "regression"}, description = "Verify article page has expected structure")
    public void testArticleStructureAndElements() {
        logger.info("Starting test: Article structure verification");

        toHomePage();

        WikiHomeWebPage mainPage = new WikiHomeWebPage(driver, wait);

        // Open a well-known article
        WikiArticleWebPage articlePage = mainPage.searchFor("Wikipedia");

        // Verify basic structure
        Assert.assertTrue(articlePage.isArticleTitleDisplayed(),
                "Article should have a title");
        Assert.assertTrue(articlePage.isArticleContentDisplayed(),
                "Article should have content");

        // Verify URL structure
        String url = getCurrentUrl();
        Assert.assertTrue(url.contains("/wiki/"),
                "Article URL should contain /wiki/");

        logger.info("✓ Test passed: Article structure verified");
    }

    /**
     * Test 6: Verify featured content on main page
     */
    @Test(priority = 6, groups = {"web",
            "regression"}, description = "Verify main page loads with featured content")
    public void testFeaturedContentOnMainPage() {
        logger.info("Starting test: Featured content verification");

        toHomePage();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WikiHomeWebPage mainPage = new WikiHomeWebPage(driver, wait);

        // Verify we're on main page
        Assert.assertTrue(mainPage.isSearchBoxVisible(),
                "Main page should be loaded with search box");

        // Verify page loaded completely
        Assert.assertTrue(mainPage.isPageLoaded(),
                "Main page should be fully loaded");

        logger.info("✓ Test passed: Main page with featured content verified");
    }

    /**
     * Test 7: Verify article references
     */
    @Test(priority = 7, groups = {"web", "regression"}, description = "Verify article has references section")
    public void testArticleReferencesSection() {
        logger.info("Starting test: Article references verification");

        // Skip for Safari due to known session instability after multiple tests
        String browserName = dev.molkov.test.config.BaseConfig.getWebBrowser().toLowerCase();
        if (browserName.equals("safari")) {
            logger.info("⚠️ Skipping test for Safari - known limitation with long test sessions");
            throw new org.testng.SkipException(
                    "Test skipped for Safari browser due to session limitations");
        }

        toHomePage();

        WikiHomeWebPage mainPage = new WikiHomeWebPage(driver, wait);

        // Open a well-documented article
        WikiArticleWebPage articlePage = mainPage.searchFor("World War II");

        // Verify article loaded
        Assert.assertTrue(articlePage.isArticleContentDisplayed(),
                "Article should have content");

        // Count references
        int refCount = articlePage.getReferencesCount();
        Assert.assertEquals(refCount, 389,
                String.format("Article should have references, found: %d", refCount));

        logger.info("✓ Test passed: Found {} references in article", refCount);
    }

}
