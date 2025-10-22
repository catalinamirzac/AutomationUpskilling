package actions;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.PropertiesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;

public final class ApiActions {

    private static final Logger logger = LogManager.getLogger(ApiActions.class);
    private static final String BASE_URL = getBaseUrl();

    static {
        // Only auto-log full request/response if an assertion/validation fails
        enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private ApiActions() {}

    private static String getBaseUrl() {
        String url = PropertiesUtil.getProperty("baseUrl");
        if (url == null || url.isBlank()) {
            throw new IllegalStateException("Base URL not set. Please configure 'baseUrl'.");
        }
        return url;
    }

    // ---------- GET ----------
    public static Response get(String endpoint) {
        logger.info("GET {}", endpoint);
        Response response = given()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
        logResponse("GET", endpoint, response);
        return response;
    }

    // ---------- POST ----------
    /** POST with no body */
    public static Response post(String endpoint) {
        logger.info("POST {}", endpoint);
        Response response = given()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.URLENC)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
        logResponse("POST", endpoint, response);
        return response;
    }

    /** POST with form params */
    public static Response postWithForm(String endpoint, Map<String, String> formParams) {
        int size = (formParams == null) ? 0 : formParams.size();
        logger.info("POST {} with {} field{}", endpoint, size, size == 1 ? "" : "s");
        Response response = given()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.URLENC)
                .formParams(formParams)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
        logResponse("POST", endpoint, response);
        return response;
    }

    // ---------- PUT ----------
    public static Response put(String endpoint) {
        logger.info("PUT {}", endpoint);
        Response response = given()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
        logResponse("PUT", endpoint, response);
        return response;
    }

    // ---------- Helper: concise response summary ----------
    private static void logResponse(String method, String endpoint, Response response) {
        int status = -1;
        String message = null;

        try {
            status = response.statusCode();
        } catch (Exception ignored) {}

        try {
            message = response.jsonPath().getString("message");
        } catch (Exception ignored) {
            // Non-JSON or no 'message' field — that's fine
        }

        if (message != null && !message.isBlank()) {
            logger.info("{} {} → {} ({})", method, endpoint, status, message);
        } else {
            logger.info("{} {} → {}", method, endpoint, status);
        }
    }
}
