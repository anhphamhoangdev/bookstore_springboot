package com.example.bookstore.entity;

import com.example.bookstore.service.StockService;
import com.example.bookstore.service.StockServiceImpl;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Blob;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "book_id")
    private String bookID;
    @Column(name = "book_name")
    private String bookName;
    @Column(name = "sell_price")
    private Double sellPrice;
    @Column(name = "description")
    private String description;
    @Column(name = "language")
    private String language;
    @Column(name = "publish_year")
    @Temporal(TemporalType.DATE)
    private Date publishYear;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
    })
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.REFRESH
            })
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
    })
    @JoinColumn(name = "category_id")
    private Category category;


    @OneToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;


    @Lob
    private Blob bookFrontImage;

    @Lob
    private Blob bookBackImage;

    public Book() {
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Date getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Date publishYear) {
        this.publishYear = publishYear;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Blob getBookFrontImage() {
        return bookFrontImage;
    }

    public void setBookFrontImage(Blob bookFrontImage) {
        this.bookFrontImage = bookFrontImage;
    }

    public Blob getBookBackImage() {
        return bookBackImage;
    }

    public void setBookBackImage(Blob bookBackImage) {
        this.bookBackImage = bookBackImage;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

// method

    @Override
    public String toString() {
        return "Book{" +
                "bookID='" + bookID + '\'' +
                ", bookName='" + bookName + '\'' +
                ", authors=" + authors +
                '}';
    }

    public void addAuthor(Author author)
    {
        if(authors == null) authors = new ArrayList<>();
        authors.add(author);
    }


}
