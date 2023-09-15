package org.example.view.sale;

import com.formdev.flatlaf.FlatClientProperties;
import org.example.controller.SaleController;
import org.example.model.Sale;
import org.example.service.SaleService;
import org.example.view.MainMenu;
import org.example.controller.ProductController;
import org.example.model.Product;
import org.example.themes.Colors;
import org.example.utils.renderer.CenterRenderer;
import org.example.view.invoice.CreateInvoiceView;

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

public class CreateSaleView extends JFrame {
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
    private JButton cleanPreviewButton;
    private JButton createSaleButton;
    private JLabel subtotalLabel;
    private final DefaultTableModel resultsTableModel;
    private final DefaultTableModel previewTableModel;
    public ProductController productController;
    private SaleController saleController;

    public CreateSaleView() {
        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");

        /* Controller setup */
        this.productController = new ProductController();
        this.saleController = new SaleController();

        /* Set sizes to the main panel */
        setLocationRelativeTo(null);
        MainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        MainPanel.setBackground(new Colors().background);
        setContentPane(MainPanel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        /* Set styles to the buttons */
        backButton.setBackground(new Colors().main);
        backButton.setForeground(Color.white);

        mainLabel.setForeground(new Colors().main);
        previewLabel.setForeground(new Colors().darkerGreen);

        titlePanel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        titlePanel.setBackground(new Colors().light);

        searchPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        searchPanel.setBackground(new Colors().light);

        previewPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        previewPanel.setBackground(new Colors().light);

        cleanPreviewButton.setBackground(new Colors().red);
        cleanPreviewButton.setForeground(Color.white);

        createSaleButton.setBackground(new Colors().green);
        createSaleButton.setForeground(Color.white);

        /* Action Listeners */
        backButton.addActionListener(e -> {
            MainMenu.getInstance().setVisible(true);
            setVisible(false);
        });

        createSaleButton.addActionListener(e -> {
            createSale();
        });

        cleanPreviewButton.addActionListener(e -> {
            cleanPreviewTable();
        });

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
        previewTableModel.addColumn("P.U");
        previewTableModel.addColumn("P.T");
        previewTableModel.addColumn("");
        previewTable.setModel(previewTableModel);

        previewTable.getColumnModel().getColumn(0).setCellRenderer(new CreateSaleView.ButtonRenderer("-"));
        previewTable.getColumnModel().getColumn(2).setCellRenderer(new CreateSaleView.ButtonRenderer("+"));
        previewTable.getColumnModel().getColumn(7).setCellRenderer(new CreateSaleView.ButtonRenderer("x"));

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
                            double newTotalPrice = (double) previewTableModel.getValueAt(row, 5) * newQty;
                            previewTableModel.setValueAt(newQty, row, 1);
                            previewTableModel.setValueAt(newTotalPrice, row, 6);
                            calculateSubtotal();
                            previewTableModel.fireTableDataChanged();
                        }
                    } else if (col == 2) {// Increase
                        Product product = productController.getProductById(productId);
                        int newQty = currentQuantity + 1;
                        if (newQty <= product.getStock()) {
                            double newPrice = (double) previewTable.getValueAt(row, 5) * newQty;
                            previewTableModel.setValueAt(newQty, row, 1);
                            previewTableModel.setValueAt(newPrice, row, 6);
                            calculateSubtotal();
                            previewTableModel.fireTableDataChanged();
                        }
                    } else if (col == 7) { // Remove
                        previewTableModel.removeRow(row);
                        calculateSubtotal();
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

        TableColumn unitPriceColumnPreviewTable = previewTable.getColumnModel().getColumn(5);
        unitPriceColumnPreviewTable.setMaxWidth(100);
        unitPriceColumnPreviewTable.setCellRenderer(centerRenderer);

        TableColumn totalPriceColumnPreviewTable = previewTable.getColumnModel().getColumn(6);
        totalPriceColumnPreviewTable.setMaxWidth(100);
        totalPriceColumnPreviewTable.setCellRenderer(centerRenderer);

        TableColumn removeButtonColumnPreviewTable = previewTable.getColumnModel().getColumn(7);
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

        filterResultsTable();
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
                if (newQty <= productStock) {
                    double newTotalPrice = (double) previewTableModel.getValueAt(row, 5) * newQty;
                    previewTableModel.setValueAt(newQty, row, 1);
                    previewTableModel.setValueAt(newTotalPrice, row, 6);
                    calculateSubtotal();
                    previewTableModel.fireTableDataChanged();
                }
                return;
            }
        }
        if (productStock > 0) {
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
        calculateSubtotal();
        previewTableModel.fireTableDataChanged();
    }

    private void calculateSubtotal() {
        double subtotal = 0.0;

        for (int i = 0; i < previewTableModel.getRowCount(); i++) {
            int quantity = (int) previewTableModel.getValueAt(i, 1);
            double unitPrice = (double) previewTableModel.getValueAt(i, 5);
            double totalPrice = quantity * unitPrice;
            subtotal += totalPrice;
        }

        subtotalLabel.setText("Subtotal: " + String.format("%.2f", subtotal));
    }


    public void cleanPreviewTable() {
        previewTableModel.setNumRows(0);
        calculateSubtotal();
        previewTableModel.fireTableDataChanged();
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

    private void createSale() {
        if (previewTable.getRowCount() > 0) {
            List<SaleService.SaleItem> items = new ArrayList<>();
            for (int i = 0; i < previewTable.getRowCount(); i++) {
                int id = (int) previewTable.getValueAt(i, 3);
                int quantity = (int) previewTable.getValueAt(i, 1);
                String name = (String) previewTable.getValueAt(i, 4);
                double unitPrice = (double) previewTable.getValueAt(i, 5);
                SaleService.SaleItem item = new SaleService.SaleItem(id, quantity, name, unitPrice);
                items.add(item);
            }
            Sale createdSale = saleController.storeSale(items);
            if (createdSale != null) {
                int option = JOptionPane.showConfirmDialog(
                        CreateSaleView.this,
                        "¿Quieres crear una factura de una vez?",
                        "Continuar con la factura",
                        JOptionPane.YES_NO_OPTION
                );
                if (option == JOptionPane.YES_OPTION) {
                    setVisible(false);
                    CreateInvoiceView invoiceView = new CreateInvoiceView(createdSale.getId());
                    invoiceView.setVisible(true);
                }
                cleanPreviewTable();
            }
        } else {
            JOptionPane.showMessageDialog(null, "No es posible continuar sin productos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
