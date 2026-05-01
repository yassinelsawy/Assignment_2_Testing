package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.LoginPage;
import com.tutorialsninja.pages.SearchPage;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SubcategorySearchTest extends BaseTest {

    @DataProvider(name = "subcategorySearchData")
    public Object[][] getSubcategorySearchData() {
        return ExcelReader.getTestData("SubcategorySearch");
    }

    @Test(dataProvider = "subcategorySearchData", description = "TC09 - Search in subcategories")
    @Description("Verify that enabling subcategory search finds products not found in the parent category alone")
    public void testSubcategorySearch(String email, String password, String searchTerm,
                                       String categoryName, String expectedProduct) {
        LoginPage loginPage = homePage.goToLogin();
        loginPage.loginSuccessfully(email, password);

        SearchPage searchPage = homePage.search(searchTerm);

        searchPage.searchWithSubcategory(searchTerm, categoryName, false);
        Assert.assertTrue(searchPage.isNoResultsMessageDisplayed(),
                "Expected no results for '" + searchTerm + "' in '" + categoryName + "' without subcategories");

        searchPage.checkSearchInSubcategories();
        searchPage.clickSearch();

        Assert.assertTrue(searchPage.hasResults(),
                "Expected results when searching with subcategories enabled");
        Assert.assertTrue(searchPage.resultsContainProduct(expectedProduct),
                "Expected to find '" + expectedProduct + "' in subcategory results");

        homePage.logout();
    }
}
