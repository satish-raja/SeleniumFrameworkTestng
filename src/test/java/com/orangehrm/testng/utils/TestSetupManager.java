package com.orangehrm.testng.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;


public class TestSetupManager {
	private TestLogger testLogger = new TestLogger();
    private static final String HUB_URL = ConfigManager.getProperty("webdriver.hub.url", "http://localhost:4444/wd/hub");

    /**
     * Initializes the WebDriver for the specified browser and configuration.
     *
     * @param browser    Name of the browser to initialize.
     * @param isHeadless Whether the browser should be in headless mode.
     * @param isRemote   Whether to use a remote WebDriver (Selenium Hub or Docker Grid).
     * @return The WebDriver instance.
     */
    public WebDriver initializeDriver(String browser, boolean isHeadless, boolean isRemote) {
        WebDriver driver;

        if (isRemote) {
            driver = initializeRemoteDriver(browser, isHeadless);
        } else {
            driver = BrowserFactory.getDriver(browser, isHeadless);
        }

        DriverManager.setDriver(driver);
		testLogger.logInfo("WebDriver initialized for browser: " + browser + " | Remote: " + isRemote + " | Headless: " + isHeadless);
        return driver;
    }

    /**
     * Initializes a remote WebDriver instance.
     *
     * @param browser    The browser type.
     * @param isHeadless Whether the browser should be in headless mode.
     * @return The RemoteWebDriver instance.
     */
    private WebDriver initializeRemoteDriver(String browser, boolean isHeadless) {
        try {
            URL remoteUrl = new URL(HUB_URL);
            switch (browser.toLowerCase()) {
                case "chrome":
                    return new RemoteWebDriver(remoteUrl, getChromeOptions(isHeadless));
                case "firefox":
                    return new RemoteWebDriver(remoteUrl, getFirefoxOptions(isHeadless));
                case "edge":
                    return new RemoteWebDriver(remoteUrl, getEdgeOptions(isHeadless));
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
        } catch (MalformedURLException e) {
            testLogger.logError("Invalid Selenium Hub URL: " + HUB_URL, e);
            throw new RuntimeException("Failed to initialize Remote WebDriver", e);
        }
    }

    /**
     * Returns ChromeOptions with necessary configurations.
     */
    private ChromeOptions getChromeOptions(boolean isHeadless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu", "--window-size=1920x1080");
        if (isHeadless) options.addArguments("--headless");
        return options;
    }

    /**
     * Returns FirefoxOptions with necessary configurations.
     */
    private FirefoxOptions getFirefoxOptions(boolean isHeadless) {
        FirefoxOptions options = new FirefoxOptions();
        if (isHeadless) options.addArguments("--headless");
        return options;
    }

    /**
     * Returns EdgeOptions with necessary configurations.
     */
    private EdgeOptions getEdgeOptions(boolean isHeadless) {
        EdgeOptions options = new EdgeOptions();
        if (isHeadless) options.addArguments("--headless");
        return options;
    }

    /**
     * Quits and cleans up the WebDriver instance.
     */
    public void quitDriver() {
        DriverManager.quitDriver();
        testLogger.logInfo("WebDriver instance quit successfully.");
    }
}
