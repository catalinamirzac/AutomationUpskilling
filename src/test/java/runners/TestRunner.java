package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

import static io.cucumber.junit.CucumberOptions.SnippetType;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "stepdefinitions.api",
                "stepdefinitions.ui",
                "hooks"   // 👈 contains ReportingHooks
        },
        plugin = {
                "pretty",
                "summary",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true,
        snippets = SnippetType.CAMELCASE,
        publish = false
)
public class TestRunner { }
