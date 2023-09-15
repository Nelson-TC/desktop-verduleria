package org.example.view.inventory;

import com.formdev.flatlaf.FlatClientProperties;
import org.example.view.MainMenu;
import org.example.controller.InventoryController;
import org.example.controller.ProductController;
import org.example.model.Product;
import org.example.service.InventoryService;
import org.example.themes.Colors;
import org.example.utils.renderer.CenterRenderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class InventoryView extends JFrame {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private JLabel mainLabel;
    private JTable resultsTable;
    private JTable previewTable;
    private JTextField searchField;
    private JPanel MainPanel;
    private JButton backButton;
    private JPanel titlePanel;
    private JPanel searchPanel;
    private JPanel previewPanel;
    private JLabel previewLabel;
    private JScrollPane resultsScrollPane;
    private JButton inventoryModeButton;
    private JButton cleanPreviewButton;
    private JButton updateStockButton;
    private final DefaultTableModel resultsTableModel;
    private final DefaultTableModel previewTableModel;
    public ProductController productController;
    public InventoryController inventoryController;

    private enum InventoryMode {
        INCREASE,
        DECREASE
    }

    ;

    InventoryMode mode = InventoryMode.INCREASE;

    public InventoryView() {
        this.productController = new ProductController();
        this.inventoryController = new InventoryController();

        setLocationRelativeTo(null);
        MainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        MainPanel.setBackground(new Colors().background);
        setContentPane(MainPanel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        backButton.setBackground(new Colors().main);
        backButton.setForeground(Color.white);
        backButton.addActionListener(e -> {
            MainMenu.getInstance().setVisible(true);
            setVisible(false);
        });

        mainLabel.setForeground(new Colors().main);
        previewLabel.setForeground(new Colors().darkerGreen);

        titlePanel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        titlePanel.setBackground(new Colors().light);

        searchPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        searchPanel.setBackground(new Colors().light);

        previewPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        previewPanel.setBackground(new Colors().light);

        resultsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultsTableModel.addColumn("Codigo");
        resultsTableModel.addColumn("Nombre");
        resultsTableModel.addColumn("P.U");
        resultsTableModel.addColumn("Invent.");
        resultsTableModel.addColumn("Medida");
        resultsTable.setModel(resultsTableModel);

        previewTableModel = new DefaultTableModel();
        previewTableModel.addColumn("");
        previewTableModel.addColumn("Cant.");
        previewTableModel.addColumn("");
        previewTableModel.addColumn("Codigo");
        previewTableModel.addColumn("Nombre");
        previewTableModel.addColumn("");
        previewTable.setModel(previewTableModel);

        previewTable.getColumnModel().getColumn(0).setCellRenderer(new InventoryView.ButtonRenderer("-"));
        previewTable.getColumnModel().getColumn(2).setCellRenderer(new InventoryView.ButtonRenderer("+"));
        previewTable.getColumnModel().getColumn(5).setCellRenderer(new InventoryView.ButtonRenderer("x"));

        previewTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = previewTable.rowAtPoint(e.getPoint());
                int col = previewTable.columnAtPoint(e.getPoint());

                if (row >= 0) {
                    int currentQuantity = (int) previewTable.getValueAt(row, 1);
                    int productId = (int) previewTable.getValueAt(row, 3);
                    if (col == 0) { // Decrease
                        if (currentQuantity > 1) {
                            int newQty = currentQuantity - 1;
                            previewTableModel.setValueAt(newQty, row, 1);
                            previewTableModel.fireTableDataChanged();
                        }

                    } else if (col == 2) {// Increase
                        Product product = productController.getProductById(productId);
                        int newQty = currentQuantity + 1;
                        if (mode == InventoryMode.INCREASE || (newQty <= product.getStock() && mode == InventoryMode.DECREASE)) {
                            previewTableModel.setValueAt(newQty, row, 1);
                            previewTableModel.fireTableDataChanged();
                        }
                    } else if (col == 5) { // Remove
                        previewTableModel.removeRow(row);
                        previewTableModel.fireTableDataChanged();
                    }
                }
            }
        });

        /* styles for the table header (results) */
        JTableHeader header = resultsTable.getTableHeader();
        Font headerFont = header.getFont();
        Font boldHeaderFont = new Font(headerFont.getFontName(), Font.BOLD, headerFont.getSize());
        header.setFont(boldHeaderFont);
        header.setBackground(new Colors().main);
        header.setForeground(Color.white);
        resultsTable.setBackground(new Colors().light);
        resultsTable.setSelectionBackground(new Colors().amber);

        /* styles for the table header (preview) */
        JTableHeader headerPreview = previewTable.getTableHeader();
        Font headerFontPreview = headerPreview.getFont();
        Font boldHeaderFontPreview = new Font(headerFontPreview.getFontName(), Font.BOLD, headerFont.getSize());
        headerPreview.setFont(boldHeaderFontPreview);
        headerPreview.setBackground(new Colors().green);
        headerPreview.setForeground(Color.white);
        previewTable.setBackground(new Colors().light);

        /* Results Table */
        TableColumn idColumnResultsTable = resultsTable.getColumnModel().getColumn(0);
        idColumnResultsTable.setMaxWidth(50);
        CenterRenderer centerRenderer = new CenterRenderer();
        idColumnResultsTable.setCellRenderer(centerRenderer);

        TableColumn priceColumnResultsTable = resultsTable.getColumnModel().getColumn(2);
        priceColumnResultsTable.setMaxWidth(100);

        TableColumn inventoryColumnResultsTable = resultsTable.getColumnModel().getColumn(3);
        inventoryColumnResultsTable.setMaxWidth(60);

        TableColumn measurementColumnResultsTable = resultsTable.getColumnModel().getColumn(4);
        measurementColumnResultsTable.setMaxWidth(200);

        /* Preview Table */
        TableColumn decreaseButtonColumnPreviewTable = previewTable.getColumnModel().getColumn(0);
        decreaseButtonColumnPreviewTable.setMaxWidth(25);

        TableColumn qtyColumnPreviewTable = previewTable.getColumnModel().getColumn(1);
        qtyColumnPreviewTable.setMaxWidth(50);
        qtyColumnPreviewTable.setCellRenderer(centerRenderer);

        TableColumn increaseButtonColumnPreviewTable = previewTable.getColumnModel().getColumn(2);
        increaseButtonColumnPreviewTable.setMaxWidth(25);

        TableColumn idColumnPreviewTable = previewTable.getColumnModel().getColumn(3);
        idColumnPreviewTable.setMaxWidth(50);
        idColumnPreviewTable.setCellRenderer(centerRenderer);

        TableColumn removeButtonColumnPreviewTable = previewTable.getColumnModel().getColumn(5);
        removeButtonColumnPreviewTable.setMaxWidth(25);

        resultsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = resultsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        Object id = resultsTable.getValueAt(selectedRow, 0);
                        Product productToAdd = productController.getProductById((int) (id));
                        addPreviewProduct(productToAdd);
                    }
                }
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterResultsTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterResultsTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterResultsTable();
            }
        });

        cleanPreviewButton.setBackground(new Colors().red);
        cleanPreviewButton.setForeground(Color.white);

        cleanPreviewButton.addActionListener(e -> {
            cleanPreviewTable();
        });

        updateStockButton.setBackground(new Colors().green);
        updateStockButton.setForeground(Color.white);

        inventoryModeButton.setBackground(new Colors().amber);
        inventoryModeButton.setForeground(Color.white);

        filterResultsTable();
        updateStockButton.addActionListener(e -> {
            updateStock();
        });
        inventoryModeButton.addActionListener(e -> {
            cleanPreviewTable();
            if (mode == InventoryMode.INCREASE) {
                mode = InventoryMode.DECREASE;
                inventoryModeButton.setText("Disminuir inventario");
            } else {
                mode = InventoryMode.INCREASE;
                inventoryModeButton.setText("Aumentar inventario");
            }
        });
    }

    public void filterResultsTable() {
        resultsTableModel.setRowCount(0);

        String searchText = searchField.getText().toLowerCase().trim();
        Product[] products = productController.searchProducts(searchText);
        if (products.length == 0) {
            resultsTableModel.addRow(new Object[]{"NA", "No se encontraron resultados", "", "", ""});
        } else {
            for (Product product : products) {
                resultsTableModel.addRow(new Object[]{
                        product.getId(),
                        product.getName(),
                        product.getUnitPrice(),
                        product.getStock(),
                        product.getUnitMeasurement()
                });
            }
        }
        resultsTableModel.fireTableDataChanged();
    }

    private void addPreviewProduct(Product product) {
        int idColumnIndex = 3;
        int productStock = product.getStock();

        for (int row = 0; row < previewTableModel.getRowCount(); row++) {
            Object idValue = previewTableModel.getValueAt(row, idColumnIndex);

            if (idValue != null && idValue.equals(product.getId())) {
                int newQty = (int) previewTableModel.getValueAt(row, 1) + 1;
                if (mode == InventoryMode.INCREASE || (newQty <= productStock && mode == InventoryMode.DECREASE)) {
                    int currentQuantity = (int) previewTableModel.getValueAt(row, 1);
                    previewTableModel.setValueAt(newQty, row, 1);
                    previewTableModel.fireTableDataChanged();
                }
                return;
            }
        }

        if (mode == InventoryMode.INCREASE || (productStock > 0 && mode == InventoryMode.DECREASE)) {
            previewTableModel.addRow(new Object[]{
                    null,
                    1,
                    null,
                    product.getId(),
                    product.getName(),
                    product.getUnitPrice(),
                    product.getUnitPrice()
            });
        }

        previewTableModel.fireTableDataChanged();
    }

    public void cleanPreviewTable() {
        previewTableModel.setNumRows(0);
        previewTableModel.fireTableDataChanged();
    }

    private void updateStock() {
        if (previewTable.getRowCount() > 0) {
            List<InventoryService.InventoryItem> items = new ArrayList<>();
            for (int i = 0; i < previewTable.getRowCount(); i++) {
                int id = (int) previewTable.getValueAt(i, 3);
                int quantity = (int) previewTable.getValueAt(i, 1);

                InventoryService.InventoryItem item = new InventoryService.InventoryItem(id, quantity);
                items.add(item);
            }
            if (mode == InventoryMode.INCREASE) {
                if (inventoryController.increaseInventory(items)) {
                    cleanPreviewTable();
                }
            } else if (mode == InventoryMode.DECREASE) {
                if (inventoryController.decreaseInventory(items)) {
                    cleanPreviewTable();
                }

            }
            filterResultsTable();
        } else {
            JOptionPane.showMessageDialog(null, "No es posible continuar sin productos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setOpaque(true);
            setText(text);
            if (text == "x") {
                setForeground(Color.white);
                setBackground(new Colors().red);
            } else {
                setForeground(Color.white);
                setBackground(new Colors().green);
            }
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
}
