package com.orangehrm.testng.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.HashMap;
import java.util.Map;

public class BrowserFactory {

    private static final Logger logger = LoggerFactory.getLogger(BrowserFactory.class);
    private static final Map<String, BrowserDriverProvider> providers = new HashMap<>();

    static {
        providers.put("chrome", new ChromeDriverProvider());
        providers.put("firefox", new FirefoxDriverProvider());
        providers.put("edge", new EdgeDriverProvider());
    }

    public static WebDriver getDriver(String browser, boolean isHeadless) {
        BrowserDriverProvider provider = providers.get(browser.toLowerCase());
        if (provider == null) {
            logger.error("Unsupported browser: {}", browser);
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        logger.info("Initializing {} driver (headless: {})", browser, isHeadless);
        return provider.createDriver(isHeadless);
    }

    public interface BrowserDriverProvider {
        WebDriver createDriver(boolean isHeadless);
    }

    public static class ChromeDriverProvider implements BrowserDriverProvider {
        @Override
        public WebDriver createDriver(boolean isHeadless) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            if (isHeadless) {
                options.addArguments("--headless", "--window-size=1920x1080");
            }
            return new ChromeDriver(options);
        }
    }

    public static class FirefoxDriverProvider implements BrowserDriverProvider {
        @Override
        public WebDriver createDriver(boolean isHeadless) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            if (isHeadless) {
                options.addArguments("--headless", "--width=1920", "--height=1080");
            }
            return new FirefoxDriver(options);
        }
    }

    public static class EdgeDriverProvider implements BrowserDriverProvider {
        @Override
        public WebDriver createDriver(boolean isHeadless) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            if (isHeadless) {
                options.addArguments("--headless", "--window-size=1920x1080");
            }
            return new EdgeDriver(options);
        }
    }

    public static void registerProvider(String browser, BrowserDriverProvider provider) {
        providers.put(browser.toLowerCase(), provider);
    }
}
