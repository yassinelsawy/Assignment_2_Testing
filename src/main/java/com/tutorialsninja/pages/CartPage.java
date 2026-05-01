package com.tutorialsninja.pages;

import com.tutorialsninja.config.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // td.text-left IS the product cell; the product name link is inside it
    private final By productNameLinks = By.cssSelector("#content .table tbody tr td.text-left a");
    private final By totalRow         = By.cssSelector("#content .table tfoot tr:last-child td:last-child");
    private final By checkoutButton   = By.linkText("Checkout");
    private final By cartHeading      = By.cssSelector("#content h1");
    private final By emptyCartMsg     = By.cssSelector("#content p");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public List<String> getCartProductNames() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productNameLinks));
        return driver.findElements(productNameLinks)
                .stream()
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public String getCartTotal() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(totalRow)).getText().trim();
    }

    public boolean containsProduct(String productName) {
        List<String> names = getCartProductNames();
        return names.stream().anyMatch(n -> n.contains(productName));
    }

    public CheckoutPage proceedToCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        return new CheckoutPage(driver);
    }

    public String getHeading() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cartHeading)).getText();
    }

    public boolean isCartEmpty() {
        try {
            String msg = driver.findElement(emptyCartMsg).getText();
            return msg.contains("Your shopping cart is empty");
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
