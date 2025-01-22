package com.orangehrm.testng.utils;

public interface ReportUtil {
    void logStep(String stepDescription);
    void attachScreenshot(String screenshotPath);
    void attachFile(String fileName, byte[] fileData);
    void logError(String errorMessage, String stepDescription);
}