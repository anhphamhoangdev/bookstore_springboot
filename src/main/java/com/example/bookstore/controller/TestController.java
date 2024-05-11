package com.example.bookstore.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/signup")
    public String signup(){
        return "signup";
    }

    @RequestMapping("/show403page")
    public String forbiddenPage(){
        return "403";
    }


}
