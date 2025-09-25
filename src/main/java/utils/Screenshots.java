// utils/Screenshots.java
package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedCondition;
import java.time.Duration;

public final class Screenshots {
    private Screenshots() {}

    public static byte[] take(WebDriver driver) {
        try {
            waitForPaint(driver);
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Throwable t) {
            return null;
        }
    }

    private static void waitForPaint(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        wait.until((ExpectedCondition<Boolean>) d ->
                "complete".equals(js.executeScript("return document.readyState")));

        wait.until((ExpectedCondition<Boolean>) d ->
                Boolean.TRUE.equals(js.executeScript(
                        "return !!document.body && document.body.getBoundingClientRect().height > 0")));

        try { Thread.sleep(150); } catch (InterruptedException ignored) {}
    }
}
