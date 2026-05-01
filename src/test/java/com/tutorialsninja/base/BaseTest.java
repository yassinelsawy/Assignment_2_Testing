package com.tutorialsninja.base;

import com.tutorialsninja.config.ConfigReader;
import com.tutorialsninja.driver.DriverManager;
import com.tutorialsninja.pages.HomePage;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected WebDriver driver;
    protected HomePage homePage;

    @BeforeMethod
    public void setUp() {
        DriverManager.initDriver();
        driver = DriverManager.getDriver();
        driver.get(ConfigReader.getUrl());
        homePage = new HomePage(driver);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshot(result.getName());
        }
        DriverManager.quitDriver();
    }

    @Attachment(value = "Screenshot on failure: {testName}", type = "image/png")
    @Step("Capturing screenshot for: {testName}")
    public byte[] captureScreenshot(String testName) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "{message}", type = "text/plain")
    public String attachLog(String message) {
        return message;
    }
}
