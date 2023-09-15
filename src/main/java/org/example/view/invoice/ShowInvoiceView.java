package org.example.view.invoice;

import com.formdev.flatlaf.FlatClientProperties;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.example.controller.SaleController;
import org.example.model.Invoice;
import org.example.model.Sale;
import org.example.service.SaleService;
import org.example.themes.Colors;
import org.example.utils.renderer.CenterRenderer;
import org.example.view.MainMenu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShowInvoiceView extends JFrame {
    private JLabel nameField;
    private JLabel nitField;
    private JTable previewTable;
    private JButton createInvoiceButton;
    private JPanel MainPanel;
    private JLabel addressField;
    private JLabel dateLabel;
    private JPanel invoiceDataPanel;
    private JButton backButton;
    private Invoice invoice;
    private Sale sale;
    private String invoiceDate;
    private final DefaultTableModel tableModel;

    private SaleController saleController;

    public ShowInvoiceView(int saleId) {
        this.saleController = new SaleController();
        this.sale = saleController.getSaleById(saleId);
        this.invoice = sale.getInvoice();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        int screenHeight20 = (int) (screenHeight * 0.2);
        int screenHeight10 = (int) (screenHeight * 0.1);


        /* Set sizes to the main panel */
        setLocationRelativeTo(null);
        MainPanel.setBorder(new EmptyBorder(screenHeight10, screenHeight20, screenHeight10, screenHeight20));
        MainPanel.setBackground(new Colors().background);
        setTitle("Crear factura");
        setContentPane(MainPanel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Cant.");
        tableModel.addColumn("Nombre");
        tableModel.addColumn("P.U");
        tableModel.addColumn("P.T");
        previewTable.setModel(tableModel);

        CenterRenderer centerRenderer = new CenterRenderer();

        TableColumn idColumn = previewTable.getColumnModel().getColumn(0);
        idColumn.setMaxWidth(50);
        idColumn.setCellRenderer(centerRenderer);

        TableColumn priceColumn = previewTable.getColumnModel().getColumn(2);
        priceColumn.setMaxWidth(100);
        priceColumn.setCellRenderer(centerRenderer);

        TableColumn totalPriceColumn = previewTable.getColumnModel().getColumn(3);
        totalPriceColumn.setMaxWidth(100);
        totalPriceColumn.setCellRenderer(centerRenderer);

        tableModel.setRowCount(0);

        List<SaleService.SaleItem> items = sale.getContent();
        for (SaleService.SaleItem item : items) {
            tableModel.addRow(new Object[]{
                    item.getQuantity(),
                    item.getName(),
                    String.format("%.2f", item.getUnitPrice()),
                    String.format("%.2f", (item.getUnitPrice() * item.getQuantity()))
            });
        }
        createInvoiceButton.addActionListener(e -> {
            try {
                generateInvoice();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al exportar a PDF: " + ex.getMessage());
            }
        });

        invoiceDataPanel.setBackground(Color.white);
        invoiceDataPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        backButton.setBackground(new Colors().main);
        backButton.setForeground(Color.white);

        nameField.setText(invoice.getName());

        nitField.setText(invoice.getNit());

        addressField.setText(invoice.getAddress());


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.invoiceDate = dateFormat.format(invoice.getDate());
        dateLabel.setText("Fecha: " + invoiceDate);
        backButton.addActionListener(e -> {
            MainMenu.getInstance().setVisible(true);
            setVisible(false);
            dispose();
        });
    }

    private void generateInvoice() throws IOException {
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
            float tableXPosition = margin;
            int rowsPerPage = 20;
            int pageNumber = 1;

            DefaultTableModel model = (DefaultTableModel) previewTable.getModel();
            int rowCount = model.getRowCount();
            int columnCount = model.getColumnCount(); // Quit edit y delete column

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
            contentStream.beginText();
            contentStream.newLineAtOffset(tableXPosition, tableYPosition);
            contentStream.showText("VERDUMARKET");
            contentStream.endText();

            // Invoice Data
            float dataXPosition = margin;
            float dataWidth = tableWidth * 0.7f;
            float nitXPosition = dataXPosition + dataWidth;

            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
            contentStream.newLineAtOffset(nitXPosition, tableYPosition);
            contentStream.showText("Fecha: " + invoiceDate);
            contentStream.endText();

            tableYPosition -= 25;

            // Space Line
            float superiorLineYPosition = tableYPosition + 15;
            float lineYPosition = tableYPosition - 5;
            float inferiorLineYPostion = tableYPosition - 25;

            contentStream.setLineWidth(1f);
            contentStream.moveTo(margin, superiorLineYPosition);
            contentStream.lineTo(margin + tableWidth, superiorLineYPosition);
            contentStream.stroke();

            contentStream.setLineWidth(1f);
            contentStream.moveTo(margin + tableWidth, superiorLineYPosition);
            contentStream.lineTo(margin + tableWidth, inferiorLineYPostion);
            contentStream.stroke();

            contentStream.setLineWidth(1f);
            contentStream.moveTo((float) (margin + (tableWidth * 0.69)), superiorLineYPosition);
            contentStream.lineTo((float) (margin + (tableWidth * 0.69)), lineYPosition);
            contentStream.stroke();

            contentStream.setLineWidth(1f);
            contentStream.moveTo(margin, superiorLineYPosition);
            contentStream.lineTo(margin, inferiorLineYPostion);
            contentStream.stroke();

            contentStream.setLineWidth(1f);
            contentStream.moveTo(margin, lineYPosition);
            contentStream.lineTo(margin + tableWidth, lineYPosition);
            contentStream.stroke();

            contentStream.setLineWidth(1f);
            contentStream.moveTo(margin, inferiorLineYPostion);
            contentStream.lineTo(margin + tableWidth, inferiorLineYPostion);
            contentStream.stroke();

            // Name
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
            contentStream.newLineAtOffset(dataXPosition + 4, tableYPosition);
            contentStream.showText("Nombre: ");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
            contentStream.newLineAtOffset(dataXPosition + 50, tableYPosition);
            contentStream.showText(invoice.getName());
            contentStream.endText();

            // NIT
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
            contentStream.newLineAtOffset(nitXPosition, tableYPosition);
            contentStream.showText("NIT: ");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
            contentStream.newLineAtOffset(nitXPosition + 30, tableYPosition);
            contentStream.showText(invoice.getNit());
            contentStream.endText();

            // Address
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 11);
            contentStream.newLineAtOffset(margin + 4, tableYPosition - 20);
            contentStream.showText("Dirección: " + invoice.getAddress());
            contentStream.endText();


            tableYPosition -= 50; // Add space to the invoice data

            float initialTablePosition = tableYPosition + 14;

            contentStream.setLineWidth(1f);
            contentStream.moveTo(margin, initialTablePosition);
            contentStream.lineTo(margin + tableWidth, initialTablePosition);
            contentStream.stroke();

            contentStream.setLineWidth(1f);
            contentStream.moveTo(margin, tableYPosition - 6);
            contentStream.lineTo(margin + tableWidth, tableYPosition - 6);
            contentStream.stroke();


            String[] tableHeaders = {"Cant.", "Descripción", "P.U", "P.T"};

            float[] columnWidthsPercentage = {10f, 60f, 15f, 15f};

            float[] columnWidths = new float[columnCount];
            for (int col = 0; col < columnCount; col++) {
                columnWidths[col] = tableWidth * (columnWidthsPercentage[col] / 100f);
            }

            for (int col = 0; col < columnCount; col++) {
                contentStream.setLineWidth(1f);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
                contentStream.setLeading(14);
                contentStream.beginText();
                if (col == 0) {
                    contentStream.newLineAtOffset(tableXPosition + 4, tableYPosition);
                } else {
                    contentStream.newLineAtOffset(tableXPosition, tableYPosition);
                }
                contentStream.showText(tableHeaders[col]);
                contentStream.endText();
                tableXPosition += columnWidths[col];
            }

            tableYPosition -= 10; // Restart position for the data of the table
            for (int row = 0; row < rowCount; row++) {
                tableYPosition -= 15;
                tableXPosition = margin;


                for (int col = 0; col < columnCount; col++) {
                    Object cellValue = model.getValueAt(row, col);

                    String value = (cellValue != null) ? cellValue.toString() : ""; // Verify null
                    float columnWidth = columnWidths[col];

                    contentStream.setLineWidth(1f);
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
                    contentStream.setLeading(14);
                    contentStream.beginText();
                    if (col == 0) {
                        contentStream.newLineAtOffset(tableXPosition + 4, tableYPosition);
                    } else {
                        contentStream.newLineAtOffset(tableXPosition, tableYPosition);
                    }
                    contentStream.showText(value);
                    contentStream.endText();
                    tableXPosition += columnWidth;
                }

                if (tableYPosition <= margin) {
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
                    contentStream.setLeading(14);
                    tableYPosition = yStart;
                    pageNumber++;
                }
            }

            float finalDataPosition = tableYPosition - 12;

            contentStream.setLineWidth(1f);
            contentStream.moveTo(margin, finalDataPosition);
            contentStream.lineTo(margin + tableWidth, finalDataPosition);
            contentStream.stroke();

            tableYPosition -= 25;

            float finalTablePosition = tableYPosition - 6;

            contentStream.setLineWidth(1f);
            contentStream.moveTo(margin, initialTablePosition);
            contentStream.lineTo(margin, finalTablePosition);
            contentStream.stroke();

            contentStream.setLineWidth(1f);
            contentStream.moveTo(margin + tableWidth, initialTablePosition);
            contentStream.lineTo(margin + tableWidth, finalTablePosition);
            contentStream.stroke();

            contentStream.setLineWidth(1f);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
            contentStream.setLeading(14);
            contentStream.beginText();
            contentStream.newLineAtOffset(tableWidth - 55, tableYPosition);
            contentStream.showText("Total: " + String.format("%.2f", sale.getTotal()));
            contentStream.endText();

            contentStream.setLineWidth(1f);
            contentStream.moveTo(margin, finalTablePosition);
            contentStream.lineTo(margin + tableWidth, finalTablePosition);
            contentStream.stroke();

            contentStream.close();

            String fileName = "VerduMarket_Invoice_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf";
            File pdfFile = new File(fileName);
            document.save(pdfFile);
            Desktop.getDesktop().open(pdfFile);
        } finally {
            document.close();
        }
    }


    private double calculateTotal(DefaultTableModel tableModel) {
        double total = 0.0;
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            String ptValue = tableModel.getValueAt(row, 3).toString();
            total += Double.parseDouble(ptValue);
        }
        return total;
    }


}
