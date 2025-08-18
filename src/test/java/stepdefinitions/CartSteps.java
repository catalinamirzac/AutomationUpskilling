package stepdefinitions;

import io.cucumber.java.en.*;
import static org.junit.Assert.*;

import pages.CartPage;
import pages.ProductsPage;
import utils.DriverManager;

public class CartSteps extends BaseDefine {

    private final ProductsPage products = new ProductsPage(DriverManager.getDriver());
    private final CartPage cart = new CartPage(DriverManager.getDriver());

    @When("the user navigates to the products page")
    public void the_user_navigates_to_the_products_page() {
        products.openProductsPage();
        logger.info("Opened Products page");
    }

    @When("the user adds {string} to the cart")
    public void the_user_adds_product_to_the_cart(String name) {
        products.addOneToCart(name);
        logger.info("Added " + name + " to cart (1 unit)");
    }

    @When("the user views the cart")
    public void the_user_views_the_cart() {
        products.clickViewCartInModal();
        logger.info("Clicked View Cart in modal");
    }

    @Then("the cart should show {string} with quantity {int}")
    public void the_cart_should_show_with_quantity(String name, Integer qty) {
        assertTrue("Cart does not contain product: " + name, cart.hasLine(name));
        assertEquals("Wrong quantity for " + name, qty.intValue(), cart.getQuantity(name));
    }
}
