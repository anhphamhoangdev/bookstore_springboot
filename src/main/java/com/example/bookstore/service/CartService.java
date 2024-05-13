package com.example.bookstore.service;

import com.example.bookstore.entity.Cart;
import com.example.bookstore.entity.Favorite;

public interface CartService {
    public void save(Cart cart);

    public Cart findByUserId(String userId);

}
