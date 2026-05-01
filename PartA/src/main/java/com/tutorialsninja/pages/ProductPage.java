package com.tutorialsninja.pages;

import com.tutorialsninja.config.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By addToCartButton = By.id("button-cart");
    private final By quantityInput = By.id("input-quantity");
    private final By successAlert = By.cssSelector(".alert-success");
    private final By productName = By.cssSelector("#content h1");
    private final By productPrice = By.cssSelector("#content li h2");
    private final By dateInput = By.cssSelector("input[type='date']");
    private final By viewCartLink = By.cssSelector(".alert-success a[href*='cart']");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public void setQuantity(int quantity) {
        WebElement qty = wait.until(ExpectedConditions.visibilityOfElementLocated(quantityInput));
        qty.clear();
        qty.sendKeys(String.valueOf(quantity));
    }

    public void setDeliveryDate(String date) {
        try {
            WebElement dateField = wait.until(ExpectedConditions.visibilityOfElementLocated(dateInput));
            dateField.clear();
            dateField.sendKeys(date);
        } catch (TimeoutException e) {
            // No delivery date field on this product
        }
    }

    public void clickAddToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
    }

    public String addToCartAndGetSuccess() {
        clickAddToCart();
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert)).getText();
    }

    public String getSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert)).getText();
    }

    public boolean isSuccessAlertVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getProductName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productName)).getText();
    }

    public String getProductPrice() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productPrice)).getText();
    }

    public boolean hasDeliveryDateField() {
        try {
            driver.findElement(dateInput);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
