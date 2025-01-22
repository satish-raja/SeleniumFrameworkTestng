package com.orangehrm.testng.utils;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import com.orangehrm.testng.reports.ExtentReportManager;  // Make sure this import is correct

public class ScreenshotHandler {

    private final ScreenshotUtil screenshotUtil;

    public ScreenshotHandler(ScreenshotUtil screenshotUtil) {
        this.screenshotUtil = screenshotUtil;
    }

    // Handles screenshot capture and attachment on test failure
    public void handleScreenshotOnFailure(ITestResult result, WebDriver driver) {
        try {
            // Directly call captureAndAttachScreenshot from ScreenshotUtil
            screenshotUtil.captureAndAttachScreenshot(driver, result.getName());
        } catch (Exception e) {
            LogUtil.log("Error capturing screenshot: " + e.getMessage(), LogUtil.LogLevel.ERROR);
        }
    }

    // Captures and returns screenshot as byte array, but now also saves the screenshot to a file and returns the file path
    public String captureScreenshot(WebDriver driver, String testName) throws Exception {
        byte[] screenshot = screenshotUtil.takeScreenshot(driver);
        // Save the screenshot to a file and return the file path
        return screenshotUtil.saveScreenshot(screenshot, testName);
    }

    // Method to attach screenshot to Extent Report
    public void attachScreenshotToExtentReport(String screenshotPath) {
        try {
            // Assuming the ExtentReportManager has a method to attach the screenshot by path
            ExtentReportManager.attachScreenshot(screenshotPath);
        } catch (Exception e) {
            LogUtil.log("Error attaching screenshot to Extent Report: " + e.getMessage(), LogUtil.LogLevel.ERROR);
        }
    }
}
