package com.orangehrm.testng.utils;

import java.time.LocalDateTime;
import org.testng.ITestContext;
import org.testng.ITestResult;

public class TestLogger {

    public void logTestStart(ITestResult result) {
        LogUtil.log("Test Started: " + result.getName() + " at " + LocalDateTime.now(), LogUtil.LogLevel.INFO);
    }

    public void logTestSuccess(ITestResult result, long duration) {
        LogUtil.log("Test Passed: " + result.getName() + " Duration: " + duration + " ms", LogUtil.LogLevel.INFO);
    }

    public void logTestFailure(ITestResult result, long duration) {
        LogUtil.log("Test Failed: " + result.getName() + " Duration: " + duration + " ms", LogUtil.LogLevel.ERROR);
    }

    public void logTestSkipped(ITestResult result) {
        LogUtil.log("Test Skipped: " + result.getName(), LogUtil.LogLevel.WARN);
    }

    public void logTestContextStart(ITestContext context) {
        LogUtil.log("Test Context Started: " + context.getName(), LogUtil.LogLevel.INFO);
    }

    public void logTestContextFinish(ITestContext context) {
        LogUtil.log("Test Context Finished: " + context.getName(), LogUtil.LogLevel.INFO);
    }

    // New logError method
    public void logError(String message) {
        LogUtil.log(message, LogUtil.LogLevel.ERROR);
    }

    // New logInfo method
    public void logInfo(String message) {
        LogUtil.log(message, LogUtil.LogLevel.INFO);
    }

    // New logWarning method
    public void logWarning(String message) {
        LogUtil.log(message, LogUtil.LogLevel.WARN);
    }
    
    public void logWarning(String message, Object... params) {
        LogUtil.log(String.format(message, params), LogUtil.LogLevel.WARN);
    }
}
