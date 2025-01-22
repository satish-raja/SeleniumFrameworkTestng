package com.orangehrm.testng.listeners;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.orangehrm.testng.reports.ExtentReportManager;
import com.orangehrm.testng.utils.DriverManager;
import com.orangehrm.testng.utils.ScreenshotHandler;
import com.orangehrm.testng.utils.ScreenshotUtil;
import com.orangehrm.testng.utils.TestLogger;

public class ExtentTestListener implements ITestListener {

    private ExtentReports extentReports;
    private TestLogger testLogger;
    private ScreenshotHandler screenshotHandler;
    private long testStartTime;

    // Constructor that initializes the dependencies
    public ExtentTestListener() {
        // Initialize the ScreenshotUtil with required dependencies
        ScreenshotUtil screenshotUtil = new ScreenshotUtil();

        // Initialize the remaining dependencies
        this.extentReports = ExtentReportManager.getExtent("extent-results/SparkReport", "src/test/resources/extent-config.json");
        this.testLogger = new TestLogger(); // Initialize TestLogger
        this.screenshotHandler = new ScreenshotHandler(screenshotUtil); // Pass ScreenshotUtil to ScreenshotHandler
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Record test start time
        testStartTime = System.currentTimeMillis();

        // Get the class name and method name
        String className = result.getTestClass().getName();
        String methodName = result.getMethod().getMethodName();

        // Extract only the class name without the package name
        String shortClassName = className.substring(className.lastIndexOf('.') + 1);

        // Format the full test name as ClassName.MethodName
        String fullTestName = shortClassName + "." + methodName;

        // Create ExtentTest for the test method
        ExtentTest test = ExtentReportManager.createTest(fullTestName, result.getMethod().getDescription());
        
        // Capture groups and add them to the report
        String[] groups = result.getMethod().getGroups();
        for (String group : groups) {
            test.assignCategory(group);  // Assign group to the test in the report
        }

        if (test != null) {
            testLogger.logTestStart(result);  // Log test start details
            ExtentReportManager.addTestMethodInfo(result);  // Add test method info to Extent report
            testLogger.logInfo("Test started: " + fullTestName);
        } else {
            testLogger.logError("Failed to create ExtentTest object for: " + fullTestName);
        }
    }


    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = System.currentTimeMillis() - testStartTime;
        ExtentTest test = ExtentReportManager.getTest();
        
        if (test != null) {
            testLogger.logTestSuccess(result, duration);  // Log test success details
            ExtentReportManager.logTestSuccess(result, duration);  // Log success in Extent report
            testLogger.logInfo("Test passed: " + result.getName() + " | Duration: " + duration + " ms");
        } else {
            testLogger.logError("Test object is null during success logging for: " + result.getName());
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        long duration = System.currentTimeMillis() - testStartTime;
        ExtentTest test = ExtentReportManager.getTest();

        if (test != null) {
            WebDriver driver = DriverManager.getDriver();
            if (driver != null) {
            	String screenshotName = "Failure_" + result.getName() + "_"
            	        + result.getThrowable().getMessage()
            	              .replaceAll("[^a-zA-Z0-9 ]", "") // Remove special characters except spaces
            	              .replaceAll(" ", "_") // Replace spaces with underscores
            	        + ".png";

                // Attach screenshot as Base64
                ExtentReportManager.attachScreenshotAsBase64(driver, screenshotName);
            }

            ExtentReportManager.logTestFailure(result, duration);
            testLogger.logError("Test failed: " + result.getName() + " | Duration: " + duration + " ms");
        } else {
            testLogger.logError("Test object is null during failure logging for: " + result.getName());
        }
    }



    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        
        if (test != null) {
            testLogger.logTestSkipped(result);  // Log skipped test
            ExtentReportManager.logTestSkipped(result);  // Log skipped test in Extent report
            testLogger.logInfo("Test skipped: " + result.getName());
        } else {
            testLogger.logError("Test object is null during skipped logging for: " + result.getName());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        
        if (test != null) {
            ExtentReportManager.logTestFailureWithinSuccessPercentage(result);
            testLogger.logWarning("Test failed but within success percentage: " + result.getName());
        } else {
            testLogger.logError("Test object is null during partial success logging for: " + result.getName());
        }
    }

    @Override
    public void onStart(ITestContext context) {
        testLogger.logTestContextStart(context);  // Log test context start
        ExtentTest test = ExtentReportManager.getTest();
        
        if (test != null) {
            ExtentReportManager.addContextInfo(context);  // Add context info to Extent report
            testLogger.logInfo("Test context started: " + context.getName());
        } else {
            testLogger.logError("Test object is null during context start for: " + context.getName());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        testLogger.logTestContextFinish(context);  // Log test context finish
        ExtentTest test = ExtentReportManager.getTest();
        
        if (test != null) {
            ExtentReportManager.logTestSummary(context);  // Log test summary
            testLogger.logInfo("Test context finished: " + context.getName());
        } else {
            testLogger.logError("Test object is null during context finish for: " + context.getName());
        }
        
        try {
            // Finalize and flush the Extent report
        	ExtentReportManager.flush();  
            testLogger.logInfo("Extent report flushed successfully.");
        } catch (Exception e) {
            testLogger.logError("Failed to flush ExtentReports: " + e.getMessage());
        }
    }
}
