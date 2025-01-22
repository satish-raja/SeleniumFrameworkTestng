/**
 * Class Name: ElementTextWait
 *
 * Description:
 * Utility class for waiting until specific text appears in a web element using Selenium's FluentWait.
 * Provides configurable timeouts and polling intervals to handle dynamic content loading scenarios.
 *
 * Key Features:
 * - Waits for text to appear in a visible web element.
 * - Customizable timeout and polling interval.
 * - Uses Selenium's FluentWait for robust waiting logic.
 *
 * Dependencies:
 * - Selenium WebDriver for browser automation.
 */

package com.orangehrm.testng.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

public class ElementTextWait {

    private final FluentWait<WebDriver> wait;

    /**
     * Constructor to initialize FluentWait with a specified timeout and polling interval.
     *
     * @param driver        WebDriver instance.
     * @param timeout       Timeout in seconds.
     * @param pollingMillis Polling interval in milliseconds.
     */
    public ElementTextWait(WebDriver driver, int timeout, long pollingMillis) {
        this.wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(pollingMillis))
                .ignoring(Exception.class);
    }

    /**
     * Waits for the visibility of an element located by the given locator.
     *
     * @param locator The locator for the element.
     * @return The visible WebElement.
     */
    public WebElement waitForElementVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for the specified text to be present in a visible element.
     *
     * @param locator The locator for the element.
     * @param text    The text to wait for.
     * @return The WebElement containing the specified text.
     * @throws WaitTimeoutException If the text is not found within the timeout period.
     */
    public WebElement waitForTextInElement(By locator, String text) throws WaitTimeoutException {
        WebElement element = waitForElementVisibility(locator);
        if (element.getText().contains(text)) {
            return element;
        } else {
            throw new WaitTimeoutException("Text not found in element: " + text);
        }
    }
}

/**
 * Custom exception for wait-related timeouts.
 */
class WaitTimeoutException extends RuntimeException {
    public WaitTimeoutException(String message) {
        super(message);
    }
}
