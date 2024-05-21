package com.example.bookstore.service;

import com.example.bookstore.entity.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> findALl();

    public Category getCategoryById(String id);

    public Category save(Category category);
}
