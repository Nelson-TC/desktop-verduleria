package org.example.service;

import com.google.gson.Gson;
import org.example.model.Invoice;

import java.io.IOException;

public class InvoiceService {
    private final ApiService apiService;
    private final Gson gson;

    public InvoiceService(ApiService apiService) {
        this.apiService = apiService;
        this.gson = new Gson();
    }
    public Invoice[] getInvoices() throws IOException{
        String endpoint = "invoices";
        String jsonResponse = apiService.doGetRequest(endpoint);
        return gson.fromJson(jsonResponse, Invoice[].class);
    }

    public Invoice getInvoiceById(int invoiceId) throws IOException {
        String endpoint = "invoices/" + invoiceId;
        String jsonResponse = apiService.doGetRequest(endpoint);
        return gson.fromJson(jsonResponse, Invoice.class);
    }

    public Invoice createInvoice(int saleId, Invoice createdInvoice) throws IOException {
        String endpoint = "invoices/create/" + saleId;
        String jsonBody = gson.toJson(createdInvoice);
        String jsonResponse = apiService.doPostRequest(endpoint, jsonBody);
        return gson.fromJson(jsonResponse, Invoice.class);
    }
}
