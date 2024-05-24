package com.example.bookstore.repository;

import com.example.bookstore.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    @Query("SELECT SUM(i.subTotal) FROM Invoice i")
    Double calculateSubtotalSum();

    @Query("SELECT SUM(i.total) FROM Invoice i")
    Double calculateTotalSum();
}


