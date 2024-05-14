package com.example.bookstore.entity;

import jakarta.persistence.*;

@Entity
public class Stock {

    @Id
    private String stockID;


    private int quantity;

    @Column(name = "import_price")
    private double importPrice;

    @OneToOne(mappedBy = "stock")
    private Book book;

    public Stock() {
    }

//Getter and setter


    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
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
