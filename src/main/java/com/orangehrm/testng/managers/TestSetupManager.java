package com.orangehrm.testng.managers;

import org.openqa.selenium.WebDriver;
import com.orangehrm.testng.utils.BrowserFactory;
import com.orangehrm.testng.utils.DriverManager;

public class TestSetupManager {

    /**
     * Initializes the WebDriver for the specified browser and configuration.
     *
     * @param browser    Name of the browser to initialize.
     * @param isHeadless Whether the browser should be in headless mode.
     * @return The WebDriver instance.
     */
    public WebDriver initializeDriver(String browser, boolean isHeadless) {
        WebDriver driver = BrowserFactory.getDriver(browser, isHeadless);
        DriverManager.setDriver(driver); // Store the driver instance in DriverManager
        return driver;
    }

    /**
     * Quits and cleans up the WebDriver instance.
     */
    public void quitDriver() {
        DriverManager.quitDriver();
    }
}
