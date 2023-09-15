package org.example.service;

import okhttp3.*;
import org.example.utils.SessionManager;

import java.io.IOException;

public class ApiService {
    private final OkHttpClient client = new OkHttpClient();
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public String doGetRequest(String endpoint) throws IOException {
        String baseUrl = "http://localhost:8080/api/";

        String apiUrl = baseUrl + endpoint;

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + SessionManager.getJwtToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Error en la solicitud HTTP. Código de respuesta: " + response.code());
            }
        }
    }
    public String doUnauthorizedPostRequest(String endpoint, String jsonBody) throws IOException {
        String baseUrl = "http://localhost:8080/api/";

        String apiUrl = baseUrl + endpoint;

        RequestBody requestBody = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Error en la solicitud. Código del error: " + response.code());
            }
        }
    }
    public String doPostRequest(String endpoint, String jsonBody) throws IOException {
        String baseUrl = "http://localhost:8080/api/";

        String apiUrl = baseUrl + endpoint;

        RequestBody requestBody = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + SessionManager.getJwtToken())
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Error en la solicitud. Código del error: " + response.code());
            }
        }
    }


    public String doPutRequest(String endpoint, String jsonBody) throws IOException {
        String baseUrl = "http://localhost:8080/api/";

        String apiUrl = baseUrl + endpoint;

        RequestBody requestBody = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + SessionManager.getJwtToken())
                .url(apiUrl)
                .put(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Error en la solicitud HTTP. Código de respuesta: " + response.code());
            }
        }
    }

    public String doDeleteRequest(String endpoint) throws IOException {
        String baseUrl = "http://localhost:8080/api/";

        String apiUrl = baseUrl + endpoint;

        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + SessionManager.getJwtToken())
                .url(apiUrl)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Error en la solicitud HTTP. Código de respuesta: " + response.code());
            }
        }
    }


}
