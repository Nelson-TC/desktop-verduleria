package org.example.view.invoice;

import com.formdev.flatlaf.FlatClientProperties;
import org.example.controller.InvoiceController;
import org.example.controller.SaleController;
import org.example.model.Invoice;
import org.example.model.Product;
import org.example.model.Sale;
import org.example.themes.Colors;
import org.example.utils.renderer.CenterRenderer;
import org.example.view.MainMenu;
import org.example.view.invoice.CreateInvoiceView;
import org.example.view.invoice.ShowInvoiceView;
import org.example.view.sale.CreateSaleView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

public class InvoiceView extends JFrame {
    private JPanel titlePanel;
    private JLabel mainLabel;
    private JButton backButton;
    private JTable invoiceTable;
    private final DefaultTableModel tableModel;
    private JPanel MainPanel;


    private SaleController saleController;
    private InvoiceController invoiceController;

    public InvoiceView() {
        this.saleController = new SaleController();
        this.invoiceController = new InvoiceController();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        /* Padding */
        MainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        setTitle("Ventas");
        MainPanel.setBackground(new Colors().background);
        setContentPane(MainPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        int width = (int) (screenWidth * 0.8);
        int height = (int) (screenHeight * 0.8);
        setSize(width, height);
        setLocationRelativeTo(null);

        titlePanel.setBackground(new Colors().light);
        titlePanel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        mainLabel.setForeground(new Colors().main);

        backButton.setBackground(new Colors().main);
        backButton.setForeground(Color.white);

        backButton.addActionListener(e -> {
            MainMenu.getInstance().setVisible(true);
            setVisible(false);
        });

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nombre");
        tableModel.addColumn("Dirección");
        tableModel.addColumn("Nit");
        tableModel.addColumn("Fecha");
        tableModel.addColumn("Acción");
        invoiceTable.setModel(tableModel);

        /* styles for the table header */
        JTableHeader header = invoiceTable.getTableHeader();
        Font headerFont = header.getFont();
        Font boldHeaderFont = new Font(headerFont.getFontName(), Font.BOLD, headerFont.getSize());
        header.setFont(boldHeaderFont);
        header.setBackground(new Colors().main);
        header.setForeground(Color.white);

        invoiceTable.setBackground(new Colors().light);
        invoiceTable.getColumnModel().getColumn(5).setCellRenderer(new InvoiceView.ButtonRenderer("Ver"));

        /* Set align & size of the columns */
        TableColumn idColumn = invoiceTable.getColumnModel().getColumn(0);
        idColumn.setMaxWidth(100);
        CenterRenderer centerRenderer = new CenterRenderer();
        idColumn.setCellRenderer(centerRenderer);

        TableColumn nameColumn = invoiceTable.getColumnModel().getColumn(1);

        TableColumn addressColumn = invoiceTable.getColumnModel().getColumn(2);
        addressColumn.setCellRenderer(centerRenderer);

        TableColumn nitColumn = invoiceTable.getColumnModel().getColumn(3);
        nitColumn.setMinWidth(150);
        nitColumn.setMaxWidth(150);
        nitColumn.setCellRenderer(centerRenderer);

        TableColumn dateColumn = invoiceTable.getColumnModel().getColumn(4);
        dateColumn.setMinWidth(150);
        dateColumn.setMaxWidth(150);
        dateColumn.setCellRenderer(centerRenderer);

        TableColumn actionColumn = invoiceTable.getColumnModel().getColumn(5);
        actionColumn.setMinWidth(100);
        actionColumn.setMaxWidth(100);

        invoiceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = invoiceTable.rowAtPoint(e.getPoint());
                int col = invoiceTable.columnAtPoint(e.getPoint());

                if (col == 5 && !isNoResultsRow(row)) {
                    Sale sale = saleController.getSaleByInvoiceId((int) invoiceTable.getValueAt(row, 0));
                    setVisible(false);
                    ShowInvoiceView showInvoiceView = new ShowInvoiceView(sale.getId());
                    showInvoiceView.setVisible(true);
                    dispose();
                }
            }
        });
        loadInvoices();
    }

    private void loadInvoices() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        tableModel.setRowCount(0);
        Invoice[] invoices = invoiceController.getInvoices();
        for (Invoice invoice : invoices) {
            String date = dateFormat.format(invoice.getDate());

            tableModel.addRow(new Object[]{
                    invoice.getId(),
                    invoice.getName(),
                    invoice.getAddress(),
                    invoice.getNit(),
                    date
            });
        }
        tableModel.fireTableDataChanged();
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setOpaque(true);
            setForeground(Color.white);
            setBackground(new Colors().amber);
            setText(text);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isNoResultsRow(row)) {
                return new JLabel("");
            }
            return this;
        }
    }

    private boolean isNoResultsRow(int row) {
        Object idValue = invoiceTable.getValueAt(row, 0);
        return idValue instanceof String && "NA".equals(idValue);
    }
}

