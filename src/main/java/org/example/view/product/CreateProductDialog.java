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

public class CreateProductDialog extends JDialog {
    private final ProductView parentView;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameField;
    private JFormattedTextField unitPriceFormattedField;
    private JComboBox unitMeasurementComboBox;
    private JComboBox categoryComboBox;

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

    public void showCreateDialog() {
        setVisible(true);
    }

    public CreateProductDialog(ProductView parentView) {
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
        categoryComboBox.setRenderer(new CreateProductDialog.CategoryComboBoxRenderer());
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
        String name = nameField.getText().trim();
        double unitPrice = -1;
        if (unitPriceFormattedField.getValue() != null) {
            unitPrice = (Double) unitPriceFormattedField.getValue();
        }
        String unitMeasurement = (String) unitMeasurementComboBox.getSelectedItem();
        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
        int categoryId = selectedCategory.getId();

        Product productToCreate = new Product();

        productToCreate.setId(-1); //Invalid id for the post request
        productToCreate.setName(name);
        productToCreate.setStock(0);
        productToCreate.setUnitPrice(unitPrice);
        productToCreate.setUnitMeasurement(unitMeasurement);
        productToCreate.setCategoryId(categoryId);

        if (name == null || name.isEmpty() || unitPrice < 0) {
            JOptionPane.showMessageDialog(this, "Asegurate de llenar todos los campos correctamente", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            setVisible(false);
            parentView.controller.storeProduct(productToCreate);
            dispose();
        }

    }

    private void onCancel() {
        dispose();
    }
}
