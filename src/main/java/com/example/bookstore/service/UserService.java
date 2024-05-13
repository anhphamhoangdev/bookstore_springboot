package com.example.bookstore.service;

import com.example.bookstore.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public void save(User user);
    public User findByUsername(String username);
    public User findByEmail(String email);
}
