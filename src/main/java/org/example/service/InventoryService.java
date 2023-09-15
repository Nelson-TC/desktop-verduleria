package org.example.service;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

public class InventoryService {
    private final ApiService apiService;
    private final Gson gson;

    public InventoryService (ApiService apiService){
        this.apiService = apiService;
        this.gson = new Gson();
    }

    public void increaseInventory(List<InventoryItem> items) throws IOException {
        String endpoint = "inventory/increase";
        String jsonBody = createJsonRequest(items);
        apiService.doPostRequest(endpoint, jsonBody);
    }

    public void decreaseInventory(List<InventoryItem> items) throws IOException{
        String endpoint = "inventory/decrease";
        String jsonBody = createJsonRequest(items);
        apiService.doPostRequest(endpoint, jsonBody);
    }
    private String createJsonRequest(List<InventoryItem> items) {
        InventoryRequest request = new InventoryRequest(items);
        return gson.toJson(request);
    }
    public static class InventoryItem {
        private final int id;
        private final int quantity;

        public InventoryItem(int id, int quantity) {
            this.id = id;
            this.quantity = quantity;
        }

        public int getId() {
            return id;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    public static class InventoryRequest {
        private final List<InventoryItem> content;

        public InventoryRequest(List<InventoryItem> content) {
            this.content = content;
        }

        public List<InventoryItem> getContent() {
            return content;
        }
    }
}
