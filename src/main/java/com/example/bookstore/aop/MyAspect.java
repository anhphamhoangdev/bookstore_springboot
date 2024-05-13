package com.example.bookstore.aop;

import com.example.bookstore.entity.*;
import com.example.bookstore.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.List;


@Aspect
@Component
public class MyAspect {

    private UserService userService;
    private FavoriteService favoriteService;
    private CategoryService categoryService;
    private AuthorService authorService;
    private BookService bookService;

    @Autowired
    public MyAspect(AuthorService authorService, BookService bookService, CategoryService categoryService, FavoriteService favoriteService, UserService userService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.favoriteService = favoriteService;
        this.userService = userService;
    }




    @Around("execution(* com.example.bookstore.controller.*.*(..))")
    public Object processRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false);

        if(session != null) {
            List<Book> bookList = (List<Book>) session.getAttribute("bookList");
            List<Category> categories = (List<Category>) session.getAttribute("categories");
            List<Author> authors= (List<Author>) session.getAttribute("authors");
            if(bookList == null) {
                bookList = bookService.findAll();
            }
            if(categories == null) {
                categories = categoryService.findALl();
            }
            if(authors == null) {
                authors = authorService.findALl();
            }
            session.setAttribute("books", bookList);
            session.setAttribute("categories", categories);
            session.setAttribute("authors", authors);
        }

        if (session != null && session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            session.setAttribute("user", user);
            Favorite favorite = favoriteService.findByUserId(user.getUserID());
            if (favorite == null) {
                favorite = new Favorite();
                favorite.setUserID(user.getUserID());
                favoriteService.save(favorite);
            }
            session.setAttribute("favorite", favorite);
        }
        Object result = joinPoint.proceed();
        return result;
    }
}