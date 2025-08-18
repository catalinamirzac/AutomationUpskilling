package stepdefinitions; // Package for step definition classes

import io.cucumber.java.en.*; // Cucumber step annotations: @Given, @When, @Then, etc.
import pages.LoginPage;       // Page Object representing the Login page
import utils.DriverManager;   // Singleton WebDriver manager
import utils.PropertiesUtil;  // Utility to read from config.properties
import static org.junit.Assert.*; // JUnit assertion methods: assertTrue, assertEquals, etc.

public class LoginSteps extends BaseDefine { // Extends BaseDefine to inherit logger

    // Create a LoginPage object using the shared WebDriver instance
    private final LoginPage loginPage = new LoginPage(DriverManager.getDriver());

    // Step for opening the Automation Exercise homepage
    @Given("the user is on the Automation exercise home page")
    public void the_user_is_on_the_home_page() {
        loginPage.navigateToHomePage(PropertiesUtil.getProperty("baseUrl")); // Open base URL
        logger.info("Navigated to Automation exercise home page"); // Log action
    }

    // Step for clicking on the Signup/Login link
    @When("the user navigates to the login page")
    public void the_user_navigates_to_the_login_page() {
        loginPage.clickSignupLogin(); // Click on "Signup / Login"
        logger.info("Clicked on Signup/Login link");
    }

    // Step for parameterized login using Scenario Outline values
    @When("the user enters {string} and {string}")
    public void the_user_enters_credentials(String email, String password) {
        loginPage.enterEmail(email);         // Enter email from feature file
        loginPage.enterPassword(password);   // Enter password from feature file
        logger.info(String.format("Entered email [%s] and password [%s]", email, password)); // Log inputs
    }

    // Step for clicking the login button
    @When("the user clicks the login button")
    public void the_user_clicks_the_login_button() {
        loginPage.clickLogin(); // Click login
        logger.info("Clicked login button");
    }

    // Step for entering static credentials from config.properties
    @When("valid Automation exercise credentials are entered")
    public void valid_credentials_are_entered() {
        String username = PropertiesUtil.getProperty("username"); // Load username
        String password = PropertiesUtil.getProperty("password"); // Load password
        loginPage.enterEmail(username); // Enter username
        loginPage.enterPassword(password); // Enter password
        logger.info("Entered valid credentials");
    }

    // Step for verifying successful login by checking UI
    @Then("the user should be logged in and see the Logged in as text")
    public void user_should_be_logged_in() {
        assertTrue("User is not logged in", loginPage.isLoggedIn()); // Validate user is logged in
        logger.info("Login successful - user is logged in"); // Log result
    }

    // Step for checking whether login succeeded or failed based on a keyword
    @Then("the login should {word}")
    public void the_login_should(String result) {
        switch (result.toLowerCase()) {
            case "succeed":
                assertTrue("Login should succeed but didn't", loginPage.isLoggedIn()); // Success condition
                logger.info("Login succeeded as expected");
                break;
            case "fail":
                assertTrue("Login should fail but didn't", loginPage.isErrorDisplayed()); // Failure condition
                logger.info("Login failed as expected");
                break;
            default:
                fail("Unknown result: " + result); // Fail if keyword is not "succeed" or "fail"
        }
    }
}
