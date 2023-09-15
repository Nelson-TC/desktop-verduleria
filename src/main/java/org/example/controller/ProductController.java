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
        private ProductService productService;

    public ProductController() {
        this.productService = new ProductService(new ApiService());
    }

    public Product[] searchProducts(String searchTerm) {
        try {
            return productService.getProductsByName(searchTerm);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al consultar los productos: " + ex.getMessage());
            return null;
        }
    }

    public Product getProductById(int productId) {
        try {
            return productService.getProductById(productId);
        } catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public void updateProduct(Product productToUpdate) {
        try {
            productService.updateProduct(productToUpdate.getId(), productToUpdate);
            JOptionPane.showMessageDialog(null, "Producto actualizado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el producto: " + ex.getMessage());
        }
    }

    public void storeProduct(Product productToCreate) {
        try {
            productService.createProduct(productToCreate);
            JOptionPane.showMessageDialog(null, "Producto creado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al crear el producto: " + ex.getMessage());
        }
    }

    public void deleteProductById(int productId) {
        try {
            productService.deleteProduct(productId);
            JOptionPane.showMessageDialog(null, "Producto eliminado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + ex.getMessage());
        }
    }
}
