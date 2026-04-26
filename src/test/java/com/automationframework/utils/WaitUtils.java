package com.automationframework.utils;

import com.automationframework.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitUtils - Centralised explicit wait helpers.
 *
 * Never use Thread.sleep() in tests — it makes suites brittle and slow.
 * These methods use WebDriverWait with ExpectedConditions for reliable,
 * dynamic synchronisation.
 */
public class WaitUtils {

    private WaitUtils() {}

    private static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    private static WebDriverWait getWait(WebDriver driver, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /** Waits until element is present in DOM and visible on page */
    public static WebElement waitForVisibility(WebDriver driver, WebElement element) {
        return getWait(driver).until(ExpectedConditions.visibilityOf(element));
    }

    /** Waits until element is clickable (visible + enabled) */
    public static WebElement waitForClickability(WebDriver driver, WebElement element) {
        return getWait(driver).until(ExpectedConditions.elementToBeClickable(element));
    }

    /** Waits until a By locator matches a visible element — useful in base class */
    public static WebElement waitForVisibilityByLocator(WebDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Waits until element disappears (e.g., loading spinner) */
    public static boolean waitForInvisibility(WebDriver driver, WebElement element) {
        return getWait(driver).until(ExpectedConditions.invisibilityOf(element));
    }

    /** Waits until page URL contains the expected substring */
    public static boolean waitForUrlContains(WebDriver driver, String urlFragment) {
        return getWait(driver).until(ExpectedConditions.urlContains(urlFragment));
    }

    /** Waits until page title contains the expected substring */
    public static boolean waitForTitleContains(WebDriver driver, String titleFragment) {
        return getWait(driver).until(ExpectedConditions.titleContains(titleFragment));
    }

    /** Custom timeout override — useful for slow operations like file uploads */
    public static WebElement waitForVisibility(WebDriver driver, WebElement element, int timeoutSeconds) {
        return getWait(driver, timeoutSeconds).until(ExpectedConditions.visibilityOf(element));
    }
}
