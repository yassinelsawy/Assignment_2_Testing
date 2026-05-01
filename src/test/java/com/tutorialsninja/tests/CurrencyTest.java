package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.CategoryPage;
import com.tutorialsninja.pages.LoginPage;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Currency")
@Feature("Currency Change")
public class CurrencyTest extends BaseTest {

    @DataProvider(name = "currencyData")
    public Object[][] getCurrencyData() {
        return ExcelReader.getTestData("CurrencyChange");
    }

    @Test(dataProvider = "currencyData", description = "TC05 - Change currency")
    @Story("TC05 - Change Currency")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that currency can be changed and product prices update accordingly")
    public void testChangeCurrency(String email, String password, String currencyCode, String expectedSymbol) {
        // Login
        attachLog("Logging in with: " + email);
        LoginPage loginPage = homePage.goToLogin();
        loginPage.loginSuccessfully(email, password);

        // Navigate to Desktops > Show All Desktops
        attachLog("Navigating to Desktops > Show All Desktops");
        CategoryPage desktopsPage = homePage.clickDesktopsShowAll();

        // Capture prices in default USD
        List<String> pricesInUSD = desktopsPage.getProductPrices();
        attachLog("Prices in USD: " + pricesInUSD);

        String defaultSymbol = desktopsPage.getCurrencySymbol();
        attachLog("Default currency symbol: " + defaultSymbol);
        Assert.assertEquals(defaultSymbol, "$", "Default currency should be $");

        // Change currency to Euro
        attachLog("Changing currency to: " + currencyCode);
        homePage.changeCurrency(currencyCode);

        // Re-navigate to Desktops after currency change
        CategoryPage desktopsAfterChange = homePage.clickDesktopsShowAll();
        List<String> pricesInEUR = desktopsAfterChange.getProductPrices();
        attachLog("Prices after currency change: " + pricesInEUR);

        String newSymbol = desktopsAfterChange.getCurrencySymbol();
        attachLog("New currency symbol: " + newSymbol);
        Assert.assertEquals(newSymbol, expectedSymbol,
                "Currency symbol should be " + expectedSymbol + " after change");

        Assert.assertNotEquals(pricesInUSD, pricesInEUR,
                "Prices should change after currency switch");

        // Logout
        homePage.logout();
        attachLog("Logged out successfully");
    }
}
