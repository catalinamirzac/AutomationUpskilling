// src/test/java/hooks/ui/UiReportHook.java
package hooks.ui;

import io.cucumber.java.*;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import utils.AllureEnv;
import utils.DriverManager;
import utils.Screenshots;

import java.io.ByteArrayInputStream;
import java.nio.file.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class UiReportHook {
    // one timestamp per JVM run
    private static String RUN_DATE; // yyyy.MM.dd
    private static String RUN_TIME; // HH_mm_ss
    private static final ThreadLocal<Path> SCENARIO_DIR = new ThreadLocal<>();

    @Before("@ui")
    public void beforeUiScenario(Scenario s) {
        ensureStamp();
        WebDriver driver = DriverManager.getDriver(); // ensure driver exists
        AllureEnv.writeOnce(driver);                  // optional Allure env tab

        Path dir = scenarioDir("UI", s);
        SCENARIO_DIR.set(dir);
        writeString(dir.resolve("log.txt"), "START " + s.getName() + System.lineSeparator());
    }

    @AfterStep("@ui")
    public void snapOnStepFailure(Scenario s) {
        if (!s.isFailed()) return;
        WebDriver d = DriverManager.getDriver();
        byte[] png = Screenshots.take(d);
        if (png != null) {
            Allure.addAttachment("Step FAIL - Screenshot", "image/png", new ByteArrayInputStream(png), ".png");
        }
    }

    // Attach artifacts BEFORE the driver is quit (order higher than teardown)
    @After(value = "@ui", order = 100)
    public void attachArtifacts(Scenario s) {
        WebDriver d = DriverManager.getDriver();
        if (d == null) return;

        Path dir = SCENARIO_DIR.get();

        // Final screenshot
        byte[] png = Screenshots.take(d);
        if (png != null) {
            String base = s.isFailed() ? "FAILED" : "PASSED";
            Allure.addAttachment(base + " - Screenshot", "image/png", new ByteArrayInputStream(png), ".png");
            writeBytes(dir.resolve(base + ".png"), png);
        }

        // Page source
        try {
            String html = d.getPageSource();
            Allure.addAttachment("Page Source", "text/html", html, ".html");
            writeString(dir.resolve("page.html"), html);
        } catch (Throwable ignored) {}

        // Browser console (if available)
        try {
            var entries = d.manage().logs().get("browser").getAll();
            if (!entries.isEmpty()) {
                String text = entries.stream().map(Object::toString).reduce((a,b)->a+"\n"+b).orElse("");
                Allure.addAttachment("Browser Console", "text/plain", text);
                writeString(dir.resolve("console.txt"), text);
            }
        } catch (Throwable ignored) {}

        writeString(dir.resolve("log.txt"),
                "END " + s.getName() + " -> " + (s.isFailed() ? "FAILED" : "PASSED") + System.lineSeparator());
    }

    // -------- helpers (local) --------
    private static void ensureStamp() {
        if (RUN_DATE == null) RUN_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        if (RUN_TIME == null) RUN_TIME = LocalTime.now().format(DateTimeFormatter.ofPattern("HH_mm_ss"));
    }

    private static Path scenarioDir(String type, Scenario s) {
        String name = slug(s.getName());
        Path dir = Paths.get("target", "reports", type, RUN_DATE, RUN_TIME, name);
        try { Files.createDirectories(dir); } catch (Exception ignored) {}
        return dir;
    }

    private static String slug(String s) {
        String n = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        n = n.trim().replaceAll("[\\s_]+","_").replaceAll("[^a-zA-Z0-9._-]","-");
        return n.length() > 120 ? n.substring(0,120) : n;
    }

    private static void writeBytes(Path to, byte[] bytes) {
        try {
            Files.createDirectories(to.getParent());
            Files.write(to, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception ignored) {}
    }

    private static void writeString(Path to, String text) {
        try {
            Files.createDirectories(to.getParent());
            Files.writeString(to, text, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception ignored) {}
    }
}
