/**
 * Class Name: ElementVisibilityWait
 *
 * Description:
 * Provides utility methods for handling visibility-related waits for web elements using FluentWait.
 *
 * Key Features:
 * - Wait for an element to be visible.
 * - Wait for an element to be clickable.
 * - Check if an element is displayed within a timeout.
 * - Flexible timeout and polling interval configuration.
 *
 * Dependencies:
 * - Selenium WebDriver for browser automation.
 * - FluentWait for implementing dynamic waits.
 */

package com.orangehrm.testng.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

public class ElementVisibilityWait {

    private final FluentWait<WebDriver> wait;

    /**
     * Constructor for ElementVisibilityWait.
     * Accepts a pre-configured FluentWait instance for flexibility.
     *
     * @param wait Pre-configured FluentWait instance.
     */
    public ElementVisibilityWait(FluentWait<WebDriver> wait) {
        this.wait = wait;
    }

    /**
     * Waits for an element to be visible.
     *
     * @param locator The locator for the element.
     * @param timeout Timeout in seconds.
     * @return The visible WebElement.
     */
    public WebElement waitForElementToBeVisible(By locator, int timeout) {
        return wait.withTimeout(Duration.ofSeconds(timeout))
                   .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for an element to be clickable.
     *
     * @param locator The locator for the element.
     * @param timeout Timeout in seconds.
     * @return The clickable WebElement.
     */
    public WebElement waitForElementToBeClickable(By locator, int timeout) {
        return wait.withTimeout(Duration.ofSeconds(timeout))
                   .until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Checks if an element is displayed within the timeout period.
     *
     * @param locator The locator for the element.
     * @param timeout Timeout in seconds.
     * @return True if the element is displayed; otherwise, false.
     */
    public boolean isElementDisplayed(By locator, int timeout) {
        try {
            return wait.withTimeout(Duration.ofSeconds(timeout))
                       .until(ExpectedConditions.visibilityOfElementLocated(locator))
                       .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Factory method for creating a FluentWait instance.
     *
     * @param driver  The WebDriver instance.
     * @param timeout The timeout in seconds.
     * @param polling The polling interval in milliseconds.
     * @return A configured FluentWait instance.
     */
    public static FluentWait<WebDriver> createFluentWait(WebDriver driver, int timeout, long polling) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(polling))
                .ignoring(Exception.class);
    }
}
