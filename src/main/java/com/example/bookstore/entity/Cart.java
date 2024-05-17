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
    @JoinTable(name = "cart_line_items",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "line_item_id"))
    private List<LineItem> lineItemList ;

    private int discount;

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

    public void deleteLineItem(LineItem lineItem) {
        if(lineItemList.contains(lineItem)) lineItemList.remove(lineItem);
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Double subTotal() {
        double subtotal = 0;
        for(LineItem lineItem : this.lineItemList)
        {
            subtotal += lineItem.getQuantity()*lineItem.getBook().getSellPrice();
        }
        return subtotal;
    }

    public Double total() {
        double subtotal = subTotal();
        if(subtotal == 0) return subtotal;
        double total;
        double discountPercentage = (double) discount / 100.0;
        if(subtotal > 100)
        {
            total = subtotal - (subtotal*discountPercentage);
        }
        else
        {
            total = subtotal + 10 - (subtotal*discountPercentage);
        }
        return total;
    }
}
