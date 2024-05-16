package com.example.bookstore.controller;

import com.example.bookstore.entity.User;
import com.example.bookstore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/account")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public String account(@RequestParam("userID") String userID) {
        return "account";
    }

    @RequestMapping("/update")
    public String update(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "account-update";
    }

    @PostMapping("/update/process")
    public String updateProcess(HttpSession session,
                                @RequestParam("firstName") String firstName,
                                @RequestParam("lastName") String lastName,
                                @RequestParam("phoneNumber") String phoneNum,
                                @RequestParam("address") String address, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNum(phoneNum);
        user.setAddress(address);
        userService.merge(user);
        session.setAttribute("user", user);
        model.addAttribute("myUpdate", "YOUR ACCOUNT IS UPDATED");
        return "account";
    }

    @RequestMapping("/change-password")
    public String changePassword(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "account-changepassword";
    }

    @PostMapping("/change-password/process")
    public String updateProcess(HttpSession session,
                                @RequestParam("oldPassword") String oldPassword,
                                @RequestParam("newPassword") String newPassword,
                                Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches(oldPassword, user.getPassword())) {
            String pattern = "^(?=.*[0-9])(?=.*[@#$%^&+=!])(?=\\S+$).*$";
            if(newPassword.length() >= 8 && newPassword.matches(pattern))
            {
                user.setPassword(encoder.encode(newPassword));
                userService.merge(user);
                model.addAttribute("myUpdate", "Your account has been updated.");
                return "account";
            }
            else
            {
                model.addAttribute("myError", "Password must have a minimum length of 8, at least 1 digit, and 1 special character !!");
                return "account-changepassword";
            }
        }
        model.addAttribute("myError", "OLD PASSWORD IS WRONG !!");
        return "account-changepassword";
    }
}
