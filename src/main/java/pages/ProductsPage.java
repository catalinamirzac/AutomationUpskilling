package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By productsLink = By.cssSelector("a[href='/products']");
    private final By anyProductCard = By.cssSelector(".features_items .product-image-wrapper");
    private final By viewCartLinkInModal = By.xpath("//u[normalize-space()='View Cart']/parent::a");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openProductsPage() {
        wait.until(ExpectedConditions.elementToBeClickable(productsLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(anyProductCard));
    }

    /** Adds a single unit of the product by visible name, e.g. "Blue Top". */
    public void addOneToCart(String productName) {
        By addBtn = By.xpath("//div[contains(@class,'productinfo')][.//p[normalize-space()='" + productName + "']]//a[contains(@class,'add-to-cart')]");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(addBtn));
        scrollIntoView(btn);
        safeClick(btn);
        // modal appears with "View Cart"
        wait.until(ExpectedConditions.elementToBeClickable(viewCartLinkInModal));
    }

    public void clickViewCartInModal() {
        wait.until(ExpectedConditions.elementToBeClickable(viewCartLinkInModal)).click();
    }

    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    private void safeClick(WebElement el) {
        try { el.click(); }
        catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }
}
