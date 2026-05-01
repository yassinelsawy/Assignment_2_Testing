package com.tutorialsninja.pages;

import com.tutorialsninja.config.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class SearchPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By searchInput         = By.id("input-search");
    // OpenCart uses name="category_id" — no guaranteed id attribute on this select
    private final By categorySelect      = By.cssSelector("select[name='category_id']");
    private final By subcategoryCheckbox = By.name("sub_category");
    private final By searchButton        = By.id("button-search");
    private final By productNameLinks    = By.cssSelector(".product-layout .caption h4 a");
    private final By noResultsMessage    = By.cssSelector("#content p");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public void enterSearchTerm(String keyword) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys(keyword);
    }

    public void selectCategory(String categoryName) {
        Select select = new Select(wait.until(ExpectedConditions.elementToBeClickable(categorySelect)));
        select.selectByVisibleText(categoryName);
    }

    public void checkSearchInSubcategories() {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(subcategoryCheckbox));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    public void searchWithSubcategory(String keyword, String category, boolean includeSubcategories) {
        enterSearchTerm(keyword);
        selectCategory(category);
        if (includeSubcategories) {
            checkSearchInSubcategories();
        }
        clickSearch();
    }

    public List<String> getSearchResults() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productNameLinks));
            return driver.findElements(productNameLinks)
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());
        } catch (TimeoutException e) {
            return List.of();
        }
    }

    public boolean hasResults() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(productNameLinks));
            return !driver.findElements(productNameLinks).isEmpty();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getNoResultsMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(noResultsMessage)).getText();
    }

    public boolean isNoResultsMessageDisplayed() {
        try {
            String msg = wait.until(ExpectedConditions.visibilityOfElementLocated(noResultsMessage)).getText();
            return msg.contains("There is no product that matches");
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean allResultsContain(String keyword) {
        List<String> results = getSearchResults();
        if (results.isEmpty()) return false;
        return results.stream().allMatch(name -> name.toLowerCase().contains(keyword.toLowerCase()));
    }

    public boolean resultsContainProduct(String productName) {
        List<String> results = getSearchResults();
        return results.stream().anyMatch(name -> name.toLowerCase().contains(productName.toLowerCase()));
    }
}
