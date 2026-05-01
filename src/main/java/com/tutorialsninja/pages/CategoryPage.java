package com.tutorialsninja.pages;

import com.tutorialsninja.config.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By breadcrumbLast = By.cssSelector("ul.breadcrumb li:last-child");
    private final By sortSelect = By.id("input-sort");
    private final By productNameLinks = By.cssSelector(".product-layout .caption h4 a");
    private final By sidebarLinks = By.cssSelector("#column-left .list-group a");
    private final By activeSidebarLink = By.cssSelector("#column-left .list-group-item.active");
    private final By productPrices = By.cssSelector(".product-layout .price");
    private final By categoryHeading = By.cssSelector("#content h2");

    public CategoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public String getBreadcrumbText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(breadcrumbLast)).getText().trim();
    }

    public String getActiveSidebarItem() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(activeSidebarLink)).getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }

    public void sortBy(String optionText) {
        Select select = new Select(wait.until(ExpectedConditions.elementToBeClickable(sortSelect)));
        select.selectByVisibleText(optionText);
        // Wait for product list to refresh after sort
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productNameLinks));
    }

    public List<String> getProductNames() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productNameLinks));
        return driver.findElements(productNameLinks)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public List<String> getProductPrices() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productPrices));
        return driver.findElements(productPrices)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public ProductPage clickProduct(String productName) {
        By productLink = By.linkText(productName);
        wait.until(ExpectedConditions.elementToBeClickable(productLink)).click();
        return new ProductPage(driver);
    }

    public String getCategoryHeading() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(categoryHeading)).getText();
    }

    public boolean isSidebarLinkActive(String linkText) {
        try {
            String activeText = getActiveSidebarItem();
            return activeText.equalsIgnoreCase(linkText);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSidebarLinkPresent(String linkText) {
        try {
            List<WebElement> links = driver.findElements(sidebarLinks);
            return links.stream().anyMatch(l -> l.getText().trim().equalsIgnoreCase(linkText));
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrencySymbol() {
        List<String> prices = getProductPrices();
        if (!prices.isEmpty()) {
            String price = prices.get(0);
            if (price.contains("Ex Tax:")) {
                price = price.split("\n")[0];
            }
            return price.substring(0, 1);
        }
        return "";
    }
}
