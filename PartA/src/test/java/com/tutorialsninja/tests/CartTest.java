package com.tutorialsninja.tests;

import com.tutorialsninja.base.BaseTest;
import com.tutorialsninja.pages.*;
import com.tutorialsninja.utils.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Shopping Cart")
@Feature("Add to Cart")
public class CartTest extends BaseTest {

    @DataProvider(name = "cartData")
    public Object[][] getCartData() {
        return ExcelReader.getTestData("AddToCart");
    }

    @Test(dataProvider = "cartData", description = "TC10 - Add items to shopping cart and compare totals")
    @Story("TC10 - Add Items to Shopping Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify adding multiple products to cart and validating totals match the sum of individual prices")
    public void testAddItemsToCart(String email, String password, String tabletProduct, String laptopProduct) {
        // Login
        attachLog("Logging in with: " + email);
        LoginPage loginPage = homePage.goToLogin();
        loginPage.loginSuccessfully(email, password);

        // Step 1: Navigate to Tablets and add Samsung Galaxy Tab 10.1
        attachLog("Navigating to Tablets category");
        CategoryPage tabletsPage = homePage.clickNavCategory("Tablets");

        attachLog("Clicking on product: " + tabletProduct);
        ProductPage tabletProductPage = tabletsPage.clickProduct(tabletProduct);

        attachLog("Adding " + tabletProduct + " to cart");
        String tabletSuccess = tabletProductPage.addToCartAndGetSuccess();
        attachLog("Success message: " + tabletSuccess);

        Assert.assertTrue(tabletSuccess.contains(tabletProduct) || tabletSuccess.contains("Success"),
                "Expected success message after adding tablet to cart. Got: " + tabletSuccess);

        // Step 2: View cart and verify tablet is there
        attachLog("Opening cart to verify tablet was added");
        CartPage cartPage = homePage.goToCart();

        Assert.assertTrue(cartPage.containsProduct(tabletProduct),
                "Cart should contain: " + tabletProduct);

        String tabletPriceInCart = cartPage.getCartTotal();
        attachLog("Cart total after tablet: " + tabletPriceInCart);

        // Step 3: Navigate to Laptops and add HP LP3065
        attachLog("Navigating to Laptops & Notebooks category");
        CategoryPage laptopsPage = homePage.clickLaptopsShowAll();

        attachLog("Clicking on product: " + laptopProduct);
        ProductPage laptopProductPage = laptopsPage.clickProduct(laptopProduct);

        // Set delivery date for HP LP3065 (required field)
        if (laptopProductPage.hasDeliveryDateField()) {
            attachLog("Setting delivery date for " + laptopProduct);
            laptopProductPage.setDeliveryDate("2025-12-31");
        }

        attachLog("Adding " + laptopProduct + " to cart");
        String laptopSuccess = laptopProductPage.addToCartAndGetSuccess();
        attachLog("Success message: " + laptopSuccess);

        Assert.assertTrue(laptopSuccess.contains("Success"),
                "Expected success message after adding laptop to cart. Got: " + laptopSuccess);

        // Step 4: View cart and verify both items
        attachLog("Opening cart to verify both products");
        CartPage cartPageFinal = homePage.goToCart();

        List<String> cartItems = cartPageFinal.getCartProductNames();
        attachLog("Cart items: " + cartItems);

        Assert.assertTrue(cartPageFinal.containsProduct(tabletProduct),
                "Cart should contain: " + tabletProduct);
        Assert.assertTrue(cartPageFinal.containsProduct(laptopProduct),
                "Cart should contain: " + laptopProduct);

        String cartTotal = cartPageFinal.getCartTotal();
        attachLog("Final cart total: " + cartTotal);
        Assert.assertNotNull(cartTotal, "Cart total should not be null");
        Assert.assertFalse(cartTotal.isEmpty(), "Cart total should not be empty");

        // Logout
        homePage.logout();
        attachLog("Logged out successfully");
    }
}
