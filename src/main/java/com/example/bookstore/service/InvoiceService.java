package com.example.bookstore.service;

import com.example.bookstore.entity.Invoice;

import java.util.List;

public interface InvoiceService {

    public void save(Invoice invoice);

    public Double getAllSubTotal();
    public Double getAllTotal();

    public List<Invoice> findAll();
}
