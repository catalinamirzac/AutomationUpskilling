package hooks.api;

import io.cucumber.java.*;
import io.qameta.allure.Allure;

import java.nio.file.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ApiReportHook {
    // share one stamp per JVM without new classes
    private static String LOG_ROOT;
    private static String RUN_DATE; // yyyy-MM-dd
    private static String RUN_TIME; // HH-mm-ss

    static {
        LOG_ROOT = System.getProperty("LOG_ROOT", "logs");
        RUN_DATE = System.getProperty("RUN_DATE");
        RUN_TIME = System.getProperty("RUN_TIME");
        if (RUN_DATE == null) {
            RUN_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.setProperty("RUN_DATE", RUN_DATE);
        }
        if (RUN_TIME == null) {
            RUN_TIME = LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
            System.setProperty("RUN_TIME", RUN_TIME);
        }
    }

    private static final ThreadLocal<Path> SCENARIO_DIR = new ThreadLocal<>();

    public static void appendToScenarioLog(String text) {
        Path dir = SCENARIO_DIR.get();
        if (dir != null) write(dir, "log.txt", text);
    }

    @Before("@api")
    public void beforeApiScenario(Scenario scenario) {
        Path dir = scenarioDir("API", scenario);
        SCENARIO_DIR.set(dir);
        write(dir, "log.txt", "START " + scenario.getName() + System.lineSeparator());
    }

    @After("@api")
    public void afterApiScenario(Scenario scenario) {
        Path dir = SCENARIO_DIR.get();
        write(dir, "log.txt",
                "END " + scenario.getName() + " -> " + (scenario.isFailed() ? "FAILED" : "PASSED") + System.lineSeparator());
        try {
            Path file = dir.resolve("log.txt");
            if (Files.exists(file)) {
                Allure.addAttachment("Scenario Log", "text/plain", Files.newInputStream(file), ".txt");
            }
        } catch (Exception ignored) {}
        SCENARIO_DIR.remove();
    }

    /** logs/API/<date>/<time>/<scenario>/ */
    private static Path scenarioDir(String type, Scenario s) {
        String name = slug(s.getName());
        Path dir = Paths.get(LOG_ROOT, type, RUN_DATE, RUN_TIME, name);
        try { Files.createDirectories(dir); } catch (Exception ignored) {}
        return dir;
    }

    private static String slug(String s) {
        String n = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        n = n.trim().replaceAll("[\\s_]+","_").replaceAll("[^a-zA-Z0-9._-]","-");
        return n.length() > 120 ? n.substring(0,120) : n;
    }

    private static void write(Path dir, String fileName, String text) {
        try {
            Files.createDirectories(dir);
            Files.writeString(dir.resolve(fileName), text,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception ignored) {}
    }
}
