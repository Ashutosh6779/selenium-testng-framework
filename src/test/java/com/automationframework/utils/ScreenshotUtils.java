package com.automationframework.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtils - Captures and attaches screenshots on test failure.
 *
 * Screenshots are:
 *   1. Saved to /target/screenshots/ (for local debugging)
 *   2. Embedded in Allure report (visible in CI pipeline reports)
 *
 * Called automatically from BaseTest's @AfterMethod via ITestResult.
 */
public class ScreenshotUtils {

    private static final String SCREENSHOT_DIR = "target/screenshots/";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtils() {}

    /**
     * Captures screenshot and attaches it to the Allure report.
     *
     * @param driver   active WebDriver instance
     * @param testName test name (used as filename)
     */
    public static void captureAndAttach(WebDriver driver, String testName) {
        if (driver == null) return;

        try {
            byte[] screenshotBytes = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);

            // Attach to Allure report
            Allure.addAttachment(
                "Screenshot - " + testName,
                "image/png",
                new ByteArrayInputStream(screenshotBytes),
                "png"
            );

            // Also save locally
            saveToFile(screenshotBytes, testName);

        } catch (Exception e) {
            System.err.println("Screenshot capture failed for test: " + testName + " | " + e.getMessage());
        }
    }

    private static void saveToFile(byte[] bytes, String testName) {
        try {
            Path dir = Paths.get(SCREENSHOT_DIR);
            Files.createDirectories(dir);

            String timestamp = LocalDateTime.now().format(FORMATTER);
            String fileName  = testName + "_" + timestamp + ".png";
            Path filePath    = dir.resolve(fileName);

            Files.write(filePath, bytes);
            System.out.println("Screenshot saved: " + filePath.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Failed to save screenshot to disk: " + e.getMessage());
        }
    }
}
