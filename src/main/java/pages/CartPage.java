package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cartInfo = By.id("cart_info");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private By rowFor(String productName) {
        return By.xpath("//tr[.//td[@class='cart_description']//a[normalize-space()='" + productName + "']]");
    }

    public boolean hasLine(String productName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartInfo));
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(rowFor(productName))).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Reads quantity whether it's rendered as a button, input, or plain text. */
    public int getQuantity(String productName) {
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(rowFor(productName)));

        // 1) Button variant (common): <td class="cart_quantity"><button class="disabled">1</button>
        List<WebElement> buttons =
                row.findElements(By.cssSelector("td.cart_quantity button.disabled, td.cart_quantity button"));
        if (!buttons.isEmpty()) {
            String txt = buttons.get(0).getText().trim();
            if (!txt.isEmpty()) {
                String digits = txt.replaceAll("[^0-9]", "");
                if (!digits.isBlank()) return Integer.parseInt(digits);
            }
        }

        // 2) Input variant (older): <input class="cart_quantity_input" value="1">
        List<WebElement> inputs =
                row.findElements(By.cssSelector("td.cart_quantity input.cart_quantity_input, td.cart_quantity input"));
        if (!inputs.isEmpty()) {
            String val = inputs.get(0).getAttribute("value");
            if (val != null && !val.isBlank()) {
                String digits = val.replaceAll("[^0-9]", "");
                if (!digits.isBlank()) return Integer.parseInt(digits);
            }
        }

        // 3) Fallback: any digits inside the quantity cell
        WebElement qtyCell = row.findElement(By.cssSelector("td.cart_quantity"));
        String digits = qtyCell.getText().replaceAll("[^0-9]", "");
        if (!digits.isBlank()) return Integer.parseInt(digits);

        throw new NoSuchElementException("Could not read quantity for '" + productName + "'");
    }
}
