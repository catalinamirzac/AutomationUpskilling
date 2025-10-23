package hooks.api;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;

public class ApiRestAssuredHook {

    @Before("@api")
    public void configureRestAssured(Scenario s) {
        // Base URL can be overridden with -DapiBase=
        RestAssured.baseURI = System.getProperty("apiBase", "https://automationexercise.com/api");

        RestAssured.config = RestAssured.config().logConfig(
                LogConfig.logConfig()
                        .enablePrettyPrinting(true)
                        .blacklistHeader("Authorization", "X-Api-Key")
        );

        // Treat non-JSON content types as JSON (the API sometimes returns text/html)
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.registerParser("text/html", Parser.JSON);
        RestAssured.registerParser("text/plain", Parser.JSON);

        RestAssured.replaceFiltersWith(
                new AllureRestAssured(),            // optional Allure attachment
                new RequestLoggingFilter(ALL),      // console/run.log (via log4j2)
                new ResponseLoggingFilter(ALL),
                new PerScenarioFileLogFilter()      // our per-scenario file writer
        );
    }

    /** Writes RA request/response into the per-scenario log file. */
    static class PerScenarioFileLogFilter implements Filter {
        @Override
        public Response filter(FilterableRequestSpecification req,
                               FilterableResponseSpecification res,
                               FilterContext ctx) {

            ApiReportHook.appendToScenarioLog("\nREQUEST " + req.getMethod() + " " + req.getURI() + "\n");
            if (!req.getHeaders().asList().isEmpty())
                ApiReportHook.appendToScenarioLog("Headers: " + req.getHeaders() + "\n");
            if (req.getBody() != null)
                ApiReportHook.appendToScenarioLog("Body: " + req.getBody() + "\n");

            Response r = ctx.next(req, res);

            ApiReportHook.appendToScenarioLog("RESPONSE " + r.getStatusLine() + " (" + r.getTime() + " ms)\n");
            if (!r.getHeaders().asList().isEmpty())
                ApiReportHook.appendToScenarioLog("Headers: " + r.getHeaders() + "\n");
            try {
                String body = r.getBody().asPrettyString();
                if (body != null && !body.trim().isEmpty())
                    ApiReportHook.appendToScenarioLog("Body:\n" + body + "\n");
            } catch (Throwable ignored) {}

            return r;
        }
    }
}
