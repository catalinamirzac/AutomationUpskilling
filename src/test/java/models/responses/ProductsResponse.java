package models.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsResponse extends BaseResponse {

    private List<Product> products;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Product {
        private int id;
        private String name;
        private String brand;
        // ⚠️ Keep as String because API returns "Rs. 500"
        private String price;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getBrand() { return brand; }
        public void setBrand(String brand) { this.brand = brand; }

        public String getPrice() { return price; }
        public void setPrice(String price) { this.price = price; }
    }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}
