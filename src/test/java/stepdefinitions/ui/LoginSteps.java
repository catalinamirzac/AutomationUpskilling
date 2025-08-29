package stepdefinitions.ui;

import io.cucumber.java.en.*;
import pages.LoginPage;
import utils.DriverManager;
import utils.PropertiesUtil;

import static org.junit.Assert.*;

public class LoginSteps extends BaseDefine {

    private final LoginPage loginPage = new LoginPage(DriverManager.getDriver());

    @Given("the user is on the Automation exercise home page")
    public void the_user_is_on_the_home_page() {
        loginPage.navigateToHomePage();
        logger.info("Navigated to Automation exercise home page");
    }

    @When("the user navigates to the login page")
    public void the_user_navigates_to_the_login_page() {
        loginPage.clickSignupLogin();
        logger.info("Clicked on Signup/Login link");
    }

    // Scenario Outline uses "<email>" and "<password>" -> {string} is correct
    @When("the user enters {string} and {string}")
    public void the_user_enters_credentials(String email, String password) {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        logger.info(String.format("Entered email [%s] and password [%s]", email, password));
    }

    @When("the user clicks the login button")
    public void the_user_clicks_the_login_button() {
        loginPage.clickLogin();
        logger.info("Clicked login button");
    }

    @When("valid Automation exercise credentials are entered")
    public void valid_credentials_are_entered() {
        String username = PropertiesUtil.getProperty("username");
        String password = PropertiesUtil.getProperty("password");
        loginPage.enterEmail(username);
        loginPage.enterPassword(password);
        logger.info("Entered valid credentials");
    }

    @Then("the user should be logged in and see the Logged in as text")
    public void user_should_be_logged_in() {
        assertTrue("User is not logged in", loginPage.isLoggedIn());
        logger.info("Login successful - user is logged in");
    }

    // Matches: Then the login should <result>   where result ∈ {succeed, fail}
    @Then("the login should {word}")
    public void the_login_should(String result) {
        switch (result.toLowerCase()) {
            case "succeed":
                assertTrue("Login should succeed but didn't", loginPage.isLoggedIn());
                logger.info("Login succeeded as expected");
                break;
            case "fail":
                assertTrue("Login should fail but didn't", loginPage.isErrorDisplayed());
                logger.info("Login failed as expected");
                break;
            default:
                fail("Unknown result: " + result);
        }
    }
}
