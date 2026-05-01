package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.AccountPage;
import com.tutorialsninja.pages.RegisterPage;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RegistrationTest extends BaseTest {

    @DataProvider(name = "registrationValidData")
    public Object[][] getValidData() {
        return ExcelReader.getTestData("Registration_Valid");
    }

    @DataProvider(name = "registrationInvalidData")
    public Object[][] getInvalidData() {
        return ExcelReader.getTestData("Registration_Invalid");
    }

    @Test(dataProvider = "registrationValidData", description = "TC01 - Valid registration")
    @Description("Verify that a user can register successfully with valid data")
    public void testValidRegistration(String firstName, String lastName, String emailPrefix,
                                      String telephone, String password, String confirmPassword) {
        String email = emailPrefix + System.currentTimeMillis() + "@test.com";

        RegisterPage registerPage = homePage.goToRegister();
        AccountPage accountPage = registerPage.registerSuccessfully(
                firstName, lastName, email, telephone, password, confirmPassword);

        Assert.assertTrue(accountPage.isAccountCreated(),
                "Account creation success message not displayed");
        Assert.assertTrue(accountPage.isLoggedIn(),
                "User should be logged in after registration");

        homePage.logout();
    }

    @Test(dataProvider = "registrationInvalidData", description = "TC02 - Registration validation errors")
    @Description("Verify that error messages appear when required fields are missing or password is too short")
    public void testRegistrationWithErrors(String firstName, String lastName, String shortPassword) {
        RegisterPage registerPage = homePage.goToRegister();

        registerPage.registerWithPartialData(firstName, lastName);
        Assert.assertTrue(registerPage.hasFieldErrors(),
                "Expected validation errors when required fields are empty");

        String email = firstName.toLowerCase() + System.currentTimeMillis() + "@test.com";
        registerPage.registerWithShortPassword(firstName, lastName, email, "0123456789",
                shortPassword, shortPassword);
        Assert.assertTrue(registerPage.isPasswordErrorVisible(),
                "Expected password length error message");
    }
}
