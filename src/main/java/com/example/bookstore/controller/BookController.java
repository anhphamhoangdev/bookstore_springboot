package com.example.bookstore.controller;

import com.example.bookstore.entity.*;
import com.example.bookstore.service.*;
import com.example.bookstore.util.GenerateID;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;

import java.util.*;


@Controller
public class BookController {
    BookService bookService;
    AuthorService authorService;
    CategoryService categoryService;
    PublisherService publisherService;
    StockService stockService;
    GenerateID generateID;


    @Autowired
    public BookController(BookService bookService, AuthorService authorService, CategoryService categoryService, PublisherService publisherService, StockService stockService, GenerateID generateID) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.publisherService = publisherService;
        this.stockService = stockService;
        this.generateID = generateID;
    }


    @GetMapping("/display-front")
    public ResponseEntity<byte[]> displayFrontImage(@RequestParam("id") String id) throws IOException, SQLException
    {
        Book image = bookService.getBookById(id);
        byte [] imageBytes = null;
        imageBytes = image.getBookFrontImage().getBytes(1,(int) image.getBookFrontImage().length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }

    @GetMapping("/display-back")
    public ResponseEntity<byte[]> displayBackImage(@RequestParam("id") String id) throws IOException, SQLException
    {
        Book image = bookService.getBookById(id);
        byte [] imageBytes = null;
        imageBytes = image.getBookBackImage().getBytes(1,(int) image.getBookBackImage().length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }



    @PostMapping("/save-book")
    public String addBook(@RequestParam("bookName") String bookName,
                          @RequestParam("importPrice") String importPrice,
                          @RequestParam("description") String description,
                          @RequestParam("language") String language,
                          @RequestParam("publishYear") String publishYear,
                          @RequestParam("publisher") String publisherID,
                          @RequestParam("category") String categoryID,
                          @RequestParam("author") String[] authors,
                          @RequestParam("frontImage") MultipartFile frontImage,
                          @RequestParam("backImage") MultipartFile backImage,
                          @RequestParam("quantity") String quantity)
            throws IOException, SerialException, SQLException {

        Book newBook = new Book();

        // image
        byte[] front_image_bytes = frontImage.getBytes();
        Blob frontBlob = new javax.sql.rowset.serial.SerialBlob(front_image_bytes);
        byte[] back_image_bytes = backImage.getBytes();
        Blob backBlob = new javax.sql.rowset.serial.SerialBlob(back_image_bytes);

        // get author, category, publisher
        Category category = categoryService.getCategoryById(categoryID);
        Publisher publisher = publisherService.getPublisherById(publisherID);

        newBook.setBookID(generateID.generateBookId());
        newBook.setBookName(bookName);
        newBook.setBookFrontImage(frontBlob);
        newBook.setBookBackImage(backBlob);
        for (String authorID : authors)
        {
            Author author = authorService.getAuthorById(authorID);
            newBook.addAuthor(author);
        }
        newBook.setLanguage(language);
        newBook.setPublishYear(Date.valueOf(publishYear));
        newBook.setCategory(category);
        newBook.setPublisher(publisher);
        newBook.setDescription(description);
        newBook.setSellPrice(Double.valueOf(importPrice) * 1.5);
        bookService.save(newBook);

        // stock
        Stock stock = new Stock();
        stock.setBookID(newBook.getBookID());
        stock.setQuantity(Integer.parseInt(quantity));
        stock.setImportPrice(Double.parseDouble(importPrice));
        stockService.save(stock);
        return "redirect:/";
    }



    @GetMapping("/search")
    public String index(@RequestParam("name") String bookName, HttpSession session)
    {
        List<Book> bookList = bookService.findBookByName(bookName);
        session.setAttribute("books", bookList);
        return "shop";
    }


    @GetMapping("/product-details")
    public String index(@RequestParam("id") String id, Model model, HttpSession session)
    {
        Book book = bookService.getBookById(id);
        if(book == null)
        {
            session.setAttribute("error", "BOOK NOT FOUND...");
            throw new EntityNotFoundException("Book not found");
        }
        Stock stock = stockService.getStockById(id);
        model.addAttribute("book", book);
        model.addAttribute("stock", stock);
        return "product-details";

    }

    @GetMapping("/price")
    public String searchByPrice(@RequestParam("min") double min, @RequestParam("max") double max, Model model, HttpSession session) {
        List<Book> bookList = bookService.findBookByPrice(min, max);
        session.setAttribute("books", bookList);
        return "shop";
    }


    @GetMapping("/sort-price")
    public String sortByPrice(@RequestParam("order") String order, HttpSession session, Model model) {
        // Xử lí giá trị đã chọn ở đây
        List<Book> bookList = (List<Book>) session.getAttribute("books");
        if(order.equals("asc"))
        {
            bookList.sort(Comparator.comparingDouble(Book::getSellPrice));
            model.addAttribute("order", "asc");
        }
        else if(order.equals("desc"))
        {
            bookList.sort(Comparator.comparingDouble(Book::getSellPrice).reversed());
            model.addAttribute("order", "desc");
        } else model.addAttribute("order", "default");
        session.setAttribute("books", bookList);
        return "shop";
    }


}
