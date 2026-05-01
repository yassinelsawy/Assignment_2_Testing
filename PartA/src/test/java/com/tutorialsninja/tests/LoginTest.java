package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.AccountPage;
import com.tutorialsninja.pages.LoginPage;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @DataProvider(name = "validLoginData")
    public Object[][] getValidData() {
        return ExcelReader.getTestData("Login_Valid");
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] getInvalidData() {
        return ExcelReader.getTestData("Login_Invalid");
    }

    @Test(dataProvider = "validLoginData", description = "TC03 - Valid login")
    @Description("Verify that a registered user can log in with valid credentials")
    public void testValidLogin(String email, String password) {
        LoginPage loginPage = homePage.goToLogin();
        AccountPage accountPage = loginPage.loginSuccessfully(email, password);

        Assert.assertTrue(accountPage.isMyAccountPage(),
                "Expected to land on My Account page after login");
        Assert.assertTrue(accountPage.isLoggedIn(),
                "Logout link should be visible after login");

        homePage.logout();
    }

    @Test(dataProvider = "invalidLoginData", description = "TC04 - Invalid login")
    @Description("Verify that an error message is shown for invalid credentials")
    public void testInvalidLogin(String email, String password) {
        LoginPage loginPage = homePage.goToLogin();
        loginPage.loginWithInvalidCredentials(email, password);

        Assert.assertTrue(loginPage.isWarningDisplayed(),
                "Expected a warning message for invalid credentials");
        Assert.assertTrue(loginPage.getWarningMessage().contains("No match for E-Mail Address and/or Password"),
                "Warning message text is incorrect");
    }
}
