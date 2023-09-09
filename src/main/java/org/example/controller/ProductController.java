package org.example.controller;

import org.example.model.Product;
import org.example.service.ApiService;
import org.example.service.ProductService;
import org.example.view.product.CreateProductDialog;
import org.example.view.product.EditProductDialog;
import org.example.view.product.ProductView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.IOException;

public class ProductController {
    private ProductView view;
    private ProductService productService;
    private CreateProductDialog createProductDialog;
    private EditProductDialog editProductDialog;

    public ProductController(ProductView view) {
        this.view = view;
        this.productService = new ProductService(new ApiService());
        this.createProductDialog = new CreateProductDialog(view);
        this.editProductDialog = new EditProductDialog(view);

        view.getCreateProductButton().addActionListener(e -> createProduct());

        view.getPdfButton().addActionListener(e -> exportToPDF());

        view.getExcelButton().addActionListener(e -> exportToExcel());

        view.getCsvButton().addActionListener(e -> exportToCSV());

        view.getSearchField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }

        });
        filterTable();
    }

    private void createProduct() {
        createProductDialog.showCreateDialog();
    }

    private void exportToPDF() {
        try {
            view.exportToPDF();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void exportToExcel() {
        view.exportToExcel();
    }

    private void exportToCSV() {
        view.exportToCSV();
    }

    private void filterTable() {
        String searchText = view.getSearchField().getText().toLowerCase().trim();
        try {
            Product[] filteredProducts = productService.getProductsByName(searchText);
            view.updateTable(filteredProducts);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void refreshTable(Boolean cleanSearch) {
        if (cleanSearch) {
            view.setSearchFieldText("");
        }
        filterTable();
    }

    public void editProductById(int productId) {
        Product productToEdit = null;
        try {
            productToEdit = productService.getProductById(productId);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (productToEdit != null) {
            editProductDialog.showEditDialog(productToEdit);
        }
    }

    public void updateProduct(Product productToUpdate) {
        try {
            productService.updateProduct(productToUpdate.getId(), productToUpdate);
            refreshTable(false);
            JOptionPane.showMessageDialog(view, "Producto actualizado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Error al actualizar el producto: " + ex.getMessage());
        }
    }

    public void storeProduct(Product productToCreate){
        try{
            productService.createProduct(productToCreate);
            refreshTable(false);
            JOptionPane.showMessageDialog(view, "Producto creado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }catch (IOException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Error al crear el producto: " + ex.getMessage());
        }
    }

    public void deleteProductById(int productId) {
        try {
            productService.deleteProduct(productId);
            refreshTable(false);
            JOptionPane.showMessageDialog(view, "Producto eliminado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(view, "Error al eliminar el producto: " + ex.getMessage());
        }
    }
}
