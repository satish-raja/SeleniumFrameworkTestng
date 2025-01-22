package com.orangehrm.testng.listeners;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.orangehrm.testng.utils.DriverManager;
import com.orangehrm.testng.utils.ScreenshotHandler;
import com.orangehrm.testng.utils.ScreenshotUtil;
import com.orangehrm.testng.utils.TestLogger;
import com.orangehrm.testng.reports.AllureReportManager;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;

public class AllureTestListener implements ITestListener {

    private TestLogger testLogger;
    private ScreenshotHandler screenshotHandler;
    private long testStartTime;

    // No-argument constructor for TestNG to instantiate the listener
    public AllureTestListener() {
        // Initialize dependencies here if necessary
    }

    @Override
    public void onStart(ITestContext context) {
        // Initialize the testLogger here to ensure it's always available
        if (testLogger == null) {
            testLogger = new TestLogger();
        }

        if (screenshotHandler == null) {
            // Initialize ScreenshotUtil as a single consolidated class
            ScreenshotUtil screenshotUtil = new ScreenshotUtil();
            screenshotHandler = new ScreenshotHandler(screenshotUtil);
        }

        // Log the start of the test context
        testLogger.logTestContextStart(context);
        AllureReportManager.logStep("Test Context Started: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        testStartTime = System.currentTimeMillis();
        testLogger.logTestStart(result);
        AllureReportManager.logStep("Test Started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = System.currentTimeMillis() - testStartTime;
        testLogger.logTestSuccess(result, duration);
        AllureReportManager.logStep("Test Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        long duration = System.currentTimeMillis() - testStartTime;
        testLogger.logTestFailure(result, duration);

        // Capture screenshot on failure
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            screenshotHandler.handleScreenshotOnFailure(result, driver);
        }

        // Log error and failure step in Allure
        AllureReportManager.logError("Test failed", "Test Failure - " + result.getName());
        AllureReportManager.logStep("Test Failed: " + result.getName());
        Allure.getLifecycle().updateStep(s -> s.setStatus(Status.FAILED));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testLogger.logTestSkipped(result);
        AllureReportManager.logStep("Test Skipped: " + result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        AllureReportManager.logStep("Test Failed but within success percentage: " + result.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        testLogger.logTestContextFinish(context);
        AllureReportManager.logStep("Test Context Finished: " + context.getName());
    }
}
