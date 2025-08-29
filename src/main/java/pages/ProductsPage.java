package pages;

// Selenium imports for interacting with web elements and handling exceptions
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductsPage {

    // WebDriver used to control the browser
    private final WebDriver driver;

    // WebDriverWait to handle explicit waits (10-second timeout)
    private final WebDriverWait wait;

    // Locator for the "Products" link in the navbar
    private final By productsLink = By.cssSelector("a[href='/products']");

    // Locator to detect when product cards are loaded on the page
    private final By anyProductCard = By.cssSelector(".features_items .product-image-wrapper");

    // Locator for "View Cart" button that appears in modal after adding a product
    private final By viewCartLinkInModal = By.xpath("//u[normalize-space()='View Cart']/parent::a");

    /**
     * Constructor to initialize the page object with WebDriver and WebDriverWait
     */
    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Opens the products page and waits for product cards to appear
     */
    public void openProductsPage() {
        wait.until(ExpectedConditions.elementToBeClickable(productsLink)).click(); // Click "Products"
        wait.until(ExpectedConditions.visibilityOfElementLocated(anyProductCard));  // Wait for products to load
    }

    /**
     * Adds a single unit of the product with the given name (e.g., "Blue Top") to the cart
     */
    public void addOneToCart(String productName) {
        // Build dynamic XPath based on product name to locate "Add to cart" button
        By addBtn = By.xpath("//div[contains(@class,'productinfo')][.//p[normalize-space()='" + productName + "']]//a[contains(@class,'add-to-cart')]");

        // Wait until the button is clickable
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(addBtn));

        // Scroll into view in case the button is off-screen
        scrollIntoView(btn);

        // Click the button (with JS fallback)
        safeClick(btn);

        // Wait for the "View Cart" modal to become clickable
        wait.until(ExpectedConditions.elementToBeClickable(viewCartLinkInModal));
    }

    /**
     * Clicks the "View Cart" link that appears in the confirmation modal
     */
    public void clickViewCartInModal() {
        wait.until(ExpectedConditions.elementToBeClickable(viewCartLinkInModal)).click();
    }

    /**
     * Scrolls the given element into view using JavaScript
     */
    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    /**
     * Attempts to click the element normally, falls back to JS click if intercepted
     */
    private void safeClick(WebElement el) {
        try {
            el.click(); // Try regular Selenium click
        } catch (ElementClickInterceptedException e) {
            // If the element is blocked or overlapped, use JS to click
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }
}
