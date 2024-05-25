package com.example.bookstore.service;

import com.example.bookstore.entity.Role;

public interface RoleService {
    Role findByName(String name);

    void save(Role role);
}
