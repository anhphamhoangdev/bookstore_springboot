package com.example.bookstore;

import com.example.bookstore.entity.*;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CategoryRepository;
import com.example.bookstore.repository.PublisherRepository;
import com.example.bookstore.service.*;
import com.example.bookstore.util.EmailSenderService;
import com.example.bookstore.util.GenerateID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@SpringBootApplication
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner(ResourceLoader resourceLoader, BookService bookService, StockService stockService , PublisherService publisherService, CategoryService categoryService , AuthorService authorService , FavoriteService favoriteService, CartService cartService, UserService userService , GenerateID generateID, DiscountService discountService, RoleService roleService)
    {
        return runner -> {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


            // ========================== DISCOUNT ============================= //
            Discount discount = new Discount();
            discount.setDiscountAmount(10);
            discount.setDiscountCode(discountService.createDiscountCode());
            discountService.save(discount);

            // ========================== ROLES ============================= //
            Role role_admin = new Role();
            role_admin.setId(1L);
            role_admin.setName("ROLE_ADMIN");
            roleService.save(role_admin);

            Role role_user = new Role();
            role_user.setId(2L);
            role_user.setName("ROLE_USER");
            roleService.save(role_user);

            // ========================== ADMIN ============================= //
            User admin = new User();
            admin.setUserID(generateID.generateUserId());
            admin.setFirstName("Anh");
            admin.setLastName("Pham");
            admin.setPassword(bCryptPasswordEncoder.encode("1"));
            admin.setUsername("admin");
            admin.setEmail("anhphamhoang033@gmail.com");
            admin.setEnabled(true);
            admin.addRoles(role_admin);
            userService.save(admin);


            // ========================== USER ============================= //
            User user = new User();
            user.setUserID(generateID.generateUserId());
            user.setFirstName("Anh");
            user.setLastName("Pham");
            user.setPassword(bCryptPasswordEncoder.encode("1"));
            user.setUsername("user");
            user.setEmail("21110753@student.hcmute.edu.vn");
            user.setEnabled(true);
            user.addRoles(role_user);
            userService.save(user);


            // ========================== CART FOR USER ============================= //
            Cart admin_cart = new Cart();
            admin_cart.setUserID(admin.getUserID());
            cartService.save(admin_cart);

            Cart user_cart = new Cart();
            user_cart.setUserID(user.getUserID());
            cartService.save(user_cart);

            // ========================== FAVORITE FOR USER ============================= //
            Favorite admin_favorite = new Favorite();
            admin_favorite.setUserID(admin.getUserID());
            favoriteService.save(admin_favorite);

            Favorite user_favorite = new Favorite();
            user_favorite.setUserID(user.getUserID());
            favoriteService.save(user_favorite);



            // ========================== AUTHOR ============================= //
            Author author = new Author();
            author.setAuthorID(generateID.generateAuthorId());
            author.setAuthorName("Kristin Hannah");
            authorService.save(author);

            Author author1 = new Author();
            author1.setAuthorID(generateID.generateAuthorId());
            author1.setAuthorName("Emily Henry");
            authorService.save(author1);

            Author author2 = new Author();
            author2.setAuthorID(generateID.generateAuthorId());
            author2.setAuthorName("Freida McFadden");
            authorService.save(author2);



            // ========================== CATEGORY ============================= //
            Category category = new Category();
            category.setCategoryID(generateID.generateCategoryId());
            category.setCategoryName("Novel");
            categoryService.save(category);


            Category category1 = new Category();
            category1.setCategoryID(generateID.generateCategoryId());
            category1.setCategoryName("Psychological thriller");
            categoryService.save(category1);

            Category category2 = new Category();
            category2.setCategoryID(generateID.generateCategoryId());
            category2.setCategoryName("Fiction");
            categoryService.save(category2);




            // ========================== PUBLISHER ============================= //
            Publisher publisher = new Publisher();
            publisher.setPublisherID(generateID.generatePublisherId());
            publisher.setPublisherName("St. Martin's Press");
            publisherService.save(publisher);

            Publisher publisher1 = new Publisher();
            publisher1.setPublisherID(generateID.generatePublisherId());
            publisher1.setPublisherName("Grand Central Publishing");
            publisherService.save(publisher1);

            Publisher publisher2 = new Publisher();
            publisher2.setPublisherID(generateID.generatePublisherId());
            publisher2.setPublisherName("Berkley");
            publisherService.save(publisher2);



            // ========================== BOOKS ============================= //

            // BOOK 1
            Book the_women = new Book();

            the_women.setBookID(generateID.generateBookId());
            the_women.setBookName("The Women: A Novel");
            the_women.setLanguage("English");
            the_women.setPublishYear(new Date(2024,2,6));
            the_women.setDescription("A #1 bestseller on The New York Times, USA Today, Washington Post, and Los Angeles Times!");
            double importPrice = 15.0;
            the_women.setSellPrice(importPrice * 1.5);

            // set image
            the_women.setBookFrontImage(getImageBlobFromPath("thewomen_front.jpg"));
            the_women.setBookBackImage(getImageBlobFromPath("thewomen_back.jpg"));
            // set author, category, publisher
            the_women.addAuthor(author);
            the_women.setCategory(category);
            the_women.setPublisher(publisher);
            bookService.save(the_women);
            // add book to stock
            Stock stock = new Stock();
            stock.setStockID(generateID.generateStockId());
            the_women.setStock(stock);
            stock.setBook(the_women);
            stock.setQuantity(100);
            stock.setImportPrice(importPrice);
            stockService.save(stock);

        };
    }

    public Blob getImageBlobFromPath(String imagePath) {
        try {
            Resource resource = new ClassPathResource("static/book_images/" + imagePath);
            InputStream inputStream = resource.getInputStream();
            byte[] imageBytes = inputStream.readAllBytes();
            inputStream.close();
            Blob imageBlob = new SerialBlob(imageBytes);
            return imageBlob;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
