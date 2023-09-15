package org.example.view.invoice;

import com.formdev.flatlaf.FlatClientProperties;
import org.example.controller.InvoiceController;
import org.example.controller.SaleController;
import org.example.model.Invoice;
import org.example.model.Sale;
import org.example.service.SaleService;
import org.example.themes.Colors;
import org.example.utils.renderer.CenterRenderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

public class CreateInvoiceView extends JFrame {
    private JTextField nameField;
    private JTextField nitField;
    private JTable previewTable;
    private JButton createInvoiceButton;
    private JPanel MainPanel;
    private JTextField addressField;
    private JPanel invoiceDataPanel;
    private Sale sale;
    private final DefaultTableModel tableModel;

    private SaleController saleController;
    private InvoiceController invoiceController;

    public CreateInvoiceView(int saleId) {
        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");

        this.saleController = new SaleController();
        this.invoiceController = new InvoiceController();
        this.sale = saleController.getSaleById(saleId);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        int screenHeight20 = (int) (screenHeight * 0.2);
        int screenHeight10 = (int) (screenHeight * 0.1);


        /* Set sizes to the main panel */
        setLocationRelativeTo(null);
        MainPanel.setBackground(new Colors().background);
        MainPanel.setBorder(new EmptyBorder(screenHeight10, screenHeight20, screenHeight10, screenHeight20));
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
                    item.getUnitPrice(),
                    String.format("%.2f", (item.getUnitPrice() * item.getQuantity()))
            });
        }
        createInvoiceButton.addActionListener(e -> {
            createSale();
        });

        invoiceDataPanel.setBackground(Color.white);
        invoiceDataPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
    }

    private void createSale() {
        String name = String.valueOf(nameField.getText()).toLowerCase().trim();
        String nit = String.valueOf(nitField.getText()).toLowerCase().trim();
        String address = String.valueOf(addressField.getText()).toLowerCase().trim();

        if (name.isEmpty() || nit.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Asegurate de llenar todos los campos correctamente", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Invoice invoiceToCreate = new Invoice();
            invoiceToCreate.setId(-1);
            invoiceToCreate.setName(name);
            invoiceToCreate.setNit(nit);
            invoiceToCreate.setAddress(address);
            Invoice createdInvoice = invoiceController.storeInvoice(sale.getId(), invoiceToCreate);
            if (createdInvoice != null) {
                ShowInvoiceView showInvoiceView = new ShowInvoiceView(sale.getId());
                showInvoiceView.setVisible(true);
                dispose();
            }else{
                JOptionPane.showMessageDialog(this, "Hubo un error inesperado al parecer no se pudo crear la factura", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
