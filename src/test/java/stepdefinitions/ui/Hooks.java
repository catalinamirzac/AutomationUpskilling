package stepdefinitions.ui;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import utils.DriverManager;

public class Hooks {

    /**
     * Runs before scenarios NOT tagged with @api.
     * Prevents browser from launching during API tests.
     */
    @Before("not @api")
    public void setUp() {
        DriverManager.getDriver();
    }

    /**
     * Runs after scenarios NOT tagged with @api.
     * Handles screenshots and browser cleanup.
     */
    @After("not @api")
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();

        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failure Screenshot");
        }

        DriverManager.quitDriver();
    }
}
