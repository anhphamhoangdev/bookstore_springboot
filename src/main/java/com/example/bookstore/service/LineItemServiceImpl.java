package com.example.bookstore.service;

import com.example.bookstore.entity.LineItem;
import com.example.bookstore.repository.LineItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LineItemServiceImpl implements LineItemService {

    private LineItemRepository lineItemRepository;

    @Autowired
    public LineItemServiceImpl(LineItemRepository lineItemRepository) {
        this.lineItemRepository = lineItemRepository;
    }

    public LineItemRepository getLineItemRepository() {
        return lineItemRepository;
    }

    public void setLineItemRepository(LineItemRepository lineItemRepository) {
        this.lineItemRepository = lineItemRepository;
    }

    @Override
    @Transactional
    public void save(LineItem lineItem) {
        lineItemRepository.save(lineItem);
    }

    @Override
    public void merge(LineItem lineItem) {
        lineItemRepository.saveAndFlush(lineItem);
    }

    @Override
    public void delete(String id) {
        lineItemRepository.deleteById(id);
    }
}
