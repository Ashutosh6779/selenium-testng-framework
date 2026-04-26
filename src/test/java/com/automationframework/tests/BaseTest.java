package com.automationframework.tests;

import com.automationframework.config.ConfigReader;
import com.automationframework.utils.DriverManager;
import com.automationframework.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

/**
 * BaseTest - Parent class for all test classes.
 *
 * Handles:
 *   - Browser initialisation before each test (@BeforeMethod)
 *   - Browser cleanup after each test (@AfterMethod)
 *   - Automatic screenshot on failure (embedded in Allure report)
 *   - Environment info added to Allure report
 *
 * All test classes extend BaseTest. They never call DriverManager directly.
 */
public class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.initDriver();

        // Add environment info to Allure report
        Allure.label("browser", ConfigReader.getBrowser());
        Allure.label("environment", ConfigReader.getEnvironment());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        // Capture screenshot on failure — attached to Allure report automatically
        if (result.getStatus() == ITestResult.FAILURE) {
            ScreenshotUtils.captureAndAttach(
                DriverManager.getDriver(),
                result.getName()
            );
        }
        DriverManager.quitDriver();
    }
}
