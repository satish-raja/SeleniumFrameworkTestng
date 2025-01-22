package com.orangehrm.testng.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.testng.basetest.BaseTest;
import com.orangehrm.testng.reports.ExtentReportManager;
import com.orangehrm.testng.utils.ConfigManager;
import com.orangehrm.testng.utils.TestLogger;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Feature("Login Feature")
@Test
public class TC_Login_ResetPassword extends BaseTest {

    private static final String RESET_PASSWORD_URL = ConfigManager.getProperty("resetPassword.url");
    private static final String RESET_PASSWORD_SUCCESS_URL = ConfigManager.getProperty("resetPasswordSuccessfull.url");
    private TestLogger testLogger = new TestLogger(); // Initialize TestLogger

    @Test(description = "Verify the reset password functionality of OrangeHRM", 
          groups = {"sanityTest"}) // Add groups here
    @Description("Test reset password functionality on the OrangeHRM Demo site")
    @Story("Reset Password Functionality")
    @Severity(SeverityLevel.CRITICAL)
    public void testResetPassword() {
        // Log detailed steps in the test
        ExtentReportManager.getTest().info("Starting test: testResetPassword");

        try {
            // Log the steps to Allure report via annotations and logger
        	testLogger.logInfo("Clicking on forgot password link.");
            ExtentReportManager.getTest().info("Clicking on forgot password link.");
            loginPage.clickForgotPasswordLink();

            Assert.assertEquals(loginPage.getCurrentPageUrl(), RESET_PASSWORD_URL, "Navigated to the wrong reset password page.");
            ExtentReportManager.getTest().info("Navigated to reset password page.");

            Assert.assertTrue(loginPage.isResetPasswordPageTitleDisplayed(), "Reset Password page title validation failed.");
            ExtentReportManager.getTest().info("Reset Password page title validated.");

            testLogger.logInfo("Entering username and clicking reset password.");
            ExtentReportManager.getTest().info("Entering username and clicking reset password.");
            loginPage.enterUserName("Admin");
            loginPage.clickLoginButton();

            Assert.assertEquals(loginPage.getCurrentPageUrl(), RESET_PASSWORD_SUCCESS_URL, "Navigated to the wrong success page.");
            ExtentReportManager.getTest().info("Navigated to reset password success page.");

            Assert.assertTrue(loginPage.isResetPasswordSuccessPageTitleDisplayed(), "Reset Password success page title validation failed.");
            ExtentReportManager.getTest().info("Reset Password success page title validated.");

            // Log success
            ExtentReportManager.getTest().pass("Reset password validation successful.");
        } catch (Exception e) {
            // Log failure
            ExtentReportManager.getTest().fail("Reset password validation failed: " + e.getMessage());
            throw e;
        }
    }
}
