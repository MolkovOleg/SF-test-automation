package dev.molkov.test.pages.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Page Object для страницы статьи Wikipedia
 */
public class WikiArticleWebPage extends BaseWebPage {

    // Основные элементы статьи с использованием аннотаций @FindBy
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

    public WikiArticleWebPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public WikiArticleWebPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Получить текст содержимого статьи
     *
     * @return основное содержимое статьи
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
     * Получить текущий URL
     *
     * @return URL страницы
     */
    public String getUrl() {
        return getCurrentUrl();
    }

    /**
     * Проверить, содержит ли URL указанный текст
     *
     * @param urlPart часть URL для проверки
     * @return true если URL содержит текст
     */
    public boolean urlContains(String urlPart) {
        return getCurrentUrl().contains(urlPart);
    }

    /**
     * Проверить отображение заголовка статьи
     *
     * @return true если элемент заголовка существует и отображается
     */
    public boolean isArticleTitleDisplayed() {
        try {
            return articleTitle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверить отображение содержимого статьи
     *
     * @return true если элемент содержимого существует и отображается
     */
    public boolean isArticleContentDisplayed() {
        try {
            return articleContent.isDisplayed() && !getArticleContent().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Получить количество ссылок в статье
     *
     * @return количество найденных ссылок
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
