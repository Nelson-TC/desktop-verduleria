package org.example.view.category;

import org.example.MainMenu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CategoryView extends JFrame {
    private JPanel MainPanel;
    private JButton backButton;

    public CategoryView() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        /* Padding */
        MainPanel.setBorder(new EmptyBorder(10,10,10,10));

        setTitle("Categorias");
        setContentPane(MainPanel);


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        int width = (int) (screenWidth * 0.6);
        int height = (int) (screenHeight * 0.6);
        setSize(width, height);
        setLocationRelativeTo(null);

        setVisible(true);
        backButton.addActionListener(e -> {
            MainMenu.getInstance().setVisible(true);
            setVisible(false);
        });
    }
}
