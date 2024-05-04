package com.example.bookstore.service;

import com.example.bookstore.entity.Stock;

public interface StockService {

    public void save(Stock stock);

    public Stock getStockById(String id);


}
