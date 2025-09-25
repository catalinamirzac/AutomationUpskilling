// src/test/java/hooks/ui/UiWebDriverHook.java
package hooks.ui;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import utils.DriverManager;

public class UiWebDriverHook {

    @Before("@ui")
    public void startDriver(Scenario s) {
        // Ensure a driver is created before the scenario
        WebDriver driver = DriverManager.getDriver();
        // Optionally: driver.manage().window().maximize();
    }

    // Quit LAST so reporting hooks (order=100) can still access the driver
    @After(value = "@ui", order = 0)
    public void stopDriver() {
        DriverManager.quitDriver();
    }
}
