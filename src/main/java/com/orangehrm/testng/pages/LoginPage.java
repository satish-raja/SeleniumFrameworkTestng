package com.orangehrm.testng.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.orangehrm.testng.managers.WaitManager;
import com.orangehrm.testng.page.locators.LoginPageLocators;
import com.orangehrm.testng.utils.LogUtil;
import com.orangehrm.testng.utils.ElementVisibilityWait;
import com.orangehrm.testng.utils.ElementTextWait;

/**
 * LoginPage class represents the login page and contains all the interactions
 * and actions that can be performed on the login page, such as entering username, 
 * entering password, clicking the login button, and handling password reset functionality.
 */
public class LoginPage {

    private WebDriver driver;
    private ElementVisibilityWait visibilityWait; // Utility class for waiting actions (element visibility)
    private ElementTextWait textWait;   // Utility class for waiting actions related to text matching


    /**
     * Constructor to initialize the LoginPage with WebDriver and default timeout.
     * @param driver WebDriver instance for interacting with the browser.
     */
    public LoginPage(WebDriver driver, int timeoutInSeconds) {
        if (driver == null) {
            throw new NullPointerException("WebDriver instance is not initialized.");
        }
        this.driver = driver;

        // Use WaitManager to create a WebDriverWait instance
        WaitManager waitManager = new WaitManager(driver, timeoutInSeconds);
        
        // Initialize ElementVisibilityWait and ElementTextWait using WaitManager
        this.visibilityWait = new ElementVisibilityWait(waitManager.getWait());
        this.textWait = new ElementTextWait(driver, timeoutInSeconds, 500);  // timeout = 10s, polling = 500ms
    }

    /**
     * Enters the username into the username input field.
     * Logs the action for tracking purposes.
     * @param username The username to enter.
     */
    public void enterUserName(String username) {
        WebElement usernameInput = visibilityWait.waitForElementToBeVisible(LoginPageLocators.getUsernameInputLocator(), 10);
        usernameInput.sendKeys(username);
        LogUtil.log("Entered username: " + username, LogUtil.LogLevel.INFO);
    }

    /**
     * Enters the password into the password input field.
     * Logs the action for tracking purposes.
     * @param password The password to enter.
     */
    public void enterPassword(String password) {
        WebElement passwordInput = visibilityWait.waitForElementToBeVisible(LoginPageLocators.getPasswordInputLocator(), 10);
        passwordInput.sendKeys(password);
        LogUtil.log("Entered password: " + password, LogUtil.LogLevel.INFO);
    }

    /**
     * Clicks the login button on the login page.
     * Logs the action for tracking purposes.
     */
    public void clickLoginButton() {
        WebElement loginButton = visibilityWait.waitForElementToBeClickable(LoginPageLocators.getLoginButtonLocator(), 10);
        loginButton.click();
        LogUtil.log("Clicked login button", LogUtil.LogLevel.INFO);
    }

    /**
     * Performs the login action by entering username, entering password, and clicking login.
     * @param username The username to enter.
     * @param password The password to enter.
     */
    public void login(String username, String password) {
        enterUserName(username);
        enterPassword(password);
        clickLoginButton();
    }

    /**
     * Checks if the Forgot Password link is displayed on the login page.
     * @return true if the Forgot Password link is displayed, false otherwise.
     */
    public boolean isForgotPasswordLinkDisplayed() {
        return visibilityWait.isElementDisplayed(LoginPageLocators.getForgotPasswordLinkLocator(), 10);
    }

    /**
     * Clicks the Forgot Password link and starts the password reset process.
     * Logs the action for tracking purposes.
     */
    public void clickForgotPasswordLink() {
        WebElement forgotPasswordLink = visibilityWait.waitForElementToBeClickable(LoginPageLocators.getForgotPasswordLinkLocator(), 10);
        forgotPasswordLink.click();
        LogUtil.log("Clicked forgot password link", LogUtil.LogLevel.INFO);
    }

    /**
     * Performs password reset by clicking the Forgot Password link, entering the username, 
     * and clicking the login button again.
     * @param username The username for resetting the password.
     */
    public void resetPassword(String username) {
        clickForgotPasswordLink();
        enterUserName(username);
        clickLoginButton();
    }

    /**
     * Retrieves the current page URL.
     * Useful for assertions in tests to verify correct page navigation.
     * @return The current URL of the page.
     */
    public String getCurrentPageUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Checks if the Reset Password page title is displayed.
     * @return true if the reset password page title is displayed, false otherwise.
     */
    public boolean isResetPasswordPageTitleDisplayed() {
        return visibilityWait.isElementDisplayed(LoginPageLocators.getResetPasswordTitleLocator(), 10);
    }

    /**
     * Checks if the Reset Password success page title is displayed after resetting password.
     * @return true if the reset password success page title is displayed, false otherwise.
     */
    public boolean isResetPasswordSuccessPageTitleDisplayed() {
        return visibilityWait.isElementDisplayed(LoginPageLocators.getResetPasswordSuccessfullPageTitleLocator(), 10);
    }

    /**
     * Clears the username input field.
     * Logs the action for tracking purposes.
     */
    public void clearUserName() {
        WebElement usernameInput = visibilityWait.waitForElementToBeVisible(LoginPageLocators.getUsernameInputLocator(), 10);
        usernameInput.clear();
        LogUtil.log("Cleared username field", LogUtil.LogLevel.INFO);
    }

    /**
     * Clears the password input field.
     * Logs the action for tracking purposes.
     */
    public void clearPassword() {
        WebElement passwordInput = visibilityWait.waitForElementToBeVisible(LoginPageLocators.getPasswordInputLocator(), 10);
        passwordInput.clear();
        LogUtil.log("Cleared password field", LogUtil.LogLevel.INFO);
    }

    /**
     * Checks if the login error message is displayed after a failed login attempt.
     * @return true if the login error message is displayed, false otherwise.
     */
    public boolean isLoginErrorMessageDisplayed() {
        return visibilityWait.isElementDisplayed(LoginPageLocators.getLoginErrorMessage(), 10);
    }

    /**
     * Retrieves the login error message text displayed on the page.
     * @return The login error message.
     */
    public String getLoginErrorMessage() {
        return textWait.waitForElementVisibility(LoginPageLocators.getLoginErrorMessage()).getText();
    }

    /**
     * Retrieves the error message displayed for the username field.
     * @return The error message for the username field.
     */
    public String getUsernameFieldErrorMessage() {
        WebElement requiredFieldUsername = visibilityWait.waitForElementToBeVisible(LoginPageLocators.getUsernameErrorLocator(), 10);
        return requiredFieldUsername.getText();
    }

    /**
     * Retrieves the error message displayed for the password field.
     * @return The error message for the password field.
     */
    public String getPasswordFieldErrorMessage() {
        WebElement requiredFieldPassword = visibilityWait.waitForElementToBeVisible(LoginPageLocators.getPasswordErrorLocator(), 10);
        return requiredFieldPassword.getText();
    }

    /**
     * Checks if the dashboard title is displayed after a successful login.
     * @return true if the dashboard title is displayed, false otherwise.
     */
    public boolean isDashboardTitleDisplayed() {
        return visibilityWait.isElementDisplayed(LoginPageLocators.getDashboardTitleLocator(), 10);
    }

    /**
     * Checks if the login page title is displayed.
     * @return true if the login page title is displayed, false otherwise.
     */
    public boolean isLoginPageTitleDisplayed() {
        return visibilityWait.isElementDisplayed(LoginPageLocators.getLoginPageTitleLocator(), 10);
    }

    /**
     * Checks if the logout link is displayed after logging in successfully.
     * @return true if the logout link is displayed, false otherwise.
     */
    public boolean isLogoutLinkDisplayed() {
        return visibilityWait.isElementDisplayed(LoginPageLocators.getLogoutLinkLocator(), 10);
    }

    /**
     * Clicks the logout link after clicking the user drop-down menu.
     * Logs the action for tracking purposes.
     */
    public void clickLogoutLink() {
        visibilityWait.waitForElementToBeClickable(LoginPageLocators.getUserDropDownLocator(), 10).click();
        WebElement logoutLink = visibilityWait.waitForElementToBeClickable(LoginPageLocators.getLogoutLinkLocator(), 10);
        logoutLink.click();
        LogUtil.log("Clicked logout link", LogUtil.LogLevel.INFO);
    }
}
