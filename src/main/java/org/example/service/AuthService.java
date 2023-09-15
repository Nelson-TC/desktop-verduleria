package org.example.service;

import com.google.gson.Gson;

import java.io.IOException;

public class AuthService {
    private final ApiService apiService;

    public AuthService(ApiService apiService) {
        this.apiService = apiService;
    }

    private static class AuthRequest {
        private final String username;
        private final String password;

        AuthRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    private static class AuthResponse {
        private String token;

        String getToken() {
            return token;
        }
    }


    public String authenticate(String username, String password) throws IOException {
        String endpoint = "auth/authenticate";
        Gson gson = new Gson();
        AuthRequest authRequest = new AuthRequest(username, password);
        String jsonBody = gson.toJson(authRequest);
        String jsonResponse = apiService.doUnauthorizedPostRequest(endpoint, jsonBody);
        AuthResponse authResponse = gson.fromJson(jsonResponse, AuthResponse.class);
        String jwtToken = authResponse.getToken();
        return jwtToken;
    }
}
