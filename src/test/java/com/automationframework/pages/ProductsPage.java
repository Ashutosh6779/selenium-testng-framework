package com.automationframework.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * ProductsPage - Page Object for the Sauce Demo inventory/products page.
 * Represents the page users land on after successful login.
 */
public class ProductsPage extends BasePage {

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "inventory_item")
    private List<WebElement> productItems;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(css = "[data-test='product-sort-container']")
    private WebElement sortDropdown;

    @FindBy(css = ".btn_inventory")
    private List<WebElement> addToCartButtons;

    // ── Actions ──────────────────────────────────────────────────────────────

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public boolean isOnProductsPage() {
        return getCurrentUrl().contains("inventory");
    }

    public int getProductCount() {
        return productItems.size();
    }

    /** Adds the first product to cart and returns this page for chaining */
    public ProductsPage addFirstItemToCart() {
        if (!addToCartButtons.isEmpty()) {
            click(addToCartButtons.get(0));
        }
        return this;
    }

    /** Returns the cart item count badge value, or "0" if badge not shown */
    public String getCartCount() {
        return isDisplayed(cartBadge) ? getText(cartBadge) : "0";
    }

    public boolean isPageTitleDisplayed() {
        return isDisplayed(pageTitle);
    }
}
