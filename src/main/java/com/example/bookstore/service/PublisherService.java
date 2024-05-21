package com.example.bookstore.service;

import com.example.bookstore.entity.Publisher;

import java.util.List;

public interface PublisherService {
    public List<Publisher> findAll();

    public Publisher getPublisherById(String id);

    public Publisher save(Publisher publisher);
}
