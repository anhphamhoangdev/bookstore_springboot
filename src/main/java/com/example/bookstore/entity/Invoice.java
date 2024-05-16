package com.example.bookstore.entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Invoice {
    @Id
    @Column(name = "invoice_id")
    private String invoiceID;

    @Column(name = "first_name")
    private String firstName;

    private String address;

    @Column(name = "payment_method")
    private String paymentMethod;

    private String status;

    @Temporal(TemporalType.DATE)
    @Column(name = "order_date")
    private Date orderDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "sub_total")
    private double subTotal;

    @Column(name = "total")
    private double total;

    private int discount;

    @OneToMany(fetch = FetchType.LAZY)
    private List<LineItem> lineItems;

    public Invoice() {
    }

    public Invoice(String address, Date deliDate, int discount, String firstName, String invoiceID, List<LineItem> lineItem, Date orderDate, String paymentMethod, String status, Double subTotal, Double total) {
        this.address = address;
        this.deliveryDate = deliDate;
        this.discount = discount;
        this.firstName = firstName;
        this.invoiceID = invoiceID;
        this.lineItems = lineItem;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.subTotal = subTotal;
        this.total = total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItem(List<LineItem> lineItem) {
        this.lineItems = lineItem;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void addLineItem(LineItem lineItem)
    {
        if(lineItems == null) lineItems = new ArrayList<>();
        lineItems.add(lineItem);
    }
}
