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

    // COMMENT START
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
            Author author_kristin_hannah = new Author();
            author_kristin_hannah.setAuthorID(generateID.generateAuthorId());
            author_kristin_hannah.setAuthorName("Kristin Hannah");
            authorService.save(author_kristin_hannah);

            Author author_emily_henry = new Author();
            author_emily_henry.setAuthorID(generateID.generateAuthorId());
            author_emily_henry.setAuthorName("Emily Henry");
            authorService.save(author_emily_henry);

            Author author_freida_mcfadden = new Author();
            author_freida_mcfadden.setAuthorID(generateID.generateAuthorId());
            author_freida_mcfadden.setAuthorName("Freida McFadden");
            authorService.save(author_freida_mcfadden);

            Author author_evie_woods = new Author();
            author_evie_woods.setAuthorID(generateID.generateAuthorId());
            author_evie_woods.setAuthorName("Evie Woods");
            authorService.save(author_evie_woods);





            // ========================== CATEGORY ============================= //
            Category category_novel = new Category();
            category_novel.setCategoryID(generateID.generateCategoryId());
            category_novel.setCategoryName("Novel");
            categoryService.save(category_novel);


            Category category_psychological_thriller = new Category();
            category_psychological_thriller.setCategoryID(generateID.generateCategoryId());
            category_psychological_thriller.setCategoryName("Psychological thriller");
            categoryService.save(category_psychological_thriller);

            Category category_fiction = new Category();
            category_fiction.setCategoryID(generateID.generateCategoryId());
            category_fiction.setCategoryName("Fiction");
            categoryService.save(category_fiction);




            // ========================== PUBLISHER ============================= //
            Publisher publisher = new Publisher();
            publisher.setPublisherID(generateID.generatePublisherId());
            publisher.setPublisherName("St. Martin's Press");
            publisherService.save(publisher);

            Publisher publisher1 = new Publisher();
            publisher1.setPublisherID(generateID.generatePublisherId());
            publisher1.setPublisherName("Bookouture");
            publisherService.save(publisher1);

            Publisher publisher2 = new Publisher();
            publisher2.setPublisherID(generateID.generatePublisherId());
            publisher2.setPublisherName("Berkley");
            publisherService.save(publisher2);

            Publisher publisher_Balaji_Publications = new Publisher();
            publisher_Balaji_Publications.setPublisherID(generateID.generatePublisherId());
            publisher_Balaji_Publications.setPublisherName("Balaji Publications");
            publisherService.save(publisher_Balaji_Publications);


            Publisher publisher_One_More_Chapter = new Publisher();
            publisher_One_More_Chapter.setPublisherID(generateID.generatePublisherId());
            publisher_One_More_Chapter.setPublisherName("One More Chapter");
            publisherService.save(publisher_One_More_Chapter);


            // ========================== BOOKS ============================= //

            // BOOK 1 : THE WOMEN : A NOVEL
            Book the_women = new Book();

            the_women.setBookID(generateID.generateBookId());
            the_women.setBookName("The Women: A Novel");
            the_women.setLanguage("English");
            the_women.setPublishYear(new Date(124,2,6));
            the_women.setDescription("A #1 bestseller on The New York Times, USA Today, Washington Post, and Los Angeles Times!");
            double importPrice = 15.0;
            the_women.setSellPrice(importPrice * 1.15);

            // set image
            the_women.setBookFrontImage(getImageBlobFromPath("thewomen_front.jpg"));
            the_women.setBookBackImage(getImageBlobFromPath("thewomen_back.jpg"));
            // set author, category, publisher
            author_kristin_hannah.addBook(the_women);
            the_women.addAuthor(author_kristin_hannah);
            the_women.setCategory(category_novel);
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
            bookService.merge(the_women);


            // BOOK 2 : THE HOUSEMAID
            Book the_housemaid = new Book();

            the_housemaid.setBookID(generateID.generateBookId());
            the_housemaid.setBookName("The Housemaid");
            the_housemaid.setLanguage("English");
            the_housemaid.setPublishYear(new Date(122,4,26));
            the_housemaid.setDescription("An absolutely addictive psychological thriller with a jaw-dropping twist");
            double the_housemaid_importPrice = 8.0;
            the_housemaid.setSellPrice(the_housemaid_importPrice * 1.15);

            // set image
            the_housemaid.setBookFrontImage(getImageBlobFromPath("thehousemaid_front.jpg"));
            the_housemaid.setBookBackImage(getImageBlobFromPath("thehousemaid_back.jpg"));
            // set author, category, publisher
            the_housemaid.addAuthor(author_freida_mcfadden);
            the_housemaid.setCategory(category_psychological_thriller);
            the_housemaid.setPublisher(publisher1);
            bookService.save(the_housemaid);

            // add book to stock
            Stock the_housemaid_stock = new Stock();
            the_housemaid_stock.setStockID(generateID.generateStockId());
            the_housemaid.setStock(the_housemaid_stock);

            the_housemaid_stock.setBook(the_housemaid);
            the_housemaid_stock.setQuantity(100);
            the_housemaid_stock.setImportPrice(the_housemaid_importPrice);
            stockService.save(the_housemaid_stock);
            bookService.merge(the_housemaid);

            // BOOK 3 : FUNNY STORY
            Book funny_story = new Book();

            funny_story.setBookID(generateID.generateBookId());
            funny_story.setBookName("Funny Story");
            funny_story.setLanguage("English");
            funny_story.setPublishYear(new Date(124,4,23));
            funny_story.setDescription("A shimmering, joyful new novel about a pair of opposites with the wrong thing in common.");
            double funny_story_importPrice = 13.0;
            funny_story.setSellPrice(funny_story_importPrice * 1.15);

            // set image
            funny_story.setBookFrontImage(getImageBlobFromPath("funnystory_front.jpg"));
            funny_story.setBookBackImage(getImageBlobFromPath("funnystory_back.jpeg"));
            // set author, category, publisher
            funny_story.addAuthor(author_emily_henry);
            funny_story.setCategory(category_fiction);
            funny_story.setPublisher(publisher2);
            bookService.save(funny_story);

            // add book to stock
            Stock funny_story_stock = new Stock();
            funny_story_stock.setStockID(generateID.generateStockId());
            funny_story.setStock(funny_story_stock);

            funny_story_stock.setBook(funny_story);
            funny_story_stock.setQuantity(100);
            funny_story_stock.setImportPrice(funny_story_importPrice);
            stockService.save(funny_story_stock);
            bookService.merge(funny_story);


            // BOOK 4 : The Lost Bookshop
            Book lost_bookshop = new Book();

            lost_bookshop.setBookID(generateID.generateBookId());
            lost_bookshop.setBookName("Lost Bookshop");
            lost_bookshop.setLanguage("English");
            lost_bookshop.setPublishYear(new Date(123,6,22));
            lost_bookshop.setDescription("The most charming and uplifting novel for 2024 and the perfect gift for book lovers!");
            double lost_bookshop_importPrice = 13.0;
            lost_bookshop.setSellPrice(lost_bookshop_importPrice * 1.15);

            // set image
            lost_bookshop.setBookFrontImage(getImageBlobFromPath("lostbookshop_front.jpg"));
            lost_bookshop.setBookBackImage(getImageBlobFromPath("lostbookshop_back.webp"));
            // set author, category, publisher
            lost_bookshop.addAuthor(author_evie_woods);
            lost_bookshop.setCategory(category_fiction);
            lost_bookshop.setPublisher(publisher_One_More_Chapter);
            bookService.save(lost_bookshop);

            // add book to stock
            Stock lost_bookshop_stock = new Stock();
            lost_bookshop_stock.setStockID(generateID.generateStockId());
            lost_bookshop.setStock(lost_bookshop_stock);

            lost_bookshop_stock.setBook(lost_bookshop);
            lost_bookshop_stock.setQuantity(100);
            lost_bookshop_stock.setImportPrice(lost_bookshop_importPrice);
            stockService.save(lost_bookshop_stock);
            bookService.merge(lost_bookshop);
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
    
    // COMMENT END

}
