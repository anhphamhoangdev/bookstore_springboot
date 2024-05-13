package com.example.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class LineItem {
    @Id
    @Column(name = "lineitem_id")
    private String lineItemID;


    private int quantity;

    @Column(name = "book_id")
    private String bookID;

    public LineItem() {
    }

    public LineItem(String bookID, String lineItemID, Integer quantity) {
        this.bookID = bookID;
        this.lineItemID = lineItemID;
        this.quantity = quantity;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getLineItemID() {
        return lineItemID;
    }

    public void setLineItemID(String lineItemID) {
        this.lineItemID = lineItemID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}