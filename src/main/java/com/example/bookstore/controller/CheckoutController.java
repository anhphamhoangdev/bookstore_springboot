package com.example.bookstore.controller;

import com.example.bookstore.entity.Cart;
import com.example.bookstore.payment.PaymentService;
import com.example.bookstore.service.CartService;
import com.example.bookstore.util.EmailSenderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.io.IOException;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final EmailSenderService emailSenderService;
    private final SpringTemplateEngine templateEngine;
    private PaymentService paymentService;
    private CartService cartService;

    @Autowired
    public CheckoutController(PaymentService paymentService, EmailSenderService emailSenderService, SpringTemplateEngine templateEngine, CartService cartService) {
        this.paymentService = paymentService;
        this.emailSenderService = emailSenderService;
        this.templateEngine = templateEngine;
        this.cartService = cartService;
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
                          @RequestParam("payment-method") String paymentMethod, HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cart cart = (Cart) session.getAttribute("cart");
        double subtotal = cart.subTotal();
        double total = cart.total();
        String url = "";
        if(paymentMethod.equals("cash-on-delivery")){
            paymentService.processPayment(session, model, firstName, address, paymentMethod, subtotal, total, request);
            url = "invoice";
        }
        if(paymentMethod.equals("vnpay")){
            paymentService.processPayment(session, model, firstName, address, paymentMethod, subtotal, total, request);
            String paymentUrl = session.getAttribute("paymentUrl").toString();
            url = "redirect:" + paymentUrl;
        }
        cart.setDiscount(0);
        cartService.merge(cart);
        String emailSubject = "Thank You For Supporting, "+firstName+" "+lastName;
        String htmlInvoice = emailSenderService.createBillHtmlEmail(session, firstName);
        emailSenderService.sendHtmlMail(email, emailSubject, htmlInvoice);
        return url;
    }


}
