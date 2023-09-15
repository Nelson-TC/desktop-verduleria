package org.example.view.category;

import org.example.model.Category;
import org.example.view.product.ProductView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EditCategoryDialog extends JDialog {
    private final CategoryView parentView;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameField;
    private Category categoryToEdit;

    public EditCategoryDialog(CategoryView parentView) {
        this.parentView = parentView;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        setSize((int) (screenHeight * 0.5), (int) (screenHeight * 0.5));
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        setLocationRelativeTo(null);


    }

    public void showEditDialog(Category category) {
        categoryToEdit = category;
        nameField.setText(category.getName());
        setVisible(true);
    }

    private void onOK() {
        String editedName = nameField.getText();

        if (editedName == null || editedName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Asegurate de llenar todos los campos correctamente", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Category categoryToUpdate = new Category();
            categoryToUpdate.setId(categoryToEdit.getId());
            categoryToUpdate.setName(editedName);

            parentView.controller.updateCategory(categoryToUpdate);
            dispose();
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
