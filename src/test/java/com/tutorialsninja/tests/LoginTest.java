package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.AccountPage;
import com.tutorialsninja.pages.LoginPage;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("User Authentication")
@Feature("Login")
public class LoginTest extends BaseTest {

    @DataProvider(name = "validLoginData")
    public Object[][] getValidLoginData() {
        return ExcelReader.getTestData("Login_Valid");
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] getInvalidLoginData() {
        return ExcelReader.getTestData("Login_Invalid");
    }

    @Test(dataProvider = "validLoginData", description = "TC03 - Valid login")
    @Story("TC03 - Valid Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a registered user can log in with valid email and password")
    public void testValidLogin(String email, String password) {
        attachLog("Attempting login with email: " + email);

        LoginPage loginPage = homePage.goToLogin();
        AccountPage accountPage = loginPage.loginSuccessfully(email, password);

        attachLog("Verifying My Account page is opened");
        Assert.assertTrue(accountPage.isMyAccountPage(),
                "Expected to be on My Account page after successful login");

        attachLog("Verifying user is logged in (Logout visible in My Account menu)");
        Assert.assertTrue(accountPage.isLoggedIn(),
                "Logout option not found — user may not be logged in");

        homePage.logout();
        attachLog("Logged out successfully");
    }

    @Test(dataProvider = "invalidLoginData", description = "TC04 - Invalid login")
    @Story("TC04 - Invalid Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that an error message is shown when login is attempted with invalid credentials")
    public void testInvalidLogin(String email, String password) {
        attachLog("Attempting login with invalid credentials. Email: " + email);

        LoginPage loginPage = homePage.goToLogin();
        loginPage.loginWithInvalidCredentials(email, password);

        Assert.assertTrue(loginPage.isWarningDisplayed(),
                "Expected warning message for invalid credentials");

        String warningText = loginPage.getWarningMessage();
        attachLog("Warning message displayed: " + warningText);

        Assert.assertTrue(warningText.contains("No match for E-Mail Address and/or Password"),
                "Expected 'No match for E-Mail Address and/or Password.' error. Got: " + warningText);
    }
}
