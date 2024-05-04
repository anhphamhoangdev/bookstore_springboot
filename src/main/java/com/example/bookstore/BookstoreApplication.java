package com.example.bookstore;

import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Category;
import com.example.bookstore.entity.Publisher;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CategoryRepository;
import com.example.bookstore.repository.PublisherRepository;
import com.example.bookstore.service.BookService;
import com.example.bookstore.util.GenerateID;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(AuthorRepository authorRepository, BookService bookService, CategoryRepository categoryRepository, PublisherRepository publisherRepository, GenerateID generateID, EntityManager entityManager)
//    {
//        return runner -> {
//            Transactional transactional;
//            transactional.sta
//            List<Book> bookList = bookService.findAll();
//            List<Author> authors = bookList.get(0).getAuthors();
//            //            Author author = new Author();
////            author.setAuthorID(generateID.generateAuthorId());
////            author.setAuthorName("Ronaldo");
////            authorRepository.save(author);
////
////            Category category = new Category();
////            category.setCategoryID(generateID.generateCategoryId());
////            category.setCategoryName("Science");
////            categoryRepository.save(category);
//
////            Publisher publisher = new Publisher();
////            publisher.setPublisherID(generateID.generatePublisherId());
////            publisher.setPublisherName("KIM DONG");
////            publisherRepository.save(publisher);
//
//            /*
//            <th scope="col">ID</th>
//        <th scope="col"> </th>
//        <th scope="col">Name</th>
//        <th scope="col">Price</th>
//        <th scope="col">Author</th>
//        <th scope="col">Category</th>
//        <th scope="col">Publisher</th>
//             */
//
//
//
//
//
//
//        };
//    }

}
