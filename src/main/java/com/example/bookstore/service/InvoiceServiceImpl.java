package com.example.bookstore.service;

import com.example.bookstore.entity.Invoice;
import com.example.bookstore.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }


    @Override
    public void save(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    @Override
    public Double getAllSubTotal() {
        return invoiceRepository.calculateSubtotalSum();
    }

    @Override
    public Double getAllTotal() {
        return invoiceRepository.calculateTotalSum();
    }

    @Override
    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }
}
