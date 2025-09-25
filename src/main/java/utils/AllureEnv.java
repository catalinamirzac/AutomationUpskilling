// src/test/java/utils/AllureEnv.java
package utils;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Writes target/allure-results/environment.properties so the Allure
 * "Overview -> Environment" panel shows Run At / OS / Java / Base URL / Browser.
 * Safe to call multiple times; it only writes once per run.
 */
public final class AllureEnv {
    private AllureEnv() {}

    public static void writeOnce(WebDriver driver) {
        try {
            // Where Allure writes results
            Path resultsDir = Paths.get(
                    System.getProperty("allure.results.directory", "target/allure-results")
            );
            Files.createDirectories(resultsDir);

            Path envFile = resultsDir.resolve("environment.properties");
            if (Files.exists(envFile)) return; // already written this run

            Properties p = new Properties();

            // Basic runtime info
            p.put("Run At", ZonedDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")));
            p.put("OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));
            p.put("Java", System.getProperty("java.version"));

            // Optional: show base URLs if you pass them via -DbaseUrl / -DapiBase
            String baseUrl = System.getProperty("baseUrl");
            String apiBase = System.getProperty("apiBase");
            if (baseUrl != null && !baseUrl.isBlank()) p.put("Base URL", baseUrl);
            if (apiBase != null && !apiBase.isBlank()) p.put("API Base", apiBase);

            // Browser info (for UI runs)
            try {
                if (driver instanceof RemoteWebDriver) {
                    Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
                    if (caps != null) {
                        String browser = String.valueOf(caps.getBrowserName());
                        Object version = caps.getCapability("browserVersion");
                        if (version == null) version = caps.getCapability("version"); // some drivers
                        p.put("Browser", browser + (version != null ? (" " + version) : ""));
                        Object platform = caps.getCapability("platformName");
                        if (platform != null) p.put("Platform", String.valueOf(platform));
                    }
                }
            } catch (Throwable ignored) { /* never break tests because of env write */ }

            try (OutputStream out = Files.newOutputStream(envFile)) {
                p.store(out, "Allure environment");
            }
        } catch (Exception ignored) {
            // swallow errors – this file is nice-to-have only
        }
    }
}
