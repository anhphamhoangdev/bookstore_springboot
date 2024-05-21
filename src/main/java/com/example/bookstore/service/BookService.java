package com.example.bookstore.service;

import com.example.bookstore.entity.Book;

import java.util.List;

public interface BookService {
    public List<Book> findAll();

    public Book getBookById(String id);

    public void save(Book book);

    public List<Book> findBookByName(String name);

    public List<Book> findBookByPrice(double min, double max);

    public void merge(Book book);
}
