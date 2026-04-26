package com.automationframework.tests;

import com.automationframework.pages.LoginPage;
import com.automationframework.pages.ProductsPage;
import com.automationframework.utils.ExcelUtils;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * LoginTest - Test class for login functionality.
 *
 * Demonstrates:
 *   - POM usage (LoginPage, ProductsPage)
 *   - Allure annotations (@Feature, @Story, @Step, @Severity)
 *   - Data-driven testing with @DataProvider + Excel
 *   - Positive and negative test scenarios
 *   - Method chaining on page objects
 */
@Epic("Authentication")
@Feature("Login")
public class LoginTest extends BaseTest {

    // ── Positive Tests ────────────────────────────────────────────────────────

    @Test(priority = 1, description = "Valid credentials should land user on Products page")
    @Story("Successful login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verifies that a registered user can log in with correct username and password")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage();
        ProductsPage productsPage = loginPage.loginAs("standard_user", "secret_sauce");

        Assert.assertTrue(productsPage.isOnProductsPage(),
            "Expected to be on Products page after login, but URL was: " + productsPage.getCurrentUrl());
        Assert.assertEquals(productsPage.getPageTitle(), "Products",
            "Products page title mismatch");
    }

    @Test(priority = 2, description = "Products page should display inventory items after login")
    @Story("Successful login")
    @Severity(SeverityLevel.CRITICAL)
    public void testProductsDisplayedAfterLogin() {
        LoginPage loginPage = new LoginPage();
        ProductsPage productsPage = loginPage.loginAs("standard_user", "secret_sauce");

        int productCount = productsPage.getProductCount();
        Assert.assertTrue(productCount > 0,
            "Expected products to be displayed after login, but found: " + productCount);
    }

    // ── Negative Tests ────────────────────────────────────────────────────────

    @Test(priority = 3, description = "Invalid password should show error message")
    @Story("Failed login")
    @Severity(SeverityLevel.CRITICAL)
    public void testInvalidPassword() {
        LoginPage loginPage = new LoginPage()
                .open()
                .enterUsername("standard_user")
                .enterPassword("wrong_password")
                .clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
            "Expected error message to be displayed for invalid password");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"),
            "Unexpected error message: " + loginPage.getErrorMessage());
    }

    @Test(priority = 4, description = "Empty credentials should show validation error")
    @Story("Failed login")
    @Severity(SeverityLevel.NORMAL)
    public void testEmptyCredentials() {
        LoginPage loginPage = new LoginPage()
                .open()
                .clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
            "Expected validation error for empty credentials");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"),
            "Unexpected error message: " + loginPage.getErrorMessage());
    }

    @Test(priority = 5, description = "Locked out user should see specific error message")
    @Story("Failed login")
    @Severity(SeverityLevel.NORMAL)
    public void testLockedOutUser() {
        LoginPage loginPage = new LoginPage()
                .open()
                .enterUsername("locked_out_user")
                .enterPassword("secret_sauce")
                .clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"),
            "Expected locked out message, got: " + loginPage.getErrorMessage());
    }

    // ── Data-Driven Tests ─────────────────────────────────────────────────────

    @Test(priority = 6, dataProvider = "invalidLoginData",
          description = "Multiple invalid credential combinations should all show errors")
    @Story("Failed login")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidCredentialCombinations(String username, String password, String expectedError) {
        LoginPage loginPage = new LoginPage()
                .open()
                .enterUsername(username)
                .enterPassword(password)
                .clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
            "Expected error for username='" + username + "', password='" + password + "'");
    }

    /**
     * Data provider — in a real project, this reads from Excel via ExcelUtils.
     * Inline here for portability (no file path dependency in CI).
     */
    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        return new Object[][] {
            {"",              "",              "Username is required"},
            {"standard_user", "",              "Password is required"},
            {"",              "secret_sauce",  "Username is required"},
            {"wrong_user",    "wrong_pass",    "Username and password do not match"},
        };
    }

    // ── Cart Interaction Test ─────────────────────────────────────────────────

    @Test(priority = 7, description = "Adding item to cart should update cart badge count")
    @Story("Shopping cart")
    @Feature("Cart")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddItemToCartUpdatesCount() {
        LoginPage loginPage = new LoginPage();
        ProductsPage productsPage = loginPage.loginAs("standard_user", "secret_sauce");

        Assert.assertEquals(productsPage.getCartCount(), "0",
            "Cart should be empty on fresh login");

        productsPage.addFirstItemToCart();

        Assert.assertEquals(productsPage.getCartCount(), "1",
            "Cart count should be 1 after adding one item");
    }
}
