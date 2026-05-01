package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.CategoryPage;
import com.tutorialsninja.pages.LoginPage;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class SortTest extends BaseTest {

    @DataProvider(name = "sortData")
    public Object[][] getSortData() {
        return ExcelReader.getTestData("SortByName");
    }

    @Test(dataProvider = "sortData", description = "TC07 - Sort products by name")
    @Description("Verify that products are sorted alphabetically A-Z and Z-A")
    public void testSortByName(String email, String password, String category) {
        LoginPage loginPage = homePage.goToLogin();
        loginPage.loginSuccessfully(email, password);

        CategoryPage categoryPage = homePage.clickNavCategory(category);

        categoryPage.sortBy("Name (A - Z)");
        List<String> namesAtoZ = categoryPage.getProductNames();
        List<String> expectedAtoZ = new ArrayList<>(namesAtoZ);
        expectedAtoZ.sort(String.CASE_INSENSITIVE_ORDER);
        Assert.assertEquals(namesAtoZ, expectedAtoZ, "Products should be sorted A to Z");

        categoryPage.sortBy("Name (Z - A)");
        List<String> namesZtoA = categoryPage.getProductNames();
        List<String> expectedZtoA = new ArrayList<>(namesZtoA);
        expectedZtoA.sort(String.CASE_INSENSITIVE_ORDER.reversed());
        Assert.assertEquals(namesZtoA, expectedZtoA, "Products should be sorted Z to A");

        homePage.logout();
    }
}
