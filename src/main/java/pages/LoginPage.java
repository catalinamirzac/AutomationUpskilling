package pages; // Defines the package where this class belongs

// Selenium imports for interacting with web elements and handling exceptions
 import utils.PropertiesUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration; // Used for specifying wait durations

/**
 * Page Object class for the Login Page in Automation Exercise.
 * Encapsulates locators and actions related to login functionality.
 */
public class LoginPage {

    // WebDriver instance used to interact with the browser
    private final WebDriver driver;

    // WebDriverWait instance used for explicit waits (waiting until conditions are met)
    private final WebDriverWait wait;

    // Locators for elements on the page (declared as final so they cannot be reassigned)
    private final By signupLoginLink = By.xpath("//a[contains(.,'Signup / Login')]");
    private final By loginEmail      = By.cssSelector("input[data-qa='login-email']");
    private final By loginPassword   = By.cssSelector("input[data-qa='login-password']");
    private final By loginButton     = By.cssSelector("button[data-qa='login-button']");
    private final By loggedInAs      = By.xpath("//*[contains(.,'Logged in as')]");
    private final By loginError      = By.xpath("//*[contains(.,'Your email or password is incorrect')]");
    private final By logoutLink      = By.xpath("//a[contains(.,'Logout')]");

    /**
     * Constructor: initializes driver and wait.
     * @param driver WebDriver instance provided by test framework
     */
    public LoginPage(WebDriver driver) {
        this.driver = driver; // Assigns the passed WebDriver to the local field
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Explicit wait with 10s timeout
    }

    /**
     * Navigates to the Automation Exercise home page.
     *
     * @param baseUrl Base URL from configuration
     */
    public void navigateToHomePage() {
        String baseUrl = PropertiesUtil.getProperty("baseUrl"); // Read from config
        driver.get(baseUrl); // Open browser with base URL
    }

    /**
     * Clicks the "Signup / Login" link on the home page.
     */
    public void clickSignupLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(signupLoginLink)).click(); // Waits until clickable, then clicks
    }

    /**
     * Enters email into the login email input field.
     * @param email The email string to type
     */
    public void enterEmail(String email) {
        WebElement e = wait.until(ExpectedConditions.visibilityOfElementLocated(loginEmail)); // Wait until visible
        e.clear();    // Clears any existing text
        e.sendKeys(email); // Types the email
    }

    /**
     * Enters password into the login password input field.
     * @param password The password string to type
     */
    public void enterPassword(String password) {
        WebElement p = wait.until(ExpectedConditions.visibilityOfElementLocated(loginPassword)); // Wait until visible
        p.clear();    // Clears any existing text
        p.sendKeys(password); // Types the password
    }

    /**
     * Clicks the login button.
     */
    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click(); // Waits until clickable, then clicks
    }

    /**
     * Checks if the user is logged in by verifying "Logged in as" text is displayed.
     * @return true if logged in, false otherwise
     */
    public boolean isLoggedIn() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loggedInAs)).isDisplayed();
        } catch (TimeoutException e) {
            return false; // If element not found in time, user is not logged in
        }
    }

    /**
     * Checks if login error message is displayed.
     * @return true if error is displayed, false otherwise
     */
    public boolean isErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loginError)).isDisplayed();
        } catch (TimeoutException e) {
            return false; // If error not shown in time, assume false
        }
    }

    /**
     * Logs out the user if they are currently logged in.
     * Useful in hooks to reset state between scenarios.
     */
    public void logoutIfLoggedIn() {
        try {
            // If logout link exists
            if (driver.findElements(logoutLink).size() > 0) {
                wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click(); // Click logout
                wait.until(ExpectedConditions.visibilityOfElementLocated(signupLoginLink)); // Wait until back on home
            }
        } catch (NoSuchElementException | TimeoutException ignored) {
            // Already logged out or logout not available — do nothing
        }
    }
}
