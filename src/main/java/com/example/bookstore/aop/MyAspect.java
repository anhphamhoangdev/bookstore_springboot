package com.example.bookstore.aop;

import com.example.bookstore.entity.*;
import com.example.bookstore.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;


@Aspect
@Component
public class MyAspect {

    private UserService userService;
    private FavoriteService favoriteService;
    private CategoryService categoryService;
    private AuthorService authorService;
    private CartService cartService;
    private BookService bookService;

    @Autowired
    public MyAspect(AuthorService authorService, BookService bookService, CategoryService categoryService, FavoriteService favoriteService, UserService userService, CartService cartService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.favoriteService = favoriteService;
        this.userService = userService;
        this.cartService = cartService;
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
            session.setAttribute("favorite", favorite);
            Cart cart = cartService.findByUserId(user.getUserID());
            session.setAttribute("cart", cart);
        }
        Object result = joinPoint.proceed();
        return result;
    }

    @Around("execution(* com.example.bookstore.controller.FavoriteController.*(..))"
            + " || execution(* com.example.bookstore.controller.CartController.*(..))")
    public Object checkAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        // Ví dụ: lấy thông tin người dùng từ session
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession();

        if(session != null && session.getAttribute("SPRING_SECURITY_CONTEXT") == null)
        {
            throw new AuthenticationException("User is not authenticated") {};
        }

        // Nếu người dùng đã đăng nhập, tiếp tục chạy aspect processRequest
        return joinPoint.proceed();
    }


}