package com.example.bookstore.controller;


import com.example.bookstore.entity.User;
import com.example.bookstore.service.UserService;
import com.example.bookstore.util.EmailSenderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordController {

    private final EmailSenderService emailSenderService;
    private UserService userService;

    @Autowired
    public PasswordController(UserService userService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }

    @RequestMapping("/forgot-password")
    public String forgotPassword(HttpSession session) {
        if(session.getAttribute("user") != null) {
            return "redirect:/";
        }
        return "forgot-password";
    }

    @PostMapping("/password-process")
    public String passwordProcess(@RequestParam("email") String email, Model model) {
        User user = userService.findByEmail(email);
        if(user != null)
        {
            String emailSubject = "Your New Password For BookStore Online";
            String new_password = emailSenderService.generateRandom();
            String htmlInvoice = emailSenderService.createForgotPasswordEmail(new_password);
            emailSenderService.sendHtmlMail(email, emailSubject, htmlInvoice);
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(new_password));
            userService.merge(user);
            model.addAttribute("success", "Please check your email !");
        }
        else
        {
            model.addAttribute("error", "Email is not found !");
        }

        return "forgot-password";
    }

}
