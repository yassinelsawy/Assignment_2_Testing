package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.LoginPage;
import com.tutorialsninja.pages.SearchPage;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SearchTest extends BaseTest {

    @DataProvider(name = "searchData")
    public Object[][] getSearchData() {
        return ExcelReader.getTestData("SearchByName");
    }

    @Test(dataProvider = "searchData", description = "TC08 - Search by name")
    @Description("Verify that searching returns results matching the search term")
    public void testSearchByName(String email, String password, String searchTerm) {
        LoginPage loginPage = homePage.goToLogin();
        loginPage.loginSuccessfully(email, password);

        SearchPage searchPage = homePage.search(searchTerm);

        Assert.assertTrue(searchPage.hasResults(),
                "Search should return results for: " + searchTerm);
        Assert.assertTrue(searchPage.allResultsContain(searchTerm),
                "All results should contain the search term: " + searchTerm);

        homePage.logout();
    }
}
