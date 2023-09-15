package org.example.view.product;

import com.formdev.flatlaf.FlatClientProperties;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.view.MainMenu;
import org.example.controller.ProductController;
import org.example.model.Product;
import org.example.themes.Colors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.opencsv.CSVWriter;
import org.example.utils.renderer.CenterRenderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class ProductView extends JFrame {
    private final ProductView self = this;
    private JPanel MainPanel;
    private JButton backButton;
    private JTextField searchField;
    private JTable productTable;
    private JButton createProductButton;
    private JButton pdfButton;
    private JButton excelButton;

    private JButton csvButton;
    private JLabel mainLabel;
    private JPanel titlePanel;
    private JScrollPane tablePane;

    private final DefaultTableModel tableModel;

    public ProductController controller;

    public ProductView() {
        /* Change the UI text language (spanish) */
        UIManager.put("FileChooser.openDialogTitleText", "Abrir");
        UIManager.put("FileChooser.lookInLabelText", "Ver en:");
        UIManager.put("FileChooser.fileNameLabelText", "Nombre del archivo:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Archivos de tipo:");
        UIManager.put("FileChooser.openButtonText", "Abrir");
        UIManager.put("FileChooser.cancelButtonText", "Cancelar");
        UIManager.put("FileChooser.acceptAllFileFilterText", "Todos los tipos");

        UIManager.put("FileChooser.saveDialogTitleText", "Guardar como");
        UIManager.put("FileChooser.saveInLabelText", "Guardar en:");
        UIManager.put("FileChooser.saveButtonText", "Guardar");

        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");

        this.controller = new ProductController();

        /* Style for the create product button */
        createProductButton.setFocusPainted(false);
        createProductButton.setForeground(Color.white);
        createProductButton.setBackground(new Colors().green);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nombre");
        tableModel.addColumn("Invent.");
        tableModel.addColumn("P.U");
        tableModel.addColumn("Medida");
        tableModel.addColumn("");
        tableModel.addColumn("");
        productTable.setModel(tableModel);

        /* styles for the table header */
        JTableHeader header = productTable.getTableHeader();
        Font headerFont = header.getFont();
        Font boldHeaderFont = new Font(headerFont.getFontName(), Font.BOLD, headerFont.getSize());
        header.setFont(boldHeaderFont);
        header.setBackground(new Colors().main);
        header.setForeground(Color.white);

        productTable.setBackground(new Colors().light);

        /* Render the edit & delete columns */
        productTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("Editar"));
        productTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer("Eliminar"));

        /* Set align & size of the columns */
        TableColumn idColumn = productTable.getColumnModel().getColumn(0);
        idColumn.setMaxWidth(50);
        CenterRenderer centerRenderer = new CenterRenderer();
        idColumn.setCellRenderer(centerRenderer);

        TableColumn inventoryColumn = productTable.getColumnModel().getColumn(2);
        inventoryColumn.setMaxWidth(60);

        TableColumn priceColumn = productTable.getColumnModel().getColumn(3);
        priceColumn.setMaxWidth(100);

        TableColumn measurementColumn = productTable.getColumnModel().getColumn(4);
        measurementColumn.setMaxWidth(200);

        TableColumn editColumn = productTable.getColumnModel().getColumn(5);
        editColumn.setMinWidth(80);
        editColumn.setMaxWidth(80);

        TableColumn deleteColumn = productTable.getColumnModel().getColumn(6);
        deleteColumn.setMinWidth(90);
        deleteColumn.setMaxWidth(90);

        createProductButton.addActionListener(e -> {
            CreateProductDialog createProductDialog = new CreateProductDialog(this);
            createProductDialog.showCreateDialog();
        });

        pdfButton.addActionListener(e -> {
            try {
                exportToPDF();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al exportar a PDF: " + ex.getMessage());
            }
        });

        excelButton.addActionListener(e -> exportToExcel());

        csvButton.addActionListener(e -> exportToCSV());

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

        productTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = productTable.rowAtPoint(e.getPoint());
                int col = productTable.columnAtPoint(e.getPoint());

                if (col == 5 && !isNoResultsRow(row)) { // Edit
                    if (e.getClickCount() == 1) {
                        int productId = (int) productTable.getValueAt(row, 0);
                        Product productToEdit = controller.getProductById(productId);
                        EditProductDialog editProductDialog =  new EditProductDialog(self);
                        editProductDialog.showEditDialog(productToEdit);
                        filterTable();
                    }
                } else if (col == 6 && !isNoResultsRow(row)) { // Delete
                    if (e.getClickCount() == 1) {
                        int productId = (int) productTable.getValueAt(row, 0);
                        int option = JOptionPane.showConfirmDialog(
                                ProductView.this,
                                "¿Estás seguro de eliminar este producto?",
                                "Confirmación de eliminación",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (option == JOptionPane.YES_OPTION) {
                            controller.deleteProductById(productId);
                            filterTable();
                        }
                    }
                }
            }
        });

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        /* Padding */
        MainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        setTitle("Productos");
        MainPanel.setBackground(new Colors().background);

        setContentPane(MainPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        int width = (int) (screenWidth * 0.8);
        int height = (int) (screenHeight * 0.8);
        setSize(width, height);
        setLocationRelativeTo(null);

        setVisible(true);

        titlePanel.setBackground(new Colors().light);
        titlePanel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        mainLabel.setForeground(new Colors().main);

        backButton.setBackground(new Colors().main);
        backButton.setForeground(Color.white);

        pdfButton.setBackground(new Colors().main);
        pdfButton.setForeground(Color.white);

        excelButton.setBackground(new Colors().main);
        excelButton.setForeground(Color.white);

        csvButton.setBackground(new Colors().main);
        csvButton.setForeground(Color.white);

        backButton.addActionListener(e -> {
            MainMenu.getInstance().setVisible(true);
            setVisible(false);
        });
        filterTable();
    }

    public void filterTable() {
        tableModel.setRowCount(0);
        String searchText = searchField.getText().toString().toLowerCase().trim();
        Product[] products = controller.searchProducts(searchText);

        if (products.length == 0) {
            tableModel.addRow(new Object[]{"NA", "No se encontraron resultados", "", "", ""});
        } else {
            for (Product product : products) {
                tableModel.addRow(new Object[]{
                        product.getId(),
                        product.getName(),
                        product.getStock(),
                        product.getUnitPrice(),
                        product.getUnitMeasurement()
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


    private boolean isNoResultsRow(int row) {
        Object idValue = productTable.getValueAt(row, 0);
        return idValue instanceof String && "NA".equals(idValue);
    }

    public void exportToPDF() throws IOException {
        PDDocument document = new PDDocument();

        try {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Fonts & sizes for the table
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
            contentStream.setLeading(14);
            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            float tableYPosition = yPosition;
            int rowsPerPage = 20;
            int pageNumber = 1;

            DefaultTableModel model = (DefaultTableModel) productTable.getModel();
            int rowCount = model.getRowCount();
            int columnCount = model.getColumnCount() - 2; // Quit edit y delete column

            String[] tableHeaders = {"ID", "Nombre", "Inventario", "Precio Unitario", "Medida"};

            float[] columnWidthsPercentage = {10f, 55f, 10f, 15f, 10f};

            float[] columnWidths = new float[columnCount];
            for (int col = 0; col < columnCount; col++) {
                columnWidths[col] = tableWidth * (columnWidthsPercentage[col] / 100f);
            }

            float tableXPosition = margin;
            for (int col = 0; col < columnCount; col++) {
                contentStream.setLineWidth(1f);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 8);
                contentStream.setLeading(14);
                contentStream.beginText();
                contentStream.newLineAtOffset(tableXPosition, tableYPosition);
                contentStream.showText(tableHeaders[col]);
                contentStream.endText();
                tableXPosition += columnWidths[col];
            }

            tableYPosition -= 15; // Restart position for the data of the table
            for (int row = 0; row < rowCount; row++) {
                tableYPosition -= 15;
                tableXPosition = margin;

                for (int col = 0; col < columnCount; col++) {
                    Object cellValue = model.getValueAt(row, col);
                    String value = (cellValue != null) ? cellValue.toString() : ""; // Verify null
                    float columnWidth = columnWidths[col];

                    contentStream.setLineWidth(1f);
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
                    contentStream.setLeading(14);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(tableXPosition, tableYPosition);
                    contentStream.showText(value);
                    contentStream.endText();
                    tableXPosition += columnWidth;
                }

                if (tableYPosition <= margin) {
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
                    contentStream.setLeading(14);
                    tableYPosition = yStart;
                    pageNumber++;
                }
            }

            contentStream.close();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar archivo PDF");
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File pdfFileToSave = fileChooser.getSelectedFile();
                String pdfFileName = pdfFileToSave.getAbsolutePath();

                if (!pdfFileName.toLowerCase().endsWith(".pdf")) {
                    pdfFileName += ".pdf";
                }

                document.save(pdfFileName);

                File pdfFileToOpen = new File(pdfFileName);
                Desktop.getDesktop().open(pdfFileToOpen);
            }
        } finally {
            document.close();
        }
    }


    public void exportToExcel() {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Productos");

            // Row for the header
            XSSFRow headerRow = sheet.createRow(0);
            int columnCount = productTable.getColumnCount();
            for (int j = 0; j < columnCount; j++) {
                XSSFCell headerCell = headerRow.createCell(j);
                headerCell.setCellValue(productTable.getColumnName(j));
            }

            int rowCount = productTable.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                XSSFRow excelRow = sheet.createRow(i + 1); // Second row after header
                for (int j = 0; j < columnCount; j++) {
                    Object cellValue = productTable.getValueAt(i, j);
                    XSSFCell excelCell = excelRow.createCell(j);
                    if (cellValue != null) {
                        excelCell.setCellValue(cellValue.toString());
                    } else {
                        excelCell.setCellValue(""); // Empty value for the null value
                    }
                }
            }

            /* Let the user choose the location & name */
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar archivo Excel");
            fileChooser.setApproveButtonText("Guardar");
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File excelFile = fileChooser.getSelectedFile();
                String excelFileName = excelFile.getAbsolutePath();

                if (!excelFileName.toLowerCase().endsWith(".xlsx")) {
                    excelFileName += ".xlsx";
                }

                try (FileOutputStream outputStream = new FileOutputStream(excelFileName)) {
                    workbook.write(outputStream);
                }

                JOptionPane.showMessageDialog(this, "Los datos se han exportado a Excel correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar a Excel: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void exportToCSV() {
        try {
            DefaultTableModel model = (DefaultTableModel) productTable.getModel();
            int rowCount = model.getRowCount();
            int columnCount = model.getColumnCount() - 2; // remove edit & delete columns
            String[] tableHeaders = {"ID", "Nombre", "Inventario", "Precio Unitario", "Medida"};

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar archivo CSV");
            fileChooser.setApproveButtonText("Guardar");
            int userSelection = fileChooser.showSaveDialog(this);

            /* Let the user choose the location & name */
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();

                try (FileWriter fileWriter = new FileWriter(fileToSave);
                     CSVWriter csvWriter = new CSVWriter(fileWriter)) {

                    csvWriter.writeNext(tableHeaders);

                    for (int row = 0; row < rowCount; row++) {
                        String[] rowData = new String[columnCount];
                        for (int col = 0; col < columnCount; col++) {
                            Object cellValue = model.getValueAt(row, col);
                            String value = (cellValue != null) ? cellValue.toString() : "";
                            rowData[col] = value;
                        }
                        csvWriter.writeNext(rowData);
                    }

                    csvWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar a CSV: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}