package com.orangehrm.testng.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.testng.basetest.BaseTest;
import com.orangehrm.testng.reports.ExtentReportManager;
import com.orangehrm.testng.utils.TestLogger;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Feature("Login Feature")
@Test
public class TC_Login_EmptyCredentials extends BaseTest {

    private static final String REQUIRED_FIELD_ERROR = "Required";
    private TestLogger testLogger = new TestLogger(); // Initialize TestLogger

    @Test(description = "Verify the login functionality of OrangeHRM with empty credentials to validate required field errors", 
          groups = {"sanityTest"}) // Add group here
    @Description("Test login with empty credentials on the OrangeHRM Demo site")
    @Story("Login with empty credentials")
    @Severity(SeverityLevel.MINOR)
    public void testLoginWithEmptyCredentials() {
        // Log detailed steps for Extent Report and TestLogger
        String logMessage = "Starting test: testLoginWithEmptyCredentials";
        ExtentReportManager.getTest().info(logMessage);  // Log to Extent Report
        testLogger.logInfo(logMessage);  // Log to TestLogger

        try {
            // Perform login with empty credentials
            performLogin("", "");
            logMessage = "Entered empty username and password.";
            ExtentReportManager.getTest().info(logMessage);  // Log to Extent Report
            testLogger.logInfo(logMessage);  // Log to TestLogger

            // Verify username field error
            String usernameError = loginPage.getUsernameFieldErrorMessage();
            Assert.assertEquals(usernameError, REQUIRED_FIELD_ERROR, "Username error validation");
            logMessage = "Username field error validated.";
            ExtentReportManager.getTest().info(logMessage);  // Log to Extent Report
            testLogger.logInfo(logMessage);  // Log to TestLogger

            // Verify password field error
            String passwordError = loginPage.getPasswordFieldErrorMessage();
            Assert.assertEquals(passwordError, REQUIRED_FIELD_ERROR, "Password error validation");
            logMessage = "Password field error validated.";
            ExtentReportManager.getTest().info(logMessage);  // Log to Extent Report
            testLogger.logInfo(logMessage);  // Log to TestLogger

            // Log success
            logMessage = "Login validation for empty credentials passed.";
            ExtentReportManager.getTest().info(logMessage);  // Log to Extent Report
            testLogger.logInfo(logMessage);  // Log to TestLogger
        } catch (AssertionError e) {
            // Log failure details
            logMessage = "Assertion failed for empty credentials: " + e.getMessage();
            ExtentReportManager.getTest().info(logMessage);  // Log to Extent Report
            testLogger.logError(logMessage);  // Log to TestLogger
            throw e;
        } catch (Exception e) {
            // Log failure details
            logMessage = "Test failed for empty credentials: " + e.getMessage();
            ExtentReportManager.getTest().info(logMessage);  // Log to Extent Report
            testLogger.logError(logMessage);  // Log to TestLogger
            throw e;
        }
    }
}
