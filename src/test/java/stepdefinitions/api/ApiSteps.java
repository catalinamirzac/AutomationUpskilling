package stepdefinitions.api;

import com.github.javafaker.Faker;
import context.ScenarioContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import actions.ApiActions;
import utils.FormDataResolver;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiSteps {

    private static final Logger logger = LogManager.getLogger(ApiSteps.class);

    private final Faker faker = new Faker();
    private final ScenarioContext scenarioContext = new ScenarioContext();
    private <T> T as(Class<T> type) {
        return response.then().extract().as(type);
    }

    private Response response;

    // ---------- Given ----------
    @Given("the API base URL is loaded from config")
    public void load_base_url_from_config() {
        // ApiActions reads baseUrl internally via PropertiesUtil
        logger.info("API base URL loaded from config.");
    }

    // ---------- When ----------
    @When("I send a GET request to {string}")
    public void i_send_get_request(String endpoint) {
        response = ApiActions.get(endpoint);
    }

    @When("I send a POST request to {string}")
    public void i_send_post_request_without_body(String endpoint) {
        response = ApiActions.post(endpoint);
    }

    @When("I send a POST request to {string} with body:")
    public void i_send_post_request_with_body(String endpoint, DataTable table) {
        Map<String, String> formParams = FormDataResolver.resolve(
                table.asMap(String.class, String.class),
                faker,
                scenarioContext
        );
        response = ApiActions.postWithForm(endpoint, formParams);
    }

    @When("I send a PUT request to {string}")
    public void i_send_put_request(String endpoint) {
        response = ApiActions.put(endpoint);
    }

    // ---------- Then ----------
    @Then("the response code should be {int}")
    public void the_response_code_should_be(int expectedCode) {
        assertThat(response).as("Response was not set").isNotNull();
        int actual = response.statusCode();
        logger.info("Status Code: expected={}, actual={}", expectedCode, actual);
        assertThat(actual).isEqualTo(expectedCode);
    }

    @Then("the response JSON message should be {string}")
    public void theResponseJSONMessageShouldBe(String expected) {
        assertThat(response).as("Response was not set").isNotNull();
        String actual = response.then().extract().jsonPath().getString("message");
        assertThat(actual).isEqualTo(expected);
    }

    @Then("the response body has path {string}")
    public void response_body_has_path(String path) {
        assertThat(response).as("Response was not set").isNotNull();
        Object value = response.jsonPath().get(path);
        assertThat(value).as("Expected JSON path '%s' to exist", path).isNotNull();
    }

    @Then("the response body should contain field {string}")
    public void response_body_should_contain_field(String path) {
        assertThat(response).as("Response was not set").isNotNull();
        Object value = response.jsonPath().get(path);
        assertThat(value).as("Expected JSON path '%s' to exist", path).isNotNull();
    }

    @Then("the response body at path {string} is a non-empty list")
    public void response_path_is_non_empty_list(String path) {
        assertThat(response).as("Response was not set").isNotNull();
        assertThat(response.jsonPath().getList(path)).isNotNull().isNotEmpty();
    }

    @Then("the response body at path {string} equals {int}")
    public void response_path_equals_int(String path, int expected) {
        assertThat(response).as("Response was not set").isNotNull();
        assertThat(response.jsonPath().getInt(path)).isEqualTo(expected);
    }

    @Then("the response body should contain responseCode {int} and message {string}")
    public void response_should_contain_code_and_message(int expectedCode, String expectedMessage) {
        assertThat(response).as("Response was not set").isNotNull();
        int code = response.then().extract().jsonPath().getInt("responseCode");
        String message = response.then().extract().jsonPath().getString("message");
        assertThat(code).isEqualTo(expectedCode);
        assertThat(message).isEqualTo(expectedMessage);
    }

    @Then("the response should contain a list of products")
    public void response_should_contain_products() {
        assertThat(response).as("Response was not set").isNotNull();
        var products = response.then().extract().jsonPath().getList("products");
        assertThat(products).as("products").isNotNull().isNotEmpty();
    }

    @Then("the response should contain a list of brands")
    public void response_should_contain_brands() {
        assertThat(response).as("Response was not set").isNotNull();
        var brands = response.then().extract().jsonPath().getList("brands");
        assertThat(brands).as("brands").isNotNull().isNotEmpty();
    }
}
