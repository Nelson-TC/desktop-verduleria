package org.example.service;

import com.google.gson.Gson;
import org.example.model.Product;

import java.io.IOException;

public class ProductService {
    private final ApiService apiService;

    public ProductService(ApiService apiService) {
        this.apiService = apiService;
    }

    public Product[] getProducts() throws IOException {
        String endpoint = "products";

        String jsonResponse = apiService.doGetRequest(endpoint);

        Gson gson = new Gson();
        return gson.fromJson(jsonResponse, Product[].class);
    }

    public Product getProductById(int productId) throws IOException {
        String endpoint = "products/" + productId;
        String jsonResponse = apiService.doGetRequest(endpoint);
        Gson gson = new Gson();
        return gson.fromJson(jsonResponse, Product.class);
    }

    public Product[] getProductsByName(String name) throws IOException {
        String endpoint = "products/search?search_query=" + name;

        String jsonResponse = apiService.doGetRequest(endpoint);

        Gson gson = new Gson();
        return gson.fromJson(jsonResponse, Product[].class);
    }

    public void updateProduct(int productId, Product updatedProduct) throws IOException {
        String endpoint = "products/" + productId;
        Gson gson = new Gson();
        String jsonBody = gson.toJson(updatedProduct);

        apiService.doPutRequest(endpoint, jsonBody);
    }

    public void createProduct(Product createdProduct) throws IOException {
        String endpoint = "products";
        Gson gson = new Gson();
        String jsonBody = gson.toJson(createdProduct);

        apiService.doPostRequest(endpoint, jsonBody);
    }

    public void deleteProduct(int productId) throws IOException{
        String endpoint = "products/" + productId;
        Gson gson = new Gson();
        apiService.doDeleteRequest(endpoint);
    }


}
