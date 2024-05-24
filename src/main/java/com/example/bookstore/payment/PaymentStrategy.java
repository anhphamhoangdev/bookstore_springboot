package com.example.bookstore.payment;

import com.example.bookstore.service.InvoiceService;
import com.example.bookstore.service.StockService;
import com.example.bookstore.util.GenerateID;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;

public interface PaymentStrategy {
    void pay(GenerateID generateID,
             InvoiceService invoiceService,
             HttpSession session,
             StockService stockService,
             Model model,
             String firstName,
             String address,
             String paymentMethod,
             Double subTotal,
             Double total, HttpServletRequest request) throws UnsupportedEncodingException;
}




