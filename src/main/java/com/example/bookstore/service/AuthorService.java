package com.example.bookstore.service;

import com.example.bookstore.entity.Author;

import java.util.List;

public interface AuthorService {

    public List<Author> findALl();

    public Author getAuthorById(String id);
}
