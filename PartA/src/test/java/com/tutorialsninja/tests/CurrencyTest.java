package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.CategoryPage;
import com.tutorialsninja.pages.LoginPage;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class CurrencyTest extends BaseTest {

    @DataProvider(name = "currencyData")
    public Object[][] getCurrencyData() {
        return ExcelReader.getTestData("CurrencyChange");
    }

    @Test(dataProvider = "currencyData", description = "TC05 - Change currency")
    @Description("Verify that switching currency updates the price symbol on the product listing page")
    public void testChangeCurrency(String email, String password, String currencyCode, String expectedSymbol) {
        LoginPage loginPage = homePage.goToLogin();
        loginPage.loginSuccessfully(email, password);

        CategoryPage desktopsPage = homePage.clickDesktopsShowAll();
        List<String> pricesBeforeChange = desktopsPage.getProductPrices();
        Assert.assertEquals(desktopsPage.getCurrencySymbol(), "$", "Default currency should be $");

        homePage.changeCurrency(currencyCode);

        CategoryPage desktopsAfter = homePage.clickDesktopsShowAll();
        List<String> pricesAfterChange = desktopsAfter.getProductPrices();

        Assert.assertEquals(desktopsAfter.getCurrencySymbol(), expectedSymbol,
                "Currency symbol should change to " + expectedSymbol);
        Assert.assertNotEquals(pricesBeforeChange, pricesAfterChange,
                "Product prices should change after currency switch");

        homePage.logout();
    }
}
