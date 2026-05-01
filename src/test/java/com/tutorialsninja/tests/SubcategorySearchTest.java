package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.LoginPage;
import com.tutorialsninja.pages.SearchPage;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("Search")
@Feature("Subcategory Search")
public class SubcategorySearchTest extends BaseTest {

    @DataProvider(name = "subcategorySearchData")
    public Object[][] getSubcategorySearchData() {
        return ExcelReader.getTestData("SubcategorySearch");
    }

    @Test(dataProvider = "subcategorySearchData", description = "TC09 - Search in subcategories")
    @Story("TC09 - Search in Subcategories")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that searching within subcategories finds products not found in the parent category alone")
    public void testSubcategorySearch(String email, String password, String searchTerm,
                                       String categoryName, String expectedProduct) {
        // Login
        attachLog("Logging in with: " + email);
        LoginPage loginPage = homePage.goToLogin();
        loginPage.loginSuccessfully(email, password);

        // Open search page via top search icon/bar
        attachLog("Navigating to search page and searching for: " + searchTerm);
        SearchPage searchPage = homePage.search(searchTerm);

        // Search within category only (no subcategories)
        attachLog("Searching in category '" + categoryName + "' WITHOUT subcategories");
        searchPage.searchWithSubcategory(searchTerm, categoryName, false);

        Assert.assertTrue(searchPage.isNoResultsMessageDisplayed(),
                "Expected no products when searching '" + searchTerm + "' in '" + categoryName + "' without subcategories");

        attachLog("No results confirmed. Now searching WITH subcategories");

        // Now search with subcategories checked
        searchPage.checkSearchInSubcategories();
        searchPage.clickSearch();

        Assert.assertTrue(searchPage.hasResults(),
                "Expected products when searching with subcategories enabled");

        Assert.assertTrue(searchPage.resultsContainProduct(expectedProduct),
                "Expected to find '" + expectedProduct + "' in subcategory search results");

        attachLog("Found product: " + expectedProduct);

        // Logout
        homePage.logout();
        attachLog("Logged out successfully");
    }
}
