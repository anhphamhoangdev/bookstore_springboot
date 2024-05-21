package com.example.bookstore.service;

import com.example.bookstore.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    public void save(User user);
    public User findByUsername(String username);
    public User findByEmail(String email);

    public void merge(User user);

    public List<User> findAll();

    public User findById(String id);
}
