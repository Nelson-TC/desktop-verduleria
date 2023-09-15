package org.example.controller;

import org.example.service.ApiService;
import org.example.service.AuthService;
import org.example.utils.SessionManager;

import java.io.IOException;

public class AuthController {
    private AuthService authService;

    public AuthController() {
        this.authService = new AuthService(new ApiService());
    }

    public boolean login(String username, String password){
        try{
            String jwtToken = authService.authenticate(username, password);
            if(jwtToken != null){
                SessionManager.setJwtToken(jwtToken);
                return true;
            }else{
                return false;
            }
        }catch(IOException ex){
            ex.printStackTrace();
            return false;
        }
    }
}
