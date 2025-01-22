package com.orangehrm.testng.page.locators;

import org.openqa.selenium.By;

/**
 * This class contains the locators for the Login Page elements.
 * It encapsulates the locators and provides getter methods to access them.
 * 
 * Notes:
 * - The locators are encapsulated as private static final variables to follow the Encapsulation principle.
 * - Getter methods are provided to access these locators, ensuring that the way they are used can be controlled in the future.
 * - This class is intended to be used in test cases and page object models for locating elements on the Login page of the OrangeHRM application.
 * Usage: This class should be used within a Page Object Model (POM) framework, where locators for various elements are kept in separate classes (like this one) for each page of the application. 
 * This allows for better maintainability, reusability, and scalability of the test framework.
 * Scalability: As you add more pages to your application, you can add more classes for each page. 
 * For example, you can create a DashboardPageLocators.java file for all locators related to the dashboard page, ensuring that the code remains modular and easier to maintain.
 */
public class LoginPageLocators {

    // Private static final variables to store the locators
    private static final By usernameInputLocator = By.name("username");
    private static final By passwordInputLocator = By.name("password");
    private static final By loginButtonLocator = By.xpath("//button[@type='submit']");
    private static final By forgotPasswordLinkLocator = By.xpath("//p[@class='oxd-text oxd-text--p orangehrm-login-forgot-header']");
    private static final By logoutLinkLocator = By.xpath("//a[normalize-space()='Logout']");
    private static final By resetPasswordTitleLocator = By.xpath("//h6[@class='oxd-text oxd-text--h6 orangehrm-forgot-password-title']");
    private static final By resetPasswordSuccessfullPageTitleLocator = By.xpath("//h6[@class='oxd-text oxd-text--h6 orangehrm-forgot-password-title']");
    private static final By loginErrorMessage = By.xpath("//p[@class='oxd-text oxd-text--p oxd-alert-content-text']");
    private static final By dashboardTitleLocator = By.xpath("//h6[@class='oxd-text oxd-text--h6 oxd-topbar-header-breadcrumb-module']");
    private static final By loginPageTitleLocator = By.xpath("//h5[@class='oxd-text oxd-text--h5 orangehrm-login-title']");
    private static final By userDropDownLocator = By.xpath("//i[@class='oxd-icon bi-caret-down-fill oxd-userdropdown-icon']");
    private static final By usernameErrorLocator = By.xpath("//label[text()='Username']/ancestor::div[contains(@class,'oxd-input-group')]/following-sibling::span[contains(@class,'oxd-input-field-error-message')]");
    private static final By passwordErrorLocator = By.xpath("//label[text()='Password']/ancestor::div[contains(@class,'oxd-input-group')]/following-sibling::span[contains(@class,'oxd-input-field-error-message')]");

    /**
     * Getter method for the username input field locator.
     * @return By locator for the username input field.
     */
    public static By getUsernameInputLocator() {
        return usernameInputLocator;
    }

    /**
     * Getter method for the password input field locator.
     * @return By locator for the password input field.
     */
    public static By getPasswordInputLocator() {
        return passwordInputLocator;
    }

    /**
     * Getter method for the login button locator.
     * @return By locator for the login button.
     */
    public static By getLoginButtonLocator() {
        return loginButtonLocator;
    }

    /**
     * Getter method for the forgot password link locator.
     * @return By locator for the forgot password link.
     */
    public static By getForgotPasswordLinkLocator() {
        return forgotPasswordLinkLocator;
    }

    /**
     * Getter method for the logout link locator.
     * @return By locator for the logout link.
     */
    public static By getLogoutLinkLocator() {
        return logoutLinkLocator;
    }

    /**
     * Getter method for the reset password title locator.
     * @return By locator for the reset password title.
     */
    public static By getResetPasswordTitleLocator() {
        return resetPasswordTitleLocator;
    }

    /**
     * Getter method for the reset password successful page title locator.
     * @return By locator for the reset password successful page title.
     */
    public static By getResetPasswordSuccessfullPageTitleLocator() {
        return resetPasswordSuccessfullPageTitleLocator;
    }

    /**
     * Getter method for the login error message locator.
     * @return By locator for the login error message.
     */
    public static By getLoginErrorMessage() {
        return loginErrorMessage;
    }

    /**
     * Getter method for the dashboard title locator.
     * @return By locator for the dashboard title.
     */
    public static By getDashboardTitleLocator() {
        return dashboardTitleLocator;
    }

    /**
     * Getter method for the login page title locator.
     * @return By locator for the login page title.
     */
    public static By getLoginPageTitleLocator() {
        return loginPageTitleLocator;
    }

    /**
     * Getter method for the user drop-down locator.
     * @return By locator for the user drop-down.
     */
    public static By getUserDropDownLocator() {
        return userDropDownLocator;
    }

    /**
     * Getter method for the username error locator.
     * @return By locator for the username error message.
     */
    public static By getUsernameErrorLocator() {
        return usernameErrorLocator;
    }

    /**
     * Getter method for the password error locator.
     * @return By locator for the password error message.
     */
    public static By getPasswordErrorLocator() {
        return passwordErrorLocator;
    }
}
