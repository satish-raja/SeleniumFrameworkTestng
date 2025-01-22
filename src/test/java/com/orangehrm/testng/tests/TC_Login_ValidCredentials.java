package com.orangehrm.testng.tests;

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
public class TC_Login_ValidCredentials extends BaseTest {

    private static final String STORY_VALID = "Valid Credentials";
    private TestLogger testLogger = new TestLogger(); // Initialize TestLogger

    @Test(description = "Verify the login functionality of OrangeHRM with valid credentials", 
          dataProvider = "loginDataValid", dataProviderClass = TestDataProvider.class,
          groups = {"endtoendTest", "regressionTest", "sanityTest", "smokeTest"}) // Add groups here
    @Description("Test login with valid credentials on the OrangeHRM Demo site")
    @Story(STORY_VALID)
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginWithValidCredentials(String username, String password, String story) {

        // Start the test and log the test name
        testLogger.logInfo("Starting test: testLoginWithValidCredentials with username: " + username);
        ExtentReportManager.getTest().info("Starting test: testLoginWithValidCredentials with username: " + username);

        // Log test parameters for traceability
        logTestParameters(username, password);

        if (!STORY_VALID.equals(story)) {
            // Skip the test if the story does not match
            testLogger.logWarning("Skipping test because story is '{}' instead of '{}'.", story, STORY_VALID);
            ExtentReportManager.getTest().warning("Skipping test because story is '" + story + "' instead of '" + STORY_VALID + "'.");
            throw new SkipException("Skipping test because story is '" + story + "' instead of '" + STORY_VALID + "'.");
        }

        try {
            // Perform login and validate dashboard
            testLogger.logInfo("Performing login with username: " + username + "and password: " + password);
            ExtentReportManager.getTest().info("Performing login with username: " + username + " and password: " + password);
            performLogin(username, password);

            // Validate dashboard
            validateDashboard();
            testLogger.logInfo("Dashboard displayed successfully.");
            ExtentReportManager.getTest().info("Login successful with valid credentials");

            // Log success
//            testLogger.logInfo("Login validation for valid credentials passed.");
//            ExtentReportManager.getTest().pass("Login validation for valid credentials passed.");
        } catch (Exception e) {
            // Log failure details
            testLogger.logError("Test failed for valid credentials: " + e.getMessage());
            ExtentReportManager.getTest().fail("Test failed for valid credentials: " + e.getMessage());
            throw e;
        }
    }
}
