package dev.molkov.test.pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page Object для страницы статьи Wikipedia
 */
public class WikipediaArticlePage extends BaseMobilePage {

    // Кнопка меню на страницы статьи
    @AndroidFindBy(id = "page_toolbar_button_show_overflow_men")
    private WebElement articleMenuButton;

    // ArticleHeader со статьей
    @AndroidFindBy(id = "view_wiki_article_header")
    private WebElement articleHeader;

    // WebView со статьей
    @AndroidFindBy(id = "page_web_view")
    private WebElement articleWebView;

    public WikipediaArticlePage(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    /**
     * Проверить наличие статьи
     */
    public boolean isArticleDisplayed() {
        try {
            pause(2000);
            return isDisplayed(articleWebView) || isDisplayed(articleHeader);
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
            if (isDisplayed(articleHeader)) {
                return articleHeader.getText();
            }
            return "";
        } catch (Exception ex) {
            logger.warn("Title of article is not displayed", ex);
            return "";
        }
    }

    /**
     * Проверить что название статьи содержит с поисковым текстом
     */
    public boolean isArticleTitleContains(String expectedTitle) {
        isDisplayed(articleWebView);
        return articleWebView.getText().contains(expectedTitle);
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
