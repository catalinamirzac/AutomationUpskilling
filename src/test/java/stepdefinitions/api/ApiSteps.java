package stepdefinitions.api;

import actions.ApiActions;
import com.github.javafaker.Faker;
import context.ScenarioContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import models.responses.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.FormDataResolver;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiSteps {

    private static final Logger logger = LogManager.getLogger(ApiSteps.class);

    private final Faker faker = new Faker();
    private final ScenarioContext scenarioContext = new ScenarioContext();
    private Response response;

    private ApiResponse asApiResponse() {
        assertThat(response).as("Response was not set").isNotNull();
        return response.then().extract().as(ApiResponse.class);
    }

    // -------- Given --------
    @Given("the API base URL is loaded from config")
    public void load_base_url_from_config() {
        logger.info("API base URL loaded from config.");
    }

    // -------- When --------
    @When("I send a GET request to {string}")
    public void i_send_get_request(String endpoint) {
        response = ApiActions.get(endpoint);
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

    // -------- Then --------
    @Then("the response code should be {int}")
    public void the_response_code_should_be(int expectedCode) {
        assertThat(response.statusCode()).isEqualTo(expectedCode);
        logger.info("Response code = {}", response.statusCode());
    }

    @Then("the response JSON message should be {string}")
    public void theResponseJSONMessageShouldBe(String expectedMessage) {
        ApiResponse body = asApiResponse();
        assertThat(body.getMessage()).isEqualTo(expectedMessage);
        logger.info("Response message = {}", body.getMessage());
    }

    @Then("the response body should contain field {string}")
    public void response_body_should_contain_field(String path) {
        Object value = response.jsonPath().get(path);
        assertThat(value).as("Expected JSON path '%s' to exist", path).isNotNull();
        logger.info("Response contains field {} = {}", path, value);
    }

    @Then("the response body should contain responseCode {int} and message {string}")
    public void response_should_contain_code_and_message(int expectedCode, String expectedMessage) {
        ApiResponse body = asApiResponse();
        assertThat(body.getResponseCode()).isEqualTo(expectedCode);
        assertThat(body.getMessage()).isEqualTo(expectedMessage);
        logger.info("responseCode={}, message={}", body.getResponseCode(), body.getMessage());
    }

    @Then("the response should contain a list of products")
    public void response_should_contain_products() {
        ApiResponse body = asApiResponse();
        assertThat(body.getProducts()).isNotNull().isNotEmpty();
        logger.info("Products count = {}", body.getProducts().size());
    }

    @Then("the response should contain a list of brands")
    public void response_should_contain_brands() {
        ApiResponse body = asApiResponse();
        assertThat(body.getBrands()).isNotNull().isNotEmpty();
        logger.info("Brands count = {}", body.getBrands().size());
    }
}
