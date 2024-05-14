package com.example.bookstore.service;

import com.example.bookstore.entity.LineItem;

public interface LineItemService {
    public void save(LineItem lineItem);

    public void merge(LineItem lineItem);

    public void delete(String id);
}
