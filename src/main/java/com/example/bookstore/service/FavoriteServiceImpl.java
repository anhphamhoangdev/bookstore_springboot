package com.example.bookstore.service;

import com.example.bookstore.entity.Favorite;
import com.example.bookstore.repository.FavoriteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private FavoriteRepository favoriteRepository;

    @Autowired
    public FavoriteServiceImpl(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    @Transactional
    public void save(Favorite favorite) {
        favoriteRepository.save(favorite);
    }

    @Override
    public Favorite findByUserId(String userId) {
        return favoriteRepository.findById(userId).orElse(null);
    }

    @Override
    public Favorite merge(Favorite favorite) {
        return favoriteRepository.saveAndFlush(favorite);
    }


}
