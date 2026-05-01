package com.tutorialsninja.pages;

import com.tutorialsninja.config.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By emailInput = By.id("input-email");
    private final By passwordInput = By.id("input-password");
    private final By loginButton = By.cssSelector("input[type='submit']");
    private final By warningAlert = By.cssSelector(".alert-danger");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public void enterEmail(String email) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        el.clear();
        el.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement el = driver.findElement(passwordInput);
        el.clear();
        el.sendKeys(password);
    }

    public AccountPage loginSuccessfully(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        driver.findElement(loginButton).click();
        return new AccountPage(driver);
    }

    public void loginWithInvalidCredentials(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        driver.findElement(loginButton).click();
    }

    public String getWarningMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(warningAlert)).getText();
    }

    public boolean isWarningDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(warningAlert));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
