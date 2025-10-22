package stepdefinitions.ui; // Defines the package that holds all Cucumber step definition classes

import io.cucumber.java.en.*;          // Imports Cucumber annotations like @When, @Then
import static org.junit.Assert.*;      // Imports JUnit assertion methods: assertTrue, assertEquals

import pages.CartPage;                 // Imports the CartPage Page Object
import pages.ProductsPage;            // Imports the ProductsPage Page Object
import utils.DriverManager;
// Singleton WebDriver manager

public class CartSteps extends BaseDefine { // Inherits logger functionality from BaseDefine

    // Create instances of Page Objects using the shared WebDriver
    private final ProductsPage products = new ProductsPage(DriverManager.getDriver());
    private final CartPage cart = new CartPage(DriverManager.getDriver());

    /**
     * Step: User navigates to the Products page from anywhere in the site.
     * This usually clicks the "Products" link in the top menu.
     */
    @When("the user navigates to the products page")
    public void the_user_navigates_to_the_products_page() {
        products.openProductsPage();               // Calls method to open the products page
        logger.info("Opened Products page");       // Logs the action
    }


    @When("the user adds {string} to the cart")
    public void the_user_adds_product_to_the_cart(String name) {
        products.addOneToCart(name);                        // Calls method to add the specified product
        logger.info("Added " + name + " to cart (1 unit)"); // Logs which product was added
    }


    @When("the user views the cart")
    public void the_user_views_the_cart() {
        products.clickViewCartInModal();              // Calls method to click "View Cart" in the modal
        logger.info("Clicked View Cart in modal");    // Logs the action
    }

    @Then("the cart should show {string} with quantity {int}")
    public void the_cart_should_show_with_quantity(String name, Integer qty) {

        assertTrue("Cart does not contain product: " + name, cart.hasLine(name));


        assertEquals("Wrong quantity for " + name, qty.intValue(), cart.getQuantity(name));
    }
}
