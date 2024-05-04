package com.example.bookstore.controller;

import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Category;
import com.example.bookstore.service.AuthorService;
import com.example.bookstore.service.CategoryService;
import com.example.bookstore.util.Util;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AuthorController {
    AuthorService authorService;
    CategoryService categoryService;
    Util util = new Util();

    @Autowired
    public AuthorController(AuthorService authorService, CategoryService categoryService) {
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @GetMapping("/author")
    public String findBookByAuthor(@RequestParam("id") String id, Model model, HttpSession session)
    {
        Author author = authorService.getAuthorById(id);
        List<Book> bookList = author.getBooks();
        return util.setInfoString(model, session, bookList, categoryService, authorService);
    }
}
