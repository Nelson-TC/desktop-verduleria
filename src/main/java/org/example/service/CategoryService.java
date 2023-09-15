package org.example.service;

import com.google.gson.Gson;
import org.example.model.Category;
import org.example.model.Product;

import java.io.IOException;

public class CategoryService {
    private final ApiService apiService;

    public CategoryService(ApiService apiService) {
        this.apiService = apiService;
    }

    public Category[] getCategories() throws IOException {
        String endpoint = "categories";

        String jsonResponse = apiService.doGetRequest(endpoint);

        Gson gson = new Gson();
        return gson.fromJson(jsonResponse, Category[].class);
    }

    public Category[] getCategoriesByName(String name) throws IOException{
        String endpoint = "categories/search?search_query="+name;
        String jsonResponse = apiService.doGetRequest(endpoint);

        Gson gson = new Gson();
        return gson.fromJson(jsonResponse, Category[].class);
    }

    public Category getCategoryById(int categoryId) throws IOException {
        String endpoint = "categories/" + categoryId;
        String jsonResponse = apiService.doGetRequest(endpoint);
        Gson gson = new Gson();
        return gson.fromJson(jsonResponse, Category.class);
    }

    public void updateCategory(int categoryId, Category updatedCategory) throws IOException {
        String endpoint = "categories/" + categoryId;
        Gson gson = new Gson();
        String jsonBody = gson.toJson(updatedCategory);

        apiService.doPutRequest(endpoint, jsonBody);
    }

    public void createCategory(Category createdCategory) throws IOException {
        String endpoint = "categories";
        Gson gson = new Gson();
        String jsonBody = gson.toJson(createdCategory);

        apiService.doPostRequest(endpoint, jsonBody);
    }

    public void deleteCategory(int categoryId) throws IOException {
        String endpoint = "categories/" + categoryId;
        Gson gson = new Gson();
        apiService.doDeleteRequest(endpoint);
    }


}
