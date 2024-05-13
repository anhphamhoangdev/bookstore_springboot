package com.example.bookstore.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Favorite {
    @Id
    @Column(name = "user_id")
    private String userID;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "favorite_book",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> books;


    public Favorite() {
        books = new ArrayList<>();
    }

    public List<Book> getBook() {
        return books;
    }

    public void setBook(List<Book> book) {
        this.books = book;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void addBook(Book book) {
        if(books == null) books = new ArrayList<>();
        books.add(book);
    }
}
