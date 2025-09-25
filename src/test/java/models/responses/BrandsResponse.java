package models.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BrandsResponse extends BaseResponse {

    private List<Brand> brands; // maps "brands": [ { "id": ..., "name": ... }, ... ]

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Brand {
        private int id;
        private String name;     // if API uses "brand" instead, rename or use @JsonProperty("brand")

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public List<Brand> getBrands() { return brands; }
    public void setBrands(List<Brand> brands) { this.brands = brands; }
}
