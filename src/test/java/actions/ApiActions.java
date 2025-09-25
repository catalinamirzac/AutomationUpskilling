package actions;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.PropertiesUtil;

import java.util.Map;

import static io.restassured.RestAssured.given;

public final class ApiActions {

    private static final String BASE_URL = getBaseUrl();

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
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .when()
                .get(endpoint)
                .then()
                .log().body()
                .extract().response();
    }

    // ---------- POST ----------
    /** POST with no body */
    public static Response post(String endpoint) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.URLENC)
                .when()
                .post(endpoint)
                .then()
                .log().body()
                .extract().response();
    }

    /** POST with form params */
    public static Response postWithForm(String endpoint, Map<String, String> formParams) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.URLENC)
                .formParams(formParams)
                .when()
                .post(endpoint)
                .then()
                .log().body()
                .extract().response();
    }

    // ---------- PUT ----------
    public static Response put(String endpoint) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .put(endpoint)
                .then()
                .log().body()
                .extract().response();
    }
}
