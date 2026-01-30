package dev.molkov.test.tests.mobile;

import dev.molkov.test.pages.mobile.WikipediaArticlePage;
import dev.molkov.test.pages.mobile.WikipediaSearchPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Мобильные тесты для приложения Wikipedia Android
 * Использует исправленные локаторы на основе XML дампов
 */
public class WikipediaMobileTests extends BaseMobileTest {

    /**
     * Тест 1: Проверка отображения контейнера поиска на главном экране
     */
    @Test(priority = 1, groups = { "mobile",
            "smoke" }, description = "Verify that the search container is visible on the main screen")
    public void testMainScreenDisplayed() {
        logger.info("Starting test: Main screen displayed");

        WikipediaSearchPage searchPage = new WikipediaSearchPage(driver, wait);
        Assert.assertTrue(searchPage.isSearchContainerDisplayed(),
                "Search container should be displayed");

        logger.info("Test passed: Main screen displayed");
    }

    /**
     * Тест 2: Проверка поиска по запросу "Selenium"
     */
    @Test(priority = 2, groups = { "mobile", "smoke" }, description = "Test search for 'Selenium' returns results")
    public void testSearchForSelenium() {
        logger.info("Starting test: Search for Selenium");

        WikipediaSearchPage searchPage = new WikipediaSearchPage(driver, wait);
        searchPage.search("Selenium");

        List<String> searchResults = searchPage.getResultTitles();
        logger.info("Found titles: {}", searchResults);

        Assert.assertFalse(searchResults.isEmpty(), "Search results should not be empty");
        Assert.assertTrue(searchResults.stream().anyMatch(t -> t.toLowerCase().contains("selenium")),
                "Results should contain text 'Selenium'");

        logger.info("Test passed: Search for Selenium");
    }

    /**
     * Тест 3: Проверка поиска по запросу "Java" и перехода в статью
     */
    @Test(priority = 3, groups = { "mobile", "smoke" }, description = "Test search for 'Java' and open article")
    public void testOpenJavaArticle() {
        logger.info("Starting test: Open Java article");

        WikipediaSearchPage searchPage = new WikipediaSearchPage(driver, wait);
        searchPage.search("Java");

        // Кликаем по первому результату
        searchPage.clickFirstSearchResult();

        WikipediaArticlePage articlePage = new WikipediaArticlePage(driver, wait);
        Assert.assertTrue(articlePage.isArticleDisplayed(), "Article should be displayed");

        String title = articlePage.getArticleTitle();
        logger.info("Article title: '{}'", title);

        Assert.assertTrue(articlePage.isArticleTitleContains("Java"),
                "Article title should contain 'Java'");

        logger.info("Test passed: Open Java article");
    }

    /**
     * Тест 4: Проверка очистки поиска
     */
    @Test(priority = 4, groups = { "mobile", "regression" }, description = "Test clear search input")
    public void testClearSearch() {
        logger.info("Starting test: Clear search");

        WikipediaSearchPage searchPage = new WikipediaSearchPage(driver, wait);
        searchPage.search("Appium");

        Assert.assertTrue(searchPage.areSearchResultsDisplayed(), "Search results should be displayed");

        searchPage.clearSearchInput();

        // После очистки инпут должен быть пуст
        String inputText = searchPage.getSearchInputText();
        Assert.assertTrue(
                inputText.isEmpty() || inputText.equals("Search Wikipedia") || inputText.equals("Поиск по Википедии"),
                "Search input should be cleared. Actual: " + inputText);

        logger.info("Test passed: Clear search");
    }
}
