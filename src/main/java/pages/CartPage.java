package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object class for the Cart page of AutomationExercise.
 * Encapsulates locators and methods to validate cart content.
 */
public class CartPage {

    private final WebDriver driver;

    // XPath for each cart line item, matched by product name
    private final String cartItemXpath = "//tr[.//a[text()='%s']]";

    // XPath for quantity cell inside a cart line item row
    private final String quantityXpath = cartItemXpath + "//button[@class='disabled']";

    /**
     * Constructor that receives a shared WebDriver instance
     * @param driver the WebDriver used to interact with the browser
     */
    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Checks if a cart line with the given product name exists
     * @param productName the name of the product (e.g., "Blue Top")
     * @return true if the product is present in the cart
     */
    public boolean hasLine(String productName) {
        String xpath = String.format(cartItemXpath, productName);
        return !driver.findElements(By.xpath(xpath)).isEmpty();
    }

    /**
     * Gets the quantity of a product in the cart by name
     * @param productName the product to search for
     * @return the quantity as an integer (e.g., 1, 2, 3...)
     */
    public int getQuantity(String productName) {
        String xpath = String.format(quantityXpath, productName);
        WebElement qtyButton = driver.findElement(By.xpath(xpath));
        return Integer.parseInt(qtyButton.getText().trim());
    }
}
