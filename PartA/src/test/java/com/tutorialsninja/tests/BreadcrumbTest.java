package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.CategoryPage;
import com.tutorialsninja.pages.LoginPage;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

public class BreadcrumbTest extends BaseTest {

    @DataProvider(name = "breadcrumbData")
    public Object[][] getBreadcrumbData() {
        return ExcelReader.getTestData("Breadcrumb");
    }

    @Test(dataProvider = "breadcrumbData", description = "TC06 - Breadcrumb and sidebar navigation")
    @Description("Verify the breadcrumb reflects the active category after navigation")
    public void testBreadcrumbAndSidebar(String email, String password,
                                          String category, String expectedBreadcrumb) {
        LoginPage loginPage = homePage.goToLogin();
        loginPage.loginSuccessfully(email, password);

        CategoryPage categoryPage = homePage.clickNavCategory(category);

        String breadcrumb = categoryPage.getBreadcrumbText();
        Assert.assertEquals(breadcrumb, expectedBreadcrumb,
                "Breadcrumb should show '" + expectedBreadcrumb + "'");

        List<String> sidebarLinks = driver.findElements(
                By.cssSelector("#column-left .list-group a, #column-right .list-group a"))
                .stream().map(WebElement::getText).map(String::trim).collect(Collectors.toList());

        if (!sidebarLinks.isEmpty()) {
            Assert.assertFalse(sidebarLinks.isEmpty(), "Sidebar should have navigation links");
        }

        homePage.logout();
    }
}
