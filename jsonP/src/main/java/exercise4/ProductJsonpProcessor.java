package exercise4;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductJsonpProcessor {
    private List<Product> products;

    public ProductJsonpProcessor(String filePath) throws Exception {
        products = new ArrayList<>();
        
        try (JsonReader reader = Json.createReader(new FileInputStream(filePath))) {
            JsonArray jsonArray = reader.readArray();
            
            for (JsonObject obj : jsonArray.getValuesAs(JsonObject.class)) {
                Product p = new Product();
                p.setSku(obj.getInt("sku"));
                p.setName(obj.getString("name"));
                p.setType(obj.getString("type"));
                
                p.setPrice(obj.getJsonNumber("price").doubleValue());
                p.setUpc(obj.getString("upc"));
                p.setShipping(obj.getJsonNumber("shipping").doubleValue());
                p.setDescription(obj.getString("description"));
                p.setManufacturer(obj.getString("manufacturer"));
                p.setModel(obj.getString("model"));
                p.setUrl(obj.getString("url"));
                p.setImage(obj.getString("image"));

                List<Category> categories = new ArrayList<>();
                JsonArray catArray = obj.getJsonArray("category");
                for (JsonObject catObj : catArray.getValuesAs(JsonObject.class)) {
                    Category c = new Category();
                    c.setId(catObj.getString("id"));
                    c.setName(catObj.getString("name"));
                    categories.add(c);
                }
                p.setCategory(categories);

                products.add(p);
            }
        }
    }

    public Product findBySku(int sku) {
        for (Product p : products) {
            if (p.getSku() == sku) {
                return p;
            }
        }
        return null;
    }

    public List<Product> findByPrice(double from, double to) {
        return products.stream()
                .filter(p -> p.getPrice() >= from && p.getPrice() <= to)
                .collect(Collectors.toList());
    }

    public List<Product> findByManufacturer(String manufacturerName) {
        return products.stream()
                .filter(p -> p.getManufacturer().equalsIgnoreCase(manufacturerName))
                .collect(Collectors.toList());
    }
}