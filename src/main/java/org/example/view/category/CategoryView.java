package org.example.view.category;

import com.formdev.flatlaf.FlatClientProperties;
import org.example.view.MainMenu;
import org.example.controller.CategoryController;
import org.example.model.Category;
import org.example.themes.Colors;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CategoryView extends JFrame {
    private final CategoryView self = this;

    private JPanel MainPanel;

    public void setSearchFieldText(String text) {
        this.searchField.setText(text);
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getCreateCategoryButton() {
        return createCategoryButton;
    }

    public JTable getCategoryTable() {
        return categoryTable;
    }

    private JButton backButton;
    private JPanel titlePanel;
    private JTextField searchField;
    private JLabel mainLabel;
    private JButton createCategoryButton;
    private JScrollPane tablePane;
    private final DefaultTableModel tableModel;
    private JTable categoryTable;

    public CategoryController controller;

    public CategoryView() {
        this.controller = new CategoryController();

        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        /* Padding */
        MainPanel.setBorder(new EmptyBorder(10,10,10,10));

        setTitle("Categorias");
        MainPanel.setBackground(new Colors().background);
        setContentPane(MainPanel);

        /* Style for the create product button */
        createCategoryButton.setFocusPainted(false);
        createCategoryButton.setForeground(Color.white);
        createCategoryButton.setBackground(new Colors().green);

            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Nombre");
            tableModel.addColumn("");
            tableModel.addColumn("");
            categoryTable.setModel(tableModel);

        /* styles for the table header */
        JTableHeader header = categoryTable.getTableHeader();
        Font headerFont = header.getFont();
        Font boldHeaderFont = new Font(headerFont.getFontName(), Font.BOLD, headerFont.getSize());
        header.setFont(boldHeaderFont);
        header.setBackground(new Colors().main);
        header.setForeground(Color.white);

        categoryTable.setBackground(new Colors().light);

        /* Render the edit & delete columns */
        categoryTable.getColumnModel().getColumn(2).setCellRenderer(new CategoryView.ButtonRenderer("Editar"));
        categoryTable.getColumnModel().getColumn(3).setCellRenderer(new CategoryView.ButtonRenderer("Eliminar"));

        /* Set align & size of the columns */
        TableColumn idColumn = categoryTable.getColumnModel().getColumn(0);
        idColumn.setMaxWidth(50);
        CategoryView.CenterRenderer centerRenderer = new CategoryView.CenterRenderer();
        idColumn.setCellRenderer(centerRenderer);


        TableColumn editColumn = categoryTable.getColumnModel().getColumn(2);
        editColumn.setMinWidth(80);
        editColumn.setMaxWidth(80);

        TableColumn deleteColumn = categoryTable.getColumnModel().getColumn(3);
        deleteColumn.setMinWidth(90);
        deleteColumn.setMaxWidth(90);

        categoryTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = categoryTable.rowAtPoint(e.getPoint());
                int col = categoryTable.columnAtPoint(e.getPoint());

                if (col == 2 && !isNoResultsRow(row)) { // Edit
                    if (e.getClickCount() == 1) {
                        int categoryId = (int) categoryTable.getValueAt(row, 0);
                        Category categoryToEdit = controller.getCategoryById(categoryId);
                        EditCategoryDialog editCategoryDialog = new EditCategoryDialog(self);
                        editCategoryDialog.showEditDialog(categoryToEdit);
                        filterTable();
                    }
                } else if (col == 3 && !isNoResultsRow(row)) { // Delete
                    if (e.getClickCount() == 1) {
                        int productId = (int) categoryTable.getValueAt(row, 0);
                        int option = JOptionPane.showConfirmDialog(
                                CategoryView.this,
                                "¿Estás seguro de eliminar este producto?",
                                "Confirmación de eliminación",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (option == JOptionPane.YES_OPTION) {
                            controller.deleteCategoryById(productId);
                        }
                    }
                }
            }
        });

        createCategoryButton.addActionListener(e -> {
            CreateCategoryDialog createCategoryDialog = new CreateCategoryDialog(this);
            createCategoryDialog.showCreateDialog();
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
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

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        int width = (int) (screenWidth * 0.6);
        int height = (int) (screenHeight * 0.6);
        setSize(width, height);
        setLocationRelativeTo(null);


        titlePanel.setBackground(new Colors().light);
        titlePanel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        mainLabel.setForeground(new Colors().main);

        backButton.setBackground(new Colors().main);
        backButton.setForeground(Color.white);

        setVisible(true);
        backButton.addActionListener(e -> {
            MainMenu.getInstance().setVisible(true);
            setVisible(false);
        });

        filterTable();
    }
    public void filterTable() {
        tableModel.setRowCount(0);

        String searchText = searchField.getText().toLowerCase().trim();
        Category[] categories = controller.searchCategories(searchText);
        if (categories.length == 0) {
            tableModel.addRow(new Object[]{"NA", "No se encontraron resultados", "", "", ""});
        } else {
            for (Category category : categories) {
                tableModel.addRow(new Object[]{
                        category.getId(),
                        category.getName(),
                });
            }
        }

        tableModel.fireTableDataChanged();
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setOpaque(true);
            setText(text);
            if (text == "Editar") {
                setForeground(Color.white);
                setBackground(new Colors().amber);
            } else if (text == "Eliminar") {
                setForeground(Color.white);
                setBackground(new Colors().red);
            }
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isNoResultsRow(row)) {
                return new JLabel("");
            }
            return this;
        }
    }

    class CenterRenderer extends DefaultTableCellRenderer {
        public CenterRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    private boolean isNoResultsRow(int row) {
        Object idValue = categoryTable.getValueAt(row, 0);
        return idValue instanceof String && "NA".equals(idValue);
    }

}
