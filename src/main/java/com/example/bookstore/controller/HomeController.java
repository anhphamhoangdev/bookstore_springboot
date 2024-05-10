package com.example.bookstore.controller;

import com.example.bookstore.entity.*;
import com.example.bookstore.service.*;
import com.example.bookstore.util.GenerateID;
import com.example.bookstore.util.SetAttributeUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;


@Controller
public class HomeController {

    BookService bookService;
    AuthorService authorService;
    CategoryService categoryService;
    PublisherService publisherService;
    StockService stockService;
    UserService userService;
    GenerateID generateID;


    @Autowired
    public HomeController(BookService bookService, AuthorService authorService, CategoryService categoryService, PublisherService publisherService, StockService stockService, UserService userService, GenerateID generateID) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.publisherService = publisherService;
        this.stockService = stockService;
        this.userService = userService;
        this.generateID = generateID;
    }

    @RequestMapping("/")
    public String index(Model model, HttpSession session)
    {
        List<Book> bookList = bookService.findAll();
        SetAttributeUtil.getInstance().setAttributeString(model, session, bookList, categoryService, authorService);
        if(session.getAttribute("SPRING_SECURITY_CONTEXT") != null)
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            session.setAttribute("user", user);
        }
        session.setAttribute("order","default");
        return "shop";
    }


    @GetMapping("/add-book-test")
    public String index(Model model)
    {
        // languages
        Locale[] locales = Locale.getAvailableLocales();
        Set<String> uniqueLanguages = new HashSet<>();
        for (Locale locale : locales) {
            String language = locale.getDisplayLanguage();
            if (!uniqueLanguages.contains(language)) {
                uniqueLanguages.add(language);
            }
        }
        String[] uniqueLanguageArray = uniqueLanguages.toArray(new String[0]);
        Arrays.sort(uniqueLanguageArray);

        // authors
        List<Author> authors = authorService.findALl();

        // categories
        List<Category> categories = categoryService.findALl();

        // publishers
        List<Publisher> publishers = publisherService.findAll();


        model.addAttribute("languages", uniqueLanguageArray);
        model.addAttribute("authors", authors);
        model.addAttribute("categories", categories);
        model.addAttribute("publishers", publishers);
        return "addnewbook";
    }
}
