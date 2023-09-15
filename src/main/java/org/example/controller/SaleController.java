package org.example.controller;

import org.example.model.Sale;
import org.example.service.ApiService;
import org.example.service.InventoryService;
import org.example.service.SaleService;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SaleController {

    private final SaleService saleService;

    public SaleController() {
        this.saleService = new SaleService(new ApiService());
    }

    public Sale[] getSales() {
        try {
            return saleService.getSales();
        }catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public Sale storeSale(List<SaleService.SaleItem> items) {
        try {
            Sale sale = saleService.createSale(items);
            JOptionPane.showMessageDialog(null, "Se han realizado la venta con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return sale;
        } catch (IOException ex) {
            int statusCode = Integer.parseInt(ex.getMessage().substring(41, 44));
            if (statusCode == 400) {
                JOptionPane.showMessageDialog(null, "Los productos no son válidos. Verifica las cantidades");
            } else if (statusCode == 500) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error interno en el servidor.");
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado, intentalo mas tarde.");
                ex.printStackTrace();
            }
            return null;
        }
    }

    public Sale getSaleById(int saleId) {
        try {
            return saleService.getSaleById(saleId);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Sale getSaleByInvoiceId(int invoiceId){
        try{
            return saleService.getSaleByInvoiceId(invoiceId);
        }catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
}
