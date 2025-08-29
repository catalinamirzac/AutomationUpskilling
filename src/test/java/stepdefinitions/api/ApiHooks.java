package stepdefinitions.api;

import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public class ApiHooks {

    @Before("@api")
    public void setupBaseUri() {
        // Set the base URI so all your requests use this as the root
        RestAssured.baseURI = "https://automationexercise.com";

        // Enable logging of every request and response in the console
        RestAssured.filters(
                new RequestLoggingFilter(),    // Logs the request: method, headers, URI
                new ResponseLoggingFilter()    // Logs the response: status, body, headers
        );
    }
}
