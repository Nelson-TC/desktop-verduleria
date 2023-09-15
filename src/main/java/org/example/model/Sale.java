package org.example.model;


import org.example.service.SaleService;

import java.util.Date;
import java.util.List;

public class Sale {
    private int id;
    private Date date;
    private double total;
    private List<SaleService.SaleItem> content;
    private Invoice invoice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<SaleService.SaleItem> getContent() {
        return content;
    }

    public void setContent(List<SaleService.SaleItem> content) {
        this.content = content;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
