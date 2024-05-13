package com.example.bookstore.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart
{
    @Id
    @Column(name = "user_id")
    private String userID;

    @OneToMany
    @JoinTable(name = "cart_line_items", joinColumns = @JoinColumn(name = "cart_id"), inverseJoinColumns = @JoinColumn(name = "line_item_id"))
    private List<LineItem> lineItemList ;


    public Cart() {
    }

    public Cart(List<LineItem> lineItemList, String userID) {
        this.lineItemList = lineItemList;
        this.userID = userID;
    }

    public List<LineItem> getLineItemList() {
        return lineItemList;
    }

    public void setLineItemList(List<LineItem> lineItemList) {
        this.lineItemList = lineItemList;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void addLineItem(LineItem lineItem)
    {
        if(lineItemList == null) lineItemList = new ArrayList<>();
        lineItemList.add(lineItem);
    }
}