package com.example.bookstore.payment;

import com.example.bookstore.entity.*;
import com.example.bookstore.service.InvoiceService;
import com.example.bookstore.service.StockService;
import com.example.bookstore.util.GenerateID;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.Date;

public class CODPayment implements PaymentStrategy{

    @Override
    public void pay(GenerateID generateID,
                    InvoiceService invoiceService,
                    HttpSession session,
                    StockService stockService,
                    Model model,
                    String firstName,
                    String address,
                    String paymentMethod,
                    Double subTotal,
                    Double total, HttpServletRequest request) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceID(generateID.generateBillId());
        invoice.setFirstName(firstName);
        invoice.setAddress(address);
        invoice.setPaymentMethod(paymentMethod);
        invoice.setStatus("Delevering");
        invoice.setOrderDate(new Date());
        // 3 days = 72 * 60 * 60 * 1000 (1000 ms)
        Long threeDays = (long) (72 * 60 * 60 * 1000);
        Date d = new Date(new Date().getTime() + threeDays);
        invoice.setDeliveryDate(d);

        invoice.setSubTotal(subTotal);
        invoice.setTotal(total);

        Cart cart = (Cart) session.getAttribute("cart");
        for (int i = cart.getLineItemList().size() - 1; i >= 0; i--) {
            LineItem l = cart.getLineItemList().get(i);
            invoice.addLineItem(l);
            Stock stock = l.getBook().getStock();
            stock.setQuantity(stock.getQuantity() - l.getQuantity());
            stockService.merge(stock);
            cart.deleteLineItem(l);
        }
        User user = (User) session.getAttribute("user");
        user.addInvoice(invoice);
        model.addAttribute("invoice", invoice);
        session.setAttribute("invoice", invoice);
        invoiceService.save(invoice);
    }
}
