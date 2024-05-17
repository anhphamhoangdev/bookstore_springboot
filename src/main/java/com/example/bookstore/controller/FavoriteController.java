package com.example.bookstore.controller;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Favorite;
import com.example.bookstore.entity.User;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.FavoriteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

    FavoriteService favoriteService;
    BookService bookService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService, BookService bookService) {
        this.favoriteService = favoriteService;
        this.bookService = bookService;
    }

    @RequestMapping("")
    public String favorite(){
        return "favorite";
    }



    @RequestMapping("/remove")
    public String remove(@RequestParam("id") String id, HttpSession session){
        User user = (User) session.getAttribute("user");
        Favorite favorite = favoriteService.findByUserId(user.getUserID());
        Book book = bookService.getBookById(id);
        if(book == null)
        {
            session.setAttribute("error", "BOOK NOT FOUND...");
            throw new EntityNotFoundException("Book not found");
        }
        favorite.deleteBook(book);
        favoriteService.merge(favorite);
        session.setAttribute("favorite", favorite);
        return "favorite";
    }

    @GetMapping("/add-to-favorite")
    public String addToFavorite(@RequestParam("id") String id, HttpSession session){
        User user = (User) session.getAttribute("user");
        Favorite favorite = favoriteService.findByUserId(user.getUserID());
        Book book = bookService.getBookById(id);
        if(book == null)
        {
            session.setAttribute("error", "BOOK NOT FOUND...");
            throw new EntityNotFoundException("Book not found");
        }
        if(!favorite.getBook().contains(book))
        {
            favorite.addBook(book);
            favoriteService.merge(favorite);

        }
        session.setAttribute("favorite", favorite);
        return "favorite";
    }


}
