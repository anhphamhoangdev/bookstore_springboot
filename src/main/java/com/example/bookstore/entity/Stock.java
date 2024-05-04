package com.example.bookstore.entity;

import jakarta.persistence.*;

@Entity
public class Stock {
    @Id
    @Column(name = "book_id")
    private String bookID;
    private int quantity;

    @Column(name = "import_price")
    private double importPrice;


    //Getter and setter


    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(double importPrice) {
        this.importPrice = importPrice;
    }
}
