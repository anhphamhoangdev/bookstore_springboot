package com.example.bookstore.service;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Stock;

import java.util.List;

public interface StockService {

    public void save(Stock stock);

    public Stock getStockById(String id);



}
