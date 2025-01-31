package com.orangehrm.testng.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BrowserFactory {

    private static final Logger logger = LoggerFactory.getLogger(BrowserFactory.class);
    private static final Map<String, BrowserDriverProvider> providers = new HashMap<>();
    private static final String REMOTE_DRIVER_URL = ConfigManager.getProperty("webdriver.hub.url");
    private static final boolean IS_REMOTE = Boolean.parseBoolean(ConfigManager.getProperty("webdriver.remote", "false"));

    static {
        providers.put("chrome", new ChromeDriverProvider());
        providers.put("firefox", new FirefoxDriverProvider());
        providers.put("edge", new EdgeDriverProvider());
    }

    public static WebDriver getDriver(String browser, boolean isHeadless) {
        logger.debug("getDriver called with browser: {}, headless: {}", browser, isHeadless);
        if (IS_REMOTE) {
            logger.info("Using remote WebDriver configuration");
            return getRemoteDriver(browser, isHeadless);
        } else {
            BrowserDriverProvider provider = providers.get(browser.toLowerCase());
            if (provider == null) {
                logger.error("Unsupported browser: {}", browser);
                throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
            logger.info("Initializing local {} WebDriver (headless: {})", browser, isHeadless);
            WebDriver driver = provider.createDriver(isHeadless);
            logger.info("Successfully initialized {} WebDriver", browser);
            return driver;
        }
    }

    public static WebDriver getRemoteDriver(String browser, boolean isHeadless) {
        try {
            logger.debug("getRemoteDriver called with browser: {}, headless: {}", browser, isHeadless);
            DesiredCapabilities capabilities = new DesiredCapabilities();

            if (browser.equalsIgnoreCase("chrome")) {
                capabilities.setBrowserName("chrome");
                ChromeOptions options = new ChromeOptions();
                if (isHeadless) {
                    options.addArguments("--headless", "--window-size=1920x1080");
                }
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            } else if (browser.equalsIgnoreCase("firefox")) {
                capabilities.setBrowserName("firefox");
                FirefoxOptions options = new FirefoxOptions();
                if (isHeadless) {
                    options.addArguments("--headless", "--width=1920", "--height=1080");
                }
                capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);
            } else if (browser.equalsIgnoreCase("edge")) {
                capabilities.setBrowserName("MicrosoftEdge");
                EdgeOptions options = new EdgeOptions();
                if (isHeadless) {
                    options.addArguments("--headless", "--window-size=1920x1080");
                }
                capabilities.setCapability(EdgeOptions.CAPABILITY, options);
            } else {
                logger.error("Unsupported browser for remote execution: {}", browser);
                throw new IllegalArgumentException("Unsupported browser: " + browser);
            }

            logger.info("Connecting to Remote WebDriver at: {}", REMOTE_DRIVER_URL);
            WebDriver remoteDriver = new RemoteWebDriver(new URL(REMOTE_DRIVER_URL), capabilities);
            logger.info("Successfully connected to Remote WebDriver for browser: {}", browser);
            return remoteDriver;
        } catch (Exception e) {
            logger.error("Failed to create Remote WebDriver", e);
            throw new RuntimeException("Failed to create Remote WebDriver", e);
        }
    }

    public interface BrowserDriverProvider {
        WebDriver createDriver(boolean isHeadless);
    }

    public static class ChromeDriverProvider implements BrowserDriverProvider {
        @Override
        public WebDriver createDriver(boolean isHeadless) {
            logger.debug("Creating Chrome WebDriver (headless: {})", isHeadless);
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            if (isHeadless) {
                options.addArguments("--headless", "--window-size=1920x1080");
            }
            WebDriver driver = new ChromeDriver(options);
            logger.info("Chrome WebDriver initialized successfully");
            return driver;
        }
    }

    public static class FirefoxDriverProvider implements BrowserDriverProvider {
        @Override
        public WebDriver createDriver(boolean isHeadless) {
            logger.debug("Creating Firefox WebDriver (headless: {})", isHeadless);
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            if (isHeadless) {
                options.addArguments("--headless", "--width=1920", "--height=1080");
            }
            WebDriver driver = new FirefoxDriver(options);
            logger.info("Firefox WebDriver initialized successfully");
            return driver;
        }
    }

    public static class EdgeDriverProvider implements BrowserDriverProvider {
        @Override
        public WebDriver createDriver(boolean isHeadless) {
            logger.debug("Creating Edge WebDriver (headless: {})", isHeadless);
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            if (isHeadless) {
                options.addArguments("--headless", "--window-size=1920x1080");
            }
            WebDriver driver = new EdgeDriver(options);
            logger.info("Edge WebDriver initialized successfully");
            return driver;
        }
    }

    public static void registerProvider(String browser, BrowserDriverProvider provider) {
        logger.debug("Registering custom provider for browser: {}", browser);
        providers.put(browser.toLowerCase(), provider);
    }
}
