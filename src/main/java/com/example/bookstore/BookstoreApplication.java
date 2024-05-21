package com.example.bookstore;

import com.example.bookstore.entity.*;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CategoryRepository;
import com.example.bookstore.repository.PublisherRepository;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.DiscountService;
import com.example.bookstore.service.UserService;
import com.example.bookstore.util.EmailSenderService;
import com.example.bookstore.util.GenerateID;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootApplication
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    // JUST USE TO TEST MY DISCOUNT
//    @Bean
//    public CommandLineRunner commandLineRunner(EmailSenderService emailSenderService, DiscountService discountService)
//    {
//        return runner -> {
//            Discount discount = new Discount();
//            discount.setDiscountAmount(10);
//            discount.setDiscountCode(discountService.createDiscountCode());
//            discountService.save(discount);
//        };
//    }

}
