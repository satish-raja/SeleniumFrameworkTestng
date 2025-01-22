package com.orangehrm.testng.utils;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScreenshotUtil {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);

    // Method to capture and attach screenshot to Allure
    public void captureAndAttachScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            logger.error("WebDriver cannot be null");
            throw new IllegalArgumentException("WebDriver cannot be null");
        }

        try {
            // Take screenshot
            byte[] screenshot = takeScreenshot(driver);

            // Save the screenshot
            String filePath = saveScreenshot(screenshot, testName);

            // Attach screenshot to Allure
            attachScreenshot(filePath, testName);
        } catch (IOException e) {
            logger.error("Error capturing or saving screenshot for test '{}': {}", testName, e.getMessage());
            throw new RuntimeException("Failed to capture or save screenshot", e);
        }
    }

    // Method to capture screenshot and return it as byte array
    public byte[] takeScreenshot(WebDriver driver) throws IOException {
        if (driver instanceof TakesScreenshot) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } else {
            throw new IOException("WebDriver does not support screenshots.");
        }
    }

    // Method to save screenshot to a file and return the file path
    public String saveScreenshot(byte[] screenshot, String testName) throws IOException {
        String filePath = "extent-results/screenshots/" + testName + ".png";

        // Log the path for debugging
        logger.info("Saving screenshot at: " + filePath);
        
        // Ensure the directory exists
        Files.createDirectories(Paths.get("extent-results/screenshots/"));

        // Save the screenshot to disk
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(screenshot);
        }

        return filePath;
    }

    // Method to attach screenshot to Allure
    private void attachScreenshot(String filePath, String testName) throws IOException {
        File screenshotFile = new File(filePath);
        if (screenshotFile.exists()) {
            try (FileInputStream fis = new FileInputStream(screenshotFile)) {
                Allure.addAttachment(testName + " Screenshot", fis);
            }
        }
    }
}
