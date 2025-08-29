package stepdefinitions.ui; // Defines the package that holds all Cucumber step definition classes

import io.cucumber.java.en.*;          // Imports Cucumber annotations like @When, @Then
import static org.junit.Assert.*;      // Imports JUnit assertion methods: assertTrue, assertEquals

import pages.CartPage;                 // Imports the CartPage Page Object
import pages.ProductsPage;            // Imports the ProductsPage Page Object
import utils.DriverManager;           // Singleton WebDriver manager

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

    /**
     * Step: Adds a product with a specific name (e.g., "Blue Top") to the cart.
     * This is usually part of a scenario testing cart functionality.
     *
     * @param name the name of the product to add
     */
    @When("the user adds {string} to the cart")
    public void the_user_adds_product_to_the_cart(String name) {
        products.addOneToCart(name);                        // Calls method to add the specified product
        logger.info("Added " + name + " to cart (1 unit)"); // Logs which product was added
    }

    /**
     * Step: Opens the cart page from the modal popup after adding a product.
     * This simulates the user clicking "View Cart" in the confirmation modal.
     */
    @When("the user views the cart")
    public void the_user_views_the_cart() {
        products.clickViewCartInModal();              // Calls method to click "View Cart" in the modal
        logger.info("Clicked View Cart in modal");    // Logs the action
    }

    /**
     * Step: Verifies that the cart contains the specified product with the correct quantity.
     *
     * @param name the name of the product expected in the cart
     * @param qty  the quantity of the product expected
     */
    @Then("the cart should show {string} with quantity {int}")
    public void the_cart_should_show_with_quantity(String name, Integer qty) {
        // Assert that the product is present in the cart
        assertTrue("Cart does not contain product: " + name, cart.hasLine(name));

        // Assert that the quantity of the product matches the expected quantity
        assertEquals("Wrong quantity for " + name, qty.intValue(), cart.getQuantity(name));
    }
}
