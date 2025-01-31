package com.orangehrm.testng.basetest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import com.orangehrm.testng.managers.WaitManager;
import com.orangehrm.testng.pages.LoginPage;
import com.orangehrm.testng.reports.ExtentReportManager;
import com.orangehrm.testng.utils.ConfigManager;
import com.orangehrm.testng.utils.LogUtil;
import com.orangehrm.testng.utils.TestSetupManager;

public class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);

    protected WebDriver driver;
    protected LoginPage loginPage;

    private final TestSetupManager testSetupManager;
    private WaitManager waitManager;

    public BaseTest() {
        this.testSetupManager = new TestSetupManager();
        logger.debug("TestSetupManager initialized.");
    }

    @BeforeClass
    @Parameters({"browser", "env", "logLevel"})
    public void setUp(String browser, String env, String logLevel) {
        if (browser == null) {
            browser = ConfigManager.getProperty("browser", "chrome");
            logger.info("No browser parameter provided. Defaulting to: {}", browser);
        }
        
        String environment = env != null ? env : ConfigManager.getProperty("env", "dev");
        logger.info("Using environment: {}", environment);
        
        // Set the log level
        if (logLevel != null) {
            try {
                LogUtil.LogLevel level = LogUtil.LogLevel.valueOf(logLevel.toUpperCase());
                LogUtil.initLogger(level);
                logger.info("Log level set to: {}", level);
            } catch (IllegalArgumentException e) {
                logger.error("Invalid log level provided: {}. Defaulting to INFO.", logLevel);
                LogUtil.initLogger(LogUtil.LogLevel.INFO); // Default to INFO if invalid log level
            }
        } else {
            logger.info("No logLevel parameter provided. Defaulting to INFO.");
            LogUtil.initLogger(LogUtil.LogLevel.INFO); // Default to INFO
        }

        // Check and log whether the test is using a remote driver
        boolean isRemote = Boolean.parseBoolean(ConfigManager.getProperty("webdriver.remote", "false"));
        logger.info("Running tests in remote mode: {}", isRemote);

        // Determine headless configuration
        boolean isHeadless = Boolean.parseBoolean(ConfigManager.getProperty("headless.run", "false"));
        logger.info("Headless mode: {}", isHeadless);

        // Log combined information
        logger.info("Initializing WebDriver for browser: {} (Remote: {}, Headless: {})", browser, isRemote, isHeadless);

        // Initialize WebDriver
        driver = testSetupManager.initializeDriver(browser, isHeadless, isRemote);

        if (!isHeadless) {
            driver.manage().window().maximize();
            logger.debug("Maximized browser window.");
        }

        // Navigate to login page
        String loginUrl = ConfigManager.getProperty("login.url");
        driver.get(loginUrl);
        logger.info("Navigated to Login URL: {}", loginUrl);

        // Wait for the login page to load
        waitManager = new WaitManager(driver, Integer.parseInt(ConfigManager.getProperty("wait.time", "10")));
        waitManager.getWait().until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h5[@class='oxd-text oxd-text--h5 orangehrm-login-title']")
        ));

        // Initialize LoginPage
        int timeoutInSeconds = Integer.parseInt(ConfigManager.getProperty("wait.time", "10"));
        loginPage = new LoginPage(driver, timeoutInSeconds); // Using the constructor with WebDriver and timeout

        logger.info("Initialized LoginPage successfully.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            logger.info("Closing browser and quitting WebDriver.");
            testSetupManager.quitDriver();
            LogUtil.log("Browser closed and WebDriver instance cleaned.", LogUtil.LogLevel.INFO);
        } else {
            logger.warn("WebDriver instance was null during teardown.");
        }
    }

    protected void performLogin(String username, String password) {
        logTestParameters(username, password);
        logger.info("Performing login with username: {}", username);
        loginPage.enterUserName(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
        logger.info("Login button clicked.");
    }

    protected void validateDashboard() {
        ExtentReportManager.getTest().info("Validating Dashboard display.");
        logger.info("Validating if Dashboard is displayed after login.");
        try {
            Assert.assertTrue(loginPage.isDashboardTitleDisplayed(), "Dashboard is not displayed or login failed.");
            ExtentReportManager.getTest().info("Dashboard displayed.");
            logger.info("Dashboard successfully displayed.");
        } catch (AssertionError e) {
            ExtentReportManager.getTest().info("Dashboard NOT displayed.");
            logger.error("Dashboard validation failed: {}", e.getMessage(), e);
            Assert.fail("Login failed due to: " + e.getMessage(), e);
        }
    }

    public void logTestParameters(String username, String password) {
        logger.debug("Logging test parameters: Username: {}, Password: {}", username, password);
    }
}
