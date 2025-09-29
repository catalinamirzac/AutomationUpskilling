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

import models.responses.BrandsResponse;
import models.responses.GenericResponse;
import models.responses.ProductsResponse;

public class ApiSteps {

    private static final Logger logger = LogManager.getLogger(ApiSteps.class);

    private final Faker faker = new Faker();
    private final ScenarioContext scenarioContext = new ScenarioContext();
    private Response response;

    // -------- Helper (used by several steps) --------
    private <T> T as(Class<T> type) {
        assertThat(response).as("Response was not set").isNotNull();
        return response.then().extract().as(type);
    }



    // -------- Custom summary loggers --------
    private void logProductsSummary(ProductsResponse productsResponse) {
        logger.info("Received {} products", productsResponse.getProducts().size());
    }

    private void logBrandsSummary(BrandsResponse brandsResponse) {
        logger.info("Received {} brands", brandsResponse.getBrands().size());
    }


    // -------- Given --------
    @Given("the API base URL is loaded from config")
    public void load_base_url_from_config() {
        logger.info("✅ API base URL loaded from config.");
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
        assertThat(response).as("Response was not set").isNotNull();
        assertThat(response.statusCode()).isEqualTo(expectedCode);
    }

    @Then("the response JSON message should be {string}")
    public void theResponseJSONMessageShouldBe(String expectedMessage) {
        GenericResponse genericResponse = as(GenericResponse.class);
        assertThat(genericResponse.getMessage()).isEqualTo(expectedMessage);
    }

    @Then("the response body should contain field {string}")
    public void response_body_should_contain_field(String path) {
        assertThat(response).as("Response was not set").isNotNull();
        Object value = response.jsonPath().get(path);
        assertThat(value).as("Expected JSON path '%s' to exist", path).isNotNull();
    }

    @Then("the response body should contain responseCode {int} and message {string}")
    public void response_should_contain_code_and_message(int expectedCode, String expectedMessage) {
        GenericResponse genericResponse = as(GenericResponse.class);
        assertThat(genericResponse.getResponseCode()).isEqualTo(expectedCode);
        assertThat(genericResponse.getMessage()).isEqualTo(expectedMessage);
    }

    @Then("the response should contain a list of products")
    public void response_should_contain_products() {
        ProductsResponse productsResponse = as(ProductsResponse.class);
        assertThat(productsResponse.getProducts()).isNotNull().isNotEmpty();
        logProductsSummary(productsResponse); // 👈 useful summary
    }

    @Then("the response should contain a list of brands")
    public void response_should_contain_brands() {
        BrandsResponse brandsResponse = as(BrandsResponse.class);
        assertThat(brandsResponse.getBrands()).isNotNull().isNotEmpty();
        logBrandsSummary(brandsResponse); // 👈 useful summary
    }
}
