package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.CategoryPage;
import com.tutorialsninja.pages.LoginPage;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@Epic("Product Sorting")
@Feature("Sort By Name")
public class SortTest extends BaseTest {

    @DataProvider(name = "sortData")
    public Object[][] getSortData() {
        return ExcelReader.getTestData("SortByName");
    }

    @Test(dataProvider = "sortData", description = "TC07 - Sort by name")
    @Story("TC07 - Sort By Name")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that products are sorted alphabetically A-Z and Z-A on sort selection")
    public void testSortByName(String email, String password, String category) {
        attachLog("Logging in with: " + email);
        LoginPage loginPage = homePage.goToLogin();
        loginPage.loginSuccessfully(email, password);

        attachLog("Navigating to category: " + category);
        CategoryPage categoryPage = homePage.clickNavCategory(category);

        // Sort A to Z
        attachLog("Sorting by Name A-Z");
        categoryPage.sortBy("Name (A - Z)");

        List<String> namesAtoZ = categoryPage.getProductNames();
        attachLog("Products A-Z: " + namesAtoZ);

        // Use case-insensitive order — the site sorts case-insensitively (iPhone before Palm)
        List<String> expectedAtoZ = new ArrayList<>(namesAtoZ);
        expectedAtoZ.sort(String.CASE_INSENSITIVE_ORDER);

        Assert.assertEquals(namesAtoZ, expectedAtoZ,
                "Products should be sorted case-insensitively A to Z");

        // Sort Z to A
        attachLog("Sorting by Name Z-A");
        categoryPage.sortBy("Name (Z - A)");

        List<String> namesZtoA = categoryPage.getProductNames();
        attachLog("Products Z-A: " + namesZtoA);

        List<String> expectedZtoA = new ArrayList<>(namesZtoA);
        expectedZtoA.sort(String.CASE_INSENSITIVE_ORDER.reversed());

        Assert.assertEquals(namesZtoA, expectedZtoA,
                "Products should be sorted case-insensitively Z to A");

        homePage.logout();
        attachLog("Logged out successfully");
    }
}
