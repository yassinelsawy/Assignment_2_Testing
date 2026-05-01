package com.tutorialsninja.pages;

import com.tutorialsninja.config.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Billing details
    private final By newAddressRadio = By.cssSelector("input[value='new']");
    private final By billingFirstName = By.id("input-payment-firstname");
    private final By billingLastName = By.id("input-payment-lastname");
    private final By billingAddress1 = By.id("input-payment-address-1");
    private final By billingCity = By.id("input-payment-city");
    private final By billingPostCode = By.id("input-payment-postcode");
    private final By billingCountry = By.id("input-payment-country");
    private final By billingZone = By.id("input-payment-zone");
    private final By billingContinueBtn = By.id("button-payment-address");

    // Delivery details
    private final By shippingNewAddressRadio = By.cssSelector("#collapse-shipping-address input[value='new']");
    private final By shippingContinueBtn = By.id("button-shipping-address");

    // Delivery method
    private final By deliveryMethodContinueBtn = By.id("button-shipping-method");
    private final By deliveryComment = By.name("comment");

    // Payment method
    private final By termsCheckbox = By.name("agree");
    private final By paymentMethodContinueBtn = By.id("button-payment-method");

    // Confirm order
    private final By confirmOrderBtn = By.id("button-confirm");
    private final By confirmOrderProducts = By.cssSelector("#collapse-checkout-confirm table tbody tr");

    // Success page
    private final By successHeading = By.cssSelector("#content h1");
    private final By successMessage = By.cssSelector("#content p");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public void selectNewBillingAddress() {
        try {
            WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(newAddressRadio));
            if (!radio.isSelected()) {
                radio.click();
            }
        } catch (TimeoutException e) {
            // No radio button — billing form is already shown for first-time checkout
        }
    }

    public void fillBillingDetails(String firstName, String lastName, String address1,
                                    String city, String postCode, String country, String zone) {
        selectNewBillingAddress();

        WebElement fn = wait.until(ExpectedConditions.visibilityOfElementLocated(billingFirstName));
        fn.clear();
        fn.sendKeys(firstName);

        WebElement ln = driver.findElement(billingLastName);
        ln.clear();
        ln.sendKeys(lastName);

        WebElement addr = driver.findElement(billingAddress1);
        addr.clear();
        addr.sendKeys(address1);

        WebElement cityEl = driver.findElement(billingCity);
        cityEl.clear();
        cityEl.sendKeys(city);

        WebElement postCodeEl = driver.findElement(billingPostCode);
        postCodeEl.clear();
        postCodeEl.sendKeys(postCode);

        Select countrySelect = new Select(driver.findElement(billingCountry));
        countrySelect.selectByVisibleText(country);

        wait.until(ExpectedConditions.elementToBeClickable(billingZone));
        Select zoneSelect = new Select(driver.findElement(billingZone));
        zoneSelect.selectByVisibleText(zone);
    }

    public void clickBillingContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(billingContinueBtn)).click();
    }

    public void clickShippingContinue() {
        try {
            WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(shippingNewAddressRadio));
            if (!radio.isSelected()) {
                radio.click();
            }
        } catch (TimeoutException e) {
            // Use existing address
        }
        wait.until(ExpectedConditions.elementToBeClickable(shippingContinueBtn)).click();
    }

    public void addCommentAndContinueDelivery(String comment) {
        try {
            WebElement commentEl = wait.until(ExpectedConditions.visibilityOfElementLocated(deliveryComment));
            commentEl.sendKeys(comment);
        } catch (TimeoutException e) {
            // Comment field might not be visible
        }
        wait.until(ExpectedConditions.elementToBeClickable(deliveryMethodContinueBtn)).click();
    }

    public void agreeAndContinuePayment() {
        WebElement terms = wait.until(ExpectedConditions.elementToBeClickable(termsCheckbox));
        if (!terms.isSelected()) {
            terms.click();
        }
        wait.until(ExpectedConditions.elementToBeClickable(paymentMethodContinueBtn)).click();
    }

    public void confirmOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmOrderBtn)).click();
    }

    public String getSuccessHeading() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successHeading)).getText();
    }

    public boolean isOrderPlaced() {
        try {
            String heading = getSuccessHeading();
            return heading.contains("Your order has been placed");
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void completeCheckout(String firstName, String lastName, String address1,
                                  String city, String postCode, String country, String zone) {
        fillBillingDetails(firstName, lastName, address1, city, postCode, country, zone);
        clickBillingContinue();
        clickShippingContinue();
        addCommentAndContinueDelivery("");
        agreeAndContinuePayment();
        confirmOrder();
    }
}
