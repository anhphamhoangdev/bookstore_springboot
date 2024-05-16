package com.example.bookstore.controller;

import com.example.bookstore.entity.Cart;
import com.example.bookstore.entity.Invoice;
import com.example.bookstore.entity.LineItem;
import com.example.bookstore.payment.PaymentService;
import com.example.bookstore.util.EmailSenderService;
import com.example.bookstore.util.GenerateID;
import org.thymeleaf.TemplateEngine;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final EmailSenderService emailSenderService;
    private final SpringTemplateEngine templateEngine;
    private PaymentService paymentService;

    @Autowired
    public CheckoutController(PaymentService paymentService, EmailSenderService emailSenderService, SpringTemplateEngine templateEngine) {
        this.paymentService = paymentService;
        this.emailSenderService = emailSenderService;
        this.templateEngine = templateEngine;
    }

    @RequestMapping("")
    public String checkout(){
        return "checkout";
    }

    @PostMapping("/process")
    public String process(@RequestParam("firstname") String firstName,
                          @RequestParam("lastname") String lastName,
                          @RequestParam("email") String email,
                          @RequestParam("address") String address,
                          @RequestParam("payment-method") String paymentMethod, HttpSession session, Model model) throws IOException {
        Cart cart = (Cart) session.getAttribute("cart");
        double subtotal = cart.subTotal();
        double total = cart.total();
        paymentService.processPayment(session, model, firstName, address, paymentMethod, subtotal, total);
        //emailSenderService.sendSimpleMail(email,"BILL","HI");
        String emailSubject = "Thank You For Supporting, "+firstName+" "+lastName;
        String htmlInvoice = emailSenderService.createHtmlEmail(session, firstName, lastName);
        emailSenderService.sendHtmlMail(email, emailSubject, htmlInvoice);
        return "invoice";
    }


}
