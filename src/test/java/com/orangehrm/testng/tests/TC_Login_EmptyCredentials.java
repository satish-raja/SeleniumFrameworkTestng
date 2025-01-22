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

    private static final String REQUIRED_FIELD_ERROR = "Require";
    private TestLogger testLogger = new TestLogger(); // Initialize TestLogger

    @Test(description = "Verify the login functionality of OrangeHRM with empty credentials to validate required field errors", 
          groups = {"sanityTest"}) // Add group here
    @Description("Test login with empty credentials on the OrangeHRM Demo site")
    @Story("Login with empty credentials")
    @Severity(SeverityLevel.MINOR)
    public void testLoginWithEmptyCredentials() {
        // Log detailed steps in the test
        ExtentReportManager.getTest().info("Starting test: testLoginWithEmptyCredentials");

        try {
            // Perform login with empty credentials
            performLogin("", "");
            ExtentReportManager.getTest().info("Entered empty username and password.");

            // Verify username field error
            String usernameError = loginPage.getUsernameFieldErrorMessage();
            Assert.assertEquals(usernameError, REQUIRED_FIELD_ERROR, "Username error validation");
            ExtentReportManager.getTest().info("Username field error validated.");

            // Verify password field error
            String passwordError = loginPage.getPasswordFieldErrorMessage();
            Assert.assertEquals(passwordError, REQUIRED_FIELD_ERROR, "Password error validation");
            ExtentReportManager.getTest().info("Password field error validated.");

            // Log success
            ExtentReportManager.getTest().info("Login validation for empty credentials passed.");
        } catch (AssertionError e) {
            // Log failure details
            ExtentReportManager.getTest().info("Assertion failed for empty credentials: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Log failure details
            ExtentReportManager.getTest().info("Test failed for empty credentials: " + e.getMessage());
            throw e;
        }
    }
}
