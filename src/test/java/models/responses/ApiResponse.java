package models.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {
    private int responseCode;
    private String message;
    private List<Map<String, Object>> products;
    private List<Map<String, Object>> brands;

    public int getResponseCode() { return responseCode; }
    public String getMessage() { return message; }

    public List<Map<String, Object>> getProducts() {
        return products == null ? Collections.emptyList() : products;
    }
    public List<Map<String, Object>> getBrands() {
        return brands == null ? Collections.emptyList() : brands;
    }
}

