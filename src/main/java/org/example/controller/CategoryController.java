package org.example.controller;

import org.example.model.Category;
import org.example.model.Product;
import org.example.service.ApiService;
import org.example.service.CategoryService;
import org.example.view.category.CategoryView;
import org.example.view.category.CreateCategoryDialog;
import org.example.view.category.EditCategoryDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.IOException;

public class CategoryController {
    private CategoryService categoryService;

    public CategoryController() {
        this.categoryService = new CategoryService(new ApiService());
    }

    public Category[] searchCategories(String searchText) {
        try {
            return categoryService.getCategoriesByName(searchText);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Category getCategoryById(int categoryId){
        try{
            return categoryService.getCategoryById(categoryId);
        }catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public void storeCategory(Category categoryToCreate) {
        try {
            categoryService.createCategory(categoryToCreate);
            JOptionPane.showMessageDialog(null, "Categoria creada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al crear la categoria: " + ex.getMessage());
        }
    }

    public void updateCategory(Category categoryToUpdate) {
        try {
            categoryService.updateCategory(categoryToUpdate.getId(), categoryToUpdate);
            JOptionPane.showMessageDialog(null, "Categoría actualizada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar la categoria: " + ex.getMessage());
        }
    }

    public void deleteCategoryById(int categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            JOptionPane.showMessageDialog(null, "Categoria eliminado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar la categoria: " + ex.getMessage());
        }
    }

}
