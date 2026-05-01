package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.*;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class CartTest extends BaseTest {

    @DataProvider(name = "cartData")
    public Object[][] getCartData() {
        return ExcelReader.getTestData("AddToCart");
    }

    @Test(dataProvider = "cartData", description = "TC10 - Add items to cart")
    @Description("Verify that multiple products can be added to the cart and both appear in the cart summary")
    public void testAddItemsToCart(String email, String password, String tabletProduct, String laptopProduct) {
        LoginPage loginPage = homePage.goToLogin();
        loginPage.loginSuccessfully(email, password);

        // Add tablet
        CategoryPage tabletsPage = homePage.clickNavCategory("Tablets");
        ProductPage tabletPage = tabletsPage.clickProduct(tabletProduct);
        String tabletMsg = tabletPage.addToCartAndGetSuccess();
        Assert.assertTrue(tabletMsg.contains(tabletProduct) || tabletMsg.contains("Success"),
                "Expected success after adding tablet. Got: " + tabletMsg);

        CartPage cartPage = homePage.goToCart();
        Assert.assertTrue(cartPage.containsProduct(tabletProduct),
                "Cart should contain: " + tabletProduct);

        // Add laptop
        CategoryPage laptopsPage = homePage.clickLaptopsShowAll();
        ProductPage laptopPage = laptopsPage.clickProduct(laptopProduct);
        if (laptopPage.hasDeliveryDateField()) {
            laptopPage.setDeliveryDate("2025-12-31");
        }
        String laptopMsg = laptopPage.addToCartAndGetSuccess();
        Assert.assertTrue(laptopMsg.contains("Success"),
                "Expected success after adding laptop. Got: " + laptopMsg);

        // Verify both in cart
        CartPage finalCart = homePage.goToCart();
        List<String> cartItems = finalCart.getCartProductNames();
        Assert.assertTrue(finalCart.containsProduct(tabletProduct),
                "Cart should contain: " + tabletProduct);
        Assert.assertTrue(finalCart.containsProduct(laptopProduct),
                "Cart should contain: " + laptopProduct);

        String total = finalCart.getCartTotal();
        Assert.assertFalse(total == null || total.isEmpty(), "Cart total should be displayed");

        homePage.logout();
    }
}
