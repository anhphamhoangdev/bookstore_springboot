package com.example.bookstore.entity;

import jakarta.persistence.*;


@Entity
public class LineItem {
    @Id
    @Column(name = "lineitem_id")
    private String lineItemID;


    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    public LineItem() {
    }

    public LineItem(Book book, String lineItemID, int quantity) {
        this.book = book;
        this.lineItemID = lineItemID;
        this.quantity = quantity;
    }


    public String getLineItemID() {
        return lineItemID;
    }

    public void setLineItemID(String lineItemID) {
        this.lineItemID = lineItemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }


}