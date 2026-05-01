package com.tutorialsninja.pages;

import com.tutorialsninja.config.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;

    private final By myAccountDropdown = By.cssSelector("#top-links .dropdown > a");
    private final By registerLink     = By.linkText("Register");
    private final By loginLink        = By.linkText("Login");
    private final By logoutLink       = By.linkText("Logout");
    private final By currencyButton   = By.cssSelector("#form-currency button");
    private final By searchBox        = By.name("search");
    private final By searchButton     = By.cssSelector(".btn.btn-default.btn-lg");
    private final By cartButton       = By.id("cart");
    private final By viewCartLink     = By.linkText("View Cart");
    private final By checkoutLink     = By.linkText("Checkout");

    public HomePage(WebDriver driver) {
        this.driver  = driver;
        this.wait    = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        this.actions = new Actions(driver);
    }

    public void navigateToHome() {
        driver.get(ConfigReader.getUrl());
    }

    public RegisterPage goToRegister() {
        wait.until(ExpectedConditions.elementToBeClickable(myAccountDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(registerLink)).click();
        return new RegisterPage(driver);
    }

    public LoginPage goToLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(myAccountDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
        return new LoginPage(driver);
    }

    public void logout() {
        WebElement accountBtn = wait.until(ExpectedConditions.elementToBeClickable(myAccountDropdown));
        actions.moveToElement(accountBtn).click().perform();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }

    public boolean isLogoutVisible() {
        try {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(myAccountDropdown));
            actions.moveToElement(btn).click().perform();
            return !driver.findElements(logoutLink).isEmpty()
                    && driver.findElement(logoutLink).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void changeCurrency(String currencyName) {
        wait.until(ExpectedConditions.elementToBeClickable(currencyButton)).click();
        By currencyOption = By.name(currencyName);
        wait.until(ExpectedConditions.elementToBeClickable(currencyOption)).click();
    }

    public CategoryPage clickNavCategory(String categoryText) {
        By navLink = By.linkText(categoryText);
        wait.until(ExpectedConditions.elementToBeClickable(navLink)).click();
        return new CategoryPage(driver);
    }

    // Hover opens the CSS dropdown; then click "Show All Desktops"
    public CategoryPage clickDesktopsShowAll() {
        WebElement desktops = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Desktops")));
        actions.moveToElement(desktops).perform();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Show All Desktops"))).click();
        return new CategoryPage(driver);
    }

    // Hover opens the CSS dropdown; then click "Show All MP3 Players"
    public CategoryPage clickMp3PlayersShowAll() {
        WebElement mp3 = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("MP3 Players")));
        actions.moveToElement(mp3).perform();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Show All MP3 Players"))).click();
        return new CategoryPage(driver);
    }

    // Hover opens the CSS dropdown; then click "Show All Laptops & Notebooks"
    public CategoryPage clickLaptopsShowAll() {
        WebElement laptops = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Laptops & Notebooks")));
        actions.moveToElement(laptops).perform();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Show All Laptops & Notebooks"))).click();
        return new CategoryPage(driver);
    }

    public SearchPage search(String keyword) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchBox));
        input.clear();
        input.sendKeys(keyword);
        driver.findElement(searchButton).click();
        return new SearchPage(driver);
    }

    public CartPage goToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(viewCartLink)).click();
        return new CartPage(driver);
    }

    public CheckoutPage goToCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(cartButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(checkoutLink)).click();
        return new CheckoutPage(driver);
    }

    public String getMiniCartText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cartButton)).getText();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
