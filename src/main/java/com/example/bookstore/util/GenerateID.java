package com.example.bookstore.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class GenerateID {
    private EntityManager entityManager;

    @Autowired
    public GenerateID(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public String generateId(String query, String prefix) {
        String lastId;
        try {
            TypedQuery<String> query1 = entityManager.createQuery(query, String.class);
            query1.setMaxResults(1);
            lastId = query1.getSingleResult();
        } catch (NoResultException e) {
            return prefix + "0000";
        }
        int number = Integer.parseInt(lastId.substring(4));
        number++;
        return String.format("%s%04d", prefix, number);
    }

    public String generateAuthorId() {
        String query = "SELECT a.authorID FROM Author a ORDER BY a.authorID DESC";
        return generateId(query, "AUTH");
    }

    public String generateBookId() {
        String query = "SELECT b.bookID FROM Book b ORDER BY b.bookID DESC";
        return generateId(query, "BOOK");
    }

    public String generateCategoryId() {
        String query = "SELECT c.categoryID FROM Category c ORDER BY c.categoryID DESC";
        return generateId(query, "CATE");
    }

    public String generatePublisherId() {
        String query = "SELECT p.publisherID FROM Publisher p ORDER BY p.publisherID DESC";
        return generateId(query, "PUBL");
    }

    public String generateUserId()
    {
        String query = "SELECT u.userID FROM User u ORDER BY u.userID DESC";
        return generateId(query, "USER");
    }
}
