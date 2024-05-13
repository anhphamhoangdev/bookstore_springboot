package com.example.bookstore.service;

import com.example.bookstore.entity.Favorite;

public interface FavoriteService {

    public void save(Favorite favorite);

    public Favorite findByUserId(String userId);
}
