package com.tutorialsninja.pages;

import com.tutorialsninja.config.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AccountPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By myAccountDropdown = By.cssSelector("#top-links .dropdown > a");
    private final By logoutLink        = By.linkText("Logout");

    public AccountPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public boolean isAccountCreated() {
        try {
            By successMsg = By.cssSelector("#content h1");
            String text = wait.until(ExpectedConditions.visibilityOfElementLocated(successMsg)).getText();
            return text.contains("Your Account Has Been Created");
        } catch (TimeoutException e) {
            return false;
        }
    }

    // After successful login, OpenCart redirects away from account/login to account/account
    public boolean isMyAccountPage() {
        try {
            wait.until(d -> !d.getCurrentUrl().contains("account/login"));
            String url = driver.getCurrentUrl();
            return url.contains("account/account") || url.contains("account/success")
                    || url.contains("route=account");
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isLoggedIn() {
        try {
            driver.findElement(myAccountDropdown).click();
            boolean visible = !driver.findElements(logoutLink).isEmpty()
                    && driver.findElement(logoutLink).isDisplayed();
            driver.findElement(By.cssSelector("body")).click();
            return visible;
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
