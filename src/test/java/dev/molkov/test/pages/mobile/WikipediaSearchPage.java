package dev.molkov.test.pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object для главного экрана поиска Wikipedia
 * Фокусируется на стабильной работе с поиском без открытия статей
 */
public class WikipediaSearchPage extends BaseMobilePage {

    // Контейнер поиска: ищем по ID или по тексту "Search Wikipedia"
    @AndroidFindBy(id = "search_container")
    private WebElement searchContainer;

    // Поле ввода: ищем по ID или по тексту "Search…" или классу
    // AutoCompleteTextView
    @AndroidFindBy(id = "search_src_text")
    private WebElement searchInput;

    // Результаты: ищем любой TextView который может быть заголовком (самый широкий
    // поиск)
    @AndroidFindBy(id = "page_list_item_title")
    private List<WebElement> searchResultTitles;

    // Кнопка вкладки поиска
    @AndroidFindBy(id = "nav_tab_search")
    private WebElement searchTabButton;

    public WikipediaSearchPage(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    /**
     * Проверить отображение контейнера поиска
     *
     * @return true если контейнер поиска отображается
     */
    public boolean isSearchContainerDisplayed() {
        return isDisplayed(searchContainer);
    }

    /**
     * Открыть поиск нажатием на контейнер
     */
    public void openSearchTab() {
        logger.info("Opening search tab");
        dismissPopupsIfPresent(); // очищаем возможные онбординги
        safeClick(searchContainer);
        pause(1000);
        dismissPopupsIfPresent(); // очищаем возможные онбординги
    }

    /**
     * Ввести текст поиска
     */
    public void enterSearchInput(String text) {
        logger.info("Entering search input: {}", text);
        safeSendKeys(searchInput, text);
        pause(1000);
    }

    /**
     * Начать поиск
     */
    public void search(String text) {
        openSearchTab();
        enterSearchInput(text);
    }

    /**
     * Получить список названий поиска
     */
    public List<String> getResultTitles() {
        logger.info("Getting results titles");
        try {
            pause(1500);
            return searchResultTitles.stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            logger.warn("Error while getting results titles", ex);
            return List.of();
        }
    }

    /**
     * Получить кол-во найденных результатов
     */
    public int getResultCount() {
        return searchResultTitles.size();
    }

    /**
     * Проверить отображен ли список поиска
     */
    public boolean areSearchResultsDisplayed() {
        return !searchResultTitles.isEmpty();
    }

    /**
     * Нажать на первый найденный результат
     */
    public void clickFirstSearchResult() {
        logger.info("Clicking first search result");
        if (!searchResultTitles.isEmpty()) {
            safeClick(searchResultTitles.getFirst());
            pause(1500);
        } else {
            logger.warn("No search results found");
        }
    }

    /**
     * Нажать поиск по названию
     */
    public void clickSearchResultByTitle(String title) {
        logger.info("Clicking search result by title: {}", title);
        for (WebElement result : searchResultTitles) {
            String resultText = result.getText();
            if (resultText.toLowerCase().contains(title.toLowerCase())) {
                logger.info("Found and clicking: {}", resultText);
                safeClick(result);
                pause(1500);
                return;
            }
        }
        logger.warn("Search result not found by title={}", title);
    }

    /**
     * Проверить что кнопка поиска отображена
     */
    public boolean isSearchTabButtonDisplayed() {
        return isDisplayed(searchTabButton);
    }

    /**
     * Нажать на кнопку поиска
     */
    public void clickSearchTabButton() {
        logger.info("Clicking search tab button");
        safeClick(searchTabButton);
        pause(500);
    }

    /**
     * Получить текст из поисковика
     */
    public String getSearchInputText() {
        return getTextFromElement(searchInput);
    }

    /**
     * Очистить поисковой контейнер
     */
    public void clearSearchInput() {
        logger.info("Clearing search input");
        try {
            searchInput.clear();
        } catch (Exception ex) {
            logger.warn("Error while clearing search input", ex);
        }
    }


}
