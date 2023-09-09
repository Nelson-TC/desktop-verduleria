package org.example;

import org.example.view.category.CategoryView;
import org.example.view.product.ProductView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainMenu extends JFrame {

    private CategoryView categoryView;

    private ProductView productView;

    private JPanel MainPanel;
    private JLabel TextoAModificar;
    private JButton categoriesButton;
    private JButton productsButton;

    private static MainMenu instance;

    public static MainMenu getInstance() {
        if (instance == null) {
            instance = new MainMenu();
        }
        return instance;
    }

    public MainMenu() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        /* Logo config */
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/VerduMarketLogo.png"));
        Image originalImage = originalIcon.getImage();
        int imageWidth = (int) (screenWidth * 0.1);
        int imageHeight = (int) (screenWidth * 0.1);
        Image resizedImage = originalImage.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        TextoAModificar.setIcon(resizedIcon);

        categoriesButton.setPreferredSize(new Dimension((int)(screenWidth*0.1),(int)( screenHeight * 0.1)));
        productsButton.setPreferredSize(new Dimension((int)(screenWidth*0.1),(int)( screenHeight * 0.1)));

        /* Padding */
        MainPanel.setBorder(new EmptyBorder(10,10,10,10));

        setTitle("Menu principal");
        setContentPane(MainPanel);


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        int width = (int) (screenWidth * 0.6);
        int height = (int) (screenHeight * 0.6);
        setSize(width, height);
        setLocationRelativeTo(null);

        setVisible(true);

        categoriesButton.addActionListener(e -> {
            if (categoryView == null) {
                categoryView = new CategoryView();
            }
            setVisible(false);
            categoryView.setVisible(true);
        });
        productsButton.addActionListener(e -> {
            if (productView == null) {
                productView = new ProductView();
            }
            setVisible(false);
            productView.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
    }

}
