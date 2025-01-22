package com.orangehrm.testng.managers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WaitManager {
    private WebDriverWait wait;

    // Constructor that takes both WebDriver and timeout value
    public WaitManager(WebDriver driver, int timeoutInSeconds) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    public WebDriverWait getWait() {
        return wait;
    }
}
