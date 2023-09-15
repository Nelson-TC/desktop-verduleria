package org.example.view;

import org.example.themes.Colors;
import org.example.view.category.CategoryView;
import org.example.view.invoice.InvoiceView;
import org.example.view.product.ProductView;
import org.example.view.sale.CreateSaleView;
import org.example.view.inventory.InventoryView;
import org.example.view.sale.SaleView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainMenu extends JFrame {

    private CategoryView categoryView;

    private ProductView productView;

    private InventoryView inventoryView;

    private CreateSaleView createSaleView;

    private JPanel MainPanel;
    private JLabel TextoAModificar;
    private JButton categoriesButton;
    private JButton productsButton;
    private JButton stockButton;
    private JButton createSaleButton;
    private JButton salesButton;
    private JButton invoicesButton;

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

        categoriesButton.setPreferredSize(new Dimension((int) (screenWidth * 0.1), (int) (screenHeight * 0.1)));
        productsButton.setPreferredSize(new Dimension((int) (screenWidth * 0.1), (int) (screenHeight * 0.1)));
        stockButton.setPreferredSize(new Dimension((int) (screenWidth * 0.1), (int) (screenHeight * 0.1)));
        createSaleButton.setPreferredSize(new Dimension((int) (screenWidth * 0.1), (int) (screenHeight * 0.1)));
        salesButton.setPreferredSize(new Dimension((int) (screenWidth * 0.1), (int) (screenHeight * 0.1)));
        invoicesButton.setPreferredSize(new Dimension((int) (screenWidth * 0.1), (int) (screenHeight * 0.1)));

        categoriesButton.setBackground(new Colors().main);
        productsButton.setBackground(new Colors().main);
        stockButton.setBackground(new Colors().main);
        createSaleButton.setBackground(new Colors().main);
        salesButton.setBackground(new Colors().main);
        invoicesButton.setBackground(new Colors().main);

        categoriesButton.setForeground(Color.white);
        productsButton.setForeground(Color.white);
        stockButton.setForeground(Color.white);
        createSaleButton.setForeground(Color.white);
        salesButton.setForeground(Color.white);
        invoicesButton.setForeground(Color.white);

        /* Padding */
        MainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        MainPanel.setBackground(new Colors().background);

        setTitle("Menu principal");
        setContentPane(MainPanel);


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        int width = (int) (screenWidth * 0.6);
        int height = (int) (screenHeight * 0.6);
        setSize(width, height);
        setLocationRelativeTo(null);

        setVisible(true);

        categoriesButton.addActionListener(e -> {
            CategoryView newCategoryView = new CategoryView();
            setVisible(false);
            newCategoryView.setVisible(true);
        });
        productsButton.addActionListener(e -> {
            ProductView newProductView = new ProductView();
            setVisible(false);
            newProductView.setVisible(true);
        });
        stockButton.addActionListener(e -> {
            InventoryView newInventoryView = new InventoryView();
            setVisible(false);
            newInventoryView.setVisible(true);
        });
        createSaleButton.addActionListener(e -> {
            CreateSaleView newCreateSaleView = new CreateSaleView();
            setVisible(false);
            newCreateSaleView.setVisible(true);
        });

        salesButton.addActionListener(e -> {
            SaleView newSaleView = new SaleView();
            setVisible(false);
            newSaleView.setVisible(true);
        });

        invoicesButton.addActionListener(e -> {
            InvoiceView newInvoiceView = new InvoiceView();
            setVisible(false);
            newInvoiceView.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
    }

}
