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
public class CategoryController {
    CategoryService categoryService;
    AuthorService authorService;
    Util util = new Util();
    @Autowired
    public CategoryController(CategoryService categoryService, AuthorService authorService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
    }

    @GetMapping("/category")
    public String findBookByCategory(@RequestParam("id") String id, Model model, HttpSession session)
    {
        Category category = categoryService.getCategoryById(id);
        List<Book> bookList = category.getBook();

        return util.setInfoString(model, session, bookList, categoryService, authorService);
    }
}
