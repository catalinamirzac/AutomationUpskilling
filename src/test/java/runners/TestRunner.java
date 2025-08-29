package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

import static io.cucumber.junit.CucumberOptions.SnippetType;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepdefinitions.api", "stepdefinitions.ui"},
        tags = "@ui",
        plugin = {
                "pretty",
                "summary",
                "html:target/ui-cucumber-report.html",
                "json:target/ui-cucumber.json"
        },
        monochrome = true,
        snippets = SnippetType.CAMELCASE,
        publish = false
)
public class TestRunner { }
