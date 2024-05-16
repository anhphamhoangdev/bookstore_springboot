package com.example.bookstore.payment;

import com.example.bookstore.service.InvoiceService;
import com.example.bookstore.service.StockService;
import com.example.bookstore.util.GenerateID;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class PaymentService {
    private PaymentStrategy paymentStrategy;

    private InvoiceService invoiceService;
    private StockService stockService;
    private GenerateID generateID;

    @Autowired
    public PaymentService(GenerateID generateID, InvoiceService invoiceService, StockService stockService) {
        this.generateID = generateID;
        this.invoiceService = invoiceService;
        this.stockService = stockService;
    }

    public void processPayment(HttpSession session,
                               Model model,
                               String firstName,
                               String address,
                               String paymentMethod,
                               Double subTotal,
                               Double total) {
        if(paymentMethod.equals("cash-on-delivery")) {
            paymentStrategy = new CODPayment();
        }
        paymentStrategy.pay(generateID, invoiceService, session, stockService,model, firstName, address, paymentMethod, subTotal, total);
    }

}
