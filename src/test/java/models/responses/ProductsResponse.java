package models.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsResponse extends BaseResponse {

    private List<Product> products; // maps "products": [ { ... }, ... ]

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Product {
        private int id;          // adjust to your real JSON
        private String name;
        private String brand;
        private double price;    // if API sends String price, change to String

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getBrand() { return brand; }
        public void setBrand(String brand) { this.brand = brand; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}
