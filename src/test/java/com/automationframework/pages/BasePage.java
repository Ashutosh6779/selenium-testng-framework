package com.automationframework.pages;

import com.automationframework.utils.DriverManager;
import com.automationframework.utils.WaitUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * BasePage - Parent class for all Page Object classes.
 *
 * This is the core of the Page Object Model (POM) pattern.
 * Every page class extends BasePage to inherit:
 *   - WebDriver access (thread-safe via DriverManager)
 *   - Common actions (click, type, getText, scroll)
 *   - Explicit wait wrappers
 *   - PageFactory initialisation
 *
 * Why POM?
 *   - Single place to update locators when UI changes (maintainability)
 *   - Test code reads like English: loginPage.enterUsername("admin")
 *   - Eliminates duplicated Selenium code across test classes
 */
public abstract class BasePage {

    protected WebDriver driver;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        PageFactory.initElements(driver, this);
    }

    /** Click with explicit wait — never click without waiting */
    protected void click(WebElement element) {
        WaitUtils.waitForClickability(driver, element).click();
    }

    /** Clear field first, then type — avoids appending to existing text */
    protected void type(WebElement element, String text) {
        WaitUtils.waitForVisibility(driver, element).clear();
        element.sendKeys(text);
    }

    /** Returns trimmed visible text of an element */
    protected String getText(WebElement element) {
        return WaitUtils.waitForVisibility(driver, element).getText().trim();
    }

    /** Returns trimmed value attribute — useful for input fields */
    protected String getValue(WebElement element) {
        return WaitUtils.waitForVisibility(driver, element).getAttribute("value").trim();
    }

    /** Checks if element is displayed (no exception-based boolean) */
    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Scrolls element into view — useful for sticky headers or footers */
    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /** JS click — fallback when normal click is intercepted by overlays */
    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /** Navigate to a URL */
    protected void navigateTo(String url) {
        driver.get(url);
    }

    /** Returns current page URL */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /** Returns current page title */
    public String getPageTitle() {
        return driver.getTitle();
    }
}
