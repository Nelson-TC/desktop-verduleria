package org.example.view.product;

import org.example.model.Category;
import org.example.model.Product;
import org.example.service.ApiService;
import org.example.service.CategoryService;
import org.example.utils.MeasurementUtils;
import org.example.utils.formatter.DoubleFormatterFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class EditProductDialog extends JDialog {
    private final ProductView parentView;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameField;
    private JFormattedTextField unitPriceFormattedField;
    private JComboBox unitMeasurementComboBox;
    private JComboBox categoryComboBox;

    private Product productToEdit;


    private static class CategoryComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Category categoryItem) {
                setText(categoryItem.getName());
            }

            return this;
        }
    }

    private Category getCategoryByCategoryId(int categoryId) {
        DefaultComboBoxModel<Category> model = (DefaultComboBoxModel<Category>) categoryComboBox.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            Category category = model.getElementAt(i);
            if (category.getId() == categoryId) {
                return category;
            }
        }
        return null;
    }

    public void showEditDialog(Product product) {
        productToEdit = product;
        nameField.setText(product.getName());
        unitPriceFormattedField.setValue(product.getUnitPrice());
        String productUnitMeasurement = product.getUnitMeasurement();
        for (int i = 0; i < unitMeasurementComboBox.getItemCount(); i++) {
            String item = (String) unitMeasurementComboBox.getItemAt(i);
            if (item.equals(productUnitMeasurement)) {
                unitMeasurementComboBox.setSelectedIndex(i);
                break;
            }
        }

        Category selectedCategory = getCategoryByCategoryId(product.getCategoryId());
        categoryComboBox.setSelectedItem(selectedCategory);
        setVisible(true);
    }

    public EditProductDialog(ProductView parentView) {
        this.parentView = parentView;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        setSize((int) (screenHeight * 0.5), (int) (screenHeight * 0.5));
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        setLocationRelativeTo(null);


        JFormattedTextField.AbstractFormatterFactory newFactory = DoubleFormatterFactory.createDoubleFormatterFactory();

        unitPriceFormattedField.setFormatterFactory(newFactory);

        /* Set the unit measurements  */
        DefaultComboBoxModel<String> comboBoxUnitMeasurementModel = new DefaultComboBoxModel<>();
        for (MeasurementUtils.UnitMeasurement measurement : MeasurementUtils.UnitMeasurement.values()) {
            comboBoxUnitMeasurementModel.addElement(measurement.getDisplayName());
        }
        unitMeasurementComboBox.setModel(comboBoxUnitMeasurementModel);

        /* Set the categories to the renderer for comboBox */
        categoryComboBox.setRenderer(new CategoryComboBoxRenderer());
        ApiService apiService = new ApiService();
        CategoryService categoryService = new CategoryService(apiService);
        Category[] categories = null;
        try {
            categories = categoryService.getCategories();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (categories != null) {
            DefaultComboBoxModel<Category> comboBoxModel = new DefaultComboBoxModel<>();
            for (Category category : categories) {
                comboBoxModel.addElement(category);
            }
            categoryComboBox.setModel(comboBoxModel);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar las categorías.");
        }
    }

    private void onOK() {
        String editedName = nameField.getText();
        double editedUnitPrice = (Double) unitPriceFormattedField.getValue();
        String editedUnitMeasurement = (String) unitMeasurementComboBox.getSelectedItem();
        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
        int editedCategoryId = selectedCategory.getId();

        Product productToUpdate = new Product();

        productToUpdate.setId(productToEdit.getId());
        productToUpdate.setName(editedName);
        productToUpdate.setStock(productToEdit.getStock());
        productToUpdate.setUnitPrice(editedUnitPrice);
        productToUpdate.setUnitMeasurement(editedUnitMeasurement);
        productToUpdate.setCategoryId(editedCategoryId);

        if (editedName == null || editedName.isEmpty() || editedUnitPrice < 0) {
            JOptionPane.showMessageDialog(this, "Asegurate de llenar todos los campos correctamente", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            setVisible(false);
            parentView.controller.updateProduct(productToUpdate);
            dispose();
        }
    }

    private void onCancel() {
        dispose();
    }
}
