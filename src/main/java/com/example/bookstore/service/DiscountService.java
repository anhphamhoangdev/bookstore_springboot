package com.example.bookstore.service;

import com.example.bookstore.entity.Discount;

import java.util.List;

public interface DiscountService {
    public void save(Discount discount);

    public String createDiscountCode();

    public Discount findById(String id);

    public List<Discount> findAll();

    public void merge(Discount discount);
}
