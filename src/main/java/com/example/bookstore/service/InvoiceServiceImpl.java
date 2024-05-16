package com.example.bookstore.service;

import com.example.bookstore.entity.Invoice;
import com.example.bookstore.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
