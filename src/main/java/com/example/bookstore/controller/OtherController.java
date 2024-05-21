package com.example.bookstore.controller;

import com.example.bookstore.service.InvoiceService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OtherController {

    private InvoiceService invoiceService;

    @Autowired
    public OtherController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @RequestMapping("/login")
    public String login(){
        return "signin-signup/signin";
    }

    @RequestMapping("/access-denied")
    public String forbiddenPage(HttpSession session){
        return "error/403";
    }

    @RequestMapping("/entity-not-found")
    public String notFoundPage(HttpSession session){
        return "error/entitynotfound";
    }

    @RequestMapping("/confirm")
    public String confirm(){
        return "signin-signup/confirm";
    }



    @RequestMapping("/bill")
    public String bill(){
        return "invoice";
    }





}
