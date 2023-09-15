package org.example.view.sale;

import com.formdev.flatlaf.FlatClientProperties;
import org.example.controller.SaleController;
import org.example.model.Product;
import org.example.model.Sale;
import org.example.themes.Colors;
import org.example.utils.renderer.CenterRenderer;
import org.example.view.MainMenu;
import org.example.view.invoice.CreateInvoiceView;
import org.example.view.invoice.ShowInvoiceView;

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

public class SaleView extends JFrame {
    private JPanel titlePanel;
    private JLabel mainLabel;
    private JButton createSaleButton;
    private JButton backButton;
    private JTable saleTable;
    private final DefaultTableModel tableModel;
    private JPanel MainPanel;


    private SaleController saleController;

    public SaleView() {
        this.saleController = new SaleController();

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

        createSaleButton.setFocusPainted(false);
        createSaleButton.setForeground(Color.white);
        createSaleButton.setBackground(new Colors().main);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Total");
        tableModel.addColumn("Fecha");
        tableModel.addColumn("Factura");
        saleTable.setModel(tableModel);

        /* styles for the table header */
        JTableHeader header = saleTable.getTableHeader();
        Font headerFont = header.getFont();
        Font boldHeaderFont = new Font(headerFont.getFontName(), Font.BOLD, headerFont.getSize());
        header.setFont(boldHeaderFont);
        header.setBackground(new Colors().main);
        header.setForeground(Color.white);

        saleTable.setBackground(new Colors().light);
        saleTable.getColumnModel().getColumn(3).setCellRenderer(new SaleView.ButtonRenderer());

        /* Set align & size of the columns */
        TableColumn idColumn = saleTable.getColumnModel().getColumn(0);
        idColumn.setMaxWidth(100);
        CenterRenderer centerRenderer = new CenterRenderer();
        idColumn.setCellRenderer(centerRenderer);

        TableColumn totalColumn = saleTable.getColumnModel().getColumn(1);
        totalColumn.setCellRenderer(centerRenderer);

        TableColumn dateColumn = saleTable.getColumnModel().getColumn(2);
        dateColumn.setCellRenderer(centerRenderer);

        TableColumn invoiceColumn = saleTable.getColumnModel().getColumn(3);
        invoiceColumn.setMinWidth(100);
        invoiceColumn.setMaxWidth(100);

        saleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = saleTable.rowAtPoint(e.getPoint());
                int col = saleTable.columnAtPoint(e.getPoint());

                if(col == 3 && !isNoResultsRow(row)){
                    String textButton = (String) saleTable.getValueAt(row, 3);
                    int saleId = (int) saleTable.getValueAt(row, 0);
                    if(textButton == "Crear"){
                        setVisible(false);
                        CreateInvoiceView createInvoiceView = new CreateInvoiceView(saleId);
                        createInvoiceView.setVisible(true);
                    }else if(textButton == "Ver"){
                        setVisible(false);
                        ShowInvoiceView showInvoiceView = new ShowInvoiceView(saleId);
                        showInvoiceView.setVisible(true);
                    }
                    dispose();
                }
            }
        });
        createSaleButton.addActionListener(e -> {
            CreateSaleView newCreateSaleView = new CreateSaleView();
            setVisible(false);
            newCreateSaleView.setVisible(true);
            dispose();
        });

        loadSales();
    }

    private void loadSales(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        tableModel.setRowCount(0);
        Sale[] sales =  saleController.getSales();
        for (Sale sale : sales){
            String date = dateFormat.format(sale.getDate());

            boolean hasInvoice = sale.getInvoice() != null;
            String buttonText = hasInvoice ? "Ver" : "Crear";

            tableModel.addRow(new Object[]{
                    sale.getId(),
                    sale.getTotal(),
                    date,
                    buttonText
            });
        }
        tableModel.fireTableDataChanged();
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setForeground(Color.white);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isNoResultsRow(row)) {
                return new JLabel("");
            }
            String invoiceText = (String) saleTable.getValueAt(row, 3);
            Color buttonColor = invoiceText == "Crear" ? new Colors().green : new Colors().amber;
            setBackground(buttonColor);
            setText(invoiceText);
            return this;
        }
    }

    private boolean isNoResultsRow(int row) {
        Object idValue = saleTable.getValueAt(row, 0);
        return idValue instanceof String && "NA".equals(idValue);
    }
}
