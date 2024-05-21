package com.example.bookstore.controller;

import com.example.bookstore.entity.*;
import com.example.bookstore.service.*;
import com.example.bookstore.util.GenerateID;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final GenerateID generateID;
    private InvoiceService invoiceService;
    private PublisherService publisherService;
    private UserService userService;
    private AuthorService authorService;
    private CategoryService categoryService;
    private BookService bookService;
    private StockService stockService;
    private RoleService roleService;

    @Autowired
    public AdminController(InvoiceService invoiceService, PublisherService publisherService, UserService userService, GenerateID generateID, AuthorService authorService, CategoryService categoryService, BookService bookService, StockService stockService, RoleService roleService ) {
        this.invoiceService = invoiceService;
        this.publisherService = publisherService;
        this.userService = userService;
        this.generateID = generateID;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.bookService = bookService;
        this.stockService = stockService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String admin(HttpSession session){
        session.setAttribute("allSubTotal", invoiceService.getAllSubTotal());
        session.setAttribute("allTotal", invoiceService.getAllTotal());
        double profit = 0.85*(invoiceService.getAllSubTotal()) - (invoiceService.getAllSubTotal() - invoiceService.getAllTotal());
        session.setAttribute("profit",profit);
        return "admin/adminpage";
    }

    @GetMapping("/authortable")
    public String authortable(Model model){
        model.addAttribute("authors", authorService.findALl());
        return "admin/authortable";
    }

    @GetMapping("/categorytable")
    public String categorytable(Model model){
        model.addAttribute("categories", categoryService.findALl());
        return "admin/categorytable";
    }

    @GetMapping("/booktable")
    public String booktable(){
        return "admin/booktable";
    }

    @GetMapping("/publishertable")
    public String publishertable(Model model){
        model.addAttribute("publishers", publisherService.findAll());
        return "admin/publishertable";
    }

    @GetMapping("/invoicetable")
    public String invoicetable(Model model){
        model.addAttribute("invoices", invoiceService.findAll());
        return "admin/invoicetable";
    }

    @GetMapping("/usertable")
    public String userTable(Model model){
        model.addAttribute("users", userService.findAll());
        return "admin/usertable";
    }

    /*
    <a class="collapse-item" th:href="@{/admin/add-new-author}">Author</a>
                    <a class="collapse-item" th:href="@{/admin/add-new-category}">Category</a>
                    <a class="collapse-item" th:href="@{/admin/add-new-publisher}">Publisher</a>
                    <a class="collapse-item" th:href="@{/admin/add-new-book}">Book</a>
                    <a class="collapse-item" th:href="@{/admin/add-new-user}">User</a>
     */

    @GetMapping("/add-new-author")
    public String addAuthor(Model model){
        Author author = new Author();
        model.addAttribute("author", author);
        return "admin/add-new-author";
    }

    @PostMapping("/process-add-author")
    public String processAddAuthor(@ModelAttribute("author") Author author){
        author.setAuthorID(generateID.generateAuthorId());
        authorService.save(author);
        return "redirect:/admin/authortable";
    }

    @GetMapping("/add-new-category")
    public String addCategory(Model model){
        Category category = new Category();
        model.addAttribute("category", category);
        return "admin/add-new-category";
    }

    @PostMapping("/process-add-category")
    public String processAddAuthor(@ModelAttribute("category") Category category){
        category.setCategoryID(generateID.generateCategoryId());
        categoryService.save(category);
        return "redirect:/admin/categorytable";
    }

    @GetMapping("/add-new-publisher")
    public String addPublisher(Model model){
        Publisher publisher = new Publisher();
        model.addAttribute("publisher", publisher);
        return "admin/add-new-publisher";
    }

    @PostMapping("/process-add-publisher")
    public String processAddAuthor(@ModelAttribute("publisher") Publisher publisher){
        publisher.setPublisherID(generateID.generatePublisherId());
        publisherService.save(publisher);
        return "redirect:/admin/publishertable";
    }

    @GetMapping("/add-new-book")
    public String addBook(Model model){
        // languages
        Locale[] locales = Locale.getAvailableLocales();
        Set<String> uniqueLanguages = new HashSet<>();
        for (Locale locale : locales) {
            String language = locale.getDisplayLanguage();
            if (!uniqueLanguages.contains(language)) {
                uniqueLanguages.add(language);
            }
        }
        String[] uniqueLanguageArray = uniqueLanguages.toArray(new String[0]);
        Arrays.sort(uniqueLanguageArray);

        // authors
        List<Author> authors = authorService.findALl();

        // categories
        List<Category> categories = categoryService.findALl();

        // publishers
        List<Publisher> publishers = publisherService.findAll();


        model.addAttribute("languages", uniqueLanguageArray);
        model.addAttribute("authors", authors);
        model.addAttribute("categories", categories);
        model.addAttribute("publishers", publishers);
        return "admin/add-new-book";
    }

    @PostMapping("/process-add-book")
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
            throws IOException, SQLException {

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


        // stock
        Stock stock = new Stock();
        stock.setStockID(generateID.generateStockId());
        newBook.setStock(stock);
        stock.setQuantity(Integer.parseInt(quantity));
        stock.setImportPrice(Double.parseDouble(importPrice));
        stockService.save(stock);
        bookService.save(newBook);
        return "redirect:/admin/booktable";
    }


    @GetMapping("/add-new-user")
    public String addUser(Model model){
        User user = new User();
        model.addAttribute("newUser", user);
        return "admin/add-new-user";
    }

    @PostMapping("/process-add-admin")
    public String processAddAdmin(@ModelAttribute("newUser") User user, @RequestParam("password") String password, Model model){
        User existedUser = userService.findByEmail(user.getEmail());
        if(existedUser != null)
        {
            user.setEmail("");
            model.addAttribute("newUser", user);
            model.addAttribute("myError", "Failed ! Email is existed.");
            return "admin/add-new-user";
        }
        existedUser = userService.findByUsername(user.getUsername());
        if(existedUser != null)
        {
            user.setUsername("");
            model.addAttribute("newUser", user);
            model.addAttribute("myError", "Failed ! Username is existed.");
            return "admin/add-new-user";
        }

        // existedUser not exist => create new user
        user.setUserID(generateID.generateUserId());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setEnabled(true);
        Role role = roleService.findByName("ROLE_ADMIN");
        user.addRoles(role);
        userService.save(user);
        return "redirect:/admin/usertable";
    }

    @PostMapping("/edit-user")
    public String editUser(Model model, @RequestParam("id") String id){
        User user = userService.findById(id);
        model.addAttribute("editUser", user);
        return "admin/edit-user";
    }

    @PostMapping("/process-edit-user")
    public String processAddAuthor(@ModelAttribute("editUser") User user){
        User editUser = userService.findById(user.getUserID());
        editUser.setEnabled(user.getEnabled());
        editUser.setFirstName(user.getFirstName());
        editUser.setLastName(user.getLastName());
        editUser.setAddress(user.getAddress());
        editUser.setPhoneNum(user.getPhoneNum());
        userService.merge(user);
        return "redirect:/admin/usertable";
    }

    @PostMapping("/edit-book")
    public String editBook(Model model, @RequestParam("id") String id){
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "admin/edit-book";
    }

    @PostMapping("/process-edit-book")
    public String processEditBook(@ModelAttribute("book") Book editBook, @RequestParam("bookImportPrice") Double importPrice, @RequestParam("bookQuantity") int quantity){
        Book book = bookService.getBookById(editBook.getBookID());
        book.setDescription(editBook.getDescription());
        Stock stock = stockService.getStockById(book.getStock().getStockID());
        stock.setQuantity(quantity);
        stock.setImportPrice(importPrice);
        book.setSellPrice(importPrice*1.15);
        bookService.merge(book);
        stockService.merge(stock);
        return "redirect:/admin/booktable";
    }



}
