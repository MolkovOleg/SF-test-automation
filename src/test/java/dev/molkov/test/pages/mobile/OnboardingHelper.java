package dev.molkov.test.pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Helper класс для обработки onboarding экранов Wikipedia
 * Автоматически пропускает приветственные экраны при запуске приложения
 */
public class OnboardingHelper extends BaseMobilePage {

    // Кнопка "Skip" на онбординге
    @AndroidFindBy(id = "fragment_onboarding_skip_button")
    private WebElement skipButton;

    // Кнопка "Next" на онбординге
    @AndroidFindBy(id = "fragment_onboarding_forward_button")
    private WebElement nextButton;

    // Кнопка "Done" на последнем экране онбординга
    @AndroidFindBy(id = "fragment_onboarding_done_button")
    private WebElement doneButton;

    // Название онбординга
    @AndroidFindBy(id = "primaryTextView")
    private WebElement onboardingTitle;

    public OnboardingHelper(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    /**
     * Проверить есть ли онбординг на экране
     */
    public boolean isOnboardingDisplayed() {
        return isDisplayed(skipButton) || isDisplayed(nextButton);
    }

    /**
     * Пропустить онбординг если есть
     */
    public void dismissOnboardingIfPresent() {
        try {
            if (isDisplayed(skipButton)) {
                logger.info("Skipping onboarding");
                safeClick(skipButton);
                pause(1000);
            }
        } catch (Exception ex) {
            logger.info("No onboarding present or already dismissed");
        }
    }

    /**
     * Пропустить онбординг с помощью пролистывания и завершения
     */
    public void doneOnboardingIfPresent() {
        logger.info("Finishing onboarding");

        try {
            for (int i = 0; i < 3; i++) {
                if (isDisplayed(nextButton)) {
                    safeClick(nextButton);
                    pause(500);
                }
            }

            if (isDisplayed(doneButton)) {
                safeClick(doneButton);
            }
        } catch (Exception ex) {
            logger.warn("Could not finish onboarding", ex);
        }
    }
}
