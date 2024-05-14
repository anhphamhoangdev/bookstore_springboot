package com.example.bookstore.repository;

import com.example.bookstore.entity.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineItemRepository extends JpaRepository<LineItem, String> {
}
