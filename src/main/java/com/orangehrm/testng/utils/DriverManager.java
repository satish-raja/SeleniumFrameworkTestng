/**
 * Class Name: DriverManager
 *
 * Description:
 * This class manages the lifecycle of WebDriver instances, ensuring that a WebDriver instance is tied to the current thread.
 * It provides methods to initialize a WebDriver for different browsers, retrieve the current WebDriver, and quit the WebDriver instance.
 * Additionally, it handles logging for each WebDriver-related operation using BrowserFactory.
 *
 * Key Features:
 * - Initializes WebDriver for different browsers using BrowserFactory.
 * - Manages WebDriver instances with ThreadLocal to ensure thread-safety.
 * - Provides methods to set, get, and quit WebDriver instances.
 * - Supports logging for each operation related to WebDriver management.
 *
 * Dependencies:
 * - Selenium WebDriver for browser automation.
 * - SLF4J (Log4j) for logging.
 * - BrowserFactory for WebDriver initialization based on browser type.
 *
 * Usage:
 * - Call `initializeDriver(String browser, boolean isHeadless)` to set up a WebDriver instance.
 * - Use `getDriver()` to retrieve the WebDriver instance for the current thread.
 * - Call `quitDriver()` to safely terminate and remove the WebDriver instance.
 */


package com.orangehrm.testng.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class Name: DriverManager
 *
 * Description:
 * This class manages the lifecycle of WebDriver instances, ensuring that a WebDriver instance is tied to the current thread.
 * It provides methods to initialize a WebDriver for different browsers, retrieve the current WebDriver, and quit the WebDriver instance.
 * Additionally, it handles logging for each WebDriver-related operation using BrowserFactory.
 *
 * Key Features:
 * - Initializes WebDriver for different browsers using BrowserFactory.
 * - Manages WebDriver instances with ThreadLocal to ensure thread-safety.
 * - Provides methods to set, get, and quit WebDriver instances.
 * - Supports logging for each operation related to WebDriver management.
 *
 * Dependencies:
 * - Selenium WebDriver for browser automation.
 * - SLF4J (Log4j) for logging.
 */
public class DriverManager {

    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = ThreadLocal.withInitial(() -> null);

    /**
     * Set the WebDriver instance for the current thread.
     *
     * @param driver The WebDriver instance to set.
     * @throws IllegalArgumentException if the driver is null.
     */
    public static void setDriver(WebDriver driver) {
        if (driver == null) {
            logger.error("Cannot set a null WebDriver instance.");
            throw new IllegalArgumentException("WebDriver cannot be null.");
        }

        if (driverThreadLocal.get() != null) {
            logger.debug("Reinitializing WebDriver instance.");
        }

        driverThreadLocal.set(driver);
        logger.info("WebDriver instance set for the current thread: {}", driver.getClass().getSimpleName());
    }

    /**
     * Get the WebDriver instance for the current thread.
     *
     * @return The WebDriver instance.
     * @throws IllegalStateException if no WebDriver instance is set for the current thread.
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            logger.error("No WebDriver instance found for the current thread.");
            throw new IllegalStateException("No WebDriver instance found for the current thread.");
        }
        return driver;
    }

    /**
     * Initialize a WebDriver instance using BrowserFactory.
     *
     * @param browser The browser name (e.g., "chrome", "firefox").
     * @param isHeadless If true, the WebDriver will be initialized in headless mode.
     * @throws IllegalArgumentException if the browser is unsupported.
     */
    public static void initializeDriver(String browser, boolean isHeadless) {
        try {
            logger.info("Initializing WebDriver for browser: {} with headless mode: {}", browser, isHeadless);
            WebDriver driver = BrowserFactory.getDriver(browser, isHeadless);
            setDriver(driver);
            logger.info("WebDriver for {} initialized successfully.", browser);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to initialize WebDriver: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Quit the WebDriver instance and remove it from ThreadLocal.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver instance quit successfully.");
            } catch (WebDriverException e) {
                logger.error("Error while quitting WebDriver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
                logger.debug("WebDriver instance removed from ThreadLocal.");
            }
        } else {
            logger.warn("No WebDriver instance found to quit.");
        }
    }
}
