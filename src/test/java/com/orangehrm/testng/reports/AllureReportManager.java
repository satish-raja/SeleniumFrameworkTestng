package com.orangehrm.testng.reports;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AllureReportManager {

    private static final Logger logger = LogManager.getLogger(AllureReportManager.class);

    private static final String ALLURE_RESULTS_DIR = System.getProperty("user.dir") + File.separator + "allure-results";
    
    // Initializes Allure results directory
    static {
        try {
            Path allureResultsPath = Paths.get(ALLURE_RESULTS_DIR);
            if (Files.exists(allureResultsPath)) {
                logger.info("Allure results directory exists at: {}", ALLURE_RESULTS_DIR);
            } else {
                Files.createDirectories(allureResultsPath);
                logger.info("Created Allure results directory at: {}", ALLURE_RESULTS_DIR);
            }
        } catch (IOException e) {
            logger.error("Failed to initialize Allure results directory: {}", e.getMessage());
        }
    }

    // Log a step to Allure report
    @Step("{0}")
    public static void logStep(String stepDescription) {
        logger.info("Logging step: {}", stepDescription);
        Allure.step(stepDescription);
    }

    // Attach a screenshot to Allure report
    public static void attachScreenshot(String screenshotPath) {
        try (FileInputStream screenshotStream = new FileInputStream(screenshotPath)) {
            Allure.addAttachment("Screenshot", screenshotStream);
            logger.info("Screenshot attached: {}", screenshotPath);
        } catch (IOException e) {
            logger.error("Error attaching screenshot at {}: {}", screenshotPath, e.getMessage());
        }
    }

    // Attach an arbitrary file to Allure report
    public static void attachFile(String fileName, byte[] fileData) {
        try {
            Allure.addAttachment(fileName, new ByteArrayInputStream(fileData));
            logger.info("File attached: {}", fileName);
        } catch (Exception e) {
            logger.error("Error attaching file: {}. Error: {}", fileName, e.getMessage());
        }
    }

    // Log an error to Allure report with timestamp and step details
    public static void logError(String errorMessage, String stepDescription) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String errorLog = String.format("Error at %s in step: '%s'. Message: %s", timestamp, stepDescription, errorMessage);
        Allure.addAttachment("Error", errorLog);
        logger.error("Error logged at {} in step: {}. Message: {}", timestamp, stepDescription, errorMessage);
    }

    // Attach a failure screenshot with the test name for failed tests
    public static void attachFailureScreenshot(String filePath, String testName) {
        try (FileInputStream screenshotStream = new FileInputStream(filePath)) {
            Allure.addAttachment(testName + " Failure Screenshot", screenshotStream);
            logger.info("Failure screenshot attached for test: {}", testName);
        } catch (IOException e) {
            logger.error("Error attaching failure screenshot for test: {}. Error: {}", testName, e.getMessage());
        }
    }

    // Clears the Allure results directory
    public static void clearAllureResultsDirectory() {
        try {
            Path allureResultsPath = Paths.get(ALLURE_RESULTS_DIR);
            if (Files.exists(allureResultsPath) && Files.isDirectory(allureResultsPath)) {
                Files.walk(allureResultsPath)
                        .map(Path::toFile)
                        .forEach(File::delete);
                logger.info("Allure results directory cleared successfully.");
            }
        } catch (IOException e) {
            logger.error("Error clearing Allure results directory: {}", e.getMessage());
        }
    }

    // Log the successful completion of a test
    public static void logTestSuccess(String testName, long duration) {
        logger.info("Test '{}' passed successfully. Duration: {} ms", testName, duration);
        Allure.step("Test Passed: " + testName + " - Duration: " + duration + " ms");
    }

    // Log test failure details
    public static void logTestFailure(String testName, Throwable throwable, long duration) {
        logger.error("Test '{}' failed. Duration: {} ms", testName, duration);
        logger.error("Failure cause: {}", throwable.getMessage());
        Allure.addAttachment("Test Failure Details", throwable.toString());
        Allure.step("Test Failed: " + testName + " - Duration: " + duration + " ms. Cause: " + throwable.getMessage());
    }

    // Log skipped tests
    public static void logTestSkipped(String testName) {
        logger.warn("Test '{}' was skipped.", testName);
        Allure.step("Test Skipped: " + testName);
    }

    // Log test summary
    public static void logTestSummary(int passed, int failed, int skipped) {
        String summary = String.format("Test Run Summary - Passed: %d, Failed: %d, Skipped: %d", passed, failed, skipped);
        logger.info(summary);
        Allure.addAttachment("Test Run Summary", summary);
    }
}
