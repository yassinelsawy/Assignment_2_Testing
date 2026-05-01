package com.tutorialsninja.pages;

import com.tutorialsninja.config.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class RegisterPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By firstNameInput       = By.id("input-firstname");
    private final By lastNameInput        = By.id("input-lastname");
    private final By emailInput           = By.id("input-email");
    private final By telephoneInput       = By.id("input-telephone");
    private final By passwordInput        = By.id("input-password");
    private final By confirmPasswordInput = By.id("input-confirm");
    private final By agreeCheckbox        = By.name("agree");
    private final By continueButton       = By.cssSelector("input[type='submit']");
    private final By warningAlert         = By.cssSelector(".alert-danger");
    private final By fieldErrors          = By.cssSelector(".text-danger");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public void enterFirstName(String firstName) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput));
        el.clear();
        el.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        WebElement el = driver.findElement(lastNameInput);
        el.clear();
        el.sendKeys(lastName);
    }

    public void enterEmail(String email) {
        WebElement el = driver.findElement(emailInput);
        el.clear();
        el.sendKeys(email);
    }

    public void enterTelephone(String telephone) {
        WebElement el = driver.findElement(telephoneInput);
        el.clear();
        el.sendKeys(telephone);
    }

    public void enterPassword(String password) {
        WebElement el = driver.findElement(passwordInput);
        el.clear();
        el.sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        WebElement el = driver.findElement(confirmPasswordInput);
        el.clear();
        el.sendKeys(confirmPassword);
    }

    // JS click bypasses any overlapping element (sticky headers, cookie banners, etc.)
    public void acceptPrivacyPolicy() {
        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(agreeCheckbox));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", checkbox);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
    }

    public void clickContinue() {
        driver.findElement(continueButton).click();
    }

    public AccountPage registerSuccessfully(String firstName, String lastName, String email,
                                             String telephone, String password, String confirmPassword) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterTelephone(telephone);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        acceptPrivacyPolicy();
        clickContinue();
        return new AccountPage(driver);
    }

    public void registerWithPartialData(String firstName, String lastName) {
        enterFirstName(firstName);
        enterLastName(lastName);
        clickContinue();
    }

    public void registerWithShortPassword(String firstName, String lastName, String email,
                                           String telephone, String password, String confirmPassword) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterTelephone(telephone);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        acceptPrivacyPolicy();
        clickContinue();
    }

    public String getWarningMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(warningAlert)).getText();
    }

    public List<String> getFieldErrors() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(fieldErrors));
        return driver.findElements(fieldErrors)
                .stream()
                .map(WebElement::getText)
                .filter(t -> !t.isBlank())
                .collect(Collectors.toList());
    }

    public boolean hasFieldErrors() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(fieldErrors));
            return !driver.findElements(fieldErrors).isEmpty();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isPasswordErrorVisible() {
        List<String> errors = getFieldErrors();
        return errors.stream().anyMatch(e -> e.contains("Password must be between 4 and 20 characters"));
    }
}
