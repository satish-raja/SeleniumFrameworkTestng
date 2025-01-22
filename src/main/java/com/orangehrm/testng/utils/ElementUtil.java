/**
 * Class Name: ElementUtil
 *
 * Description:
 * Utility class for interacting with web elements in a Selenium WebDriver context.
 * Provides methods for locating elements, validating their visibility, and performing actions such as clicks and sending keys.
 * Includes retry logic with exponential backoff for robust interaction.
 *
 * Key Features:
 * - Element location with detailed logging.
 * - Visibility validation before interaction.
 * - Retry mechanism with exponential backoff for resilient operations.
 *
 * Dependencies:
 * - Selenium WebDriver for browser automation.
 * - Log4j for logging.
 *
 * Usage:
 * - Use static methods for common element interactions, such as `findElement`, `clickElement`, and `sendKeysToElement`.
 */

package com.orangehrm.testng.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;

public class ElementUtil {

    private static final Logger logger = LogManager.getLogger(ElementUtil.class);

    /**
     * Finds an element using the specified locator.
     *
     * @param driver  WebDriver instance.
     * @param locator Locator for the web element.
     * @return WebElement if found.
     */
    public static WebElement findElement(WebDriver driver, By locator) {
        try {
            WebElement element = driver.findElement(locator);
            logger.info("Element found: {}", locator);
            return element;
        } catch (NoSuchElementException e) {
            logger.error("Element not found: {}", locator, e);
            throw new NoSuchElementException("Element not found: " + locator, e);
        }
    }

    /**
     * Validates that an element is visible and interactable.
     *
     * @param element WebElement to validate.
     * @param locator Locator for logging purposes.
     */
    private static void validateElementVisibility(WebElement element, By locator) {
        if (element == null || !element.isDisplayed() || !element.isEnabled()) {
            logger.error("Element is not interactable: {} (Visible: {}, Enabled: {})", locator,
                    element != null && element.isDisplayed(), element != null && element.isEnabled());
            throw new IllegalArgumentException("Element is not interactable: " + locator);
        }
    }

    /**
     * Clicks on an element with retry and exponential backoff.
     *
     * @param driver       WebDriver instance.
     * @param locator      Locator for the web element.
     * @param retries      Number of retry attempts.
     * @param backoffDelay Initial delay for exponential backoff in milliseconds.
     */
    public static void clickElement(WebDriver driver, By locator, int retries, long backoffDelay) {
        retryWithBackoff(retries, backoffDelay, () -> {
            WebElement element = findElement(driver, locator);
            validateElementVisibility(element, locator);
            element.click();
            logger.info("Clicked on element: {}", locator);
        });
    }

    /**
     * Sends keys to an element with retry and exponential backoff.
     *
     * @param driver       WebDriver instance.
     * @param locator      Locator for the web element.
     * @param text         Text to send to the element.
     * @param retries      Number of retry attempts.
     * @param backoffDelay Initial delay for exponential backoff in milliseconds.
     */
    public static void sendKeysToElement(WebDriver driver, By locator, String text, int retries, long backoffDelay) {
        retryWithBackoff(retries, backoffDelay, () -> {
            WebElement element = findElement(driver, locator);
            validateElementVisibility(element, locator);
            element.clear();
            element.sendKeys(text);
            logger.info("Sent keys to element: {}", locator);
        });
    }

    /**
     * Retry logic with exponential backoff.
     *
     * @param retries      Number of retry attempts.
     * @param backoffDelay Initial delay for exponential backoff in milliseconds.
     * @param action       Action to perform.
     */
    private static void retryWithBackoff(int retries, long backoffDelay, Runnable action) {
        int attempts = 0;
        while (attempts < retries) {
            try {
                action.run();
                return;
            } catch (Exception e) {
                attempts++;
                if (attempts >= retries) {
                    logger.error("Failed to perform action after {} attempts", retries, e);
                    throw new RuntimeException("Action failed after retries", e);
                }
                logger.warn("Retrying action (Attempt {}/{})", attempts, retries);
                try {
                    Thread.sleep(backoffDelay * attempts); // Exponential backoff
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
