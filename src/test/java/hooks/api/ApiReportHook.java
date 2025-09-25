package hooks.api;

import io.cucumber.java.*;
import io.qameta.allure.Allure;

import java.nio.file.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ApiReportHook {
    private static String RUN_DATE; // yyyy.MM.dd
    private static String RUN_TIME; // HH_mm_ss
    private static final ThreadLocal<Path> SCENARIO_DIR = new ThreadLocal<>();

    public static void appendToScenarioLog(String text) {
        Path dir = SCENARIO_DIR.get();
        if (dir != null) write(dir, "log.txt", text);
    }

    @Before("@api")
    public void beforeApiScenario(Scenario scenario) {
        utils.AllureEnv.writeOnce(null); // environment tab in Allure
        ensureStamp();
        Path dir = scenarioDir("API", scenario);
        SCENARIO_DIR.set(dir);
        write(dir, "log.txt", "START " + scenario.getName() + System.lineSeparator());
    }

    @After("@api")
    public void afterApiScenario(Scenario scenario) {
        Path dir = SCENARIO_DIR.get();
        write(dir, "log.txt",
                "END " + scenario.getName() + " -> " + (scenario.isFailed() ? "FAILED" : "PASSED")
                        + System.lineSeparator());
        try {
            Path file = dir.resolve("log.txt");
            if (Files.exists(file)) {
                Allure.addAttachment("Scenario Log", "text/plain", Files.newInputStream(file), ".txt");
            }
        } catch (Exception ignored) {}
        SCENARIO_DIR.remove();
    }

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

    private static void write(Path dir, String fileName, String text) {
        try {
            if (dir == null) return;
            Files.createDirectories(dir);
            Files.writeString(dir.resolve(fileName), text,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception ignored) {}
    }
}
