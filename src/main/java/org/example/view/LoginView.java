package org.example.view;

import com.formdev.flatlaf.FlatClientProperties;
import org.example.controller.AuthController;
import org.example.themes.Colors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginView extends JFrame {
    private JPanel MainPanel;
    private JLabel mainLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPanel LoginPanel;
    private JButton loginButton;
    private AuthController authController;

    private static LoginView loginViewInstance;
    public LoginView(){
        this.authController = new AuthController();
        loginViewInstance = this;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;


        int screenHeight10 = (int)(screenHeight * 0.1);
        int screenHeight20 = (int)(screenHeight * 0.2);

        setTitle("Inicio de sesión");
        MainPanel.setBorder(new EmptyBorder(screenHeight10, screenHeight20, screenHeight10, screenHeight20));
        MainPanel.setBackground(new Colors().background);
        setContentPane(MainPanel);

        LoginPanel.setBorder(new EmptyBorder(10,screenHeight10,10,screenHeight10));
        LoginPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        LoginPanel.setBackground(Color.white);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        int width = (int) (screenWidth * 0.6);
        int height = (int) (screenHeight * 0.6);
        setSize(width, height);
        setLocationRelativeTo(null);

        passwordField.setMargin(new Insets(8,6,8,6));
        passwordField.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        usernameField.setMargin(new Insets(8,6,8,6));
        usernameField.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        loginButton.setBackground(new Colors().main);
        loginButton.setForeground(Color.white);
        loginButton.addActionListener(e -> {
           login();
        });


        setVisible(true);
    }

    private void login(){
        String username = String.valueOf(usernameField.getText());
        String password = String.valueOf(passwordField.getPassword());
        boolean isAuthenticated = authController.login(username, password);
        if(isAuthenticated){
            new MainMenu();
            loginViewInstance.dispose();
        }else{
            JOptionPane.showMessageDialog(null, "Inicio de sesión fallido. Verifica tus credenciales e intentálo nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginView::new);
    }

}
