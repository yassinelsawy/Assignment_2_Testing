package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.*;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

    @DataProvider(name = "checkoutData")
    public Object[][] getCheckoutData() {
        return ExcelReader.getTestData("Checkout");
    }

    @Test(dataProvider = "checkoutData", description = "TC11 - Complete checkout process")
    @Description("Verify the full checkout flow: add product, fill billing, confirm order")
    public void testNormalCheckout(String email, String password, String product,
                                    String firstName, String lastName, String address1,
                                    String city, String postCode, String country, String zone) {
        LoginPage loginPage = homePage.goToLogin();
        loginPage.loginSuccessfully(email, password);

        // Add product to cart
        CategoryPage mp3Page = homePage.clickMp3PlayersShowAll();
        ProductPage productPage = mp3Page.clickProduct(product);
        String successMsg = productPage.addToCartAndGetSuccess();
        Assert.assertTrue(successMsg.contains("Success"),
                "Expected success message after adding product. Got: " + successMsg);

        // Verify cart
        CartPage cartPage = homePage.goToCart();
        Assert.assertTrue(cartPage.containsProduct(product),
                "Cart should contain: " + product);

        // Checkout
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        checkoutPage.fillBillingDetails(firstName, lastName, address1, city, postCode, country, zone);
        checkoutPage.clickBillingContinue();
        checkoutPage.clickShippingContinue();
        checkoutPage.addCommentAndContinueDelivery("");
        checkoutPage.agreeAndContinuePayment();
        checkoutPage.confirmOrder();

        Assert.assertTrue(checkoutPage.isOrderPlaced(),
                "Expected order confirmation message");

        String miniCart = homePage.getMiniCartText();
        Assert.assertTrue(miniCart.contains("0 item(s)") || miniCart.contains("0 item"),
                "Cart should be empty after order. Got: " + miniCart);

        homePage.logout();
    }
}
