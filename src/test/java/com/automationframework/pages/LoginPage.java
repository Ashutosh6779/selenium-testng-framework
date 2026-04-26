package com.automationframework.pages;

import com.automationframework.config.ConfigReader;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage - Page Object for https://www.saucedemo.com login page.
 *
 * All locators for this page live here — if the UI changes,
 * only this file needs updating, not every test that uses login.
 *
 * Follows POM principle: locators + page-specific actions only.
 * No assertions in page objects — assertions belong in test classes.
 */
public class LoginPage extends BasePage {

    // ── Locators ────────────────────────────────────────────────────────────
    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    // ── Actions ──────────────────────────────────────────────────────────────

    /** Opens the login page URL from config */
    public LoginPage open() {
        navigateTo(ConfigReader.getBaseUrl());
        return this;
    }

    public LoginPage enterUsername(String username) {
        type(usernameField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    /** Returns the next page (Products) after successful login */
    public ProductsPage clickLogin() {
        click(loginButton);
        return new ProductsPage();
    }

    /** Use when expecting login to fail — stays on LoginPage */
    public LoginPage clickLoginExpectingFailure() {
        click(loginButton);
        return this;
    }

    /** Convenience: full login flow in one call */
    public ProductsPage loginAs(String username, String password) {
        return open()
                .enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    public boolean isOnLoginPage() {
        return getCurrentUrl().contains("saucedemo.com");
    }
}
