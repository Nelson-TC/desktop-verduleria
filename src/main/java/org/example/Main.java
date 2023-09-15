package org.example;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import org.example.view.LoginView;

public class Main {
    public static void main(String[] args) {
        FlatMacLightLaf.setup();
        new LoginView();
    }
}