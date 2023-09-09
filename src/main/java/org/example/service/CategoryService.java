package org.example.service;

import com.google.gson.Gson;
import org.example.model.Category;

import java.io.IOException;

public class CategoryService {
    private final ApiService apiService;
    public CategoryService(ApiService apiService){
        this.apiService = apiService;
    }

    public Category[] getCategories() throws IOException{
        String endpoint = "categories";

        String jsonResponse = apiService.doGetRequest(endpoint);

        Gson gson = new Gson();
        return gson.fromJson(jsonResponse, Category[].class);
    }

}
