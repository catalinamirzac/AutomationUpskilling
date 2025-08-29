package stepdefinitions.api;

import com.github.javafaker.Faker;
import Enums.AccountFields;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.PropertiesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

public class ApiSteps {

    private static final Logger logger = LogManager.getLogger(ApiSteps.class);
    private final Faker faker = new Faker();

    private Response response;
    private RequestSpecification request;
    private String baseUrl;

    @Given("the API base URL is loaded from config")
    public void load_base_url_from_config() {
        this.baseUrl = PropertiesUtil.getProperty("baseUrl");
        this.request = given()
                .baseUri(baseUrl)
                .contentType("application/x-www-form-urlencoded");

        logger.info(" Base URL set to: {}", baseUrl);
    }

    @When("I send a GET request to {string}")
    public void i_send_get_request(String endpoint) {
        response = given().get(endpoint);
        String raw = response.getBody().asString();

        if (raw.contains("<body>") && raw.contains("</body>")) {
            raw = raw.substring(raw.indexOf("<body>") + 6, raw.indexOf("</body>")).trim();
        }

        logger.info(" GET → {}", endpoint);
        logger.info(" Response body:\n{}", raw);
    }

    @When("I send a POST request to {string} with body:")
    public void i_send_post_request_to_with_body(String endpoint, Map<String, String> originalParams) {
        Map<String, String> formParams = new HashMap<>();

        for (Map.Entry<String, String> entry : originalParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if ("faker".equalsIgnoreCase(value)) {
                try {
                    AccountFields field = AccountFields.fromKey(key);
                    value = field.generate(faker);
                    logger.info(" Faker generated {}: {}", key, value);
                } catch (IllegalArgumentException e) {
                    logger.warn(" Unknown key: {}. Skipping Faker.", key);
                }
            }

            formParams.put(key, value);
        }

        response = request
                .formParams(formParams)
                .post(endpoint)
                .then()
                .extract()
                .response();

        logger.info(" POST to: {} with body: {}", endpoint, formParams);
        logger.info(" Response:\n{}", response.asPrettyString());
    }


    @Then("the response code should be {int}")
    public void the_response_code_should_be(int expectedCode) {
        int actualCode = response.statusCode();
        logger.info(" Status Code: expected={}, actual={}", expectedCode, actualCode);
        assertThat(actualCode).isEqualTo(expectedCode);
    }

    @Then("the response body should contain field {string}")
    public void response_should_contain_field(String field) {
        int responseCode = response.jsonPath().getInt(field);
        logger.info("🔎 Response field: {} = {}", field, responseCode);
        assertThat(responseCode).isEqualTo(201);
    }


    @When("I send a PUT request to {string}")
    public void i_send_put_request_to(String endpoint) {
        baseUrl = PropertiesUtil.getProperty("baseUrl");

        response = given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();

        logger.info(" PUT → {}", endpoint);
        logger.info(" Response body:\n{}", response.asPrettyString());
    }

    @Then("the response body should contain responseCode {int} and message {string}")
    public void response_should_contain_code_and_message(int expectedCode, String expectedMessage) {
        String body = response.getBody().asString();
        logger.info(" Verifying response body contains: responseCode {} and message \"{}\"", expectedCode, expectedMessage);

        assertThat(body).contains("\"responseCode\": " + expectedCode);
        assertThat(body).contains("\"message\": \"" + expectedMessage + "\"");

        logger.info(" Verified response contains correct code and message.");
    }


    @Then("the response body should contain the message {string}")
    public void verify_response_contains_message(String expectedMessage) {
        String body = response.getBody().asString();
        assertThat(body).contains(expectedMessage);
        logger.info(" Response body contains message: {}", expectedMessage);
    }

    @Then("the response should contain a list of products")
    public void response_should_contain_products() {
        int size = response.jsonPath().getList("products").size();
        logger.info(" Number of products returned: {}", size);
        assertThat(size).isGreaterThan(0);
    }

    @Then("the response should contain a list of brands")
    public void response_should_contain_brands() {
        int size = response.jsonPath().getList("brands").size();
        logger.info(" Number of brands returned: {}", size);
        assertThat(size).isGreaterThan(0);
    }
}
