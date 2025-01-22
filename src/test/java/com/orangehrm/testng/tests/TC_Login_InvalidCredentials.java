package com.orangehrm.testng.tests;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.testng.basetest.BaseTest;
import com.orangehrm.testng.reports.ExtentReportManager;
import com.orangehrm.testng.utils.TestDataProvider;
import com.orangehrm.testng.utils.TestLogger;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Feature("Login Feature")
@Test
public class TC_Login_InvalidCredentials extends BaseTest {

    private static final String STORY_INVALID = "Invalid Credentials";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials";
    private TestLogger testLogger = new TestLogger(); // Initialize TestLogger

    @Test(description = "Verify the login functionality of OrangeHRM with invalid credentials", 
          dataProvider = "loginDataInvalid", dataProviderClass = TestDataProvider.class, 
          groups = {"regressionTest", "sanityTest"}) // Add groups here
    @Description("Test login with invalid credentials on the OrangeHRM Demo site")
    @Story(STORY_INVALID)
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWithInvalidCredentials(String username, String password, String story) {

        // Start the test and log the test name
    	testLogger.logInfo("Starting test: testLoginWithInvalidCredentials with username: " + username);
        ExtentReportManager.getTest().info("Starting test: testLoginWithInvalidCredentials with username: " + username);
        logTestParameters(username, password);

        if (!STORY_INVALID.equals(story)) {
            // Skip the test if the story does not match
        	testLogger.logWarning("Skipping test because story is '{}' instead of '{}'.", story, STORY_INVALID);
            ExtentReportManager.getTest().warning("Skipping test because story is '" + story + "' instead of '" + STORY_INVALID + "'.");
            throw new SkipException("Skipping test because story is '" + story + "' instead of '" + STORY_INVALID + "'.");
        }

        try {
        	  testLogger.logInfo("Performing login with username: " + username + " and password: " + password);
              ExtentReportManager.getTest().info("Performing login with username: " + username + "and password: " + password);
            // Perform login with invalid credentials
            performLogin(username, password);

            // Validate the error message after attempting login
            Assert.assertEquals(loginPage.getLoginErrorMessage(), INVALID_CREDENTIALS_MESSAGE, "Error message does not match!");
            ExtentReportManager.getTest().info("Invalid credentials error message validated.");

            // Log success
            ExtentReportManager.getTest().pass("Login validation for invalid credentials passed.");
        } catch (Exception e) {
            // Log failure details
            ExtentReportManager.getTest().fail("Test failed for invalid credentials: " + e.getMessage());
            throw e;
        }
    }
}
