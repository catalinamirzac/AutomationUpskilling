// utils/DriverManager.java
package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverManager {
    private static WebDriver driver;

    private DriverManager() {}

    public static WebDriver getDriver() {
        if (driver == null) {
            ChromeOptions opts = new ChromeOptions();

            // toggle with -Dheadless=true/false (default: false = show the window)
            boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

            if (headless) {
                opts.addArguments("--headless=new");
                opts.addArguments("--window-size=1920,1080"); // avoids blank screenshots
            } else {
                opts.addArguments("--start-maximized");
            }

            // stability flags (safe on Windows too)
            opts.addArguments("--disable-gpu");
            opts.addArguments("--no-sandbox");

            driver = new ChromeDriver(opts);

            if (!headless) {
                try { driver.manage().window().maximize(); } catch (Throwable ignored) {}
            }
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
