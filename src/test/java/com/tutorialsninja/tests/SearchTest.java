package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.LoginPage;
import com.tutorialsninja.pages.SearchPage;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Search")
@Feature("Search By Name")
public class SearchTest extends BaseTest {

    @DataProvider(name = "searchData")
    public Object[][] getSearchData() {
        return ExcelReader.getTestData("SearchByName");
    }

    @Test(dataProvider = "searchData", description = "TC08 - Search by name")
    @Story("TC08 - Search By Name")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that searching for a product name returns all matching results containing the search term")
    public void testSearchByName(String email, String password, String searchTerm) {
        // Login
        attachLog("Logging in with: " + email);
        LoginPage loginPage = homePage.goToLogin();
        loginPage.loginSuccessfully(email, password);

        // Search using the top search bar
        attachLog("Searching for: " + searchTerm);
        SearchPage searchPage = homePage.search(searchTerm);

        // Verify results
        Assert.assertTrue(searchPage.hasResults(),
                "Expected search results for term: " + searchTerm);

        List<String> results = searchPage.getSearchResults();
        attachLog("Search results: " + results);

        Assert.assertTrue(searchPage.allResultsContain(searchTerm),
                "All product names should contain the search term '" + searchTerm + "'. Results: " + results);

        // Logout
        homePage.logout();
        attachLog("Logged out successfully");
    }
}
