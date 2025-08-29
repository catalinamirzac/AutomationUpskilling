// DriverManager.java
package utils;

import utils.PropertiesUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverManager {
    private static WebDriver driver;

    private DriverManager(){}

    public static WebDriver getDriver() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");

            // headless toggle
            if (PropertiesUtil.isHeadless()) {
                // if this fails on older Chrome, switch to "--headless"
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
            }

            // ✅ Selenium Manager will download / pick the right ChromeDriver
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) { driver.quit(); driver = null; }
    }
}
