package com.example.bookstore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
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








}
