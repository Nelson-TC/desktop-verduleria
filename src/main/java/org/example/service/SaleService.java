package org.example.service;

import com.google.gson.Gson;
import org.example.model.Sale;

import java.io.IOException;
import java.util.List;

public class SaleService {
    private final ApiService apiService;
    private final Gson gson;

    public SaleService(ApiService apiService) {
        this.apiService = apiService;
        this.gson = new Gson();
    }
    public Sale[] getSales() throws IOException{
        String endpoint ="sales";
        String jsonResponse = apiService.doGetRequest(endpoint);
        return gson.fromJson(jsonResponse, Sale[].class);
    }

    public Sale createSale(List<SaleItem> items) throws IOException {
        String endpoint = "sales/create";
        String jsonBody = createJsonRequest(items);
        String jsonResponse = apiService.doPostRequest(endpoint, jsonBody);
        Sale sale = gson.fromJson(jsonResponse, Sale.class);
        return sale;
    }

    public Sale getSaleById(int saleId) throws IOException {
        String endpoint = "sales/" + saleId;
        String jsonResponse = apiService.doGetRequest(endpoint);
        return gson.fromJson(jsonResponse, Sale.class);
    }

    public Sale getSaleByInvoiceId(int invoiceId) throws IOException{
        String endpoint = "sales/by-invoice-id/" + invoiceId;
        String jsonResponse = apiService.doGetRequest(endpoint);
        return gson.fromJson(jsonResponse, Sale.class);
    }

    private String createJsonRequest(List<SaleItem> items) {
        SaleRequest request = new SaleRequest(items);
        return gson.toJson(request);
    }

    public static class SaleItem {
        private final int id;
        private final int quantity;
        private final String name;
        private final double unitPrice;

        public SaleItem(int id, int quantity, String name, double unitPrice) {
            this.id = id;
            this.quantity = quantity;
            this.name = name;
            this.unitPrice = unitPrice;
        }

        public int getId() {
            return id;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getName() {
            return name;
        }

        public double getUnitPrice() {
            return unitPrice;
        }
    }

    public static class SaleRequest {
        private final List<SaleItem> content;

        public SaleRequest(List<SaleItem> content) {
            this.content = content;
        }

        public List<SaleItem> getContent() {
            return content;
        }
    }
}
