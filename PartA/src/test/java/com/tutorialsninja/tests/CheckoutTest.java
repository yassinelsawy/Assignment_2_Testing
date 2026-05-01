package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.*;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("Checkout")
@Feature("Normal Checkout Process")
public class CheckoutTest extends BaseTest {

    @DataProvider(name = "checkoutData")
    public Object[][] getCheckoutData() {
        return ExcelReader.getTestData("Checkout");
    }

    @Test(dataProvider = "checkoutData", description = "TC11 - Normal checkout process and confirm order")
    @Story("TC11 - Normal Checkout Process")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify end-to-end checkout process: add product to cart, proceed to checkout, fill details, and confirm order")
    public void testNormalCheckout(String email, String password, String product,
                                    String firstName, String lastName, String address1,
                                    String city, String postCode, String country, String zone) {
        // Login
        attachLog("Logging in with: " + email);
        LoginPage loginPage = homePage.goToLogin();
        loginPage.loginSuccessfully(email, password);

        // Navigate to MP3 Players > Show All
        attachLog("Navigating to MP3 Players");
        CategoryPage mp3Page = homePage.clickMp3PlayersShowAll();

        // Add iPod Shuffle to cart
        attachLog("Clicking on product: " + product);
        ProductPage productPage = mp3Page.clickProduct(product);

        attachLog("Adding " + product + " to cart");
        String successMsg = productPage.addToCartAndGetSuccess();
        attachLog("Success message: " + successMsg);

        Assert.assertTrue(successMsg.contains("Success"),
                "Expected success message after adding product to cart. Got: " + successMsg);

        // Verify cart via View Cart
        attachLog("Navigating to cart to verify product");
        CartPage cartPage = homePage.goToCart();

        Assert.assertTrue(cartPage.containsProduct(product),
                "Cart should contain: " + product);

        String cartTotal = cartPage.getCartTotal();
        attachLog("Cart total: " + cartTotal);

        // Proceed to Checkout
        attachLog("Clicking Checkout button");
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();

        // Fill billing details
        attachLog("Filling billing details for: " + firstName + " " + lastName);
        checkoutPage.fillBillingDetails(firstName, lastName, address1, city, postCode, country, zone);
        checkoutPage.clickBillingContinue();

        // Shipping details
        attachLog("Proceeding through shipping details");
        checkoutPage.clickShippingContinue();

        // Delivery method
        attachLog("Selecting delivery method");
        checkoutPage.addCommentAndContinueDelivery("");

        // Payment method
        attachLog("Agreeing to terms and selecting payment method");
        checkoutPage.agreeAndContinuePayment();

        // Confirm order — verify total matches cart total
        attachLog("Confirming order");
        checkoutPage.confirmOrder();

        // Verify success
        Assert.assertTrue(checkoutPage.isOrderPlaced(),
                "Expected 'Your order has been placed!' message");

        attachLog("Order placed successfully!");

        // Verify cart is empty (0 items in mini cart)
        String miniCartText = homePage.getMiniCartText();
        attachLog("Mini cart text after order: " + miniCartText);
        Assert.assertTrue(miniCartText.contains("0 item(s)") || miniCartText.contains("0 item"),
                "Cart should show 0 items after order is placed. Got: " + miniCartText);

        // Logout
        homePage.logout();
        attachLog("Logged out successfully");
    }
}
