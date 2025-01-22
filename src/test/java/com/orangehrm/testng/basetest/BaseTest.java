package com.orangehrm.testng.basetest;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.orangehrm.testng.managers.TestSetupManager;
import com.orangehrm.testng.managers.WaitManager;
import com.orangehrm.testng.pages.LoginPage;
import com.orangehrm.testng.reports.ExtentReportManager;
import com.orangehrm.testng.utils.ConfigManager;
import com.orangehrm.testng.utils.LogUtil;

public class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);

    protected WebDriver driver;
    protected LoginPage loginPage;

    private final TestSetupManager testSetupManager;
    private WaitManager waitManager;

    public BaseTest() {
        this.testSetupManager = new TestSetupManager();
    }

    @BeforeClass
    @Parameters({"browser", "isHeadless"})
    public void setUp(String browser, @Optional("false") String isHeadlessParam) {
        if (browser == null) {
            // Provide a default browser if none is provided
            browser = ConfigManager.getProperty("default.browser", "chrome");
            logger.info("No browser parameter provided. Defaulting to: " + browser);
        }

        LogUtil.initLogger(LogUtil.LogLevel.INFO);

        // Load headless config from XML or default to the config file
        boolean isHeadless = resolveHeadlessConfig(isHeadlessParam);

        // Initialize WebDriver
        driver = testSetupManager.initializeDriver(browser, isHeadless);
        if (!isHeadless) {
            driver.manage().window().maximize(); // Maximizes the browser window
        }

        // Initialize WaitManager
        int waitTime = Integer.parseInt(ConfigManager.getProperty("wait.time", "10"));
        waitManager = new WaitManager(driver, waitTime);

        // Navigate to login page
        String loginUrl = ConfigManager.getProperty("login.url");
        driver.get(loginUrl);
        logger.info("Navigating to Login URL: " + loginUrl);

        // Wait for login page title to appear
        waitManager.getWait().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h5[@class='oxd-text oxd-text--h5 orangehrm-login-title']")
        ));

        // Initialize LoginPage
        loginPage = new LoginPage(driver, waitTime);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            testSetupManager.quitDriver();
            LogUtil.log("Browser closed and WebDriver instance cleaned.", LogUtil.LogLevel.INFO);
        }
    }
    
    private boolean resolveHeadlessConfig(String isHeadlessParam) {
        if (isHeadlessParam != null && !isHeadlessParam.isEmpty()) {
            return Boolean.parseBoolean(isHeadlessParam);
        }

        String configHeadless = ConfigManager.getProperty("headless.run", "false");
        return Boolean.parseBoolean(configHeadless);
    }

    protected void performLogin(String username, String password) {
        logTestParameters(username, password);
        loginPage.enterUserName(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
    }

    protected void validateDashboard() {
    	ExtentReportManager.getTest().info("Validating Dashboard display.");
        try {
            Assert.assertTrue(loginPage.isDashboardTitleDisplayed(), "Dashboard is not displayed or login failed.");
            ExtentReportManager.getTest().info("Dashboard displayed.");
            LogUtil.log("Login successful with valid credentials.", LogUtil.LogLevel.INFO);
        } catch (AssertionError e) {
        	ExtentReportManager.getTest().info("Dashboard NOT displayed.");
            LogUtil.log("Login failed with valid credentials, reason: " + e.getMessage(), LogUtil.LogLevel.ERROR);
            Assert.fail("Login failed due to: " + e.getMessage(), e);
        }
    }

  
    public void logTestParameters(String username, String password) {
    	logger.info("Parameters - Username: {}, Password: {}", username, password);
    }
    
    @BeforeSuite
    // Method to clear Allure results before each test run
    public void clearAllureResults() {
        // Get the project directory dynamically
        String projectDir = System.getProperty("user.dir");

        // Fetch the Allure results directory path from config (or use a default)
        String allureResultsDir = ConfigManager.getProperty("allure.results.dir", "allure-results");

        // Append the relative path to the allure-results directory
        File directory = new File(projectDir + File.separator + allureResultsDir);

        logger.info("Clearing Allure results at: {}", directory.getAbsolutePath());

        if (directory.exists() && directory.isDirectory()) {
            try {
                FileUtils.cleanDirectory(directory);  // Using Apache Commons IO to clean the directory
                logger.info("Allure results cleared successfully.");
            } catch (IOException e) {
                logger.error("Error clearing Allure results: ", e);
            }
        } else {
            directory.mkdirs();  // Create directory if it doesn't exist
            logger.info("Allure results directory created at: {}", directory.getAbsolutePath());
        }
    }

}