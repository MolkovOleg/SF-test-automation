package dev.molkov.test.pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object для страницы статьи Wikipedia
 */
public class WikipediaArticlePage extends BaseMobilePage {

    // Кнопка меню на страницы статьи
    @AndroidFindBy(id = "org.wikipedia:id/page_toolbar_button_show_overflow_menu")
    private WebElement articleMenuButton;

    // Название статьи (берем первый TextView внутри WebView)
    @AndroidFindBy(xpath = "//android.webkit.WebView/android.view.View/android.view.View[1]/android.view.View[1]/android.widget.TextView[1]")
    private WebElement articleTitle;

    // WebView со статьей
    @AndroidFindBy(className = "android.webkit.WebView")
    private WebElement articleWebView;

    public WikipediaArticlePage(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    /**
     * Проверить наличие статьи
     */
    public boolean isArticleDisplayed() {
        try {
            logger.info("Verifying article is displayed");
            pause(2000);
            dismissPopupsIfPresent(); // Обработка всплывающих окон при первом открытии статьи
            return isDisplayed(articleWebView) || isDisplayed(articleTitle);
        } catch (Exception ex) {
            logger.warn("Article is not displayed", ex);
            return false;
        }
    }

    /**
     * Получить название статьи
     */
    public String getArticleTitle() {
        try {
            if (isDisplayed(articleTitle)) {
                return articleTitle.getText();
            }
            return "";
        } catch (Exception ex) {
            logger.warn("Title of article is not displayed", ex);
            return "";
        }
    }

    /**
     * Проверить что название статьи содержит ожидаемый текст
     */
    public boolean isArticleTitleContains(String expectedTitle) {
        String title = getArticleTitle();
        logger.info("Actual title: '{}', expected to contain: '{}'", title, expectedTitle);
        return title.toLowerCase().contains(expectedTitle.toLowerCase());
    }

    /**
     * Скролл вниз по статье
     */
    public void scrollDown(int times) {
        logger.info("Scrolling down {} times ", times);
        for (int i = 0; i < times; i++) {
            try {
                pause(500);
            } catch (Exception ex) {
                logger.warn("Scrolling down failed", ex);
            }
        }
    }

    /**
     * Вернуться назад на главную
     */
    public void goBack() {
        logger.info("Going back from article menu");
        driver.navigate().back();
        pause(1000);
    }
}
