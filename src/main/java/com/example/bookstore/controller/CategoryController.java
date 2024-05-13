package com.example.bookstore.controller;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Category;
import com.example.bookstore.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CategoryController {
    CategoryService categoryService;
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/category")
    public String findBookByCategory(@RequestParam("id") String id, HttpSession session)
    {
        Category category = categoryService.getCategoryById(id);
        if(category == null)
        {
            session.setAttribute("error", "CATEGORY NOT FOUND...");
            throw new EntityNotFoundException("CATEGORY NOT FOUND...");
        }
        List<Book> bookList = category.getBook();
        session.setAttribute("books", bookList);
        return "shop";
    }
}
