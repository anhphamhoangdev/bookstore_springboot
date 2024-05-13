package com.example.bookstore.controller;

import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AuthorController {
    AuthorService authorService;


    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/author")
    public String findBookByAuthor(@RequestParam("id") String id, HttpSession session)
    {
        Author author = authorService.getAuthorById(id);
        if(author == null)
        {
            session.setAttribute("error", "AUTHOR NOT FOUND...");
            throw new EntityNotFoundException("Author not found");
        }

        List<Book> bookList = author.getBooks();
        session.setAttribute("books", bookList);
        return "shop";

    }
}
