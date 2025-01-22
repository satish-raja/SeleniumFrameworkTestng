package com.orangehrm.testng.reports;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.MediaEntityBuilder;

public class ExtentReportManager {

    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extentReports;
    private static ThreadLocal<ExtentTest> threadExtentTest = new ThreadLocal<>();

    // Initialize ExtentReports (global initialization method)
    public static synchronized ExtentReports getExtent(String reportPath, String configPath) {
        if (extentReports == null) {
            logger.info("Initializing ExtentReports...");
            try {
                // Use ExtentSparkReporter instead of ExtentHtmlReporter
                ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

                // Load the configuration (depending on your config format)
                File configFile = new File(configPath);
                if (configFile.getName().endsWith(".json")) {
                    sparkReporter.loadJSONConfig(configFile);
                    logger.info("Loaded JSON configuration: {}", configPath);
                } else if (configFile.getName().endsWith(".xml")) {
                    sparkReporter.loadXMLConfig(configFile);
                    logger.info("Loaded XML configuration: {}", configPath);
                } else {
                    logger.warn("Unsupported config format. Using default configuration.");
                }

                sparkReporter.config().setTheme(Theme.DARK);  // Optional: Set dark theme for the report

                extentReports = new ExtentReports();
                extentReports.attachReporter(sparkReporter);

                // Adding system info
                extentReports.setSystemInfo("OS", System.getProperty("os.name"));
                extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
                extentReports.setSystemInfo("User", System.getProperty("user.name"));
                logger.info("ExtentReports initialized with report path: {}", reportPath);
            } catch (Exception e) {
                logger.error("Failed to initialize ExtentReports. Error: {}", e.getMessage());
            }
        }
        return extentReports;
    }

    // Create a test in the report
    public static synchronized ExtentTest createTest(String testName, String description) {
        logger.info("Creating test: {} - {}", testName, description);
        ExtentTest test = extentReports.createTest(testName, description);
        threadExtentTest.set(test); // Save the current test in the thread-local variable
        return test;
    }

    // Get the current test
    public static synchronized ExtentTest getTest() {
        ExtentTest test = threadExtentTest.get();
        if (test == null) {
            logger.error("Attempted to get current test but no test is set.");
        }
        return test;
    }

    // Log a step
    public static void logStep(String stepDescription) {
        logger.info("Logging step: {}", stepDescription);
        try {
            getTest().log(Status.INFO, stepDescription);
        } catch (NullPointerException e) {
            logger.error("Failed to log step: {}. No active test found.", stepDescription);
        }
    }

    // Attach a screenshot
    public static void attachScreenshot(String screenshotPath) {
        try {
            getTest().addScreenCaptureFromPath(screenshotPath);
            logger.info("Screenshot attached: {}", screenshotPath);
        } catch (NullPointerException e) {
            logger.error("Failed to attach screenshot. No active test found for path: {}", screenshotPath);
        }
    }

    // Attach a file
    public static void attachFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            try {
                getTest().addScreenCaptureFromPath(file.getAbsolutePath());
                logger.info("File attached: {}", fileName);
            } catch (NullPointerException e) {
                logger.error("Failed to attach file. No active test found for file: {}", fileName);
            }
        } else {
            logger.warn("File does not exist: {}", fileName);
        }
    }

    // Log test success
    public static void logTestSuccess(ITestResult result, long duration) {
        try {
            getTest().log(Status.PASS, "Test Passed: " + result.getName() + ". Duration: " + duration + " ms");
            logger.info("Test passed: {}. Duration: {} ms", result.getName(), duration);
        } catch (NullPointerException e) {
            logger.error("Failed to log test success for: {}. No active test found.", result.getName());
        }
    }

    // Log test failure
    public static void logTestFailure(ITestResult result, long duration) {
        try {
            getTest().log(Status.FAIL, "Test Failed: " + result.getName() + ". Duration: " + duration + " ms");
            getTest().log(Status.FAIL, "Reason: " + result.getThrowable());
            logger.error("Test failed: {}. Duration: {} ms. Reason: {}", result.getName(), duration, result.getThrowable());
        } catch (NullPointerException e) {
            logger.error("Failed to log test failure for: {}. No active test found.", result.getName());
        }
    }

    // Log test skipped
    public static void logTestSkipped(ITestResult result) {
        try {
            getTest().log(Status.SKIP, "Test Skipped: " + result.getName());
            logger.warn("Test skipped: {}", result.getName());
        } catch (NullPointerException e) {
            logger.error("Failed to log test skipped for: {}. No active test found.", result.getName());
        }
    }

    // Log test failure within success percentage
    public static void logTestFailureWithinSuccessPercentage(ITestResult result) {
        try {
            getTest().log(Status.WARNING, "Test Failed but within success percentage: " + result.getName());
            logger.warn("Test failed within success percentage: {}", result.getName());
        } catch (NullPointerException e) {
            logger.error("Failed to log test failure within success percentage for: {}. No active test found.", result.getName());
        }
    }

    // Add test method info
    public static void addTestMethodInfo(ITestResult result) {
        try {
            getTest().info("Test Method: " + result.getMethod().getMethodName());
            logger.info("Added test method info: {}", result.getMethod().getMethodName());
        } catch (NullPointerException e) {
            logger.error("Failed to add test method info for: {}. No active test found.", result.getMethod().getMethodName());
        }
    }

    // Add context info
    public static void addContextInfo(ITestContext context) {
        try {
            getTest().info("Browser: " + context.getCurrentXmlTest().getParameter("browser"));
            getTest().info("Environment: " + context.getCurrentXmlTest().getParameter("env"));
            logger.info("Added context info: Browser - {}, Environment - {}",
                    context.getCurrentXmlTest().getParameter("browser"),
                    context.getCurrentXmlTest().getParameter("env"));
        } catch (NullPointerException e) {
            logger.error("Failed to add context info. No active test found.");
        }
    }

    // Log test context start
    public static void logTestContextStart(ITestContext context) {
        logger.info("Test context started: {}", context.getName());
    }

    // Log test context finish
    public static void logTestContextFinish(ITestContext context) {
        logger.info("Test context finished: {}", context.getName());
    }

    // Log test summary
    public static void logTestSummary(ITestContext context) {
        try {
            getTest().info("Test Summary: Passed = " + context.getPassedTests().size() +
                    ", Failed = " + context.getFailedTests().size() +
                    ", Skipped = " + context.getSkippedTests().size());
            logger.info("Test summary logged for context: {}", context.getName());
        } catch (NullPointerException e) {
            logger.error("Failed to log test summary. No active test found for context: {}", context.getName());
        }
    }

    // Finalize and flush the Extent report
    public static void flush() {
        if (extentReports != null) {
            extentReports.flush();
            logger.info("Extent report flushed successfully.");
        } else {
            logger.error("Extent report is not initialized. Failed to flush.");
        }
    }
    
    // Attach a screenshot using Base64 encoding
    public static void attachScreenshotAsBase64(WebDriver driver, String screenshotName) {
        if (driver == null) {
            logger.error("WebDriver is null. Cannot capture screenshot.");
            return;
        }

        try {
            // Capture screenshot as Base64
            String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);

            // Attach Base64 screenshot to the current test
            getTest().fail("Failure Screenshot: " + screenshotName, 
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());

            logger.info("Screenshot attached: {}", screenshotName);
        } catch (Exception e) {
            logger.error("Failed to attach screenshot as Base64. Error: {}", e.getMessage());
        }
    }
    
}
