package org.example.controller;

import org.example.model.Invoice;
import org.example.service.ApiService;
import org.example.service.InvoiceService;

import javax.swing.*;
import java.io.IOException;

public class InvoiceController {
    private final InvoiceService invoiceService;
    public InvoiceController() {
        this.invoiceService =  new InvoiceService(new ApiService());
    }

    public Invoice[] getInvoices(){
        try{
            return invoiceService.getInvoices();
        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
    public Invoice getInvoiceById(int invoiceId){
        try{
            return invoiceService.getInvoiceById(invoiceId);
        }catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public Invoice storeInvoice(int saleId, Invoice invoiceToCreate){
        try {
            Invoice createdInvoice =  invoiceService.createInvoice(saleId, invoiceToCreate);
            JOptionPane.showMessageDialog(null, "Factura creada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return createdInvoice;
        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

}
