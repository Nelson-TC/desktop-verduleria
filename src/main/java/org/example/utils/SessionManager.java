package org.example.utils;

public class SessionManager {
    private static String jwtToken;

    public static String getJwtToken() {
        return jwtToken;
    }

    public static void setJwtToken(String token) {
        jwtToken = token;
    }

    public static void clearSession() {
        jwtToken = null;
    }

    public static boolean isAuthenticated() {
        return jwtToken != null && !jwtToken.isEmpty();
    }
}

