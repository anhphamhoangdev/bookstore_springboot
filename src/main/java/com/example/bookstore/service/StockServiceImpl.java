package com.example.bookstore.service;


import com.example.bookstore.entity.Stock;
import com.example.bookstore.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl implements StockService {

    private StockRepository stockRepository;

    @Autowired
    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    @Transactional
    public void save(Stock stock) {
        stockRepository.save(stock);
    }

    @Override
    public Stock getStockById(String id) {
        return stockRepository.getReferenceById(id);
    }


}
