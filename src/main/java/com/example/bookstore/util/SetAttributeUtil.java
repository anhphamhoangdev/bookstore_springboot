package com.example.bookstore.util;

import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Category;
import com.example.bookstore.service.AuthorService;
import com.example.bookstore.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.List;

public class SetAttributeUtil {
    private static SetAttributeUtil instance;

    private SetAttributeUtil() {
    }

    public static SetAttributeUtil getInstance() {
        if (instance == null) {
            synchronized (SetAttributeUtil.class) {
                if (instance == null) {
                    instance = new SetAttributeUtil();
                }
            }
        }
        return instance;
    }

    public String setAttributeString(Model model, HttpSession session, List<Book> bookList, CategoryService categoryService, AuthorService authorService) {
        model.addAttribute("books", bookList);
        session.setAttribute("books", bookList);
        List<Category> categories = categoryService.findALl();
        List<Author> authors = authorService.findALl();
        session.setAttribute("categories", categories);
        session.setAttribute("authors", authors);
        return "shop";
    }
}
