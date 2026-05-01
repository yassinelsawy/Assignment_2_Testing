package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.AccountPage;
import com.tutorialsninja.pages.RegisterPage;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("User Registration")
@Feature("Registration")
public class RegistrationTest extends BaseTest {

    @DataProvider(name = "registrationValidData")
    public Object[][] getRegistrationValidData() {
        return ExcelReader.getTestData("Registration_Valid");
    }

    @DataProvider(name = "registrationInvalidData")
    public Object[][] getRegistrationInvalidData() {
        return ExcelReader.getTestData("Registration_Invalid");
    }

    @Test(dataProvider = "registrationValidData", description = "TC01 - Registration without errors")
    @Story("TC01 - Registration without errors")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a user can successfully register with all valid required data")
    public void testRegistrationWithoutErrors(String firstName, String lastName, String emailPrefix,
                                               String telephone, String password, String confirmPassword) {
        String uniqueEmail = emailPrefix + System.currentTimeMillis() + "@test.com";
        attachLog("Registering with email: " + uniqueEmail);

        RegisterPage registerPage = homePage.goToRegister();

        AccountPage accountPage = registerPage.registerSuccessfully(
                firstName, lastName, uniqueEmail, telephone, password, confirmPassword
        );

        attachLog("Checking account creation success message");
        Assert.assertTrue(accountPage.isAccountCreated(),
                "Account creation success message not displayed");

        attachLog("Checking logout is available in My Account menu");
        Assert.assertTrue(accountPage.isLoggedIn(),
                "Logout option not found in My Account menu — user may not be logged in");

        homePage.logout();
        attachLog("Logged out successfully");
    }

    @Test(dataProvider = "registrationInvalidData", description = "TC02 - Registration with errors")
    @Story("TC02 - Registration with errors")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that appropriate error messages are shown when registration form is submitted with missing or invalid data")
    public void testRegistrationWithErrors(String firstName, String lastName, String shortPassword) {
        RegisterPage registerPage = homePage.goToRegister();

        // Step 1: Submit with only first name and last name filled
        attachLog("Submitting form with only firstName and lastName");
        registerPage.registerWithPartialData(firstName, lastName);

        Assert.assertTrue(registerPage.hasFieldErrors(),
                "Expected field validation errors when required fields are missing");

        attachLog("Field errors displayed: " + registerPage.getFieldErrors());

        // Step 2: Fill remaining fields but with short password (< 4 chars)
        String uniqueEmail = firstName.toLowerCase() + System.currentTimeMillis() + "@test.com";
        attachLog("Submitting with short password: " + shortPassword);
        registerPage.registerWithShortPassword(
                firstName, lastName, uniqueEmail, "0123456789", shortPassword, shortPassword
        );

        Assert.assertTrue(registerPage.isPasswordErrorVisible(),
                "Expected 'Password must be between 4 and 20 characters!' error message");

        attachLog("Password error message verified successfully");
    }
}
