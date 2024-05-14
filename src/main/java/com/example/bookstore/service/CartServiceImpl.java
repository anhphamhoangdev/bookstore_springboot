package com.example.bookstore.service;

import com.example.bookstore.entity.Cart;
import com.example.bookstore.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private CartRepository cartRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional
    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public void merge(Cart cart) {
        cartRepository.saveAndFlush(cart);
    }

    @Override
    public Cart findByUserId(String userId) {
        return cartRepository.findById(userId).orElse(null);
    }




}
